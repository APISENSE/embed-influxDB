package io.apisense.embed.influx.configuration.server;

public class HeadConfigurationSection extends AbstractConfigurationSection {
    public HeadConfigurationSection(int backupAndRestorePort) {
        super(null); // This configuration shouldn't be under any section.
        addProperty(ConfigurationProperty.BIND_ADDRESS, ":" + backupAndRestorePort);
    }
}
