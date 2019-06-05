// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import java.util.List;
import org.mozilla.focus.history.model.Site;

public class TopSitesContract
{
    interface Model
    {
        void pinSite(final Site p0, final Runnable p1);
    }
    
    interface Presenter
    {
        List<Site> getSites();
        
        void pinSite(final Site p0, final Runnable p1);
        
        void populateSites();
        
        void removeSite(final Site p0);
        
        void setModel(final Model p0);
        
        void setSites(final List<Site> p0);
        
        void setView(final View p0);
    }
    
    interface View
    {
        void removeSite(final Site p0);
        
        void showSites(final List<Site> p0);
    }
}
