package org.telegram.messenger;

import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$rNUlr7DAfpCKy3vrWYUfF6QWZEw */
public final /* synthetic */ class C0712-$$Lambda$MessagesController$rNUlr7DAfpCKy3vrWYUfF6QWZEw implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0712-$$Lambda$MessagesController$rNUlr7DAfpCKy3vrWYUfF6QWZEw(MessagesController messagesController, TLObject tLObject, int i) {
        this.f$0 = messagesController;
        this.f$1 = tLObject;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$null$114$MessagesController(this.f$1, this.f$2);
    }
}
