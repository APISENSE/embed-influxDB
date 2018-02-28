package io.apisense.embed.influx.configuration;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.apisense.embed.influx.configuration.InfluxConfigurationWriter.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class InfluxConfigurationWriterTest {
    private int httpPort;
    private int udpPort;
    private int backupAndRestorePort;

    private InfluxConfigurationWriter config;

    @Before
    public void setUp() throws Exception {
        httpPort = 1234;
        udpPort = 4312;
        backupAndRestorePort = 4321;
        config = new InfluxConfigurationWriter(backupAndRestorePort, httpPort, udpPort);
    }

    @Test
    public void TestDefaultGeneration() throws Exception {
        File file = config.writeFile();
        List<String> content = Files.readAllLines(file.toPath());
        file.delete();

        assertThat("We have a line in the file", content.size(), equalTo(16));
        String onlyLine = content.get(0);
        assertThat("Our backup port configuration key is present", onlyLine.contains(BIND_ADDRESS_ENTRY), is(true));
        assertThat("Our backup port configuration value is present", onlyLine.contains(":" + backupAndRestorePort), is(true));

        onlyLine = content.get(1);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(2);
        assertThat("We have the meta Section", onlyLine.contains("[meta]"), is(true));
        onlyLine = content.get(3);
        assertThat("Our data dir key is present", onlyLine.contains(DIR_ENTRY), is(true));

        onlyLine = content.get(4);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(5);
        assertThat("We have the data Section", onlyLine.contains("[data]"), is(true));
        onlyLine = content.get(6);
        assertThat("Our data dir key is present", onlyLine.contains(DIR_ENTRY), is(true));
        onlyLine = content.get(7);
        assertThat("Our wal-dir key is present", onlyLine.contains(WAL_DIR_ENTRY), is(true));

        onlyLine = content.get(8);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(9);
        assertThat("We have the HTTP Section", onlyLine.contains("[http]"), is(true));
        onlyLine = content.get(10);
        assertThat("Our http port configuration key is present", onlyLine.contains(BIND_ADDRESS_ENTRY), is(true));
        assertThat("Our http port configuration value is present", onlyLine.contains(":" + httpPort), is(true));

        onlyLine = content.get(11);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(12);
        assertThat("We have the UDP Section", onlyLine.contains("[udp]"), is(true));
        onlyLine = content.get(13);
        assertEquals("Our udp enabled configuration is present", onlyLine, ENABLED_ENTRY + " = true" );

        onlyLine = content.get(14);
        assertThat("Our udp port configuration key is present", onlyLine.contains(BIND_ADDRESS_ENTRY), is(true));
        assertThat("Our udp port configuration value is present", onlyLine.contains(":" + udpPort), is(true));

        onlyLine = content.get(15);
        assertThat("Our udp database configuration key is present", onlyLine.contains(DATABASE_ENTRY), is(true));
        assertThat("Our udp database configuration value is present", onlyLine.contains("udp"), is(true));
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

        assertThat("We have a line in the file", content.size(), equalTo(20));
        String onlyLine = content.get(0);
        assertThat("Our backup port configuration is present", onlyLine.contains("bind-address"), is(true));
        assertThat("Our backup port configuration is present", onlyLine.contains(":" + backupAndRestorePort), is(true));

        onlyLine = content.get(1);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(2);
        assertThat("We have the meta Section", onlyLine.contains("[meta]"), is(true));
        onlyLine = content.get(3);
        assertThat("Our data dir key is present", onlyLine.contains(DIR_ENTRY), is(true));

        onlyLine = content.get(4);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(5);
        assertThat("We have the data Section", onlyLine.contains("[data]"), is(true));
        onlyLine = content.get(6);
        assertThat("Our data dir key is present", onlyLine.contains(DIR_ENTRY), is(true));
        onlyLine = content.get(7);
        assertThat("Our wal-dir key is present", onlyLine.contains(WAL_DIR_ENTRY), is(true));

        onlyLine = content.get(8);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(9);
        assertThat("We have the HTTP Section", onlyLine.contains("[http]"), is(true));
        onlyLine = content.get(10);
        assertThat("Our http port configuration is present", onlyLine.contains("bind-address"), is(true));
        assertThat("Our http port configuration is present", onlyLine.contains(":" + httpPort), is(true));

        onlyLine = content.get(11);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(12);
        assertThat("We have the UDP Section", onlyLine.contains("[udp]"), is(true));
        onlyLine = content.get(13);
        assertThat("Our udp enabled configuration key is present", onlyLine.contains(ENABLED_ENTRY), is(true));
        assertThat("Our udp enabled configuration value is present", onlyLine.contains("true"), is(true));

        onlyLine = content.get(14);
        assertThat("Our udp port configuration key is present", onlyLine.contains(BIND_ADDRESS_ENTRY), is(true));
        assertThat("Our udp port configuration value is present", onlyLine.contains(":" + udpPort), is(true));

        onlyLine = content.get(15);
        assertThat("Our udp database configuration key is present", onlyLine.contains(DATABASE_ENTRY), is(true));
        assertThat("Our udp database configuration value is present", onlyLine.contains("udp"), is(true));

        onlyLine = content.get(16);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(17);
        assertThat("Our test category is present", onlyLine.contains(customCategory), is(true));
        onlyLine = content.get(18);
        assertThat("Our first custom configuration is present (key)", onlyLine.contains(firstKey), is(true));
        assertThat("Our first custom configuration is present (value)", onlyLine.contains(firstValue), is(true));
        onlyLine = content.get(19);
        assertThat("Our second custom configuration is present (key)", onlyLine.contains(secondKey), is(true));
        assertThat("Our second custom configuration is present (value)", onlyLine.contains(secondValue), is(true));
    }

}