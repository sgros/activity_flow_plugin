package org.mozilla.rocket.tabs;

import android.webkit.GeolocationPermissions.Callback;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: TabViewEngineObserver.kt */
final class TabViewEngineObserver$onGeolocationPermissionsShowPrompt$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ Callback $callback;
    final /* synthetic */ String $origin;

    TabViewEngineObserver$onGeolocationPermissionsShowPrompt$1(String str, Callback callback) {
        this.$origin = str;
        this.$callback = callback;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onGeolocationPermissionsShowPrompt(this.$origin, this.$callback);
    }
}
