package org.mozilla.focus.activity;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$onCreate$2 implements OnClickListener {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$onCreate$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
    }

    public final void onClick(View view) {
        this.this$0.getEditTextLocation().getText().clear();
    }
}
