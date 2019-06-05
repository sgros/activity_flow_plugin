package org.mozilla.focus.tabs.tabtray;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.browser.session.Download;
import mozilla.components.browser.session.Session.FindResult;
import org.mozilla.focus.tabs.tabtray.TabTrayContract.Model.OnLoadCompleteListener;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.Session.Observer;
import org.mozilla.rocket.tabs.Session.Observer.DefaultImpls;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.SessionManager.Factor;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;

/* compiled from: TabsSessionModel.kt */
public final class TabsSessionModel implements Model {
    private SessionModelObserver modelObserver;
    private final SessionManager sessionManager;
    private final ArrayList<Session> tabs = new ArrayList();

    /* compiled from: TabsSessionModel.kt */
    private static abstract class SessionModelObserver implements Observer, SessionManager.Observer {
        private Session session;

        public abstract void onTabModelChanged$app_focusWebkitRelease(Session session);

        public boolean onDownload(Session session, Download download) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(download, "download");
            return DefaultImpls.onDownload(this, session, download);
        }

        public void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view) {
            Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            DefaultImpls.onEnterFullScreen(this, fullscreenCallback, view);
        }

        public void onExitFullScreen() {
            DefaultImpls.onExitFullScreen(this);
        }

        public void onFindResult(Session session, FindResult findResult) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(findResult, "result");
            DefaultImpls.onFindResult(this, session, findResult);
        }

        public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
            Intrinsics.checkParameterIsNotNull(str, "origin");
            DefaultImpls.onGeolocationPermissionsShowPrompt(this, str, callback);
        }

        public void onLoadingStateChanged(Session session, boolean z) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            DefaultImpls.onLoadingStateChanged(this, session, z);
        }

        public void onLongPress(Session session, HitTarget hitTarget) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            DefaultImpls.onLongPress(this, session, hitTarget);
        }

        public void onNavigationStateChanged(Session session, boolean z, boolean z2) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            DefaultImpls.onNavigationStateChanged(this, session, z, z2);
        }

        public void onProgress(Session session, int i) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            DefaultImpls.onProgress(this, session, i);
        }

        public void onSecurityChanged(Session session, boolean z) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            DefaultImpls.onSecurityChanged(this, session, z);
        }

        public void onSessionAdded(Session session, Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            SessionManager.Observer.DefaultImpls.onSessionAdded(this, session, bundle);
        }

        public void onSessionCountChanged(int i) {
            SessionManager.Observer.DefaultImpls.onSessionCountChanged(this, i);
        }

        public void onUrlChanged(Session session, String str) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            onTabModelChanged$app_focusWebkitRelease(session);
        }

        public void onTitleChanged(Session session, String str) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            onTabModelChanged$app_focusWebkitRelease(session);
        }

        public void onReceivedIcon(Bitmap bitmap) {
            Session session = this.session;
            if (session != null) {
                onTabModelChanged$app_focusWebkitRelease(session);
            }
        }

        public void onFocusChanged(Session session, Factor factor) {
            Intrinsics.checkParameterIsNotNull(factor, "factor");
            Session session2 = this.session;
            if (session2 != null) {
                session2.unregister((Observer) this);
            }
            this.session = session;
            session = this.session;
            if (session != null) {
                session.register((Observer) this);
            }
        }
    }

    public TabsSessionModel(SessionManager sessionManager) {
        Intrinsics.checkParameterIsNotNull(sessionManager, "sessionManager");
        this.sessionManager = sessionManager;
    }

    public void loadTabs(OnLoadCompleteListener onLoadCompleteListener) {
        this.tabs.clear();
        this.tabs.addAll(this.sessionManager.getTabs());
        if (onLoadCompleteListener != null) {
            onLoadCompleteListener.onLoadComplete();
        }
    }

    public List<Session> getTabs() {
        return this.tabs;
    }

    public Session getFocusedTab() {
        return this.sessionManager.getFocusSession();
    }

    public void switchTab(int i) {
        if (i >= 0 && i < this.tabs.size()) {
            Object obj = this.tabs.get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "tabs[tabPosition]");
            Session session = (Session) obj;
            if ((this.sessionManager.getTabs().indexOf(session) != -1 ? 1 : null) != null) {
                this.sessionManager.switchToTab(session.getId());
            }
        }
    }

    public void removeTab(int i) {
        if (i >= 0 && i < this.tabs.size()) {
            this.sessionManager.dropTab(((Session) this.tabs.get(i)).getId());
        }
    }

    public void clearTabs() {
        for (Session id : this.sessionManager.getTabs()) {
            this.sessionManager.dropTab(id.getId());
        }
    }

    public void subscribe(Model.Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        if (this.modelObserver == null) {
            this.modelObserver = new TabsSessionModel$subscribe$1(this, observer);
        }
        SessionManager sessionManager = this.sessionManager;
        SessionModelObserver sessionModelObserver = this.modelObserver;
        if (sessionModelObserver != null) {
            sessionManager.register((SessionManager.Observer) sessionModelObserver);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Observer");
    }

    public void unsubscribe() {
        if (this.modelObserver != null) {
            SessionManager sessionManager = this.sessionManager;
            SessionModelObserver sessionModelObserver = this.modelObserver;
            if (sessionModelObserver != null) {
                sessionManager.unregister((SessionManager.Observer) sessionModelObserver);
                this.modelObserver = (SessionModelObserver) null;
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Observer");
        }
    }
}
