package org.mozilla.focus.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ScreenshotDatabaseHelper {
    private static ScreenshotDatabaseHelper sInstacne;
    private final OpenHelper mOpenHelper;

    private static final class OpenHelper extends SQLiteOpenHelper {
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public OpenHelper(Context context, String str, CursorFactory cursorFactory, int i) {
            super(context, str, cursorFactory, i);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS screenshot");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS screenshot (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,url TEXT NOT NULL,timestamp INTEGER NOT NULL,image_uri TEXT NOT NULL);");
            sQLiteDatabase.execSQL("DROP TRIGGER IF EXISTS screenshot_inserted;");
            sQLiteDatabase.execSQL("CREATE TRIGGER IF NOT EXISTS screenshot_inserted    AFTER INSERT ON screenshot WHEN (SELECT count() FROM screenshot) > 2000 BEGIN    DELETE FROM screenshot     WHERE _id = (SELECT _id FROM screenshot ORDER BY timestamp LIMIT 1); END");
        }
    }

    private ScreenshotDatabaseHelper(Context context) {
        this.mOpenHelper = new OpenHelper(context, "screenshot.db", null, 1);
    }

    public static synchronized ScreenshotDatabaseHelper getsInstacne(Context context) {
        ScreenshotDatabaseHelper screenshotDatabaseHelper;
        synchronized (ScreenshotDatabaseHelper.class) {
            if (sInstacne == null) {
                sInstacne = new ScreenshotDatabaseHelper(context);
            }
            screenshotDatabaseHelper = sInstacne;
        }
        return screenshotDatabaseHelper;
    }

    public SQLiteDatabase getReadableDatabase() {
        return this.mOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return this.mOpenHelper.getWritableDatabase();
    }
}
