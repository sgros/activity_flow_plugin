package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.p004ui.ChangePhoneActivity.LoginActivitySmsView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangePhoneActivity$LoginActivitySmsView$tUraGeWZvqlYJBrGcWDDzAfODJQ */
public final /* synthetic */ class C1247xe61975aa implements OnEditorActionListener {
    private final /* synthetic */ LoginActivitySmsView f$0;

    public /* synthetic */ C1247xe61975aa(LoginActivitySmsView loginActivitySmsView) {
        this.f$0 = loginActivitySmsView;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$setParams$5$ChangePhoneActivity$LoginActivitySmsView(textView, i, keyEvent);
    }
}
