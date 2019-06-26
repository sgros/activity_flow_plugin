package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.LanguageSelectActivity;
import org.telegram.p004ui.LaunchActivity;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$LEkyy2uvzoVwagVSlDPe5F3R2jI */
public final /* synthetic */ class C2474-$$Lambda$AlertsCreator$LEkyy2uvzoVwagVSlDPe5F3R2jI implements OnClickListener {
    private final /* synthetic */ LaunchActivity f$0;

    public /* synthetic */ C2474-$$Lambda$AlertsCreator$LEkyy2uvzoVwagVSlDPe5F3R2jI(LaunchActivity launchActivity) {
        this.f$0 = launchActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$runLinkRequest$27$LaunchActivity(new LanguageSelectActivity());
    }
}
