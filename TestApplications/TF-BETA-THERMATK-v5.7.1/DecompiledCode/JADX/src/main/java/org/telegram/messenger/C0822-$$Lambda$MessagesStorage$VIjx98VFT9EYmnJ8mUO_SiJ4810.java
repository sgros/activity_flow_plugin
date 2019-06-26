package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810 */
public final /* synthetic */ class C0822-$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C0822-$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810(MessagesStorage messagesStorage, boolean z, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = z;
        this.f$2 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$putContacts$86$MessagesStorage(this.f$1, this.f$2);
    }
}
