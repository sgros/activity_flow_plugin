package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$ViewClient$onPageStarted$2$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ String $it;

    TabViewEngineSession$ViewClient$onPageStarted$2$1(String str) {
        this.$it = str;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onLocationChange(this.$it);
    }
}
