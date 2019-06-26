package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$IRnMGvkA4nj7Im0gunHCNPyjjd0 */
public final /* synthetic */ class C0786-$$Lambda$MessagesStorage$IRnMGvkA4nj7Im0gunHCNPyjjd0 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0786-$$Lambda$MessagesStorage$IRnMGvkA4nj7Im0gunHCNPyjjd0(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$loadWebRecent$34$MessagesStorage(this.f$1);
    }
}
