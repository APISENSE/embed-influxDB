package io.apisense.embed.influx.configuration;

public class VersionConfiguration {
    public final OSType os;
    public final OSArchitecture architecture;
    public final InfluxVersion version;

    public VersionConfiguration(OSType os, OSArchitecture architecture, InfluxVersion version) {
        this.os = os;
        this.architecture = architecture;
        this.version = version;
    }

    public static VersionConfiguration fromRuntime(InfluxVersion version) {
        return new VersionConfiguration(OSType.getCurrent(), OSArchitecture.getCurrent(), version);
    }
}
