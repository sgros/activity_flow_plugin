package org.telegram.messenger;

import android.util.SparseArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$bHd9VuzZp_-o4GfCjtn5be2-y4I */
public final /* synthetic */ class C0678-$$Lambda$MessagesController$bHd9VuzZp_-o4GfCjtn5be2-y4I implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ SparseArray f$1;

    public /* synthetic */ C0678-$$Lambda$MessagesController$bHd9VuzZp_-o4GfCjtn5be2-y4I(MessagesController messagesController, SparseArray sparseArray) {
        this.f$0 = messagesController;
        this.f$1 = sparseArray;
    }

    public final void run() {
        this.f$0.lambda$didAddedNewTask$31$MessagesController(this.f$1);
    }
}
