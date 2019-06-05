// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.home.pinsite;

import org.mozilla.focus.history.model.Site;
import java.util.List;

public interface PinSiteDelegate
{
    List<Site> getPinSites();
    
    boolean isEnabled();
    
    boolean isFirstTimeEnable();
    
    boolean isPinned(final Site p0);
    
    void pin(final Site p0);
    
    void unpinned(final Site p0);
}
