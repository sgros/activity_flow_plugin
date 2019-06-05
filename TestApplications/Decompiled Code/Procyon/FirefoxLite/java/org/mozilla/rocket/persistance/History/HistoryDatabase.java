// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.persistance.History;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.arch.persistence.room.RoomDatabase;

public abstract class HistoryDatabase extends RoomDatabase
{
    private static final Migration MIGRATION_1_2;
    private static final Migration MIGRATION_2_3;
    private static volatile HistoryDatabase instance;
    
    static {
        MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.beginTransaction();
                try {
                    supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS browsing_history_legacy (_id INTEGER PRIMARY KEY NOT NULL,url TEXT NOT NULL,fav_icon BLOB);");
                    supportSQLiteDatabase.execSQL("INSERT INTO browsing_history_legacy (_id, fav_icon, url) SELECT _id, fav_icon, url FROM browsing_history ORDER BY last_view_timestamp DESC LIMIT 50");
                    supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO browsing_history_legacy (_id, fav_icon, url) SELECT _id, fav_icon, url FROM browsing_history WHERE view_count > 6 ORDER BY view_count LIMIT 16");
                    supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS browsing_history_new (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title TEXT,url TEXT NOT NULL,view_count INTEGER NOT NULL DEFAULT 1,last_view_timestamp INTEGER NOT NULL,fav_icon_uri TEXT);");
                    supportSQLiteDatabase.execSQL("INSERT INTO browsing_history_new (_id, title, url, view_count, last_view_timestamp) SELECT _id, title, url, view_count, last_view_timestamp FROM browsing_history");
                    supportSQLiteDatabase.execSQL("DROP TABLE browsing_history");
                    supportSQLiteDatabase.execSQL("ALTER TABLE browsing_history_new RENAME TO browsing_history");
                    supportSQLiteDatabase.setTransactionSuccessful();
                }
                finally {
                    supportSQLiteDatabase.endTransaction();
                }
            }
        };
        MIGRATION_2_3 = new Migration(2, 3) {
            @Override
            public void migrate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS index_browsing_history_view_count ON browsing_history(view_count)");
            }
        };
    }
    
    public static HistoryDatabase getInstance(final Context context) {
        if (HistoryDatabase.instance == null) {
            synchronized (HistoryDatabase.class) {
                if (HistoryDatabase.instance == null) {
                    HistoryDatabase.instance = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "history.db").addMigrations(HistoryDatabase.MIGRATION_1_2, HistoryDatabase.MIGRATION_2_3).build();
                }
            }
        }
        return HistoryDatabase.instance;
    }
}
