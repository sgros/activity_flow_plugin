package org.mozilla.focus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.NewInstanceFactory;
import java.util.List;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.repository.BookmarkRepository;

public class BookmarkViewModel extends ViewModel {
    private BookmarkRepository bookmarkRepository;
    private final LiveData<List<BookmarkModel>> observableBookmarks;

    public static class Factory extends NewInstanceFactory {
        private final BookmarkRepository repository;

        public Factory(BookmarkRepository bookmarkRepository) {
            this.repository = bookmarkRepository;
        }

        public <T extends ViewModel> T create(Class<T> cls) {
            return new BookmarkViewModel(this.repository);
        }
    }

    public BookmarkViewModel(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.observableBookmarks = bookmarkRepository.loadBookmarks();
    }

    public LiveData<List<BookmarkModel>> getBookmarks() {
        return this.observableBookmarks;
    }

    public LiveData<BookmarkModel> getBookmarkById(String str) {
        return this.bookmarkRepository.getBookmarkById(str);
    }

    public LiveData<List<BookmarkModel>> getBookmarksByUrl(String str) {
        return this.bookmarkRepository.getBookmarksByUrl(str);
    }

    public String addBookmark(String str, String str2) {
        return this.bookmarkRepository.addBookmark(str, str2);
    }

    public void updateBookmark(BookmarkModel bookmarkModel) {
        this.bookmarkRepository.updateBookmark(bookmarkModel);
    }

    public void deleteBookmark(BookmarkModel bookmarkModel) {
        this.bookmarkRepository.deleteBookmark(bookmarkModel);
    }

    public void deleteBookmarksByUrl(String str) {
        this.bookmarkRepository.deleteBookmarksByUrl(str);
    }
}
