package org.mozilla.focus.activity;

import android.text.Editable;
import android.text.TextWatcher;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: EditBookmarkActivity.kt */
public final class EditBookmarkActivity$nameWatcher$1 implements TextWatcher {
    final /* synthetic */ EditBookmarkActivity this$0;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    EditBookmarkActivity$nameWatcher$1(EditBookmarkActivity editBookmarkActivity) {
        this.this$0 = editBookmarkActivity;
    }

    public void afterTextChanged(Editable editable) {
        if (this.this$0.bookmark != null) {
            this.this$0.nameChanged = Intrinsics.areEqual(String.valueOf(editable), this.this$0.getOriginalName()) ^ 1;
            this.this$0.setupMenuItemSave();
        }
    }
}
