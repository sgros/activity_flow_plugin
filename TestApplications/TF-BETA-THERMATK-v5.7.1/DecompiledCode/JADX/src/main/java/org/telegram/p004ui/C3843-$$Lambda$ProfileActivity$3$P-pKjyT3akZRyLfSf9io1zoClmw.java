package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.p004ui.ProfileActivity.C43083;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProfileActivity$3$P-pKjyT3akZRyLfSf9io1zoClmw */
public final /* synthetic */ class C3843-$$Lambda$ProfileActivity$3$P-pKjyT3akZRyLfSf9io1zoClmw implements DialogsActivityDelegate {
    private final /* synthetic */ C43083 f$0;
    private final /* synthetic */ User f$1;

    public /* synthetic */ C3843-$$Lambda$ProfileActivity$3$P-pKjyT3akZRyLfSf9io1zoClmw(C43083 c43083, User user) {
        this.f$0 = c43083;
        this.f$1 = user;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onItemClick$2$ProfileActivity$3(this.f$1, dialogsActivity, arrayList, charSequence, z);
    }
}
