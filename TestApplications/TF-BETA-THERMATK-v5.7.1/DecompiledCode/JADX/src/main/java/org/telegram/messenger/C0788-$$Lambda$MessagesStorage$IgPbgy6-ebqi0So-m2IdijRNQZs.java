package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.messenger.support.SparseLongArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$IgPbgy6-ebqi0So-m2IdijRNQZs */
public final /* synthetic */ class C0788-$$Lambda$MessagesStorage$IgPbgy6-ebqi0So-m2IdijRNQZs implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ SparseLongArray f$1;
    private final /* synthetic */ SparseLongArray f$2;
    private final /* synthetic */ ArrayList f$3;

    public /* synthetic */ C0788-$$Lambda$MessagesStorage$IgPbgy6-ebqi0So-m2IdijRNQZs(MessagesStorage messagesStorage, SparseLongArray sparseLongArray, SparseLongArray sparseLongArray2, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = sparseLongArray;
        this.f$2 = sparseLongArray2;
        this.f$3 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$updateDialogsWithReadMessages$65$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
