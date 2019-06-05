package org.mozilla.rocket.tabs;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import org.mozilla.rocket.tabs.web.DownloadCallback;

public interface TabView {
   void bindOnNewWindowCreation(Message var1);

   boolean canGoBack();

   boolean canGoForward();

   void destroy();

   String getTitle();

   String getUrl();

   View getView();

   void goBack();

   void goForward();

   void insertBrowsingHistory();

   void loadUrl(String var1);

   void onPause();

   void onResume();

   void performExitFullScreen();

   void reload();

   void restoreViewState(Bundle var1);

   void saveViewState(Bundle var1);

   void setChromeClient(TabChromeClient var1);

   void setContentBlockingEnabled(boolean var1);

   void setDownloadCallback(DownloadCallback var1);

   void setFindListener(TabView.FindListener var1);

   void setImageBlockingEnabled(boolean var1);

   void setViewClient(TabViewClient var1);

   void stopLoading();

   public interface FindListener {
      void onFindResultReceived(int var1, int var2, boolean var3);
   }

   public interface FullscreenCallback {
      void fullScreenExited();
   }

   public static class HitTarget {
      public final String imageURL;
      public final boolean isImage;
      public final boolean isLink;
      public final String linkURL;
      public final TabView source;

      public HitTarget(TabView var1, boolean var2, String var3, boolean var4, String var5) {
         if (var2 && var3 == null) {
            throw new IllegalStateException("link hittarget must contain URL");
         } else if (var4 && var5 == null) {
            throw new IllegalStateException("image hittarget must contain URL");
         } else {
            this.source = var1;
            this.isLink = var2;
            this.linkURL = var3;
            this.isImage = var4;
            this.imageURL = var5;
         }
      }
   }
}
