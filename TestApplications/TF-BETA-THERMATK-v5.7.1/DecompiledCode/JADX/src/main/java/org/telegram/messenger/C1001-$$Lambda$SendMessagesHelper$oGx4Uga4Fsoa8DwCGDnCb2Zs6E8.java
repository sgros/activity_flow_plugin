package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_updateShortSentMessage;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$oGx4Uga4Fsoa8DwCGDnCb2Zs6E8 */
public final /* synthetic */ class C1001-$$Lambda$SendMessagesHelper$oGx4Uga4Fsoa8DwCGDnCb2Zs6E8 implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ TL_updateShortSentMessage f$1;

    public /* synthetic */ C1001-$$Lambda$SendMessagesHelper$oGx4Uga4Fsoa8DwCGDnCb2Zs6E8(SendMessagesHelper sendMessagesHelper, TL_updateShortSentMessage tL_updateShortSentMessage) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = tL_updateShortSentMessage;
    }

    public final void run() {
        this.f$0.lambda$null$33$SendMessagesHelper(this.f$1);
    }
}
