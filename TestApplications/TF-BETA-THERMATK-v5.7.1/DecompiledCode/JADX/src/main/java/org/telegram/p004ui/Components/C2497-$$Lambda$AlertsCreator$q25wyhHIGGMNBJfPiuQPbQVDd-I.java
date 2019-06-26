package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd-I */
public final /* synthetic */ class C2497-$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd-I implements OnClickListener {
    private final /* synthetic */ long f$0;
    private final /* synthetic */ Runnable f$1;

    public /* synthetic */ C2497-$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd-I(long j, Runnable runnable) {
        this.f$0 = j;
        this.f$1 = runnable;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createColorSelectDialog$26(this.f$0, this.f$1, dialogInterface, i);
    }
}
