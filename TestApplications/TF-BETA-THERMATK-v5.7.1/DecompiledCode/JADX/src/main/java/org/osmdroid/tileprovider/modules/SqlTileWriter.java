package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;
import java.io.File;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GarbageCollector;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.SplashScreenable;

public class SqlTileWriter implements IFilesystemCache, SplashScreenable {
    private static boolean cleanOnStartup = true;
    protected static File db_file;
    private static final String[] expireQueryColumn = new String[]{"expires"};
    static boolean hasInited = false;
    protected static SQLiteDatabase mDb;
    private static final Object mLock = new Object();
    private static final String[] queryColumns;
    private final GarbageCollector garbageCollector = new GarbageCollector(new C02561());
    protected long lastSizeCheck = 0;

    /* renamed from: org.osmdroid.tileprovider.modules.SqlTileWriter$1 */
    class C02561 implements Runnable {
        C02561() {
        }

        public void run() {
            SqlTileWriter.this.runCleanupOperation();
        }
    }

    public static long getIndex(long j, long j2, long j3) {
        int i = (int) j3;
        return (((j3 << i) + j) << i) + j2;
    }

    public void onDetach() {
    }

    static {
        String[] strArr = new String[2];
        strArr[0] = "tile";
        strArr[1] = "expires";
        queryColumns = strArr;
    }

    public SqlTileWriter() {
        getDb();
        if (!hasInited) {
            hasInited = true;
            if (cleanOnStartup) {
                this.garbageCollector.mo4115gc();
            }
        }
    }

