package org.telegram.p004ui;

import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$4z_sufq32stWQmeljAxBuSXODwQ */
public final /* synthetic */ class C1447-$$Lambda$ChatUsersActivity$4z_sufq32stWQmeljAxBuSXODwQ implements Runnable {
    private final /* synthetic */ ChatUsersActivity f$0;
    private final /* synthetic */ Updates f$1;

    public /* synthetic */ C1447-$$Lambda$ChatUsersActivity$4z_sufq32stWQmeljAxBuSXODwQ(ChatUsersActivity chatUsersActivity, Updates updates) {
        this.f$0 = chatUsersActivity;
        this.f$1 = updates;
    }

    public final void run() {
        this.f$0.lambda$null$12$ChatUsersActivity(this.f$1);
    }
}