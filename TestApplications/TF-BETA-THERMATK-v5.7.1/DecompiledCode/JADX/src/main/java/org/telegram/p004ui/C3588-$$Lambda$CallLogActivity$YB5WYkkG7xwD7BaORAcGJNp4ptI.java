package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI */
public final /* synthetic */ class C3588-$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI implements RequestDelegate {
    private final /* synthetic */ CallLogActivity f$0;

    public /* synthetic */ C3588-$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI(CallLogActivity callLogActivity) {
        this.f$0 = callLogActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$getCalls$6$CallLogActivity(tLObject, tL_error);
    }
}
