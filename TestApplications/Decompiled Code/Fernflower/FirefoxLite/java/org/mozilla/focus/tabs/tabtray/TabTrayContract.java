package org.mozilla.focus.tabs.tabtray;

import java.util.List;
import org.mozilla.rocket.tabs.Session;

class TabTrayContract {
   interface Model {
      void clearTabs();

      Session getFocusedTab();

      List getTabs();

      void loadTabs(TabTrayContract.Model.OnLoadCompleteListener var1);

      void removeTab(int var1);

      void subscribe(TabTrayContract.Model.Observer var1);

      void switchTab(int var1);

      void unsubscribe();

      public interface Observer {
         void onTabUpdate(Session var1);

         void onUpdate(List var1);
      }

      public interface OnLoadCompleteListener {
         void onLoadComplete();
      }
   }

   interface Presenter {
      void closeAllTabs();

      void tabClicked(int var1);

      void tabCloseClicked(int var1);

      void tabTrayClosed();

      void viewReady();
   }

   interface View {
      void closeTabTray();

      void initData(List var1, Session var2);

      void navigateToHome();

      void refreshData(List var1, Session var2);

      void refreshTabData(Session var1);

      void showFocusedTab(int var1);

      void tabSwitched(int var1);
   }
}
