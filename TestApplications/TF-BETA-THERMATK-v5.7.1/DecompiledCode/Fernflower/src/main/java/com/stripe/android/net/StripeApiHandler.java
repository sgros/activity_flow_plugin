package com.stripe.android.net;

import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.exception.PermissionException;
import com.stripe.android.exception.RateLimitException;
import com.stripe.android.model.Token;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONObject;

public class StripeApiHandler {
   private static final SSLSocketFactory SSL_SOCKET_FACTORY = new StripeSSLSocketFactory();

   private static HttpURLConnection createGetConnection(String var0, String var1, RequestOptions var2) throws IOException {
      HttpURLConnection var3 = createStripeConnection(formatURL(var0, var1), var2);
      var3.setRequestMethod("GET");
      return var3;
   }

   private static HttpURLConnection createPostConnection(String var0, String var1, RequestOptions var2) throws IOException {
      HttpURLConnection var10 = createStripeConnection(var0, var2);
      var10.setDoOutput(true);
      var10.setRequestMethod("POST");
      var10.setRequestProperty("Content-Type", String.format("application/x-www-form-urlencoded;charset=%s", "UTF-8"));
      boolean var7 = false;

      OutputStream var11;
      try {
         var7 = true;
         var11 = var10.getOutputStream();
         var7 = false;
      } finally {
         if (var7) {
            var1 = null;
            if (var1 != null) {
               var1.close();
            }

         }
      }

      try {
         var11.write(var1.getBytes("UTF-8"));
      } finally {
         ;
      }

      if (var11 != null) {
         var11.close();
      }

      return var10;
   }

   static String createQuery(Map var0) throws UnsupportedEncodingException, InvalidRequestException {
      StringBuilder var1 = new StringBuilder();
      Iterator var3 = flattenParams(var0).iterator();

      while(var3.hasNext()) {
         if (var1.length() > 0) {
            var1.append("&");
         }

         StripeApiHandler.Parameter var2 = (StripeApiHandler.Parameter)var3.next();
         var1.append(urlEncodePair(var2.key, var2.value));
      }

      return var1.toString();
   }

   private static HttpURLConnection createStripeConnection(String var0, RequestOptions var1) throws IOException {
      HttpURLConnection var3 = (HttpURLConnection)(new URL(var0)).openConnection();
      var3.setConnectTimeout(30000);
      var3.setReadTimeout(80000);
      var3.setUseCaches(false);
      Iterator var2 = getHeaders(var1).entrySet().iterator();

      while(var2.hasNext()) {
         Entry var4 = (Entry)var2.next();
         var3.setRequestProperty((String)var4.getKey(), (String)var4.getValue());
      }

      if (var3 instanceof HttpsURLConnection) {
         ((HttpsURLConnection)var3).setSSLSocketFactory(SSL_SOCKET_FACTORY);
      }

      return var3;
   }

