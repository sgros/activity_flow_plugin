package org.telegram.messenger;

import android.util.LongSparseArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c */
public final /* synthetic */ class C0620-$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ LongSparseArray f$1;
    private final /* synthetic */ LongSparseArray f$2;

    public /* synthetic */ C0620-$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c(MessagesController messagesController, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        this.f$0 = messagesController;
        this.f$1 = longSparseArray;
        this.f$2 = longSparseArray2;
    }

    public final void run() {
        this.f$0.lambda$updatePrintingStrings$97$MessagesController(this.f$1, this.f$2);
    }
}
