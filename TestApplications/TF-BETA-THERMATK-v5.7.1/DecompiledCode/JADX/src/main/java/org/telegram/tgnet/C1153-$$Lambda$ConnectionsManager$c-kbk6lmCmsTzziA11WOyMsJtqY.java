package org.telegram.tgnet;

import org.telegram.messenger.MessagesController;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$c-kbk6lmCmsTzziA11WOyMsJtqY */
public final /* synthetic */ class C1153-$$Lambda$ConnectionsManager$c-kbk6lmCmsTzziA11WOyMsJtqY implements Runnable {
    private final /* synthetic */ int f$0;

    public /* synthetic */ C1153-$$Lambda$ConnectionsManager$c-kbk6lmCmsTzziA11WOyMsJtqY(int i) {
        this.f$0 = i;
    }

    public final void run() {
        MessagesController.getInstance(this.f$0).getDifference();
    }
}
