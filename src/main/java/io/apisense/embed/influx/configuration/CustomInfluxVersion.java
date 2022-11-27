package io.apisense.embed.influx.configuration;

/**
 * Define a non-integrated InfluxDB version to download.
 */
public class CustomInfluxVersion implements InfluxIVersion {
    private final String versionNumber;
    private final ReleaseType releaseType;

    /**
     * Constructor for a specific version not integrated.
     *
     * @param versionNumber Version to download.
     * @param releaseType   Type of release for this version, can be fetched from {@link ReleaseType}.
     */
    public CustomInfluxVersion(String versionNumber, ReleaseType releaseType) {
        this.versionNumber = versionNumber;
        this.releaseType = releaseType;
    }

    /**
     * Constructor with version name = specific version
     *
     * @param versionNumber Version to download.
     */
    public CustomInfluxVersion(String versionNumber) {
        this(versionNumber, ReleaseType.RELEASE);
    }

    @Override
    public ReleaseType getReleaseType() {
        return this.releaseType;
    }

    @Override
    public String asInDownloadPath() {
        return this.versionNumber;
    }
}
