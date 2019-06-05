package net.sqlcipher.database;

import android.content.Context;
import android.util.Log;
import java.io.File;
import net.sqlcipher.DatabaseErrorHandler;
import net.sqlcipher.DefaultDatabaseErrorHandler;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;

public abstract class SQLiteOpenHelper {
    private static final String TAG = "SQLiteOpenHelper";
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private final DatabaseErrorHandler mErrorHandler;
    private final CursorFactory mFactory;
    private final SQLiteDatabaseHook mHook;
    private boolean mIsInitializing;
    private final String mName;
    private final int mNewVersion;

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
    }

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    public SQLiteOpenHelper(Context context, String str, CursorFactory cursorFactory, int i) {
        this(context, str, cursorFactory, i, null, new DefaultDatabaseErrorHandler());
    }

    public SQLiteOpenHelper(Context context, String str, CursorFactory cursorFactory, int i, SQLiteDatabaseHook sQLiteDatabaseHook) {
        this(context, str, cursorFactory, i, sQLiteDatabaseHook, new DefaultDatabaseErrorHandler());
    }

    public SQLiteOpenHelper(Context context, String str, CursorFactory cursorFactory, int i, SQLiteDatabaseHook sQLiteDatabaseHook, DatabaseErrorHandler databaseErrorHandler) {
        this.mDatabase = null;
        this.mIsInitializing = false;
        if (i < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Version must be >= 1, was ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (databaseErrorHandler == null) {
            throw new IllegalArgumentException("DatabaseErrorHandler param value can't be null.");
        } else {
            this.mContext = context;
            this.mName = str;
            this.mFactory = cursorFactory;
            this.mNewVersion = i;
            this.mHook = sQLiteDatabaseHook;
            this.mErrorHandler = databaseErrorHandler;
        }
    }

    public synchronized SQLiteDatabase getWritableDatabase(String str) {
        return getWritableDatabase(str == null ? null : str.toCharArray());
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x009c */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:47|48|49|50) */
    public synchronized net.sqlcipher.database.SQLiteDatabase getWritableDatabase(char[] r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        if (r0 == 0) goto L_0x0019;
    L_0x0005:
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        r0 = r0.isOpen();	 Catch:{ all -> 0x00b7 }
        if (r0 == 0) goto L_0x0019;
    L_0x000d:
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        r0 = r0.isReadOnly();	 Catch:{ all -> 0x00b7 }
        if (r0 != 0) goto L_0x0019;
    L_0x0015:
        r7 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        monitor-exit(r6);
        return r7;
    L_0x0019:
        r0 = r6.mIsInitializing;	 Catch:{ all -> 0x00b7 }
        if (r0 == 0) goto L_0x0025;
    L_0x001d:
        r7 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00b7 }
        r0 = "getWritableDatabase called recursively";
        r7.<init>(r0);	 Catch:{ all -> 0x00b7 }
        throw r7;	 Catch:{ all -> 0x00b7 }
    L_0x0025:
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        if (r0 == 0) goto L_0x002e;
    L_0x0029:
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        r0.lock();	 Catch:{ all -> 0x00b7 }
    L_0x002e:
        r0 = 1;
        r1 = 0;
        r2 = 0;
        r6.mIsInitializing = r0;	 Catch:{ all -> 0x00a5 }
        r0 = r6.mName;	 Catch:{ all -> 0x00a5 }
        if (r0 != 0) goto L_0x003d;
    L_0x0037:
        r7 = net.sqlcipher.database.SQLiteDatabase.create(r2, r7);	 Catch:{ all -> 0x00a5 }
    L_0x003b:
        r2 = r7;
        goto L_0x0066;
    L_0x003d:
        r0 = r6.mContext;	 Catch:{ all -> 0x00a5 }
        r3 = r6.mName;	 Catch:{ all -> 0x00a5 }
        r0 = r0.getDatabasePath(r3);	 Catch:{ all -> 0x00a5 }
        r0 = r0.getPath();	 Catch:{ all -> 0x00a5 }
        r3 = new java.io.File;	 Catch:{ all -> 0x00a5 }
        r3.<init>(r0);	 Catch:{ all -> 0x00a5 }
        r4 = r3.exists();	 Catch:{ all -> 0x00a5 }
        if (r4 != 0) goto L_0x005b;
    L_0x0054:
        r3 = r3.getParentFile();	 Catch:{ all -> 0x00a5 }
        r3.mkdirs();	 Catch:{ all -> 0x00a5 }
    L_0x005b:
        r3 = r6.mFactory;	 Catch:{ all -> 0x00a5 }
        r4 = r6.mHook;	 Catch:{ all -> 0x00a5 }
        r5 = r6.mErrorHandler;	 Catch:{ all -> 0x00a5 }
        r7 = net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(r0, r7, r3, r4, r5);	 Catch:{ all -> 0x00a5 }
        goto L_0x003b;
    L_0x0066:
        r7 = r2.getVersion();	 Catch:{ all -> 0x00a5 }
        r0 = r6.mNewVersion;	 Catch:{ all -> 0x00a5 }
        if (r7 == r0) goto L_0x008e;
    L_0x006e:
        r2.beginTransaction();	 Catch:{ all -> 0x00a5 }
        if (r7 != 0) goto L_0x0079;
    L_0x0073:
        r6.onCreate(r2);	 Catch:{ all -> 0x0077 }
        goto L_0x007e;
    L_0x0077:
        r7 = move-exception;
        goto L_0x008a;
    L_0x0079:
        r0 = r6.mNewVersion;	 Catch:{ all -> 0x0077 }
        r6.onUpgrade(r2, r7, r0);	 Catch:{ all -> 0x0077 }
    L_0x007e:
        r7 = r6.mNewVersion;	 Catch:{ all -> 0x0077 }
        r2.setVersion(r7);	 Catch:{ all -> 0x0077 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x0077 }
        r2.endTransaction();	 Catch:{ all -> 0x00a5 }
        goto L_0x008e;
    L_0x008a:
        r2.endTransaction();	 Catch:{ all -> 0x00a5 }
        throw r7;	 Catch:{ all -> 0x00a5 }
    L_0x008e:
        r6.onOpen(r2);	 Catch:{ all -> 0x00a5 }
        r6.mIsInitializing = r1;	 Catch:{ all -> 0x00b7 }
        r7 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        if (r7 == 0) goto L_0x00a1;
    L_0x0097:
        r7 = r6.mDatabase;	 Catch:{ Exception -> 0x009c }
        r7.close();	 Catch:{ Exception -> 0x009c }
    L_0x009c:
        r7 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        r7.unlock();	 Catch:{ all -> 0x00b7 }
    L_0x00a1:
        r6.mDatabase = r2;	 Catch:{ all -> 0x00b7 }
        monitor-exit(r6);
        return r2;
    L_0x00a5:
        r7 = move-exception;
        r6.mIsInitializing = r1;	 Catch:{ all -> 0x00b7 }
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        if (r0 == 0) goto L_0x00b1;
    L_0x00ac:
        r0 = r6.mDatabase;	 Catch:{ all -> 0x00b7 }
        r0.unlock();	 Catch:{ all -> 0x00b7 }
    L_0x00b1:
        if (r2 == 0) goto L_0x00b6;
    L_0x00b3:
        r2.close();	 Catch:{ all -> 0x00b7 }
    L_0x00b6:
        throw r7;	 Catch:{ all -> 0x00b7 }
    L_0x00b7:
        r7 = move-exception;
        monitor-exit(r6);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sqlcipher.database.SQLiteOpenHelper.getWritableDatabase(char[]):net.sqlcipher.database.SQLiteDatabase");
    }

    public synchronized SQLiteDatabase getReadableDatabase(String str) {
        return getReadableDatabase(str == null ? null : str.toCharArray());
    }

    public synchronized SQLiteDatabase getReadableDatabase(char[] cArr) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase writableDatabase;
        SQLiteDatabase openDatabase;
        Throwable th;
        if (this.mDatabase != null && this.mDatabase.isOpen()) {
            return this.mDatabase;
        } else if (this.mIsInitializing) {
            throw new IllegalStateException("getReadableDatabase called recursively");
        } else {
            try {
                return getWritableDatabase(cArr);
            } catch (SQLiteException e) {
                if (this.mName == null) {
                    throw e;
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't open ");
                stringBuilder.append(this.mName);
                stringBuilder.append(" for writing (will try read-only):");
                Log.e(str, stringBuilder.toString(), e);
                sQLiteDatabase = null;
                this.mIsInitializing = true;
                String path = this.mContext.getDatabasePath(this.mName).getPath();
                File file = new File(path);
                File file2 = new File(this.mContext.getDatabasePath(this.mName).getParent());
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (!file.exists()) {
                    this.mIsInitializing = false;
                    writableDatabase = getWritableDatabase(cArr);
                    this.mIsInitializing = true;
                    writableDatabase.close();
                    sQLiteDatabase = writableDatabase;
                }
                openDatabase = SQLiteDatabase.openDatabase(path, cArr, this.mFactory, 1);
                if (openDatabase.getVersion() != this.mNewVersion) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Can't upgrade read-only database from version ");
                    stringBuilder.append(openDatabase.getVersion());
                    stringBuilder.append(" to ");
                    stringBuilder.append(this.mNewVersion);
                    stringBuilder.append(": ");
                    stringBuilder.append(path);
                    throw new SQLiteException(stringBuilder.toString());
                }
                onOpen(openDatabase);
                String str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Opened ");
                stringBuilder.append(this.mName);
                stringBuilder.append(" in read-only mode");
                Log.w(str2, stringBuilder.toString());
                this.mDatabase = openDatabase;
                sQLiteDatabase = this.mDatabase;
                this.mIsInitializing = false;
                if (!(openDatabase == null || openDatabase == this.mDatabase)) {
                    openDatabase.close();
                }
                return sQLiteDatabase;
            } catch (Throwable th2) {
                th = th2;
                this.mIsInitializing = false;
                openDatabase.close();
                throw th;
            }
        }
    }

    public synchronized void close() {
        if (this.mIsInitializing) {
            throw new IllegalStateException("Closed during initialization");
        } else if (this.mDatabase != null && this.mDatabase.isOpen()) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }
}
