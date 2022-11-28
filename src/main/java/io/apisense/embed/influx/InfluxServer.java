package io.apisense.embed.influx;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.store.IDownloadConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.store.IArtifactStore;
import io.apisense.embed.influx.configuration.ConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.configuration.embed.InfluxArtifactStoreBuilder;
import io.apisense.embed.influx.configuration.embed.InfluxDownloadConfigBuilder;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;
import io.apisense.embed.influx.configuration.embed.InfluxRuntimeConfigBuilder;
import io.apisense.embed.influx.execution.InfluxExecutor;
import io.apisense.embed.influx.execution.embed.InfluxProcess;
import io.apisense.embed.influx.execution.embed.InfluxServerStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of the InfluxDB embedded server controller.
 */
public class InfluxServer implements EmbeddedDB {
    private static final Logger logger = LoggerFactory.getLogger(InfluxServer.class.getName());

    private ServerState currentState = ServerState.UNKNOWN;

    private final InfluxExecutor executor;

    private InfluxServer(InfluxServerStarter influxServerStarter, InfluxExecutableConfig executableConfig,
                         Distribution versionConfiguration) {
        this(new InfluxExecutor(influxServerStarter, executableConfig, versionConfiguration));
    }

    InfluxServer(InfluxExecutor executor) {
        this.executor = executor;
    }


    @Override
    public void init() throws ServerAlreadyRunningException {
        if (currentState == ServerState.STARTED) {
            throw new ServerAlreadyRunningException(this);
        }

        logger.info("Initializing server: " + this.toString());
        executor.prepare();

        this.currentState = ServerState.READY;
    }

    @Override
    public void start() throws ServerAlreadyRunningException {
        if (currentState == ServerState.STARTED) {
            throw new ServerAlreadyRunningException(this);
        }
        if (currentState != ServerState.READY && currentState != ServerState.STOPPED) {
            init();
        }

        logger.info("Starting server: " + this.toString());
        try {
            executor.start();
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
        executor.stop();
        currentState = ServerState.STOPPED;
    }

    @Override
    public void cleanup() throws ServerNotRunningException {
        if (currentState != ServerState.STOPPED) {
            stop();
        }

        logger.info("Removing data for server: " + this.toString());
        executor.cleanup();

        currentState = ServerState.CLEAN;
    }

    public static class Builder {
        private ConfigurationWriter configurationWriter;
        private VersionConfiguration versionConfig;
        private File storePath;
        private File extractionPath;

        public InfluxServer build() throws IOException {
            if (versionConfig == null) {
                setVersionConfig(VersionConfiguration.fromRuntime(InfluxVersion.PRODUCTION));
            }

            IRuntimeConfig runtimeConfig = buildRuntimeConfig();

            InfluxServerStarter influxServerStarter = new InfluxServerStarter(runtimeConfig);
            InfluxExecutableConfig executionConfig = new InfluxExecutableConfig(versionConfig.getVersion(), configurationWriter);

            return new InfluxServer(influxServerStarter, executionConfig, versionConfig);
        }

        private IRuntimeConfig buildRuntimeConfig() {
            IDownloadConfig downloadConfig = new InfluxDownloadConfigBuilder(storePath).getConfig();
            IArtifactStore store = new InfluxArtifactStoreBuilder(downloadConfig, extractionPath).getConfig();
            return new InfluxRuntimeConfigBuilder(store).getConfig();
        }

        /**
         * Configure the version of InfluxDB to use.
         * <p>
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
         * Set the location of the stored InfluxDB binaries directory.
         *
         * @param storePath Tge directory in which binaries should be stored.
         * @return The current {@link Builder}.
         * @throws IOException If the given {@link File} is not a directory.
         */
        public Builder setStorePath(File storePath) throws IOException {
            if (!storePath.isDirectory()) {
                throw new IOException("Not a directory: " + storePath.getAbsolutePath());
            }
            this.storePath = storePath;
            return this;
        }

        /**
         * Set the location of the extracted InfluxDB binaries directory.
         *
         * @param extractionPath The directory extraction should occur.
         * @return The current {@link Builder}.
         * @throws IOException If the given {@link File} is not a directory.
         */
        public Builder setExtractionPath(File extractionPath) throws IOException {
            if (!extractionPath.isDirectory()) {
                throw new IOException("Not a directory: " + extractionPath.getAbsolutePath());
            }
            this.extractionPath = extractionPath;
            return this;
        }

        /**
         * Customize Influx behavior by changing its configuration.
         *
         * Default: A default configuration.
         *
         * @param configurationWriter The configuration to set.
         * @return The current {@link Builder}.
         */
        public Builder setInfluxConfiguration(ConfigurationWriter configurationWriter) {
            this.configurationWriter = configurationWriter;
            return this;
        }
    }

    /**
     * Returns the internal {@link InfluxProcess} created upon startup.
     * <p>
     * You can use this object to interact with the actual process,
     * but please note that doing might tamper with the library behavior.
     *
     * @return The {@link InfluxProcess} if the server has been started, null otherwise.
     */
    public InfluxProcess getProcess() {
        return executor.getInfluxProcess();
    }

    @Override
    public ServerState getCurrentState() {
        return currentState;
    }
}
