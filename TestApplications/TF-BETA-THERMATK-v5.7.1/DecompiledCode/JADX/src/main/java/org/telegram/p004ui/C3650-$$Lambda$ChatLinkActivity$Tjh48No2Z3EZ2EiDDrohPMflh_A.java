package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A */
public final /* synthetic */ class C3650-$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A implements RequestDelegate {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ Chat f$2;
    private final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ C3650-$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A(ChatLinkActivity chatLinkActivity, AlertDialog[] alertDialogArr, Chat chat, BaseFragment baseFragment) {
        this.f$0 = chatLinkActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = chat;
        this.f$3 = baseFragment;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$linkChat$11$ChatLinkActivity(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
