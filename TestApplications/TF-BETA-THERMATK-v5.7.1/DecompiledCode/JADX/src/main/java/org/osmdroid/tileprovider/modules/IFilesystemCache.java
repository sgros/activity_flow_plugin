package org.osmdroid.tileprovider.modules;

import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IFilesystemCache {
    void onDetach();

    boolean saveFile(ITileSource iTileSource, long j, InputStream inputStream, Long l);
}
