package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$kEfeT-ztr5ipao3BeuOGHHEiQuA */
public final /* synthetic */ class C2493-$$Lambda$AlertsCreator$kEfeT-ztr5ipao3BeuOGHHEiQuA implements OnClickListener {
    private final /* synthetic */ long f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Runnable f$2;

    public /* synthetic */ C2493-$$Lambda$AlertsCreator$kEfeT-ztr5ipao3BeuOGHHEiQuA(long j, int i, Runnable runnable) {
        this.f$0 = j;
        this.f$1 = i;
        this.f$2 = runnable;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createColorSelectDialog$25(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}
