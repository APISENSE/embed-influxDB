package io.apisense.embed.influx.configuration;

import com.moandjiezana.toml.TomlWriter;
import io.apisense.embed.influx.download.InfluxBinaryDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of a {@link ConfigurationWriter}.
 */
public class InfluxConfigurationWriter implements ConfigurationWriter {
    private static final Logger logger = LoggerFactory.getLogger(InfluxBinaryDownloader.class.getName());
    public static final String META_SECTION = "meta";
    public static final String DATA_SECTION = "data";
    public static final String HTTP_SECTION = "http";

    private final Map<String, Object> configMap;
    private final TomlWriter tomlWriter;

    public InfluxConfigurationWriter(int port) {
        this(port, new TomlWriter());
    }

    InfluxConfigurationWriter(int port, TomlWriter writer) {
        configMap = new HashMap<>();
        configMap.put(HTTP_SECTION, defaultHttpSection(port));
        tomlWriter = writer;
    }

    @Override
    public void setDataPath(File dataPath) {
        configMap.put(META_SECTION, defaultMetaSection(dataPath));
        configMap.put(DATA_SECTION, defaultDataSection(dataPath));
    }

    private static Map<String, String> defaultHttpSection(int port) {
        HashMap<String, String> meta = new HashMap<>();
        meta.put("bind-address", ":" + port);
        return meta;
    }

    private static Map<String, String> defaultMetaSection(File dataPath) {
        HashMap<String, String> meta = new HashMap<>();
        meta.put("dir", dataPath.getAbsolutePath() + File.separator + "meta");
        return meta;
    }

    private static Map<String, String> defaultDataSection(File dataPath) {
        HashMap<String, String> meta = new HashMap<>();
        meta.put("dir", dataPath.getAbsolutePath() + File.separator + "data");
        meta.put("wal-dir", dataPath.getAbsolutePath() + File.separator + "wal-dir");
        return meta;
    }

    @Override
    public void addStatements(Map<String, Object> config) {
        for (String key : config.keySet()) {
            this.configMap.put(key, config.get(key));
        }
    }

    @Override
    public File writeFile() throws IOException {
        File configFile = File.createTempFile("influxdb.config", Long.toString(System.nanoTime()));
        logger.debug("Writing configuration file into: " + configFile.getAbsolutePath());
        tomlWriter.write(configMap, configFile);
        return configFile;
    }
}
