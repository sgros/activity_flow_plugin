package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw */
public final /* synthetic */ class C1914-$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw implements OnClickListener {
    private final /* synthetic */ PrivacyControlActivity f$0;
    private final /* synthetic */ SharedPreferences f$1;

    public /* synthetic */ C1914-$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw(PrivacyControlActivity privacyControlActivity, SharedPreferences sharedPreferences) {
        this.f$0 = privacyControlActivity;
        this.f$1 = sharedPreferences;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processDone$5$PrivacyControlActivity(this.f$1, dialogInterface, i);
    }
}
