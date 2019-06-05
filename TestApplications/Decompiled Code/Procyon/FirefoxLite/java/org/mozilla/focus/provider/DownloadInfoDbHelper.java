// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.Context;

public class DownloadInfoDbHelper
{
    private static DownloadInfoDbHelper sInstance;
    private final OpenHelper mOpenHelper;
    
    private DownloadInfoDbHelper(final Context context) {
        this.mOpenHelper = new OpenHelper(context, "DownloadInfo.db", null, this.getDatabaseVersion());
    }
    
    private int getDatabaseVersion() {
        return 2;
    }
    
    public static DownloadInfoDbHelper getsInstance(final Context context) {
        synchronized (DownloadInfoDbHelper.class) {
            if (DownloadInfoDbHelper.sInstance == null) {
                DownloadInfoDbHelper.sInstance = new DownloadInfoDbHelper(context);
            }
            return DownloadInfoDbHelper.sInstance;
        }
    }
    
    public SQLiteDatabase getReadableDB() {
        return this.mOpenHelper.getReadableDatabase();
    }
    
    public SQLiteDatabase getWritableDB() {
        return this.mOpenHelper.getWritableDatabase();
    }
    
    private static final class OpenHelper extends SQLiteOpenHelper
    {
        public OpenHelper(final Context context, final String s, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory, final int n) {
            super(context, s, sqLiteDatabase$CursorFactory, n);
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE download_info(_id INTEGER PRIMARY KEY AUTOINCREMENT,download_id INTEGER,file_path TEXT,status INTEGER,is_read INTEGER DEFAULT 0)");
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
            if (n < 2) {
                sqLiteDatabase.execSQL("ALTER TABLE download_info ADD status INTEGER;");
                final StringBuilder sb = new StringBuilder();
                sb.append("UPDATE download_info SET status = ");
                sb.append(String.valueOf(8));
                sb.append(";");
                sqLiteDatabase.execSQL(sb.toString());
                sqLiteDatabase.execSQL("ALTER TABLE download_info ADD is_read INTEGER DEFAULT 0;");
                sqLiteDatabase.execSQL("UPDATE download_info SET is_read = 1;");
            }
        }
    }
}
