package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.GroupCreateActivity.ContactsAddActivityDelegate;
import org.telegram.p004ui.GroupCreateActivity.ContactsAddActivityDelegate.C3035-CC;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProfileActivity$00s-Q5Ms7AV0v3XsAsD6561decA */
public final /* synthetic */ class C3841-$$Lambda$ProfileActivity$00s-Q5Ms7AV0v3XsAsD6561decA implements ContactsAddActivityDelegate {
    private final /* synthetic */ ProfileActivity f$0;

    public /* synthetic */ C3841-$$Lambda$ProfileActivity$00s-Q5Ms7AV0v3XsAsD6561decA(ProfileActivity profileActivity) {
        this.f$0 = profileActivity;
    }

    public final void didSelectUsers(ArrayList arrayList, int i) {
        this.f$0.lambda$openAddMember$20$ProfileActivity(arrayList, i);
    }

    public /* synthetic */ void needAddBot(User user) {
        C3035-CC.$default$needAddBot(this, user);
    }
}
