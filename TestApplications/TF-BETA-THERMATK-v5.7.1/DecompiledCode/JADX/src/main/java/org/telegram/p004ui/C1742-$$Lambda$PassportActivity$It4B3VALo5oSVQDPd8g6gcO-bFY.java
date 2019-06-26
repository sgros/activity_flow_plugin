package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$It4B3VALo5oSVQDPd8g6gcO-bFY */
public final /* synthetic */ class C1742-$$Lambda$PassportActivity$It4B3VALo5oSVQDPd8g6gcO-bFY implements OnEditorActionListener {
    private final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ C1742-$$Lambda$PassportActivity$It4B3VALo5oSVQDPd8g6gcO-bFY(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createPasswordInterface$7$PassportActivity(textView, i, keyEvent);
    }
}
