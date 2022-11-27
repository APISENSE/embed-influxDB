package io.apisense.embed.influx.execution;

import io.apisense.embed.influx.InfluxServer;
import io.apisense.embed.influx.configuration.ConfigurationWriter;
import io.apisense.embed.influx.configuration.CustomInfluxVersion;
import io.apisense.embed.influx.configuration.FromFileConfigurationWriter;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.execution.embed.InfluxProcess;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InfluxActualExecutionTest {
    private InfluxServer server;

    @Before
    public void setUp() throws Exception {
        ConfigurationWriter config = new FromFileConfigurationWriter.Builder(retrieveTestConfiguration()).build();

        InfluxServer.Builder builder = new InfluxServer.Builder();
        server = builder
                .setInfluxConfiguration(config)
                .setVersionConfig(VersionConfiguration.fromRuntime(new CustomInfluxVersion("2.5.1")))
                .build();
    }

    File retrieveTestConfiguration() {
        URL resource = getClass().getResource(File.separator + "execution"
                + File.separator + "influxdb.conf");
        return new File(resource.getFile());
    }

    @Test
    public void testPrepareCreateOneExecution() throws Exception {
        server.start();
        InfluxProcess process = server.getProcess();
        assertThat("The process is not null upon startup", process, is(IsNull.notNullValue()));
        assertThat("The process is running", process.isProcessRunning(), is(true));

        server.stop();
        assertThat("The process is running", process.isProcessRunning(), is(false));
        process = server.getProcess();
        assertThat("The process is null after stop", process, is(IsNull.nullValue()));

    }
}