package org.mozilla.focus.activity;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$focusChangeListener$1 implements OnFocusChangeListener {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$focusChangeListener$1(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
    }

    public final void onFocusChange(View view, boolean z) {
        Intrinsics.checkExpressionValueIsNotNull(view, "v");
        int id = view.getId();
        if (id == C0427R.C0426id.bookmark_location) {
            this.this$0.getLabelLocation().setActivated(z);
        } else if (id == C0427R.C0426id.bookmark_name) {
            this.this$0.getLabelName().setActivated(z);
        }
    }
}
