package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatEditActivity$p1fZRHy8NDyNuO213khwXU229Jc */
public final /* synthetic */ class C1403-$$Lambda$ChatEditActivity$p1fZRHy8NDyNuO213khwXU229Jc implements OnEditorActionListener {
    private final /* synthetic */ ChatEditActivity f$0;

    public /* synthetic */ C1403-$$Lambda$ChatEditActivity$p1fZRHy8NDyNuO213khwXU229Jc(ChatEditActivity chatEditActivity) {
        this.f$0 = chatEditActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$4$ChatEditActivity(textView, i, keyEvent);
    }
}
