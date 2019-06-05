package org.mozilla.rocket.tabs;

import android.arch.lifecycle.LifecycleOwner;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.support.base.observer.Consumable;
import mozilla.components.support.base.observer.Observable;
import mozilla.components.support.base.observer.ObserverRegistry;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.rocket.tabs.utils.TabUtil;

/* compiled from: SessionManager.kt */
public final class SessionManager implements Observable<Observer> {
    private final /* synthetic */ Observable $$delegate_0;
    private WeakReference<Session> focusRef;
    private final Notifier notifier;
    private final LinkedList<Session> sessions;
    private final TabViewProvider tabViewProvider;

    /* compiled from: SessionManager.kt */
    public enum Factor {
        FACTOR_UNKNOWN(1),
        FACTOR_TAB_ADDED(2),
        FACTOR_TAB_REMOVED(3),
        FACTOR_TAB_SWITCHED(4),
        FACTOR_NO_FOCUS(5),
        FACTOR_BACK_EXTERNAL(6);
        
        private final int value;

        protected Factor(int i) {
            this.value = i;
        }
    }

    /* compiled from: SessionManager.kt */
    private static final class Notifier extends Handler {
        private final String ENUM_KEY = "_key_enum";
        private final SessionManager observable;

        public Notifier(SessionManager sessionManager) {
            Intrinsics.checkParameterIsNotNull(sessionManager, "observable");
            super(Looper.getMainLooper());
            this.observable = sessionManager;
        }

        public void handleMessage(Message message) {
            Intrinsics.checkParameterIsNotNull(message, "msg");
            int i = message.what;
            if (i == SessionManagerKt.getMSG_FOCUS_TAB()) {
                Session session = (Session) message.obj;
                Serializable serializable = message.getData().getSerializable(this.ENUM_KEY);
                if (serializable != null) {
                    focusTab(session, (Factor) serializable);
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Factor");
            } else if (i == SessionManagerKt.getMSG_ADDED_TAB()) {
                addedTab(message);
            } else if (i == SessionManagerKt.getMSG_REMOVEDED_TAB()) {
                removedTab(message);
            }
        }

        public final void notifyTabAdded(Session session, Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Message obtainMessage = obtainMessage(SessionManagerKt.getMSG_ADDED_TAB());
            obtainMessage.obj = new Pair(session, bundle);
            sendMessage(obtainMessage);
        }

        public final void notifyTabRemoved(Session session) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Message obtainMessage = obtainMessage(SessionManagerKt.getMSG_REMOVEDED_TAB());
            obtainMessage.obj = session;
            sendMessage(obtainMessage);
        }

