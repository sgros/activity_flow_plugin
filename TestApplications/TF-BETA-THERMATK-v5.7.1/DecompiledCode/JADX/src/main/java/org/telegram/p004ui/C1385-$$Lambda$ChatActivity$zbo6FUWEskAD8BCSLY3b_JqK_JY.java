package org.telegram.p004ui;

import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.MessagesStorage;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$zbo6FUWEskAD8BCSLY3b_JqK_JY */
public final /* synthetic */ class C1385-$$Lambda$ChatActivity$zbo6FUWEskAD8BCSLY3b_JqK_JY implements Runnable {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ MessagesStorage f$1;
    private final /* synthetic */ CountDownLatch f$2;

    public /* synthetic */ C1385-$$Lambda$ChatActivity$zbo6FUWEskAD8BCSLY3b_JqK_JY(ChatActivity chatActivity, MessagesStorage messagesStorage, CountDownLatch countDownLatch) {
        this.f$0 = chatActivity;
        this.f$1 = messagesStorage;
        this.f$2 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$onFragmentCreate$3$ChatActivity(this.f$1, this.f$2);
    }
}
