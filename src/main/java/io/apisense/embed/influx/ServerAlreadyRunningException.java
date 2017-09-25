package io.apisense.embed.influx;

public class ServerAlreadyRunningException extends Exception {
    public ServerAlreadyRunningException(InfluxServer server) {
        super("Server is already running on port: " + server.toString());
    }
}
