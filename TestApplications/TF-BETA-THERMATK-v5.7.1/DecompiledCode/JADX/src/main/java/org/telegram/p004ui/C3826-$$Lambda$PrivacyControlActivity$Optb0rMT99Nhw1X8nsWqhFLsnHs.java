package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.PrivacyUsersActivity.PrivacyActivityDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacyControlActivity$Optb0rMT99Nhw1X8nsWqhFLsnHs */
public final /* synthetic */ class C3826-$$Lambda$PrivacyControlActivity$Optb0rMT99Nhw1X8nsWqhFLsnHs implements PrivacyActivityDelegate {
    private final /* synthetic */ PrivacyControlActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3826-$$Lambda$PrivacyControlActivity$Optb0rMT99Nhw1X8nsWqhFLsnHs(PrivacyControlActivity privacyControlActivity, int i) {
        this.f$0 = privacyControlActivity;
        this.f$1 = i;
    }

    public final void didUpdateUserList(ArrayList arrayList, boolean z) {
        this.f$0.lambda$null$1$PrivacyControlActivity(this.f$1, arrayList, z);
    }
}
