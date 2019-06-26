// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.database.sqlite.SQLiteDatabase$CursorFactory;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import android.database.Cursor;
import org.osmdroid.config.Configuration;
import android.util.Log;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseFileArchive implements IArchiveFile
{
    static final String[] tile_column;
    private SQLiteDatabase mDatabase;
    private boolean mIgnoreTileSource;
    
    static {
        tile_column = new String[] { "tile" };
    }
    
    public DatabaseFileArchive() {
        this.mIgnoreTileSource = false;
    }
    
    @Override
    public void close() {
        this.mDatabase.close();
    }
    
    public byte[] getImage(final ITileSource tileSource, final long n) {
        final SQLiteDatabase mDatabase = this.mDatabase;
        if (mDatabase != null && mDatabase.isOpen()) {
            try {
                final String[] array = { "tile" };
                final long n2 = MapTileIndex.getX(n);
                final long n3 = MapTileIndex.getY(n);
                final long n4 = MapTileIndex.getZoom(n);
                final int n5 = (int)n4;
                final long n6 = ((n4 << n5) + n2 << n5) + n3;
                Cursor cursor;
                if (!this.mIgnoreTileSource) {
                    final SQLiteDatabase mDatabase2 = this.mDatabase;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("key = ");
                    sb.append(n6);
                    sb.append(" and ");
                    sb.append("provider");
                    sb.append(" = ?");
                    cursor = mDatabase2.query("tiles", array, sb.toString(), new String[] { tileSource.name() }, (String)null, (String)null, (String)null);
                }
                else {
                    final SQLiteDatabase mDatabase3 = this.mDatabase;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("key = ");
                    sb2.append(n6);
                    cursor = mDatabase3.query("tiles", array, sb2.toString(), (String[])null, (String)null, (String)null, (String)null);
                }
                byte[] blob;
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    blob = cursor.getBlob(0);
                }
                else {
                    blob = null;
                }
                cursor.close();
                if (blob != null) {
                    return blob;
                }
            }
            catch (Throwable t) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Error getting db stream: ");
                sb3.append(MapTileIndex.toString(n));
                Log.w("OsmDroid", sb3.toString(), t);
            }
            return null;
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            Log.d("OsmDroid", "Skipping DatabaseFileArchive lookup, database is closed");
        }
        return null;
    }
    
    @Override
    public InputStream getInputStream(final ITileSource tileSource, final long n) {
        try {
            final byte[] image = this.getImage(tileSource, n);
            InputStream inputStream;
            if (image != null) {
                inputStream = new ByteArrayInputStream(image);
            }
            else {
                inputStream = null;
            }
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
    public void setIgnoreTileSource(final boolean mIgnoreTileSource) {
        this.mIgnoreTileSource = mIgnoreTileSource;
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
