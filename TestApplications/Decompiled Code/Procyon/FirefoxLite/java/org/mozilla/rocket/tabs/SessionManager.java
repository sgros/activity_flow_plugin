// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import java.io.Serializable;
import kotlin.TypeCastException;
import kotlin.Pair;
import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import mozilla.components.support.base.observer.Consumable;
import kotlin.jvm.functions.Function2;
import android.arch.lifecycle.LifecycleOwner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.rocket.tabs.utils.TabUtil;
import java.util.Iterator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import android.text.TextUtils;
import android.os.Bundle;
import mozilla.components.support.base.observer.ObserverRegistry;
import kotlin.jvm.internal.Intrinsics;
import java.util.LinkedList;
import java.lang.ref.WeakReference;
import mozilla.components.support.base.observer.Observable;

public final class SessionManager implements Observable<Observer>
{
    private final /* synthetic */ Observable $$delegate_0;
    private WeakReference<Session> focusRef;
    private final Notifier notifier;
    private final LinkedList<Session> sessions;
    private final TabViewProvider tabViewProvider;
    
    public SessionManager(final TabViewProvider tabViewProvider) {
        this(tabViewProvider, null, 2, null);
    }
    
    public SessionManager(final TabViewProvider tabViewProvider, final Observable<Observer> $$delegate_0) {
        Intrinsics.checkParameterIsNotNull(tabViewProvider, "tabViewProvider");
        Intrinsics.checkParameterIsNotNull($$delegate_0, "delegate");
        this.$$delegate_0 = $$delegate_0;
        this.tabViewProvider = tabViewProvider;
        this.sessions = new LinkedList<Session>();
        this.focusRef = new WeakReference<Session>(null);
        this.notifier = new Notifier(this);
    }
    
    public static final /* synthetic */ LinkedList access$getSessions$p(final SessionManager sessionManager) {
        return sessionManager.sessions;
    }
    
    private final String addTabInternal(final String url, final String s, final boolean b, final boolean b2, final Bundle bundle) {
        final Session session = new Session(null, null, null, null, 15, null);
        session.setUrl(url);
        int tabIndex;
        if (TextUtils.isEmpty((CharSequence)s)) {
            tabIndex = -1;
        }
        else {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            tabIndex = this.getTabIndex(s);
        }
        if (b) {
            session.setParentId("_open_from_external_");
            this.sessions.add(session);
        }
        else {
            this.insertTab(tabIndex, session);
        }
        this.notifier.notifyTabAdded(session, bundle);
        WeakReference<Session> focusRef;
        if (!b2 && !b) {
            focusRef = this.focusRef;
        }
        else {
            focusRef = new WeakReference<Session>(session);
        }
        this.focusRef = focusRef;
        this.getOrCreateEngineSession(session);
        this.initializeEngineView(session);
        if (b2 || b) {
            this.notifier.notifyTabFocused(session, Factor.FACTOR_TAB_ADDED);
        }
        this.notifyObservers((Function1<? super Observer, Unit>)new SessionManager$addTabInternal.SessionManager$addTabInternal$1(this));
        return session.getId();
    }
    
    private final void destroySession(final Session session) {
        this.unlink(session);
        session.unregisterObservers();
    }
    
    private final Session getTab(final String s) {
        final int tabIndex = this.getTabIndex(s);
        Session session;
        if (tabIndex == -1) {
            session = null;
        }
        else {
            session = this.sessions.get(tabIndex);
        }
        return session;
    }
    
    private final int getTabIndex(final String s) {
        if (s == null) {
            return -1;
        }
        for (int i = 0; i < this.sessions.size(); ++i) {
            final Session value = this.sessions.get(i);
            Intrinsics.checkExpressionValueIsNotNull(value, "sessions[i]");
            if (Intrinsics.areEqual(value.getId(), s)) {
                return i;
            }
        }
        return -1;
    }
    
