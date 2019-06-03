package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.IVersion;

public enum InfluxVersion implements IVersion {
    PRODUCTION("1.7.6", "releases"),
    V1_7_6("1.7.6", "releases"),
    V1_3_5("1.3.5", "releases"),
    NIGHTLY("nightly", "nightlies");

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
}
