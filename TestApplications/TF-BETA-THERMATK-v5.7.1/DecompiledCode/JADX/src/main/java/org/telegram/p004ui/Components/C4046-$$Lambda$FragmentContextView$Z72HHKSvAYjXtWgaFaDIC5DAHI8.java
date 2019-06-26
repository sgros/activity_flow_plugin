package org.telegram.p004ui.Components;

import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.p004ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$FragmentContextView$Z72HHKSvAYjXtWgaFaDIC5DAHI8 */
public final /* synthetic */ class C4046-$$Lambda$FragmentContextView$Z72HHKSvAYjXtWgaFaDIC5DAHI8 implements SharingLocationsAlertDelegate {
    private final /* synthetic */ FragmentContextView f$0;

    public /* synthetic */ C4046-$$Lambda$FragmentContextView$Z72HHKSvAYjXtWgaFaDIC5DAHI8(FragmentContextView fragmentContextView) {
        this.f$0 = fragmentContextView;
    }

    public final void didSelectLocation(SharingLocationInfo sharingLocationInfo) {
        this.f$0.openSharingLocation(sharingLocationInfo);
    }
}
