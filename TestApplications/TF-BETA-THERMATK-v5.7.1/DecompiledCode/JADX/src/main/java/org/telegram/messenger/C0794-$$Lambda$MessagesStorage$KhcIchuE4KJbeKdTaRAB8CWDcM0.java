package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$KhcIchuE4KJbeKdTaRAB8CWDcM0 */
public final /* synthetic */ class C0794-$$Lambda$MessagesStorage$KhcIchuE4KJbeKdTaRAB8CWDcM0 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ boolean f$3;

    public /* synthetic */ C0794-$$Lambda$MessagesStorage$KhcIchuE4KJbeKdTaRAB8CWDcM0(MessagesStorage messagesStorage, ArrayList arrayList, boolean z, boolean z2) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = z;
        this.f$3 = z2;
    }

    public final void run() {
        this.f$0.lambda$updateUsers$128$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}