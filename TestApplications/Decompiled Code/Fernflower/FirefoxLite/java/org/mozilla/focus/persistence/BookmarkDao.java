package org.mozilla.focus.persistence;

import android.arch.lifecycle.LiveData;

public interface BookmarkDao {
   void addBookmarks(BookmarkModel... var1);

   void deleteBookmark(BookmarkModel var1);

   void deleteBookmarksByUrl(String var1);

   LiveData getBookmarkById(String var1);

   LiveData getBookmarksByUrl(String var1);

   LiveData loadBookmarks();

   void updateBookmark(BookmarkModel var1);
}
