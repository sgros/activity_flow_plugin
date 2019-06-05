package org.mozilla.rocket.tabs;

import android.arch.lifecycle.LifecycleOwner;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import java.util.List;
import java.util.UUID;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import mozilla.components.browser.session.Download;
import mozilla.components.browser.session.Session.FindResult;
import mozilla.components.browser.session.Session.SecurityInfo;
import mozilla.components.support.base.observer.Consumable;
import mozilla.components.support.base.observer.Observable;
import mozilla.components.support.base.observer.ObserverRegistry;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;

/* compiled from: Session.kt */
public final class Session implements Observable<Observer> {
    static final /* synthetic */ KProperty[] $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "url", "getUrl()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "title", "getTitle()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "progress", "getProgress()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "loading", "getLoading()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoBack", "getCanGoBack()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoForward", "getCanGoForward()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "securityInfo", "getSecurityInfo()Lmozilla/components/browser/session/Session$SecurityInfo;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "download", "getDownload()Lmozilla/components/support/base/observer/Consumable;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "findResults", "getFindResults()Ljava/util/List;"))};
    public static final Companion Companion = new Companion();
    private final ReadWriteProperty canGoBack$delegate;
    private final ReadWriteProperty canGoForward$delegate;
    private final Observable<Observer> delegate;
    private final ReadWriteProperty download$delegate;
    private org.mozilla.rocket.tabs.TabViewEngineSession.Observer engineObserver;
    private TabViewEngineSession engineSession;
    private Bitmap favicon;
    private final ReadWriteProperty findResults$delegate;
    /* renamed from: id */
    private final String f70id;
    private String initialUrl;
    private final ReadWriteProperty loading$delegate;
    private String parentId;
    private final ReadWriteProperty progress$delegate;
    private final ReadWriteProperty securityInfo$delegate;
    private final ReadWriteProperty title$delegate;
    private final ReadWriteProperty url$delegate;

    /* compiled from: Session.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: Session.kt */
    public interface Observer {

        /* compiled from: Session.kt */
        public static final class DefaultImpls {
            public static boolean onDownload(Observer observer, Session session, Download download) {
                Intrinsics.checkParameterIsNotNull(session, "session");
                Intrinsics.checkParameterIsNotNull(download, "download");
                return false;
            }

            public static void onEnterFullScreen(Observer observer, FullscreenCallback fullscreenCallback, View view) {
                Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            }

            public static void onExitFullScreen(Observer observer) {
            }

            public static void onFindResult(Observer observer, Session session, FindResult findResult) {
                Intrinsics.checkParameterIsNotNull(session, "session");
                Intrinsics.checkParameterIsNotNull(findResult, "result");
            }

            public static void onGeolocationPermissionsShowPrompt(Observer observer, String str, Callback callback) {
                Intrinsics.checkParameterIsNotNull(str, "origin");
            }

            public static void onLoadingStateChanged(Observer observer, Session session, boolean z) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }

            public static void onLongPress(Observer observer, Session session, HitTarget hitTarget) {
                Intrinsics.checkParameterIsNotNull(session, "session");
                Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            }

            public static void onNavigationStateChanged(Observer observer, Session session, boolean z, boolean z2) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }

            public static void onProgress(Observer observer, Session session, int i) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }

            public static void onReceivedIcon(Observer observer, Bitmap bitmap) {
            }

            public static void onSecurityChanged(Observer observer, Session session, boolean z) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }
        }

        boolean onDownload(Session session, Download download);

        void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view);

        void onExitFullScreen();

        void onFindResult(Session session, FindResult findResult);

        void onGeolocationPermissionsShowPrompt(String str, Callback callback);

        void onLoadingStateChanged(Session session, boolean z);

        void onLongPress(Session session, HitTarget hitTarget);

        void onNavigationStateChanged(Session session, boolean z, boolean z2);

        void onProgress(Session session, int i);

        void onReceivedIcon(Bitmap bitmap);

        void onSecurityChanged(Session session, boolean z);

        void onTitleChanged(Session session, String str);

        void onUrlChanged(Session session, String str);
    }

    public Session() {
        this(null, null, null, null, 15, null);
    }

    public Session(String str, String str2, String str3) {
        this(str, str2, str3, null, 8, null);
    }

    public final boolean getCanGoBack() {
        return ((Boolean) this.canGoBack$delegate.getValue(this, $$delegatedProperties[4])).booleanValue();
    }

    public final boolean getCanGoForward() {
        return ((Boolean) this.canGoForward$delegate.getValue(this, $$delegatedProperties[5])).booleanValue();
    }

    public final List<FindResult> getFindResults() {
        return (List) this.findResults$delegate.getValue(this, $$delegatedProperties[8]);
    }

    public final SecurityInfo getSecurityInfo() {
        return (SecurityInfo) this.securityInfo$delegate.getValue(this, $$delegatedProperties[6]);
    }

    public final String getTitle() {
        return (String) this.title$delegate.getValue(this, $$delegatedProperties[1]);
    }

    public final String getUrl() {
        return (String) this.url$delegate.getValue(this, $$delegatedProperties[0]);
    }

    public void notifyObservers(Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.delegate.notifyObservers(function1);
    }

    public void register(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.register(observer);
    }

    public void register(Observer observer, LifecycleOwner lifecycleOwner, boolean z) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        this.delegate.register(observer, lifecycleOwner, z);
    }

    public final void setCanGoBack(boolean z) {
        this.canGoBack$delegate.setValue(this, $$delegatedProperties[4], Boolean.valueOf(z));
    }

    public final void setCanGoForward(boolean z) {
        this.canGoForward$delegate.setValue(this, $$delegatedProperties[5], Boolean.valueOf(z));
    }

    public final void setDownload(Consumable<Download> consumable) {
        Intrinsics.checkParameterIsNotNull(consumable, "<set-?>");
        this.download$delegate.setValue(this, $$delegatedProperties[7], consumable);
    }

    public final void setFindResults(List<FindResult> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.findResults$delegate.setValue(this, $$delegatedProperties[8], list);
    }

    public final void setLoading(boolean z) {
        this.loading$delegate.setValue(this, $$delegatedProperties[3], Boolean.valueOf(z));
    }

    public final void setProgress(int i) {
        this.progress$delegate.setValue(this, $$delegatedProperties[2], Integer.valueOf(i));
    }

    public final void setSecurityInfo(SecurityInfo securityInfo) {
        Intrinsics.checkParameterIsNotNull(securityInfo, "<set-?>");
        this.securityInfo$delegate.setValue(this, $$delegatedProperties[6], securityInfo);
    }

    public final void setTitle(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.title$delegate.setValue(this, $$delegatedProperties[1], str);
    }

    public final void setUrl(String str) {
        this.url$delegate.setValue(this, $$delegatedProperties[0], str);
    }

    public void unregister(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.unregister(observer);
    }

    public void unregisterObservers() {
        this.delegate.unregisterObservers();
    }

    public <R> List<Function1<R, Boolean>> wrapConsumers(Function2<? super Observer, ? super R, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return this.delegate.wrapConsumers(function2);
    }

    public Session(String str, String str2, String str3, Observable<Observer> observable) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        Intrinsics.checkParameterIsNotNull(observable, "delegate");
        this.f70id = str;
        this.parentId = str2;
        this.initialUrl = str3;
        this.delegate = observable;
        Delegates delegates = Delegates.INSTANCE;
        str = this.initialUrl;
        this.url$delegate = new Session$$special$$inlined$observable$1(str, str, this);
        delegates = Delegates.INSTANCE;
        str = "";
        this.title$delegate = new Session$$special$$inlined$observable$2(str, str, this);
        delegates = Delegates.INSTANCE;
        Integer valueOf = Integer.valueOf(0);
        this.progress$delegate = new Session$$special$$inlined$observable$3(valueOf, valueOf, this);
        Delegates delegates2 = Delegates.INSTANCE;
        Boolean valueOf2 = Boolean.valueOf(false);
        this.loading$delegate = new Session$$special$$inlined$observable$4(valueOf2, valueOf2, this);
        delegates2 = Delegates.INSTANCE;
        valueOf2 = Boolean.valueOf(false);
        this.canGoBack$delegate = new Session$$special$$inlined$observable$5(valueOf2, valueOf2, this);
        delegates2 = Delegates.INSTANCE;
        Boolean valueOf3 = Boolean.valueOf(false);
        this.canGoForward$delegate = new Session$$special$$inlined$observable$6(valueOf3, valueOf3, this);
        delegates = Delegates.INSTANCE;
        SecurityInfo securityInfo = new SecurityInfo(false, null, null, 7, null);
        this.securityInfo$delegate = new Session$$special$$inlined$observable$7(securityInfo, securityInfo, this);
        delegates = Delegates.INSTANCE;
        Consumable empty = Consumable.Companion.empty();
        this.download$delegate = new Session$$special$$inlined$vetoable$1(empty, empty, this);
        delegates = Delegates.INSTANCE;
        List emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.findResults$delegate = new Session$$special$$inlined$observable$8(emptyList, emptyList, this);
    }

    public /* synthetic */ Session(String str, String str2, String str3, Observable observable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = UUID.randomUUID().toString();
            Intrinsics.checkExpressionValueIsNotNull(str, "UUID.randomUUID().toString()");
        }
        if ((i & 2) != 0) {
            str2 = "";
        }
        if ((i & 4) != 0) {
            str3 = "";
        }
        if ((i & 8) != 0) {
            observable = new ObserverRegistry();
        }
        this(str, str2, str3, observable);
    }

    public final String getId() {
        return this.f70id;
    }

    public final String getParentId() {
        return this.parentId;
    }

    public final void setParentId(String str) {
        this.parentId = str;
    }

    public final String getInitialUrl() {
        return this.initialUrl;
    }

    public final TabViewEngineSession getEngineSession() {
        return this.engineSession;
    }

    public final void setEngineSession(TabViewEngineSession tabViewEngineSession) {
        this.engineSession = tabViewEngineSession;
    }

    public final org.mozilla.rocket.tabs.TabViewEngineSession.Observer getEngineObserver() {
        return this.engineObserver;
    }

    public final void setEngineObserver(org.mozilla.rocket.tabs.TabViewEngineSession.Observer observer) {
        this.engineObserver = observer;
    }

    public final Bitmap getFavicon() {
        return this.favicon;
    }

    public final void setFavicon(Bitmap bitmap) {
        this.favicon = bitmap;
    }

    public final boolean isFromExternal() {
        return Intrinsics.areEqual("_open_from_external_", this.parentId);
    }

    public final boolean isValid() {
        if ((StringsKt__StringsJVMKt.isBlank(this.f70id) ^ 1) != 0) {
            String url = getUrl();
            if ((url != null ? StringsKt__StringsJVMKt.isBlank(url) ^ 1 : 0) != 0) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasParentTab() {
        return (isFromExternal() || TextUtils.isEmpty(this.parentId)) ? false : true;
    }

    private final void notifyObservers(Object obj, Object obj2, Function1<? super Observer, Unit> function1) {
        if ((Intrinsics.areEqual(obj, obj2) ^ 1) != 0) {
            notifyObservers(function1);
        }
    }
}
