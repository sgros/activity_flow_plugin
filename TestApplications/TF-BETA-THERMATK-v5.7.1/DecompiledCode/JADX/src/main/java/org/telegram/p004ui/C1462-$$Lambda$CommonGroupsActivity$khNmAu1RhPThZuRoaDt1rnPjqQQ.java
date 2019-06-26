package org.telegram.p004ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CommonGroupsActivity$khNmAu1RhPThZuRoaDt1rnPjqQQ */
public final /* synthetic */ class C1462-$$Lambda$CommonGroupsActivity$khNmAu1RhPThZuRoaDt1rnPjqQQ implements Runnable {
    private final /* synthetic */ CommonGroupsActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C1462-$$Lambda$CommonGroupsActivity$khNmAu1RhPThZuRoaDt1rnPjqQQ(CommonGroupsActivity commonGroupsActivity, TL_error tL_error, TLObject tLObject, int i) {
        this.f$0 = commonGroupsActivity;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
        this.f$3 = i;
    }

    public final void run() {
        this.f$0.lambda$null$1$CommonGroupsActivity(this.f$1, this.f$2, this.f$3);
    }
}
