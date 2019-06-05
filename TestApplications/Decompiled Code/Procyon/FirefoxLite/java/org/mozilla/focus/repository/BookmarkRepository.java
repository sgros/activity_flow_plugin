// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.repository;

import java.util.List;
import android.arch.lifecycle.LiveData;
import org.mozilla.threadutils.ThreadUtils;
import java.util.UUID;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.persistence.BookmarksDatabase;

public class BookmarkRepository
{
    private static volatile BookmarkRepository instance;
    private BookmarksDatabase bookmarksDatabase;
    
    private BookmarkRepository(final BookmarksDatabase bookmarksDatabase) {
        this.bookmarksDatabase = bookmarksDatabase;
    }
    
    public static BookmarkRepository getInstance(final BookmarksDatabase bookmarksDatabase) {
        if (BookmarkRepository.instance == null) {
            synchronized (BookmarkRepository.class) {
                if (BookmarkRepository.instance == null) {
                    BookmarkRepository.instance = new BookmarkRepository(bookmarksDatabase);
                }
            }
        }
        return BookmarkRepository.instance;
    }
    
    public String addBookmark(final String s, final String s2) {
        final BookmarkModel bookmarkModel = new BookmarkModel(UUID.randomUUID().toString(), s, s2);
        ThreadUtils.postToBackgroundThread(new _$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog(this, bookmarkModel));
        return bookmarkModel.getId();
    }
    
    public void deleteBookmark(final BookmarkModel bookmarkModel) {
        ThreadUtils.postToBackgroundThread(new _$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk(this, bookmarkModel));
    }
    
    public void deleteBookmarksByUrl(final String s) {
        ThreadUtils.postToBackgroundThread(new _$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y(this, s));
    }
    
    public LiveData<BookmarkModel> getBookmarkById(final String s) {
        return this.bookmarksDatabase.bookmarkDao().getBookmarkById(s);
    }
    
    public LiveData<List<BookmarkModel>> getBookmarksByUrl(final String s) {
        return this.bookmarksDatabase.bookmarkDao().getBookmarksByUrl(s);
    }
    
    public LiveData<List<BookmarkModel>> loadBookmarks() {
        return this.bookmarksDatabase.bookmarkDao().loadBookmarks();
    }
    
    public void updateBookmark(final BookmarkModel bookmarkModel) {
        ThreadUtils.postToBackgroundThread(new _$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0(this, bookmarkModel));
    }
}
