package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$PjPiLXNm6_EsiAsuc-msXC__TJg */
public final /* synthetic */ class C0808-$$Lambda$MessagesStorage$PjPiLXNm6_EsiAsuc-msXC__TJg implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0808-$$Lambda$MessagesStorage$PjPiLXNm6_EsiAsuc-msXC__TJg(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$checkIfFolderEmptyInternal$143$MessagesStorage(this.f$1);
    }
}
