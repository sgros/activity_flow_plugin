package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$ViewClient$onPageStarted$3$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ TabView $it;

    TabViewEngineSession$ViewClient$onPageStarted$3$1(TabView tabView) {
        this.$it = tabView;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onNavigationStateChange(Boolean.valueOf(this.$it.canGoBack()), Boolean.valueOf(this.$it.canGoForward()));
    }
}
