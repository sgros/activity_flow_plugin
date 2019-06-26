// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.database.sqlite.SQLiteFullException;
import java.io.Closeable;
import org.osmdroid.tileprovider.util.StreamUtils;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import android.graphics.drawable.Drawable;
import android.database.Cursor;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import org.osmdroid.config.Configuration;
import android.database.sqlite.SQLiteException;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.GarbageCollector;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import org.osmdroid.util.SplashScreenable;

public class SqlTileWriter implements IFilesystemCache, SplashScreenable
{
    private static boolean cleanOnStartup = true;
    protected static File db_file;
    private static final String[] expireQueryColumn;
    static boolean hasInited;
    protected static SQLiteDatabase mDb;
    private static final Object mLock;
    private static final String[] queryColumns;
    private final GarbageCollector garbageCollector;
    protected long lastSizeCheck;
    
    static {
        mLock = new Object();
        SqlTileWriter.hasInited = false;
        queryColumns = new String[] { "tile", "expires" };
        expireQueryColumn = new String[] { "expires" };
    }
    
    public SqlTileWriter() {
        this.lastSizeCheck = 0L;
        this.garbageCollector = new GarbageCollector(new Runnable() {
            @Override
            public void run() {
                SqlTileWriter.this.runCleanupOperation();
            }
        });
        this.getDb();
        if (!SqlTileWriter.hasInited) {
            SqlTileWriter.hasInited = true;
            if (SqlTileWriter.cleanOnStartup) {
                this.garbageCollector.gc();
            }
        }
    }
    
    private void createIndex(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS expires_index ON tiles (expires);");
    }
    
    public static long getIndex(final long n) {
        return getIndex(MapTileIndex.getX(n), MapTileIndex.getY(n), MapTileIndex.getZoom(n));
    }
    
    public static long getIndex(final long n, final long n2, final long n3) {
        final int n4 = (int)n3;
        return ((n3 << n4) + n << n4) + n2;
    }
    
    public static String[] getPrimaryKeyParameters(final long l, final String s) {
        return new String[] { String.valueOf(l), s };
    }
    
    public static String[] getPrimaryKeyParameters(final long n, final ITileSource tileSource) {
        return getPrimaryKeyParameters(n, tileSource.name());
    }
    
