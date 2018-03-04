package io.apisense.embed.influx.configuration.server;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This defines the {@link ConfigurationSection} default behavior.
 * The properties are ordered as first in, first printed.
 * <p>
 * Two {@link AbstractConfigurationSection}s are equals if they share the same section name.
 */
public abstract class AbstractConfigurationSection implements ConfigurationSection {
    private final String sectionName;
    private final Map<ConfigurationProperty, Object> config;

    protected AbstractConfigurationSection(String sectionName) {
        this.sectionName = sectionName;
        config = new LinkedHashMap<>();
    }

    @Override
    public String getName() {
        return sectionName;
    }

    @Override
    public Map<ConfigurationProperty, Object> getConfiguration() {
        return config;
    }

    @Override
    public void addProperty(ConfigurationProperty property, Object value) {
        this.config.put(property, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractConfigurationSection that = (AbstractConfigurationSection) o;
        return Objects.equals(sectionName, that.sectionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionName);
    }
}
