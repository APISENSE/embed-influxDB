package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.IVersion;

/**
 * Description of the InfluxDB version to fetch.
 */
public class VersionConfiguration extends Distribution {
    private final OSType os;
    private final OSArchitecture architecture;
    private final InfluxIVersion version;

    public VersionConfiguration(OSType os, OSArchitecture architecture, InfluxIVersion version) {
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

    public OSType getOs() {
        return os;
    }

    public OSArchitecture getArchitecture() {
        return architecture;
    }

    @Override
    public InfluxIVersion getVersion() {
        return version;
    }
}
