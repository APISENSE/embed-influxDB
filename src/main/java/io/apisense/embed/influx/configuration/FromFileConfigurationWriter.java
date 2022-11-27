package io.apisense.embed.influx.configuration;

import java.io.File;
import java.util.Map;

/**
 * Implementation of a {@link ConfigurationWriter} to use an existing configuration file.
 */
public final class FromFileConfigurationWriter implements ConfigurationWriter {
    private final File configurationFile;
    private final File dataPath;

    FromFileConfigurationWriter(File configurationFile, File dataPath) {
        this.configurationFile = configurationFile;
        this.dataPath = dataPath;
    }

    @Override
    public File getDataPath() {
        return dataPath;
    }

    @Override
    public void addStatements(Map<String, Object> config) {
        throw new UnsupportedOperationException("The given configuration file is immutable");
    }

    @Override
    public File writeFile() {
        return configurationFile;
    }

    /**
     * Build an {@link FromFileConfigurationWriter}.
     */
    public static final class Builder {
        private final File configuration;
        private File dataPath = null;

        public Builder(File configurationFile) {
            configuration = configurationFile;
        }

        public FromFileConfigurationWriter build() {
            return new FromFileConfigurationWriter(configuration, dataPath);
        }

        /**
         * Set the path to remove on server stop,
         * nothing will be removed if not specified.
         * <p>
         * This can be used to remove the data between each database usage.
         *
         * @param dataPath - path to data location
         * @return The current {@link Builder}.
         */
        public Builder removePathOnStop(File dataPath) {
            this.dataPath = dataPath;
            return this;
        }

    }
}
