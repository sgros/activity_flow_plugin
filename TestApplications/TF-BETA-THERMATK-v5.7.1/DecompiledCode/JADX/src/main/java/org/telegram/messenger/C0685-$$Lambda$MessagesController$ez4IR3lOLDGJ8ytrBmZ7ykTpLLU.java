package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$ez4IR3lOLDGJ8ytrBmZ7ykTpLLU */
public final /* synthetic */ class C0685-$$Lambda$MessagesController$ez4IR3lOLDGJ8ytrBmZ7ykTpLLU implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C0685-$$Lambda$MessagesController$ez4IR3lOLDGJ8ytrBmZ7ykTpLLU(MessagesController messagesController, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
    }

    public final void run() {
        this.f$0.lambda$processUpdateArray$238$MessagesController(this.f$1, this.f$2);
    }
}
