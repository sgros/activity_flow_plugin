package org.telegram.messenger;

import android.os.Bundle;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$khcet7XZs3bzMGRlSwPxErmYGcs */
public final /* synthetic */ class C0699-$$Lambda$MessagesController$khcet7XZs3bzMGRlSwPxErmYGcs implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ BaseFragment f$3;
    private final /* synthetic */ Bundle f$4;

    public /* synthetic */ C0699-$$Lambda$MessagesController$khcet7XZs3bzMGRlSwPxErmYGcs(MessagesController messagesController, AlertDialog alertDialog, TLObject tLObject, BaseFragment baseFragment, Bundle bundle) {
        this.f$0 = messagesController;
        this.f$1 = alertDialog;
        this.f$2 = tLObject;
        this.f$3 = baseFragment;
        this.f$4 = bundle;
    }

    public final void run() {
        this.f$0.lambda$null$256$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
