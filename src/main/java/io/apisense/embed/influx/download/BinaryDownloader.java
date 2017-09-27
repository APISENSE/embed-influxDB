package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.VersionConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Available operations to download an InfluxDB binary archive.
 */
public interface BinaryDownloader {

    /**
     * Download the right version of InfluxDB if necessary,
     * then return a {@link File} referencing the server binary.
     *
     * @param configuration Server's version description.
     * @return The server binary.
     * @throws IOException If anything goes wrong during download.
     */
    File download(VersionConfiguration configuration) throws IOException;
}
