package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputMedia;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$TtlUVA9H7pRWgwj4hEtQKuSUaGg */
public final /* synthetic */ class C0984-$$Lambda$SendMessagesHelper$TtlUVA9H7pRWgwj4hEtQKuSUaGg implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ InputMedia f$2;
    private final /* synthetic */ DelayedMessage f$3;

    public /* synthetic */ C0984-$$Lambda$SendMessagesHelper$TtlUVA9H7pRWgwj4hEtQKuSUaGg(SendMessagesHelper sendMessagesHelper, TLObject tLObject, InputMedia inputMedia, DelayedMessage delayedMessage) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = tLObject;
        this.f$2 = inputMedia;
        this.f$3 = delayedMessage;
    }

    public final void run() {
        this.f$0.lambda$null$19$SendMessagesHelper(this.f$1, this.f$2, this.f$3);
    }
}
