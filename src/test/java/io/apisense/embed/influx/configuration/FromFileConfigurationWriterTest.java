package io.apisense.embed.influx.configuration;

import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FromFileConfigurationWriterTest {

    @Test
    public void TestTheFileIsReturned() throws Exception {
        File configFile = new File("/tmp/my-config-file");
        ConfigurationWriter defaultConfig = new FromFileConfigurationWriter.Builder(configFile).build();

        assertThat("We should have the same File", defaultConfig.writeFile(), is(configFile));
        assertThat("We don't have dataPath to remove by default", defaultConfig.getDataPath(), is(nullValue()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void TestWeCantUseAddStatement() throws Exception {
        File configFile = new File("/tmp/my-config-file");
        ConfigurationWriter defaultConfig = new FromFileConfigurationWriter.Builder(configFile).build();

        defaultConfig.addStatements(new HashMap<String, Object>());
    }

    @Test
    public void TestWeCanSetADataPathToRemove() throws Exception {
        File configFile = new File("/tmp/my-config-file");
        File toRemove = new File("/tmp/my-data-path-to-remove");
        ConfigurationWriter defaultConfig = new FromFileConfigurationWriter.Builder(configFile)
                .removePathOnStop(toRemove)
                .build();

        assertThat("We should have the same File", defaultConfig.writeFile(), is(configFile));
        assertThat("We have the set dataPath to remove", defaultConfig.getDataPath(), is(toRemove));
    }

}