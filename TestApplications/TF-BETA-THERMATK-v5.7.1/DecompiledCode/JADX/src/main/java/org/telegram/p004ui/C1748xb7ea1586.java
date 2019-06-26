package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.p004ui.PassportActivity.PhoneConfirmationView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$PhoneConfirmationView$IfZlFmJVSS8IJyDQ8zJhpbvhePs */
public final /* synthetic */ class C1748xb7ea1586 implements OnEditorActionListener {
    private final /* synthetic */ PhoneConfirmationView f$0;

    public /* synthetic */ C1748xb7ea1586(PhoneConfirmationView phoneConfirmationView) {
        this.f$0 = phoneConfirmationView;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$setParams$5$PassportActivity$PhoneConfirmationView(textView, i, keyEvent);
    }
}
