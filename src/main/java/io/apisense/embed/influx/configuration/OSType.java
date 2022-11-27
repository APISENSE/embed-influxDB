package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.Platform;
import io.apisense.embed.influx.download.InfluxArchiveType;

/**
 * Available platforms for InfluxDB associated with their available archive {@link InfluxArchiveType}.
 */
public enum OSType {
    Linux("linux", InfluxArchiveType.TGZ, Platform.Linux),
    Windows("windows", InfluxArchiveType.ZIP, Platform.Windows),
    Mac("darwin", InfluxArchiveType.TGZ, Platform.OS_X);

    private final String dlPath;
    private final InfluxArchiveType archiveType;
    private final Platform platform;

    OSType(String dlPath, InfluxArchiveType archiveType, Platform platform) {
        this.dlPath = dlPath;
        this.archiveType = archiveType;
        this.platform = platform;
    }

    /**
     * Return the {@link OSType} for the current system.
     *
     * @return The value of the currently running {@link OSType}.
     */
    public static OSType getCurrent() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            return OSType.Windows;
        }
        if (os.contains("mac")) {
            return OSType.Mac;
        }
        return OSType.Linux;
    }

    public Platform toPlatform() {
        return platform;
    }

    public String getDlPath() {
        return dlPath;
    }

    public InfluxArchiveType getArchiveType() {
        return archiveType;
    }
}
