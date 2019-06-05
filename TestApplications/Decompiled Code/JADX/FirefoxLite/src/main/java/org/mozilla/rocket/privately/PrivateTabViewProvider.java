package org.mozilla.rocket.privately;

import android.app.Activity;
import android.view.View;
import android.webkit.WebSettings;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.web.WebViewProvider.WebSettingsHook;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewProvider;

/* compiled from: PrivateTabViewProvider.kt */
public final class PrivateTabViewProvider extends TabViewProvider {
    private final Activity host;

    /* compiled from: PrivateTabViewProvider.kt */
    public static final class WebViewSettingsHook implements WebSettingsHook {
        public static final WebViewSettingsHook INSTANCE = new WebViewSettingsHook();

        private WebViewSettingsHook() {
        }

        public void modify(WebSettings webSettings) {
            if (webSettings != null) {
                webSettings.setSupportMultipleWindows(false);
            }
        }
    }

    public PrivateTabViewProvider(Activity activity) {
        Intrinsics.checkParameterIsNotNull(activity, "host");
        this.host = activity;
    }

    public TabView create() {
        View create = WebViewProvider.create(this.host, null, WebViewSettingsHook.INSTANCE);
        if (create != null) {
            return (TabView) create;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.TabView");
    }
}
