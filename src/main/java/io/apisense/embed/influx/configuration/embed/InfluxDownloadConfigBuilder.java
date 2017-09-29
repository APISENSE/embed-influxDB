package io.apisense.embed.influx.configuration.embed;

import de.flapdoodle.embed.process.config.store.DownloadConfigBuilder;
import de.flapdoodle.embed.process.config.store.IDownloadConfig;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.io.directories.UserHome;
import de.flapdoodle.embed.process.io.progress.StandardConsoleProgressListener;
import io.apisense.embed.influx.download.InfluxUrlGenerator;
import io.apisense.embed.influx.download.embed.PackageResolver;

import java.io.File;

/**
 * Builder of a {@link IDownloadConfig} to use with InfluxDB.
 */
public class InfluxDownloadConfigBuilder {
    private static final String DEFAULT_PATH = ".embedded-influx";
    public final DownloadConfigBuilder builder;

    /**
     * Create a new {@link DownloadConfigBuilder} to use with InfluxDB.
     *
     * @param storePath The file to store artifacts into.
     *                  Default to ~/.embedded-influx if null.
     */
    public InfluxDownloadConfigBuilder(File storePath) {
        this(storePath != null ? new FixedPath(storePath.getAbsolutePath()) : new UserHome(DEFAULT_PATH));
    }

    private InfluxDownloadConfigBuilder(IDirectory storePath) {
        this.builder = downloadConfigBuilder(storePath);
    }

    private static DownloadConfigBuilder downloadConfigBuilder(IDirectory storePath) {
        return new DownloadConfigBuilder()
                .artifactStorePath(storePath)
                .fileNaming(new UUIDTempNaming())
                .downloadPath(InfluxUrlGenerator.HOSTNAME)
                .progressListener(new StandardConsoleProgressListener())
                .downloadPrefix("embedded-influx-download")
                .packageResolver(new PackageResolver())
                .userAgent("Mozilla/5.0 (compatible; Embedded Influx; +https://github.com/APISENSE/embed-influxDB)");
    }

    public IDownloadConfig getConfig() {
        return builder.build();
    }
}
