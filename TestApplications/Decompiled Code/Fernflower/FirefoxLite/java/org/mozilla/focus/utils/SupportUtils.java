package org.mozilla.focus.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.webkit.WebView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Pattern;
import org.mozilla.focus.locale.Locales;

public class SupportUtils {
   static final String[] SUPPORTED_URLS = new String[]{"about:blank", "focusabout:", "file:///android_res/raw/rights.html", "https://www.mozilla.org/privacy/firefox-rocket", "file:///android_res/raw/about.html"};
   private static final Pattern schemePattern = Pattern.compile("^.+://");

   public static String getAboutURI() {
      return "file:///android_res/raw/about.html";
   }

   public static String getManifestoURL() {
      String var0 = Locales.getLanguageTag(Locale.getDefault());
      StringBuilder var1 = new StringBuilder();
      var1.append("https://www.mozilla.org/");
      var1.append(var0);
      var1.append("/about/manifesto/");
      return var1.toString();
   }

   public static String getPrivacyURL() {
      return "https://www.mozilla.org/privacy/firefox-rocket";
   }

   public static String getSumoURLForTopic(Context var0, String var1) {
      try {
         var1 = URLEncoder.encode(var1, "UTF-8");
      } catch (UnsupportedEncodingException var5) {
         throw new IllegalStateException("utf-8 should always be available", var5);
      }

      String var6;
      try {
         var6 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0).versionName;
      } catch (NameNotFoundException var4) {
         throw new IllegalStateException("Unable find package details for Focus", var4);
      }

