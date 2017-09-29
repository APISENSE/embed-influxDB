package io.apisense.embed.influx.execution;

import de.flapdoodle.embed.process.distribution.Distribution;
import io.apisense.embed.influx.configuration.ConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.OSArchitecture;
import io.apisense.embed.influx.configuration.OSType;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;
import io.apisense.embed.influx.execution.embed.InfluxExecutable;
import io.apisense.embed.influx.execution.embed.InfluxProcess;
import io.apisense.embed.influx.execution.embed.InfluxServerStarter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InfluxExecutorTest {

    private EmbeddedExecutor executor;
    private InfluxServerStarter starterMock;
    private InfluxExecutableConfig influxExecutableConfig;
    private Distribution distribution;
    private InfluxExecutable executionMock;
    private InfluxProcess processMock;

    @Before
    public void setUp() throws Exception {
        distribution = new VersionConfiguration(OSType.Linux, OSArchitecture.ARM, InfluxVersion.PRODUCTION);
        influxExecutableConfig = new InfluxExecutableConfig(distribution.getVersion(), Mockito.mock(ConfigurationWriter.class));

        starterMock = Mockito.mock(InfluxServerStarter.class);

        executor = new InfluxExecutor(starterMock, influxExecutableConfig, distribution);
        executionMock = Mockito.mock(InfluxExecutable.class);
        doReturn(executionMock).when(starterMock).prepare(influxExecutableConfig, distribution);

        processMock = Mockito.mock(InfluxProcess.class);
        doReturn(processMock).when(executionMock).start();
    }

    @Test
    public void testPrepareCreateOneExecution() throws Exception {
        executor.prepare();
        executor.prepare(); // prepare is called only once

        verify(starterMock, times(1)).prepare(influxExecutableConfig, distribution);
    }

    @Test
    public void testCleanupResetsExecution() throws Exception {
        executor.prepare();
        executor.cleanup();
        executor.prepare();

        verify(starterMock, times(2)).prepare(influxExecutableConfig, distribution);
    }

    @Test
    public void testStartNotCalledWhileNotPrepared() throws Exception {
        executor.start();

        verify(executionMock, never()).start();
    }

    @Test
    public void testStartWillStartExecution() throws Exception {
        executor.prepare();
        executor.start();
        executor.start(); // Start is called only once

        verify(executionMock).start();
    }

    @Test
    public void testStopNotCalledWhileNotPrepared() throws Exception {
        executor.stop();

        verify(executionMock, never()).stop();
    }

    @Test
    public void testStopWillStopProcess() throws Exception {
        executor.prepare();
        executor.start();
        executor.stop();

        verify(executionMock).start();
        verify(executionMock, never()).stop();
        //verify(processMock).stop(); // TODO: We can't test that since method is final
    }

    @Test
    public void testCleanupWillDoNothingIfNotStarted() throws Exception {
        executor.prepare();
        executor.cleanup();

        verify(processMock, never()).stop();
    }

    @Test
    public void testCleanupWillStopExecution() throws Exception {
        executor.prepare();
        executor.start();
        executor.cleanup();

        verify(executionMock).stop();
    }
}