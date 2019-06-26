package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileRefController$2YbOQ-Rvo_LvdJ_-ALCga2DKRrU */
public final /* synthetic */ class C3400-$$Lambda$FileRefController$2YbOQ-Rvo_LvdJ_-ALCga2DKRrU implements RequestDelegate {
    public static final /* synthetic */ C3400-$$Lambda$FileRefController$2YbOQ-Rvo_LvdJ_-ALCga2DKRrU INSTANCE = new C3400-$$Lambda$FileRefController$2YbOQ-Rvo_LvdJ_-ALCga2DKRrU();

    private /* synthetic */ C3400-$$Lambda$FileRefController$2YbOQ-Rvo_LvdJ_-ALCga2DKRrU() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        FileRefController.lambda$onUpdateObjectReference$20(tLObject, tL_error);
    }
}
