package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$UniaOWI4GiF_wLDJANnyTaeI1I0 */
public final /* synthetic */ class C0820-$$Lambda$MessagesStorage$UniaOWI4GiF_wLDJANnyTaeI1I0 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0820-$$Lambda$MessagesStorage$UniaOWI4GiF_wLDJANnyTaeI1I0(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$updateDbToLastVersion$1$MessagesStorage(this.f$1);
    }
}
