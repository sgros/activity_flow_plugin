package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$OUR20mf5_d9gsY1zs0m8SWbYyH8 */
public final /* synthetic */ class C0647-$$Lambda$MessagesController$OUR20mf5_d9gsY1zs0m8SWbYyH8 implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C0647-$$Lambda$MessagesController$OUR20mf5_d9gsY1zs0m8SWbYyH8(MessagesController messagesController, int i, ArrayList arrayList) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$generateJoinMessage$225$MessagesController(this.f$1, this.f$2);
    }
}