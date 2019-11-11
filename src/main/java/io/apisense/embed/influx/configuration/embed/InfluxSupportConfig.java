package io.apisense.embed.influx.configuration.embed;

import de.flapdoodle.embed.process.config.ISupportConfig;

import java.util.concurrent.TimeUnit;

class InfluxSupportConfig implements ISupportConfig {
    static final InfluxSupportConfig instance = new InfluxSupportConfig();

    @Override
    public String getName() {
        return "influx";
    }

    @Override
    public String getSupportUrl() {
        return "https://github.com/APISENSE/embed-influxDB";
    }

    @Override
    public long maxStopTimeoutMillis() {
        return TimeUnit.SECONDS.toMillis(5);
    }

    @Override
    public String messageOnException(Class<?> context, Exception exception) {
        return null;
    }
}
