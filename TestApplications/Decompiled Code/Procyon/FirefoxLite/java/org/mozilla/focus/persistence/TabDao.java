// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import java.util.List;

public abstract class TabDao
{
    public abstract void deleteAllTabs();
    
    public void deleteAllTabsAndInsertTabsInTransaction(final TabEntity... array) {
        this.deleteAllTabs();
        this.insertTabs(array);
    }
    
    public abstract List<TabEntity> getTabs();
    
    public abstract void insertTabs(final TabEntity... p0);
}
