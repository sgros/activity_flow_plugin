package org.mozilla.rocket.tabs;

import android.graphics.Bitmap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: TabViewEngineObserver.kt */
final class TabViewEngineObserver$onReceivedIcon$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ Bitmap $icon;

    TabViewEngineObserver$onReceivedIcon$1(Bitmap bitmap) {
        this.$icon = bitmap;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onReceivedIcon(this.$icon);
    }
}
