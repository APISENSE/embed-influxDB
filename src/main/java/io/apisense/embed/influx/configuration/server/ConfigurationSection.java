package io.apisense.embed.influx.configuration.server;

import java.util.Map;

/**
 * Define the behavior of an InfluxDB server configuration section.
 */
public interface ConfigurationSection {

    /**
     * Return the section name.
     *
     * @return The section name.
     */
    String getName();

    /**
     * Return the section content.
     *
     * @return A {@link Map} containing the {@link ConfigurationProperty} linked to their values.
     */
    Map<ConfigurationProperty, Object> getConfiguration();

    /**
     * Add a property to the current map.
     *
     * @param property The {@link ConfigurationProperty} to add.
     * @param value    The value to link to this property.
     */
    void addProperty(ConfigurationProperty property, Object value);
}
