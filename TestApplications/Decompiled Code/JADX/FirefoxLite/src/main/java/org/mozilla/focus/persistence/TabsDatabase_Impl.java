package org.mozilla.focus.persistence;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import java.util.HashMap;
import java.util.HashSet;

public class TabsDatabase_Impl extends TabsDatabase {
    private volatile TabDao _tabDao;

    /* Access modifiers changed, original: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(2) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `tabs` (`tab_id` TEXT NOT NULL, `tab_parent_id` TEXT, `tab_title` TEXT, `tab_url` TEXT, PRIMARY KEY(`tab_id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"e4cc9f6316824138aa0032ce908db7d6\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `tabs`");
            }

            /* Access modifiers changed, original: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (TabsDatabase_Impl.this.mCallbacks != null) {
                    int size = TabsDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) TabsDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                TabsDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                TabsDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (TabsDatabase_Impl.this.mCallbacks != null) {
                    int size = TabsDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) TabsDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* Access modifiers changed, original: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(4);
                hashMap.put("tab_id", new Column("tab_id", "TEXT", true, 1));
                hashMap.put("tab_parent_id", new Column("tab_parent_id", "TEXT", false, 0));
                hashMap.put("tab_title", new Column("tab_title", "TEXT", false, 0));
                hashMap.put("tab_url", new Column("tab_url", "TEXT", false, 0));
                TableInfo tableInfo = new TableInfo("tabs", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "tabs");
                if (!tableInfo.equals(read)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Migration didn't properly handle tabs(org.mozilla.focus.persistence.TabEntity).\n Expected:\n");
                    stringBuilder.append(tableInfo);
                    stringBuilder.append("\n Found:\n");
                    stringBuilder.append(read);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }, "e4cc9f6316824138aa0032ce908db7d6", "34f484a157ca50a1d8f3c2529bd492dc")).build());
    }

    /* Access modifiers changed, original: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "tabs");
    }

    public TabDao tabDao() {
        if (this._tabDao != null) {
            return this._tabDao;
        }
        TabDao tabDao;
        synchronized (this) {
            if (this._tabDao == null) {
                this._tabDao = new TabDao_Impl(this);
            }
            tabDao = this._tabDao;
        }
        return tabDao;
    }
}
