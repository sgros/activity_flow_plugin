package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_channels_createChannel;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$0bxY0b0iZuUAKIyUkrQ28a0391w */
public final /* synthetic */ class C3435-$$Lambda$MessagesController$0bxY0b0iZuUAKIyUkrQ28a0391w implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ BaseFragment f$1;
    private final /* synthetic */ TL_channels_createChannel f$2;

    public /* synthetic */ C3435-$$Lambda$MessagesController$0bxY0b0iZuUAKIyUkrQ28a0391w(MessagesController messagesController, BaseFragment baseFragment, TL_channels_createChannel tL_channels_createChannel) {
        this.f$0 = messagesController;
        this.f$1 = baseFragment;
        this.f$2 = tL_channels_createChannel;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$createChat$160$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
