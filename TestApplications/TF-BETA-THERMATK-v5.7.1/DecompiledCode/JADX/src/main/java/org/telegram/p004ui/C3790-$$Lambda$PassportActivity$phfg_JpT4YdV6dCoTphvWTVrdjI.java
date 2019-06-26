package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI */
public final /* synthetic */ class C3790-$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI implements RequestDelegate {
    private final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ C3790-$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadPasswordInfo$4$PassportActivity(tLObject, tL_error);
    }
}
