package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_updateNewMessage;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$L5hF2IhINUG6nIA5dSvAtaxsz8k */
public final /* synthetic */ class C0972-$$Lambda$SendMessagesHelper$L5hF2IhINUG6nIA5dSvAtaxsz8k implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ TL_updateNewMessage f$1;

    public /* synthetic */ C0972-$$Lambda$SendMessagesHelper$L5hF2IhINUG6nIA5dSvAtaxsz8k(SendMessagesHelper sendMessagesHelper, TL_updateNewMessage tL_updateNewMessage) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = tL_updateNewMessage;
    }

    public final void run() {
        this.f$0.lambda$null$34$SendMessagesHelper(this.f$1);
    }
}
