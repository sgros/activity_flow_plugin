package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.CancelAccountDeletionActivity.PhoneView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_sendConfirmPhoneCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CancelAccountDeletionActivity$PhoneView$P6GwpG9TiT0cGhXBWpHiOCU7n94 */
public final /* synthetic */ class C1232x87074d3 implements Runnable {
    private final /* synthetic */ PhoneView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ Bundle f$2;
    private final /* synthetic */ TLObject f$3;
    private final /* synthetic */ TL_account_sendConfirmPhoneCode f$4;

    public /* synthetic */ C1232x87074d3(PhoneView phoneView, TL_error tL_error, Bundle bundle, TLObject tLObject, TL_account_sendConfirmPhoneCode tL_account_sendConfirmPhoneCode) {
        this.f$0 = phoneView;
        this.f$1 = tL_error;
        this.f$2 = bundle;
        this.f$3 = tLObject;
        this.f$4 = tL_account_sendConfirmPhoneCode;
    }

    public final void run() {
        this.f$0.lambda$null$0$CancelAccountDeletionActivity$PhoneView(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
