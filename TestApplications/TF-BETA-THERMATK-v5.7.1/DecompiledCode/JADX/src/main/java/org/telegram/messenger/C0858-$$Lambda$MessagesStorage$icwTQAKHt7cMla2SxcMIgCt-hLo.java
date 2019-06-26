package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$icwTQAKHt7cMla2SxcMIgCt-hLo */
public final /* synthetic */ class C0858-$$Lambda$MessagesStorage$icwTQAKHt7cMla2SxcMIgCt-hLo implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0858-$$Lambda$MessagesStorage$icwTQAKHt7cMla2SxcMIgCt-hLo(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$checkIfFolderEmpty$144$MessagesStorage(this.f$1);
    }
}
