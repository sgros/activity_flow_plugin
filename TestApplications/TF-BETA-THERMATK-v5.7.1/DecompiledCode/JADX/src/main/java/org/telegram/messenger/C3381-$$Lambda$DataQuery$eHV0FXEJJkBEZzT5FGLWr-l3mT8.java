package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr-l3mT8 */
public final /* synthetic */ class C3381-$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr-l3mT8 implements RequestDelegate {
    public static final /* synthetic */ C3381-$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr-l3mT8 INSTANCE = new C3381-$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr-l3mT8();

    private /* synthetic */ C3381-$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr-l3mT8() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        DataQuery.lambda$removePeer$78(tLObject, tL_error);
    }
}
