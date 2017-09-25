package io.apisense.embed.influx.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ConfigurationWriter {

    /**
     * Add configuration statements.
     *
     * @param config Statements to import in the Influx configuration.
     * @see <a href="https://docs.influxdata.com/influxdb/v1.3/administration/config">configuration elements</a>
     * @see <a href="https://github.com/mwanji/toml4j#user-content-converting-objects-to-toml">How to format the input {@link Map}</a>
     */
    void addStatements(Map<String, Object> config);

    /**
     * Write a file with the content of the Toml configuration, then returns it.
     *
     * @return The created file.
     * @throws IOException If the file writing goes wrong.
     */
    File writeFile() throws IOException;
}
