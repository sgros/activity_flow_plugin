package org.telegram.p004ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco */
public final /* synthetic */ class C4064-$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco implements RequestDelegate {
    private final /* synthetic */ StickersAlert f$0;

    public /* synthetic */ C4064-$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco(StickersAlert stickersAlert) {
        this.f$0 = stickersAlert;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadStickerSet$4$StickersAlert(tLObject, tL_error);
    }
}