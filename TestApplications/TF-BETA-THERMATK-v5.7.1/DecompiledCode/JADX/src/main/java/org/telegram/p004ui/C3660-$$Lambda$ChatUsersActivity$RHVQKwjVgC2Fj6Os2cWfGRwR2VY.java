package org.telegram.p004ui;

import org.telegram.p004ui.ChatUsersActivity.ChatUsersActivityDelegate;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$RHVQKwjVgC2Fj6Os2cWfGRwR2VY */
public final /* synthetic */ class C3660-$$Lambda$ChatUsersActivity$RHVQKwjVgC2Fj6Os2cWfGRwR2VY implements ChatUsersActivityDelegate {
    private final /* synthetic */ ChatUsersActivity f$0;

    public /* synthetic */ C3660-$$Lambda$ChatUsersActivity$RHVQKwjVgC2Fj6Os2cWfGRwR2VY(ChatUsersActivity chatUsersActivity) {
        this.f$0 = chatUsersActivity;
    }

    public final void didAddParticipantToList(int i, TLObject tLObject) {
        this.f$0.lambda$null$1$ChatUsersActivity(i, tLObject);
    }
}
