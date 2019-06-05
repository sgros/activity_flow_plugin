package mozilla.components.browser.session;

import android.arch.lifecycle.LifecycleOwner;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import mozilla.components.support.base.observer.Observable;

/* compiled from: Session.kt */
public final class Session implements Observable<Observer> {
    static final /* synthetic */ KProperty[] $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "url", "getUrl()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "title", "getTitle()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "progress", "getProgress()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "loading", "getLoading()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoBack", "getCanGoBack()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoForward", "getCanGoForward()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "searchTerms", "getSearchTerms()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "securityInfo", "getSecurityInfo()Lmozilla/components/browser/session/Session$SecurityInfo;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "customTabConfig", "getCustomTabConfig()Lmozilla/components/browser/session/tab/CustomTabConfig;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "download", "getDownload()Lmozilla/components/support/base/observer/Consumable;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "trackerBlockingEnabled", "getTrackerBlockingEnabled()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "trackersBlocked", "getTrackersBlocked()Ljava/util/List;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "findResults", "getFindResults()Ljava/util/List;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "hitResult", "getHitResult()Lmozilla/components/support/base/observer/Consumable;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "thumbnail", "getThumbnail()Landroid/graphics/Bitmap;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "desktopMode", "getDesktopMode()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "fullScreenMode", "getFullScreenMode()Z"))};
    private final /* synthetic */ Observable $$delegate_0;
    /* renamed from: id */
    private final String f62id;

    /* compiled from: Session.kt */
    public static final class FindResult {
        private final int activeMatchOrdinal;
        private final boolean isDoneCounting;
        private final int numberOfMatches;

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof FindResult) {
                    FindResult findResult = (FindResult) obj;
                    if ((this.activeMatchOrdinal == findResult.activeMatchOrdinal ? 1 : null) != null) {
                        if ((this.numberOfMatches == findResult.numberOfMatches ? 1 : null) != null) {
                            if ((this.isDoneCounting == findResult.isDoneCounting ? 1 : null) != null) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = ((this.activeMatchOrdinal * 31) + this.numberOfMatches) * 31;
            int i2 = this.isDoneCounting;
            if (i2 != 0) {
                i2 = 1;
            }
            return i + i2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FindResult(activeMatchOrdinal=");
            stringBuilder.append(this.activeMatchOrdinal);
            stringBuilder.append(", numberOfMatches=");
            stringBuilder.append(this.numberOfMatches);
            stringBuilder.append(", isDoneCounting=");
            stringBuilder.append(this.isDoneCounting);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public FindResult(int i, int i2, boolean z) {
            this.activeMatchOrdinal = i;
            this.numberOfMatches = i2;
            this.isDoneCounting = z;
        }

        public final int getActiveMatchOrdinal() {
            return this.activeMatchOrdinal;
        }

        public final int getNumberOfMatches() {
            return this.numberOfMatches;
        }
    }

    /* compiled from: Session.kt */
    public interface Observer {
    }

    /* compiled from: Session.kt */
    public static final class SecurityInfo {
        private final String host;
        private final String issuer;
        private final boolean secure;

        public SecurityInfo() {
            this(false, null, null, 7, null);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof SecurityInfo) {
                    SecurityInfo securityInfo = (SecurityInfo) obj;
                    if (!((this.secure == securityInfo.secure ? 1 : null) != null && Intrinsics.areEqual(this.host, securityInfo.host) && Intrinsics.areEqual(this.issuer, securityInfo.issuer))) {
                        return false;
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = this.secure;
            if (i != 0) {
                i = 1;
            }
            i *= 31;
            String str = this.host;
            int i2 = 0;
            i = (i + (str != null ? str.hashCode() : 0)) * 31;
            str = this.issuer;
            if (str != null) {
                i2 = str.hashCode();
            }
            return i + i2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecurityInfo(secure=");
            stringBuilder.append(this.secure);
            stringBuilder.append(", host=");
            stringBuilder.append(this.host);
            stringBuilder.append(", issuer=");
            stringBuilder.append(this.issuer);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public SecurityInfo(boolean z, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "host");
            Intrinsics.checkParameterIsNotNull(str2, "issuer");
            this.secure = z;
            this.host = str;
            this.issuer = str2;
        }

        public /* synthetic */ SecurityInfo(boolean z, String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            if ((i & 1) != 0) {
                z = false;
            }
            if ((i & 2) != 0) {
                str = "";
            }
            if ((i & 4) != 0) {
                str2 = "";
            }
            this(z, str, str2);
        }

        public final boolean getSecure() {
            return this.secure;
        }
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

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((Intrinsics.areEqual(getClass(), obj != null ? obj.getClass() : null) ^ 1) != 0) {
            return false;
        }
        if (obj != null) {
            if ((Intrinsics.areEqual(this.f62id, ((Session) obj).f62id) ^ 1) != 0) {
                return false;
            }
            return true;
        }
        throw new TypeCastException("null cannot be cast to non-null type mozilla.components.browser.session.Session");
    }

    public int hashCode() {
        return this.f62id.hashCode();
    }
}
