package io.apisense.embed.influx.configuration;

import io.apisense.embed.influx.download.ArchiveType;

/**
 * Available platforms for InfluxDB associated with their available archive {@link ArchiveType}.
 */
public enum OSType {
    Linux("linux", ArchiveType.TGZ),
    Windows("windows", ArchiveType.ZIP);

    public final String dlPath;
    public final ArchiveType archiveType;

    OSType(String dlPath, ArchiveType archiveType) {
        this.dlPath = dlPath;
        this.archiveType = archiveType;
    }

    /**
     * Return the {@link OSType} for the current system.
     *
     * @return The value of the currently running {@link OSType}.
     */
    public static OSType getCurrent() {
        return System.getProperty("os.name").contains("windows") ? OSType.Windows : OSType.Linux;
    }
}
