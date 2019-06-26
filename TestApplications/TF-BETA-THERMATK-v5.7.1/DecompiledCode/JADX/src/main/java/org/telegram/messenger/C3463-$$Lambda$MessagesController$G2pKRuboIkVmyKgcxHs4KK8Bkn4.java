package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_wallPaperSettings;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4 */
public final /* synthetic */ class C3463-$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_wallPaperSettings f$1;

    public /* synthetic */ C3463-$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4(MessagesController messagesController, TL_wallPaperSettings tL_wallPaperSettings) {
        this.f$0 = messagesController;
        this.f$1 = tL_wallPaperSettings;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$didReceivedNotification$6$MessagesController(this.f$1, tLObject, tL_error);
    }
}
