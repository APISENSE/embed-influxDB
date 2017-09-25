package io.apisense.embed.influx;

import io.apisense.embed.influx.configuration.ConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.OSArchitecture;
import io.apisense.embed.influx.configuration.OSType;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.download.BinaryDownloader;
import io.apisense.embed.influx.execution.ProcessExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InfluxServerTest {

    private File temporaryFile;
    private VersionConfiguration versionConfig;
    private BinaryDownloader downloaderMock;
    private ProcessExecutor executorMock;
    private ConfigurationWriter influxConfigMock;
    private EmbeddedDB server;

    private File configFile;
    private File binFile;
    private InfluxServer.Builder builder;

    @Before
    public void setUp() throws Exception {
        temporaryFile = File.createTempFile("Influxtest-tempFileIsNotADir", Long.toString(System.nanoTime()));
        versionConfig = new VersionConfiguration(OSType.Linux, OSArchitecture.ARM, InfluxVersion.PRODUCTION);
        downloaderMock = Mockito.mock(BinaryDownloader.class);
        executorMock = Mockito.mock(ProcessExecutor.class);
        influxConfigMock = Mockito.mock(ConfigurationWriter.class);
        binFile = Mockito.mock(File.class);
        configFile = Mockito.mock(File.class);
        doReturn(binFile)
                .when(downloaderMock).download(versionConfig);
        doReturn(configFile).when(influxConfigMock).writeFile();

        builder = new InfluxServer.Builder()
                .setDownloader(downloaderMock)
                .setVersionConfig(versionConfig)
                .setInfluxConfiguration(influxConfigMock)
                .setExecutor(executorMock);
        server = builder.build();
    }

    @After
    public void tearDown() throws Exception {
        temporaryFile.delete();
    }

    @Test(expected = IOException.class)
    public void testBuilderThrowsIfDatabasePathIsFile() throws Exception {
        new InfluxServer.Builder().setDatabasePath(temporaryFile);
    }

    @Test
    public void testInitWillDownloadBinary() throws Exception {
        server.init();

        verify(downloaderMock).download(versionConfig);
    }

    @Test
    public void testStartWillCallInitIfNotCalled() throws Exception {
        server.start();

        verify(downloaderMock).download(versionConfig);
        verify(executorMock).startProcess(binFile, configFile);
        verify(influxConfigMock).writeFile();
    }

    @Test(expected = ServerAlreadyRunningException.class)
    public void testStartASecondTimeWillThrowAnException() throws Exception {
        server.start();
        server.start();
    }

    @Test(expected = ServerNotRunningException.class)
    public void testStopWithoutStartingWillThrowAnException() throws Exception {
        server.stop();
    }

    @Test
    public void testStartStopWillStartThenStopExecution() throws Exception {
        server.start();
        server.stop();

        verify(downloaderMock).download(versionConfig);
        verify(executorMock).startProcess(binFile, configFile);
        verify(influxConfigMock).writeFile();
        verify(executorMock).stopProcess();
    }

    @Test
    public void testCleanupWillRemoveConfigFileAndData() throws Exception {
        File dbPath = mock(File.class);
        doReturn(true).when(dbPath).isDirectory();
        builder.setDatabasePath(dbPath);
        EmbeddedDB server = builder.build();

        server.start();
        server.stop();
        server.cleanup();

        verify(downloaderMock).download(versionConfig);
        verify(executorMock).startProcess(binFile, configFile);
        verify(influxConfigMock).writeFile();
        verify(executorMock).stopProcess();
        verify(configFile).delete();
        verify(dbPath).delete();
    }

    @Test
    public void testCleanupWillStopThenCleanup() throws Exception {
        File dbPath = mock(File.class);
        doReturn(true).when(dbPath).isDirectory();
        builder.setDatabasePath(dbPath);
        EmbeddedDB server = builder.build();

        server.start();
        server.cleanup();

        verify(downloaderMock).download(versionConfig);
        verify(executorMock).startProcess(binFile, configFile);
        verify(influxConfigMock).writeFile();
        verify(executorMock).stopProcess();
        verify(configFile).delete();
        verify(dbPath).delete();

    }
}