package org.telegram.p004ui;

import org.telegram.p004ui.PaymentFormActivity.TelegramWebviewProxy;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$TelegramWebviewProxy$GYkvPF7FkeOF4Jpek1fag25gp2E */
public final /* synthetic */ class C1807xb90c3c14 implements Runnable {
    private final /* synthetic */ TelegramWebviewProxy f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C1807xb90c3c14(TelegramWebviewProxy telegramWebviewProxy, String str, String str2) {
        this.f$0 = telegramWebviewProxy;
        this.f$1 = str;
        this.f$2 = str2;
    }

    public final void run() {
        this.f$0.lambda$postEvent$0$PaymentFormActivity$TelegramWebviewProxy(this.f$1, this.f$2);
    }
}