        public final void addedTab(Message message) {
            Intrinsics.checkParameterIsNotNull(message, "msg");
            Object obj = message.obj;
            if (obj != null) {
                this.observable.notifyObservers(new SessionManager$Notifier$addedTab$1((Pair) obj));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Pair<org.mozilla.rocket.tabs.Session, android.os.Bundle?>");
        }

        public final void removedTab(Message message) {
            Intrinsics.checkParameterIsNotNull(message, "msg");
            Object obj = message.obj;
            if (obj != null) {
                this.observable.destroySession((Session) obj);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.Session");
        }

        public final void notifyTabFocused(Session session, Factor factor) {
            Intrinsics.checkParameterIsNotNull(factor, "factor");
            Message obtainMessage = obtainMessage(SessionManagerKt.getMSG_FOCUS_TAB());
            obtainMessage.obj = session;
            Intrinsics.checkExpressionValueIsNotNull(obtainMessage, "msg");
            obtainMessage.getData().putSerializable(this.ENUM_KEY, factor);
            sendMessage(obtainMessage);
        }

        private final void focusTab(Session session, Factor factor) {
            if (session != null && this.observable.getOrCreateEngineSession(session).getTabView() == null) {
                this.observable.initializeEngineView(session);
            }
            this.observable.notifyObservers(new SessionManager$Notifier$focusTab$2(session, factor));
        }
    }

    /* compiled from: SessionManager.kt */
    public static final class SessionWithState {
        private final TabViewEngineSession engineSession;
        private final Session session;

        /* JADX WARNING: Missing block: B:6:0x001a, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r2.engineSession, r3.engineSession) != false) goto L_0x001f;
     */
        public boolean equals(java.lang.Object r3) {
            /*
            r2 = this;
            if (r2 == r3) goto L_0x001f;
        L_0x0002:
            r0 = r3 instanceof org.mozilla.rocket.tabs.SessionManager.SessionWithState;
            if (r0 == 0) goto L_0x001d;
        L_0x0006:
            r3 = (org.mozilla.rocket.tabs.SessionManager.SessionWithState) r3;
            r0 = r2.session;
            r1 = r3.session;
            r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
            if (r0 == 0) goto L_0x001d;
        L_0x0012:
            r0 = r2.engineSession;
            r3 = r3.engineSession;
            r3 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r3);
            if (r3 == 0) goto L_0x001d;
        L_0x001c:
            goto L_0x001f;
        L_0x001d:
            r3 = 0;
            return r3;
        L_0x001f:
            r3 = 1;
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.tabs.SessionManager$SessionWithState.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            Session session = this.session;
            int i = 0;
            int hashCode = (session != null ? session.hashCode() : 0) * 31;
            TabViewEngineSession tabViewEngineSession = this.engineSession;
            if (tabViewEngineSession != null) {
                i = tabViewEngineSession.hashCode();
            }
            return hashCode + i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SessionWithState(session=");
            stringBuilder.append(this.session);
            stringBuilder.append(", engineSession=");
            stringBuilder.append(this.engineSession);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public SessionWithState(Session session, TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            this.session = session;
            this.engineSession = tabViewEngineSession;
        }

        public final Session getSession() {
            return this.session;
        }

        public final TabViewEngineSession getEngineSession() {
            return this.engineSession;
        }
    }

    /* compiled from: SessionManager.kt */
    public final class Client implements org.mozilla.rocket.tabs.TabViewEngineSession.Client {
        public void updateFailingUrl(String str, boolean z) {
            SessionManager.this.notifyObservers(new SessionManager$Client$updateFailingUrl$1(str, z));
        }

        public boolean handleExternalUrl(String str) {
            return Consumable.Companion.from(str).consumeBy(SessionManager.this.wrapConsumers(SessionManager$Client$handleExternalUrl$consumers$1.INSTANCE));
        }

        public boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            return Consumable.Companion.from(fileChooserParams).consumeBy(SessionManager.this.wrapConsumers(new SessionManager$Client$onShowFileChooser$consumers$1(tabViewEngineSession, valueCallback)));
        }

        public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
            SessionManager.this.notifyObservers(new SessionManager$Client$onHttpAuthRequest$1(httpAuthCallback, str, str2));
        }
    }

    /* compiled from: SessionManager.kt */
    public interface Observer extends org.mozilla.rocket.tabs.TabViewEngineSession.Client {

        /* compiled from: SessionManager.kt */
        public static final class DefaultImpls {
            public static void onFocusChanged(Observer observer, Session session, Factor factor) {
                Intrinsics.checkParameterIsNotNull(factor, "factor");
            }

            public static void onSessionAdded(Observer observer, Session session, Bundle bundle) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }

            public static void onSessionCountChanged(Observer observer, int i) {
            }
        }

        void onFocusChanged(Session session, Factor factor);

        void onSessionAdded(Session session, Bundle bundle);

        void onSessionCountChanged(int i);
    }

    /* compiled from: SessionManager.kt */
    public final class WindowClient implements org.mozilla.rocket.tabs.TabViewEngineSession.WindowClient {
        private Session source;
        final /* synthetic */ SessionManager this$0;

        public WindowClient(SessionManager sessionManager, Session session) {
            Intrinsics.checkParameterIsNotNull(session, "source");
            this.this$0 = sessionManager;
            this.source = session;
        }

