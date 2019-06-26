package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc */
public final /* synthetic */ class C3527-$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc implements RequestDelegate {
    public static final /* synthetic */ C3527-$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc INSTANCE = new C3527-$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc();

    private /* synthetic */ C3527-$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$reportSpam$24(tLObject, tL_error);
    }
}
