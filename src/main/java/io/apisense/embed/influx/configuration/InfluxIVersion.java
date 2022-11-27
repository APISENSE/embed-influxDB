package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.IVersion;

/**
 * Extends {@link IVersion} with Influx specific data.
 */
public interface InfluxIVersion extends IVersion {
    ReleaseType getReleaseType();
}
