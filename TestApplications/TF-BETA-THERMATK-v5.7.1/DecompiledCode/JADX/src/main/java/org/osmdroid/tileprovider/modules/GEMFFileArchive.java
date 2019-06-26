package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GEMFFile;
import org.osmdroid.util.MapTileIndex;

public class GEMFFileArchive implements IArchiveFile {
    private GEMFFile mFile;

    public void setIgnoreTileSource(boolean z) {
    }

    public void init(File file) throws Exception {
        this.mFile = new GEMFFile(file);
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        return this.mFile.getInputStream(MapTileIndex.getX(j), MapTileIndex.getY(j), MapTileIndex.getZoom(j));
    }

    public void close() {
        try {
            this.mFile.close();
        } catch (IOException unused) {
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GEMFFileArchive [mGEMFFile=");
        stringBuilder.append(this.mFile.getName());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
