package org.mozilla.focus.webkit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import org.mozilla.rocket.tabs.TabChromeClient;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabView.HitTarget;

class LinkHandler implements OnLongClickListener {
    private TabChromeClient chromeClient = null;
    private final TabView tabView;
    private final WebView webView;

    /* renamed from: org.mozilla.focus.webkit.LinkHandler$1 */
    class C05531 extends Handler {
        C05531() {
        }

        public void handleMessage(Message message) {
            Bundle data = message.getData();
            String string = data.getString("url");
            String string2 = data.getString("src");
            if (string == null || string2 == null) {
                throw new IllegalStateException("WebView did not supply url or src for image link");
            } else if (LinkHandler.this.chromeClient != null) {
                LinkHandler.this.chromeClient.onLongPress(new HitTarget(LinkHandler.this.tabView, true, string, true, string2));
            }
        }
    }

    public LinkHandler(TabView tabView, WebView webView) {
        this.tabView = tabView;
        this.webView = webView;
    }

    public void setChromeClient(TabChromeClient tabChromeClient) {
        this.chromeClient = tabChromeClient;
    }

    public boolean onLongClick(View view) {
        if (this.chromeClient == null) {
            return false;
        }
        HitTestResult hitTestResult = this.webView.getHitTestResult();
        int type = hitTestResult.getType();
        if (type != 5) {
            switch (type) {
                case 7:
                    this.chromeClient.onLongPress(new HitTarget(this.tabView, true, hitTestResult.getExtra(), false, null));
                    return true;
                case 8:
                    Message message = new Message();
                    message.setTarget(new C05531());
                    this.webView.requestFocusNodeHref(message);
                    return true;
                default:
                    return false;
            }
        }
        this.chromeClient.onLongPress(new HitTarget(this.tabView, false, null, true, hitTestResult.getExtra()));
        return true;
    }
}
