package org.telegram.p004ui;

import org.telegram.p004ui.ContactsActivity.ContactsActivityDelegate;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CallLogActivity$glOYCOInhnTGmJL8rtqySIZYCwY */
public final /* synthetic */ class C3589-$$Lambda$CallLogActivity$glOYCOInhnTGmJL8rtqySIZYCwY implements ContactsActivityDelegate {
    private final /* synthetic */ CallLogActivity f$0;

    public /* synthetic */ C3589-$$Lambda$CallLogActivity$glOYCOInhnTGmJL8rtqySIZYCwY(CallLogActivity callLogActivity) {
        this.f$0 = callLogActivity;
    }

    public final void didSelectContact(User user, String str, ContactsActivity contactsActivity) {
        this.f$0.lambda$null$3$CallLogActivity(user, str, contactsActivity);
    }
}
