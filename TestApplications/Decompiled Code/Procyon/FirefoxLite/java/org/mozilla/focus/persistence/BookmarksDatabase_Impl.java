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
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.db.SupportSQLiteDatabase;
import java.util.List;

public class BookmarksDatabase_Impl extends BookmarksDatabase
{
    private volatile BookmarkDao _bookmarkDao;
    
    @Override
    public BookmarkDao bookmarkDao() {
        if (this._bookmarkDao != null) {
            return this._bookmarkDao;
        }
        synchronized (this) {
            if (this._bookmarkDao == null) {
                this._bookmarkDao = new BookmarkDao_Impl(this);
            }
            return this._bookmarkDao;
        }
    }
    
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new String[] { "bookmarks" });
    }
    
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, (RoomOpenHelper.Delegate)new RoomOpenHelper.Delegate(1) {
            public void createAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`id` TEXT NOT NULL, `title` TEXT, `url` TEXT, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"d9d8ebc3eb829f5e9f3e096acd2ce08a\")");
            }
            
            public void dropAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `bookmarks`");
            }
            
            @Override
            protected void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                if (BookmarksDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < BookmarksDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)BookmarksDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }
            
            public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
                BookmarksDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                RoomDatabase.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (BookmarksDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < BookmarksDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)BookmarksDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }
            
            @Override
            protected void validateMigration(final SupportSQLiteDatabase supportSQLiteDatabase) {
                final HashMap<String, TableInfo.Column> hashMap = new HashMap<String, TableInfo.Column>(3);
                hashMap.put("id", new TableInfo.Column("id", "TEXT", true, 1));
                hashMap.put("title", new TableInfo.Column("title", "TEXT", false, 0));
                hashMap.put("url", new TableInfo.Column("url", "TEXT", false, 0));
                final TableInfo obj = new TableInfo("bookmarks", hashMap, new HashSet<TableInfo.ForeignKey>(0), new HashSet<TableInfo.Index>(0));
                final TableInfo read = TableInfo.read(supportSQLiteDatabase, "bookmarks");
                if (obj.equals(read)) {
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Migration didn't properly handle bookmarks(org.mozilla.focus.persistence.BookmarkModel).\n Expected:\n");
                sb.append(obj);
                sb.append("\n Found:\n");
                sb.append(read);
                throw new IllegalStateException(sb.toString());
            }
        }, "d9d8ebc3eb829f5e9f3e096acd2ce08a", "30da370378aadd165e18a3e0c60327a5")).build());
    }
}
