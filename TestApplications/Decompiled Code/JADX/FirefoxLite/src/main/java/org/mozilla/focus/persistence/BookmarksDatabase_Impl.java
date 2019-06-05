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

public class BookmarksDatabase_Impl extends BookmarksDatabase {
    private volatile BookmarkDao _bookmarkDao;

    /* Access modifiers changed, original: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(1) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`id` TEXT NOT NULL, `title` TEXT, `url` TEXT, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"d9d8ebc3eb829f5e9f3e096acd2ce08a\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `bookmarks`");
            }

            /* Access modifiers changed, original: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (BookmarksDatabase_Impl.this.mCallbacks != null) {
                    int size = BookmarksDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) BookmarksDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                BookmarksDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                BookmarksDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (BookmarksDatabase_Impl.this.mCallbacks != null) {
                    int size = BookmarksDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) BookmarksDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* Access modifiers changed, original: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(3);
                hashMap.put("id", new Column("id", "TEXT", true, 1));
                hashMap.put("title", new Column("title", "TEXT", false, 0));
                hashMap.put("url", new Column("url", "TEXT", false, 0));
                TableInfo tableInfo = new TableInfo("bookmarks", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "bookmarks");
                if (!tableInfo.equals(read)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Migration didn't properly handle bookmarks(org.mozilla.focus.persistence.BookmarkModel).\n Expected:\n");
                    stringBuilder.append(tableInfo);
                    stringBuilder.append("\n Found:\n");
                    stringBuilder.append(read);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }, "d9d8ebc3eb829f5e9f3e096acd2ce08a", "30da370378aadd165e18a3e0c60327a5")).build());
    }

    /* Access modifiers changed, original: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "bookmarks");
    }

    public BookmarkDao bookmarkDao() {
        if (this._bookmarkDao != null) {
            return this._bookmarkDao;
        }
        BookmarkDao bookmarkDao;
        synchronized (this) {
            if (this._bookmarkDao == null) {
                this._bookmarkDao = new BookmarkDao_Impl(this);
            }
            bookmarkDao = this._bookmarkDao;
        }
        return bookmarkDao;
    }
}
