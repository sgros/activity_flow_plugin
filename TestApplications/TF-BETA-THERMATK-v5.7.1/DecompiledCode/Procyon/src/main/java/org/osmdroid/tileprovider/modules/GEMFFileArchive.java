// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import java.io.File;
import org.osmdroid.util.MapTileIndex;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.io.IOException;
import org.osmdroid.util.GEMFFile;

public class GEMFFileArchive implements IArchiveFile
{
    private GEMFFile mFile;
    
    @Override
    public void close() {
        try {
            this.mFile.close();
        }
        catch (IOException ex) {}
    }
    
    @Override
    public InputStream getInputStream(final ITileSource tileSource, final long n) {
        return this.mFile.getInputStream(MapTileIndex.getX(n), MapTileIndex.getY(n), MapTileIndex.getZoom(n));
    }
    
    @Override
    public void init(final File file) throws Exception {
        this.mFile = new GEMFFile(file);
    }
    
    @Override
    public void setIgnoreTileSource(final boolean b) {
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GEMFFileArchive [mGEMFFile=");
        sb.append(this.mFile.getName());
        sb.append("]");
        return sb.toString();
    }
}
