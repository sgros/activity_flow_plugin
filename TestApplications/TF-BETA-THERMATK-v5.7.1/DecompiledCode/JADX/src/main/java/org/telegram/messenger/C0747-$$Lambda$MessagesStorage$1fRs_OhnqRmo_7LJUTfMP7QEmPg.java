package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$1fRs_OhnqRmo_7LJUTfMP7QEmPg */
public final /* synthetic */ class C0747-$$Lambda$MessagesStorage$1fRs_OhnqRmo_7LJUTfMP7QEmPg implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0747-$$Lambda$MessagesStorage$1fRs_OhnqRmo_7LJUTfMP7QEmPg(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$clearUserPhotos$49$MessagesStorage(this.f$1);
    }
}
