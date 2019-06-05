// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.browser.session;

import java.util.List;
import kotlin.jvm.functions.Function2;
import android.arch.lifecycle.LifecycleOwner;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty;
import mozilla.components.support.base.observer.Observable;

public final class Session implements Observable<Observer>
{
    private final /* synthetic */ Observable $$delegate_0;
    private final String id;
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "url", "getUrl()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "title", "getTitle()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "progress", "getProgress()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "loading", "getLoading()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoBack", "getCanGoBack()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoForward", "getCanGoForward()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "searchTerms", "getSearchTerms()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "securityInfo", "getSecurityInfo()Lmozilla/components/browser/session/Session$SecurityInfo;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "customTabConfig", "getCustomTabConfig()Lmozilla/components/browser/session/tab/CustomTabConfig;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "download", "getDownload()Lmozilla/components/support/base/observer/Consumable;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "trackerBlockingEnabled", "getTrackerBlockingEnabled()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "trackersBlocked", "getTrackersBlocked()Ljava/util/List;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "findResults", "getFindResults()Ljava/util/List;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "hitResult", "getHitResult()Lmozilla/components/support/base/observer/Consumable;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "thumbnail", "getThumbnail()Landroid/graphics/Bitmap;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "desktopMode", "getDesktopMode()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "fullScreenMode", "getFullScreenMode()Z")) };
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        final Class<? extends Session> class1 = this.getClass();
        Class<?> class2;
        if (o != null) {
            class2 = o.getClass();
        }
        else {
            class2 = null;
        }
        if (Intrinsics.areEqual(class1, class2) ^ true) {
            return false;
        }
        if (o != null) {
            return !(Intrinsics.areEqual(this.id, ((Session)o).id) ^ true);
        }
        throw new TypeCastException("null cannot be cast to non-null type mozilla.components.browser.session.Session");
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
    
    @Override
    public void notifyObservers(final Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.$$delegate_0.notifyObservers(function1);
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
    
    public static final class FindResult
    {
        private final int activeMatchOrdinal;
        private final boolean isDoneCounting;
        private final int numberOfMatches;
        
        public FindResult(final int activeMatchOrdinal, final int numberOfMatches, final boolean isDoneCounting) {
            this.activeMatchOrdinal = activeMatchOrdinal;
            this.numberOfMatches = numberOfMatches;
            this.isDoneCounting = isDoneCounting;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o instanceof FindResult) {
                    final FindResult findResult = (FindResult)o;
                    if (this.activeMatchOrdinal == findResult.activeMatchOrdinal && this.numberOfMatches == findResult.numberOfMatches && this.isDoneCounting == findResult.isDoneCounting) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public final int getActiveMatchOrdinal() {
            return this.activeMatchOrdinal;
        }
        
        public final int getNumberOfMatches() {
            return this.numberOfMatches;
        }
        
        @Override
        public int hashCode() {
            final int activeMatchOrdinal = this.activeMatchOrdinal;
            final int numberOfMatches = this.numberOfMatches;
            int isDoneCounting;
            if ((isDoneCounting = (this.isDoneCounting ? 1 : 0)) != 0) {
                isDoneCounting = 1;
            }
            return (activeMatchOrdinal * 31 + numberOfMatches) * 31 + isDoneCounting;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("FindResult(activeMatchOrdinal=");
            sb.append(this.activeMatchOrdinal);
            sb.append(", numberOfMatches=");
            sb.append(this.numberOfMatches);
            sb.append(", isDoneCounting=");
            sb.append(this.isDoneCounting);
            sb.append(")");
            return sb.toString();
        }
    }
    
    public interface Observer
    {
    }
    
    public static final class SecurityInfo
    {
        private final String host;
        private final String issuer;
        private final boolean secure;
        
        public SecurityInfo() {
            this(false, null, null, 7, null);
        }
        
        public SecurityInfo(final boolean secure, final String host, final String issuer) {
            Intrinsics.checkParameterIsNotNull(host, "host");
            Intrinsics.checkParameterIsNotNull(issuer, "issuer");
            this.secure = secure;
            this.host = host;
            this.issuer = issuer;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o instanceof SecurityInfo) {
                    final SecurityInfo securityInfo = (SecurityInfo)o;
                    if (this.secure == securityInfo.secure && Intrinsics.areEqual(this.host, securityInfo.host) && Intrinsics.areEqual(this.issuer, securityInfo.issuer)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public final boolean getSecure() {
            return this.secure;
        }
        
        @Override
        public int hashCode() {
            int secure;
            if ((secure = (this.secure ? 1 : 0)) != 0) {
                secure = 1;
            }
            final String host = this.host;
            int hashCode = 0;
            int hashCode2;
            if (host != null) {
                hashCode2 = host.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            final String issuer = this.issuer;
            if (issuer != null) {
                hashCode = issuer.hashCode();
            }
            return (secure * 31 + hashCode2) * 31 + hashCode;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SecurityInfo(secure=");
            sb.append(this.secure);
            sb.append(", host=");
            sb.append(this.host);
            sb.append(", issuer=");
            sb.append(this.issuer);
            sb.append(")");
            return sb.toString();
        }
    }
}