    public static boolean isFunctionalException(final SQLiteException ex) {
        final String simpleName = ex.getClass().getSimpleName();
        int n = 0;
        Label_0391: {
            switch (simpleName.hashCode()) {
                case 1939376593: {
                    if (simpleName.equals("SQLiteDatatypeMismatchException")) {
                        n = 3;
                        break Label_0391;
                    }
                    break;
                }
                case 1400520606: {
                    if (simpleName.equals("SQLiteDatabaseLockedException")) {
                        n = 11;
                        break Label_0391;
                    }
                    break;
                }
                case 1061155622: {
                    if (simpleName.equals("SQLiteConstraintException")) {
                        n = 2;
                        break Label_0391;
                    }
                    break;
                }
                case 666588538: {
                    if (simpleName.equals("SQLiteBlobTooBigException")) {
                        n = 1;
                        break Label_0391;
                    }
                    break;
                }
                case 532355648: {
                    if (simpleName.equals("SQLiteDiskIOException")) {
                        n = 12;
                        break Label_0391;
                    }
                    break;
                }
                case 325468747: {
                    if (simpleName.equals("SQLiteAbortException")) {
                        n = 7;
                        break Label_0391;
                    }
                    break;
                }
                case 124364321: {
                    if (simpleName.equals("SQLiteDoneException")) {
                        n = 13;
                        break Label_0391;
                    }
                    break;
                }
                case 20404371: {
                    if (simpleName.equals("SQLiteMisuseException")) {
                        n = 5;
                        break Label_0391;
                    }
                    break;
                }
                case -119599910: {
                    if (simpleName.equals("SQLiteCantOpenDatabaseException")) {
                        n = 9;
                        break Label_0391;
                    }
                    break;
                }
                case -669227773: {
                    if (simpleName.equals("SQLiteTableLockedException")) {
                        n = 6;
                        break Label_0391;
                    }
                    break;
                }
                case -672728977: {
                    if (simpleName.equals("SQLiteAccessPermException")) {
                        n = 8;
                        break Label_0391;
                    }
                    break;
                }
                case -1113540439: {
                    if (simpleName.equals("SQLiteDatabaseCorruptException")) {
                        n = 10;
                        break Label_0391;
                    }
                    break;
                }
                case -1115484154: {
                    if (simpleName.equals("SQLiteReadOnlyDatabaseException")) {
                        n = 15;
                        break Label_0391;
                    }
                    break;
                }
                case -1458338457: {
                    if (simpleName.equals("SQLiteBindOrColumnIndexOutOfRangeException")) {
                        n = 0;
                        break Label_0391;
                    }
                    break;
                }
                case -1764604492: {
                    if (simpleName.equals("SQLiteFullException")) {
                        n = 4;
                        break Label_0391;
                    }
                    break;
                }
                case -2070793707: {
                    if (simpleName.equals("SQLiteOutOfMemoryException")) {
                        n = 14;
                        break Label_0391;
                    }
                    break;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return false;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                return true;
            }
        }
    }
    
    protected void catchException(final Exception ex) {
        if (ex instanceof SQLiteException && !isFunctionalException((SQLiteException)ex)) {
            this.refreshDb();
        }
    }
    
    protected SQLiteDatabase getDb() {
        final SQLiteDatabase mDb = SqlTileWriter.mDb;
        if (mDb != null) {
            return mDb;
        }
        synchronized (SqlTileWriter.mLock) {
            Configuration.getInstance().getOsmdroidTileCache().mkdirs();
            final StringBuilder sb = new StringBuilder();
            sb.append(Configuration.getInstance().getOsmdroidTileCache().getAbsolutePath());
            sb.append(File.separator);
            sb.append("cache.db");
            SqlTileWriter.db_file = new File(sb.toString());
            if (SqlTileWriter.mDb == null) {
                try {
                    (SqlTileWriter.mDb = SQLiteDatabase.openOrCreateDatabase(SqlTileWriter.db_file, (SQLiteDatabase$CursorFactory)null)).execSQL("CREATE TABLE IF NOT EXISTS tiles (key INTEGER , provider TEXT, tile BLOB, expires INTEGER, PRIMARY KEY (key, provider));");
                }
                catch (Exception ex) {
                    Log.e("OsmDroid", "Unable to start the sqlite tile writer. Check external storage availability.", (Throwable)ex);
                    this.catchException(ex);
                    return null;
                }
            }
            return SqlTileWriter.mDb;
        }
    }
    
    public Cursor getTileCursor(final String[] array, final String[] array2) {
        return this.getDb().query("tiles", array2, "key=? and provider=?", array, (String)null, (String)null, (String)null);
    }
    
    public Drawable loadTile(final ITileSource tileSource, final long n) throws Exception {
        Drawable drawable = null;
        Object blob = null;
        byte[] array = null;
        try {
            try {
                Object tileCursor = this.getTileCursor(getPrimaryKeyParameters(getIndex(n), tileSource), SqlTileWriter.queryColumns);
                try {
                    final boolean moveToFirst = ((Cursor)tileCursor).moveToFirst();
                    boolean b = true;
                    long long1;
                    if (moveToFirst) {
                        blob = ((Cursor)tileCursor).getBlob(0);
                        long1 = ((Cursor)tileCursor).getLong(1);
                    }
                    else {
                        long1 = 0L;
                        blob = null;
                    }
                    if (blob == null) {
                        if (Configuration.getInstance().isDebugMode()) {
                            blob = new StringBuilder();
                            ((StringBuilder)blob).append("SqlCache - Tile doesn't exist: ");
                            ((StringBuilder)blob).append(tileSource.name());
                            ((StringBuilder)blob).append(MapTileIndex.toString(n));
                            Log.d("OsmDroid", ((StringBuilder)blob).toString());
                        }
                        if (tileCursor != null) {
                            ((Cursor)tileCursor).close();
                        }
                        return null;
                    }
                    if (tileCursor != null) {
                        ((Cursor)tileCursor).close();
                    }
                    Closeable closeable;
                    try {
                        tileCursor = new ByteArrayInputStream((byte[])blob);
                        try {
                            drawable = tileSource.getDrawable((InputStream)tileCursor);
                            if (long1 >= System.currentTimeMillis()) {
                                b = false;
                            }
                            if (b && drawable != null) {
                                if (Configuration.getInstance().isDebugMode()) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Tile expired: ");
                                    sb.append(tileSource.name());
                                    sb.append(MapTileIndex.toString(n));
                                    Log.d("OsmDroid", sb.toString());
                                }
                                ExpirableBitmapDrawable.setState(drawable, -2);
                            }
                            StreamUtils.closeStream((Closeable)tileCursor);
                            return drawable;
                        }
                        finally {}
                    }
                    finally {
                        closeable = null;
                    }
                    if (closeable != null) {
                        StreamUtils.closeStream(closeable);
                    }
                }
                catch (Exception blob) {}
            }
            finally {
                array = (byte[])blob;
            }
        }
        catch (Exception array) {}
        this.catchException((Exception)(Object)array);
        throw array;
        if (array != null) {
            ((Cursor)(Object)array).close();
        }
    }
    
