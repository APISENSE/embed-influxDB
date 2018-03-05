package io.apisense.embed.influx.configuration.server;

import java.io.File;

public class DataConfigurationSection extends AbstractConfigurationSection {
    public DataConfigurationSection(File dataPath) {
        super("data");
        addProperty(ConfigurationProperty.DIR,
                dataPath.getAbsolutePath() + File.separator + getName());
        addProperty(ConfigurationProperty.WAL_DIR,
                dataPath.getAbsolutePath() + File.separator + "wal-dir");
    }
}
