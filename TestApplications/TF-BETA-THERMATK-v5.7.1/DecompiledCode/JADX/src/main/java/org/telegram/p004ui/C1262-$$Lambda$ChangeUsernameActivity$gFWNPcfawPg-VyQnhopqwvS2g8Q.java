package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangeUsernameActivity$gFWNPcfawPg-VyQnhopqwvS2g8Q */
public final /* synthetic */ class C1262-$$Lambda$ChangeUsernameActivity$gFWNPcfawPg-VyQnhopqwvS2g8Q implements OnCancelListener {
    private final /* synthetic */ ChangeUsernameActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1262-$$Lambda$ChangeUsernameActivity$gFWNPcfawPg-VyQnhopqwvS2g8Q(ChangeUsernameActivity changeUsernameActivity, int i) {
        this.f$0 = changeUsernameActivity;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$saveName$8$ChangeUsernameActivity(this.f$1, dialogInterface);
    }
}
