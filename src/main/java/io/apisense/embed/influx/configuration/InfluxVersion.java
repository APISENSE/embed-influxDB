package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.IVersion;

/**
 * Define available InfluxDB version.
 */
public enum InfluxVersion implements IVersion {
    PRODUCTION("2.5.1", ReleaseDir.RELEASE),
    V2_5_1("2.5.1", ReleaseDir.RELEASE),
    V1_8_10("1.8.10", ReleaseDir.RELEASE),
    V1_7_6("1.7.6", ReleaseDir.RELEASE),
    V1_5_3("1.5.3", ReleaseDir.RELEASE),
    V1_3_5("1.3.5", ReleaseDir.RELEASE),
    NIGHTLY("nightly", ReleaseDir.NIGHTLY);

    public final String dlPath;
    public final String directory;

    InfluxVersion(String dlPath, String directory) {
        this.dlPath = dlPath;
        this.directory = directory;
    }

    @Override
    public String asInDownloadPath() {
        return dlPath;
    }

    /**
     * Define the right directory to query the binary package from
     * depending on the version release type.
     */
    private static class ReleaseDir {
        private ReleaseDir() {
            // Hide our constructor
        }

        static final String RELEASE = "releases";
        static final String NIGHTLY = "nightlies";
    }
}
