// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.arch.persistence.room.RoomDatabase;

public abstract class TabsDatabase extends RoomDatabase
{
    static final Migration MIGRATION_1_2;
    private static volatile TabsDatabase instance;
    
    static {
        MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("ALTER TABLE tabs RENAME TO tabs_old");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tabs (tab_id TEXT NOT NULL, tab_parent_id TEXT, tab_title TEXT, tab_url TEXT, PRIMARY KEY(tab_id))");
                supportSQLiteDatabase.execSQL("INSERT INTO tabs (tab_id, tab_parent_id, tab_title, tab_url) SELECT tab_id, tab_parent_id, tab_title, tab_url FROM tabs_old");
                supportSQLiteDatabase.execSQL("DROP TABLE tabs_old");
            }
        };
    }
    
    public static TabsDatabase getInstance(final Context context) {
        if (TabsDatabase.instance == null) {
            synchronized (TabsDatabase.class) {
                if (TabsDatabase.instance == null) {
                    TabsDatabase.instance = Room.databaseBuilder(context.getApplicationContext(), TabsDatabase.class, "tabs.db").addMigrations(TabsDatabase.MIGRATION_1_2).build();
                }
            }
        }
        return TabsDatabase.instance;
    }
    
    public abstract TabDao tabDao();
}
