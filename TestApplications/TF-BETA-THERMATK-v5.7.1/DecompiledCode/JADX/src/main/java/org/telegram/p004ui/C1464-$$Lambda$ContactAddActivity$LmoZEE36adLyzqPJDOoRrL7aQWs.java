package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ContactAddActivity$LmoZEE36adLyzqPJDOoRrL7aQWs */
public final /* synthetic */ class C1464-$$Lambda$ContactAddActivity$LmoZEE36adLyzqPJDOoRrL7aQWs implements OnEditorActionListener {
    private final /* synthetic */ ContactAddActivity f$0;

    public /* synthetic */ C1464-$$Lambda$ContactAddActivity$LmoZEE36adLyzqPJDOoRrL7aQWs(ContactAddActivity contactAddActivity) {
        this.f$0 = contactAddActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$2$ContactAddActivity(textView, i, keyEvent);
    }
}
