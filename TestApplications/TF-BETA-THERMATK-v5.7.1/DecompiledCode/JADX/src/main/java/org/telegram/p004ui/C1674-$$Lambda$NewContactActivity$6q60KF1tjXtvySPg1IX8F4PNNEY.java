package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$NewContactActivity$6q60KF1tjXtvySPg1IX8F4PNNEY */
public final /* synthetic */ class C1674-$$Lambda$NewContactActivity$6q60KF1tjXtvySPg1IX8F4PNNEY implements OnEditorActionListener {
    private final /* synthetic */ NewContactActivity f$0;

    public /* synthetic */ C1674-$$Lambda$NewContactActivity$6q60KF1tjXtvySPg1IX8F4PNNEY(NewContactActivity newContactActivity) {
        this.f$0 = newContactActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$7$NewContactActivity(textView, i, keyEvent);
    }
}
