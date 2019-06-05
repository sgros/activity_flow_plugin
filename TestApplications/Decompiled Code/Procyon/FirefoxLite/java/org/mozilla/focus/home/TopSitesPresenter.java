// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import java.util.ArrayList;
import org.mozilla.focus.history.model.Site;
import java.util.List;

public class TopSitesPresenter implements Presenter
{
    private Model model;
    private List<Site> sites;
    private View view;
    
    public TopSitesPresenter() {
        this.sites = new ArrayList<Site>();
    }
    
    @Override
    public List<Site> getSites() {
        return this.sites;
    }
    
    @Override
    public void pinSite(final Site site, final Runnable runnable) {
        this.model.pinSite(site, runnable);
    }
    
    @Override
    public void populateSites() {
        if (this.view != null) {
            this.view.showSites(this.sites);
        }
    }
    
    @Override
    public void removeSite(final Site site) {
        this.sites.remove(site);
        if (this.view != null) {
            this.view.removeSite(site);
        }
    }
    
    @Override
    public void setModel(final Model model) {
        this.model = model;
    }
    
    @Override
    public void setSites(final List<Site> sites) {
        if (sites != null) {
            this.sites = sites;
        }
        else {
            this.sites = new ArrayList<Site>();
        }
    }
    
    @Override
    public void setView(final View view) {
        this.view = view;
    }
}
