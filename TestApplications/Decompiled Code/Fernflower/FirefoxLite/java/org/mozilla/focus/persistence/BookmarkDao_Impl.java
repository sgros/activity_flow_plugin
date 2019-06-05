package org.mozilla.focus.persistence;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker;
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

   public BookmarkDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfBookmarkModel = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, BookmarkModel var2) {
            if (var2.getId() == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.getId());
            }

            if (var2.getTitle() == null) {
               var1.bindNull(2);
            } else {
               var1.bindString(2, var2.getTitle());
            }

            if (var2.getUrl() == null) {
               var1.bindNull(3);
            } else {
               var1.bindString(3, var2.getUrl());
            }

         }

         public String createQuery() {
            return "INSERT OR REPLACE INTO `bookmarks`(`id`,`title`,`url`) VALUES (?,?,?)";
         }
      };
      this.__deletionAdapterOfBookmarkModel = new EntityDeletionOrUpdateAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, BookmarkModel var2) {
            if (var2.getId() == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.getId());
            }

         }

         public String createQuery() {
            return "DELETE FROM `bookmarks` WHERE `id` = ?";
         }
      };
      this.__updateAdapterOfBookmarkModel = new EntityDeletionOrUpdateAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, BookmarkModel var2) {
            if (var2.getId() == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.getId());
            }

            if (var2.getTitle() == null) {
               var1.bindNull(2);
            } else {
               var1.bindString(2, var2.getTitle());
            }

            if (var2.getUrl() == null) {
               var1.bindNull(3);
            } else {
               var1.bindString(3, var2.getUrl());
            }

            if (var2.getId() == null) {
               var1.bindNull(4);
            } else {
               var1.bindString(4, var2.getId());
            }

         }

         public String createQuery() {
            return "UPDATE OR ABORT `bookmarks` SET `id` = ?,`title` = ?,`url` = ? WHERE `id` = ?";
         }
      };
      this.__preparedStmtOfDeleteBookmarksByUrl = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "DELETE FROM bookmarks WHERE url = ?";
         }
      };
      this.__preparedStmtOfDeleteAllBookmarks = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "DELETE FROM bookmarks";
         }
      };
   }

   public void addBookmarks(BookmarkModel... var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfBookmarkModel.insert((Object[])var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }

   public void deleteBookmark(BookmarkModel var1) {
      this.__db.beginTransaction();

      try {
         this.__deletionAdapterOfBookmarkModel.handle(var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }

   public void deleteBookmarksByUrl(String var1) {
      SupportSQLiteStatement var2;
      label100: {
         Throwable var10000;
         label99: {
            var2 = this.__preparedStmtOfDeleteBookmarksByUrl.acquire();
            this.__db.beginTransaction();
            boolean var10001;
            if (var1 == null) {
               try {
                  var2.bindNull(1);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label99;
               }
            } else {
               try {
                  var2.bindString(1, var1);
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label99;
               }
            }

            label93:
            try {
               var2.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label100;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label93;
            }
         }

         Throwable var15 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfDeleteBookmarksByUrl.release(var2);
         throw var15;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfDeleteBookmarksByUrl.release(var2);
   }

   public LiveData getBookmarkById(String var1) {
      final RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks WHERE id = ?", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      return (new ComputableLiveData() {
         private InvalidationTracker.Observer _observer;

         protected BookmarkModel compute() {
            if (this._observer == null) {
               this._observer = new InvalidationTracker.Observer("bookmarks", new String[0]) {
                  public void onInvalidated(Set var1) {
                     invalidate();
                  }
               };
               BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
            }

            Cursor var1 = BookmarkDao_Impl.this.__db.query(var2);
            boolean var7 = false;

            BookmarkModel var5;
            label43: {
               try {
                  var7 = true;
                  int var2x = var1.getColumnIndexOrThrow("id");
                  int var3 = var1.getColumnIndexOrThrow("title");
                  int var4 = var1.getColumnIndexOrThrow("url");
                  if (var1.moveToFirst()) {
                     var5 = new BookmarkModel(var1.getString(var2x), var1.getString(var3), var1.getString(var4));
                     var7 = false;
                     break label43;
                  }

                  var7 = false;
               } finally {
                  if (var7) {
                     var1.close();
                  }
               }

               var5 = null;
            }

            var1.close();
            return var5;
         }

         protected void finalize() {
            var2.release();
         }
      }).getLiveData();
   }

   public LiveData getBookmarksByUrl(String var1) {
      final RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT * FROM bookmarks WHERE url = ?", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      return (new ComputableLiveData() {
         private InvalidationTracker.Observer _observer;

         protected List compute() {
            if (this._observer == null) {
               this._observer = new InvalidationTracker.Observer("bookmarks", new String[0]) {
                  public void onInvalidated(Set var1) {
                     invalidate();
                  }
               };
               BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
            }

            Cursor var1 = BookmarkDao_Impl.this.__db.query(var2);

            ArrayList var5;
            label88: {
               Throwable var10000;
               label87: {
                  int var2x;
                  int var3;
                  int var4;
                  boolean var10001;
                  try {
                     var2x = var1.getColumnIndexOrThrow("id");
                     var3 = var1.getColumnIndexOrThrow("title");
                     var4 = var1.getColumnIndexOrThrow("url");
                     var5 = new ArrayList(var1.getCount());
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label87;
                  }

                  while(true) {
                     try {
                        if (!var1.moveToNext()) {
                           break label88;
                        }

                        String var6 = var1.getString(var2x);
                        String var7 = var1.getString(var3);
                        String var16 = var1.getString(var4);
                        BookmarkModel var9 = new BookmarkModel(var6, var7, var16);
                        var5.add(var9);
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                     }
                  }
               }

               Throwable var8 = var10000;
               var1.close();
               throw var8;
            }

            var1.close();
            return var5;
         }

         protected void finalize() {
            var2.release();
         }
      }).getLiveData();
   }

   public LiveData loadBookmarks() {
      return (new ComputableLiveData(RoomSQLiteQuery.acquire("SELECT * FROM bookmarks", 0)) {
         private InvalidationTracker.Observer _observer;
         // $FF: synthetic field
         final RoomSQLiteQuery val$_statement;

         {
            this.val$_statement = var2;
         }

         protected List compute() {
            if (this._observer == null) {
               this._observer = new InvalidationTracker.Observer("bookmarks", new String[0]) {
                  public void onInvalidated(Set var1) {
                     invalidate();
                  }
               };
               BookmarkDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
            }

            Cursor var1 = BookmarkDao_Impl.this.__db.query(this.val$_statement);

            ArrayList var5;
            label88: {
               Throwable var10000;
               label87: {
                  int var2;
                  int var3;
                  int var4;
                  boolean var10001;
                  try {
                     var2 = var1.getColumnIndexOrThrow("id");
                     var3 = var1.getColumnIndexOrThrow("title");
                     var4 = var1.getColumnIndexOrThrow("url");
                     var5 = new ArrayList(var1.getCount());
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label87;
                  }

                  while(true) {
                     try {
                        if (!var1.moveToNext()) {
                           break label88;
                        }

                        String var16 = var1.getString(var2);
                        String var7 = var1.getString(var3);
                        String var8 = var1.getString(var4);
                        BookmarkModel var9 = new BookmarkModel(var16, var7, var8);
                        var5.add(var9);
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                     }
                  }
               }

               Throwable var6 = var10000;
               var1.close();
               throw var6;
            }

            var1.close();
            return var5;
         }

         protected void finalize() {
            this.val$_statement.release();
         }
      }).getLiveData();
   }

   public void updateBookmark(BookmarkModel var1) {
      this.__db.beginTransaction();

      try {
         this.__updateAdapterOfBookmarkModel.handle(var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }
}
