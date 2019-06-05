// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.Context;

public class ScreenshotDatabaseHelper
{
    private static ScreenshotDatabaseHelper sInstacne;
    private final OpenHelper mOpenHelper;
    
    private ScreenshotDatabaseHelper(final Context context) {
        this.mOpenHelper = new OpenHelper(context, "screenshot.db", null, 1);
    }
    
    public static ScreenshotDatabaseHelper getsInstacne(final Context context) {
        synchronized (ScreenshotDatabaseHelper.class) {
            if (ScreenshotDatabaseHelper.sInstacne == null) {
                ScreenshotDatabaseHelper.sInstacne = new ScreenshotDatabaseHelper(context);
            }
            return ScreenshotDatabaseHelper.sInstacne;
        }
    }
    
    public SQLiteDatabase getReadableDatabase() {
        return this.mOpenHelper.getReadableDatabase();
    }
    
    public SQLiteDatabase getWritableDatabase() {
        return this.mOpenHelper.getWritableDatabase();
    }
    
    private static final class OpenHelper extends SQLiteOpenHelper
    {
        public OpenHelper(final Context context, final String s, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory, final int n) {
            super(context, s, sqLiteDatabase$CursorFactory, n);
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS screenshot");
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS screenshot (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,url TEXT NOT NULL,timestamp INTEGER NOT NULL,image_uri TEXT NOT NULL);");
            sqLiteDatabase.execSQL("DROP TRIGGER IF EXISTS screenshot_inserted;");
            sqLiteDatabase.execSQL("CREATE TRIGGER IF NOT EXISTS screenshot_inserted    AFTER INSERT ON screenshot WHEN (SELECT count() FROM screenshot) > 2000 BEGIN    DELETE FROM screenshot     WHERE _id = (SELECT _id FROM screenshot ORDER BY timestamp LIMIT 1); END");
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        }
    }
}
