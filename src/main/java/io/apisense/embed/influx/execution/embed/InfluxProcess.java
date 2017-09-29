package io.apisense.embed.influx.execution.embed;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import de.flapdoodle.embed.process.io.file.Files;
import de.flapdoodle.embed.process.runtime.AbstractProcess;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfluxProcess extends AbstractProcess<InfluxExecutableConfig, InfluxExecutable, InfluxProcess> {
    private static final Logger logger = LoggerFactory.getLogger(InfluxProcess.class.getName());

    private File configFile;
    private File dataPath;

    InfluxProcess(Distribution distribution, InfluxExecutableConfig config,
                  IRuntimeConfig runtimeConfig, InfluxExecutable executable) throws IOException {
        super(distribution, config, runtimeConfig, executable); // Calls getCommandLine
    }

    @Override
    protected List<String> getCommandLine(Distribution distribution, InfluxExecutableConfig config,
                                          IExtractedFileSet exe) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(Files.fileOf(exe.baseDir(), exe.executable()).getAbsolutePath());
        commands.add("run");
        if (config.configurationWriter() != null) {
            dataPath = config.configurationWriter().getDataPath();
            configFile = config.configurationWriter().writeFile();
            logger.debug("Using config file " + configFile.getAbsolutePath());
            commands.add("-config=" + configFile.getAbsolutePath());
        }
        return commands;
    }

    @Override
    protected void stopInternal() {
        //TODO: Wait until new release fixing https://github.com/flapdoodle-oss/de.flapdoodle.embed.process/issues/62
        setProcessId(getProcessId());
        if (!sendTermToProcess()) {
            logger.warn("SIGTERM not working, trying SIGINT (id n°" + getProcessId() + ")");
            if (!sendKillToProcess()) {
                logger.warn("SIGINT not working! trying Windows kill (?) (id n°" + getProcessId() + ")");
                if (!tryKillToProcess()) {
                    logger.warn("Unable to close process (id n°" + getProcessId() + ")");
                }
            }
        }
        stopProcess();
    }


    @Override
    protected void cleanupInternal() {
        if (configFile != null) {
            if (!(configFile.delete())) {
                logger.warn("Unable to remove log file: " + configFile.getAbsolutePath());
            }
            configFile = null;
        }
        if (dataPath != null) {
            if (!Files.forceDelete(dataPath)) {
                logger.warn("Unable to remove data dir: " + configFile.getAbsolutePath());
            }
        }
    }
}
