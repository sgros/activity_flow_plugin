package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$_VEgWywMVWfxr_9Ssi-lPDOcdtc */
public final /* synthetic */ class C0671-$$Lambda$MessagesController$_VEgWywMVWfxr_9Ssi-lPDOcdtc implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ C0671-$$Lambda$MessagesController$_VEgWywMVWfxr_9Ssi-lPDOcdtc(MessagesController messagesController, long j, boolean z, int i, int i2) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = z;
        this.f$3 = i;
        this.f$4 = i2;
    }

    public final void run() {
        this.f$0.lambda$markDialogAsRead$154$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}