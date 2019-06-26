package org.telegram.p004ui;

import org.telegram.p004ui.MediaActivity.MediaSearchAdapter;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$MediaActivity$MediaSearchAdapter$TrL5Kc9dMCjvGfdjifj6CuBCggQ */
public final /* synthetic */ class C3750xb6605184 implements RequestDelegate {
    private final /* synthetic */ MediaSearchAdapter f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C3750xb6605184(MediaSearchAdapter mediaSearchAdapter, int i, int i2) {
        this.f$0 = mediaSearchAdapter;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$queryServerSearch$1$MediaActivity$MediaSearchAdapter(this.f$1, this.f$2, tLObject, tL_error);
    }
}
