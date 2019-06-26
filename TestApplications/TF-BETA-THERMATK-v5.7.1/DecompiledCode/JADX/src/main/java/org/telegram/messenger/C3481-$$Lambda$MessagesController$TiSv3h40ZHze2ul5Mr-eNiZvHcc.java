package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$TiSv3h40ZHze2ul5Mr-eNiZvHcc */
public final /* synthetic */ class C3481-$$Lambda$MessagesController$TiSv3h40ZHze2ul5Mr-eNiZvHcc implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C3481-$$Lambda$MessagesController$TiSv3h40ZHze2ul5Mr-eNiZvHcc(MessagesController messagesController, int i, int i2) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$getDifference$214$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
