package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabView.HitTarget;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$ChromeClient$onLongPress$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ HitTarget $hitTarget;

    TabViewEngineSession$ChromeClient$onLongPress$1(HitTarget hitTarget) {
        this.$hitTarget = hitTarget;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onLongPress(this.$hitTarget);
    }
}
