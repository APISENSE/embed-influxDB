package io.apisense.embed.influx.configuration;

/**
 * Define available InfluxDB version.
 */
public enum InfluxVersion implements InfluxIVersion {
    PRODUCTION("2.5.1", ReleaseType.RELEASE),
    V2_5_1("2.5.1", ReleaseType.RELEASE),
    V1_8_10("1.8.10", ReleaseType.RELEASE),
    V1_7_6("1.7.6", ReleaseType.RELEASE),
    V1_5_3("1.5.3", ReleaseType.RELEASE),
    V1_3_5("1.3.5", ReleaseType.RELEASE),
    NIGHTLY("nightly", ReleaseType.NIGHTLY);

    private final String dlPath;
    private final ReleaseType releaseType;

    InfluxVersion(String dlPath, ReleaseType releaseType) {
        this.dlPath = dlPath;
        this.releaseType = releaseType;
    }

    @Override
    public String asInDownloadPath() {
        return this.dlPath;
    }

    @Override
    public ReleaseType getReleaseType() {
        return this.releaseType;
    }

}
