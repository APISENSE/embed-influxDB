package io.apisense.embed.influx.execution.embed;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import de.flapdoodle.embed.process.runtime.Starter;
import io.apisense.embed.influx.configuration.embed.InfluxExecutableConfig;

public class InfluxServerStarter extends Starter<InfluxExecutableConfig, InfluxExecutable, InfluxProcess> {
    public InfluxServerStarter(IRuntimeConfig config) {
        super(config);
    }

    @Override
    protected InfluxExecutable newExecutable(InfluxExecutableConfig config, Distribution distribution,
                                             IRuntimeConfig runtime, IExtractedFileSet exe) {
        return new InfluxExecutable(distribution, config, runtime, exe);
    }
}
