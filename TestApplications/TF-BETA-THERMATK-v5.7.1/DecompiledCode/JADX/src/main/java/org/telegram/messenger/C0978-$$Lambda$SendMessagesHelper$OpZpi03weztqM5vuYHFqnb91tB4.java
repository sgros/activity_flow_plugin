package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Message;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$OpZpi03weztqM5vuYHFqnb91tB4 */
public final /* synthetic */ class C0978-$$Lambda$SendMessagesHelper$OpZpi03weztqM5vuYHFqnb91tB4 implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ Message f$1;

    public /* synthetic */ C0978-$$Lambda$SendMessagesHelper$OpZpi03weztqM5vuYHFqnb91tB4(SendMessagesHelper sendMessagesHelper, Message message) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = message;
    }

    public final void run() {
        this.f$0.lambda$null$8$SendMessagesHelper(this.f$1);
    }
}
