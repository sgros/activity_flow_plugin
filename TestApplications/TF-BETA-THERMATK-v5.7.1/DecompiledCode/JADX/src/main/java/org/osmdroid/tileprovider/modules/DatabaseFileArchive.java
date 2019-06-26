package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class DatabaseFileArchive implements IArchiveFile {
    static final String[] tile_column = new String[]{"tile"};
    private SQLiteDatabase mDatabase;
    private boolean mIgnoreTileSource = false;

    public void setIgnoreTileSource(boolean z) {
        this.mIgnoreTileSource = z;
    }

    public void init(File file) throws Exception {
        this.mDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, 17);
    }

    public byte[] getImage(ITileSource iTileSource, long j) {
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        String str = "OsmDroid";
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(str, "Skipping DatabaseFileArchive lookup, database is closed");
            }
            return null;
        }
        try {
            Cursor query;
            byte[] blob;
            String[] strArr = new String[]{"tile"};
            long y = (long) MapTileIndex.getY(j);
            long zoom = (long) MapTileIndex.getZoom(j);
            int i = (int) zoom;
            long x = (((zoom << i) + ((long) MapTileIndex.getX(j))) << i) + y;
            String str2 = "key = ";
            if (this.mIgnoreTileSource) {
                SQLiteDatabase sQLiteDatabase2 = this.mDatabase;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(x);
                query = sQLiteDatabase2.query("tiles", strArr, stringBuilder.toString(), null, null, null, null);
            } else {
                SQLiteDatabase sQLiteDatabase3 = this.mDatabase;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(x);
                stringBuilder2.append(" and ");
                stringBuilder2.append("provider");
                stringBuilder2.append(" = ?");
                query = sQLiteDatabase3.query("tiles", strArr, stringBuilder2.toString(), new String[]{iTileSource.name()}, null, null, null);
            }
            if (query.getCount() != 0) {
                query.moveToFirst();
                blob = query.getBlob(0);
            } else {
                blob = null;
            }
            query.close();
            if (blob != null) {
                return blob;
            }
            return null;
        } catch (Throwable th) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Error getting db stream: ");
            stringBuilder3.append(MapTileIndex.toString(j));
            Log.w(str, stringBuilder3.toString(), th);
        }
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        try {
            byte[] image = getImage(iTileSource, j);
            InputStream byteArrayInputStream = image != null ? new ByteArrayInputStream(image) : null;
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
