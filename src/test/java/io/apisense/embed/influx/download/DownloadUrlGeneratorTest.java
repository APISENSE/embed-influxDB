package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.OSArchitecture;
import io.apisense.embed.influx.configuration.OSType;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DownloadUrlGeneratorTest {
    private InfluxUrlGenerator influxUrlGenerator;

    @Before
    public void setUp() throws Exception {
        this.influxUrlGenerator = new InfluxUrlGenerator();
    }

    @Test
    public void testVersion2MacUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Mac, OSArchitecture.x86_64, InfluxVersion.V2_5_1);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/releases/influxdb2-2.5.1-darwin-amd64.tar.gz"));
    }

    @Test
    public void testProductionMacUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Mac, OSArchitecture.x86_64, InfluxVersion.V1_7_6);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/releases/influxdb-1.7.6_darwin_amd64.tar.gz"));
    }

    @Test
    public void testProductionLinuxArmUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Linux, OSArchitecture.ARM, InfluxVersion.V1_7_6);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/releases/influxdb-1.7.6_linux_armhf.tar.gz"));
    }

    @Test
    public void testNightlyLinuxArmUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Linux, OSArchitecture.ARM, InfluxVersion.NIGHTLY);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/nightlies/influxdb-nightly_linux_armhf.tar.gz"));
    }

    @Test
    public void testProductionLinux32BitsUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Linux, OSArchitecture.i386, InfluxVersion.V1_7_6);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/releases/influxdb-1.7.6_linux_i386.tar.gz"));
    }

    @Test
    public void testNightlyLinux32BitsUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Linux, OSArchitecture.i386, InfluxVersion.NIGHTLY);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/nightlies/influxdb-nightly_linux_i386.tar.gz"));
    }

    @Test
    public void testProductionLinux64BitsUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Linux, OSArchitecture.x86_64, InfluxVersion.V1_7_6);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/releases/influxdb-1.7.6_linux_amd64.tar.gz"));
    }

    @Test
    public void testNightlyLinux64BitsUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Linux, OSArchitecture.x86_64, InfluxVersion.NIGHTLY);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/nightlies/influxdb-nightly_linux_amd64.tar.gz"));
    }

    @Test
    public void testProductionWindows64BitsUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Windows, OSArchitecture.x86_64, InfluxVersion.V1_7_6);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/releases/influxdb-1.7.6_windows_amd64.zip"));
    }

    @Test
    public void testNightlyWindows64BitsUrlIsCorrect() throws Exception {
        VersionConfiguration config = new VersionConfiguration(OSType.Windows, OSArchitecture.x86_64, InfluxVersion.NIGHTLY);
        String url = influxUrlGenerator.buildSource(config).toString();

        assertThat("Url is the same as on the download website", url,
                is("https://dl.influxdata.com/influxdb/nightlies/influxdb-nightly_windows_amd64.zip"));
    }
}
