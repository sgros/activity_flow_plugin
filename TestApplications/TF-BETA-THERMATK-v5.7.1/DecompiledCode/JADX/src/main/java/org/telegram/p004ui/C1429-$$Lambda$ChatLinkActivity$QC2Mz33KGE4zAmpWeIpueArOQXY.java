package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$QC2Mz33KGE4zAmpWeIpueArOQXY */
public final /* synthetic */ class C1429-$$Lambda$ChatLinkActivity$QC2Mz33KGE4zAmpWeIpueArOQXY implements Runnable {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ Chat f$2;
    private final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ C1429-$$Lambda$ChatLinkActivity$QC2Mz33KGE4zAmpWeIpueArOQXY(ChatLinkActivity chatLinkActivity, AlertDialog[] alertDialogArr, Chat chat, BaseFragment baseFragment) {
        this.f$0 = chatLinkActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = chat;
        this.f$3 = baseFragment;
    }

    public final void run() {
        this.f$0.lambda$null$10$ChatLinkActivity(this.f$1, this.f$2, this.f$3);
    }
}
