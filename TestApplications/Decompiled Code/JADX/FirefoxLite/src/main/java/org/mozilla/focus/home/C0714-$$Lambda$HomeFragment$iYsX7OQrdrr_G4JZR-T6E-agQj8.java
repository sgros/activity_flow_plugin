package org.mozilla.focus.home;

import org.mozilla.focus.history.model.Site;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR-T6E-agQj8 */
public final /* synthetic */ class C0714-$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR-T6E-agQj8 implements OnRemovedListener {
    private final /* synthetic */ Site f$0;

    public /* synthetic */ C0714-$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR-T6E-agQj8(Site site) {
        this.f$0 = site;
    }

    public final void onRemoved(Site site) {
        this.f$0.setViewCount(this.f$0.getViewCount() + site.getViewCount());
    }
}
