package org.mozilla.focus.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadInfoDbHelper {
    private static DownloadInfoDbHelper sInstance;
    private final OpenHelper mOpenHelper;

    private static final class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context, String str, CursorFactory cursorFactory, int i) {
            super(context, str, cursorFactory, i);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE download_info(_id INTEGER PRIMARY KEY AUTOINCREMENT,download_id INTEGER,file_path TEXT,status INTEGER,is_read INTEGER DEFAULT 0)");
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            if (i < 2) {
                sQLiteDatabase.execSQL("ALTER TABLE download_info ADD status INTEGER;");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UPDATE download_info SET status = ");
                stringBuilder.append(String.valueOf(8));
                stringBuilder.append(";");
                sQLiteDatabase.execSQL(stringBuilder.toString());
                sQLiteDatabase.execSQL("ALTER TABLE download_info ADD is_read INTEGER DEFAULT 0;");
                sQLiteDatabase.execSQL("UPDATE download_info SET is_read = 1;");
            }
        }
    }

    private int getDatabaseVersion() {
        return 2;
    }

    private DownloadInfoDbHelper(Context context) {
        this.mOpenHelper = new OpenHelper(context, "DownloadInfo.db", null, getDatabaseVersion());
    }

    public static synchronized DownloadInfoDbHelper getsInstance(Context context) {
        DownloadInfoDbHelper downloadInfoDbHelper;
        synchronized (DownloadInfoDbHelper.class) {
            if (sInstance == null) {
                sInstance = new DownloadInfoDbHelper(context);
            }
            downloadInfoDbHelper = sInstance;
        }
        return downloadInfoDbHelper;
    }

    public SQLiteDatabase getReadableDB() {
        return this.mOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDB() {
        return this.mOpenHelper.getWritableDatabase();
    }
}
