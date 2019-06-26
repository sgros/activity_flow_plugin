package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.ProfileActivity.C43083;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProfileActivity$3$-wA0DEMpYVaVxb1SmJsBe0hOY-I */
public final /* synthetic */ class C1931-$$Lambda$ProfileActivity$3$-wA0DEMpYVaVxb1SmJsBe0hOY-I implements OnClickListener {
    private final /* synthetic */ C43083 f$0;
    private final /* synthetic */ User f$1;

    public /* synthetic */ C1931-$$Lambda$ProfileActivity$3$-wA0DEMpYVaVxb1SmJsBe0hOY-I(C43083 c43083, User user) {
        this.f$0 = c43083;
        this.f$1 = user;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$1$ProfileActivity$3(this.f$1, dialogInterface, i);
    }
}
