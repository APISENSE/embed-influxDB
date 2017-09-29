package io.apisense.embed.influx.configuration.embed;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.runtime.ICommandLinePostProcessor;
import de.flapdoodle.embed.process.store.IArtifactStore;

/**
 * Builder of a {@link IRuntimeConfig} to use with InfluxDB.
 */
public class InfluxRuntimeConfigBuilder {
    public final RuntimeConfigBuilder builder;

    /**
     * Create a new {@link RuntimeConfigBuilder} to use with InfluxDB.
     *
     * @param store The {@link IArtifactStore}.
     */
    public InfluxRuntimeConfigBuilder(IArtifactStore store) {
        this(store, "influxd");
    }

    /**
     * Create a new {@link RuntimeConfigBuilder} to use with InfluxDB.
     *
     * @param store      The {@link IArtifactStore}.
     * @param executable Process label.
     */
    public InfluxRuntimeConfigBuilder(IArtifactStore store, String executable) {
        builder = runtimeConfigBuilder(store, executable);
    }

    private static RuntimeConfigBuilder runtimeConfigBuilder(IArtifactStore store, String executable) {
        return new RuntimeConfigBuilder()
                .processOutput(ProcessOutput.getDefaultInstance(executable))
                .commandLinePostProcessor(new ICommandLinePostProcessor.Noop())
                .artifactStore(store);
    }

    public IRuntimeConfig getConfig() {
        return builder.build();
    }
}
