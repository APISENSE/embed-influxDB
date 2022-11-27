package io.apisense.embed.influx.configuration;

/**
 * Define the right directory to query the binary package from
 * depending on the version release type.
 */
public enum ReleaseType {
    RELEASE("releases"),
    NIGHTLY("nightlies");

    private final String directory;

    ReleaseType(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
