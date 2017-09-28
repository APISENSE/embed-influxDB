package io.apisense.embed.influx.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Default implementation of a {@link ProcessExecutor}, using {@link Runtime} to start server.
 */
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
        logger.debug("Executing command: " + command);
        return runtime.exec(command);
    }

    @Override
    public void stopProcess() {
        if (currentProcess != null) {
            String errorMessage = readErrorStream();
            if (!errorMessage.isEmpty()) {
                logger.warn("Runtime error: " + errorMessage);
            }
            currentProcess.destroy();
            currentProcess = null;
        }
    }

    private String readErrorStream() {
        final char[] buffer = new char[4096];
        final StringBuilder content = new StringBuilder();
        InputStream errorStream = currentProcess.getErrorStream();
        try {
            Reader in = new InputStreamReader(errorStream, "UTF-8");
            int rsz;
            while ((rsz = in.read(buffer, 0, buffer.length)) != -1) {
                content.append(buffer, 0, rsz);
            }
        } catch (IOException e) {
            logger.warn("Unable to open error stream", e);
        }

        try {
            errorStream.close();
        } catch (IOException e) {
            logger.warn("Unable to close error stream", e);
        }
        return content.toString();
    }
}
