package com.adjust.sdk;

import android.net.Uri;
import android.net.Uri.Builder;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;

public class UtilNetworking {
   private static String userAgent;

   private static Uri buildUri(String var0, Map var1) {
      Builder var2 = new Builder();
      var2.scheme("https");
      var2.authority("app.adjust.com");
      var2.appendPath(var0);
      Iterator var5 = var1.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         var2.appendQueryParameter((String)var6.getKey(), (String)var6.getValue());
      }

      long var3 = System.currentTimeMillis();
      var2.appendQueryParameter("sent_at", Util.dateFormatter.format(var3));
      return var2.build();
   }

   public static ResponseData createGETHttpsURLConnection(ActivityPackage var0) throws Exception {
      try {
         HashMap var1 = new HashMap(var0.getParameters());
         Uri var2 = buildUri(var0.getPath(), var1);
         URL var5 = new URL(var2.toString());
         HttpsURLConnection var6 = AdjustFactory.getHttpsURLConnection(var5);
         setDefaultHttpsUrlConnectionProperties(var6, var0.getClientSdk());
         var6.setRequestMethod("GET");
         ResponseData var4 = readHttpResponse(var6, var0);
         return var4;
      } catch (Exception var3) {
         throw var3;
      }
   }

   public static ResponseData createPOSTHttpsURLConnection(String param0, ActivityPackage param1, int param2) throws Exception {
      // $FF: Couldn't be decompiled
   }

   private static ILogger getLogger() {
      return AdjustFactory.getLogger();
   }

   private static String getPostDataString(Map var0, int var1) throws UnsupportedEncodingException {
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = var0.entrySet().iterator();

      String var8;
      while(var3.hasNext()) {
         Entry var7 = (Entry)var3.next();
         String var4 = URLEncoder.encode((String)var7.getKey(), "UTF-8");
         var8 = (String)var7.getValue();
         if (var8 != null) {
            var8 = URLEncoder.encode(var8, "UTF-8");
         } else {
            var8 = "";
         }

         if (var2.length() > 0) {
            var2.append("&");
         }

         var2.append(var4);
         var2.append("=");
         var2.append(var8);
      }

      long var5 = System.currentTimeMillis();
      var8 = Util.dateFormatter.format(var5);
      var2.append("&");
      var2.append(URLEncoder.encode("sent_at", "UTF-8"));
      var2.append("=");
      var2.append(URLEncoder.encode(var8, "UTF-8"));
      if (var1 > 0) {
         var2.append("&");
         var2.append(URLEncoder.encode("queue_size", "UTF-8"));
         var2.append("=");
         StringBuilder var9 = new StringBuilder();
         var9.append("");
         var9.append(var1);
         var2.append(URLEncoder.encode(var9.toString(), "UTF-8"));
      }

      return var2.toString();
   }

   private static ResponseData readHttpResponse(HttpsURLConnection param0, ActivityPackage param1) throws Exception {
      // $FF: Couldn't be decompiled
   }

   private static void setDefaultHttpsUrlConnectionProperties(HttpsURLConnection var0, String var1) {
      var0.setRequestProperty("Client-SDK", var1);
      var0.setConnectTimeout(60000);
      var0.setReadTimeout(60000);
      if (userAgent != null) {
         var0.setRequestProperty("User-Agent", userAgent);
      }

   }

   public static void setUserAgent(String var0) {
      userAgent = var0;
   }
}
