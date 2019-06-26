package org.telegram.p004ui;

import org.telegram.p004ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_chatAdminRights;
import org.telegram.tgnet.TLRPC.TL_chatBannedRights;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$YHObzh31hcmoIGxeIXPRtV6u3bY */
public final /* synthetic */ class C3664-$$Lambda$ChatUsersActivity$YHObzh31hcmoIGxeIXPRtV6u3bY implements ChatRightsEditActivityDelegate {
    private final /* synthetic */ ChatUsersActivity f$0;
    private final /* synthetic */ TLObject f$1;

    public /* synthetic */ C3664-$$Lambda$ChatUsersActivity$YHObzh31hcmoIGxeIXPRtV6u3bY(ChatUsersActivity chatUsersActivity, TLObject tLObject) {
        this.f$0 = chatUsersActivity;
        this.f$1 = tLObject;
    }

    public final void didSetRights(int i, TL_chatAdminRights tL_chatAdminRights, TL_chatBannedRights tL_chatBannedRights) {
        this.f$0.lambda$null$3$ChatUsersActivity(this.f$1, i, tL_chatAdminRights, tL_chatBannedRights);
    }
}
