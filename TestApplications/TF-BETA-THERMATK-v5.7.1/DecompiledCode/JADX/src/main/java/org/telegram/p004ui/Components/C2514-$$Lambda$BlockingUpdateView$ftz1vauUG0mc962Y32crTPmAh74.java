package org.telegram.p004ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$BlockingUpdateView$ftz1vauUG0mc962Y32crTPmAh74 */
public final /* synthetic */ class C2514-$$Lambda$BlockingUpdateView$ftz1vauUG0mc962Y32crTPmAh74 implements OnClickListener {
    private final /* synthetic */ Context f$0;

    public /* synthetic */ C2514-$$Lambda$BlockingUpdateView$ftz1vauUG0mc962Y32crTPmAh74(Context context) {
        this.f$0 = context;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        BlockingUpdateView.lambda$checkApkInstallPermissions$2(this.f$0, dialogInterface, i);
    }
}
