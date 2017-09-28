package io.apisense.embed.influx.download;

import org.codehaus.plexus.archiver.AbstractUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Default implementation of an {@link ArchiveExtractor} using {@link org.codehaus.plexus.archiver.UnArchiver}
 */
public class InfluxArchiveExtractor implements ArchiveExtractor {
    private static final Logger logger = LoggerFactory.getLogger(InfluxArchiveExtractor.class.getName());

    @Override
    public void extract(ArchiveType type, File archive, File targetPath) {
        final AbstractUnArchiver unArchiver = type.unArchiver();
        ConsoleLoggerManager manager = new ConsoleLoggerManager();
        manager.initialize();
        unArchiver.enableLogging(manager.getLoggerForComponent(logger.getName()));

        unArchiver.setSourceFile(archive);
        if (!(targetPath.mkdirs())) {
            logger.warn("Unable to create directory: " + targetPath.getAbsolutePath());
        }
        unArchiver.setDestDirectory(targetPath);
        unArchiver.extract();
    }

    @Override
    public File findServerDaemon(File parent) throws IOException {
        DaemonFileVisitor visitor = new DaemonFileVisitor();
        Files.walkFileTree(parent.toPath(), visitor);
        return visitor.retrieveDaemonFile();
    }

    /**
     * One shot visitor configured to find an InfluxDB daemon binary file.
     */
    private static class DaemonFileVisitor extends SimpleFileVisitor<Path> {
        private static final String DAEMON_PATH = "usr" + File.separator + "bin" + File.separator + "influxd";
        private File daemonFile;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toAbsolutePath().endsWith(DAEMON_PATH)) {
                daemonFile = file.toFile();
                return FileVisitResult.TERMINATE;
            }
            return super.visitFile(file, attrs);
        }

        /**
         * Retrieve the previously found daemon file.
         * The visitor must have been used before.
         *
         * @return The daemon file if found.
         * @throws IOException If no file has been found by this visitor yet.
         */
        File retrieveDaemonFile() throws IOException {
            if (daemonFile == null) {
                throw new IOException("Daemon file not found");
            }
            return daemonFile;
        }
    }
}
