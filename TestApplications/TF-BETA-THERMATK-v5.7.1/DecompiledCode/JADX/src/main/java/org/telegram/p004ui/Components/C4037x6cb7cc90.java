package org.telegram.p004ui.Components;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BottomSheet.Builder;
import org.telegram.p004ui.Components.EmojiView.EmojiSearchAdapter.C28342.C28331;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$EmojiView$EmojiSearchAdapter$2$1$vESTLZoOoKr-laMmjTYW6gGhW9U */
public final /* synthetic */ class C4037x6cb7cc90 implements RequestDelegate {
    private final /* synthetic */ C28331 f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ Builder f$2;

    public /* synthetic */ C4037x6cb7cc90(C28331 c28331, AlertDialog[] alertDialogArr, Builder builder) {
        this.f$0 = c28331;
        this.f$1 = alertDialogArr;
        this.f$2 = builder;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onClick$1$EmojiView$EmojiSearchAdapter$2$1(this.f$1, this.f$2, tLObject, tL_error);
    }
}
