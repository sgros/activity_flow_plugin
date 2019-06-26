package org.telegram.messenger;

import org.telegram.tgnet.NativeByteBuffer;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3_-p-EFkrwY3ws */
public final /* synthetic */ class C0817-$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3_-p-EFkrwY3ws implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ NativeByteBuffer f$2;

    public /* synthetic */ C0817-$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3_-p-EFkrwY3ws(MessagesStorage messagesStorage, long j, NativeByteBuffer nativeByteBuffer) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = nativeByteBuffer;
    }

    public final void run() {
        this.f$0.lambda$createPendingTask$6$MessagesStorage(this.f$1, this.f$2);
    }
}
