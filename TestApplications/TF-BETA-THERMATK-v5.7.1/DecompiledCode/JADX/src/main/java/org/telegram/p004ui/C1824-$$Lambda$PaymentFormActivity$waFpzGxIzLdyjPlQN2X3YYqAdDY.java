package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$waFpzGxIzLdyjPlQN2X3YYqAdDY */
public final /* synthetic */ class C1824-$$Lambda$PaymentFormActivity$waFpzGxIzLdyjPlQN2X3YYqAdDY implements OnEditorActionListener {
    private final /* synthetic */ PaymentFormActivity f$0;

    public /* synthetic */ C1824-$$Lambda$PaymentFormActivity$waFpzGxIzLdyjPlQN2X3YYqAdDY(PaymentFormActivity paymentFormActivity) {
        this.f$0 = paymentFormActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$2$PaymentFormActivity(textView, i, keyEvent);
    }
}
