package org.telegram.p004ui;

import org.telegram.p004ui.PhonebookSelectActivity.PhonebookSelectActivityDelegate;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$NigUKwurkV8y3w-c45IqajRbqSw */
public final /* synthetic */ class C3629-$$Lambda$ChatActivity$NigUKwurkV8y3w-c45IqajRbqSw implements PhonebookSelectActivityDelegate {
    private final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ C3629-$$Lambda$ChatActivity$NigUKwurkV8y3w-c45IqajRbqSw(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    public final void didSelectContact(User user) {
        this.f$0.lambda$processSelectedAttach$43$ChatActivity(user);
    }
}
