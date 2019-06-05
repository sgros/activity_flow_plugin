package org.mozilla.focus.home;

import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.history.model.Site;

public class TopSitesPresenter implements Presenter {
    private Model model;
    private List<Site> sites = new ArrayList();
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void populateSites() {
        if (this.view != null) {
            this.view.showSites(this.sites);
        }
    }

    public void removeSite(Site site) {
        this.sites.remove(site);
        if (this.view != null) {
            this.view.removeSite(site);
        }
    }

    public void pinSite(Site site, Runnable runnable) {
        this.model.pinSite(site, runnable);
    }

    public void setSites(List<Site> list) {
        if (list != null) {
            this.sites = list;
        } else {
            this.sites = new ArrayList();
        }
    }

    public List<Site> getSites() {
        return this.sites;
    }
}
