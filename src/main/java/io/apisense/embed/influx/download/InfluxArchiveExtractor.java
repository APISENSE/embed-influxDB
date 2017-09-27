package io.apisense.embed.influx.download;

import org.codehaus.plexus.archiver.AbstractUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Default implementation of an {@link ArchiveExtractor} using {@link org.codehaus.plexus.archiver.UnArchiver}.
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
}
