package org.mozilla.focus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$5HFyet6oHYCHE46-DyDOrH6PVuc */
public final /* synthetic */ class C0527-$$Lambda$DialogUtils$5HFyet6oHYCHE46-DyDOrH6PVuc implements OnCancelListener {
    private final /* synthetic */ Context f$0;

    public /* synthetic */ C0527-$$Lambda$DialogUtils$5HFyet6oHYCHE46-DyDOrH6PVuc(Context context) {
        this.f$0 = context;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        DialogUtils.telemetryShareApp(this.f$0, "dismiss");
    }
}
