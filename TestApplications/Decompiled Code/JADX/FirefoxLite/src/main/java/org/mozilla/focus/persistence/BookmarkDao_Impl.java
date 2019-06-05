package org.mozilla.focus.persistence;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookmarkDao_Impl implements BookmarkDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBookmarkModel;
    private final EntityInsertionAdapter __insertionAdapterOfBookmarkModel;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllBookmarks;
    private final SharedSQLiteStatement __preparedStmtOfDeleteBookmarksByUrl;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfBookmarkModel;

    public BookmarkDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfBookmarkModel = new EntityInsertionAdapter<BookmarkModel>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `bookmarks`(`id`,`title`,`url`) VALUES (?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, BookmarkModel bookmarkModel) {
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, bookmarkModel.getId());
                }
                if (bookmarkModel.getTitle() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, bookmarkModel.getTitle());
                }
                if (bookmarkModel.getUrl() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, bookmarkModel.getUrl());
                }
            }
        };
        this.__deletionAdapterOfBookmarkModel = new EntityDeletionOrUpdateAdapter<BookmarkModel>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `bookmarks` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, BookmarkModel bookmarkModel) {
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, bookmarkModel.getId());
                }
            }
        };
        this.__updateAdapterOfBookmarkModel = new EntityDeletionOrUpdateAdapter<BookmarkModel>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `bookmarks` SET `id` = ?,`title` = ?,`url` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, BookmarkModel bookmarkModel) {
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, bookmarkModel.getId());
                }
                if (bookmarkModel.getTitle() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, bookmarkModel.getTitle());
                }
                if (bookmarkModel.getUrl() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, bookmarkModel.getUrl());
                }
                if (bookmarkModel.getId() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, bookmarkModel.getId());
                }
            }
        };
        this.__preparedStmtOfDeleteBookmarksByUrl = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM bookmarks WHERE url = ?";
            }
        };
        this.__preparedStmtOfDeleteAllBookmarks = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM bookmarks";
            }
        };
    }

    public void addBookmarks(BookmarkModel... bookmarkModelArr) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfBookmarkModel.insert((Object[]) bookmarkModelArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteBookmark(BookmarkModel bookmarkModel) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfBookmarkModel.handle(bookmarkModel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateBookmark(BookmarkModel bookmarkModel) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfBookmarkModel.handle(bookmarkModel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteBookmarksByUrl(String str) {
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteBookmarksByUrl.acquire();
        this.__db.beginTransaction();
        if (str == null) {
            try {
                acquire.bindNull(1);
            } catch (Throwable th) {
                this.__db.endTransaction();
                this.__preparedStmtOfDeleteBookmarksByUrl.release(acquire);
            }
        } else {
            acquire.bindString(1, str);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfDeleteBookmarksByUrl.release(acquire);
    }

    public LiveData<List<BookmarkModel>> loadBookmarks() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks", 0);
        return new ComputableLiveData<List<BookmarkModel>>() {
            private Observer _observer;

            /* Access modifiers changed, original: protected */
            public List<BookmarkModel> compute() {
                if (this._observer == null) {
                    this._observer = new Observer("bookmarks", new String[0]) {
                        public void onInvalidated(Set<String> set) {
                            C07216.this.invalidate();
                        }
                    };
                    BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                Cursor query = BookmarkDao_Impl.this.__db.query(acquire);
                try {
                    int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
                    int columnIndexOrThrow2 = query.getColumnIndexOrThrow("title");
                    int columnIndexOrThrow3 = query.getColumnIndexOrThrow("url");
                    List<BookmarkModel> arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new BookmarkModel(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3)));
                    }
                    return arrayList;
                } finally {
                    query.close();
                }
            }

            /* Access modifiers changed, original: protected */
            public void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }

    public LiveData<BookmarkModel> getBookmarkById(String str) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks WHERE id = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        return new ComputableLiveData<BookmarkModel>() {
            private Observer _observer;

            /* Access modifiers changed, original: protected */
            public BookmarkModel compute() {
                if (this._observer == null) {
                    this._observer = new Observer("bookmarks", new String[0]) {
                        public void onInvalidated(Set<String> set) {
                            C07237.this.invalidate();
                        }
                    };
                    BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                Cursor query = BookmarkDao_Impl.this.__db.query(acquire);
                try {
                    BookmarkModel bookmarkModel = query.moveToFirst() ? new BookmarkModel(query.getString(query.getColumnIndexOrThrow("id")), query.getString(query.getColumnIndexOrThrow("title")), query.getString(query.getColumnIndexOrThrow("url"))) : null;
                    query.close();
                    return bookmarkModel;
                } catch (Throwable th) {
                    query.close();
                }
            }

            /* Access modifiers changed, original: protected */
            public void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }

    public LiveData<List<BookmarkModel>> getBookmarksByUrl(String str) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks WHERE url = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        return new ComputableLiveData<List<BookmarkModel>>() {
            private Observer _observer;

            /* Access modifiers changed, original: protected */
            public List<BookmarkModel> compute() {
                if (this._observer == null) {
                    this._observer = new Observer("bookmarks", new String[0]) {
                        public void onInvalidated(Set<String> set) {
                            C07258.this.invalidate();
                        }
                    };
                    BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                Cursor query = BookmarkDao_Impl.this.__db.query(acquire);
                try {
                    int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
                    int columnIndexOrThrow2 = query.getColumnIndexOrThrow("title");
                    int columnIndexOrThrow3 = query.getColumnIndexOrThrow("url");
                    List<BookmarkModel> arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new BookmarkModel(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3)));
                    }
                    return arrayList;
                } finally {
                    query.close();
                }
            }

            /* Access modifiers changed, original: protected */
            public void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }
}
