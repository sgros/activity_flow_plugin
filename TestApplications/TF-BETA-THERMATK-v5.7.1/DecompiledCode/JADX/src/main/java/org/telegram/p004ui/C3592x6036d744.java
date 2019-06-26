package org.telegram.p004ui;

import org.telegram.p004ui.CancelAccountDeletionActivity.LoginActivitySmsView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_confirmPhone;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$1W4kZzPjzRDtXCpazovyaTPi9gk */
public final /* synthetic */ class C3592x6036d744 implements RequestDelegate {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ TL_account_confirmPhone f$1;

    public /* synthetic */ C3592x6036d744(LoginActivitySmsView loginActivitySmsView, TL_account_confirmPhone tL_account_confirmPhone) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = tL_account_confirmPhone;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.mo15413x417aab38(this.f$1, tLObject, tL_error);
    }
}
