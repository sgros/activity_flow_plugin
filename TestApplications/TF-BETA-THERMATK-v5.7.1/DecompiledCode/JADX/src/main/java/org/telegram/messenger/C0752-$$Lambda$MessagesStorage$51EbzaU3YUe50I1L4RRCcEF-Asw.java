package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$51EbzaU3YUe50I1L4RRCcEF-Asw */
public final /* synthetic */ class C0752-$$Lambda$MessagesStorage$51EbzaU3YUe50I1L4RRCcEF-Asw implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0752-$$Lambda$MessagesStorage$51EbzaU3YUe50I1L4RRCcEF-Asw(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$getUnsentMessages$92$MessagesStorage(this.f$1);
    }
}
