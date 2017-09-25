package io.apisense.embed.influx.configuration;

public class UnknownArchitectureException extends RuntimeException {
    UnknownArchitectureException(String arch) {
        super("Unable to find architecture: " + arch);
    }
}
