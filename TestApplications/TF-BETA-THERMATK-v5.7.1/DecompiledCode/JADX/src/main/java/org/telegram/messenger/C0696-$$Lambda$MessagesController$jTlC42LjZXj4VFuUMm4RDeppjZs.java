package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_editChatDefaultBannedRights;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$jTlC42LjZXj4VFuUMm4RDeppjZs */
public final /* synthetic */ class C0696-$$Lambda$MessagesController$jTlC42LjZXj4VFuUMm4RDeppjZs implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TL_messages_editChatDefaultBannedRights f$3;
    private final /* synthetic */ boolean f$4;

    public /* synthetic */ C0696-$$Lambda$MessagesController$jTlC42LjZXj4VFuUMm4RDeppjZs(MessagesController messagesController, TL_error tL_error, BaseFragment baseFragment, TL_messages_editChatDefaultBannedRights tL_messages_editChatDefaultBannedRights, boolean z) {
        this.f$0 = messagesController;
        this.f$1 = tL_error;
        this.f$2 = baseFragment;
        this.f$3 = tL_messages_editChatDefaultBannedRights;
        this.f$4 = z;
    }

    public final void run() {
        this.f$0.lambda$null$44$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
