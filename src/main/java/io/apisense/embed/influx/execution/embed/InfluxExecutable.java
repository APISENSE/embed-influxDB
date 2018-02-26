package io.apisense.embed.influx.execution.embed;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import de.flapdoodle.embed.process.runtime.Executable;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;

import java.io.IOException;

public class InfluxExecutable extends Executable<InfluxExecutableConfig, InfluxProcess> {

    InfluxExecutable(Distribution distribution,
                     InfluxExecutableConfig config,
                     IRuntimeConfig runtimeConfig,
                     IExtractedFileSet executable) {
        super(distribution, config, runtimeConfig, executable);
    }

    @Override
    protected InfluxProcess start(Distribution distribution,
                                  InfluxExecutableConfig config,
                                  IRuntimeConfig runtime) throws IOException {
        return new InfluxProcess(distribution, config, runtime, this);
    }
}
