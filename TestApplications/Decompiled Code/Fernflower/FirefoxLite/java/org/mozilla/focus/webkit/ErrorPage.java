package org.mozilla.focus.webkit;

import android.content.res.Resources;
import android.support.v4.util.ArrayMap;
import android.webkit.WebView;
import java.util.Map;
import org.mozilla.focus.utils.HtmlLoader;

public class ErrorPage {
   public static void loadErrorPage(WebView var0, String var1, int var2) {
      String var3 = HtmlLoader.loadResourceFile(var0.getContext(), 2131689479, (Map)null);
      ArrayMap var4 = new ArrayMap();
      Resources var5 = var0.getContext().getResources();
      String var6;
      if (var2 == -10) {
         var6 = var5.getString(2131755142);
         var4.put("%pageTitle%", var5.getString(2131755147));
         var4.put("%messageShort%", var5.getString(2131755147));
         var4.put("%messageLong%", var5.getString(2131755143, new Object[]{var6, ""}));
      } else {
         var6 = var5.getString(2131755144);
         String var7 = var5.getString(2131755145);
         var4.put("%pageTitle%", var5.getString(2131755146));
         var4.put("%messageShort%", var5.getString(2131755146));
         var4.put("%messageLong%", var5.getString(2131755143, new Object[]{var6, var7}));
      }

      var4.put("%button%", var5.getString(2131755141));
      var4.put("%css%", var3);
      var4.put("%imageContentInBase64%", HtmlLoader.loadPngAsDataURI(var0.getContext(), 2131230856));
      String var8 = HtmlLoader.loadResourceFile(var0.getContext(), 2131689478, var4);
      var0.getSettings().setLoadsImagesAutomatically(true);
      var0.loadDataWithBaseURL(var1, var8, "text/html", "UTF8", var1);
   }

   public static boolean supportsErrorCode(int var0) {
      return true;
   }
}
