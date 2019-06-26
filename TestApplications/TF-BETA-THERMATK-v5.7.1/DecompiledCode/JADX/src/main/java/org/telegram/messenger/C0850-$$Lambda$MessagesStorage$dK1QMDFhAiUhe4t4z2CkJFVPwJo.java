package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo */
public final /* synthetic */ class C0850-$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C0850-$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo(MessagesStorage messagesStorage, int i, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$updateChannelUsers$70$MessagesStorage(this.f$1, this.f$2);
    }
}
