// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.arch.persistence.db.SupportSQLiteQuery;
import java.util.Set;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.lifecycle.ComputableLiveData;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.RoomDatabase;

public class BookmarkDao_Impl implements BookmarkDao
{
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBookmarkModel;
    private final EntityInsertionAdapter __insertionAdapterOfBookmarkModel;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllBookmarks;
    private final SharedSQLiteStatement __preparedStmtOfDeleteBookmarksByUrl;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfBookmarkModel;
    
    public BookmarkDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfBookmarkModel = new EntityInsertionAdapter<BookmarkModel>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final BookmarkModel bookmarkModel) {
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, bookmarkModel.getId());
                }
                if (bookmarkModel.getTitle() == null) {
                    supportSQLiteStatement.bindNull(2);
                }
                else {
                    supportSQLiteStatement.bindString(2, bookmarkModel.getTitle());
                }
                if (bookmarkModel.getUrl() == null) {
                    supportSQLiteStatement.bindNull(3);
                }
                else {
                    supportSQLiteStatement.bindString(3, bookmarkModel.getUrl());
                }
            }
            
            public String createQuery() {
                return "INSERT OR REPLACE INTO `bookmarks`(`id`,`title`,`url`) VALUES (?,?,?)";
            }
        };
        this.__deletionAdapterOfBookmarkModel = new EntityDeletionOrUpdateAdapter<BookmarkModel>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final BookmarkModel bookmarkModel) {
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, bookmarkModel.getId());
                }
            }
            
            public String createQuery() {
                return "DELETE FROM `bookmarks` WHERE `id` = ?";
            }
        };
        this.__updateAdapterOfBookmarkModel = new EntityDeletionOrUpdateAdapter<BookmarkModel>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final BookmarkModel bookmarkModel) {
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, bookmarkModel.getId());
                }
                if (bookmarkModel.getTitle() == null) {
                    supportSQLiteStatement.bindNull(2);
                }
                else {
                    supportSQLiteStatement.bindString(2, bookmarkModel.getTitle());
                }
                if (bookmarkModel.getUrl() == null) {
                    supportSQLiteStatement.bindNull(3);
                }
                else {
                    supportSQLiteStatement.bindString(3, bookmarkModel.getUrl());
                }
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(4);
                }
                else {
                    supportSQLiteStatement.bindString(4, bookmarkModel.getId());
                }
            }
            
            public String createQuery() {
                return "UPDATE OR ABORT `bookmarks` SET `id` = ?,`title` = ?,`url` = ? WHERE `id` = ?";
            }
        };
        this.__preparedStmtOfDeleteBookmarksByUrl = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "DELETE FROM bookmarks WHERE url = ?";
            }
        };
        this.__preparedStmtOfDeleteAllBookmarks = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "DELETE FROM bookmarks";
            }
        };
    }
    
    @Override
    public void addBookmarks(final BookmarkModel... array) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfBookmarkModel.insert(array);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
    
    @Override
    public void deleteBookmark(final BookmarkModel bookmarkModel) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfBookmarkModel.handle(bookmarkModel);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
    
    @Override
    public void deleteBookmarksByUrl(final String s) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteBookmarksByUrl.acquire();
        this.__db.beginTransaction();
        Label_0041: {
            Label_0033: {
                if (s == null) {
                    Label_0071: {
                        try {
                            acquire.bindNull(1);
                            break Label_0041;
                        }
                        finally {
                            break Label_0071;
                        }
                        break Label_0033;
                    }
                    this.__db.endTransaction();
                    this.__preparedStmtOfDeleteBookmarksByUrl.release(acquire);
                }
            }
            acquire.bindString(1, s);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfDeleteBookmarksByUrl.release(acquire);
    }
    
    @Override
    public LiveData<BookmarkModel> getBookmarkById(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks WHERE id = ?", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        return new ComputableLiveData<BookmarkModel>() {
            private InvalidationTracker.Observer _observer;
            
            @Override
            protected BookmarkModel compute() {
                if (this._observer == null) {
                    this._observer = new InvalidationTracker.Observer("bookmarks", new String[0]) {
                        @Override
                        public void onInvalidated(final Set<String> set) {
                            ComputableLiveData.this.invalidate();
                        }
                    };
                    BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                final Cursor query = BookmarkDao_Impl.this.__db.query(acquire);
                try {
                    final int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
                    final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("title");
                    final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("url");
                    BookmarkModel bookmarkModel;
                    if (query.moveToFirst()) {
                        bookmarkModel = new BookmarkModel(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3));
                    }
                    else {
                        bookmarkModel = null;
                    }
                    return bookmarkModel;
                }
                finally {
                    query.close();
                }
            }
            
            @Override
            protected void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }
    
    @Override
    public LiveData<List<BookmarkModel>> getBookmarksByUrl(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks WHERE url = ?", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        return new ComputableLiveData<List<BookmarkModel>>() {
            private InvalidationTracker.Observer _observer;
            
            @Override
            protected List<BookmarkModel> compute() {
                if (this._observer == null) {
                    this._observer = new InvalidationTracker.Observer("bookmarks", new String[0]) {
                        @Override
                        public void onInvalidated(final Set<String> set) {
                            ComputableLiveData.this.invalidate();
                        }
                    };
                    BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                final Cursor query = BookmarkDao_Impl.this.__db.query(acquire);
                try {
                    final int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
                    final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("title");
                    final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("url");
                    final ArrayList list = new ArrayList<BookmarkModel>(query.getCount());
                    while (query.moveToNext()) {
                        list.add(new BookmarkModel(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3)));
                    }
                    return (List<BookmarkModel>)list;
                }
                finally {
                    query.close();
                }
            }
            
            @Override
            protected void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }
    
    @Override
    public LiveData<List<BookmarkModel>> loadBookmarks() {
        return new ComputableLiveData<List<BookmarkModel>>() {
            private InvalidationTracker.Observer _observer;
            final /* synthetic */ RoomSQLiteQuery val$_statement = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks", 0);
            
            @Override
            protected List<BookmarkModel> compute() {
                if (this._observer == null) {
                    this._observer = new InvalidationTracker.Observer("bookmarks", new String[0]) {
                        @Override
                        public void onInvalidated(final Set<String> set) {
                            ComputableLiveData.this.invalidate();
                        }
                    };
                    BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                final Cursor query = BookmarkDao_Impl.this.__db.query(this.val$_statement);
                try {
                    final int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
                    final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("title");
                    final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("url");
                    final ArrayList list = new ArrayList<BookmarkModel>(query.getCount());
                    while (query.moveToNext()) {
                        list.add(new BookmarkModel(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3)));
                    }
                    return (List<BookmarkModel>)list;
                }
                finally {
                    query.close();
                }
            }
            
            @Override
            protected void finalize() {
                this.val$_statement.release();
            }
        }.getLiveData();
    }
    
    @Override
    public void updateBookmark(final BookmarkModel bookmarkModel) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfBookmarkModel.handle(bookmarkModel);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
}
