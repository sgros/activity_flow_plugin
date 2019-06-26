package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityRegisterView;
import org.telegram.tgnet.TLRPC.PhotoSize;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityRegisterView$29x-JtuV_r7woPg6Q9kl8QOB7qw */
public final /* synthetic */ class C1625x3dccfb9e implements Runnable {
    private final /* synthetic */ LoginActivityRegisterView f$0;
    private final /* synthetic */ PhotoSize f$1;
    private final /* synthetic */ PhotoSize f$2;

    public /* synthetic */ C1625x3dccfb9e(LoginActivityRegisterView loginActivityRegisterView, PhotoSize photoSize, PhotoSize photoSize2) {
        this.f$0 = loginActivityRegisterView;
        this.f$1 = photoSize;
        this.f$2 = photoSize2;
    }

    public final void run() {
        this.f$0.lambda$didUploadPhoto$9$LoginActivity$LoginActivityRegisterView(this.f$1, this.f$2);
    }
}
