package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0 */
public final /* synthetic */ class C3383-$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0 implements RequestDelegate {
    public static final /* synthetic */ C3383-$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0 INSTANCE = new C3383-$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0();

    private /* synthetic */ C3383-$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        DataQuery.lambda$saveDraft$99(tLObject, tL_error);
    }
}
