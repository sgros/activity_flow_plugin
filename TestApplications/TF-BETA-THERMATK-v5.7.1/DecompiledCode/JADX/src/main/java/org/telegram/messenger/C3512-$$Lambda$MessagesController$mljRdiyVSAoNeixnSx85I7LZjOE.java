package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$mljRdiyVSAoNeixnSx85I7LZjOE */
public final /* synthetic */ class C3512-$$Lambda$MessagesController$mljRdiyVSAoNeixnSx85I7LZjOE implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ long f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ C3512-$$Lambda$MessagesController$mljRdiyVSAoNeixnSx85I7LZjOE(MessagesController messagesController, int i, int i2, long j, int i3) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = j;
        this.f$4 = i3;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadDialogPhotos$38$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tL_error);
    }
}
