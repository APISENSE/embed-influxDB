package io.apisense.embed.influx.configuration;

public enum InfluxVersion {
    PRODUCTION("1.3.5", "releases"),
    V1_3_5("1.3.5", "releases"),
    NIGHTLY("nightly", "nightlies");

    public final String dlPath;
    public final String directory;

    InfluxVersion(String dlPath, String directory) {
        this.dlPath = dlPath;
        this.directory = directory;
    }
}
