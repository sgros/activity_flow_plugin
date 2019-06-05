package org.mozilla.focus.repository;

import android.arch.lifecycle.LiveData;
import java.util.List;
import java.util.UUID;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.threadutils.ThreadUtils;

public class BookmarkRepository {
    private static volatile BookmarkRepository instance;
    private BookmarksDatabase bookmarksDatabase;

    private BookmarkRepository(BookmarksDatabase bookmarksDatabase) {
        this.bookmarksDatabase = bookmarksDatabase;
    }

    public static BookmarkRepository getInstance(BookmarksDatabase bookmarksDatabase) {
        if (instance == null) {
            synchronized (BookmarkRepository.class) {
                if (instance == null) {
                    instance = new BookmarkRepository(bookmarksDatabase);
                }
            }
        }
        return instance;
    }

    public LiveData<List<BookmarkModel>> loadBookmarks() {
        return this.bookmarksDatabase.bookmarkDao().loadBookmarks();
    }

    public LiveData<BookmarkModel> getBookmarkById(String str) {
        return this.bookmarksDatabase.bookmarkDao().getBookmarkById(str);
    }

    public LiveData<List<BookmarkModel>> getBookmarksByUrl(String str) {
        return this.bookmarksDatabase.bookmarkDao().getBookmarksByUrl(str);
    }

    public String addBookmark(String str, String str2) {
        BookmarkModel bookmarkModel = new BookmarkModel(UUID.randomUUID().toString(), str, str2);
        ThreadUtils.postToBackgroundThread(new C0501-$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog(this, bookmarkModel));
        return bookmarkModel.getId();
    }

    public void updateBookmark(BookmarkModel bookmarkModel) {
        ThreadUtils.postToBackgroundThread(new C0504-$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0(this, bookmarkModel));
    }

    public void deleteBookmark(BookmarkModel bookmarkModel) {
        ThreadUtils.postToBackgroundThread(new C0502-$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk(this, bookmarkModel));
    }

    public void deleteBookmarksByUrl(String str) {
        ThreadUtils.postToBackgroundThread(new C0503-$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y(this, str));
    }
}
