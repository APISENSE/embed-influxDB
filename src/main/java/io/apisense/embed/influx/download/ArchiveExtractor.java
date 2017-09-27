package io.apisense.embed.influx.download;

import java.io.File;

/**
 * Describe the available operations to extract binary archives.
 */
public interface ArchiveExtractor {
    /**
     * Extract given archive to the target path.
     *
     * @param type       Type of archive to extract.
     * @param archive    Archive to extract.
     * @param targetPath Folder to extract to.
     */
    void extract(ArchiveType type, File archive, File targetPath);
}
