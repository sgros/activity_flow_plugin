package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.NewContactActivity.C42411;
import org.telegram.tgnet.TLRPC.TL_inputPhoneContact;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$NewContactActivity$1$D3bcNIiNfpYsfTq2W2DLeJJEU84 */
public final /* synthetic */ class C1671-$$Lambda$NewContactActivity$1$D3bcNIiNfpYsfTq2W2DLeJJEU84 implements OnClickListener {
    private final /* synthetic */ C42411 f$0;
    private final /* synthetic */ TL_inputPhoneContact f$1;

    public /* synthetic */ C1671-$$Lambda$NewContactActivity$1$D3bcNIiNfpYsfTq2W2DLeJJEU84(C42411 c42411, TL_inputPhoneContact tL_inputPhoneContact) {
        this.f$0 = c42411;
        this.f$1 = tL_inputPhoneContact;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$0$NewContactActivity$1(this.f$1, dialogInterface, i);
    }
}
