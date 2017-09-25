package io.apisense.embed.influx.configuration;

import io.apisense.embed.influx.download.ArchiveType;

public enum OSType {
    Linux("linux", ArchiveType.TGZ),
    Windows("windows", ArchiveType.ZIP);

    public final String dlPath;
    public final ArchiveType archiveType;

    OSType(String dlPath, ArchiveType archiveType) {
        this.dlPath = dlPath;
        this.archiveType = archiveType;
    }

    public static OSType getCurrent() {
        return System.getProperty("os.name").contains("windows") ? OSType.Windows : OSType.Linux;
    }
}
