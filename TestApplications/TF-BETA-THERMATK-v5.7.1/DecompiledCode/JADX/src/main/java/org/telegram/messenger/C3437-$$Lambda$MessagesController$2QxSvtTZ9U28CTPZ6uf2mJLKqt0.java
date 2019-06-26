package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$2QxSvtTZ9U28CTPZ6uf2mJLKqt0 */
public final /* synthetic */ class C3437-$$Lambda$MessagesController$2QxSvtTZ9U28CTPZ6uf2mJLKqt0 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C3437-$$Lambda$MessagesController$2QxSvtTZ9U28CTPZ6uf2mJLKqt0(MessagesController messagesController, AlertDialog[] alertDialogArr, BaseFragment baseFragment, int i) {
        this.f$0 = messagesController;
        this.f$1 = alertDialogArr;
        this.f$2 = baseFragment;
        this.f$3 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$openByUserName$260$MessagesController(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