    private final void initializeEngineView(final Session session) {
        if (session.getEngineSession() == null) {
            this.getOrCreateEngineSession(session);
        }
        String s;
        if (TextUtils.isEmpty((CharSequence)session.getUrl())) {
            s = session.getInitialUrl();
        }
        else {
            s = session.getUrl();
        }
        final TabView create = this.tabViewProvider.create();
        final TabViewEngineSession engineSession = session.getEngineSession();
        if (engineSession != null) {
            engineSession.setTabView(create);
        }
        final TabViewEngineSession engineSession2 = session.getEngineSession();
        final Bundle bundle = null;
        Bundle webViewState;
        if (engineSession2 != null) {
            webViewState = engineSession2.getWebViewState();
        }
        else {
            webViewState = null;
        }
        if (webViewState != null) {
            final TabViewEngineSession engineSession3 = session.getEngineSession();
            Bundle webViewState2 = bundle;
            if (engineSession3 != null) {
                webViewState2 = engineSession3.getWebViewState();
            }
            create.restoreViewState(webViewState2);
        }
        else if (!TextUtils.isEmpty((CharSequence)s)) {
            create.loadUrl(s);
        }
    }
    
    private final void insertTab(final int index, final Session session) {
        Session session2;
        if (index >= 0 && index < this.sessions.size()) {
            session2 = this.sessions.get(index);
        }
        else {
            session2 = null;
        }
        if (session2 == null) {
            this.sessions.add(session);
            return;
        }
        this.sessions.add(index + 1, session);
        for (final Session session3 : this.sessions) {
            if (Intrinsics.areEqual(session2.getId(), session3.getParentId())) {
                session3.setParentId(session.getId());
            }
        }
        session.setParentId(session2.getId());
    }
    
    private final void link(final Session session, final TabViewEngineSession engineSession) {
        this.unlink(session);
        final TabViewEngineObserver engineObserver = new TabViewEngineObserver(session);
        engineSession.register((TabViewEngineSession.Observer)engineObserver);
        session.setEngineObserver(engineObserver);
        engineSession.setWindowClient((TabViewEngineSession.WindowClient)new WindowClient(session));
        engineSession.setEngineSessionClient((TabViewEngineSession.Client)new Client());
        session.setEngineSession(engineSession);
    }
    
    private final void removeTabInternal(final String s, final boolean b) {
        final Session tab = this.getTab(s);
        if (tab != null) {
            final int tabIndex = this.getTabIndex(s);
            this.sessions.remove(tab);
            this.notifier.notifyTabRemoved(tab);
            for (final Session session : this.sessions) {
                if (TextUtils.equals((CharSequence)session.getParentId(), (CharSequence)tab.getId())) {
                    session.setParentId(tab.getParentId());
                }
            }
            if (tab == this.focusRef.get()) {
                if (b) {
                    final int min = Math.min(tabIndex, this.sessions.size() - 1);
                    WeakReference<Session> focusRef;
                    if (min == -1) {
                        focusRef = new WeakReference<Session>(null);
                    }
                    else {
                        focusRef = new WeakReference<Session>(this.sessions.get(min));
                    }
                    this.focusRef = focusRef;
                }
                else {
                    this.updateFocusOnClosing(tab);
                }
            }
            this.notifyObservers((Function1<? super Observer, Unit>)new SessionManager$removeTabInternal.SessionManager$removeTabInternal$1(this));
        }
    }
    
    private final void unlink(final Session session) {
        final TabViewEngineSession.Observer engineObserver = session.getEngineObserver();
        if (engineObserver != null) {
            final TabViewEngineSession engineSession = session.getEngineSession();
            if (engineSession != null) {
                engineSession.unregister(engineObserver);
            }
        }
        final TabViewEngineSession engineSession2 = session.getEngineSession();
        if (engineSession2 != null) {
            engineSession2.destroy$feature_tabs_release();
        }
        session.setEngineSession(null);
        session.setEngineObserver(null);
    }
    
    private final void updateFocusOnClosing(final Session session) {
        if (TextUtils.isEmpty((CharSequence)session.getParentId())) {
            this.focusRef.clear();
            this.notifier.notifyTabFocused(null, Factor.FACTOR_NO_FOCUS);
        }
        else if (TextUtils.equals((CharSequence)session.getParentId(), (CharSequence)"_open_from_external_")) {
            this.focusRef.clear();
            this.notifier.notifyTabFocused(null, Factor.FACTOR_BACK_EXTERNAL);
        }
        else {
            final String parentId = session.getParentId();
            if (parentId == null) {
                Intrinsics.throwNpe();
            }
            this.focusRef = new WeakReference<Session>(this.getTab(parentId));
            this.notifier.notifyTabFocused(this.focusRef.get(), Factor.FACTOR_TAB_REMOVED);
        }
    }
    
