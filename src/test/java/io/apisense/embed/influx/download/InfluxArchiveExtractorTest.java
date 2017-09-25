package io.apisense.embed.influx.download;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InfluxArchiveExtractorTest {

    private ArchiveExtractor extractor;
    private File outputDir;

    @Before
    public void setUp() throws Exception {
        extractor = new InfluxArchiveExtractor();
        outputDir = File.createTempFile(getClass().getName(), Long.toString(System.nanoTime()));
        outputDir.delete(); // We have to give an existing directory or a non existing path.
    }

    @After
    public void tearDown() throws Exception {
        recursiveDeletion(outputDir);
    }

    @Test
    public void testExtractZip() throws Exception {
        checkFileExtraction(ArchiveType.ZIP);
    }

    @Test
    public void testExtractTarGzip() throws Exception {
        checkFileExtraction(ArchiveType.TGZ);
    }

    private void checkFileExtraction(ArchiveType type) {
        extractor.extract(type, fetchTestArchive(type), outputDir);

        File expected = new File(outputDir, "test");
        assertThat("Our file has been extracted", expected.exists(), is(true));

    }

    private File fetchTestArchive(ArchiveType type) {
        String resourcePath = File.separator + "extraction" + File.separator + "test." + type.extension;
        URL resource = getClass().getResource(resourcePath);
        return new File(resource.getFile());
    }

    private static void recursiveDeletion(File outputDir) throws IOException {
        Files.walkFileTree(outputDir.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                FileVisitResult result = super.visitFile(file, attrs);
                file.toFile().delete();
                return result;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                FileVisitResult result = super.postVisitDirectory(dir, exc);
                dir.toFile().delete();
                return result;
            }
        });
        outputDir.delete();
    }
}