package org.mozilla.focus.activity;

import android.widget.TextView;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.C0427R;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$labelName$2 extends Lambda implements Function0<TextView> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$labelName$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
        super(0);
    }

    public final TextView invoke() {
        return (TextView) this.this$0.findViewById(C0427R.C0426id.bookmark_name_label);
    }
}
