package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$GMUC_3wWZaXGMALhkVNJZ4ROQdc */
public final /* synthetic */ class C0781-$$Lambda$MessagesStorage$GMUC_3wWZaXGMALhkVNJZ4ROQdc implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C0781-$$Lambda$MessagesStorage$GMUC_3wWZaXGMALhkVNJZ4ROQdc(MessagesStorage messagesStorage, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$deleteContacts$87$MessagesStorage(this.f$1);
    }
}
