package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M */
public final /* synthetic */ class C0744-$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;
    private final /* synthetic */ int f$5;

    public /* synthetic */ C0744-$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M(MessagesStorage messagesStorage, ArrayList arrayList, int i, int i2, int i3, int i4) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = i3;
        this.f$5 = i4;
    }

    public final void run() {
        this.f$0.lambda$createTaskForSecretChat$64$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
