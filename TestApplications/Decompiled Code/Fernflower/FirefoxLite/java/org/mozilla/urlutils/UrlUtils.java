package org.mozilla.urlutils;

import android.text.TextUtils;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
   private static final String[] COMMON_SUBDOMAINS_PREFIX_ARRAY = new String[]{"www.", "mobile.", "m."};
   private static final String[] HTTP_SCHEME_PREFIX_ARRAY = new String[]{"http://", "https://"};

   public static boolean isHttpOrHttps(String var0) {
      boolean var1 = TextUtils.isEmpty(var0);
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         if (var0.startsWith("http:") || var0.startsWith("https:")) {
            var2 = true;
         }

         return var2;
      }
   }

   public static boolean isInternalErrorURL(String var0) {
      return "data:text/html;charset=utf-8;base64,".equals(var0);
   }

   public static boolean isPermittedResourceProtocol(String var0) {
      boolean var1 = TextUtils.isEmpty(var0);
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         if (var0.startsWith("http") || var0.startsWith("https") || var0.startsWith("file") || var0.startsWith("data")) {
            var2 = true;
         }

         return var2;
      }
   }

   public static boolean isSupportedProtocol(String var0) {
      boolean var1 = TextUtils.isEmpty(var0);
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         if (isPermittedResourceProtocol(var0) || var0.startsWith("error")) {
            var2 = true;
         }

         return var2;
      }
   }

   public static String stripCommonSubdomains(String var0) {
      return stripPrefix(var0, COMMON_SUBDOMAINS_PREFIX_ARRAY);
   }

   public static String stripHttp(String var0) {
      return stripPrefix(var0, HTTP_SCHEME_PREFIX_ARRAY);
   }

   public static String stripPrefix(String var0, String[] var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = var1.length;
         byte var3 = 0;
         int var4 = 0;

         int var5;
         while(true) {
            var5 = var3;
            if (var4 >= var2) {
               break;
            }

            String var6 = var1[var4];
            if (var0.startsWith(var6)) {
               var5 = var6.length();
               break;
            }

            ++var4;
         }

         return var0.substring(var5);
      }
   }

   public static String stripUserInfo(String var0) {
      if (TextUtils.isEmpty(var0)) {
         return "";
      } else {
         try {
            URI var1 = new URI(var0);
            if (var1.getUserInfo() == null) {
               return var0;
            } else {
               URI var2 = new URI(var1.getScheme(), (String)null, var1.getHost(), var1.getPort(), var1.getPath(), var1.getQuery(), var1.getFragment());
               String var4 = var2.toString();
               return var4;
            }
         } catch (URISyntaxException var3) {
            return var0;
         }
      }
   }

   public static boolean urlsMatchExceptForTrailingSlash(String var0, String var1) {
      int var2 = var0.length() - var1.length();
      if (var2 == 0) {
         return var0.equalsIgnoreCase(var1);
      } else {
         boolean var3 = false;
         boolean var4 = false;
         boolean var5;
         if (var2 == 1) {
            var5 = var4;
            if (var0.charAt(var0.length() - 1) == '/') {
               var5 = var4;
               if (var0.regionMatches(true, 0, var1, 0, var1.length())) {
                  var5 = true;
               }
            }

            return var5;
         } else if (var2 == -1) {
            var5 = var3;
            if (var1.charAt(var1.length() - 1) == '/') {
               var5 = var3;
               if (var1.regionMatches(true, 0, var0, 0, var0.length())) {
                  var5 = true;
               }
            }

            return var5;
         } else {
            return false;
         }
      }
   }
}
