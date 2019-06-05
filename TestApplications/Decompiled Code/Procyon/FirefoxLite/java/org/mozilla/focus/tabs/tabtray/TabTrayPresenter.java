// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import java.util.List;
import org.mozilla.rocket.tabs.Session;
import java.util.ArrayList;

public class TabTrayPresenter implements Presenter
{
    private Model model;
    private View view;
    
    TabTrayPresenter(final View view, final Model model) {
        this.view = view;
        (this.model = model).loadTabs((TabTrayContract.Model.OnLoadCompleteListener)new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete() {
                view.initData(model.getTabs(), model.getFocusedTab());
            }
        });
    }
    
    @Override
    public void closeAllTabs() {
        this.view.refreshData(new ArrayList<Session>(), null);
        this.view.closeTabTray();
        this.view.navigateToHome();
        this.model.clearTabs();
    }
    
    @Override
    public void tabClicked(final int n) {
        this.model.switchTab(n);
        this.view.tabSwitched(n);
    }
    
    @Override
    public void tabCloseClicked(int index) {
        this.model.removeTab(index);
        final List<Session> tabs = this.model.getTabs();
        index = tabs.indexOf(this.model.getFocusedTab());
        if (tabs.isEmpty()) {
            this.view.closeTabTray();
            this.view.navigateToHome();
        }
        else if (index >= 0 && index < tabs.size()) {
            this.model.switchTab(index);
        }
    }
    
    @Override
    public void tabTrayClosed() {
        this.model.unsubscribe();
    }
    
    @Override
    public void viewReady() {
        final List<Session> tabs = this.model.getTabs();
        if (tabs.isEmpty()) {
            this.view.closeTabTray();
        }
        else {
            this.model.subscribe((TabTrayContract.Model.Observer)new Observer() {
                @Override
                public void onTabUpdate(final Session session) {
                    TabTrayPresenter.this.view.refreshTabData(session);
                }
                
                @Override
                public void onUpdate(final List<Session> list) {
                    TabTrayPresenter.this.view.refreshData(list, TabTrayPresenter.this.model.getFocusedTab());
                    TabTrayPresenter.this.model.loadTabs(null);
                }
            });
            this.view.showFocusedTab(tabs.indexOf(this.model.getFocusedTab()));
        }
    }
}
