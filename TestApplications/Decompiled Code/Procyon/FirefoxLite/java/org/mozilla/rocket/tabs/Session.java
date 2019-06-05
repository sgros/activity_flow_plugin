// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import android.webkit.GeolocationPermissions$Callback;
import android.view.View;
import kotlin.jvm.functions.Function2;
import mozilla.components.browser.session.Download;
import android.arch.lifecycle.LifecycleOwner;
import android.text.TextUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import mozilla.components.support.base.observer.ObserverRegistry;
import java.util.UUID;
import java.util.List;
import mozilla.components.support.base.observer.Consumable;
import kotlin.properties.Delegates;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KMutableProperty1;
import android.graphics.Bitmap;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import mozilla.components.support.base.observer.Observable;

public final class Session implements Observable<Observer>
{
    static final /* synthetic */ KProperty[] $$delegatedProperties;
    public static final Companion Companion;
    private final ReadWriteProperty canGoBack$delegate;
    private final ReadWriteProperty canGoForward$delegate;
    private final Observable<Observer> delegate;
    private final ReadWriteProperty download$delegate;
    private TabViewEngineSession.Observer engineObserver;
    private TabViewEngineSession engineSession;
    private Bitmap favicon;
    private final ReadWriteProperty findResults$delegate;
    private final String id;
    private String initialUrl;
    private final ReadWriteProperty loading$delegate;
    private String parentId;
    private final ReadWriteProperty progress$delegate;
    private final ReadWriteProperty securityInfo$delegate;
    private final ReadWriteProperty title$delegate;
    private final ReadWriteProperty url$delegate;
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "url", "getUrl()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "title", "getTitle()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "progress", "getProgress()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "loading", "getLoading()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoBack", "getCanGoBack()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoForward", "getCanGoForward()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "securityInfo", "getSecurityInfo()Lmozilla/components/browser/session/Session$SecurityInfo;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "download", "getDownload()Lmozilla/components/support/base/observer/Consumable;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "findResults", "getFindResults()Ljava/util/List;")) };
        Companion = new Companion(null);
    }
    
    public Session() {
        this(null, null, null, null, 15, null);
    }
    
    public Session(final String s, final String s2, final String s3) {
        this(s, s2, s3, null, 8, null);
    }
    
    public Session(String initialUrl, final String parentId, final String initialUrl2, final Observable<Observer> delegate) {
        Intrinsics.checkParameterIsNotNull(initialUrl, "id");
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this.id = initialUrl;
        this.parentId = parentId;
        this.initialUrl = initialUrl2;
        this.delegate = delegate;
        final Delegates instance = Delegates.INSTANCE;
        initialUrl = this.initialUrl;
        this.url$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$1((Object)initialUrl, (Object)initialUrl, this);
        final Delegates instance2 = Delegates.INSTANCE;
        this.title$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$2((Object)"", (Object)"", this);
        final Delegates instance3 = Delegates.INSTANCE;
        final Integer value = 0;
        this.progress$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$3((Object)value, (Object)value, this);
        final Delegates instance4 = Delegates.INSTANCE;
        final Boolean value2 = false;
        this.loading$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$4((Object)value2, (Object)value2, this);
        final Delegates instance5 = Delegates.INSTANCE;
        final Boolean value3 = false;
        this.canGoBack$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$5((Object)value3, (Object)value3, this);
        final Delegates instance6 = Delegates.INSTANCE;
        final Boolean value4 = false;
        this.canGoForward$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$6((Object)value4, (Object)value4, this);
        final Delegates instance7 = Delegates.INSTANCE;
        final mozilla.components.browser.session.Session.SecurityInfo securityInfo = new mozilla.components.browser.session.Session.SecurityInfo(false, null, null, 7, null);
        this.securityInfo$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$7((Object)securityInfo, (Object)securityInfo, this);
        final Delegates instance8 = Delegates.INSTANCE;
        final Consumable<Object> empty = Consumable.Companion.empty();
        this.download$delegate = (ReadWriteProperty)new Session$$special$$inlined$vetoable.Session$$special$$inlined$vetoable$1((Object)empty, (Object)empty, this);
        final Delegates instance9 = Delegates.INSTANCE;
        final List<Object> emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.findResults$delegate = (ReadWriteProperty)new Session$$special$$inlined$observable.Session$$special$$inlined$observable$8((Object)emptyList, (Object)emptyList, this);
    }
    
    private final void notifyObservers(final Object o, final Object o2, final Function1<? super Observer, Unit> function1) {
        if (Intrinsics.areEqual(o, o2) ^ true) {
            this.notifyObservers(function1);
        }
    }
    
    public final boolean getCanGoBack() {
        return this.canGoBack$delegate.getValue(this, Session.$$delegatedProperties[4]);
    }
    
    public final boolean getCanGoForward() {
        return this.canGoForward$delegate.getValue(this, Session.$$delegatedProperties[5]);
    }
    
    public final TabViewEngineSession.Observer getEngineObserver() {
        return this.engineObserver;
    }
    
    public final TabViewEngineSession getEngineSession() {
        return this.engineSession;
    }
    
    public final Bitmap getFavicon() {
        return this.favicon;
    }
    
    public final List<mozilla.components.browser.session.Session.FindResult> getFindResults() {
        return this.findResults$delegate.getValue(this, Session.$$delegatedProperties[8]);
    }
    
    public final String getId() {
        return this.id;
    }
    
    public final String getInitialUrl() {
        return this.initialUrl;
    }
    
    public final String getParentId() {
        return this.parentId;
    }
    
    public final mozilla.components.browser.session.Session.SecurityInfo getSecurityInfo() {
        return this.securityInfo$delegate.getValue(this, Session.$$delegatedProperties[6]);
    }
    
    public final String getTitle() {
        return this.title$delegate.getValue(this, Session.$$delegatedProperties[1]);
    }
    
    public final String getUrl() {
        return this.url$delegate.getValue(this, Session.$$delegatedProperties[0]);
    }
    
    public final boolean hasParentTab() {
        return !this.isFromExternal() && !TextUtils.isEmpty((CharSequence)this.parentId);
    }
    
    public final boolean isFromExternal() {
        return Intrinsics.areEqual("_open_from_external_", this.parentId);
    }
    
    public final boolean isValid() {
        final boolean blank = StringsKt__StringsJVMKt.isBlank(this.id);
        boolean b = true;
        if (blank ^ true) {
            final String url = this.getUrl();
            if (url != null && (StringsKt__StringsJVMKt.isBlank(url) ^ true)) {
                return b;
            }
        }
        b = false;
        return b;
    }
    
    @Override
    public void notifyObservers(final Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.delegate.notifyObservers(function1);
    }
    
    @Override
    public void register(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.register(observer);
    }
    
    @Override
    public void register(final Observer observer, final LifecycleOwner lifecycleOwner, final boolean b) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        this.delegate.register(observer, lifecycleOwner, b);
    }
    
    public final void setCanGoBack(final boolean b) {
        this.canGoBack$delegate.setValue(this, Session.$$delegatedProperties[4], b);
    }
    
    public final void setCanGoForward(final boolean b) {
        this.canGoForward$delegate.setValue(this, Session.$$delegatedProperties[5], b);
    }
    
    public final void setDownload(final Consumable<Download> consumable) {
        Intrinsics.checkParameterIsNotNull(consumable, "<set-?>");
        this.download$delegate.setValue(this, Session.$$delegatedProperties[7], consumable);
    }
    
    public final void setEngineObserver(final TabViewEngineSession.Observer engineObserver) {
        this.engineObserver = engineObserver;
    }
    
    public final void setEngineSession(final TabViewEngineSession engineSession) {
        this.engineSession = engineSession;
    }
    
    public final void setFavicon(final Bitmap favicon) {
        this.favicon = favicon;
    }
    
    public final void setFindResults(final List<mozilla.components.browser.session.Session.FindResult> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.findResults$delegate.setValue(this, Session.$$delegatedProperties[8], list);
    }
    
    public final void setLoading(final boolean b) {
        this.loading$delegate.setValue(this, Session.$$delegatedProperties[3], b);
    }
    
    public final void setParentId(final String parentId) {
        this.parentId = parentId;
    }
    
    public final void setProgress(final int i) {
        this.progress$delegate.setValue(this, Session.$$delegatedProperties[2], i);
    }
    
    public final void setSecurityInfo(final mozilla.components.browser.session.Session.SecurityInfo securityInfo) {
        Intrinsics.checkParameterIsNotNull(securityInfo, "<set-?>");
        this.securityInfo$delegate.setValue(this, Session.$$delegatedProperties[6], securityInfo);
    }
    
    public final void setTitle(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "<set-?>");
        this.title$delegate.setValue(this, Session.$$delegatedProperties[1], s);
    }
    
    public final void setUrl(final String s) {
        this.url$delegate.setValue(this, Session.$$delegatedProperties[0], s);
    }
    
    @Override
    public void unregister(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.unregister(observer);
    }
    
    @Override
    public void unregisterObservers() {
        this.delegate.unregisterObservers();
    }
    
    @Override
    public <R> List<Function1<R, Boolean>> wrapConsumers(final Function2<? super Observer, ? super R, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return this.delegate.wrapConsumers(function2);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
    
    public interface Observer
    {
        boolean onDownload(final Session p0, final Download p1);
        
        void onEnterFullScreen(final TabView.FullscreenCallback p0, final View p1);
        
        void onExitFullScreen();
        
        void onFindResult(final Session p0, final mozilla.components.browser.session.Session.FindResult p1);
        
        void onGeolocationPermissionsShowPrompt(final String p0, final GeolocationPermissions$Callback p1);
        
        void onLoadingStateChanged(final Session p0, final boolean p1);
        
        void onLongPress(final Session p0, final TabView.HitTarget p1);
        
        void onNavigationStateChanged(final Session p0, final boolean p1, final boolean p2);
        
        void onProgress(final Session p0, final int p1);
        
        void onReceivedIcon(final Bitmap p0);
        
        void onSecurityChanged(final Session p0, final boolean p1);
        
        void onTitleChanged(final Session p0, final String p1);
        
        void onUrlChanged(final Session p0, final String p1);
        
        public static final class DefaultImpls
        {
            public static boolean onDownload(final Observer observer, final Session session, final Download download) {
                Intrinsics.checkParameterIsNotNull(session, "session");
                Intrinsics.checkParameterIsNotNull(download, "download");
                return false;
            }
            
            public static void onEnterFullScreen(final Observer observer, final TabView.FullscreenCallback fullscreenCallback, final View view) {
                Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            }
            
            public static void onExitFullScreen(final Observer observer) {
            }
            
            public static void onFindResult(final Observer observer, final Session session, final mozilla.components.browser.session.Session.FindResult findResult) {
                Intrinsics.checkParameterIsNotNull(session, "session");
                Intrinsics.checkParameterIsNotNull(findResult, "result");
            }
            
            public static void onGeolocationPermissionsShowPrompt(final Observer observer, final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
                Intrinsics.checkParameterIsNotNull(s, "origin");
            }
            
            public static void onLoadingStateChanged(final Observer observer, final Session session, final boolean b) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }
            
            public static void onLongPress(final Observer observer, final Session session, final TabView.HitTarget hitTarget) {
                Intrinsics.checkParameterIsNotNull(session, "session");
                Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            }
            
            public static void onNavigationStateChanged(final Observer observer, final Session session, final boolean b, final boolean b2) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }
            
            public static void onProgress(final Observer observer, final Session session, final int n) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }
            
            public static void onReceivedIcon(final Observer observer, final Bitmap bitmap) {
            }
            
            public static void onSecurityChanged(final Observer observer, final Session session, final boolean b) {
                Intrinsics.checkParameterIsNotNull(session, "session");
            }
        }
    }
}
