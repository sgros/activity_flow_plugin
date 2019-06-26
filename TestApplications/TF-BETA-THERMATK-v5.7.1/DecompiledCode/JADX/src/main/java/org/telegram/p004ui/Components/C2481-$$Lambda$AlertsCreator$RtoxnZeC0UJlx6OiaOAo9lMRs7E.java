package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$RtoxnZeC0UJlx6OiaOAo9lMRs7E */
public final /* synthetic */ class C2481-$$Lambda$AlertsCreator$RtoxnZeC0UJlx6OiaOAo9lMRs7E implements OnClickListener {
    private final /* synthetic */ Runnable f$0;

    public /* synthetic */ C2481-$$Lambda$AlertsCreator$RtoxnZeC0UJlx6OiaOAo9lMRs7E(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$showSecretLocationAlert$4(this.f$0, dialogInterface, i);
    }
}
