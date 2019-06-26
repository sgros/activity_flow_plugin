package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE */
public final /* synthetic */ class C0608-$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ C0608-$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE(MessagesController messagesController, AlertDialog[] alertDialogArr, int i, BaseFragment baseFragment) {
        this.f$0 = messagesController;
        this.f$1 = alertDialogArr;
        this.f$2 = i;
        this.f$3 = baseFragment;
    }

    public final void run() {
        this.f$0.lambda$openByUserName$262$MessagesController(this.f$1, this.f$2, this.f$3);
    }
}
