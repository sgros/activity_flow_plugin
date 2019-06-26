package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Dialog;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$B2hd0oVj2x9ARog9ivfgDWOv-vg */
public final /* synthetic */ class C0598-$$Lambda$MessagesController$B2hd0oVj2x9ARog9ivfgDWOv-vg implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Dialog f$1;

    public /* synthetic */ C0598-$$Lambda$MessagesController$B2hd0oVj2x9ARog9ivfgDWOv-vg(MessagesController messagesController, Dialog dialog) {
        this.f$0 = messagesController;
        this.f$1 = dialog;
    }

    public final void run() {
        this.f$0.lambda$null$134$MessagesController(this.f$1);
    }
}
