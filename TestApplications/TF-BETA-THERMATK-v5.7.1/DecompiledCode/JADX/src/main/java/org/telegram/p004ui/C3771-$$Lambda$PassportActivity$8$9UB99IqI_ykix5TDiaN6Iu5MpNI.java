package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C42578;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI */
public final /* synthetic */ class C3771-$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI implements RequestDelegate {
    private final /* synthetic */ C42578 f$0;

    public /* synthetic */ C3771-$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI(C42578 c42578) {
        this.f$0 = c42578;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$resetSecret$3$PassportActivity$8(tLObject, tL_error);
    }
}
