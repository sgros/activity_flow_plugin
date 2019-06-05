// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import java.util.List;
import org.mozilla.rocket.tabs.Session;

class TabTrayContract
{
    interface Model
    {
        void clearTabs();
        
        Session getFocusedTab();
        
        List<Session> getTabs();
        
        void loadTabs(final OnLoadCompleteListener p0);
        
        void removeTab(final int p0);
        
        void subscribe(final Observer p0);
        
        void switchTab(final int p0);
        
        void unsubscribe();
        
        public interface Observer
        {
            void onTabUpdate(final Session p0);
            
            void onUpdate(final List<Session> p0);
        }
        
        public interface OnLoadCompleteListener
        {
            void onLoadComplete();
        }
    }
    
    interface Presenter
    {
        void closeAllTabs();
        
        void tabClicked(final int p0);
        
        void tabCloseClicked(final int p0);
        
        void tabTrayClosed();
        
        void viewReady();
    }
    
    interface View
    {
        void closeTabTray();
        
        void initData(final List<Session> p0, final Session p1);
        
        void navigateToHome();
        
        void refreshData(final List<Session> p0, final Session p1);
        
        void refreshTabData(final Session p0);
        
        void showFocusedTab(final int p0);
        
        void tabSwitched(final int p0);
    }
}
