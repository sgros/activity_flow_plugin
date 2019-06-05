package org.mozilla.focus.activity;

import android.widget.EditText;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.C0427R;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$editTextName$2 extends Lambda implements Function0<EditText> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$editTextName$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
        super(0);
    }

    public final EditText invoke() {
        return (EditText) this.this$0.findViewById(C0427R.C0426id.bookmark_name);
    }
}
