package io.apisense.embed.influx.execution.embed;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import de.flapdoodle.embed.process.runtime.ICommandLinePostProcessor;
import io.apisense.embed.influx.configuration.ConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.OSArchitecture;
import io.apisense.embed.influx.configuration.OSType;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InfluxProcessTest {

    private InfluxProcess process;
    private Distribution distribution;
    private InfluxExecutableConfig execConfig;
    private File configFileSpy;
    private ConfigurationWriter configWriterMock;
    private IExtractedFileSet extractedFileSetMock;
    private IRuntimeConfig runtimeConfigMock;
    private InfluxExecutable executable;

    @Before
    public void setUp() throws Exception {
        extractedFileSetMock = Mockito.mock(IExtractedFileSet.class);
        File pidFileSpy = spy(new File(""));
        doReturn(retrieveExecutableResource()).when(extractedFileSetMock).executable();
        doReturn(pidFileSpy).when(extractedFileSetMock).baseDir();

        configWriterMock = Mockito.mock(ConfigurationWriter.class);
        configFileSpy = spy(new File("toto"));
        doReturn(configFileSpy).when(configWriterMock).writeFile();

        runtimeConfigMock = Mockito.mock(IRuntimeConfig.class);
        doReturn(Mockito.mock(ProcessOutput.class)).when(runtimeConfigMock).getProcessOutput();
        doReturn(new ICommandLinePostProcessor.Noop()).when(runtimeConfigMock).getCommandLinePostProcessor();
        distribution = new VersionConfiguration(OSType.Linux, OSArchitecture.ARM, InfluxVersion.PRODUCTION);
        execConfig = new InfluxExecutableConfig(distribution.getVersion(), configWriterMock);
        executable = new InfluxExecutable(distribution, execConfig, runtimeConfigMock, extractedFileSetMock);
    }

    File retrieveExecutableResource() {
        URL resource = getClass().getResource(File.separator + "execution"
                + File.separator + "noOperationExecutable.sh");
        return new File(resource.getFile());
    }

    @Test
    public void testProcessWillCreateConfigFile() throws Exception {
        process = new InfluxProcess(distribution, execConfig, runtimeConfigMock, executable);
        verify(configWriterMock).writeFile();

        List<String> commandLine = process.getCommandLine(distribution, execConfig, extractedFileSetMock);

        System.out.println(commandLine);
        assertThat("Command line contains 2 elements", commandLine.size(), is(3));
        assertThat("First element is the file", commandLine.get(0), is(retrieveExecutableResource().getAbsolutePath()));
        assertThat("Second element is the command", commandLine.get(1), equalTo("run"));
        assertThat("Third element is the config file", commandLine.get(2), equalTo("-config=" + configFileSpy.getAbsolutePath()));
    }
}