package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.CacheControlActivity;
import org.telegram.p004ui.LaunchActivity;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$xGMadtvSKvLVwUxwu6lfN7pshfU */
public final /* synthetic */ class C2499-$$Lambda$AlertsCreator$xGMadtvSKvLVwUxwu6lfN7pshfU implements OnClickListener {
    private final /* synthetic */ LaunchActivity f$0;

    public /* synthetic */ C2499-$$Lambda$AlertsCreator$xGMadtvSKvLVwUxwu6lfN7pshfU(LaunchActivity launchActivity) {
        this.f$0 = launchActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$runLinkRequest$27$LaunchActivity(new CacheControlActivity());
    }
}
