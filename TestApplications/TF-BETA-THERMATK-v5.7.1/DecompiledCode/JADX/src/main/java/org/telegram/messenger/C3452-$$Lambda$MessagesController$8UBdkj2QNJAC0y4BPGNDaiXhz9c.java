package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$8UBdkj2QNJAC0y4BPGNDaiXhz9c */
public final /* synthetic */ class C3452-$$Lambda$MessagesController$8UBdkj2QNJAC0y4BPGNDaiXhz9c implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Chat f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C3452-$$Lambda$MessagesController$8UBdkj2QNJAC0y4BPGNDaiXhz9c(MessagesController messagesController, Chat chat, int i) {
        this.f$0 = messagesController;
        this.f$1 = chat;
        this.f$2 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$230$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
