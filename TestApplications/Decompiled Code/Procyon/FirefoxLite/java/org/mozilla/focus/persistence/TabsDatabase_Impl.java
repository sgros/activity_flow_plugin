// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import android.arch.persistence.room.util.TableInfo;
import java.util.HashMap;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.db.SupportSQLiteDatabase;
import java.util.List;

public class TabsDatabase_Impl extends TabsDatabase
{
    private volatile TabDao _tabDao;
    
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new String[] { "tabs" });
    }
    
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, (RoomOpenHelper.Delegate)new RoomOpenHelper.Delegate(2) {
            public void createAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `tabs` (`tab_id` TEXT NOT NULL, `tab_parent_id` TEXT, `tab_title` TEXT, `tab_url` TEXT, PRIMARY KEY(`tab_id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"e4cc9f6316824138aa0032ce908db7d6\")");
            }
            
            public void dropAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `tabs`");
            }
            
            @Override
            protected void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                if (TabsDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < TabsDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)TabsDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }
            
            public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
                TabsDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                RoomDatabase.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (TabsDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < TabsDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)TabsDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }
            
            @Override
            protected void validateMigration(final SupportSQLiteDatabase supportSQLiteDatabase) {
                final HashMap<String, TableInfo.Column> hashMap = new HashMap<String, TableInfo.Column>(4);
                hashMap.put("tab_id", new TableInfo.Column("tab_id", "TEXT", true, 1));
                hashMap.put("tab_parent_id", new TableInfo.Column("tab_parent_id", "TEXT", false, 0));
                hashMap.put("tab_title", new TableInfo.Column("tab_title", "TEXT", false, 0));
                hashMap.put("tab_url", new TableInfo.Column("tab_url", "TEXT", false, 0));
                final TableInfo obj = new TableInfo("tabs", hashMap, new HashSet<TableInfo.ForeignKey>(0), new HashSet<TableInfo.Index>(0));
                final TableInfo read = TableInfo.read(supportSQLiteDatabase, "tabs");
                if (obj.equals(read)) {
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Migration didn't properly handle tabs(org.mozilla.focus.persistence.TabEntity).\n Expected:\n");
                sb.append(obj);
                sb.append("\n Found:\n");
                sb.append(read);
                throw new IllegalStateException(sb.toString());
            }
        }, "e4cc9f6316824138aa0032ce908db7d6", "34f484a157ca50a1d8f3c2529bd492dc")).build());
    }
    
    @Override
    public TabDao tabDao() {
        if (this._tabDao != null) {
            return this._tabDao;
        }
        synchronized (this) {
            if (this._tabDao == null) {
                this._tabDao = new TabDao_Impl(this);
            }
            return this._tabDao;
        }
    }
}
