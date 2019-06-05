package org.mozilla.focus.persistence;

import java.util.List;

public abstract class TabDao {
    public abstract void deleteAllTabs();

    public abstract List<TabEntity> getTabs();

    public abstract void insertTabs(TabEntity... tabEntityArr);

    public void deleteAllTabsAndInsertTabsInTransaction(TabEntity... tabEntityArr) {
        deleteAllTabs();
        insertTabs(tabEntityArr);
    }
}
