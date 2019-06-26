package org.telegram.messenger;

import android.os.Bundle;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs */
public final /* synthetic */ class C3447-$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ Bundle f$3;

    public /* synthetic */ C3447-$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs(MessagesController messagesController, AlertDialog alertDialog, BaseFragment baseFragment, Bundle bundle) {
        this.f$0 = messagesController;
        this.f$1 = alertDialog;
        this.f$2 = baseFragment;
        this.f$3 = bundle;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$checkCanOpenChat$257$MessagesController(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
