package io.apisense.embed.influx.execution;

import java.io.IOException;

/**
 * Interface of a server execution process.
 */
public interface EmbeddedExecutor {

    /**
     * Prepare the server for execution.
     */
    void prepare();

    /**
     * Start the underlying server process.
     *
     * @throws IOException If the execution goes wrong.
     */
    void start() throws IOException;

    /**
     * Stop the underlying server process
     */
    void stop();

    /**
     * Remove all server data and processes.
     */
    void cleanup();

}