      String var2 = Locales.getLanguageTag(Locale.getDefault());
      StringBuilder var3 = new StringBuilder();
      var3.append("https://support.mozilla.org/1/mobile/");
      var3.append(var6);
      var3.append("/");
      var3.append("Android");
      var3.append("/");
      var3.append(var2);
      var3.append("/");
      var3.append(var1);
      return var3.toString();
   }

   public static String getYourRightsURI() {
      return "file:///android_res/raw/rights.html";
   }

   public static boolean isTemplateSupportPages(String var0) {
      boolean var2;
      byte var3;
      label21: {
         int var1 = var0.hashCode();
         var2 = true;
         if (var1 != -473277749) {
            if (var1 == 2112069925 && var0.equals("focusabout:")) {
               var3 = 0;
               break label21;
            }
         } else if (var0.equals("file:///android_res/raw/rights.html")) {
            var3 = 1;
            break label21;
         }

         var3 = -1;
      }

      switch(var3) {
      default:
         var2 = false;
      case 0:
      case 1:
         return var2;
      }
   }

   public static boolean isUrl(String var0) {
      boolean var1 = TextUtils.isEmpty(var0);
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         var0 = var0.trim().toLowerCase(Locale.getDefault());
         if (var0.contains(" ")) {
            return false;
         } else {
            String[] var3 = SUPPORTED_URLS;
            int var4 = var3.length;

            int var5;
            for(var5 = 0; var5 < var4; ++var5) {
               if (var3[var5].equals(var0)) {
                  return true;
               }
            }

            Uri var7;
            if (schemePattern.matcher(var0).find()) {
               var7 = Uri.parse(var0);
            } else {
               StringBuilder var8 = new StringBuilder();
               var8.append("http://");
               var8.append(var0);
               var7 = Uri.parse(var8.toString());
            }

            String var9;
            if (TextUtils.isEmpty(var7.getHost())) {
               var9 = "";
            } else {
               var9 = var7.getHost();
            }

            byte var10;
            label61: {
               String var6 = var7.getScheme();
               var5 = var6.hashCode();
               if (var5 != 3143036) {
                  if (var5 != 3213448) {
                     if (var5 == 99617003 && var6.equals("https")) {
                        var10 = 1;
                        break label61;
                     }
                  } else if (var6.equals("http")) {
                     var10 = 0;
                     break label61;
                  }
               } else if (var6.equals("file")) {
                  var10 = 2;
                  break label61;
               }

               var10 = -1;
            }

            switch(var10) {
            case 0:
            case 1:
               if (!var9.contains(".")) {
                  return var9.equals("localhost");
               }

               var1 = var2;
               if (!var9.startsWith(".")) {
                  var1 = var2;
                  if (!var9.endsWith(".")) {
                     var1 = true;
                  }
               }

               return var1;
            case 2:
               return TextUtils.isEmpty(var7.getPath()) ^ true;
            default:
               return true;
            }
         }
      }
   }

   private static void loadAbout(WebView var0) {
      Context var1 = var0.getContext();
      Resources var2 = Locales.getLocalizedResources(var0.getContext());
      String var3 = DebugUtils.loadWebViewVersion(var0.getContext());
      ArrayMap var4 = new ArrayMap();
      String var5 = var0.getContext().getResources().getString(2131755062);
      String var6 = var0.getContext().getResources().getString(2131755267);
      String var7 = var0.getContext().getResources().getString(2131755048, new Object[]{var5, var6});
      String var8 = getAboutURI();
      var5 = getManifestoURL();
      String var9 = getSumoURLForTopic(var0.getContext(), "rocket-help");
      var6 = getYourRightsURI();
      String var10 = getPrivacyURL();
      String var11 = getSumoURLForTopic(var0.getContext(), "firefox-lite-feed");
      String var12 = var2.getString(2131755049);
      String var13 = var2.getString(2131755052);
      String var14 = var2.getString(2131755053);
      String var15 = var2.getString(2131755051);
      String var16 = var2.getString(2131755050, new Object[]{var2.getString(2131755245)});

      String var20;
      label21: {
         label20: {
            PackageManager var17;
            try {
               var17 = var1.getPackageManager();
               var20 = var1.getPackageName();
            } catch (NameNotFoundException var19) {
               break label20;
            }

            try {
               var20 = var17.getPackageInfo(var20, 0).versionName;
               break label21;
            } catch (NameNotFoundException var18) {
            }
         }

         var20 = "";
      }

      var4.put("%about-version%", var20);
      var4.put("%about-content%", var2.getString(2131755047, new Object[]{var7, var5, var12, var9, var13, var6, var14, var10, var15, var11, var16}));
      var4.put("%wordmark%", HtmlLoader.loadDrawableAsDataURI(var0.getContext(), 2131230881, 2131099672));
      var4.put("%webview-version%", var3);
      var0.loadDataWithBaseURL(var8, HtmlLoader.loadResourceFile(var0.getContext(), 2131689472, var4), "text/html", "UTF-8", (String)null);
   }

   private static void loadRights(WebView var0) {
      Context var1 = var0.getContext();
      Resources var2 = Locales.getLocalizedResources(var0.getContext());
      ArrayMap var3 = new ArrayMap();
      String var4 = var1.getResources().getString(2131755062);
      String var5 = var1.getResources().getString(2131755267);
      String var6 = var1.getResources().getString(2131755199);
      String var7 = var1.getResources().getString(2131755268);
      var3.put("%your-rights-content1%", var2.getString(2131755428, new Object[]{var4, var5}));
      var3.put("%your-rights-content2%", var2.getString(2131755429, new Object[]{var4, "https://www.mozilla.org/en-US/MPL/", var7}));
      var3.put("%your-rights-content3%", var2.getString(2131755430, new Object[]{var4, "https://www.mozilla.org/foundation/trademarks/policy/", var5, var6}));
      var3.put("%your-rights-content4%", var2.getString(2131755431, new Object[]{var4, "file:///android_asset/licenses.html"}));
      var3.put("%your-rights-content5%", var2.getString(2131755432, new Object[]{var4, "file:///android_asset/gpl.html", "https://wiki.mozilla.org/Security/Tracking_protection#Lists"}));
      String var8 = HtmlLoader.loadResourceFile(var0.getContext(), 2131689484, var3);
      var0.loadDataWithBaseURL(getYourRightsURI(), var8, "text/html", "UTF-8", (String)null);
   }

   public static void loadSupportPages(WebView var0, String var1) {
      byte var4;
      label24: {
         int var2 = var1.hashCode();
         if (var2 != -473277749) {
            if (var2 == 2112069925 && var1.equals("focusabout:")) {
               var4 = 0;
               break label24;
            }
         } else if (var1.equals("file:///android_res/raw/rights.html")) {
            var4 = 1;
            break label24;
         }

         var4 = -1;
      }

      switch(var4) {
      case 0:
         loadAbout(var0);
         break;
      case 1:
         loadRights(var0);
         break;
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Unknown internal pages url: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }

   }

   public static String normalize(String var0) {
      String var1 = var0.trim();
      Uri var2 = Uri.parse(var1);
      String[] var3 = SUPPORTED_URLS;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         if (var3[var5].equals(var0)) {
            return var0;
         }
      }

      Uri var6 = var2;
      if (TextUtils.isEmpty(var2.getScheme())) {
         StringBuilder var7 = new StringBuilder();
         var7.append("http://");
         var7.append(var1);
         var6 = Uri.parse(var7.toString());
      }

      return var6.toString();
   }
}
