package org.telegram.p004ui;

import org.telegram.p004ui.WallpapersListActivity.C43612;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$WallpapersListActivity$2$tLgeSLFOeRml08rU2UfCXZWRhT0 */
public final /* synthetic */ class C3893-$$Lambda$WallpapersListActivity$2$tLgeSLFOeRml08rU2UfCXZWRhT0 implements RequestDelegate {
    private final /* synthetic */ C43612 f$0;
    private final /* synthetic */ int[] f$1;

    public /* synthetic */ C3893-$$Lambda$WallpapersListActivity$2$tLgeSLFOeRml08rU2UfCXZWRhT0(C43612 c43612, int[] iArr) {
        this.f$0 = c43612;
        this.f$1 = iArr;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$1$WallpapersListActivity$2(this.f$1, tLObject, tL_error);
    }
}
