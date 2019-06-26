package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE */
public final /* synthetic */ class C2467-$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE implements OnClickListener {
    private final /* synthetic */ IntCallback f$0;

    public /* synthetic */ C2467-$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE(IntCallback intCallback) {
        this.f$0 = intCallback;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.run(0);
    }
}
