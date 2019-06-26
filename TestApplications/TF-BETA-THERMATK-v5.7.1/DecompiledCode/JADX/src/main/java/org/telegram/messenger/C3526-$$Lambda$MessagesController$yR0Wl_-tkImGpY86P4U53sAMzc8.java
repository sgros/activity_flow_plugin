package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$yR0Wl_-tkImGpY86P4U53sAMzc8 */
public final /* synthetic */ class C3526-$$Lambda$MessagesController$yR0Wl_-tkImGpY86P4U53sAMzc8 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C3526-$$Lambda$MessagesController$yR0Wl_-tkImGpY86P4U53sAMzc8(MessagesController messagesController, long j, long j2) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = j2;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$deleteDialog$69$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
