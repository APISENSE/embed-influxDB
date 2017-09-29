package io.apisense.embed.influx.configuration.embed;

import de.flapdoodle.embed.process.config.IExecutableProcessConfig;
import de.flapdoodle.embed.process.config.ISupportConfig;
import de.flapdoodle.embed.process.distribution.IVersion;
import io.apisense.embed.influx.configuration.ConfigurationWriter;

public class InfluxExecutableConfig implements IExecutableProcessConfig {
    private final IVersion version;
    private final ConfigurationWriter configurationWriter;

    public InfluxExecutableConfig(IVersion version, ConfigurationWriter configurationWriter) {
        this.version = version;
        this.configurationWriter = configurationWriter;
    }

    /**
     * Return the current {@link ConfigurationWriter}, may be null.
     *
     * @return The set {@link ConfigurationWriter}.
     */
    public ConfigurationWriter configurationWriter() {
        return configurationWriter;
    }

    @Override
    public IVersion version() {
        return version;
    }

    @Override
    public ISupportConfig supportConfig() {
        return InfluxSupportConfig.instance;
    }
}
