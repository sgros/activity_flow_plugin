package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE */
public final /* synthetic */ class C0798-$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0798-$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE(MessagesStorage messagesStorage, ArrayList arrayList, ArrayList arrayList2, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
        this.f$3 = i;
    }

    public final void run() {
        this.f$0.lambda$updateDialogsWithDeletedMessages$133$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}