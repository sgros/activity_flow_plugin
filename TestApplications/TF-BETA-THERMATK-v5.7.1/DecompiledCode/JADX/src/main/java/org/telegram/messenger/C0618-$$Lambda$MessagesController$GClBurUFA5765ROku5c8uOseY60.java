package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$GClBurUFA5765ROku5c8uOseY60 */
public final /* synthetic */ class C0618-$$Lambda$MessagesController$GClBurUFA5765ROku5c8uOseY60 implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0618-$$Lambda$MessagesController$GClBurUFA5765ROku5c8uOseY60(MessagesController messagesController, boolean z, long j) {
        this.f$0 = messagesController;
        this.f$1 = z;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$setLastCreatedDialogId$9$MessagesController(this.f$1, this.f$2);
    }
}
