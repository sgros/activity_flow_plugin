package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$lpvHitH1qNbCguTGgQkc2rG8PIQ */
public final /* synthetic */ class C1781-$$Lambda$PassportActivity$lpvHitH1qNbCguTGgQkc2rG8PIQ implements OnEditorActionListener {
    private final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ C1781-$$Lambda$PassportActivity$lpvHitH1qNbCguTGgQkc2rG8PIQ(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createEmailInterface$25$PassportActivity(textView, i, keyEvent);
    }
}
