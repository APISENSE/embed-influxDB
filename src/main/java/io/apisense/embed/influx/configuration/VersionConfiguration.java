package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.Distribution;

/**
 * Description of the InfluxDB version to fetch.
 */
public class VersionConfiguration extends Distribution {
    public final OSType os;
    public final OSArchitecture architecture;
    public final InfluxVersion version;

    public VersionConfiguration(OSType os, OSArchitecture architecture, InfluxVersion version) {
        super(version, os.toPlatform(), architecture.toBitSize());
        this.os = os;
        this.architecture = architecture;
        this.version = version;
    }

    /**
     * Use the runtime environment to guess the currently running OS and architecture.
     *
     * @param version The version of InfluxDB to retrieve.
     * @return The filled {@link VersionConfiguration}.
     */
    public static VersionConfiguration fromRuntime(InfluxVersion version) {
        return new VersionConfiguration(OSType.getCurrent(), OSArchitecture.getCurrent(), version);
    }
}
