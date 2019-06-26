package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$5tLDgdMiD0B9gd7akTqO-XaBigI */
public final /* synthetic */ class C0754-$$Lambda$MessagesStorage$5tLDgdMiD0B9gd7akTqO-XaBigI implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C0754-$$Lambda$MessagesStorage$5tLDgdMiD0B9gd7akTqO-XaBigI(MessagesStorage messagesStorage, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$markMessagesAsDeletedByRandoms$132$MessagesStorage(this.f$1);
    }
}