    @Override
    public void onDetach() {
    }
    
    public void refreshDb() {
        synchronized (SqlTileWriter.mLock) {
            if (SqlTileWriter.mDb != null) {
                SqlTileWriter.mDb.close();
                SqlTileWriter.mDb = null;
            }
        }
    }
    
    public void runCleanupOperation() {
        final SQLiteDatabase db = this.getDb();
        if (db == null || !db.isOpen()) {
            if (Configuration.getInstance().isDebugMode()) {
                Log.d("OsmDroid", "Finished init thread, aborted due to null database reference");
            }
            return;
        }
        this.createIndex(db);
        final long length = SqlTileWriter.db_file.length();
        if (length <= Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
            return;
        }
        this.runCleanupOperation(length - Configuration.getInstance().getTileFileSystemCacheTrimBytes(), Configuration.getInstance().getTileGCBulkSize(), Configuration.getInstance().getTileGCBulkPauseInMillis(), true);
    }
    
    public void runCleanupOperation(long n, final int i, final long n2, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        final SQLiteDatabase db = this.getDb();
        int n3 = 1;
    Label_0047_Outer:
        while (true) {
            if (n <= 0L) {
                return;
            }
            while (true) {
                if (n3 != 0) {
                    n3 = 0;
                    break Label_0047;
                }
                if (n2 <= 0L) {
                    break Label_0047;
                }
                try {
                    Thread.sleep(n2);
                    final long currentTimeMillis = System.currentTimeMillis();
                    try {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("SELECT key,LENGTH(HEX(tile))/2 FROM tiles WHERE expires IS NOT NULL ");
                        final String s = "";
                        String string;
                        if (b) {
                            string = "";
                        }
                        else {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("AND expires < ");
                            sb3.append(currentTimeMillis);
                            sb3.append(" ");
                            string = sb3.toString();
                        }
                        sb2.append(string);
                        sb2.append("ORDER BY ");
                        sb2.append("expires");
                        sb2.append(" ASC LIMIT ");
                        sb2.append(i);
                        final Cursor rawQuery = db.rawQuery(sb2.toString(), (String[])null);
                        rawQuery.moveToFirst();
                        sb.setLength(0);
                        sb.append("key in (");
                        String s2 = "";
                        while (!rawQuery.isAfterLast()) {
                            final long long1 = rawQuery.getLong(0);
                            final long long2 = rawQuery.getLong(1);
                            rawQuery.moveToNext();
                            sb.append(s2);
                            sb.append(long1);
                            s2 = ",";
                            n -= long2;
                            if (n <= 0L) {
                                break;
                            }
                        }
                        rawQuery.close();
                        if (s.equals(s2)) {
                            return;
                        }
                        sb.append(')');
                        try {
                            db.delete("tiles", sb.toString(), (String[])null);
                        }
                        catch (Exception ex) {
                            this.catchException(ex);
                            return;
                        }
                        catch (SQLiteFullException ex2) {
                            Log.e("OsmDroid", "SQLiteFullException while cleanup.", (Throwable)ex2);
                            this.catchException((Exception)ex2);
                        }
                        continue Label_0047_Outer;
                    }
                    catch (Exception ex3) {
                        this.catchException(ex3);
                    }
                }
                catch (InterruptedException ex4) {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public boolean saveFile(final ITileSource p0, final long p1, final InputStream p2, final Long p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/osmdroid/tileprovider/modules/SqlTileWriter.getDb:()Landroid/database/sqlite/SQLiteDatabase;
        //     4: astore          6
        //     6: aload           6
        //     8: ifnull          543
        //    11: aload           6
        //    13: invokevirtual   android/database/sqlite/SQLiteDatabase.isOpen:()Z
        //    16: ifne            22
        //    19: goto            543
        //    22: aconst_null    
        //    23: astore          7
        //    25: aconst_null    
        //    26: astore          8
        //    28: aconst_null    
        //    29: astore          9
        //    31: aload           9
        //    33: astore          10
        //    35: new             Landroid/content/ContentValues;
        //    38: astore          11
        //    40: aload           9
        //    42: astore          10
        //    44: aload           11
        //    46: invokespecial   android/content/ContentValues.<init>:()V
        //    49: aload           9
        //    51: astore          10
        //    53: lload_2        
        //    54: invokestatic    org/osmdroid/tileprovider/modules/SqlTileWriter.getIndex:(J)J
        //    57: lstore          12
        //    59: aload           9
        //    61: astore          10
        //    63: aload           11
        //    65: ldc_w           "provider"
        //    68: aload_1        
        //    69: invokeinterface org/osmdroid/tileprovider/tilesource/ITileSource.name:()Ljava/lang/String;
        //    74: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/String;)V
        //    77: aload           9
        //    79: astore          10
        //    81: sipush          512
        //    84: newarray        B
        //    86: astore          14
        //    88: aload           9
        //    90: astore          10
        //    92: new             Ljava/io/ByteArrayOutputStream;
        //    95: astore          15
        //    97: aload           9
        //    99: astore          10
        //   101: aload           15
        //   103: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //   106: aload           4
        //   108: aload           14
        //   110: invokevirtual   java/io/InputStream.read:([B)I
        //   113: istore          16
        //   115: iload           16
        //   117: iconst_m1      
        //   118: if_icmpeq       134
        //   121: aload           15
        //   123: aload           14
        //   125: iconst_0       
        //   126: iload           16
        //   128: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
        //   131: goto            106
        //   134: aload           15
        //   136: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //   139: astore          4
        //   141: aload           11
        //   143: ldc_w           "key"
        //   146: lload           12
        //   148: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   151: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   154: aload           11
        //   156: ldc             "tile"
        //   158: aload           4
        //   160: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;[B)V
        //   163: aload           5
        //   165: ifnull          177
        //   168: aload           11
        //   170: ldc             "expires"
        //   172: aload           5
        //   174: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   177: aload           6
        //   179: ldc             "tiles"
        //   181: aconst_null    
        //   182: aload           11
        //   184: invokevirtual   android/database/sqlite/SQLiteDatabase.replaceOrThrow:(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
        //   187: pop2           
        //   188: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   191: invokeinterface org/osmdroid/config/IConfigurationProvider.isDebugMode:()Z
        //   196: ifeq            251
        //   199: new             Ljava/lang/StringBuilder;
        //   202: astore          4
        //   204: aload           4
        //   206: invokespecial   java/lang/StringBuilder.<init>:()V
        //   209: aload           4
        //   211: ldc_w           "tile inserted "
        //   214: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   217: pop            
        //   218: aload           4
        //   220: aload_1        
        //   221: invokeinterface org/osmdroid/tileprovider/tilesource/ITileSource.name:()Ljava/lang/String;
        //   226: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   229: pop            
        //   230: aload           4
        //   232: lload_2        
        //   233: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //   236: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   239: pop            
        //   240: ldc             "OsmDroid"
        //   242: aload           4
        //   244: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   247: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   250: pop            
        //   251: invokestatic    java/lang/System.currentTimeMillis:()J
        //   254: aload_0        
        //   255: getfield        org/osmdroid/tileprovider/modules/SqlTileWriter.lastSizeCheck:J
        //   258: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   261: invokeinterface org/osmdroid/config/IConfigurationProvider.getTileGCFrequencyInMillis:()J
        //   266: ladd           
        //   267: lcmp           
        //   268: ifle            286
        //   271: aload_0        
        //   272: invokestatic    java/lang/System.currentTimeMillis:()J
        //   275: putfield        org/osmdroid/tileprovider/modules/SqlTileWriter.lastSizeCheck:J
        //   278: aload_0        
        //   279: getfield        org/osmdroid/tileprovider/modules/SqlTileWriter.garbageCollector:Lorg/osmdroid/util/GarbageCollector;
        //   282: invokevirtual   org/osmdroid/util/GarbageCollector.gc:()Z
        //   285: pop            
        //   286: aload           15
        //   288: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //   291: goto            534
        //   294: astore_1       
        //   295: aload           15
        //   297: astore          10
        //   299: goto            536
        //   302: astore          5
        //   304: aload           15
        //   306: astore          4
        //   308: goto            329
        //   311: astore          4
        //   313: aload           15
        //   315: astore_1       
        //   316: goto            497
        //   319: astore_1       
        //   320: goto            536
        //   323: astore          5
        //   325: aload           7
        //   327: astore          4
        //   329: aload           4
        //   331: astore          10
        //   333: new             Ljava/lang/StringBuilder;
        //   336: astore          15
        //   338: aload           4
        //   340: astore          10
        //   342: aload           15
        //   344: invokespecial   java/lang/StringBuilder.<init>:()V
        //   347: aload           4
        //   349: astore          10
        //   351: aload           15
        //   353: ldc_w           "Unable to store cached tile from "
        //   356: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   359: pop            
        //   360: aload           4
        //   362: astore          10
        //   364: aload           15
        //   366: aload_1        
        //   367: invokeinterface org/osmdroid/tileprovider/tilesource/ITileSource.name:()Ljava/lang/String;
        //   372: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   375: pop            
        //   376: aload           4
        //   378: astore          10
        //   380: aload           15
        //   382: ldc_w           " "
        //   385: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   388: pop            
        //   389: aload           4
        //   391: astore          10
        //   393: aload           15
        //   395: lload_2        
        //   396: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //   399: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   402: pop            
        //   403: aload           4
        //   405: astore          10
        //   407: aload           15
        //   409: ldc_w           " db is "
        //   412: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   415: pop            
        //   416: aload           6
        //   418: ifnonnull       428
        //   421: ldc_w           "null"
        //   424: astore_1       
        //   425: goto            432
        //   428: ldc_w           "not null"
        //   431: astore_1       
        //   432: aload           4
        //   434: astore          10
        //   436: aload           15
        //   438: aload_1        
        //   439: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   442: pop            
        //   443: aload           4
        //   445: astore          10
        //   447: ldc             "OsmDroid"
        //   449: aload           15
        //   451: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   454: aload           5
        //   456: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   459: pop            
        //   460: aload           4
        //   462: astore          10
        //   464: getstatic       org/osmdroid/tileprovider/util/Counters.fileCacheSaveErrors:I
        //   467: iconst_1       
        //   468: iadd           
        //   469: putstatic       org/osmdroid/tileprovider/util/Counters.fileCacheSaveErrors:I
        //   472: aload           4
        //   474: astore          10
        //   476: aload_0        
        //   477: aload           5
        //   479: invokevirtual   org/osmdroid/tileprovider/modules/SqlTileWriter.catchException:(Ljava/lang/Exception;)V
        //   482: aload           4
        //   484: astore_1       
        //   485: aload_1        
        //   486: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //   489: goto            534
        //   492: astore          4
        //   494: aload           8
        //   496: astore_1       
        //   497: aload_1        
        //   498: astore          10
        //   500: ldc             "OsmDroid"
        //   502: ldc_w           "SQLiteFullException while saving tile."
        //   505: aload           4
        //   507: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   510: pop            
        //   511: aload_1        
        //   512: astore          10
        //   514: aload_0        
        //   515: getfield        org/osmdroid/tileprovider/modules/SqlTileWriter.garbageCollector:Lorg/osmdroid/util/GarbageCollector;
        //   518: invokevirtual   org/osmdroid/util/GarbageCollector.gc:()Z
        //   521: pop            
        //   522: aload_1        
        //   523: astore          10
        //   525: aload_0        
        //   526: aload           4
        //   528: invokevirtual   org/osmdroid/tileprovider/modules/SqlTileWriter.catchException:(Ljava/lang/Exception;)V
        //   531: goto            485
        //   534: iconst_0       
        //   535: ireturn        
        //   536: aload           10
        //   538: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //   541: aload_1        
        //   542: athrow         
        //   543: new             Ljava/lang/StringBuilder;
        //   546: dup            
        //   547: invokespecial   java/lang/StringBuilder.<init>:()V
        //   550: astore          4
        //   552: aload           4
        //   554: ldc_w           "Unable to store cached tile from "
        //   557: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   560: pop            
        //   561: aload           4
        //   563: aload_1        
        //   564: invokeinterface org/osmdroid/tileprovider/tilesource/ITileSource.name:()Ljava/lang/String;
        //   569: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   572: pop            
        //   573: aload           4
        //   575: ldc_w           " "
        //   578: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   581: pop            
        //   582: aload           4
        //   584: lload_2        
        //   585: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //   588: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   591: pop            
        //   592: aload           4
        //   594: ldc_w           ", database not available."
        //   597: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   600: pop            
        //   601: ldc             "OsmDroid"
        //   603: aload           4
        //   605: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   608: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   611: pop            
        //   612: getstatic       org/osmdroid/tileprovider/util/Counters.fileCacheSaveErrors:I
        //   615: iconst_1       
        //   616: iadd           
        //   617: putstatic       org/osmdroid/tileprovider/util/Counters.fileCacheSaveErrors:I
        //   620: iconst_0       
        //   621: ireturn        
        //   622: astore_1       
        //   623: goto            534
        //   626: astore          4
        //   628: goto            541
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  35     40     492    497    Landroid/database/sqlite/SQLiteFullException;
        //  35     40     323    329    Ljava/lang/Exception;
        //  35     40     319    323    Any
        //  44     49     492    497    Landroid/database/sqlite/SQLiteFullException;
        //  44     49     323    329    Ljava/lang/Exception;
        //  44     49     319    323    Any
        //  53     59     492    497    Landroid/database/sqlite/SQLiteFullException;
        //  53     59     323    329    Ljava/lang/Exception;
        //  53     59     319    323    Any
        //  63     77     492    497    Landroid/database/sqlite/SQLiteFullException;
        //  63     77     323    329    Ljava/lang/Exception;
        //  63     77     319    323    Any
        //  81     88     492    497    Landroid/database/sqlite/SQLiteFullException;
        //  81     88     323    329    Ljava/lang/Exception;
        //  81     88     319    323    Any
        //  92     97     492    497    Landroid/database/sqlite/SQLiteFullException;
        //  92     97     323    329    Ljava/lang/Exception;
        //  92     97     319    323    Any
        //  101    106    492    497    Landroid/database/sqlite/SQLiteFullException;
        //  101    106    323    329    Ljava/lang/Exception;
        //  101    106    319    323    Any
        //  106    115    311    319    Landroid/database/sqlite/SQLiteFullException;
        //  106    115    302    311    Ljava/lang/Exception;
        //  106    115    294    302    Any
        //  121    131    311    319    Landroid/database/sqlite/SQLiteFullException;
        //  121    131    302    311    Ljava/lang/Exception;
        //  121    131    294    302    Any
        //  134    163    311    319    Landroid/database/sqlite/SQLiteFullException;
        //  134    163    302    311    Ljava/lang/Exception;
        //  134    163    294    302    Any
        //  168    177    311    319    Landroid/database/sqlite/SQLiteFullException;
        //  168    177    302    311    Ljava/lang/Exception;
        //  168    177    294    302    Any
        //  177    251    311    319    Landroid/database/sqlite/SQLiteFullException;
        //  177    251    302    311    Ljava/lang/Exception;
        //  177    251    294    302    Any
        //  251    286    311    319    Landroid/database/sqlite/SQLiteFullException;
        //  251    286    302    311    Ljava/lang/Exception;
        //  251    286    294    302    Any
        //  286    291    622    626    Ljava/io/IOException;
        //  333    338    319    323    Any
        //  342    347    319    323    Any
        //  351    360    319    323    Any
        //  364    376    319    323    Any
        //  380    389    319    323    Any
        //  393    403    319    323    Any
        //  407    416    319    323    Any
        //  436    443    319    323    Any
        //  447    460    319    323    Any
        //  464    472    319    323    Any
        //  476    482    319    323    Any
        //  485    489    622    626    Ljava/io/IOException;
        //  500    511    319    323    Any
        //  514    522    319    323    Any
        //  525    531    319    323    Any
        //  536    541    626    631    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0286:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
