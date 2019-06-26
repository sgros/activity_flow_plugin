package org.telegram.p004ui;

import org.telegram.p004ui.DialogsActivity.SwipeController;
import org.telegram.tgnet.TLRPC.Dialog;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DialogsActivity$SwipeController$YQ1mguBHuXIRhAK21aamsJthsAQ */
public final /* synthetic */ class C1501xafc802e0 implements Runnable {
    private final /* synthetic */ SwipeController f$0;
    private final /* synthetic */ Dialog f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C1501xafc802e0(SwipeController swipeController, Dialog dialog, int i) {
        this.f$0 = swipeController;
        this.f$1 = dialog;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$null$0$DialogsActivity$SwipeController(this.f$1, this.f$2);
    }
}
