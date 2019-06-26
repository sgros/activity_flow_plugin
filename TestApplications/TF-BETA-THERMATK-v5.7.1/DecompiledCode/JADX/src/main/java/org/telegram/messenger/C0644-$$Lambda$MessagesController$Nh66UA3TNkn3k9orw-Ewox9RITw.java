package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_channels_channelParticipant;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$Nh66UA3TNkn3k9orw-Ewox9RITw */
public final /* synthetic */ class C0644-$$Lambda$MessagesController$Nh66UA3TNkn3k9orw-Ewox9RITw implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_channels_channelParticipant f$1;

    public /* synthetic */ C0644-$$Lambda$MessagesController$Nh66UA3TNkn3k9orw-Ewox9RITw(MessagesController messagesController, TL_channels_channelParticipant tL_channels_channelParticipant) {
        this.f$0 = messagesController;
        this.f$1 = tL_channels_channelParticipant;
    }

    public final void run() {
        this.f$0.lambda$null$226$MessagesController(this.f$1);
    }
}
