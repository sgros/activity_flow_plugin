package org.mozilla.focus.home;

import java.util.List;
import org.mozilla.focus.history.model.Site;

public class TopSitesContract {

    interface Model {
        void pinSite(Site site, Runnable runnable);
    }

    interface Presenter {
        List<Site> getSites();

        void pinSite(Site site, Runnable runnable);

        void populateSites();

        void removeSite(Site site);

        void setModel(Model model);

        void setSites(List<Site> list);

        void setView(View view);
    }

    interface View {
        void removeSite(Site site);

        void showSites(List<Site> list);
    }
}
