package io.apisense.embed.influx.download;

import de.flapdoodle.embed.process.distribution.ArchiveType;
import org.codehaus.plexus.archiver.AbstractUnArchiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

/**
 * Describe the handled Archive and their respective extensions.
 */
public enum InfluxArchiveType {
    ZIP("zip", ArchiveType.ZIP) {
        @Override
        public AbstractUnArchiver unArchiver() {
            return new ZipUnArchiver();
        }
    },
    TGZ("tar.gz", ArchiveType.TGZ) {
        @Override
        public AbstractUnArchiver unArchiver() {
            return new TarGZipUnArchiver();
        }
    };

    private final String extension;
    private final ArchiveType embedArchiveType;

    InfluxArchiveType(String extension, ArchiveType embedArchiveType) {
        this.extension = extension;
        this.embedArchiveType = embedArchiveType;
    }

    /**
     * Returns the {@link UnArchiver} associated with the {@link InfluxArchiveType}.
     *
     * @return The {@link UnArchiver} to use, will return {@link AbstractUnArchiver} to access logging methods.
     */
    public abstract AbstractUnArchiver unArchiver();

    public ArchiveType toEmbedArchiveType() {
        return embedArchiveType;
    }

    public String getExtension() {
        return extension;
    }

    public ArchiveType getEmbedArchiveType() {
        return embedArchiveType;
    }
}
