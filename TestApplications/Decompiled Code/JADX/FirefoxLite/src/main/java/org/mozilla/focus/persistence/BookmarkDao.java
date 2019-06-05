package org.mozilla.focus.persistence;

import android.arch.lifecycle.LiveData;
import java.util.List;

public interface BookmarkDao {
    void addBookmarks(BookmarkModel... bookmarkModelArr);

    void deleteBookmark(BookmarkModel bookmarkModel);

    void deleteBookmarksByUrl(String str);

    LiveData<BookmarkModel> getBookmarkById(String str);

    LiveData<List<BookmarkModel>> getBookmarksByUrl(String str);

    LiveData<List<BookmarkModel>> loadBookmarks();

    void updateBookmark(BookmarkModel bookmarkModel);
}
