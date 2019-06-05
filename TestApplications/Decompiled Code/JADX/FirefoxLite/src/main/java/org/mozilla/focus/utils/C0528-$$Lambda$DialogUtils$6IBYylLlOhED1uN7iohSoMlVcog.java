package org.mozilla.focus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog */
public final /* synthetic */ class C0528-$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog implements OnCancelListener {
    private final /* synthetic */ Context f$0;

    public /* synthetic */ C0528-$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog(Context context) {
        this.f$0 = context;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        DialogUtils.lambda$showRateAppDialog$0(this.f$0, dialogInterface);
    }
}
