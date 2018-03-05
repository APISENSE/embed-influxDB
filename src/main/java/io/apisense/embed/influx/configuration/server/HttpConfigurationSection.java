package io.apisense.embed.influx.configuration.server;

public class HttpConfigurationSection extends AbstractConfigurationSection {
    public HttpConfigurationSection(int port, boolean auth) {
        super("http");
        addProperty(ConfigurationProperty.BIND_ADDRESS, ":" + port);
        if (auth) {
            addProperty(ConfigurationProperty.AUTH_ENABLED, true);
        }
    }
}
