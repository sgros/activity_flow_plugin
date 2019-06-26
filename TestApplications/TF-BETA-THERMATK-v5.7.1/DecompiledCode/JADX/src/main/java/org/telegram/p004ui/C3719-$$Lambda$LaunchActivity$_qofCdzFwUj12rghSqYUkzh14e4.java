package org.telegram.p004ui;

import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.p004ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4 */
public final /* synthetic */ class C3719-$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4 implements SharingLocationsAlertDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ int[] f$1;

    public /* synthetic */ C3719-$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4(LaunchActivity launchActivity, int[] iArr) {
        this.f$0 = launchActivity;
        this.f$1 = iArr;
    }

    public final void didSelectLocation(SharingLocationInfo sharingLocationInfo) {
        this.f$0.lambda$handleIntent$7$LaunchActivity(this.f$1, sharingLocationInfo);
    }
}
