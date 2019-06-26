package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$GroupStickersActivity$SODwEnuUWhbL0-5dEEd38j4JKu8 */
public final /* synthetic */ class C3697-$$Lambda$GroupStickersActivity$SODwEnuUWhbL0-5dEEd38j4JKu8 implements RequestDelegate {
    private final /* synthetic */ GroupStickersActivity f$0;

    public /* synthetic */ C3697-$$Lambda$GroupStickersActivity$SODwEnuUWhbL0-5dEEd38j4JKu8(GroupStickersActivity groupStickersActivity) {
        this.f$0 = groupStickersActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$saveStickerSet$7$GroupStickersActivity(tLObject, tL_error);
    }
}
