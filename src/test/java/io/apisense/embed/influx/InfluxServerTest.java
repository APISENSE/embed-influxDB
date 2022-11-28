package io.apisense.embed.influx;

import io.apisense.embed.influx.execution.InfluxExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InfluxServerTest {
    private EmbeddedDB server;
    private InfluxExecutor executorMock;

    @Before
    public void setUp() throws Exception {
        executorMock = mock(InfluxExecutor.class);
        server = new InfluxServer(executorMock);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitWillPrepareExecution() throws Exception {
        server.init();

        verify(executorMock).prepare();
    }

    @Test
    public void testStartWillCallInitIfNotCalled() throws Exception {
        server.start();

        verify(executorMock).prepare();
        verify(executorMock).start();
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

        verify(executorMock).prepare();
        verify(executorMock).start();
        verify(executorMock).stop();
    }

    @Test
    public void testCleanupWillRemoveConfigFileAndData() throws Exception {
        assertThat("Server is in unknown state", server.getCurrentState(), equalTo(ServerState.UNKNOWN));
        server.init();
        assertThat("Server is in ready state", server.getCurrentState(), equalTo(ServerState.READY));
        server.start();
        assertThat("Server is in started state", server.getCurrentState(), equalTo(ServerState.STARTED));
        server.stop();
        assertThat("Server is in stopped state", server.getCurrentState(), equalTo(ServerState.STOPPED));
        server.cleanup();
        assertThat("Server is in clean state", server.getCurrentState(), equalTo(ServerState.CLEAN));

        verify(executorMock).prepare();
        verify(executorMock).start();
        verify(executorMock).stop();
        verify(executorMock).cleanup();
    }

    @Test
    public void testCleanupWillStopThenCleanup() throws Exception {
        server.start();
        server.cleanup();

        verify(executorMock).prepare();
        verify(executorMock).start();
        verify(executorMock).stop();
        verify(executorMock).cleanup();
    }
}