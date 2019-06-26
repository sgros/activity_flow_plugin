package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_updateServiceNotification;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu_-Fuu7gvw */
public final /* synthetic */ class C0697-$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu_-Fuu7gvw implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_updateServiceNotification f$1;

    public /* synthetic */ C0697-$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu_-Fuu7gvw(MessagesController messagesController, TL_updateServiceNotification tL_updateServiceNotification) {
        this.f$0 = messagesController;
        this.f$1 = tL_updateServiceNotification;
    }

    public final void run() {
        this.f$0.lambda$processUpdateArray$242$MessagesController(this.f$1);
    }
}
