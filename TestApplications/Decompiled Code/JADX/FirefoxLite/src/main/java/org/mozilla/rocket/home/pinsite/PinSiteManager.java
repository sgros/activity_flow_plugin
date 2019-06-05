package org.mozilla.rocket.home.pinsite;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.history.model.Site;

/* compiled from: PinSiteManager.kt */
public final class PinSiteManager implements PinSiteDelegate {
    private final PinSiteDelegate pinSiteDelegate;

    public List<Site> getPinSites() {
        return this.pinSiteDelegate.getPinSites();
    }

    public boolean isEnabled() {
        return this.pinSiteDelegate.isEnabled();
    }

    public boolean isFirstTimeEnable() {
        return this.pinSiteDelegate.isFirstTimeEnable();
    }

    public boolean isPinned(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        return this.pinSiteDelegate.isPinned(site);
    }

    public void pin(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        this.pinSiteDelegate.pin(site);
    }

    public void unpinned(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        this.pinSiteDelegate.unpinned(site);
    }

    public PinSiteManager(PinSiteDelegate pinSiteDelegate) {
        Intrinsics.checkParameterIsNotNull(pinSiteDelegate, "pinSiteDelegate");
        this.pinSiteDelegate = pinSiteDelegate;
    }
}
