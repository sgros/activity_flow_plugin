package org.telegram.messenger;

import android.util.SparseIntArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$a-e0LwnKknlA9E0BfJ3sY5J_dD0 */
public final /* synthetic */ class C0836-$$Lambda$MessagesStorage$a-e0LwnKknlA9E0BfJ3sY5J_dD0 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ SparseIntArray f$2;

    public /* synthetic */ C0836-$$Lambda$MessagesStorage$a-e0LwnKknlA9E0BfJ3sY5J_dD0(MessagesStorage messagesStorage, boolean z, SparseIntArray sparseIntArray) {
        this.f$0 = messagesStorage;
        this.f$1 = z;
        this.f$2 = sparseIntArray;
    }

    public final void run() {
        this.f$0.lambda$putBlockedUsers$40$MessagesStorage(this.f$1, this.f$2);
    }
}
