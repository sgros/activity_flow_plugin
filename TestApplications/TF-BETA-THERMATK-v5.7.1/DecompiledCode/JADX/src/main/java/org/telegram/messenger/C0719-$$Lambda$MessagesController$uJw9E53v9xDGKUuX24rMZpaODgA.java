package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$uJw9E53v9xDGKUuX24rMZpaODgA */
public final /* synthetic */ class C0719-$$Lambda$MessagesController$uJw9E53v9xDGKUuX24rMZpaODgA implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0719-$$Lambda$MessagesController$uJw9E53v9xDGKUuX24rMZpaODgA(MessagesController messagesController, ArrayList arrayList, int i) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$getNewDeleteTask$32$MessagesController(this.f$1, this.f$2);
    }
}
