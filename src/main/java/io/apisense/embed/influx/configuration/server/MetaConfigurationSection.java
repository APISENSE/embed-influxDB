package io.apisense.embed.influx.configuration.server;

import java.io.File;

public class MetaConfigurationSection extends AbstractConfigurationSection {
    public MetaConfigurationSection(File dataPath) {
        super("meta");
        addProperty(ConfigurationProperty.DIR,
                dataPath.getAbsolutePath() + File.separator + getName());
    }
}
