package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$Iy-_bAEHS1bHHS34WDzDt7Rdm0U */
public final /* synthetic */ class C1428-$$Lambda$ChatLinkActivity$Iy-_bAEHS1bHHS34WDzDt7Rdm0U implements OnCancelListener {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1428-$$Lambda$ChatLinkActivity$Iy-_bAEHS1bHHS34WDzDt7Rdm0U(ChatLinkActivity chatLinkActivity, int i) {
        this.f$0 = chatLinkActivity;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$null$12$ChatLinkActivity(this.f$1, dialogInterface);
    }
}
