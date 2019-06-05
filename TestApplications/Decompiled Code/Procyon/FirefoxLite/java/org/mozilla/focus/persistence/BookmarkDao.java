// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import java.util.List;
import android.arch.lifecycle.LiveData;

public interface BookmarkDao
{
    void addBookmarks(final BookmarkModel... p0);
    
    void deleteBookmark(final BookmarkModel p0);
    
    void deleteBookmarksByUrl(final String p0);
    
    LiveData<BookmarkModel> getBookmarkById(final String p0);
    
    LiveData<List<BookmarkModel>> getBookmarksByUrl(final String p0);
    
    LiveData<List<BookmarkModel>> loadBookmarks();
    
    void updateBookmark(final BookmarkModel p0);
}
