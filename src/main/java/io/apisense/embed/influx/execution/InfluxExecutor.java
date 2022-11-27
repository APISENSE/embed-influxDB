package io.apisense.embed.influx.execution;

import de.flapdoodle.embed.process.distribution.Distribution;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;
import io.apisense.embed.influx.execution.embed.InfluxExecutable;
import io.apisense.embed.influx.execution.embed.InfluxProcess;
import io.apisense.embed.influx.execution.embed.InfluxServerStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Implementation of {@link EmbeddedExecutor} wrapping an {@link InfluxExecutable} and {@link InfluxProcess}.
 */
public class InfluxExecutor implements EmbeddedExecutor {
    private static final Logger logger = LoggerFactory.getLogger(InfluxExecutor.class.getName());
    private InfluxExecutable preparedExecution;

    private final InfluxServerStarter serverStarter;
    private final InfluxExecutableConfig executableConfig;
    private final Distribution versionConfiguration;
    private InfluxProcess influxProcess;

    public InfluxExecutor(InfluxServerStarter starter, InfluxExecutableConfig executableConfig, Distribution versionConfiguration) {
        this.serverStarter = starter;
        this.executableConfig = executableConfig;
        this.versionConfiguration = versionConfiguration;
    }

    @Override
    public synchronized void prepare() {
        if (preparedExecution == null) {
            preparedExecution = serverStarter.prepare(executableConfig, versionConfiguration);
        }
    }

    @Override
    public synchronized void start() throws IOException {
        if (preparedExecution != null && influxProcess == null) {
            influxProcess = preparedExecution.start();
            logger.debug("Process started (id n°" + influxProcess.getProcessId() + ")");
        }
    }

    @Override
    public synchronized void stop() {
        if (influxProcess != null) {
            logger.debug("Stopping process (id n°" + influxProcess.getProcessId() + ")");
            influxProcess.stop();
            influxProcess = null;
        }
    }

    @Override
    public synchronized void cleanup() {
        if (preparedExecution != null) {
            preparedExecution.stop();
            preparedExecution = null;
        }
    }

    public InfluxProcess getInfluxProcess() {
        return influxProcess;
    }
}
