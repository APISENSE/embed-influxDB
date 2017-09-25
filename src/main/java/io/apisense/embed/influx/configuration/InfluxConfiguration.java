package io.apisense.embed.influx.configuration;

import com.moandjiezana.toml.TomlWriter;
import io.apisense.embed.influx.download.InfluxBinaryDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InfluxConfiguration implements ConfigurationWriter {
    private static final Logger logger = LoggerFactory.getLogger(InfluxBinaryDownloader.class.getName());
    private final Map<String, Object> configMap;
    private final TomlWriter tomlWriter;

    public InfluxConfiguration(int port) {
        this(port, new TomlWriter());
    }

    InfluxConfiguration(int port, TomlWriter writer) {
        configMap = new HashMap<>();
        configMap.put("bind-address", "127.0.0.1:" + port);
        tomlWriter = writer;
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
