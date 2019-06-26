package org.telegram.p004ui;

import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE */
public final /* synthetic */ class C1419-$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE implements Runnable {
    private final /* synthetic */ ChatEditTypeActivity f$0;
    private final /* synthetic */ CountDownLatch f$1;

    public /* synthetic */ C1419-$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE(ChatEditTypeActivity chatEditTypeActivity, CountDownLatch countDownLatch) {
        this.f$0 = chatEditTypeActivity;
        this.f$1 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$onFragmentCreate$0$ChatEditTypeActivity(this.f$1);
    }
}
