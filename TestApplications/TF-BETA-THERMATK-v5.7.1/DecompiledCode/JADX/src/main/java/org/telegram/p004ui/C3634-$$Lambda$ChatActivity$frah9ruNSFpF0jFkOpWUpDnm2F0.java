package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_editMessage;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$frah9ruNSFpF0jFkOpWUpDnm2F0 */
public final /* synthetic */ class C3634-$$Lambda$ChatActivity$frah9ruNSFpF0jFkOpWUpDnm2F0 implements RequestDelegate {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ TL_messages_editMessage f$2;

    public /* synthetic */ C3634-$$Lambda$ChatActivity$frah9ruNSFpF0jFkOpWUpDnm2F0(ChatActivity chatActivity, AlertDialog[] alertDialogArr, TL_messages_editMessage tL_messages_editMessage) {
        this.f$0 = chatActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = tL_messages_editMessage;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$78$ChatActivity(this.f$1, this.f$2, tLObject, tL_error);
    }
}