    public final String addTab(String addTabInternal, final Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(addTabInternal, "url");
        Intrinsics.checkParameterIsNotNull(bundle, "arguments");
        if (TextUtils.isEmpty((CharSequence)addTabInternal)) {
            addTabInternal = null;
        }
        else {
            addTabInternal = this.addTabInternal(addTabInternal, TabUtil.getParentId(bundle), TabUtil.isFromExternal(bundle), TabUtil.toFocus(bundle), bundle);
        }
        return addTabInternal;
    }
    
    public final void closeTab(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        this.removeTabInternal(s, false);
    }
    
    public final void destroy() {
        for (final Session session : this.sessions) {
            Intrinsics.checkExpressionValueIsNotNull(session, "tab");
            this.destroySession(session);
        }
    }
    
    public final void dropTab(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        this.removeTabInternal(s, true);
    }
    
    public final TabViewEngineSession getEngineSession(final Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        return session.getEngineSession();
    }
    
    public final Session getFocusSession() {
        return this.focusRef.get();
    }
    
    public final TabViewEngineSession getOrCreateEngineSession(final Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        final TabViewEngineSession engineSession = this.getEngineSession(session);
        if (engineSession != null) {
            return engineSession;
        }
        final TabViewEngineSession tabViewEngineSession = new TabViewEngineSession(null, 1, null);
        this.link(session, tabViewEngineSession);
        return tabViewEngineSession;
    }
    
    public final List<Session> getTabs() {
        return new ArrayList<Session>(this.sessions);
    }
    
    public final int getTabsCount() {
        return this.sessions.size();
    }
    
    @Override
    public void notifyObservers(final Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.$$delegate_0.notifyObservers(function1);
    }
    
    public final void pause() {
        final Iterator<Session> iterator = this.sessions.iterator();
        while (iterator.hasNext()) {
            final TabViewEngineSession engineSession = iterator.next().getEngineSession();
            if (engineSession != null) {
                final TabView tabView = engineSession.getTabView();
                if (tabView == null) {
                    continue;
                }
                tabView.onPause();
            }
        }
    }
    
    @Override
    public void register(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.$$delegate_0.register(observer);
    }
    
