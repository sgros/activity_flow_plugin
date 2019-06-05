package org.mozilla.focus.persistence;

import java.util.List;

public abstract class TabDao {
   public abstract void deleteAllTabs();

   public void deleteAllTabsAndInsertTabsInTransaction(TabEntity... var1) {
      this.deleteAllTabs();
      this.insertTabs(var1);
   }

   public abstract List getTabs();

   public abstract void insertTabs(TabEntity... var1);
}
