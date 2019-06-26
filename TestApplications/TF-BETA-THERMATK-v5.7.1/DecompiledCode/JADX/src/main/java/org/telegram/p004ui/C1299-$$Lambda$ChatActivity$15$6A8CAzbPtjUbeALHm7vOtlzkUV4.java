package org.telegram.p004ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.p004ui.ChatActivity.C240815;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$15$6A8CAzbPtjUbeALHm7vOtlzkUV4 */
public final /* synthetic */ class C1299-$$Lambda$ChatActivity$15$6A8CAzbPtjUbeALHm7vOtlzkUV4 implements Runnable {
    private final /* synthetic */ C240815 f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ TL_error f$2;
    private final /* synthetic */ MessagesStorage f$3;

    public /* synthetic */ C1299-$$Lambda$ChatActivity$15$6A8CAzbPtjUbeALHm7vOtlzkUV4(C240815 c240815, TLObject tLObject, TL_error tL_error, MessagesStorage messagesStorage) {
        this.f$0 = c240815;
        this.f$1 = tLObject;
        this.f$2 = tL_error;
        this.f$3 = messagesStorage;
    }

    public final void run() {
        this.f$0.lambda$null$1$ChatActivity$15(this.f$1, this.f$2, this.f$3);
    }
}
