package io.apisense.embed.influx;

/**
 * Describe the available operations on an embedded database.
 */
public interface EmbeddedDB {

    /**
     * Check if the server binaries are present locally
     * and install them if missing.
     *
     * @throws ServerAlreadyRunningException If the server is already started.
     */
    void init() throws ServerAlreadyRunningException;

    /**
     * Install the server if not already present locally,
     * then start a new instance.
     *
     * @throws ServerAlreadyRunningException If the server is already started.
     */
    void start() throws ServerAlreadyRunningException;

    /**
     * Stop the started instance.
     *
     * @throws ServerNotRunningException If the server as not yet been started, or already been stopped.
     */
    void stop() throws ServerNotRunningException;

    /**
     * Stop the server if still running,
     * then remove the server data.
     *
     * @throws ServerNotRunningException If the server as not yet been started, or already been stopped.
     */
    void cleanup() throws ServerNotRunningException;

    /**
     * Retrive the current {@link ServerState}.
     *
     * @return The current {@link ServerState}.
     */
    ServerState getCurrentState();
}
