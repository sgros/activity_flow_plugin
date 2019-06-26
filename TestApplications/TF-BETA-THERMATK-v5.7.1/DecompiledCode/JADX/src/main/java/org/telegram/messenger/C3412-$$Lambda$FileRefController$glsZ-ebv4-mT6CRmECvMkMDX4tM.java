package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileRefController$glsZ-ebv4-mT6CRmECvMkMDX4tM */
public final /* synthetic */ class C3412-$$Lambda$FileRefController$glsZ-ebv4-mT6CRmECvMkMDX4tM implements RequestDelegate {
    public static final /* synthetic */ C3412-$$Lambda$FileRefController$glsZ-ebv4-mT6CRmECvMkMDX4tM INSTANCE = new C3412-$$Lambda$FileRefController$glsZ-ebv4-mT6CRmECvMkMDX4tM();

    private /* synthetic */ C3412-$$Lambda$FileRefController$glsZ-ebv4-mT6CRmECvMkMDX4tM() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        FileRefController.lambda$onUpdateObjectReference$18(tLObject, tL_error);
    }
}
