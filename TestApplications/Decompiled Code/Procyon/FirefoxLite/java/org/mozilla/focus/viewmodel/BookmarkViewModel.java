// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.viewmodel;

import android.arch.lifecycle.ViewModelProvider;
import org.mozilla.focus.persistence.BookmarkModel;
import java.util.List;
import android.arch.lifecycle.LiveData;
import org.mozilla.focus.repository.BookmarkRepository;
import android.arch.lifecycle.ViewModel;

public class BookmarkViewModel extends ViewModel
{
    private BookmarkRepository bookmarkRepository;
    private final LiveData<List<BookmarkModel>> observableBookmarks;
    
    public BookmarkViewModel(final BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.observableBookmarks = bookmarkRepository.loadBookmarks();
    }
    
    public String addBookmark(final String s, final String s2) {
        return this.bookmarkRepository.addBookmark(s, s2);
    }
    
    public void deleteBookmark(final BookmarkModel bookmarkModel) {
        this.bookmarkRepository.deleteBookmark(bookmarkModel);
    }
    
    public void deleteBookmarksByUrl(final String s) {
        this.bookmarkRepository.deleteBookmarksByUrl(s);
    }
    
    public LiveData<BookmarkModel> getBookmarkById(final String s) {
        return this.bookmarkRepository.getBookmarkById(s);
    }
    
    public LiveData<List<BookmarkModel>> getBookmarks() {
        return this.observableBookmarks;
    }
    
    public LiveData<List<BookmarkModel>> getBookmarksByUrl(final String s) {
        return this.bookmarkRepository.getBookmarksByUrl(s);
    }
    
    public void updateBookmark(final BookmarkModel bookmarkModel) {
        this.bookmarkRepository.updateBookmark(bookmarkModel);
    }
    
    public static class Factory extends NewInstanceFactory
    {
        private final BookmarkRepository repository;
        
        public Factory(final BookmarkRepository repository) {
            this.repository = repository;
        }
        
        @Override
        public <T extends ViewModel> T create(final Class<T> clazz) {
            return (T)new BookmarkViewModel(this.repository);
        }
    }
}
