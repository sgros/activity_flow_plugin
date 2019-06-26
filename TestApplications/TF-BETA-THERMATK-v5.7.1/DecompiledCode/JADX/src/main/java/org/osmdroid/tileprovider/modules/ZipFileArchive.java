package org.osmdroid.tileprovider.modules;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class ZipFileArchive implements IArchiveFile {
    private boolean mIgnoreTileSource = false;
    protected ZipFile mZipFile;

    public void setIgnoreTileSource(boolean z) {
        this.mIgnoreTileSource = z;
    }

    public void init(File file) throws Exception {
        this.mZipFile = new ZipFile(file);
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        String str = "/";
        try {
            if (this.mIgnoreTileSource) {
                Enumeration entries = this.mZipFile.entries();
                while (entries.hasMoreElements()) {
                    String name = ((ZipEntry) entries.nextElement()).getName();
                    if (name.contains(str)) {
                        ZipEntry entry = this.mZipFile.getEntry(getTileRelativeFilenameString(j, name.split(str)[0]));
                        if (entry != null) {
                            return this.mZipFile.getInputStream(entry);
                        }
                    }
                }
            } else {
                ZipEntry entry2 = this.mZipFile.getEntry(iTileSource.getTileRelativeFilenameString(j));
                if (entry2 != null) {
                    return this.mZipFile.getInputStream(entry2);
                }
            }
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error getting zip stream: ");
            stringBuilder.append(MapTileIndex.toString(j));
            Log.w("OsmDroid", stringBuilder.toString(), e);
        }
        return null;
    }

    private String getTileRelativeFilenameString(long j, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append('/');
        stringBuilder.append(MapTileIndex.getZoom(j));
        stringBuilder.append('/');
        stringBuilder.append(MapTileIndex.getX(j));
        stringBuilder.append('/');
        stringBuilder.append(MapTileIndex.getY(j));
        stringBuilder.append(".png");
        return stringBuilder.toString();
    }

    public void close() {
        try {
            this.mZipFile.close();
        } catch (IOException unused) {
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ZipFileArchive [mZipFile=");
        stringBuilder.append(this.mZipFile.getName());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
