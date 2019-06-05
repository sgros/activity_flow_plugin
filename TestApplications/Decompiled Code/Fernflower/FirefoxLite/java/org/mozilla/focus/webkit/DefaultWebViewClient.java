package org.mozilla.focus.webkit;

import android.content.Context;
import android.net.Uri;
import android.webkit.WebView;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.urlutils.UrlUtils;

public class DefaultWebViewClient extends TrackingProtectionWebViewClient {
   public DefaultWebViewClient(Context var1) {
      super(var1);
   }

   private boolean shouldOverrideInternalPages(WebView var1, String var2) {
      if (SupportUtils.isTemplateSupportPages(var2)) {
         SupportUtils.loadSupportPages(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
      var1.getSettings().setLoadsImagesAutomatically(true);
      if (this.shouldOverrideInternalPages(var1, var2)) {
         return true;
      } else if (!UrlUtils.isSupportedProtocol(Uri.parse(var2).getScheme()) && IntentUtils.handleExternalUri(var1.getContext(), var2)) {
         return true;
      } else {
         var1.getSettings().setLoadsImagesAutomatically(true ^ Settings.getInstance(var1.getContext()).shouldBlockImages());
         return super.shouldOverrideUrlLoading(var1, var2);
      }
   }
}
