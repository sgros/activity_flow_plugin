package org.telegram.tgnet;

import org.telegram.messenger.MessagesController;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$GiXNWNTneL61N5XH1sI4IVkif4k */
public final /* synthetic */ class C1148-$$Lambda$ConnectionsManager$GiXNWNTneL61N5XH1sI4IVkif4k implements Runnable {
    private final /* synthetic */ int f$0;

    public /* synthetic */ C1148-$$Lambda$ConnectionsManager$GiXNWNTneL61N5XH1sI4IVkif4k(int i) {
        this.f$0 = i;
    }

    public final void run() {
        MessagesController.getInstance(this.f$0).updateTimerProc();
    }
}
