package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.tgnet.ConnectionsManager;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$5X2t5ruqXcfyHHT0cc-ZZigoOb8 */
public final /* synthetic */ class C2464-$$Lambda$AlertsCreator$5X2t5ruqXcfyHHT0cc-ZZigoOb8 implements OnCancelListener {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C2464-$$Lambda$AlertsCreator$5X2t5ruqXcfyHHT0cc-ZZigoOb8(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.f$0).cancelRequest(this.f$1, true);
    }
}
