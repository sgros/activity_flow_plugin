package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_createChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$BwCRJLOHKlBHZCobPU1cBtbarBc */
public final /* synthetic */ class C3461-$$Lambda$MessagesController$BwCRJLOHKlBHZCobPU1cBtbarBc implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ BaseFragment f$1;
    private final /* synthetic */ TL_messages_createChat f$2;

    public /* synthetic */ C3461-$$Lambda$MessagesController$BwCRJLOHKlBHZCobPU1cBtbarBc(MessagesController messagesController, BaseFragment baseFragment, TL_messages_createChat tL_messages_createChat) {
        this.f$0 = messagesController;
        this.f$1 = baseFragment;
        this.f$2 = tL_messages_createChat;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$createChat$157$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
