package io.apisense.embed.influx;

/**
 * Describe the available states of an {@link EmbeddedDB}.
 */
public enum ServerState {
    UNKNOWN, READY, STARTED, STOPPED, CLEAN
}
