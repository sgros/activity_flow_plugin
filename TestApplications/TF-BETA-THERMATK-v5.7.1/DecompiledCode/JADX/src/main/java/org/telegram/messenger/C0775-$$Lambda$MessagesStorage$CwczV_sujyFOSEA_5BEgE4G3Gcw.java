package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$CwczV_sujyFOSEA_5BEgE4G3Gcw */
public final /* synthetic */ class C0775-$$Lambda$MessagesStorage$CwczV_sujyFOSEA_5BEgE4G3Gcw implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0775-$$Lambda$MessagesStorage$CwczV_sujyFOSEA_5BEgE4G3Gcw(MessagesStorage messagesStorage, ArrayList arrayList, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$markMessagesAsDeleted$134$MessagesStorage(this.f$1, this.f$2);
    }
}
