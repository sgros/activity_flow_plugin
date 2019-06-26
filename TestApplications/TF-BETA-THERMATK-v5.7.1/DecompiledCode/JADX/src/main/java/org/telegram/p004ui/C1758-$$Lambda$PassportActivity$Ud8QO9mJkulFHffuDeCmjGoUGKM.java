package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$Ud8QO9mJkulFHffuDeCmjGoUGKM */
public final /* synthetic */ class C1758-$$Lambda$PassportActivity$Ud8QO9mJkulFHffuDeCmjGoUGKM implements OnEditorActionListener {
    private final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ C1758-$$Lambda$PassportActivity$Ud8QO9mJkulFHffuDeCmjGoUGKM(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createPhoneInterface$30$PassportActivity(textView, i, keyEvent);
    }
}