    @Override
    public void register(final Observer observer, final LifecycleOwner lifecycleOwner, final boolean b) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        this.$$delegate_0.register(observer, lifecycleOwner, b);
    }
    
    public final void restore(final List<SessionWithState> list, final String s) {
        Intrinsics.checkParameterIsNotNull(list, "states");
        final Iterator<SessionWithState> iterator = list.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            final SessionWithState sessionWithState = iterator.next();
            if (sessionWithState.getSession().isValid()) {
                this.link(sessionWithState.getSession(), this.getOrCreateEngineSession(sessionWithState.getSession()));
                final TabViewEngineSession engineSession = sessionWithState.getSession().getEngineSession();
                if (engineSession != null) {
                    final TabViewEngineSession engineSession2 = sessionWithState.getEngineSession();
                    Bundle webViewState;
                    if (engineSession2 != null) {
                        webViewState = engineSession2.getWebViewState();
                    }
                    else {
                        webViewState = null;
                    }
                    engineSession.setWebViewState(webViewState);
                }
                this.sessions.add(index, sessionWithState.getSession());
                ++index;
            }
        }
        if (this.sessions.size() > 0 && this.sessions.size() == list.size()) {
            this.focusRef = new WeakReference<Session>(this.getTab(s));
        }
        this.notifyObservers((Function1<? super Observer, Unit>)new SessionManager$restore.SessionManager$restore$2(this));
    }
    
    public final void resume() {
        final Iterator<Session> iterator = this.sessions.iterator();
        while (iterator.hasNext()) {
            final TabViewEngineSession engineSession = iterator.next().getEngineSession();
            if (engineSession != null) {
                final TabView tabView = engineSession.getTabView();
                if (tabView == null) {
                    continue;
                }
                tabView.onResume();
            }
        }
    }
    
    public final void switchToTab(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        final Session tab = this.getTab(s);
        if (tab != null) {
            this.focusRef = new WeakReference<Session>(tab);
        }
        this.notifier.notifyTabFocused(tab, Factor.FACTOR_TAB_SWITCHED);
    }
    
    @Override
    public void unregister(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.$$delegate_0.unregister(observer);
    }
    
    @Override
    public void unregisterObservers() {
        this.$$delegate_0.unregisterObservers();
    }
    
    @Override
    public <R> List<Function1<R, Boolean>> wrapConsumers(final Function2<? super Observer, ? super R, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return this.$$delegate_0.wrapConsumers(function2);
    }
    
    public final class Client implements TabViewEngineSession.Client
    {
        @Override
        public boolean handleExternalUrl(final String s) {
            return Consumable.Companion.from(s).consumeBy(SessionManager.this.wrapConsumers((Function2<? super SessionManager.Observer, ? super Object, Boolean>)SessionManager$Client$handleExternalUrl$consumers.SessionManager$Client$handleExternalUrl$consumers$1.INSTANCE));
        }
        
        @Override
        public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback httpAuthCallback, final String s, final String s2) {
            Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
            SessionManager.this.notifyObservers((Function1<? super SessionManager.Observer, Unit>)new SessionManager$Client$onHttpAuthRequest.SessionManager$Client$onHttpAuthRequest$1(httpAuthCallback, s, s2));
        }
        
        @Override
        public boolean onShowFileChooser(final TabViewEngineSession tabViewEngineSession, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            return Consumable.Companion.from(webChromeClient$FileChooserParams).consumeBy(SessionManager.this.wrapConsumers((Function2<? super SessionManager.Observer, ? super Object, Boolean>)new SessionManager$Client$onShowFileChooser$consumers.SessionManager$Client$onShowFileChooser$consumers$1(tabViewEngineSession, (ValueCallback)valueCallback)));
        }
        
        @Override
        public void updateFailingUrl(final String s, final boolean b) {
            SessionManager.this.notifyObservers((Function1<? super SessionManager.Observer, Unit>)new SessionManager$Client$updateFailingUrl.SessionManager$Client$updateFailingUrl$1(s, b));
        }
    }
    
    public enum Factor
    {
        FACTOR_BACK_EXTERNAL(6), 
        FACTOR_NO_FOCUS(5), 
        FACTOR_TAB_ADDED(2), 
        FACTOR_TAB_REMOVED(3), 
        FACTOR_TAB_SWITCHED(4), 
        FACTOR_UNKNOWN(1);
        
        private final int value;
        
        protected Factor(final int value) {
            this.value = value;
        }
    }
    
    private static final class Notifier extends Handler
    {
        private final String ENUM_KEY;
        private final SessionManager observable;
        
        public Notifier(final SessionManager observable) {
            Intrinsics.checkParameterIsNotNull(observable, "observable");
            super(Looper.getMainLooper());
            this.observable = observable;
            this.ENUM_KEY = "_key_enum";
        }
        
        private final void focusTab(final Session session, final Factor factor) {
            if (session != null && this.observable.getOrCreateEngineSession(session).getTabView() == null) {
                this.observable.initializeEngineView(session);
            }
            this.observable.notifyObservers((Function1<? super Observer, Unit>)new SessionManager$Notifier$focusTab.SessionManager$Notifier$focusTab$2(session, factor));
        }
        
        public final void addedTab(final Message message) {
            Intrinsics.checkParameterIsNotNull(message, "msg");
            final Object obj = message.obj;
            if (obj != null) {
                this.observable.notifyObservers((Function1<? super Observer, Unit>)new SessionManager$Notifier$addedTab.SessionManager$Notifier$addedTab$1((Pair)obj));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Pair<org.mozilla.rocket.tabs.Session, android.os.Bundle?>");
        }
        
        public void handleMessage(final Message message) {
            Intrinsics.checkParameterIsNotNull(message, "msg");
            final int what = message.what;
            if (what == SessionManagerKt.getMSG_FOCUS_TAB()) {
                final Session session = (Session)message.obj;
                final Serializable serializable = message.getData().getSerializable(this.ENUM_KEY);
                if (serializable == null) {
                    throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Factor");
                }
                this.focusTab(session, (Factor)serializable);
            }
            else if (what == SessionManagerKt.getMSG_ADDED_TAB()) {
                this.addedTab(message);
            }
            else if (what == SessionManagerKt.getMSG_REMOVEDED_TAB()) {
                this.removedTab(message);
            }
        }
        
        public final void notifyTabAdded(final Session session, final Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            final Message obtainMessage = this.obtainMessage(SessionManagerKt.getMSG_ADDED_TAB());
            obtainMessage.obj = new Pair(session, bundle);
            this.sendMessage(obtainMessage);
        }
        
        public final void notifyTabFocused(final Session obj, final Factor factor) {
            Intrinsics.checkParameterIsNotNull(factor, "factor");
            final Message obtainMessage = this.obtainMessage(SessionManagerKt.getMSG_FOCUS_TAB());
            obtainMessage.obj = obj;
            Intrinsics.checkExpressionValueIsNotNull(obtainMessage, "msg");
            obtainMessage.getData().putSerializable(this.ENUM_KEY, (Serializable)factor);
            this.sendMessage(obtainMessage);
        }
        
        public final void notifyTabRemoved(final Session obj) {
            Intrinsics.checkParameterIsNotNull(obj, "session");
            final Message obtainMessage = this.obtainMessage(SessionManagerKt.getMSG_REMOVEDED_TAB());
            obtainMessage.obj = obj;
            this.sendMessage(obtainMessage);
        }
        
        public final void removedTab(final Message message) {
            Intrinsics.checkParameterIsNotNull(message, "msg");
            final Object obj = message.obj;
            if (obj != null) {
                this.observable.destroySession((Session)obj);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.Session");
        }
    }
    
    public interface Observer extends TabViewEngineSession.Client
    {
        void onFocusChanged(final Session p0, final Factor p1);
        
        void onSessionAdded(final Session p0, final Bundle p1);
        
        void onSessionCountChanged(final int p0);
        
        public static final class DefaultImpls
        {
            public static void onFocusChanged(final Observer observer, final Session session, final Factor factor) {
                Intrinsics.checkParameterIsNotNull(factor, "factor");
            }
            
            public static void onSessionAdded(final Observer observer, final Session session, final Bundle bundle) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }
            
            public static void onSessionCountChanged(final Observer observer, final int n) {
            }
        }
    }
    
    public static final class SessionWithState
    {
        private final TabViewEngineSession engineSession;
        private final Session session;
        
        public SessionWithState(final Session session, final TabViewEngineSession engineSession) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            this.session = session;
            this.engineSession = engineSession;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o instanceof SessionWithState) {
                    final SessionWithState sessionWithState = (SessionWithState)o;
                    if (Intrinsics.areEqual(this.session, sessionWithState.session) && Intrinsics.areEqual(this.engineSession, sessionWithState.engineSession)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public final TabViewEngineSession getEngineSession() {
            return this.engineSession;
        }
        
        public final Session getSession() {
            return this.session;
        }
        
        @Override
        public int hashCode() {
            final Session session = this.session;
            int hashCode = 0;
            int hashCode2;
            if (session != null) {
                hashCode2 = session.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            final TabViewEngineSession engineSession = this.engineSession;
            if (engineSession != null) {
                hashCode = engineSession.hashCode();
            }
            return hashCode2 * 31 + hashCode;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SessionWithState(session=");
            sb.append(this.session);
            sb.append(", engineSession=");
            sb.append(this.engineSession);
            sb.append(")");
            return sb.toString();
        }
    }
    
    public final class WindowClient implements TabViewEngineSession.WindowClient
    {
        private Session source;
        
        public WindowClient(final Session source) {
            Intrinsics.checkParameterIsNotNull(source, "source");
            this.source = source;
        }
        
        @Override
        public void onCloseWindow(final TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            if (this.source.getEngineSession() == tabViewEngineSession) {
                while (true) {
                    for (final Session next : SessionManager.access$getSessions$p(SessionManager.this)) {
                        if (Intrinsics.areEqual(next.getEngineSession(), tabViewEngineSession)) {
                            final Session session = next;
                            final Session session2 = session;
                            if (session2 != null) {
                                SessionManager.this.closeTab(session2.getId());
                            }
                            return;
                        }
                    }
                    final Session session = null;
                    continue;
                }
            }
        }
        
        @Override
        public boolean onCreateWindow(final boolean b, final boolean b2, final Message message) {
            if (message == null) {
                return false;
            }
            final Session access$getTab = SessionManager.this.getTab(SessionManager.this.addTabInternal(null, this.source.getId(), false, b2, null));
            if (access$getTab != null) {
                if (access$getTab.getEngineSession() != null) {
                    final TabViewEngineSession engineSession = access$getTab.getEngineSession();
                    if (engineSession == null) {
                        Intrinsics.throwNpe();
                    }
                    if (engineSession.getTabView() != null) {
                        final TabViewEngineSession engineSession2 = access$getTab.getEngineSession();
                        if (engineSession2 == null) {
                            Intrinsics.throwNpe();
                        }
                        final TabView tabView = engineSession2.getTabView();
                        if (tabView == null) {
                            Intrinsics.throwNpe();
                        }
                        tabView.bindOnNewWindowCreation(message);
                        return true;
                    }
                }
                throw new RuntimeException("webview is null, previous creation failed");
            }
            return false;
        }
    }
}
