// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.database.sqlite.SQLiteDatabase$CursorFactory;
import java.io.File;
import android.database.Cursor;
import android.util.Log;
import java.io.ByteArrayInputStream;
import org.osmdroid.util.MapTileIndex;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import android.database.sqlite.SQLiteDatabase;

public class MBTilesFileArchive implements IArchiveFile
{
    private SQLiteDatabase mDatabase;
    
    @Override
    public void close() {
        this.mDatabase.close();
    }
    
    @Override
    public InputStream getInputStream(final ITileSource tileSource, final long n) {
        try {
            final String string = Integer.toString(MapTileIndex.getX(n));
            final double pow = Math.pow(2.0, MapTileIndex.getZoom(n));
            final double v = MapTileIndex.getY(n);
            Double.isNaN(v);
            final Cursor query = this.mDatabase.query("tiles", new String[] { "tile_data" }, "tile_column=? and tile_row=? and zoom_level=?", new String[] { string, Double.toString(pow - v - 1.0), Integer.toString(MapTileIndex.getZoom(n)) }, (String)null, (String)null, (String)null);
            InputStream inputStream;
            if (query.getCount() != 0) {
                query.moveToFirst();
                inputStream = new ByteArrayInputStream(query.getBlob(0));
            }
            else {
                inputStream = null;
            }
            query.close();
            if (inputStream != null) {
                return inputStream;
            }
        }
        catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error getting db stream: ");
            sb.append(MapTileIndex.toString(n));
            Log.w("OsmDroid", sb.toString(), t);
        }
        return null;
    }
    
    @Override
    public void init(final File file) throws Exception {
        this.mDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), (SQLiteDatabase$CursorFactory)null, 17);
    }
    
    @Override
    public void setIgnoreTileSource(final boolean b) {
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DatabaseFileArchive [mDatabase=");
        sb.append(this.mDatabase.getPath());
        sb.append("]");
        return sb.toString();
    }
}
