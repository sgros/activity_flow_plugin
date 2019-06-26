package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_editChatDefaultBannedRights;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$1P_GCY-PWvevMznLENn0Y1ly8wA */
public final /* synthetic */ class C3436-$$Lambda$MessagesController$1P_GCY-PWvevMznLENn0Y1ly8wA implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TL_messages_editChatDefaultBannedRights f$3;
    private final /* synthetic */ boolean f$4;

    public /* synthetic */ C3436-$$Lambda$MessagesController$1P_GCY-PWvevMznLENn0Y1ly8wA(MessagesController messagesController, int i, BaseFragment baseFragment, TL_messages_editChatDefaultBannedRights tL_messages_editChatDefaultBannedRights, boolean z) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = baseFragment;
        this.f$3 = tL_messages_editChatDefaultBannedRights;
        this.f$4 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$setDefaultBannedRole$45$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tL_error);
    }
}
