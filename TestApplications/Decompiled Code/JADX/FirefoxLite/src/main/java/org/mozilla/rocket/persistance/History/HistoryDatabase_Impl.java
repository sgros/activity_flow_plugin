package org.mozilla.rocket.persistance.History;

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
import android.arch.persistence.room.util.TableInfo.Index;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class HistoryDatabase_Impl extends HistoryDatabase {
    /* Access modifiers changed, original: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(3) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `browsing_history` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `url` TEXT NOT NULL, `view_count` INTEGER NOT NULL, `last_view_timestamp` INTEGER NOT NULL, `fav_icon_uri` TEXT)");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_browsing_history_view_count` ON `browsing_history` (`view_count`)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"ed1350002e11ab61b6b9f2aab52e8ce4\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `browsing_history`");
            }

            /* Access modifiers changed, original: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (HistoryDatabase_Impl.this.mCallbacks != null) {
                    int size = HistoryDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) HistoryDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                HistoryDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                HistoryDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (HistoryDatabase_Impl.this.mCallbacks != null) {
                    int size = HistoryDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) HistoryDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* Access modifiers changed, original: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(6);
                hashMap.put("_id", new Column("_id", "INTEGER", true, 1));
                hashMap.put("title", new Column("title", "TEXT", false, 0));
                hashMap.put("url", new Column("url", "TEXT", true, 0));
                hashMap.put("view_count", new Column("view_count", "INTEGER", true, 0));
                hashMap.put("last_view_timestamp", new Column("last_view_timestamp", "INTEGER", true, 0));
                hashMap.put("fav_icon_uri", new Column("fav_icon_uri", "TEXT", false, 0));
                HashSet hashSet = new HashSet(0);
                HashSet hashSet2 = new HashSet(1);
                hashSet2.add(new Index("index_browsing_history_view_count", false, Arrays.asList(new String[]{"view_count"})));
                TableInfo tableInfo = new TableInfo("browsing_history", hashMap, hashSet, hashSet2);
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "browsing_history");
                if (!tableInfo.equals(read)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Migration didn't properly handle browsing_history(org.mozilla.focus.history.model.Site).\n Expected:\n");
                    stringBuilder.append(tableInfo);
                    stringBuilder.append("\n Found:\n");
                    stringBuilder.append(read);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }, "ed1350002e11ab61b6b9f2aab52e8ce4", "187c585e7ba70c63c7fcbeb3f99508f2")).build());
    }

    /* Access modifiers changed, original: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "browsing_history");
    }
}
