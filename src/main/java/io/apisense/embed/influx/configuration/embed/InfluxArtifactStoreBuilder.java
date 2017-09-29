package io.apisense.embed.influx.configuration.embed;

import de.flapdoodle.embed.process.config.store.IDownloadConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.io.directories.PropertyOrPlatformTempDir;
import de.flapdoodle.embed.process.io.directories.UserHome;
import de.flapdoodle.embed.process.store.Downloader;
import de.flapdoodle.embed.process.store.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.process.store.IArtifactStore;

import java.io.File;

/**
 * Builder of a {@link IArtifactStore} to use with InfluxDB.
 */
public class InfluxArtifactStoreBuilder {
    private static final String DEFAULT_PATH = ".embedded-influx" + File.separator + "extracted";
    public final ExtractedArtifactStoreBuilder builder;

    /**
     * Create a new {@link ExtractedArtifactStoreBuilder} to use with InfluxDB.
     *
     * @param downloadConfig The {@link IDownloadConfig}.
     * @param extractDir     The file to store artifacts into.
     *                       Default to ~/.embedded-influx/extracted if null.
     */
    public InfluxArtifactStoreBuilder(IDownloadConfig downloadConfig, File extractDir) {
        this(downloadConfig, extractDir != null ? new FixedPath(extractDir.getAbsolutePath()) : new UserHome(DEFAULT_PATH));
    }

    private InfluxArtifactStoreBuilder(IDownloadConfig downloadConfig, IDirectory extractDir) {
        builder = storeBuilder(downloadConfig, extractDir);
    }

    private static ExtractedArtifactStoreBuilder storeBuilder(IDownloadConfig downloadConfig, IDirectory extractDir) {
        return new ExtractedArtifactStoreBuilder()
                .extractDir(extractDir)
                .extractExecutableNaming(new OriginNaming())
                .tempDir(new PropertyOrPlatformTempDir())
                .executableNaming(new UUIDTempNaming())
                .download(downloadConfig)
                .downloader(new Downloader());
    }

    public IArtifactStore getConfig() {
        return builder.build();
    }

    private static final class OriginNaming implements ITempNaming {
        @Override
        public String nameFor(String prefix, String postfix) {
            return postfix;
        }

    }
}
