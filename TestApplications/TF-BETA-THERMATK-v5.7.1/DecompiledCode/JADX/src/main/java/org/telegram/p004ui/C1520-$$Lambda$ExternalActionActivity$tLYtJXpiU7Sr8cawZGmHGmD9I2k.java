package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k */
public final /* synthetic */ class C1520-$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k implements OnDismissListener {
    private final /* synthetic */ ExternalActionActivity f$0;
    private final /* synthetic */ TL_error f$1;

    public /* synthetic */ C1520-$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k(ExternalActionActivity externalActionActivity, TL_error tL_error) {
        this.f$0 = externalActionActivity;
        this.f$1 = tL_error;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$null$8$ExternalActionActivity(this.f$1, dialogInterface);
    }
}
