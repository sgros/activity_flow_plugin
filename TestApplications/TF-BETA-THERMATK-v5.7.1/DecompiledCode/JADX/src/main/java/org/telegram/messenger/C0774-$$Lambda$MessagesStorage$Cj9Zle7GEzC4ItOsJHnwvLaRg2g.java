package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$Cj9Zle7GEzC4ItOsJHnwvLaRg2g */
public final /* synthetic */ class C0774-$$Lambda$MessagesStorage$Cj9Zle7GEzC4ItOsJHnwvLaRg2g implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0774-$$Lambda$MessagesStorage$Cj9Zle7GEzC4ItOsJHnwvLaRg2g(MessagesStorage messagesStorage, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$clearUserPhoto$50$MessagesStorage(this.f$1, this.f$2);
    }
}
