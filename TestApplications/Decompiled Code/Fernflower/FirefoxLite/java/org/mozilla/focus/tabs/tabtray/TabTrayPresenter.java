package org.mozilla.focus.tabs.tabtray;

import java.util.ArrayList;
import java.util.List;
import org.mozilla.rocket.tabs.Session;

public class TabTrayPresenter implements TabTrayContract.Presenter {
   private TabTrayContract.Model model;
   private TabTrayContract.View view;

   TabTrayPresenter(final TabTrayContract.View var1, final TabTrayContract.Model var2) {
      this.view = var1;
      this.model = var2;
      this.model.loadTabs(new TabTrayContract.Model.OnLoadCompleteListener() {
         public void onLoadComplete() {
            var1.initData(var2.getTabs(), var2.getFocusedTab());
         }
      });
   }

   public void closeAllTabs() {
      this.view.refreshData(new ArrayList(), (Session)null);
      this.view.closeTabTray();
      this.view.navigateToHome();
      this.model.clearTabs();
   }

   public void tabClicked(int var1) {
      this.model.switchTab(var1);
      this.view.tabSwitched(var1);
   }

   public void tabCloseClicked(int var1) {
      this.model.removeTab(var1);
      List var2 = this.model.getTabs();
      var1 = var2.indexOf(this.model.getFocusedTab());
      if (var2.isEmpty()) {
         this.view.closeTabTray();
         this.view.navigateToHome();
      } else if (var1 >= 0 && var1 < var2.size()) {
         this.model.switchTab(var1);
      }

   }

   public void tabTrayClosed() {
      this.model.unsubscribe();
   }

   public void viewReady() {
      List var1 = this.model.getTabs();
      if (var1.isEmpty()) {
         this.view.closeTabTray();
      } else {
         this.model.subscribe(new TabTrayContract.Model.Observer() {
            public void onTabUpdate(Session var1) {
               TabTrayPresenter.this.view.refreshTabData(var1);
            }

            public void onUpdate(List var1) {
               TabTrayPresenter.this.view.refreshData(var1, TabTrayPresenter.this.model.getFocusedTab());
               TabTrayPresenter.this.model.loadTabs((TabTrayContract.Model.OnLoadCompleteListener)null);
            }
         });
         this.view.showFocusedTab(var1.indexOf(this.model.getFocusedTab()));
      }

   }
}
