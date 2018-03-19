package io.apisense.embed.influx.configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.apisense.embed.influx.configuration.server.ConfigurationProperty.AUTH_ENABLED;
import static io.apisense.embed.influx.configuration.server.ConfigurationProperty.DIR;
import static io.apisense.embed.influx.configuration.server.ConfigurationProperty.ENABLED;
import static io.apisense.embed.influx.configuration.server.ConfigurationProperty.WAL_DIR;
import static io.apisense.embed.influx.configuration.server.ConfigurationProperty.BIND_ADDRESS;
import static io.apisense.embed.influx.configuration.server.ConfigurationProperty.DATABASE;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class InfluxConfigurationWriterTest {
    private int httpPort;
    private int udpPort;
    private int backupAndRestorePort;

    private InfluxConfigurationWriter config;
    private File dataPath;

    @Before
    public void setUp() throws Exception {
        httpPort = 1234;
        udpPort = 4312;
        backupAndRestorePort = 4321;
        dataPath = File.createTempFile("data-path", Long.toString(System.nanoTime()));
        config = new InfluxConfigurationWriter.Builder()
                .setBackupAndRestorePort(backupAndRestorePort)
                .setDataPath(dataPath)
                .setHttp(httpPort)
                .setUdp(udpPort)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        dataPath.delete();
    }

    @Test
    public void TestDefaultGeneration() throws Exception {
        ConfigurationWriter defaultConfig = new InfluxConfigurationWriter.Builder().build();
        File file = defaultConfig.writeFile();
        List<String> content = Files.readAllLines(file.toPath());
        file.delete();
        defaultConfig.getDataPath().delete();

        assertThat("We have a line in the file", content.size(), equalTo(12));
        String onlyLine = content.get(0);
        assertThat("Our backup port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our backup port configuration default value is present", onlyLine.contains(":" + 8088), is(true));

        onlyLine = content.get(1);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(2);
        assertThat("We have the HTTP Section", onlyLine.contains("[http]"), is(true));
        onlyLine = content.get(3);
        assertThat("Our http port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our http port configuration default value is present", onlyLine.contains(":" + 8086), is(true));
        onlyLine = content.get(4);
        assertThat("Our http auth configuration key is present", onlyLine.contains(AUTH_ENABLED.toString()), is(true));
        assertThat("Our http auth configuration default value is present", onlyLine.contains("false"), is(true));

        onlyLine = content.get(5);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(6);
        assertThat("We have the meta Section", onlyLine.contains("[meta]"), is(true));
        onlyLine = content.get(7);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));

        onlyLine = content.get(8);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(9);
        assertThat("We have the data Section", onlyLine.contains("[data]"), is(true));
        onlyLine = content.get(10);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));
        onlyLine = content.get(11);
        assertThat("Our wal-dir key is present", onlyLine.contains(WAL_DIR.toString()), is(true));
    }

    @Test
    public void TestCompleteGeneration() throws Exception {
        File file = config.writeFile();
        List<String> content = Files.readAllLines(file.toPath());
        file.delete();

        assertThat("We have a line in the file", content.size(), equalTo(17));
        String onlyLine = content.get(0);
        assertThat("Our backup port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our backup port configuration value is present", onlyLine.contains(":" + backupAndRestorePort), is(true));

        onlyLine = content.get(1);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(2);
        assertThat("We have the meta Section", onlyLine.contains("[meta]"), is(true));
        onlyLine = content.get(3);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));

        onlyLine = content.get(4);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(5);
        assertThat("We have the data Section", onlyLine.contains("[data]"), is(true));
        onlyLine = content.get(6);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));
        onlyLine = content.get(7);
        assertThat("Our wal-dir key is present", onlyLine.contains(WAL_DIR.toString()), is(true));

        onlyLine = content.get(8);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(9);
        assertThat("We have the HTTP Section", onlyLine.contains("[http]"), is(true));
        onlyLine = content.get(10);
        assertThat("Our http port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our http port configuration value is present", onlyLine.contains(":" + httpPort), is(true));
        onlyLine = content.get(11);
        assertThat("Our http auth configuration key is present", onlyLine.contains(AUTH_ENABLED.toString()), is(true));
        assertThat("Our http auth configuration value is present", onlyLine.contains("false"), is(true));

        onlyLine = content.get(12);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(13);
        assertThat("We have the UDP Section", onlyLine.contains("[[udp]]"), is(true));
        onlyLine = content.get(14);
        assertEquals("Our udp enabled configuration is present", onlyLine, ENABLED + " = true");

        onlyLine = content.get(15);
        assertThat("Our udp port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our udp port configuration value is present", onlyLine.contains(":" + udpPort), is(true));

        onlyLine = content.get(16);
        assertThat("Our udp database configuration key is present", onlyLine.contains(DATABASE.toString()), is(true));
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

        assertThat("We have a line in the file", content.size(), equalTo(21));
        String onlyLine = content.get(0);
        assertThat("Our backup port configuration is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our backup port configuration is present", onlyLine.contains(":" + backupAndRestorePort), is(true));

        onlyLine = content.get(1);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(2);
        assertThat("We have the meta Section", onlyLine.contains("[meta]"), is(true));
        onlyLine = content.get(3);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));

        onlyLine = content.get(4);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(5);
        assertThat("We have the data Section", onlyLine.contains("[data]"), is(true));
        onlyLine = content.get(6);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));
        onlyLine = content.get(7);
        assertThat("Our wal-dir key is present", onlyLine.contains(WAL_DIR.toString()), is(true));

        onlyLine = content.get(8);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(9);
        assertThat("We have the HTTP Section", onlyLine.contains("[http]"), is(true));
        onlyLine = content.get(10);
        assertThat("Our http port configuration is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our http port configuration is present", onlyLine.contains(":" + httpPort), is(true));
        onlyLine = content.get(11);
        assertThat("Our http auth configuration is present", onlyLine.contains(AUTH_ENABLED.toString()), is(true));
        assertThat("Our http auth configuration is present", onlyLine.contains("false"), is(true));

        onlyLine = content.get(12);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(13);
        assertThat("We have the UDP Section", onlyLine.contains("[[udp]]"), is(true));
        onlyLine = content.get(14);
        assertThat("Our udp enabled configuration key is present", onlyLine.contains(ENABLED.toString()), is(true));
        assertThat("Our udp enabled configuration value is present", onlyLine.contains("true"), is(true));

        onlyLine = content.get(15);
        assertThat("Our udp port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our udp port configuration value is present", onlyLine.contains(":" + udpPort), is(true));

        onlyLine = content.get(16);
        assertThat("Our udp database configuration key is present", onlyLine.contains(DATABASE.toString()), is(true));
        assertThat("Our udp database configuration value is present", onlyLine.contains("udp"), is(true));

        onlyLine = content.get(17);
        assertThat("This is an empty line", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(18);
        assertThat("Our test category is present", onlyLine.contains(customCategory), is(true));
        onlyLine = content.get(19);
        assertThat("Our first custom configuration is present (key)", onlyLine.contains(firstKey), is(true));
        assertThat("Our first custom configuration is present (value)", onlyLine.contains(firstValue), is(true));
        onlyLine = content.get(20);
        assertThat("Our second custom configuration is present (key)", onlyLine.contains(secondKey), is(true));
        assertThat("Our second custom configuration is present (value)", onlyLine.contains(secondValue), is(true));
    }

    @Test
    public void TestGeneratingWithoutUDPSection() throws Exception {
        config = new InfluxConfigurationWriter.Builder()
                .setDataPath(dataPath)
                .setBackupAndRestorePort(backupAndRestorePort)
                .setHttp(httpPort, true)
                .build();
        File file = config.writeFile();
        List<String> content = Files.readAllLines(file.toPath());
        file.delete();

        assertThat("We have a line in the file", content.size(), equalTo(12));
        String onlyLine = content.get(0);
        assertThat("Our backup port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our backup port configuration value is present", onlyLine.contains(":" + backupAndRestorePort), is(true));

        onlyLine = content.get(1);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(2);
        assertThat("We have the meta Section", onlyLine.contains("[meta]"), is(true));
        onlyLine = content.get(3);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));

        onlyLine = content.get(4);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(5);
        assertThat("We have the data Section", onlyLine.contains("[data]"), is(true));
        onlyLine = content.get(6);
        assertThat("Our data dir key is present", onlyLine.contains(DIR.toString()), is(true));
        onlyLine = content.get(7);
        assertThat("Our wal-dir key is present", onlyLine.contains(WAL_DIR.toString()), is(true));

        onlyLine = content.get(8);
        assertThat("This line is empty", onlyLine.isEmpty(), is(true));

        onlyLine = content.get(9);
        assertThat("We have the HTTP Section", onlyLine.contains("[http]"), is(true));
        onlyLine = content.get(10);
        assertThat("Our http port configuration key is present", onlyLine.contains(BIND_ADDRESS.toString()), is(true));
        assertThat("Our http port configuration value is present", onlyLine.contains(":" + httpPort), is(true));

        onlyLine = content.get(11);
        assertThat("Our http auth configuration key is present", onlyLine.contains(AUTH_ENABLED.toString()), is(true));
        assertThat("Our http auth configuration value is present", onlyLine.contains("true"), is(true));

    }

}