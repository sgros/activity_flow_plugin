package org.telegram.messenger;

import android.util.SparseArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc */
public final /* synthetic */ class C0753-$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ SparseArray f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0753-$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc(MessagesStorage messagesStorage, SparseArray sparseArray, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = sparseArray;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$putChannelViews$122$MessagesStorage(this.f$1, this.f$2);
    }
}
