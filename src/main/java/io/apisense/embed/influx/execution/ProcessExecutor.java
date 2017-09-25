package io.apisense.embed.influx.execution;

import java.io.File;
import java.io.IOException;

/**
 * Interface of a stateful InfluxDB execution service.
 */
public interface ProcessExecutor {

    /**
     * Start a new influx process if not already started.
     *
     * @param binary Binary file to use.
     * @throws IOException if the execution fails.
     */
    void startProcess(File binary) throws IOException;

    /**
     * Start a new influx process if not already started.
     *
     * @param binary        Binary file to use.
     * @param configuration Influx configuration file to load.
     * @throws IOException if the execution fails.
     */
    void startProcess(File binary, File configuration) throws IOException;

    /**
     * Stop the started influx process, if any.
     */
    void stopProcess();
}
