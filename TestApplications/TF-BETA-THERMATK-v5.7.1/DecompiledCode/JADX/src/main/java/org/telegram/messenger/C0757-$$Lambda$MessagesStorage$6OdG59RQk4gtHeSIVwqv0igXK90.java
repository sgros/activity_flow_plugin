package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.messages_Messages;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90 */
public final /* synthetic */ class C0757-$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ messages_Messages f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ long f$3;
    private final /* synthetic */ int f$4;
    private final /* synthetic */ boolean f$5;

    public /* synthetic */ C0757-$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90(MessagesStorage messagesStorage, messages_Messages messages_messages, int i, long j, int i2, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = messages_messages;
        this.f$2 = i;
        this.f$3 = j;
        this.f$4 = i2;
        this.f$5 = z;
    }

    public final void run() {
        this.f$0.lambda$putMessages$138$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
