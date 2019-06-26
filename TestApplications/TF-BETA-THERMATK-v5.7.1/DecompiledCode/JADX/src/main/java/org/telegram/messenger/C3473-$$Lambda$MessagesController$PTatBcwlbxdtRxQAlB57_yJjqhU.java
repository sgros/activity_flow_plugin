package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_channels_editBanned;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU */
public final /* synthetic */ class C3473-$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TL_channels_editBanned f$3;
    private final /* synthetic */ boolean f$4;

    public /* synthetic */ C3473-$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU(MessagesController messagesController, int i, BaseFragment baseFragment, TL_channels_editBanned tL_channels_editBanned, boolean z) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = baseFragment;
        this.f$3 = tL_channels_editBanned;
        this.f$4 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$setUserBannedRole$42$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tL_error);
    }
}
