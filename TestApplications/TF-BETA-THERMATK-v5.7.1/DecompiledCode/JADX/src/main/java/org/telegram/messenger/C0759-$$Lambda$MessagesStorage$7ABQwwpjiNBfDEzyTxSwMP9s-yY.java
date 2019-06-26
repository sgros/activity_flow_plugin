package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_updates_channelDifferenceTooLong;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s-yY */
public final /* synthetic */ class C0759-$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s-yY implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ TL_updates_channelDifferenceTooLong f$3;

    public /* synthetic */ C0759-$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s-yY(MessagesStorage messagesStorage, int i, int i2, TL_updates_channelDifferenceTooLong tL_updates_channelDifferenceTooLong) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = tL_updates_channelDifferenceTooLong;
    }

    public final void run() {
        this.f$0.lambda$overwriteChannel$121$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
