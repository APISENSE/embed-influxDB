package io.apisense.embed.influx.download;

import java.io.File;
import java.io.IOException;

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


    /**
     * Find the first daemon binary into the given directory.
     *
     * @param parent The directory to look into.
     * @return The daemon file.
     * @throws IOException If no file has been found.
     */
    File findServerDaemon(File parent) throws IOException;
}
