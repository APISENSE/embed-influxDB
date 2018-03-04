package io.apisense.embed.influx.configuration.server;

/**
 * Defines all the available properties in an InfluxDB configuration file.
 */
public enum ConfigurationProperty {
    BIND_ADDRESS("bind-address"),
    DIR("dir"),
    WAL_DIR("wal-dir"),
    ENABLED("enabled"),
    DATABASE("database");

    private final String value;

    ConfigurationProperty(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
