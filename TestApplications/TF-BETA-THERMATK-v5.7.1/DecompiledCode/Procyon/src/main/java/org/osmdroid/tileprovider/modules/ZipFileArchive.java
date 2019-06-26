// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.util.Enumeration;
import android.util.Log;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.io.IOException;
import org.osmdroid.util.MapTileIndex;
import java.util.zip.ZipFile;

public class ZipFileArchive implements IArchiveFile
{
    private boolean mIgnoreTileSource;
    protected ZipFile mZipFile;
    
    public ZipFileArchive() {
        this.mIgnoreTileSource = false;
    }
    
    private String getTileRelativeFilenameString(final long n, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append('/');
        sb.append(MapTileIndex.getZoom(n));
        sb.append('/');
        sb.append(MapTileIndex.getX(n));
        sb.append('/');
        sb.append(MapTileIndex.getY(n));
        sb.append(".png");
        return sb.toString();
    }
    
    @Override
    public void close() {
        try {
            this.mZipFile.close();
        }
        catch (IOException ex) {}
    }
    
    @Override
    public InputStream getInputStream(final ITileSource tileSource, final long n) {
        try {
            if (this.mIgnoreTileSource) {
                final Enumeration<? extends ZipEntry> entries = this.mZipFile.entries();
                while (entries.hasMoreElements()) {
                    final String name = ((ZipEntry)entries.nextElement()).getName();
                    if (name.contains("/")) {
                        final ZipEntry entry = this.mZipFile.getEntry(this.getTileRelativeFilenameString(n, name.split("/")[0]));
                        if (entry != null) {
                            return this.mZipFile.getInputStream(entry);
                        }
                        continue;
                    }
                }
                return null;
            }
            final ZipEntry entry2 = this.mZipFile.getEntry(tileSource.getTileRelativeFilenameString(n));
            if (entry2 != null) {
                return this.mZipFile.getInputStream(entry2);
            }
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error getting zip stream: ");
            sb.append(MapTileIndex.toString(n));
            Log.w("OsmDroid", sb.toString(), (Throwable)ex);
        }
        return null;
    }
    
    @Override
    public void init(final File file) throws Exception {
        this.mZipFile = new ZipFile(file);
    }
    
    @Override
    public void setIgnoreTileSource(final boolean mIgnoreTileSource) {
        this.mIgnoreTileSource = mIgnoreTileSource;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ZipFileArchive [mZipFile=");
        sb.append(this.mZipFile.getName());
        sb.append("]");
        return sb.toString();
    }
}
