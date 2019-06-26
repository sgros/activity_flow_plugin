package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$QOjj_wrwvuHdKpPrDByKhoWSuVs */
public final /* synthetic */ class C0981-$$Lambda$SendMessagesHelper$QOjj_wrwvuHdKpPrDByKhoWSuVs implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ TL_updateNewChannelMessage f$1;

    public /* synthetic */ C0981-$$Lambda$SendMessagesHelper$QOjj_wrwvuHdKpPrDByKhoWSuVs(SendMessagesHelper sendMessagesHelper, TL_updateNewChannelMessage tL_updateNewChannelMessage) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = tL_updateNewChannelMessage;
    }

    public final void run() {
        this.f$0.lambda$null$24$SendMessagesHelper(this.f$1);
    }
}
