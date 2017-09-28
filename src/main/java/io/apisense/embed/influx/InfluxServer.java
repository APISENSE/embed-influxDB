package io.apisense.embed.influx;

import io.apisense.embed.influx.configuration.ConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.download.BinaryDownloader;
import io.apisense.embed.influx.download.InfluxBinaryDownloader;
import io.apisense.embed.influx.execution.InfluxExecutor;
import io.apisense.embed.influx.execution.ProcessExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of the InfluxDB embedded server controller.
 */
public class InfluxServer implements EmbeddedDB {
    private static final Logger logger = LoggerFactory.getLogger(InfluxServer.class.getName());

    private final File dataPath;
    private final VersionConfiguration versionConfig;
    private final BinaryDownloader downloader;
    private final ConfigurationWriter influxConfigurationWriter;
    private final ProcessExecutor executor;


    private File serverBinary = null;
    private ServerState currentState = ServerState.UNKNOWN;
    private File serverConfig = null;

    private InfluxServer(File dataPath, VersionConfiguration versionConfig,
                         BinaryDownloader downloader, ConfigurationWriter influxConfigurationWriter,
                         ProcessExecutor executor) {
        this.influxConfigurationWriter = influxConfigurationWriter;
        influxConfigurationWriter.setDataPath(dataPath);
        this.versionConfig = versionConfig;
        this.downloader = downloader;
        this.dataPath = dataPath;
        this.executor = executor;
    }

    @Override
    public void init() throws ServerAlreadyRunningException {
        if (currentState == ServerState.STARTED) {
            throw new ServerAlreadyRunningException(this);
        }

        logger.info("Initializing server: " + this.toString());
        try {
            this.serverBinary = downloader.download(versionConfig);
        } catch (IOException e) {
            logger.error("Unable to download InfluxDB: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        this.currentState = ServerState.READY;
    }

    @Override
    public void start() throws ServerAlreadyRunningException {
        if (currentState == ServerState.STARTED) {
            throw new ServerAlreadyRunningException(this);
        }
        if (currentState != ServerState.READY) {
            init();
        }

        logger.info("Starting server: " + this.toString());
        try {
            serverConfig = influxConfigurationWriter.writeFile();
            executor.startProcess(serverBinary, serverConfig);
        } catch (IOException e) {
            logger.error("Unable to start server: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        currentState = ServerState.STARTED;
    }

    @Override
    public void stop() throws ServerNotRunningException {
        if (currentState != ServerState.STARTED) {
            throw new ServerNotRunningException(this);
        }

        logger.info("Stopping server: " + this.toString());
        executor.stopProcess();

        currentState = ServerState.STOPPED;
    }

    @Override
    public void cleanup() throws ServerNotRunningException {
        if (currentState != ServerState.STOPPED) {
            stop();
        }

        logger.info("Removing data for server: " + this.toString());
        if (!(dataPath.delete())) {
            logger.warn("Unable to remove data dir: " + dataPath.getAbsolutePath());
        }
        if (!(serverConfig.delete())) {
            logger.warn("Unable to remove config file: " + serverConfig.getAbsolutePath());
        }
        serverConfig = null;

        currentState = ServerState.CLEAN;
    }

    public static class Builder {
        private ConfigurationWriter influxConfiguration;
        private BinaryDownloader downloader;
        private File dataPath;
        private VersionConfiguration versionConfig;
        private ProcessExecutor executor;

        public InfluxServer build() throws IOException {
            if (dataPath == null) {
                setDataPath(createTempDir());
            }
            if (versionConfig == null) {
                setVersionConfig(VersionConfiguration.fromRuntime(InfluxVersion.PRODUCTION));
            }
            if (downloader == null) {
                File binaryCache = new File(System.getProperty("user.home") + File.separator + ".embedded-influx");
                setDownloader(new InfluxBinaryDownloader(binaryCache));
            }
            if (influxConfiguration == null) {
                setInfluxConfiguration(new InfluxConfigurationWriter(8086));
            }
            if (executor == null) {
                setExecutor(new InfluxExecutor());
            }
            return new InfluxServer(dataPath, versionConfig, downloader, influxConfiguration, executor);
        }

        private File createTempDir() throws IOException {
            File tempFile = File.createTempFile("embedded-influx", Long.toString(System.nanoTime()));
            if (!(tempFile.delete())) {
                throw new IOException("Could not delete temp file: " + tempFile.getAbsolutePath());
            }

            if (!(tempFile.mkdir())) {
                throw new IOException("Could not create temp directory: " + tempFile.getAbsolutePath());
            }
            return tempFile;
        }

        /**
         * Configure the version of InfluxDB to use.
         *
         * Default: The production server using runtime to discover OS and Architecture.
         *
         * @param versionConfig The configuration.
         * @return The current {@link Builder}.
         */
        public Builder setVersionConfig(VersionConfiguration versionConfig) {
            this.versionConfig = versionConfig;
            return this;
        }

        /**
         * Set the tool used for downloading InfluxDB.
         *
         * Default: Downloading into ~/.embedded-influx with {@link InfluxBinaryDownloader}
         *
         * @param downloader The directory in which the binary should be stored.
         * @return The current {@link Builder}.
         */
        public Builder setDownloader(BinaryDownloader downloader) {
            this.downloader = downloader;
            return this;
        }

        /**
         * Set the data containing folder.
         *
         * Default: A temporary folder prefixed with embedded-influx and suffixed with a nano seconds timestamp.
         *
         * @param dataPath The path in which the data will be stored.
         * @return The current {@link Builder}.
         * @throws IOException If the given file is not a directory.
         */
        public Builder setDataPath(File dataPath) throws IOException {
            if (!dataPath.isDirectory()) {
                throw new IOException("Not a directory: " + dataPath.getAbsolutePath());
            }
            this.dataPath = dataPath;
            return this;
        }

        /**
         * Customize Influx behavior by changing its configuration.
         *
         * Default: A default configuration.
         *
         * @param influxConfiguration The configuration to set.
         * @return The current {@link Builder}.
         */
        public Builder setInfluxConfiguration(ConfigurationWriter influxConfiguration) {
            this.influxConfiguration = influxConfiguration;
            return this;
        }

        /**
         * Set the {@link ProcessExecutor}.
         *
         * Default: An {@link io.apisense.embed.influx.execution.InfluxExecutor} instance.
         *
         * @param executor Executor to use.
         * @return The current {@link Builder}.
         */
        public Builder setExecutor(ProcessExecutor executor) {
            this.executor = executor;
            return this;
        }
    }
}
