package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$Vnf-RLnltCEPaLdbkpbktxt0AFA */
public final /* synthetic */ class C0825-$$Lambda$MessagesStorage$Vnf-RLnltCEPaLdbkpbktxt0AFA implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0825-$$Lambda$MessagesStorage$Vnf-RLnltCEPaLdbkpbktxt0AFA(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$loadChannelAdmins$68$MessagesStorage(this.f$1);
    }
}
