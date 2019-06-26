package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$zabhB89XpHUfevzEww0brDV8Dzs */
public final /* synthetic */ class C1827-$$Lambda$PaymentFormActivity$zabhB89XpHUfevzEww0brDV8Dzs implements OnEditorActionListener {
    private final /* synthetic */ PaymentFormActivity f$0;

    public /* synthetic */ C1827-$$Lambda$PaymentFormActivity$zabhB89XpHUfevzEww0brDV8Dzs(PaymentFormActivity paymentFormActivity) {
        this.f$0 = paymentFormActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$16$PaymentFormActivity(textView, i, keyEvent);
    }
}
