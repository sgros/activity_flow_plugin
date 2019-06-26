package org.telegram.p004ui;

import org.telegram.p004ui.PhonebookSelectActivity.PhonebookSelectActivityDelegate;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhonebookSelectActivity$4oi9dmdaHeu97U36787Z9FtySPo */
public final /* synthetic */ class C3805-$$Lambda$PhonebookSelectActivity$4oi9dmdaHeu97U36787Z9FtySPo implements PhonebookSelectActivityDelegate {
    private final /* synthetic */ PhonebookSelectActivity f$0;

    public /* synthetic */ C3805-$$Lambda$PhonebookSelectActivity$4oi9dmdaHeu97U36787Z9FtySPo(PhonebookSelectActivity phonebookSelectActivity) {
        this.f$0 = phonebookSelectActivity;
    }

    public final void didSelectContact(User user) {
        this.f$0.lambda$null$0$PhonebookSelectActivity(user);
    }
}
