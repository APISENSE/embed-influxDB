package io.apisense.embed.influx.configuration;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class InfluxConfigurationTest {
    private int port;
    private InfluxConfiguration config;

    @Before
    public void setUp() throws Exception {
        port = 1234;
        config = new InfluxConfiguration(port);
    }

    @Test
    public void TestDefaultGeneration() throws Exception {
        File file = config.writeFile();
        List<String> content = Files.readAllLines(file.toPath());
        file.delete();

        assertThat("We have a line in the file", content.size(), equalTo(1));
        String onlyLine = content.get(0);
        assertThat("Our port configuration is present", onlyLine.contains("bind-address"), is(true));
        assertThat("Our port configuration is present", onlyLine.contains("127.0.0.1:" + port), is(true));
    }

    @Test
    public void TestGenerationWithStatement() throws Exception {
        HashMap<String, Object> customConfig = new HashMap<>();
        Map<String, String> innerConfig = new HashMap<>();

        String firstKey = "a";
        String firstValue = "1";
        innerConfig.put(firstKey, firstValue);
        String secondKey = "b";
        String secondValue = "2";
        innerConfig.put(secondKey, secondValue);
        String customCategory = "test";
        customConfig.put(customCategory, innerConfig);
        config.addStatements(customConfig);

        File file = config.writeFile();
        List<String> content = Files.readAllLines(file.toPath());
        file.delete();

        assertThat("We have a line in the file", content.size(), equalTo(5));
        String onlyLine = content.get(0);
        assertThat("Our port configuration is present (key)", onlyLine.contains("bind-address"), is(true));
        assertThat("Our port configuration is present (value)", onlyLine.contains("127.0.0.1:" + port), is(true));

        onlyLine = content.get(1);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));
        onlyLine = content.get(2);
        assertThat("Our test category is present", onlyLine.contains(customCategory), is(true));
        onlyLine = content.get(3);
        assertThat("Our first custom configuration is present (key)", onlyLine.contains(firstKey), is(true));
        assertThat("Our first custom configuration is present (value)", onlyLine.contains(firstValue), is(true));
        onlyLine = content.get(4);
        assertThat("Our second custom configuration is present (key)", onlyLine.contains(secondKey), is(true));
        assertThat("Our second custom configuration is present (value)", onlyLine.contains(secondValue), is(true));
    }


}