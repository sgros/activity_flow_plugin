package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao */
public final /* synthetic */ class C3900-$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao implements RequestDelegate {
    private final /* synthetic */ WebviewActivity f$0;

    public /* synthetic */ C3900-$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao(WebviewActivity webviewActivity) {
        this.f$0 = webviewActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$reloadStats$1$WebviewActivity(tLObject, tL_error);
    }
}
