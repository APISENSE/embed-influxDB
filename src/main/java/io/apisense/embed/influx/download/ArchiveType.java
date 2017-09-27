package io.apisense.embed.influx.download;

import org.codehaus.plexus.archiver.AbstractUnArchiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

/**
 * Describe the handled Archive and their respective extensions.
 */
public enum ArchiveType {
    ZIP("zip") {
        @Override
        public AbstractUnArchiver unArchiver() {
            return new ZipUnArchiver();
        }
    },
    TGZ("tar.gz") {
        @Override
        public AbstractUnArchiver unArchiver() {
            return new TarGZipUnArchiver();
        }
    };

    public final String extension;

    ArchiveType(String extension) {
        this.extension = extension;
    }

    /**
     * Returns the {@link UnArchiver} associated with the {@link ArchiveType}.
     *
     * @return The {@link UnArchiver} to use, will return {@link AbstractUnArchiver} to access logging methods.
     */
    public abstract AbstractUnArchiver unArchiver();
}
