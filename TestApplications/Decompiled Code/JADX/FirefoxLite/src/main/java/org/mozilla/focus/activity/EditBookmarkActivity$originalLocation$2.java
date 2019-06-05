package org.mozilla.focus.activity;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: EditBookmarkActivity.kt */
final class EditBookmarkActivity$originalLocation$2 extends Lambda implements Function0<String> {
    final /* synthetic */ EditBookmarkActivity this$0;

    EditBookmarkActivity$originalLocation$2(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
        super(0);
    }

    public final String invoke() {
        return EditBookmarkActivity.access$getBookmark$p(this.this$0).getUrl();
    }
}
