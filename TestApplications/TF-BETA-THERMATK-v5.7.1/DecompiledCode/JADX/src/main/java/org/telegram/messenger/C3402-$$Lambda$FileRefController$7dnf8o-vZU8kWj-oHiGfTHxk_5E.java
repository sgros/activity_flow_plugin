package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileRefController$7dnf8o-vZU8kWj-oHiGfTHxk_5E */
public final /* synthetic */ class C3402-$$Lambda$FileRefController$7dnf8o-vZU8kWj-oHiGfTHxk_5E implements RequestDelegate {
    public static final /* synthetic */ C3402-$$Lambda$FileRefController$7dnf8o-vZU8kWj-oHiGfTHxk_5E INSTANCE = new C3402-$$Lambda$FileRefController$7dnf8o-vZU8kWj-oHiGfTHxk_5E();

    private /* synthetic */ C3402-$$Lambda$FileRefController$7dnf8o-vZU8kWj-oHiGfTHxk_5E() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        FileRefController.lambda$onUpdateObjectReference$19(tLObject, tL_error);
    }
}