package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.OSArchitecture;
import io.apisense.embed.influx.configuration.OSType;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class InfluxBinaryDownloaderTest {
    private VersionConfiguration configuration;
    private MockWebServer webServer;
    private File parentDir;
    private File targetPath;
    private UrlGenerator urlGeneratorMock;
    private BinaryDownloader downloader;
    private HttpUrl requestUrl;
    private ArchiveExtractor extractorMock;

    @Before
    public void setUp() throws Exception {
        configuration = new VersionConfiguration(OSType.Linux, OSArchitecture.ARM, InfluxVersion.PRODUCTION);

        webServer = new MockWebServer();
        webServer.start();
        requestUrl = webServer.url("/test");
        webServer.enqueue(new MockResponse().setBody("response"));

        parentDir = Mockito.mock(File.class);
        doReturn(true).when(parentDir).exists();
        doReturn(true).when(parentDir).isDirectory();

        targetPath = Mockito.spy(new File("/target"));
        doReturn(true).when(targetPath).exists();

        urlGeneratorMock = Mockito.mock(UrlGenerator.class);
        doReturn(targetPath).when(urlGeneratorMock).buildTarget(parentDir, configuration);

        extractorMock = Mockito.mock(ArchiveExtractor.class);

        downloader = new InfluxBinaryDownloader(parentDir, urlGeneratorMock, new OkHttpClient(), extractorMock);
    }

    @After
    public void tearDown() throws Exception {
        webServer.shutdown();
    }

    @Test
    public void testDownloadCreateParentDirectoryIfNotExisting() throws Exception {
        Mockito.reset(parentDir);
        doReturn(false).when(parentDir).exists();
        doReturn(true).when(parentDir).mkdirs();

        downloader.download(configuration);

        verify(parentDir).mkdirs();
    }

    @Test(expected = IOException.class)
    public void testDownloadThrowExceptionIfParentIsAFile() throws Exception {
        Mockito.reset(parentDir);
        doReturn(true).when(parentDir).exists();
        doReturn(false).when(parentDir).isDirectory();

        downloader.download(configuration);
    }

    @Test
    public void testDownloadWillDownloadIfNotInCache() throws Exception {
        Mockito.reset(targetPath);
        doReturn(false).when(targetPath).exists();
        doReturn(requestUrl.url()).when(urlGeneratorMock).buildSource(configuration);

        downloader.download(configuration);

        RecordedRequest recordedRequest = webServer.takeRequest(1, TimeUnit.SECONDS);
        verify(parentDir, never()).mkdirs();
        assertThat("We had a request", webServer.getRequestCount(), equalTo(1));
        assertThat("The request was on the right URL", recordedRequest.getRequestUrl(), equalTo(requestUrl));
        verify(extractorMock).extract(eq(configuration.os.archiveType), any(), eq(targetPath));
    }

    @Test
    public void testDownloadWillDoNothingIfInCache() throws Exception {
        downloader.download(configuration);

        verify(targetPath, never()).mkdirs();
        verify(parentDir, never()).mkdirs();
        assertThat("We had no request", webServer.getRequestCount(), equalTo(0));
    }
}