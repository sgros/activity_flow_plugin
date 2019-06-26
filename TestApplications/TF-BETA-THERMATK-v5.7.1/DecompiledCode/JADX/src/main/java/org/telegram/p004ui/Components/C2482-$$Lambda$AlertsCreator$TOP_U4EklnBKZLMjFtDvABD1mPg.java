package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.LaunchActivity;
import org.telegram.tgnet.TLRPC.TL_langPackLanguage;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg */
public final /* synthetic */ class C2482-$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg implements OnClickListener {
    private final /* synthetic */ TL_langPackLanguage f$0;
    private final /* synthetic */ LaunchActivity f$1;

    public /* synthetic */ C2482-$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg(TL_langPackLanguage tL_langPackLanguage, LaunchActivity launchActivity) {
        this.f$0 = tL_langPackLanguage;
        this.f$1 = launchActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createLanguageAlert$2(this.f$0, this.f$1, dialogInterface, i);
    }
}
