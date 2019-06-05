package org.mozilla.focus.home;

import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.history.model.Site;

public class TopSitesPresenter implements TopSitesContract.Presenter {
   private TopSitesContract.Model model;
   private List sites = new ArrayList();
   private TopSitesContract.View view;

   public List getSites() {
      return this.sites;
   }

   public void pinSite(Site var1, Runnable var2) {
      this.model.pinSite(var1, var2);
   }

   public void populateSites() {
      if (this.view != null) {
         this.view.showSites(this.sites);
      }

   }

   public void removeSite(Site var1) {
      this.sites.remove(var1);
      if (this.view != null) {
         this.view.removeSite(var1);
      }

   }

   public void setModel(TopSitesContract.Model var1) {
      this.model = var1;
   }

   public void setSites(List var1) {
      if (var1 != null) {
         this.sites = var1;
      } else {
         this.sites = new ArrayList();
      }

   }

   public void setView(TopSitesContract.View var1) {
      this.view = var1;
   }
}
