package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$PtlR0pP29l9GCXkUdVv_AI_Hdug */
public final /* synthetic */ class C0651-$$Lambda$MessagesController$PtlR0pP29l9GCXkUdVv_AI_Hdug implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TL_error f$3;
    private final /* synthetic */ TLObject f$4;
    private final /* synthetic */ int f$5;

    public /* synthetic */ C0651-$$Lambda$MessagesController$PtlR0pP29l9GCXkUdVv_AI_Hdug(MessagesController messagesController, AlertDialog[] alertDialogArr, BaseFragment baseFragment, TL_error tL_error, TLObject tLObject, int i) {
        this.f$0 = messagesController;
        this.f$1 = alertDialogArr;
        this.f$2 = baseFragment;
        this.f$3 = tL_error;
        this.f$4 = tLObject;
        this.f$5 = i;
    }

    public final void run() {
        this.f$0.lambda$null$259$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
