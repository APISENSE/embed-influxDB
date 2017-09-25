package io.apisense.embed.influx;

public class ServerNotRunningException extends Exception {
    public ServerNotRunningException(InfluxServer server) {
        super("Server is not running: " + server.toString());
    }
}
