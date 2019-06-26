package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU */
public final /* synthetic */ class C2489-$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU implements OnClickListener {
    private final /* synthetic */ int[] f$0;
    private final /* synthetic */ IntCallback f$1;

    public /* synthetic */ C2489-$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU(int[] iArr, IntCallback intCallback) {
        this.f$0 = iArr;
        this.f$1 = intCallback;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createLocationUpdateDialog$29(this.f$0, this.f$1, dialogInterface, i);
    }
}
