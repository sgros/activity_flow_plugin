package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.messages_Dialogs;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$vf0a3gbyExIJKu8Fv0SfXfbDaBc */
public final /* synthetic */ class C0726-$$Lambda$MessagesController$vf0a3gbyExIJKu8Fv0SfXfbDaBc implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ messages_Dialogs f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ boolean f$3;
    private final /* synthetic */ int[] f$4;
    private final /* synthetic */ int f$5;

    public /* synthetic */ C0726-$$Lambda$MessagesController$vf0a3gbyExIJKu8Fv0SfXfbDaBc(MessagesController messagesController, messages_Dialogs messages_dialogs, int i, boolean z, int[] iArr, int i2) {
        this.f$0 = messagesController;
        this.f$1 = messages_dialogs;
        this.f$2 = i;
        this.f$3 = z;
        this.f$4 = iArr;
        this.f$5 = i2;
    }

    public final void run() {
        this.f$0.lambda$null$127$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
