package org.mozilla.focus.tabs.tabtray;

import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.tabs.tabtray.TabTrayContract.Model.Observer;
import org.mozilla.focus.tabs.tabtray.TabTrayContract.Model.OnLoadCompleteListener;
import org.mozilla.rocket.tabs.Session;

public class TabTrayPresenter implements Presenter {
    private Model model;
    private View view;

    /* renamed from: org.mozilla.focus.tabs.tabtray.TabTrayPresenter$2 */
    class C07462 implements Observer {
        C07462() {
        }

        public void onUpdate(List<Session> list) {
            TabTrayPresenter.this.view.refreshData(list, TabTrayPresenter.this.model.getFocusedTab());
            TabTrayPresenter.this.model.loadTabs(null);
        }

        public void onTabUpdate(Session session) {
            TabTrayPresenter.this.view.refreshTabData(session);
        }
    }

    TabTrayPresenter(final View view, final Model model) {
        this.view = view;
        this.model = model;
        this.model.loadTabs(new OnLoadCompleteListener() {
            public void onLoadComplete() {
                view.initData(model.getTabs(), model.getFocusedTab());
            }
        });
    }

    public void viewReady() {
        List tabs = this.model.getTabs();
        if (tabs.isEmpty()) {
            this.view.closeTabTray();
            return;
        }
        this.model.subscribe(new C07462());
        this.view.showFocusedTab(tabs.indexOf(this.model.getFocusedTab()));
    }

    public void tabClicked(int i) {
        this.model.switchTab(i);
        this.view.tabSwitched(i);
    }

    public void tabCloseClicked(int i) {
        this.model.removeTab(i);
        List tabs = this.model.getTabs();
        int indexOf = tabs.indexOf(this.model.getFocusedTab());
        if (tabs.isEmpty()) {
            this.view.closeTabTray();
            this.view.navigateToHome();
        } else if (indexOf >= 0 && indexOf < tabs.size()) {
            this.model.switchTab(indexOf);
        }
    }

    public void tabTrayClosed() {
        this.model.unsubscribe();
    }

    public void closeAllTabs() {
        this.view.refreshData(new ArrayList(), null);
        this.view.closeTabTray();
        this.view.navigateToHome();
        this.model.clearTabs();
    }
}
