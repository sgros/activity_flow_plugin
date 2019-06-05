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

class LinkHandler implements OnLongClickListener {
   private TabChromeClient chromeClient = null;
   private final TabView tabView;
   private final WebView webView;

   public LinkHandler(TabView var1, WebView var2) {
      this.tabView = var1;
      this.webView = var2;
   }

   public boolean onLongClick(View var1) {
      if (this.chromeClient == null) {
         return false;
      } else {
         HitTestResult var3 = this.webView.getHitTestResult();
         int var2 = var3.getType();
         String var4;
         if (var2 != 5) {
            switch(var2) {
            case 7:
               var4 = var3.getExtra();
               this.chromeClient.onLongPress(new TabView.HitTarget(this.tabView, true, var4, false, (String)null));
               return true;
            case 8:
               Message var5 = new Message();
               var5.setTarget(new Handler() {
                  public void handleMessage(Message var1) {
                     Bundle var2 = var1.getData();
                     String var3 = var2.getString("url");
                     String var4 = var2.getString("src");
                     if (var3 != null && var4 != null) {
                        if (LinkHandler.this.chromeClient != null) {
                           LinkHandler.this.chromeClient.onLongPress(new TabView.HitTarget(LinkHandler.this.tabView, true, var3, true, var4));
                        }

                     } else {
                        throw new IllegalStateException("WebView did not supply url or src for image link");
                     }
                  }
               });
               this.webView.requestFocusNodeHref(var5);
               return true;
            default:
               return false;
            }
         } else {
            var4 = var3.getExtra();
            this.chromeClient.onLongPress(new TabView.HitTarget(this.tabView, false, (String)null, true, var4));
            return true;
         }
      }
   }

   public void setChromeClient(TabChromeClient var1) {
      this.chromeClient = var1;
   }
}
