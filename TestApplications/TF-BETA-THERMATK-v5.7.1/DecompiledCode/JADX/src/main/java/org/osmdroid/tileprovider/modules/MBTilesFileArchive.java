package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class MBTilesFileArchive implements IArchiveFile {
    private SQLiteDatabase mDatabase;

    public void setIgnoreTileSource(boolean z) {
    }

    public void init(File file) throws Exception {
        this.mDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, 17);
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        try {
            InputStream byteArrayInputStream;
            String[] strArr = new String[]{"tile_data"};
            String[] strArr2 = new String[3];
            strArr2[0] = Integer.toString(MapTileIndex.getX(j));
            double pow = Math.pow(2.0d, (double) MapTileIndex.getZoom(j));
            double y = (double) MapTileIndex.getY(j);
            Double.isNaN(y);
            strArr2[1] = Double.toString((pow - y) - 1.0d);
            strArr2[2] = Integer.toString(MapTileIndex.getZoom(j));
            Cursor query = this.mDatabase.query("tiles", strArr, "tile_column=? and tile_row=? and zoom_level=?", strArr2, null, null, null);
            if (query.getCount() != 0) {
                query.moveToFirst();
                byteArrayInputStream = new ByteArrayInputStream(query.getBlob(0));
            } else {
                byteArrayInputStream = null;
            }
            query.close();
            if (byteArrayInputStream != null) {
                return byteArrayInputStream;
            }
            return null;
        } catch (Throwable th) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error getting db stream: ");
            stringBuilder.append(MapTileIndex.toString(j));
            Log.w("OsmDroid", stringBuilder.toString(), th);
        }
    }

    public void close() {
        this.mDatabase.close();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DatabaseFileArchive [mDatabase=");
        stringBuilder.append(this.mDatabase.getPath());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
