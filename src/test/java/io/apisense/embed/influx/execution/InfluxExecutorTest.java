package io.apisense.embed.influx.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

public class InfluxExecutorTest {

    private Runtime runtimeMock;
    private ProcessExecutor executor;

    @Before
    public void setUp() throws Exception {
        runtimeMock = Mockito.mock(Runtime.class);
        doReturn(Mockito.mock(Process.class)).when(runtimeMock).exec(anyString());
        executor = new InfluxExecutor(runtimeMock);
    }

    @Test
    public void testStartProcessWillRunTheServer() throws Exception {
        String pathname = "/path";
        executor.startProcess(new File(pathname));

        Mockito.verify(runtimeMock).exec(pathname + " run");
    }

    @Test
    public void testStartMultipleTimeWillExecuteTheProcessOnlyOneTime() throws Exception {
        String pathname = "/path";
        executor.startProcess(new File(pathname));
        executor.startProcess(new File(pathname));

        Mockito.verify(runtimeMock, Mockito.times(1)).exec(pathname + " run");
    }

    @Test
    public void testStopWillResetProcess() throws Exception {
        String pathname = "/path";
        executor.startProcess(new File(pathname));
        executor.stopProcess();
        executor.startProcess(new File(pathname));

        Mockito.verify(runtimeMock, Mockito.times(2)).exec(pathname + " run");
    }

    @Test
    public void testStartProcessWillRunTheServerWithConfig() throws Exception {
        String binPath = "/path";
        String configPath = "/config";
        executor.startProcess(new File(binPath), new File(configPath));

        Mockito.verify(runtimeMock).exec(binPath + " run -config " + configPath);
    }

    @Test(expected = IOException.class)
    public void testStartProcessWillThrowIfExecutionThrows() throws Exception {
        Mockito.doThrow(new IOException()).when(runtimeMock).exec(anyString());

        executor.startProcess(new File("/path"), new File("/config"));
    }

    @Test
    public void testStopProcessWillDestroyProcessIfStarted() throws Exception {
        Process processMock = Mockito.mock(Process.class);
        doReturn(processMock).when(runtimeMock).exec(anyString());

        executor.startProcess(new File("/path"));
        executor.stopProcess();

        Mockito.verify(processMock).destroy();
    }
}