package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatEditTypeActivity$NQoUsXIxZfHWCE1utCEcp_nY2As */
public final /* synthetic */ class C3640-$$Lambda$ChatEditTypeActivity$NQoUsXIxZfHWCE1utCEcp_nY2As implements RequestDelegate {
    private final /* synthetic */ ChatEditTypeActivity f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C3640-$$Lambda$ChatEditTypeActivity$NQoUsXIxZfHWCE1utCEcp_nY2As(ChatEditTypeActivity chatEditTypeActivity, boolean z) {
        this.f$0 = chatEditTypeActivity;
        this.f$1 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$generateLink$21$ChatEditTypeActivity(this.f$1, tLObject, tL_error);
    }
}
