// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.webkit.GeolocationPermissions$Callback;
import android.view.View;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.Session.Observer;
import mozilla.components.browser.session.Download;
import kotlin.TypeCastException;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.tabs.Session;
import java.util.ArrayList;
import org.mozilla.rocket.tabs.SessionManager;

public final class TabsSessionModel implements Model
{
    private SessionModelObserver modelObserver;
    private final SessionManager sessionManager;
    private final ArrayList<Session> tabs;
    
    public TabsSessionModel(final SessionManager sessionManager) {
        Intrinsics.checkParameterIsNotNull(sessionManager, "sessionManager");
        this.sessionManager = sessionManager;
        this.tabs = new ArrayList<Session>();
    }
    
    @Override
    public void clearTabs() {
        final Iterator<Session> iterator = this.sessionManager.getTabs().iterator();
        while (iterator.hasNext()) {
            this.sessionManager.dropTab(iterator.next().getId());
        }
    }
    
    @Override
    public Session getFocusedTab() {
        return this.sessionManager.getFocusSession();
    }
    
    @Override
    public List<Session> getTabs() {
        return this.tabs;
    }
    
    @Override
    public void loadTabs(final OnLoadCompleteListener onLoadCompleteListener) {
        this.tabs.clear();
        this.tabs.addAll(this.sessionManager.getTabs());
        if (onLoadCompleteListener != null) {
            onLoadCompleteListener.onLoadComplete();
        }
    }
    
    @Override
    public void removeTab(final int index) {
        if (index >= 0 && index < this.tabs.size()) {
            this.sessionManager.dropTab(this.tabs.get(index).getId());
        }
    }
    
    @Override
    public void subscribe(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        if (this.modelObserver == null) {
            this.modelObserver = (SessionModelObserver)new TabsSessionModel$subscribe.TabsSessionModel$subscribe$1(this, observer);
        }
        final SessionManager sessionManager = this.sessionManager;
        final SessionModelObserver modelObserver = this.modelObserver;
        if (modelObserver != null) {
            sessionManager.register((SessionManager.Observer)modelObserver);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Observer");
    }
    
    @Override
    public void switchTab(int index) {
        if (index >= 0 && index < this.tabs.size()) {
            final Session value = this.tabs.get(index);
            Intrinsics.checkExpressionValueIsNotNull(value, "tabs[tabPosition]");
            final Session session = value;
            if (this.sessionManager.getTabs().indexOf(session) != -1) {
                index = 1;
            }
            else {
                index = 0;
            }
            if (index != 0) {
                this.sessionManager.switchToTab(session.getId());
            }
        }
    }
    
    @Override
    public void unsubscribe() {
        if (this.modelObserver != null) {
            final SessionManager sessionManager = this.sessionManager;
            final SessionModelObserver modelObserver = this.modelObserver;
            if (modelObserver == null) {
                throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Observer");
            }
            sessionManager.unregister((SessionManager.Observer)modelObserver);
            this.modelObserver = null;
        }
    }
    
    private abstract static class SessionModelObserver implements Session.Observer, SessionManager.Observer
    {
        private Session session;
        
        public SessionModelObserver() {
        }
        
        @Override
        public boolean onDownload(final Session session, final Download download) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(download, "download");
            return Session.Observer.DefaultImpls.onDownload((Session.Observer)this, session, download);
        }
        
        @Override
        public void onEnterFullScreen(final TabView.FullscreenCallback fullscreenCallback, final android.view.View view) {
            Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            Session.Observer.DefaultImpls.onEnterFullScreen((Session.Observer)this, fullscreenCallback, view);
        }
        
        @Override
        public void onExitFullScreen() {
            Session.Observer.DefaultImpls.onExitFullScreen((Session.Observer)this);
        }
        
        @Override
        public void onFindResult(final Session session, final mozilla.components.browser.session.Session.FindResult findResult) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(findResult, "result");
            Session.Observer.DefaultImpls.onFindResult((Session.Observer)this, session, findResult);
        }
        
        @Override
        public void onFocusChanged(Session session, final Factor factor) {
            Intrinsics.checkParameterIsNotNull(factor, "factor");
            final Session session2 = this.session;
            if (session2 != null) {
                session2.unregister((Session.Observer)this);
            }
            this.session = session;
            session = this.session;
            if (session != null) {
                session.register((Session.Observer)this);
            }
        }
        
        @Override
        public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
            Intrinsics.checkParameterIsNotNull(s, "origin");
            Session.Observer.DefaultImpls.onGeolocationPermissionsShowPrompt((Session.Observer)this, s, geolocationPermissions$Callback);
        }
        
        @Override
        public void onLoadingStateChanged(final Session session, final boolean b) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Session.Observer.DefaultImpls.onLoadingStateChanged((Session.Observer)this, session, b);
        }
        
        @Override
        public void onLongPress(final Session session, final TabView.HitTarget hitTarget) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            Session.Observer.DefaultImpls.onLongPress((Session.Observer)this, session, hitTarget);
        }
        
        @Override
        public void onNavigationStateChanged(final Session session, final boolean b, final boolean b2) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Session.Observer.DefaultImpls.onNavigationStateChanged((Session.Observer)this, session, b, b2);
        }
        
        @Override
        public void onProgress(final Session session, final int n) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Session.Observer.DefaultImpls.onProgress((Session.Observer)this, session, n);
        }
        
        @Override
        public void onReceivedIcon(final Bitmap bitmap) {
            final Session session = this.session;
            if (session != null) {
                this.onTabModelChanged$app_focusWebkitRelease(session);
            }
        }
        
        @Override
        public void onSecurityChanged(final Session session, final boolean b) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Session.Observer.DefaultImpls.onSecurityChanged((Session.Observer)this, session, b);
        }
        
        @Override
        public void onSessionAdded(final Session session, final Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            SessionManager.Observer.DefaultImpls.onSessionAdded((SessionManager.Observer)this, session, bundle);
        }
        
        @Override
        public void onSessionCountChanged(final int n) {
            SessionManager.Observer.DefaultImpls.onSessionCountChanged((SessionManager.Observer)this, n);
        }
        
        public abstract void onTabModelChanged$app_focusWebkitRelease(final Session p0);
        
        @Override
        public void onTitleChanged(final Session session, final String s) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            this.onTabModelChanged$app_focusWebkitRelease(session);
        }
        
        @Override
        public void onUrlChanged(final Session session, final String s) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            this.onTabModelChanged$app_focusWebkitRelease(session);
        }
    }
}
