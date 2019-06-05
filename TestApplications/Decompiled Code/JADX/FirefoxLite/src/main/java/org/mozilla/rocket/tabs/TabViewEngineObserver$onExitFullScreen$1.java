package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: TabViewEngineObserver.kt */
final class TabViewEngineObserver$onExitFullScreen$1 extends Lambda implements Function1<Observer, Unit> {
    public static final TabViewEngineObserver$onExitFullScreen$1 INSTANCE = new TabViewEngineObserver$onExitFullScreen$1();

    TabViewEngineObserver$onExitFullScreen$1() {
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onExitFullScreen();
    }
}
