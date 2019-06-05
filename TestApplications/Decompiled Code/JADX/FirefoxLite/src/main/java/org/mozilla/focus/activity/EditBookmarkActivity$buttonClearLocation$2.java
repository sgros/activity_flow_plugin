package org.mozilla.focus.activity;

import android.widget.ImageButton;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.C0427R;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$buttonClearLocation$2 extends Lambda implements Function0<ImageButton> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$buttonClearLocation$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
        super(0);
    }

    public final ImageButton invoke() {
        return (ImageButton) this.this$0.findViewById(C0427R.C0426id.bookmark_location_clear);
    }
}
