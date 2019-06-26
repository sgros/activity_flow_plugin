package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4 */
public final /* synthetic */ class C1660-$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4 implements OnClickListener {
    private final /* synthetic */ LoginActivity f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C1660-$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4(LoginActivity loginActivity, boolean z, String str) {
        this.f$0 = loginActivity;
        this.f$1 = z;
        this.f$2 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$needShowInvalidAlert$0$LoginActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
