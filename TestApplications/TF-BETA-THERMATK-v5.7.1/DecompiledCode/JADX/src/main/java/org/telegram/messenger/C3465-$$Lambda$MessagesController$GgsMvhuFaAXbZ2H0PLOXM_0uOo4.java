package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4 */
public final /* synthetic */ class C3465-$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ long f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;
    private final /* synthetic */ boolean f$5;
    private final /* synthetic */ InputPeer f$6;

    public /* synthetic */ C3465-$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4(MessagesController messagesController, long j, long j2, int i, int i2, boolean z, InputPeer inputPeer) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = j2;
        this.f$3 = i;
        this.f$4 = i2;
        this.f$5 = z;
        this.f$6 = inputPeer;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$deleteDialog$70$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, tLObject, tL_error);
    }
}
