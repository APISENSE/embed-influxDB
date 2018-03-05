package io.apisense.embed.influx.configuration;

import com.moandjiezana.toml.TomlWriter;
import io.apisense.embed.influx.configuration.server.ConfigurationSection;
import io.apisense.embed.influx.configuration.server.HttpConfigurationSection;
import io.apisense.embed.influx.configuration.server.MetaConfigurationSection;
import io.apisense.embed.influx.configuration.server.UdpConfigurationSection;
import io.apisense.embed.influx.configuration.server.ConfigurationProperty;
import io.apisense.embed.influx.configuration.server.HeadConfigurationSection;
import io.apisense.embed.influx.configuration.server.DataConfigurationSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of a {@link ConfigurationWriter}.
 */
public final class InfluxConfigurationWriter implements ConfigurationWriter {
    private static final Logger logger = LoggerFactory.getLogger(InfluxConfigurationWriter.class.getName());

    private final Map<String, Object> configMap;
    private final TomlWriter tomlWriter;
    private File dataPath;

    InfluxConfigurationWriter(Set<ConfigurationSection> configurationSections, File dataPath, TomlWriter writer) {
        this.dataPath = dataPath;
        this.tomlWriter = writer;
        this.configMap = inflateConfigurationMap(configurationSections);
    }

    /**
     * Build a configuration map in order to inflate a Toml configuration file.
     * The provided section with a null name will be added before others in order to fall under no section.
     *
     * @param configurationSections The set of sections to add to our configuration map.
     * @return The inflated {@link Map}.
     */
    private static Map<String, Object> inflateConfigurationMap(Set<ConfigurationSection> configurationSections) {
        Map<String, Object> configMap = new LinkedHashMap<>(); // to add some predictable order when creating config file

        // We are looping a first time to find every section without name,
        // For those sections we put all the defined properties as is in the configuration file.
        // We have to do it before to not fall under an existing section.
        for (ConfigurationSection configurationSection : configurationSections) {
            if (configurationSection.getName() == null) {
                addConfigProperties(configurationSection, configMap);
            }
        }

        // We can then process all the named sections.
        for (ConfigurationSection configurationSection : configurationSections) {
            if (configurationSection.getName() != null) {
                configMap.put(configurationSection.getName(), configurationSection.getConfiguration());
            }
        }

        return configMap;
    }

    /**
     * Break down the given section to add its properties one by one.
     *
     * @param configurationSection The section to add.
     * @param configMap            The map to add the section into.
     */
    private static void addConfigProperties(ConfigurationSection configurationSection, Map<String, Object> configMap) {
        Map<ConfigurationProperty, Object> configuration = configurationSection.getConfiguration();
        for (ConfigurationProperty configurationProperty : configuration.keySet()) {
            configMap.put(configurationProperty.toString(), configuration.get(configurationProperty));
        }
    }

    @Override
    public File getDataPath() {
        return dataPath;
    }

    @Override
    public void addStatements(Map<String, Object> config) {
        this.configMap.putAll(config);
    }

    @Override
    public File writeFile() throws IOException {
        File configFile = File.createTempFile("influxdb.config", Long.toString(System.nanoTime()));
        logger.debug("Writing configuration file into: " + configFile.getAbsolutePath());
        tomlWriter.write(configMap, configFile);
        return configFile;
    }

    /**
     * Build an {@link InfluxConfigurationWriter}.
     */
    public static final class Builder {
        private Set<ConfigurationSection> configuration;
        private TomlWriter writer = new TomlWriter();
        private File dataPath = null;

        public Builder() {
            configuration = new LinkedHashSet<>();

            // Default configuration
            setBackupAndRestorePort(8088);
            setHttp(8086);
        }

        public InfluxConfigurationWriter build() {
            if (dataPath == null) {
                try {
                    File tempFile = Files
                            .createTempDirectory("embed-influx-data-" + Long.toString(System.nanoTime()))
                            .toFile();
                    setDataPath(tempFile);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to create default data path", e);
                }
            }
            return new InfluxConfigurationWriter(configuration, dataPath, writer);
        }

        /**
         * Add HTTP section with defined port
         *
         * @param httpPort - port number
         * @return The current {@link Builder}.
         */
        public Builder setHttp(int httpPort) {
            addSection(new HttpConfigurationSection(httpPort, false));
            return this;
        }

        /**
         * Add HTTP section with defined port and auth enabled
         *
         * @param httpPort - port number
         * @param auth - flag to enable/disable auth
         * @return The current {@link Builder}.
         */
        public Builder setHttp(int httpPort, boolean auth) {
            addSection(new HttpConfigurationSection(httpPort, auth));
            return this;
        }

        /**
         * Add BackupAndRestore section with defined port
         *
         * @param backupAndRestorePort - port number
         * @return The current {@link Builder}.
         */
        public Builder setBackupAndRestorePort(int backupAndRestorePort) {
            addSection(new HeadConfigurationSection(backupAndRestorePort));
            return this;
        }

        /**
         * Add UDP section with defined port
         *
         * @param udpPort - port number
         * @return The current {@link Builder}.
         */
        public Builder setUdp(int udpPort) {
            addSection(new UdpConfigurationSection(udpPort, "udp"));
            return this;
        }

        /**
         * Add HTTP section with defined port and auth enabled
         *
         * @param udpPort - port number
         * @param database - name for UDP database
         * @return The current {@link Builder}.
         */
        public Builder setUdp(int udpPort, String database) {
            addSection(new UdpConfigurationSection(udpPort, database));
            return this;
        }

        /**
         * Add data and meta sections.
         *
         * @param dataPath - path to data location
         * @return The current {@link Builder}.
         */
        public Builder setDataPath(File dataPath) {
            this.dataPath = dataPath;
            addSection(new MetaConfigurationSection(dataPath));
            addSection(new DataConfigurationSection(dataPath));
            return this;
        }

        /**
         * Add a customized section to the current configuration.
         *
         * @param section The {@link ConfigurationSection} to add.
         * @return The current {@link Builder}.
         */
        public Builder addSection(ConfigurationSection section) {
            configuration.remove(section);
            configuration.add(section);
            return this;
        }

        public Builder setWriter(TomlWriter writer) {
            this.writer = writer;
            return this;
        }
    }
}
