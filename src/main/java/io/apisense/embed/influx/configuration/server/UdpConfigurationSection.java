package io.apisense.embed.influx.configuration.server;

public class UdpConfigurationSection extends AbstractConfigurationSection {
    public UdpConfigurationSection(int port, String database) {
        super("udp");
        addProperty(ConfigurationProperty.ENABLED, true);
        addProperty(ConfigurationProperty.BIND_ADDRESS, ":" + port);
        addProperty(ConfigurationProperty.DATABASE, database);
    }
}
