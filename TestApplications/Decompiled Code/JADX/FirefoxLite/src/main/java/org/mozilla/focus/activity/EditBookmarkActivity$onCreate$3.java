package org.mozilla.focus.activity;

import android.arch.lifecycle.Observer;
import org.mozilla.focus.persistence.BookmarkModel;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$onCreate$3<T> implements Observer<BookmarkModel> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$onCreate$3(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
    }

    public final void onChanged(BookmarkModel bookmarkModel) {
        if (bookmarkModel != null) {
            this.this$0.bookmark = bookmarkModel;
            this.this$0.getEditTextName().setText(EditBookmarkActivity.access$getBookmark$p(this.this$0).getTitle());
            this.this$0.getEditTextLocation().setText(EditBookmarkActivity.access$getBookmark$p(this.this$0).getUrl());
        }
    }
}