   public static Token createToken(Map var0, RequestOptions var1) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
      return requestToken("POST", getApiUrl(), var0, var1);
   }

   private static List flattenParams(Map var0) throws InvalidRequestException {
      return flattenParamsMap(var0, (String)null);
   }

   private static List flattenParamsList(List var0, String var1) throws InvalidRequestException {
      LinkedList var2 = new LinkedList();
      Iterator var3 = var0.iterator();
      String var4 = String.format("%s[]", var1);
      if (var0.isEmpty()) {
         var2.add(new StripeApiHandler.Parameter(var1, ""));
      } else {
         while(var3.hasNext()) {
            var2.addAll(flattenParamsValue(var3.next(), var4));
         }
      }

      return var2;
   }

   private static List flattenParamsMap(Map var0, String var1) throws InvalidRequestException {
      LinkedList var2 = new LinkedList();
      if (var0 == null) {
         return var2;
      } else {
         Object var5;
         String var7;
         for(Iterator var3 = var0.entrySet().iterator(); var3.hasNext(); var2.addAll(flattenParamsValue(var5, var7))) {
            Entry var6 = (Entry)var3.next();
            String var4 = (String)var6.getKey();
            var5 = var6.getValue();
            var7 = var4;
            if (var1 != null) {
               var7 = String.format("%s[%s]", var1, var4);
            }
         }

         return var2;
      }
   }

   private static List flattenParamsValue(Object var0, String var1) throws InvalidRequestException {
      if (var0 instanceof Map) {
         var0 = flattenParamsMap((Map)var0, var1);
      } else if (var0 instanceof List) {
         var0 = flattenParamsList((List)var0, var1);
      } else {
         if ("".equals(var0)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("You cannot set '");
            var3.append(var1);
            var3.append("' to an empty string. We interpret empty strings as null in requests. You may set '");
            var3.append(var1);
            var3.append("' to null to delete the property.");
            throw new InvalidRequestException(var3.toString(), var1, (String)null, 0, (Throwable)null);
         }

         if (var0 == null) {
            var0 = new LinkedList();
            ((List)var0).add(new StripeApiHandler.Parameter(var1, ""));
         } else {
            LinkedList var2 = new LinkedList();
            var2.add(new StripeApiHandler.Parameter(var1, var0.toString()));
            var0 = var2;
         }
      }

      return (List)var0;
   }

   private static String formatURL(String var0, String var1) {
      String var2 = var0;
      if (var1 != null) {
         if (var1.isEmpty()) {
            var2 = var0;
         } else {
            var2 = "?";
            if (var0.contains("?")) {
               var2 = "&";
            }

            var2 = String.format("%s%s%s", var0, var2, var1);
         }
      }

      return var2;
   }

   static String getApiUrl() {
      return String.format("%s/v1/%s", "https://api.stripe.com", "tokens");
   }

   static Map getHeaders(RequestOptions var0) {
      HashMap var1 = new HashMap();
      String var2 = var0.getApiVersion();
      var1.put("Accept-Charset", "UTF-8");
      var1.put("Accept", "application/json");
      int var3 = 0;
      var1.put("User-Agent", String.format("Stripe/v1 JavaBindings/%s", "3.5.0"));
      var1.put("Authorization", String.format("Bearer %s", var0.getPublishableApiKey()));
      String[] var4 = new String[]{"os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"};
      HashMap var5 = new HashMap();

      for(int var6 = var4.length; var3 < var6; ++var3) {
         String var7 = var4[var3];
         var5.put(var7, System.getProperty(var7));
      }

      var5.put("bindings.version", "3.5.0");
      var5.put("lang", "Java");
      var5.put("publisher", "Stripe");
      var1.put("X-Stripe-Client-User-Agent", (new JSONObject(var5)).toString());
      if (var2 != null) {
         var1.put("Stripe-Version", var2);
      }

      if (var0.getIdempotencyKey() != null) {
         var1.put("Idempotency-Key", var0.getIdempotencyKey());
      }

      return var1;
   }

   private static String getResponseBody(InputStream var0) throws IOException {
      String var1 = (new Scanner(var0, "UTF-8")).useDelimiter("\\A").next();
      var0.close();
      return var1;
   }

   private static StripeResponse getStripeResponse(String var0, String var1, Map var2, RequestOptions var3) throws InvalidRequestException, APIConnectionException, APIException {
      String var5;
      try {
         var5 = createQuery(var2);
      } catch (UnsupportedEncodingException var4) {
         throw new InvalidRequestException("Unable to encode parameters to UTF-8. Please contact support@stripe.com for assistance.", (String)null, (String)null, 0, var4);
      }

      return makeURLConnectionRequest(var0, var1, var5, var3);
   }

   private static void handleAPIError(String var0, int var1, String var2) throws InvalidRequestException, AuthenticationException, CardException, APIException {
      ErrorParser.StripeError var3 = ErrorParser.parseError(var0);
      if (var1 != 429) {
         switch(var1) {
         case 400:
            throw new InvalidRequestException(var3.message, var3.param, var2, var1, (Throwable)null);
         case 401:
            throw new AuthenticationException(var3.message, var2, var1);
         case 402:
            throw new CardException(var3.message, var2, var3.code, var3.param, var3.decline_code, var3.charge, var1, (Throwable)null);
         case 403:
            throw new PermissionException(var3.message, var2, var1);
         case 404:
            throw new InvalidRequestException(var3.message, var3.param, var2, var1, (Throwable)null);
         default:
            throw new APIException(var3.message, var2, var1, (Throwable)null);
         }
      } else {
         throw new RateLimitException(var3.message, var3.param, var2, var1, (Throwable)null);
      }
   }

   private static StripeResponse makeURLConnectionRequest(String param0, String param1, String param2, RequestOptions param3) throws APIConnectionException {
      // $FF: Couldn't be decompiled
   }

   private static Token requestToken(String param0, String param1, Map param2, RequestOptions param3) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
      // $FF: Couldn't be decompiled
   }

   private static String urlEncode(String var0) throws UnsupportedEncodingException {
      return var0 == null ? null : URLEncoder.encode(var0, "UTF-8");
   }

   private static String urlEncodePair(String var0, String var1) throws UnsupportedEncodingException {
      return String.format("%s=%s", urlEncode(var0), urlEncode(var1));
   }

   private static final class Parameter {
      public final String key;
      public final String value;

      public Parameter(String var1, String var2) {
         this.key = var1;
         this.value = var2;
      }
   }
}
