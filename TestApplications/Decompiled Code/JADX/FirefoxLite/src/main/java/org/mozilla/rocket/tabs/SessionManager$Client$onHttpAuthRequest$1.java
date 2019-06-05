package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Observer;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;

/* compiled from: SessionManager.kt */
final class SessionManager$Client$onHttpAuthRequest$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ HttpAuthCallback $callback;
    final /* synthetic */ String $host;
    final /* synthetic */ String $realm;

    SessionManager$Client$onHttpAuthRequest$1(HttpAuthCallback httpAuthCallback, String str, String str2) {
        this.$callback = httpAuthCallback;
        this.$host = str;
        this.$realm = str2;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onHttpAuthRequest(this.$callback, this.$host, this.$realm);
    }
}
