package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Dialog;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I */
public final /* synthetic */ class C3470-$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Dialog f$2;
    private final /* synthetic */ long f$3;

    public /* synthetic */ C3470-$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I(MessagesController messagesController, int i, Dialog dialog, long j) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = dialog;
        this.f$3 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$checkLastDialogMessage$136$MessagesController(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
