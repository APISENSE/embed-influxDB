package io.apisense.embed.influx.configuration;

/**
 * Available architectures for InfluxDB.
 */
public enum OSArchitecture {
    i386("i386"),
    x86_64("amd64"),
    ARM("armhf");

    public final String dlPath;

    OSArchitecture(String dlPath) {
        this.dlPath = dlPath;
    }

    /**
     * Return the {@link OSArchitecture} for the current system.
     *
     * @return The value of the currently running {@link OSArchitecture}.
     */
    public static OSArchitecture getCurrent() {
        String arch = System.getProperty("os.arch");
        if (arch.contains("64")) {
            return x86_64;
        }
        if (arch.contains("32")) {
            return i386;
        }
        if (arch.contains("arm")) {
            return ARM;
        }
        throw new UnknownArchitectureException(arch);
    }
}