    public void runCleanupOperation() {
        SQLiteDatabase db = getDb();
        if (db == null || !db.isOpen()) {
            if (Configuration.getInstance().isDebugMode()) {
                Log.d("OsmDroid", "Finished init thread, aborted due to null database reference");
            }
            return;
        }
        createIndex(db);
        long length = db_file.length();
        if (length > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
            runCleanupOperation(length - Configuration.getInstance().getTileFileSystemCacheTrimBytes(), Configuration.getInstance().getTileGCBulkSize(), Configuration.getInstance().getTileGCBulkPauseInMillis(), true);
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:40:0x00f4=Splitter:B:40:0x00f4, B:31:0x00b7=Splitter:B:31:0x00b7} */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x00a7=Splitter:B:21:0x00a7, B:37:0x00ef=Splitter:B:37:0x00ef} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00da A:{Catch:{ all -> 0x00b3 }} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d7 A:{Catch:{ all -> 0x00b3 }} */
    public boolean saveFile(org.osmdroid.tileprovider.tilesource.ITileSource r17, long r18, java.io.InputStream r20, java.lang.Long r21) {
        /*
        r16 = this;
        r1 = r16;
        r0 = r21;
        r2 = r16.getDb();
        r3 = " ";
        r4 = "Unable to store cached tile from ";
        r5 = 0;
        r6 = "OsmDroid";
        if (r2 == 0) goto L_0x0107;
    L_0x0011:
        r7 = r2.isOpen();
        if (r7 != 0) goto L_0x0019;
    L_0x0017:
        goto L_0x0107;
    L_0x0019:
        r7 = 0;
        r8 = new android.content.ContentValues;	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r8.<init>();	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r9 = getIndex(r18);	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r11 = "provider";
        r12 = r17.name();	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r8.put(r11, r12);	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r11 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r11 = new byte[r11];	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r12 = new java.io.ByteArrayOutputStream;	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r12.<init>();	 Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
        r13 = r20;
    L_0x0037:
        r14 = r13.read(r11);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r15 = -1;
        if (r14 == r15) goto L_0x0042;
    L_0x003e:
        r12.write(r11, r5, r14);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        goto L_0x0037;
    L_0x0042:
        r11 = r12.toByteArray();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r13 = "key";
        r9 = java.lang.Long.valueOf(r9);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r8.put(r13, r9);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r9 = "tile";
        r8.put(r9, r11);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        if (r0 == 0) goto L_0x005b;
    L_0x0056:
        r9 = "expires";
        r8.put(r9, r0);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
    L_0x005b:
        r0 = "tiles";
        r2.replaceOrThrow(r0, r7, r8);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0 = r0.isDebugMode();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        if (r0 == 0) goto L_0x0089;
    L_0x006a:
        r0 = new java.lang.StringBuilder;	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0.<init>();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r7 = "tile inserted ";
        r0.append(r7);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r7 = r17.name();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0.append(r7);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r7 = org.osmdroid.util.MapTileIndex.toString(r18);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0.append(r7);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0 = r0.toString();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        android.util.Log.d(r6, r0);	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
    L_0x0089:
        r7 = java.lang.System.currentTimeMillis();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r9 = r1.lastSizeCheck;	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r13 = r0.getTileGCFrequencyInMillis();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r9 = r9 + r13;
        r0 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r0 <= 0) goto L_0x00a7;
    L_0x009c:
        r7 = java.lang.System.currentTimeMillis();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r1.lastSizeCheck = r7;	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0 = r1.garbageCollector;	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
        r0.mo4115gc();	 Catch:{ SQLiteFullException -> 0x00b0, Exception -> 0x00ad, all -> 0x00ab }
    L_0x00a7:
        r12.close();	 Catch:{ IOException -> 0x0102 }
        goto L_0x0102;
    L_0x00ab:
        r0 = move-exception;
        goto L_0x0103;
    L_0x00ad:
        r0 = move-exception;
        r7 = r12;
        goto L_0x00b7;
    L_0x00b0:
        r0 = move-exception;
        r7 = r12;
        goto L_0x00f4;
    L_0x00b3:
        r0 = move-exception;
        r12 = r7;
        goto L_0x0103;
    L_0x00b6:
        r0 = move-exception;
    L_0x00b7:
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b3 }
        r8.<init>();	 Catch:{ all -> 0x00b3 }
        r8.append(r4);	 Catch:{ all -> 0x00b3 }
        r4 = r17.name();	 Catch:{ all -> 0x00b3 }
        r8.append(r4);	 Catch:{ all -> 0x00b3 }
        r8.append(r3);	 Catch:{ all -> 0x00b3 }
        r3 = org.osmdroid.util.MapTileIndex.toString(r18);	 Catch:{ all -> 0x00b3 }
        r8.append(r3);	 Catch:{ all -> 0x00b3 }
        r3 = " db is ";
        r8.append(r3);	 Catch:{ all -> 0x00b3 }
        if (r2 != 0) goto L_0x00da;
    L_0x00d7:
        r2 = "null";
        goto L_0x00dc;
    L_0x00da:
        r2 = "not null";
    L_0x00dc:
        r8.append(r2);	 Catch:{ all -> 0x00b3 }
        r2 = r8.toString();	 Catch:{ all -> 0x00b3 }
        android.util.Log.e(r6, r2, r0);	 Catch:{ all -> 0x00b3 }
        r2 = org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors;	 Catch:{ all -> 0x00b3 }
        r2 = r2 + 1;
        org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors = r2;	 Catch:{ all -> 0x00b3 }
        r1.catchException(r0);	 Catch:{ all -> 0x00b3 }
    L_0x00ef:
        r7.close();	 Catch:{ IOException -> 0x0102 }
        goto L_0x0102;
    L_0x00f3:
        r0 = move-exception;
    L_0x00f4:
        r2 = "SQLiteFullException while saving tile.";
        android.util.Log.e(r6, r2, r0);	 Catch:{ all -> 0x00b3 }
        r2 = r1.garbageCollector;	 Catch:{ all -> 0x00b3 }
        r2.mo4115gc();	 Catch:{ all -> 0x00b3 }
        r1.catchException(r0);	 Catch:{ all -> 0x00b3 }
        goto L_0x00ef;
    L_0x0102:
        return r5;
    L_0x0103:
        r12.close();	 Catch:{ IOException -> 0x0106 }
    L_0x0106:
        throw r0;
    L_0x0107:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r4);
        r2 = r17.name();
        r0.append(r2);
        r0.append(r3);
        r2 = org.osmdroid.util.MapTileIndex.toString(r18);
        r0.append(r2);
        r2 = ", database not available.";
        r0.append(r2);
        r0 = r0.toString();
        android.util.Log.d(r6, r0);
        r0 = org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors;
        r0 = r0 + 1;
        org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors = r0;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.saveFile(org.osmdroid.tileprovider.tilesource.ITileSource, long, java.io.InputStream, java.lang.Long):boolean");
    }

    public static long getIndex(long j) {
        return getIndex((long) MapTileIndex.getX(j), (long) MapTileIndex.getY(j), (long) MapTileIndex.getZoom(j));
    }

    public static String[] getPrimaryKeyParameters(long j, ITileSource iTileSource) {
        return getPrimaryKeyParameters(j, iTileSource.name());
    }

    public static String[] getPrimaryKeyParameters(long j, String str) {
        return new String[]{String.valueOf(j), str};
    }

    public Cursor getTileCursor(String[] strArr, String[] strArr2) {
        return getDb().query("tiles", strArr2, "key=? and provider=?", strArr, null, null, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00bc  */
    public android.graphics.drawable.Drawable loadTile(org.osmdroid.tileprovider.tilesource.ITileSource r11, long r12) throws java.lang.Exception {
        /*
        r10 = this;
        r0 = 0;
        r1 = getIndex(r12);	 Catch:{ Exception -> 0x00b5 }
        r1 = getPrimaryKeyParameters(r1, r11);	 Catch:{ Exception -> 0x00b5 }
        r2 = queryColumns;	 Catch:{ Exception -> 0x00b5 }
        r1 = r10.getTileCursor(r1, r2);	 Catch:{ Exception -> 0x00b5 }
        r2 = r1.moveToFirst();	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r3 = 1;
        r4 = 0;
        if (r2 == 0) goto L_0x0020;
    L_0x0017:
        r2 = r1.getBlob(r4);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r5 = r1.getLong(r3);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        goto L_0x0023;
    L_0x0020:
        r5 = 0;
        r2 = r0;
    L_0x0023:
        r7 = "OsmDroid";
        if (r2 != 0) goto L_0x0056;
    L_0x0027:
        r2 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r2 = r2.isDebugMode();	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        if (r2 == 0) goto L_0x0050;
    L_0x0031:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r2.<init>();	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r3 = "SqlCache - Tile doesn't exist: ";
        r2.append(r3);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r11 = r11.name();	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r2.append(r11);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r11 = org.osmdroid.util.MapTileIndex.toString(r12);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r2.append(r11);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        r11 = r2.toString();	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
        android.util.Log.d(r7, r11);	 Catch:{ Exception -> 0x00af, all -> 0x00ad }
    L_0x0050:
        if (r1 == 0) goto L_0x0055;
    L_0x0052:
        r1.close();
    L_0x0055:
        return r0;
    L_0x0056:
        if (r1 == 0) goto L_0x005b;
    L_0x0058:
        r1.close();
    L_0x005b:
        r1 = new java.io.ByteArrayInputStream;	 Catch:{ all -> 0x00a5 }
        r1.<init>(r2);	 Catch:{ all -> 0x00a5 }
        r0 = r11.getDrawable(r1);	 Catch:{ all -> 0x00a3 }
        r8 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x00a3 }
        r2 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r2 >= 0) goto L_0x006d;
    L_0x006c:
        goto L_0x006e;
    L_0x006d:
        r3 = 0;
    L_0x006e:
        if (r3 == 0) goto L_0x009f;
    L_0x0070:
        if (r0 == 0) goto L_0x009f;
    L_0x0072:
        r2 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00a3 }
        r2 = r2.isDebugMode();	 Catch:{ all -> 0x00a3 }
        if (r2 == 0) goto L_0x009b;
    L_0x007c:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a3 }
        r2.<init>();	 Catch:{ all -> 0x00a3 }
        r3 = "Tile expired: ";
        r2.append(r3);	 Catch:{ all -> 0x00a3 }
        r11 = r11.name();	 Catch:{ all -> 0x00a3 }
        r2.append(r11);	 Catch:{ all -> 0x00a3 }
        r11 = org.osmdroid.util.MapTileIndex.toString(r12);	 Catch:{ all -> 0x00a3 }
        r2.append(r11);	 Catch:{ all -> 0x00a3 }
        r11 = r2.toString();	 Catch:{ all -> 0x00a3 }
        android.util.Log.d(r7, r11);	 Catch:{ all -> 0x00a3 }
    L_0x009b:
        r11 = -2;
        org.osmdroid.tileprovider.ExpirableBitmapDrawable.setState(r0, r11);	 Catch:{ all -> 0x00a3 }
    L_0x009f:
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1);
        return r0;
    L_0x00a3:
        r11 = move-exception;
        goto L_0x00a7;
    L_0x00a5:
        r11 = move-exception;
        r1 = r0;
    L_0x00a7:
        if (r1 == 0) goto L_0x00ac;
    L_0x00a9:
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1);
    L_0x00ac:
        throw r11;
    L_0x00ad:
        r11 = move-exception;
        goto L_0x00ba;
    L_0x00af:
        r11 = move-exception;
        r0 = r1;
        goto L_0x00b6;
    L_0x00b2:
        r11 = move-exception;
        r1 = r0;
        goto L_0x00ba;
    L_0x00b5:
        r11 = move-exception;
    L_0x00b6:
        r10.catchException(r11);	 Catch:{ all -> 0x00b2 }
        throw r11;	 Catch:{ all -> 0x00b2 }
    L_0x00ba:
        if (r1 == 0) goto L_0x00bf;
    L_0x00bc:
        r1.close();
    L_0x00bf:
        throw r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.loadTile(org.osmdroid.tileprovider.tilesource.ITileSource, long):android.graphics.drawable.Drawable");
    }

    public void runCleanupOperation(long j, int i, long j2, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        SQLiteDatabase db = getDb();
        long j3 = j;
        Object obj = 1;
        while (j3 > 0) {
            Object obj2;
            if (obj != null) {
                obj2 = null;
            } else {
                if (j2 > 0) {
                    try {
                        Thread.sleep(j2);
                    } catch (InterruptedException unused) {
                    }
                }
                obj2 = obj;
            }
            long currentTimeMillis = System.currentTimeMillis();
            try {
                String str;
                String str2;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("SELECT key,LENGTH(HEX(tile))/2 FROM tiles WHERE expires IS NOT NULL ");
                String str3 = "";
                if (z) {
                    str = str3;
                } else {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("AND expires < ");
                    stringBuilder3.append(currentTimeMillis);
                    stringBuilder3.append(" ");
                    str = stringBuilder3.toString();
                }
                stringBuilder2.append(str);
                stringBuilder2.append("ORDER BY ");
                stringBuilder2.append("expires");
                stringBuilder2.append(" ASC LIMIT ");
                stringBuilder2.append(i);
                Cursor rawQuery = db.rawQuery(stringBuilder2.toString(), null);
                rawQuery.moveToFirst();
                stringBuilder.setLength(0);
                stringBuilder.append("key in (");
                long j4 = j3;
                Object obj3 = str3;
                while (!rawQuery.isAfterLast()) {
                    str2 = str3;
                    long j5 = rawQuery.getLong(0);
                    long j6 = rawQuery.getLong(1);
                    rawQuery.moveToNext();
                    stringBuilder.append(obj3);
                    stringBuilder.append(j5);
                    obj3 = ",";
                    j4 -= j6;
                    if (j4 <= 0) {
                        break;
                    }
                    str3 = str2;
                }
                str2 = str3;
                rawQuery.close();
                if (!str2.equals(obj3)) {
                    stringBuilder.append(')');
                    try {
                        db.delete("tiles", stringBuilder.toString(), null);
                    } catch (SQLiteFullException e) {
                        Log.e("OsmDroid", "SQLiteFullException while cleanup.", e);
                        catchException(e);
                    } catch (Exception e2) {
                        catchException(e2);
                        return;
                    }
                    obj = obj2;
                    j3 = j4;
                } else {
                    return;
                }
            } catch (Exception e22) {
                catchException(e22);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public SQLiteDatabase getDb() {
        SQLiteDatabase sQLiteDatabase = mDb;
        if (sQLiteDatabase != null) {
            return sQLiteDatabase;
        }
        synchronized (mLock) {
            Configuration.getInstance().getOsmdroidTileCache().mkdirs();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Configuration.getInstance().getOsmdroidTileCache().getAbsolutePath());
            stringBuilder.append(File.separator);
            stringBuilder.append("cache.db");
            db_file = new File(stringBuilder.toString());
            if (mDb == null) {
                try {
                    mDb = SQLiteDatabase.openOrCreateDatabase(db_file, null);
                    mDb.execSQL("CREATE TABLE IF NOT EXISTS tiles (key INTEGER , provider TEXT, tile BLOB, expires INTEGER, PRIMARY KEY (key, provider));");
                } catch (Exception e) {
                    Log.e("OsmDroid", "Unable to start the sqlite tile writer. Check external storage availability.", e);
                    catchException(e);
                    return null;
                }
            }
        }
        return mDb;
    }

    public void refreshDb() {
        synchronized (mLock) {
            if (mDb != null) {
                mDb.close();
                mDb = null;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void catchException(Exception exception) {
        if ((exception instanceof SQLiteException) && !isFunctionalException((SQLiteException) exception)) {
            refreshDb();
        }
    }

    public static boolean isFunctionalException(android.database.sqlite.SQLiteException r3) {
        /*
        r3 = r3.getClass();
        r3 = r3.getSimpleName();
        r0 = r3.hashCode();
        r1 = 1;
        r2 = 0;
        switch(r0) {
            case -2070793707: goto L_0x00b6;
            case -1764604492: goto L_0x00ac;
            case -1458338457: goto L_0x00a2;
            case -1115484154: goto L_0x0097;
            case -1113540439: goto L_0x008c;
            case -672728977: goto L_0x0081;
            case -669227773: goto L_0x0077;
            case -119599910: goto L_0x006c;
            case 20404371: goto L_0x0062;
            case 124364321: goto L_0x0057;
            case 325468747: goto L_0x004c;
            case 532355648: goto L_0x0040;
            case 666588538: goto L_0x0035;
            case 1061155622: goto L_0x002a;
            case 1400520606: goto L_0x001e;
            case 1939376593: goto L_0x0013;
            default: goto L_0x0011;
        };
    L_0x0011:
        goto L_0x00c1;
    L_0x0013:
        r0 = "SQLiteDatatypeMismatchException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x001b:
        r3 = 3;
        goto L_0x00c2;
    L_0x001e:
        r0 = "SQLiteDatabaseLockedException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0026:
        r3 = 11;
        goto L_0x00c2;
    L_0x002a:
        r0 = "SQLiteConstraintException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0032:
        r3 = 2;
        goto L_0x00c2;
    L_0x0035:
        r0 = "SQLiteBlobTooBigException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x003d:
        r3 = 1;
        goto L_0x00c2;
    L_0x0040:
        r0 = "SQLiteDiskIOException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0048:
        r3 = 12;
        goto L_0x00c2;
    L_0x004c:
        r0 = "SQLiteAbortException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0054:
        r3 = 7;
        goto L_0x00c2;
    L_0x0057:
        r0 = "SQLiteDoneException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x005f:
        r3 = 13;
        goto L_0x00c2;
    L_0x0062:
        r0 = "SQLiteMisuseException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x006a:
        r3 = 5;
        goto L_0x00c2;
    L_0x006c:
        r0 = "SQLiteCantOpenDatabaseException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0074:
        r3 = 9;
        goto L_0x00c2;
    L_0x0077:
        r0 = "SQLiteTableLockedException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x007f:
        r3 = 6;
        goto L_0x00c2;
    L_0x0081:
        r0 = "SQLiteAccessPermException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0089:
        r3 = 8;
        goto L_0x00c2;
    L_0x008c:
        r0 = "SQLiteDatabaseCorruptException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x0094:
        r3 = 10;
        goto L_0x00c2;
    L_0x0097:
        r0 = "SQLiteReadOnlyDatabaseException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x009f:
        r3 = 15;
        goto L_0x00c2;
    L_0x00a2:
        r0 = "SQLiteBindOrColumnIndexOutOfRangeException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x00aa:
        r3 = 0;
        goto L_0x00c2;
    L_0x00ac:
        r0 = "SQLiteFullException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x00b4:
        r3 = 4;
        goto L_0x00c2;
    L_0x00b6:
        r0 = "SQLiteOutOfMemoryException";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c1;
    L_0x00be:
        r3 = 14;
        goto L_0x00c2;
    L_0x00c1:
        r3 = -1;
    L_0x00c2:
        switch(r3) {
            case 0: goto L_0x00c6;
            case 1: goto L_0x00c6;
            case 2: goto L_0x00c6;
            case 3: goto L_0x00c6;
            case 4: goto L_0x00c6;
            case 5: goto L_0x00c6;
            case 6: goto L_0x00c6;
            case 7: goto L_0x00c5;
            case 8: goto L_0x00c5;
            case 9: goto L_0x00c5;
            case 10: goto L_0x00c5;
            case 11: goto L_0x00c5;
            case 12: goto L_0x00c5;
            case 13: goto L_0x00c5;
            case 14: goto L_0x00c5;
            case 15: goto L_0x00c5;
            default: goto L_0x00c5;
        };
    L_0x00c5:
        return r2;
    L_0x00c6:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.isFunctionalException(android.database.sqlite.SQLiteException):boolean");
    }

    private void createIndex(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS expires_index ON tiles (expires);");
    }
}
