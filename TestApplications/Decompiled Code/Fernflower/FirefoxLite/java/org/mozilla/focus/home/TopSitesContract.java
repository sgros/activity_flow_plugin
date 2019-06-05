package org.mozilla.focus.home;

import java.util.List;
import org.mozilla.focus.history.model.Site;

public class TopSitesContract {
   interface Model {
      void pinSite(Site var1, Runnable var2);
   }

   interface Presenter {
      List getSites();

      void pinSite(Site var1, Runnable var2);

      void populateSites();

      void removeSite(Site var1);

      void setModel(TopSitesContract.Model var1);

      void setSites(List var1);

      void setView(TopSitesContract.View var1);
   }

   interface View {
      void removeSite(Site var1);

      void showSites(List var1);
   }
}
