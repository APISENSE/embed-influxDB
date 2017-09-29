package io.apisense.embed.influx.configuration;

import de.flapdoodle.embed.process.distribution.BitSize;

/**
 * Available architectures for InfluxDB.
 */
public enum OSArchitecture {
    i386("i386", BitSize.B32),
    x86_64("amd64", BitSize.B64),
    ARM("armhf", BitSize.B32); //TODO: See if there is any issue with arm and BitSize?

    public final String dlPath;
    public final BitSize bitSize;

    OSArchitecture(String dlPath, BitSize bitSize) {
        this.dlPath = dlPath;
        this.bitSize = bitSize;
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
        throw new IllegalArgumentException("Unable to find architecture: " + arch);
    }

    public BitSize toBitSize() {
        return bitSize;
    }
}
