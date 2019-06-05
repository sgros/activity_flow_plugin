package org.mozilla.focus.activity;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$onCreate$1 implements OnClickListener {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$onCreate$1(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
    }

    public final void onClick(View view) {
        this.this$0.getEditTextName().getText().clear();
    }
}
