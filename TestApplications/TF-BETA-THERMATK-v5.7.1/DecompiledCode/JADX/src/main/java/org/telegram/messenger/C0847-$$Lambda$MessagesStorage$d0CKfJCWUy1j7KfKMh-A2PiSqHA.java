package org.telegram.messenger;

import java.util.HashMap;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh-A2PiSqHA */
public final /* synthetic */ class C0847-$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh-A2PiSqHA implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ HashMap f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0847-$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh-A2PiSqHA(MessagesStorage messagesStorage, HashMap hashMap, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = hashMap;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$putCachedPhoneBook$89$MessagesStorage(this.f$1, this.f$2);
    }
}
