package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.VersionConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Default implementation of a {@link BinaryDownloader}.
 */
public class InfluxBinaryDownloader implements BinaryDownloader {
    private static final Logger logger = LoggerFactory.getLogger(InfluxBinaryDownloader.class.getName());
    private static final String INFLUX_BIN_LOCATION = "usr" + File.separator + "bin" + File.separator + "influxd";

    private final File outputDir;
    private final UrlGenerator urlGenerator;
    private final OkHttpClient okHttpClient;
    private ArchiveExtractor archiveExtractor;

    public InfluxBinaryDownloader(File outputDir) {
        this(outputDir, new InfluxUrlGenerator(), new OkHttpClient(), new InfluxArchiveExtractor());
    }

    InfluxBinaryDownloader(File outputDir, UrlGenerator urlGenerator,
                           OkHttpClient okHttpClient, ArchiveExtractor archiveExtractor) {
        this.outputDir = outputDir;
        this.urlGenerator = urlGenerator;
        this.okHttpClient = okHttpClient;
        this.archiveExtractor = archiveExtractor;
    }

    @Override
    public File download(VersionConfiguration configuration) throws IOException {
        ensureDirectory(outputDir);

        File targetPath = urlGenerator.buildTarget(outputDir, configuration);
        if (!targetPath.exists()) {
            downloadAndExtractServer(configuration, targetPath);
        }

        return new File(targetPath, INFLUX_BIN_LOCATION);
    }

    /**
     * Ensure that the given {@link File} is a directory, by creating it if not already existing.
     *
     * @param toCheck The directory to check.
     * @throws IOException If the given {@link File} is an already existing file.
     */
    private static void ensureDirectory(File toCheck) throws IOException {
        if (!toCheck.exists()) {
            logger.debug("Creating server cache directory: " + toCheck.getAbsolutePath());
            if (!(toCheck.mkdirs())) {
                logger.warn("Unable to create directory: " + toCheck.getAbsolutePath());
            }
        } else {
            if (!toCheck.isDirectory()) {
                throw new IOException("Not a directory: " + toCheck);
            }
        }
    }

    /**
     * Download the required InfluxDB archive and extract it into the given targetPath.
     *
     * @param configuration The version to download.
     * @param targetPath    The extraction target.
     * @throws IOException If anything goes wrong during download or extraction.
     */
    private void downloadAndExtractServer(VersionConfiguration configuration, File targetPath) throws IOException {
        logger.debug("Server is not locally available (" + configuration.os
                + "-" + configuration.architecture + "-" + configuration.version.dlPath + "), downloading...");
        URL sourceUrl = urlGenerator.buildSource(configuration);
        File archive = downloadVersion(sourceUrl);
        archiveExtractor.extract(configuration.os.archiveType, archive, targetPath);
        if (!(archive.delete())) {
            logger.warn("Unable to remove archive file: " + archive.getAbsolutePath());
        }
    }

    /**
     * Download the given source to a temporary file.
     *
     * @param sourceUrl The source to download
     * @return The output file.
     * @throws IOException If anything goes wrong during download or writing.
     */
    private File downloadVersion(URL sourceUrl) throws IOException {
        File outputFile = File.createTempFile("influxDB-archive", Long.toString(System.nanoTime()));
        logger.debug("Downloading Influx binaries into: " + outputFile.getAbsolutePath());

        Request request = new Request.Builder().url(sourceUrl).get().build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            writeFile(response.body().byteStream(), outputFile);
        } else {
            logger.error("Unable to download influx server (" + response.code() + ")");
            throw new IOException("Error on download: " + response.code());
        }

        return outputFile;
    }

    /**
     * Actual writing of the input stream to the output file.
     *
     * @param input      The stream to read from.
     * @param outputFile The file to write into.
     * @throws IOException If anything goes wrong during writing.
     */
    private void writeFile(InputStream input, File outputFile) throws IOException {
        byte[] buffer = new byte[4096];
        int n;
        OutputStream output = new FileOutputStream(outputFile);
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        output.close();
    }
}
