package org.telegram.tgnet;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC.TL_config;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2-hJ1zU */
public final /* synthetic */ class C1144-$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2-hJ1zU implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ TL_config f$1;

    public /* synthetic */ C1144-$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2-hJ1zU(int i, TL_config tL_config) {
        this.f$0 = i;
        this.f$1 = tL_config;
    }

    public final void run() {
        MessagesController.getInstance(this.f$0).updateConfig(this.f$1);
    }
}