        public boolean onCreateWindow(boolean z, boolean z2, Message message) {
            if (message == null) {
                return false;
            }
            Session access$getTab = this.this$0.getTab(this.this$0.addTabInternal(null, this.source.getId(), false, z2, null));
            if (access$getTab == null) {
                return false;
            }
            if (access$getTab.getEngineSession() != null) {
                TabViewEngineSession engineSession = access$getTab.getEngineSession();
                if (engineSession == null) {
                    Intrinsics.throwNpe();
                }
                if (engineSession.getTabView() != null) {
                    engineSession = access$getTab.getEngineSession();
                    if (engineSession == null) {
                        Intrinsics.throwNpe();
                    }
                    TabView tabView = engineSession.getTabView();
                    if (tabView == null) {
                        Intrinsics.throwNpe();
                    }
                    tabView.bindOnNewWindowCreation(message);
                    return true;
                }
            }
            throw new RuntimeException("webview is null, previous creation failed");
        }

        public void onCloseWindow(TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            if (this.source.getEngineSession() == tabViewEngineSession) {
                for (Object next : this.this$0.sessions) {
                    if (Intrinsics.areEqual(((Session) next).getEngineSession(), tabViewEngineSession)) {
                        break;
                    }
                }
                Object next2 = null;
                Session session = (Session) next2;
                if (session != null) {
                    this.this$0.closeTab(session.getId());
                }
            }
        }
    }

    public SessionManager(TabViewProvider tabViewProvider) {
        this(tabViewProvider, null, 2, null);
    }

    public void notifyObservers(Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.$$delegate_0.notifyObservers(function1);
    }

    public void register(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.$$delegate_0.register(observer);
    }

    public void register(Observer observer, LifecycleOwner lifecycleOwner, boolean z) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        this.$$delegate_0.register(observer, lifecycleOwner, z);
    }

    public void unregister(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.$$delegate_0.unregister(observer);
    }

    public void unregisterObservers() {
        this.$$delegate_0.unregisterObservers();
    }

    public <R> List<Function1<R, Boolean>> wrapConsumers(Function2<? super Observer, ? super R, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return this.$$delegate_0.wrapConsumers(function2);
    }

    public SessionManager(TabViewProvider tabViewProvider, Observable<Observer> observable) {
        Intrinsics.checkParameterIsNotNull(tabViewProvider, "tabViewProvider");
        Intrinsics.checkParameterIsNotNull(observable, "delegate");
        this.$$delegate_0 = observable;
        this.tabViewProvider = tabViewProvider;
        this.sessions = new LinkedList();
        this.focusRef = new WeakReference(null);
        this.notifier = new Notifier(this);
    }

