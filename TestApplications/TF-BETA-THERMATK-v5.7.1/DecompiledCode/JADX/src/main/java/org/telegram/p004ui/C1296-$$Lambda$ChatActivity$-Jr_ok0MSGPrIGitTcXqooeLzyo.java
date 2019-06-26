package org.telegram.p004ui;

import org.telegram.messenger.MessageObject;
import org.telegram.p004ui.ActionBar.ActionBarLayout;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$-Jr_ok0MSGPrIGitTcXqooeLzyo */
public final /* synthetic */ class C1296-$$Lambda$ChatActivity$-Jr_ok0MSGPrIGitTcXqooeLzyo implements Runnable {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ BaseFragment f$1;
    private final /* synthetic */ MessageObject f$2;
    private final /* synthetic */ ActionBarLayout f$3;

    public /* synthetic */ C1296-$$Lambda$ChatActivity$-Jr_ok0MSGPrIGitTcXqooeLzyo(ChatActivity chatActivity, BaseFragment baseFragment, MessageObject messageObject, ActionBarLayout actionBarLayout) {
        this.f$0 = chatActivity;
        this.f$1 = baseFragment;
        this.f$2 = messageObject;
        this.f$3 = actionBarLayout;
    }

    public final void run() {
        this.f$0.lambda$migrateToNewChat$57$ChatActivity(this.f$1, this.f$2, this.f$3);
    }
}
