package org.mozilla.focus.tabs.tabtray;

import java.util.List;
import org.mozilla.rocket.tabs.Session;

class TabTrayContract {

    interface Model {

        public interface Observer {
            void onTabUpdate(Session session);

            void onUpdate(List<Session> list);
        }

        public interface OnLoadCompleteListener {
            void onLoadComplete();
        }

        void clearTabs();

        Session getFocusedTab();

        List<Session> getTabs();

        void loadTabs(OnLoadCompleteListener onLoadCompleteListener);

        void removeTab(int i);

        void subscribe(Observer observer);

        void switchTab(int i);

        void unsubscribe();
    }

    interface Presenter {
        void closeAllTabs();

        void tabClicked(int i);

        void tabCloseClicked(int i);

        void tabTrayClosed();

        void viewReady();
    }

    interface View {
        void closeTabTray();

        void initData(List<Session> list, Session session);

        void navigateToHome();

        void refreshData(List<Session> list, Session session);

        void refreshTabData(Session session);

        void showFocusedTab(int i);

        void tabSwitched(int i);
    }
}
