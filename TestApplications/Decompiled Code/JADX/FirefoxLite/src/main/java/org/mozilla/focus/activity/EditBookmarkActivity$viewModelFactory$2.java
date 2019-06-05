package org.mozilla.focus.activity;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.focus.repository.BookmarkRepository;
import org.mozilla.focus.viewmodel.BookmarkViewModel.Factory;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$viewModelFactory$2 extends Lambda implements Function0<Factory> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$viewModelFactory$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
        super(0);
    }

    public final Factory invoke() {
        return new Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance(this.this$0)));
    }
}
