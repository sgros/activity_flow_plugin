package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLRPC.TL_channels_createChannel;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$1QEtWqNTIuN7FfcY0dyqyX9a0nc */
public final /* synthetic */ class C0583-$$Lambda$MessagesController$1QEtWqNTIuN7FfcY0dyqyX9a0nc implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TL_channels_createChannel f$3;

    public /* synthetic */ C0583-$$Lambda$MessagesController$1QEtWqNTIuN7FfcY0dyqyX9a0nc(MessagesController messagesController, TL_error tL_error, BaseFragment baseFragment, TL_channels_createChannel tL_channels_createChannel) {
        this.f$0 = messagesController;
        this.f$1 = tL_error;
        this.f$2 = baseFragment;
        this.f$3 = tL_channels_createChannel;
    }

    public final void run() {
        this.f$0.lambda$null$158$MessagesController(this.f$1, this.f$2, this.f$3);
    }
}