    public /* synthetic */ SessionManager(TabViewProvider tabViewProvider, Observable observable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 2) != 0) {
            observable = new ObserverRegistry();
        }
        this(tabViewProvider, observable);
    }

    public final int getTabsCount() {
        return this.sessions.size();
    }

    public final Session getFocusSession() {
        return (Session) this.focusRef.get();
    }

    public final List<Session> getTabs() {
        return new ArrayList(this.sessions);
    }

    public final void restore(List<SessionWithState> list, String str) {
        Intrinsics.checkParameterIsNotNull(list, "states");
        int i = 0;
        for (SessionWithState sessionWithState : list) {
            if (sessionWithState.getSession().isValid()) {
                link(sessionWithState.getSession(), getOrCreateEngineSession(sessionWithState.getSession()));
                TabViewEngineSession engineSession = sessionWithState.getSession().getEngineSession();
                if (engineSession != null) {
                    TabViewEngineSession engineSession2 = sessionWithState.getEngineSession();
                    engineSession.setWebViewState(engineSession2 != null ? engineSession2.getWebViewState() : null);
                }
                int i2 = i + 1;
                this.sessions.add(i, sessionWithState.getSession());
                i = i2;
            }
        }
        if (this.sessions.size() > 0 && this.sessions.size() == list.size()) {
            this.focusRef = new WeakReference(getTab(str));
        }
        notifyObservers(new SessionManager$restore$2(this));
    }

    public final String addTab(String str, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(bundle, "arguments");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return addTabInternal(str, TabUtil.getParentId(bundle), TabUtil.isFromExternal(bundle), TabUtil.toFocus(bundle), bundle);
    }

    public final void dropTab(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        removeTabInternal(str, true);
    }

    public final void closeTab(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        removeTabInternal(str, false);
    }

    private final void removeTabInternal(String str, boolean z) {
        Session tab = getTab(str);
        if (tab != null) {
            int tabIndex = getTabIndex(str);
            this.sessions.remove(tab);
            this.notifier.notifyTabRemoved(tab);
            Iterator it = this.sessions.iterator();
            while (it.hasNext()) {
                Session session = (Session) it.next();
                if (TextUtils.equals(session.getParentId(), tab.getId())) {
                    session.setParentId(tab.getParentId());
                }
            }
            if (tab == ((Session) this.focusRef.get())) {
                if (z) {
                    WeakReference weakReference;
                    tabIndex = Math.min(tabIndex, this.sessions.size() - 1);
                    if (tabIndex == -1) {
                        weakReference = new WeakReference(null);
                    } else {
                        weakReference = new WeakReference(this.sessions.get(tabIndex));
                    }
                    this.focusRef = weakReference;
                } else {
                    updateFocusOnClosing(tab);
                }
            }
            notifyObservers(new SessionManager$removeTabInternal$1(this));
        }
    }

    private final void updateFocusOnClosing(Session session) {
        if (TextUtils.isEmpty(session.getParentId())) {
            this.focusRef.clear();
            this.notifier.notifyTabFocused(null, Factor.FACTOR_NO_FOCUS);
        } else if (TextUtils.equals(session.getParentId(), "_open_from_external_")) {
            this.focusRef.clear();
            this.notifier.notifyTabFocused(null, Factor.FACTOR_BACK_EXTERNAL);
        } else {
            String parentId = session.getParentId();
            if (parentId == null) {
                Intrinsics.throwNpe();
            }
            this.focusRef = new WeakReference(getTab(parentId));
            this.notifier.notifyTabFocused((Session) this.focusRef.get(), Factor.FACTOR_TAB_REMOVED);
        }
    }

    public final void switchToTab(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        Session tab = getTab(str);
        if (tab != null) {
            this.focusRef = new WeakReference(tab);
        }
        this.notifier.notifyTabFocused(tab, Factor.FACTOR_TAB_SWITCHED);
    }

    public final void destroy() {
        Iterator it = this.sessions.iterator();
        while (it.hasNext()) {
            Session session = (Session) it.next();
            Intrinsics.checkExpressionValueIsNotNull(session, "tab");
            destroySession(session);
        }
    }

    public final void pause() {
        Iterator it = this.sessions.iterator();
        while (it.hasNext()) {
            TabViewEngineSession engineSession = ((Session) it.next()).getEngineSession();
            if (engineSession != null) {
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    tabView.onPause();
                }
            }
        }
    }

    public final void resume() {
        Iterator it = this.sessions.iterator();
        while (it.hasNext()) {
            TabViewEngineSession engineSession = ((Session) it.next()).getEngineSession();
            if (engineSession != null) {
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    tabView.onResume();
                }
            }
        }
    }

    public final TabViewEngineSession getEngineSession(Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        return session.getEngineSession();
    }

    public final TabViewEngineSession getOrCreateEngineSession(Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        TabViewEngineSession engineSession = getEngineSession(session);
        if (engineSession != null) {
            return engineSession;
        }
        engineSession = new TabViewEngineSession(null, 1, null);
        link(session, engineSession);
        return engineSession;
    }

    private final void initializeEngineView(Session session) {
        if (session.getEngineSession() == null) {
            getOrCreateEngineSession(session);
        }
        String initialUrl = TextUtils.isEmpty((CharSequence) session.getUrl()) ? session.getInitialUrl() : session.getUrl();
        TabView create = this.tabViewProvider.create();
        TabViewEngineSession engineSession = session.getEngineSession();
        if (engineSession != null) {
            engineSession.setTabView(create);
        }
        engineSession = session.getEngineSession();
        Bundle bundle = null;
        if ((engineSession != null ? engineSession.getWebViewState() : null) != null) {
            TabViewEngineSession engineSession2 = session.getEngineSession();
            if (engineSession2 != null) {
                bundle = engineSession2.getWebViewState();
            }
            create.restoreViewState(bundle);
        } else if (!TextUtils.isEmpty(initialUrl)) {
            create.loadUrl(initialUrl);
        }
    }

    private final void link(Session session, TabViewEngineSession tabViewEngineSession) {
        unlink(session);
        org.mozilla.rocket.tabs.TabViewEngineSession.Observer tabViewEngineObserver = new TabViewEngineObserver(session);
        tabViewEngineSession.register(tabViewEngineObserver);
        session.setEngineObserver(tabViewEngineObserver);
        tabViewEngineSession.setWindowClient(new WindowClient(this, session));
        tabViewEngineSession.setEngineSessionClient(new Client());
        session.setEngineSession(tabViewEngineSession);
    }

    private final void unlink(Session session) {
        org.mozilla.rocket.tabs.TabViewEngineSession.Observer engineObserver = session.getEngineObserver();
        if (engineObserver != null) {
            TabViewEngineSession engineSession = session.getEngineSession();
            if (engineSession != null) {
                engineSession.unregister(engineObserver);
            }
        }
        TabViewEngineSession engineSession2 = session.getEngineSession();
        if (engineSession2 != null) {
            engineSession2.destroy$feature_tabs_release();
        }
        session.setEngineSession((TabViewEngineSession) null);
        session.setEngineObserver((org.mozilla.rocket.tabs.TabViewEngineSession.Observer) null);
    }

    private final void destroySession(Session session) {
        unlink(session);
        session.unregisterObservers();
    }

    private final String addTabInternal(String str, String str2, boolean z, boolean z2, Bundle bundle) {
        int i;
        Session session = new Session(null, null, null, null, 15, null);
        session.setUrl(str);
        if (TextUtils.isEmpty(str2)) {
            i = -1;
        } else {
            if (str2 == null) {
                Intrinsics.throwNpe();
            }
            i = getTabIndex(str2);
        }
        if (z) {
            session.setParentId("_open_from_external_");
            this.sessions.add(session);
        } else {
            insertTab(i, session);
        }
        this.notifier.notifyTabAdded(session, bundle);
        WeakReference weakReference = (z2 || z) ? new WeakReference(session) : this.focusRef;
        this.focusRef = weakReference;
        getOrCreateEngineSession(session);
        initializeEngineView(session);
        if (z2 || z) {
            this.notifier.notifyTabFocused(session, Factor.FACTOR_TAB_ADDED);
        }
        notifyObservers(new SessionManager$addTabInternal$1(this));
        return session.getId();
    }

    private final Session getTab(String str) {
        int tabIndex = getTabIndex(str);
        if (tabIndex == -1) {
            return null;
        }
        return (Session) this.sessions.get(tabIndex);
    }

    private final int getTabIndex(String str) {
        if (str == null) {
            return -1;
        }
        int size = this.sessions.size();
        for (int i = 0; i < size; i++) {
            Object obj = this.sessions.get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "sessions[i]");
            if (Intrinsics.areEqual(((Session) obj).getId(), str)) {
                return i;
            }
        }
        return -1;
    }

    private final void insertTab(int i, Session session) {
        Session session2 = (i < 0 || i >= this.sessions.size()) ? null : (Session) this.sessions.get(i);
        if (session2 == null) {
            this.sessions.add(session);
            return;
        }
        this.sessions.add(i + 1, session);
        Iterator it = this.sessions.iterator();
        while (it.hasNext()) {
            Session session3 = (Session) it.next();
            if (Intrinsics.areEqual(session2.getId(), session3.getParentId())) {
                session3.setParentId(session.getId());
            }
        }
        session.setParentId(session2.getId());
    }
}
