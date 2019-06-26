package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig */
public final /* synthetic */ class C0591-$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0591-$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig(MessagesController messagesController, ArrayList arrayList, int i) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$processLoadedDeleteTask$36$MessagesController(this.f$1, this.f$2);
    }
}
