// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.home.pinsite;

import org.mozilla.focus.history.model.Site;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

public final class PinSiteManager implements PinSiteDelegate
{
    private final PinSiteDelegate pinSiteDelegate;
    
    public PinSiteManager(final PinSiteDelegate pinSiteDelegate) {
        Intrinsics.checkParameterIsNotNull(pinSiteDelegate, "pinSiteDelegate");
        this.pinSiteDelegate = pinSiteDelegate;
    }
    
    @Override
    public List<Site> getPinSites() {
        return this.pinSiteDelegate.getPinSites();
    }
    
    @Override
    public boolean isEnabled() {
        return this.pinSiteDelegate.isEnabled();
    }
    
    @Override
    public boolean isFirstTimeEnable() {
        return this.pinSiteDelegate.isFirstTimeEnable();
    }
    
    @Override
    public boolean isPinned(final Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        return this.pinSiteDelegate.isPinned(site);
    }
    
    @Override
    public void pin(final Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        this.pinSiteDelegate.pin(site);
    }
    
    @Override
    public void unpinned(final Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        this.pinSiteDelegate.unpinned(site);
    }
}
