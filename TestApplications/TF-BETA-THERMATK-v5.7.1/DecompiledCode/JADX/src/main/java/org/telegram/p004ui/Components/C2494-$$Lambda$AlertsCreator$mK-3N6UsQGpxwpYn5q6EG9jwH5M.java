package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$mK-3N6UsQGpxwpYn5q6EG9jwH5M */
public final /* synthetic */ class C2494-$$Lambda$AlertsCreator$mK-3N6UsQGpxwpYn5q6EG9jwH5M implements OnClickListener {
    private final /* synthetic */ IntCallback f$0;

    public /* synthetic */ C2494-$$Lambda$AlertsCreator$mK-3N6UsQGpxwpYn5q6EG9jwH5M(IntCallback intCallback) {
        this.f$0 = intCallback;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.run(1);
    }
}
