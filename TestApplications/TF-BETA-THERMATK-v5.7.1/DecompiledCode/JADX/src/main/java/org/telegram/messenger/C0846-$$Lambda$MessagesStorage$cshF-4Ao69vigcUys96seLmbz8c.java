package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$cshF-4Ao69vigcUys96seLmbz8c */
public final /* synthetic */ class C0846-$$Lambda$MessagesStorage$cshF-4Ao69vigcUys96seLmbz8c implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0846-$$Lambda$MessagesStorage$cshF-4Ao69vigcUys96seLmbz8c(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$clearDownloadQueue$115$MessagesStorage(this.f$1);
    }
}
