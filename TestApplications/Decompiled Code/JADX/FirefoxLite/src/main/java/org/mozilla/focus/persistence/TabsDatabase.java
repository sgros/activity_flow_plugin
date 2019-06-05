package org.mozilla.focus.persistence;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

public abstract class TabsDatabase extends RoomDatabase {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE tabs RENAME TO tabs_old");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tabs (tab_id TEXT NOT NULL, tab_parent_id TEXT, tab_title TEXT, tab_url TEXT, PRIMARY KEY(tab_id))");
            supportSQLiteDatabase.execSQL("INSERT INTO tabs (tab_id, tab_parent_id, tab_title, tab_url) SELECT tab_id, tab_parent_id, tab_title, tab_url FROM tabs_old");
            supportSQLiteDatabase.execSQL("DROP TABLE tabs_old");
        }
    };
    private static volatile TabsDatabase instance;

    public abstract TabDao tabDao();

    public static TabsDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (TabsDatabase.class) {
                if (instance == null) {
                    instance = (TabsDatabase) Room.databaseBuilder(context.getApplicationContext(), TabsDatabase.class, "tabs.db").addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return instance;
    }
}
