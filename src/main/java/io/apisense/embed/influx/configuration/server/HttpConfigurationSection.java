package io.apisense.embed.influx.configuration.server;

public class HttpConfigurationSection extends AbstractConfigurationSection {
    public HttpConfigurationSection(int port) {
        super("http");
        addProperty(ConfigurationProperty.BIND_ADDRESS, ":" + port);
    }
}
