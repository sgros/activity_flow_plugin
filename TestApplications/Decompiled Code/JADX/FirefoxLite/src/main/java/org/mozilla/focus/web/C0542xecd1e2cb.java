package org.mozilla.focus.web;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.web.-$$Lambda$HttpAuthenticationDialogBuilder$OWVDbtV3vCIyTwaiMgH1w9Yb7EU */
public final /* synthetic */ class C0542xecd1e2cb implements OnEditorActionListener {
    private final /* synthetic */ HttpAuthenticationDialogBuilder f$0;

    public /* synthetic */ C0542xecd1e2cb(HttpAuthenticationDialogBuilder httpAuthenticationDialogBuilder) {
        this.f$0 = httpAuthenticationDialogBuilder;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return HttpAuthenticationDialogBuilder.lambda$createDialog$0(this.f$0, textView, i, keyEvent);
    }
}
