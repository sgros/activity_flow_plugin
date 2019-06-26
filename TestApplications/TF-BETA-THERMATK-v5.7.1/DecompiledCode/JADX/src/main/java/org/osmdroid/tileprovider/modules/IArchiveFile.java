package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IArchiveFile {
    void close();

    InputStream getInputStream(ITileSource iTileSource, long j);

    void init(File file) throws Exception;

    void setIgnoreTileSource(boolean z);
}
