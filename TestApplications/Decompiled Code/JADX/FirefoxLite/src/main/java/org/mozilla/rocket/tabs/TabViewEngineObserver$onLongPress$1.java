package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session.Observer;
import org.mozilla.rocket.tabs.TabView.HitTarget;

/* compiled from: TabViewEngineObserver.kt */
final class TabViewEngineObserver$onLongPress$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ HitTarget $hitTarget;
    final /* synthetic */ TabViewEngineObserver this$0;

    TabViewEngineObserver$onLongPress$1(TabViewEngineObserver tabViewEngineObserver, HitTarget hitTarget) {
        this.this$0 = tabViewEngineObserver;
        this.$hitTarget = hitTarget;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onLongPress(this.this$0.getSession(), this.$hitTarget);
    }
}
