package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLRPC.TL_channels_inviteToChannel;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$JvtxNBYrSDi77SdNL-OBKU4hIZo */
public final /* synthetic */ class C0631-$$Lambda$MessagesController$JvtxNBYrSDi77SdNL-OBKU4hIZo implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TL_channels_inviteToChannel f$3;

    public /* synthetic */ C0631-$$Lambda$MessagesController$JvtxNBYrSDi77SdNL-OBKU4hIZo(MessagesController messagesController, TL_error tL_error, BaseFragment baseFragment, TL_channels_inviteToChannel tL_channels_inviteToChannel) {
        this.f$0 = messagesController;
        this.f$1 = tL_error;
        this.f$2 = baseFragment;
        this.f$3 = tL_channels_inviteToChannel;
    }

    public final void run() {
        this.f$0.lambda$null$166$MessagesController(this.f$1, this.f$2, this.f$3);
    }
}
