package org.telegram.messenger;

import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$vHup36gKZnehVbIgi6EYZgekiCc */
public final /* synthetic */ class C0724-$$Lambda$MessagesController$vHup36gKZnehVbIgi6EYZgekiCc implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C0724-$$Lambda$MessagesController$vHup36gKZnehVbIgi6EYZgekiCc(MessagesController messagesController, long j, TLObject tLObject) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$28$MessagesController(this.f$1, this.f$2);
    }
}
