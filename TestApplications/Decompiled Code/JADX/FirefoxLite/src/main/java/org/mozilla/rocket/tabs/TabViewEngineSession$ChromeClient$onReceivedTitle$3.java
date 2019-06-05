package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$ChromeClient$onReceivedTitle$3 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ TabView $view;

    TabViewEngineSession$ChromeClient$onReceivedTitle$3(TabView tabView) {
        this.$view = tabView;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onNavigationStateChange(Boolean.valueOf(this.$view.canGoBack()), Boolean.valueOf(this.$view.canGoForward()));
    }
}
