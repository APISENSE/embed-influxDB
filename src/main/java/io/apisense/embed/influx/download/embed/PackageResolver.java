package io.apisense.embed.influx.download.embed;

import de.flapdoodle.embed.process.config.store.FileSet;
import de.flapdoodle.embed.process.config.store.FileType;
import de.flapdoodle.embed.process.config.store.IPackageResolver;
import de.flapdoodle.embed.process.distribution.ArchiveType;
import de.flapdoodle.embed.process.distribution.Distribution;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.download.InfluxUrlGenerator;

public class PackageResolver implements IPackageResolver {
    @Override
    public FileSet getFileSet(Distribution distribution) {
        String executableName = "influxd";
        if (!distribution.getPlatform().isUnixLike()) {
            executableName += ".exe";
        }
        return FileSet.builder().addEntry(FileType.Executable, executableName).build();
    }

    @Override
    public ArchiveType getArchiveType(Distribution distribution) {
        return ((VersionConfiguration) distribution).os.archiveType.toEmbedArchiveType();
    }

    @Override
    public String getPath(Distribution distribution) {
        return new InfluxUrlGenerator().buildSource((VersionConfiguration) distribution).getPath();
    }
}
