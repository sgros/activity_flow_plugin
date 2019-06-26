package org.telegram.p004ui;

import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatEditActivity$j-VWblaHSOc0ptEwu8DVX6LNsH0 */
public final /* synthetic */ class C1401-$$Lambda$ChatEditActivity$j-VWblaHSOc0ptEwu8DVX6LNsH0 implements Runnable {
    private final /* synthetic */ ChatEditActivity f$0;
    private final /* synthetic */ CountDownLatch f$1;

    public /* synthetic */ C1401-$$Lambda$ChatEditActivity$j-VWblaHSOc0ptEwu8DVX6LNsH0(ChatEditActivity chatEditActivity, CountDownLatch countDownLatch) {
        this.f$0 = chatEditActivity;
        this.f$1 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$onFragmentCreate$0$ChatEditActivity(this.f$1);
    }
}
