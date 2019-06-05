package org.mozilla.rocket.tabs;

import android.webkit.GeolocationPermissions.Callback;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
/* renamed from: org.mozilla.rocket.tabs.TabViewEngineSession$ChromeClient$onGeolocationPermissionsShowPrompt$1 */
final class C0768xfa022780 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ Callback $callback;
    final /* synthetic */ String $origin;

    C0768xfa022780(String str, Callback callback) {
        this.$origin = str;
        this.$callback = callback;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onGeolocationPermissionsShowPrompt(this.$origin, this.$callback);
    }
}
