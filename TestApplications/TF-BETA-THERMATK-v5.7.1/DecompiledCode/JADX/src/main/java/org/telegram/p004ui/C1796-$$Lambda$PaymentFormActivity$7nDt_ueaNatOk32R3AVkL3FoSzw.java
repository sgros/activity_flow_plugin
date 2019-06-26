package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$7nDt_ueaNatOk32R3AVkL3FoSzw */
public final /* synthetic */ class C1796-$$Lambda$PaymentFormActivity$7nDt_ueaNatOk32R3AVkL3FoSzw implements OnEditorActionListener {
    private final /* synthetic */ PaymentFormActivity f$0;

    public /* synthetic */ C1796-$$Lambda$PaymentFormActivity$7nDt_ueaNatOk32R3AVkL3FoSzw(PaymentFormActivity paymentFormActivity) {
        this.f$0 = paymentFormActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$11$PaymentFormActivity(textView, i, keyEvent);
    }
}
