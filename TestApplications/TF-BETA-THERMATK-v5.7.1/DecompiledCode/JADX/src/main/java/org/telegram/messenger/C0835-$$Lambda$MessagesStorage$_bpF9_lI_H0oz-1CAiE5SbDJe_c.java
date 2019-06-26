package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$_bpF9_lI_H0oz-1CAiE5SbDJe_c */
public final /* synthetic */ class C0835-$$Lambda$MessagesStorage$_bpF9_lI_H0oz-1CAiE5SbDJe_c implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ C0835-$$Lambda$MessagesStorage$_bpF9_lI_H0oz-1CAiE5SbDJe_c(MessagesStorage messagesStorage, int i, int i2, int i3, int i4) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = i4;
    }

    public final void run() {
        this.f$0.lambda$saveDiffParams$23$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
