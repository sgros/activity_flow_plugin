package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$PasscodeView$TCcTv4oyYUzBTnrh4HFlzxWR-gM */
public final /* synthetic */ class C2613-$$Lambda$PasscodeView$TCcTv4oyYUzBTnrh4HFlzxWR-gM implements OnDismissListener {
    private final /* synthetic */ PasscodeView f$0;

    public /* synthetic */ C2613-$$Lambda$PasscodeView$TCcTv4oyYUzBTnrh4HFlzxWR-gM(PasscodeView passcodeView) {
        this.f$0 = passcodeView;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$checkFingerprint$5$PasscodeView(dialogInterface);
    }
}
