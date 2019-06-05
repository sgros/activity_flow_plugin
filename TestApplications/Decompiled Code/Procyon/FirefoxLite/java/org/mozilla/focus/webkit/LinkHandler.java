// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.webkit.WebView$HitTestResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabChromeClient;
import android.view.View$OnLongClickListener;

class LinkHandler implements View$OnLongClickListener
{
    private TabChromeClient chromeClient;
    private final TabView tabView;
    private final WebView webView;
    
    public LinkHandler(final TabView tabView, final WebView webView) {
        this.chromeClient = null;
        this.tabView = tabView;
        this.webView = webView;
    }
    
    public boolean onLongClick(final View view) {
        if (this.chromeClient == null) {
            return false;
        }
        final WebView$HitTestResult hitTestResult = this.webView.getHitTestResult();
        final int type = hitTestResult.getType();
        if (type == 5) {
            this.chromeClient.onLongPress(new TabView.HitTarget(this.tabView, false, null, true, hitTestResult.getExtra()));
            return true;
        }
        switch (type) {
            default: {
                return false;
            }
            case 8: {
                final Message message = new Message();
                message.setTarget((Handler)new Handler() {
                    public void handleMessage(final Message message) {
                        final Bundle data = message.getData();
                        final String string = data.getString("url");
                        final String string2 = data.getString("src");
                        if (string != null && string2 != null) {
                            if (LinkHandler.this.chromeClient != null) {
                                LinkHandler.this.chromeClient.onLongPress(new TabView.HitTarget(LinkHandler.this.tabView, true, string, true, string2));
                            }
                            return;
                        }
                        throw new IllegalStateException("WebView did not supply url or src for image link");
                    }
                });
                this.webView.requestFocusNodeHref(message);
                return true;
            }
            case 7: {
                this.chromeClient.onLongPress(new TabView.HitTarget(this.tabView, true, hitTestResult.getExtra(), false, null));
                return true;
            }
        }
    }
    
    public void setChromeClient(final TabChromeClient chromeClient) {
        this.chromeClient = chromeClient;
    }
}
