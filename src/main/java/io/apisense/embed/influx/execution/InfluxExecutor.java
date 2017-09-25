package io.apisense.embed.influx.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class InfluxExecutor implements ProcessExecutor {
    private static final Logger logger = LoggerFactory.getLogger(InfluxExecutor.class.getName());

    private Process currentProcess;
    private Runtime runtime;

    public InfluxExecutor() {
        this(Runtime.getRuntime());
    }

    InfluxExecutor(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void startProcess(File binary) throws IOException {
        currentProcess = start(binary.getAbsolutePath() + " run");
    }

    @Override
    public void startProcess(File binary, File config) throws IOException {
        currentProcess = start(binary.getAbsolutePath() + " run -config " + config.getAbsolutePath());
    }

    private Process start(String command) throws IOException {
        if (currentProcess != null) {
            return currentProcess;
        }
        logger.trace("Executing command: " + command);
        return runtime.exec(command);
    }

    @Override
    public void stopProcess() {
        if (currentProcess != null) {
            currentProcess.destroy();
            currentProcess = null;
        }
    }
}
