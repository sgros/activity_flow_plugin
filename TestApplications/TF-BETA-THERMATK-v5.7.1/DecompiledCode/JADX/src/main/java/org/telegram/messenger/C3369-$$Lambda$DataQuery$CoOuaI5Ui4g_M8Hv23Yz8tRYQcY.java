package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY */
public final /* synthetic */ class C3369-$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY implements RequestDelegate {
    public static final /* synthetic */ C3369-$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY INSTANCE = new C3369-$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY();

    private /* synthetic */ C3369-$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        DataQuery.lambda$markFaturedStickersByIdAsRead$27(tLObject, tL_error);
    }
}
