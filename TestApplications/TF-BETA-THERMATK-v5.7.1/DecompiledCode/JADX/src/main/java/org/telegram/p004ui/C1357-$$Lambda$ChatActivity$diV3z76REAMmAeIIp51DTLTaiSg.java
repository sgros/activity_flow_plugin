package org.telegram.p004ui;

import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.MessagesStorage;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$diV3z76REAMmAeIIp51DTLTaiSg */
public final /* synthetic */ class C1357-$$Lambda$ChatActivity$diV3z76REAMmAeIIp51DTLTaiSg implements Runnable {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ MessagesStorage f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ C1357-$$Lambda$ChatActivity$diV3z76REAMmAeIIp51DTLTaiSg(ChatActivity chatActivity, MessagesStorage messagesStorage, int i, CountDownLatch countDownLatch) {
        this.f$0 = chatActivity;
        this.f$1 = messagesStorage;
        this.f$2 = i;
        this.f$3 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$onFragmentCreate$0$ChatActivity(this.f$1, this.f$2, this.f$3);
    }
}
