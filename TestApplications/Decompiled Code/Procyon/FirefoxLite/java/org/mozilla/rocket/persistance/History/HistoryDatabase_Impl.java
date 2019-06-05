// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.persistance.History;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
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

public class HistoryDatabase_Impl extends HistoryDatabase
{
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new String[] { "browsing_history" });
    }
    
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, (RoomOpenHelper.Delegate)new RoomOpenHelper.Delegate(3) {
            public void createAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `browsing_history` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `url` TEXT NOT NULL, `view_count` INTEGER NOT NULL, `last_view_timestamp` INTEGER NOT NULL, `fav_icon_uri` TEXT)");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_browsing_history_view_count` ON `browsing_history` (`view_count`)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"ed1350002e11ab61b6b9f2aab52e8ce4\")");
            }
            
            public void dropAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `browsing_history`");
            }
            
            @Override
            protected void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                if (HistoryDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < HistoryDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)HistoryDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }
            
            public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
                HistoryDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                RoomDatabase.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (HistoryDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < HistoryDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)HistoryDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }
            
            @Override
            protected void validateMigration(final SupportSQLiteDatabase supportSQLiteDatabase) {
                final HashMap<String, TableInfo.Column> hashMap = new HashMap<String, TableInfo.Column>(6);
                hashMap.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1));
                hashMap.put("title", new TableInfo.Column("title", "TEXT", false, 0));
                hashMap.put("url", new TableInfo.Column("url", "TEXT", true, 0));
                hashMap.put("view_count", new TableInfo.Column("view_count", "INTEGER", true, 0));
                hashMap.put("last_view_timestamp", new TableInfo.Column("last_view_timestamp", "INTEGER", true, 0));
                hashMap.put("fav_icon_uri", new TableInfo.Column("fav_icon_uri", "TEXT", false, 0));
                final HashSet<TableInfo.ForeignKey> set = new HashSet<TableInfo.ForeignKey>(0);
                final HashSet<TableInfo.Index> set2 = new HashSet<TableInfo.Index>(1);
                set2.add(new TableInfo.Index("index_browsing_history_view_count", false, Arrays.asList("view_count")));
                final TableInfo obj = new TableInfo("browsing_history", hashMap, set, set2);
                final TableInfo read = TableInfo.read(supportSQLiteDatabase, "browsing_history");
                if (obj.equals(read)) {
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Migration didn't properly handle browsing_history(org.mozilla.focus.history.model.Site).\n Expected:\n");
                sb.append(obj);
                sb.append("\n Found:\n");
                sb.append(read);
                throw new IllegalStateException(sb.toString());
            }
        }, "ed1350002e11ab61b6b9f2aab52e8ce4", "187c585e7ba70c63c7fcbeb3f99508f2")).build());
    }
}
