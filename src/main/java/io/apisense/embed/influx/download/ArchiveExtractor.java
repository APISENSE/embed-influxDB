package io.apisense.embed.influx.download;

import java.io.File;

public interface ArchiveExtractor {
    /**
     * Extract given archive to the target path.
     *
     * @param type       Type of archive to extract.
     * @param archive    Archive to extract.
     * @param targetPath Folder to extract to.
     * @see <a href="https://stackoverflow.com/questions/7128171/how-to-compress-decompress-tar-gz-files-in-java"></a>
     * @see <a href="https://github.com/codehaus-plexus/plexus-archiver"></a>
     */
    void extract(ArchiveType type, File archive, File targetPath);
}
