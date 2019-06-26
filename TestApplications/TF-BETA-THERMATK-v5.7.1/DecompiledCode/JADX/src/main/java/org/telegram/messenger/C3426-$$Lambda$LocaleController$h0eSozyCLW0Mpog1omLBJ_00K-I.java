package org.telegram.messenger;

import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$LocaleController$h0eSozyCLW0Mpog1omLBJ_00K-I */
public final /* synthetic */ class C3426-$$Lambda$LocaleController$h0eSozyCLW0Mpog1omLBJ_00K-I implements RequestDelegate {
    private final /* synthetic */ LocaleController f$0;
    private final /* synthetic */ LocaleInfo f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C3426-$$Lambda$LocaleController$h0eSozyCLW0Mpog1omLBJ_00K-I(LocaleController localeController, LocaleInfo localeInfo, int i) {
        this.f$0 = localeController;
        this.f$1 = localeInfo;
        this.f$2 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$applyRemoteLanguage$14$LocaleController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
