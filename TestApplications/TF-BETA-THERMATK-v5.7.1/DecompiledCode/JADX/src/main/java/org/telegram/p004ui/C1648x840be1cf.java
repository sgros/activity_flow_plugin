package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.p004ui.LoginActivity.LoginActivitySmsView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivitySmsView$f1ikQgn9Rce7rxNwD6eW9NGEWc4 */
public final /* synthetic */ class C1648x840be1cf implements OnEditorActionListener {
    private final /* synthetic */ LoginActivitySmsView f$0;

    public /* synthetic */ C1648x840be1cf(LoginActivitySmsView loginActivitySmsView) {
        this.f$0 = loginActivitySmsView;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$setParams$4$LoginActivity$LoginActivitySmsView(textView, i, keyEvent);
    }
}
