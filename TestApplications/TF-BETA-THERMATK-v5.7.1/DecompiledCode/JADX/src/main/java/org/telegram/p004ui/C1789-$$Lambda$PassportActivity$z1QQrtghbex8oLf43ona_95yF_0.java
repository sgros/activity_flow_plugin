package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$z1QQrtghbex8oLf43ona_95yF_0 */
public final /* synthetic */ class C1789-$$Lambda$PassportActivity$z1QQrtghbex8oLf43ona_95yF_0 implements OnEditorActionListener {
    private final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ C1789-$$Lambda$PassportActivity$z1QQrtghbex8oLf43ona_95yF_0(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createEmailVerificationInterface$5$PassportActivity(textView, i, keyEvent);
    }
}
