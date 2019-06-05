package org.mozilla.focus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.repository.BookmarkRepository;

public class BookmarkViewModel extends ViewModel {
   private BookmarkRepository bookmarkRepository;
   private final LiveData observableBookmarks;

   public BookmarkViewModel(BookmarkRepository var1) {
      this.bookmarkRepository = var1;
      this.observableBookmarks = var1.loadBookmarks();
   }

   public String addBookmark(String var1, String var2) {
      return this.bookmarkRepository.addBookmark(var1, var2);
   }

   public void deleteBookmark(BookmarkModel var1) {
      this.bookmarkRepository.deleteBookmark(var1);
   }

   public void deleteBookmarksByUrl(String var1) {
      this.bookmarkRepository.deleteBookmarksByUrl(var1);
   }

   public LiveData getBookmarkById(String var1) {
      return this.bookmarkRepository.getBookmarkById(var1);
   }

   public LiveData getBookmarks() {
      return this.observableBookmarks;
   }

   public LiveData getBookmarksByUrl(String var1) {
      return this.bookmarkRepository.getBookmarksByUrl(var1);
   }

   public void updateBookmark(BookmarkModel var1) {
      this.bookmarkRepository.updateBookmark(var1);
   }

   public static class Factory extends ViewModelProvider.NewInstanceFactory {
      private final BookmarkRepository repository;

      public Factory(BookmarkRepository var1) {
         this.repository = var1;
      }

      public ViewModel create(Class var1) {
         return new BookmarkViewModel(this.repository);
      }
   }
}
