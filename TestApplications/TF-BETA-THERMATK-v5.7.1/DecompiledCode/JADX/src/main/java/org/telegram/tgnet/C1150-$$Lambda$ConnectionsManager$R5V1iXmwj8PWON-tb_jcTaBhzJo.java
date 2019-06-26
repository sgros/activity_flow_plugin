package org.telegram.tgnet;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$R5V1iXmwj8PWON-tb_jcTaBhzJo */
public final /* synthetic */ class C1150-$$Lambda$ConnectionsManager$R5V1iXmwj8PWON-tb_jcTaBhzJo implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ TLObject f$1;

    public /* synthetic */ C1150-$$Lambda$ConnectionsManager$R5V1iXmwj8PWON-tb_jcTaBhzJo(int i, TLObject tLObject) {
        this.f$0 = i;
        this.f$1 = tLObject;
    }

    public final void run() {
        MessagesController.getInstance(this.f$0).processUpdates((Updates) this.f$1, false);
    }
}
