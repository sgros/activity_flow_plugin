package org.mozilla.rocket.tabs;

public class TabViewClient {
   public boolean handleExternalUrl(String var1) {
      return false;
   }

   public void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1, String var2, String var3) {
   }

   public void onPageFinished(boolean var1) {
   }

   public void onPageStarted(String var1) {
   }

   public void onURLChanged(String var1) {
   }

   public void updateFailingUrl(String var1, boolean var2) {
   }

   public interface HttpAuthCallback {
      void cancel();

      void proceed(String var1, String var2);
   }
}
