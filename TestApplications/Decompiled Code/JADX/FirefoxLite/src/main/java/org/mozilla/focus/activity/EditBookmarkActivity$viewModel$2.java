package org.mozilla.focus.activity;

import android.arch.lifecycle.ViewModelProvider.Factory;
import android.arch.lifecycle.ViewModelProviders;
import android.support.p001v4.app.FragmentActivity;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.viewmodel.BookmarkViewModel;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$viewModel$2 extends Lambda implements Function0<BookmarkViewModel> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$viewModel$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
        super(0);
    }

    public final BookmarkViewModel invoke() {
        return (BookmarkViewModel) ViewModelProviders.m3of((FragmentActivity) this.this$0, (Factory) this.this$0.getViewModelFactory()).get(BookmarkViewModel.class);
    }
}
