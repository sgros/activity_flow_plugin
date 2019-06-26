package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage.IntCallback;
import org.telegram.tgnet.TLRPC.Dialog;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg */
public final /* synthetic */ class C3483-$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg implements IntCallback {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Dialog f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C3483-$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg(MessagesController messagesController, Dialog dialog, long j) {
        this.f$0 = messagesController;
        this.f$1 = dialog;
        this.f$2 = j;
    }

    public final void run(int i) {
        this.f$0.lambda$updateInterfaceWithMessages$255$MessagesController(this.f$1, this.f$2, i);
    }
}
