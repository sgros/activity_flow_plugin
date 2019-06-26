package org.telegram.p004ui;

import org.telegram.p004ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_chatAdminRights;
import org.telegram.tgnet.TLRPC.TL_chatBannedRights;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$s-tj1rO5rel9STPZNYUOQpZ18CQ */
public final /* synthetic */ class C3667-$$Lambda$ChatUsersActivity$s-tj1rO5rel9STPZNYUOQpZ18CQ implements ChatRightsEditActivityDelegate {
    private final /* synthetic */ ChatUsersActivity f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C3667-$$Lambda$ChatUsersActivity$s-tj1rO5rel9STPZNYUOQpZ18CQ(ChatUsersActivity chatUsersActivity, TLObject tLObject, boolean z) {
        this.f$0 = chatUsersActivity;
        this.f$1 = tLObject;
        this.f$2 = z;
    }

    public final void didSetRights(int i, TL_chatAdminRights tL_chatAdminRights, TL_chatBannedRights tL_chatBannedRights) {
        this.f$0.lambda$openRightsEdit$7$ChatUsersActivity(this.f$1, this.f$2, i, tL_chatAdminRights, tL_chatBannedRights);
    }
}
