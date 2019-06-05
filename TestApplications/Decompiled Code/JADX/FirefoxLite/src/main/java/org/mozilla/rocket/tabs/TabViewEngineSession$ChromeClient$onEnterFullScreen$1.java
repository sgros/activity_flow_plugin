package org.mozilla.rocket.tabs;

import android.view.View;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$ChromeClient$onEnterFullScreen$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ FullscreenCallback $callback;
    final /* synthetic */ View $view;

    TabViewEngineSession$ChromeClient$onEnterFullScreen$1(FullscreenCallback fullscreenCallback, View view) {
        this.$callback = fullscreenCallback;
        this.$view = view;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onEnterFullScreen(this.$callback, this.$view);
    }
}
