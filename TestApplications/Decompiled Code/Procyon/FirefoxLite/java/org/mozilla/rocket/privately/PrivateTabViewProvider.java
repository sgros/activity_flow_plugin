// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.privately;

import android.webkit.WebSettings;
import android.view.View;
import kotlin.TypeCastException;
import android.util.AttributeSet;
import org.mozilla.focus.web.WebViewProvider;
import android.content.Context;
import org.mozilla.rocket.tabs.TabView;
import kotlin.jvm.internal.Intrinsics;
import android.app.Activity;
import org.mozilla.rocket.tabs.TabViewProvider;

public final class PrivateTabViewProvider extends TabViewProvider
{
    private final Activity host;
    
    public PrivateTabViewProvider(final Activity host) {
        Intrinsics.checkParameterIsNotNull(host, "host");
        this.host = host;
    }
    
    @Override
    public TabView create() {
        final View create = WebViewProvider.create((Context)this.host, null, (WebViewProvider.WebSettingsHook)WebViewSettingsHook.INSTANCE);
        if (create != null) {
            return (TabView)create;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.TabView");
    }
    
    public static final class WebViewSettingsHook implements WebSettingsHook
    {
        public static final WebViewSettingsHook INSTANCE;
        
        static {
            INSTANCE = new WebViewSettingsHook();
        }
        
        private WebViewSettingsHook() {
        }
        
        @Override
        public void modify(final WebSettings webSettings) {
            if (webSettings == null) {
                return;
            }
            webSettings.setSupportMultipleWindows(false);
        }
    }
}
