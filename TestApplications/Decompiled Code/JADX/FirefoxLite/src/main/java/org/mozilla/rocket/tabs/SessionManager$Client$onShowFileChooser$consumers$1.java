package org.mozilla.rocket.tabs;

import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Observer;

/* compiled from: SessionManager.kt */
final class SessionManager$Client$onShowFileChooser$consumers$1 extends Lambda implements Function2<Observer, FileChooserParams, Boolean> {
    final /* synthetic */ TabViewEngineSession $es;
    final /* synthetic */ ValueCallback $filePathCallback;

    SessionManager$Client$onShowFileChooser$consumers$1(TabViewEngineSession tabViewEngineSession, ValueCallback valueCallback) {
        this.$es = tabViewEngineSession;
        this.$filePathCallback = valueCallback;
        super(2);
    }

    public final boolean invoke(Observer observer, FileChooserParams fileChooserParams) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        return observer.onShowFileChooser(this.$es, this.$filePathCallback, fileChooserParams);
    }
}
