package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$0RUkjN1o4ur0BN0-Uwykda9jP9E */
public final /* synthetic */ class C3434-$$Lambda$MessagesController$0RUkjN1o4ur0BN0-Uwykda9jP9E implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C3434-$$Lambda$MessagesController$0RUkjN1o4ur0BN0-Uwykda9jP9E(MessagesController messagesController, int i, long j) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$sendTyping$99$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
