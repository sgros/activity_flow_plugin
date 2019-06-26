package okhttp3.internal.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public final class HttpHeaders {
   private static final Pattern PARAMETER = Pattern.compile(" +([^ \"=]*)=(:?\"([^\"]*)\"|([^ \"=]*)) *(:?,|$)");
   private static final String QUOTED_STRING = "\"([^\"]*)\"";
   private static final String TOKEN = "([^ \"=]*)";

   private HttpHeaders() {
   }

   public static long contentLength(Headers var0) {
      return stringToLong(var0.get("Content-Length"));
   }

   public static long contentLength(Response var0) {
      return contentLength(var0.headers());
   }

   public static boolean hasBody(Response var0) {
      boolean var1 = false;
      if (!var0.request().method().equals("HEAD")) {
         int var2 = var0.code();
         if ((var2 < 100 || var2 >= 200) && var2 != 204 && var2 != 304) {
            var1 = true;
         } else if (contentLength(var0) != -1L || "chunked".equalsIgnoreCase(var0.header("Transfer-Encoding"))) {
            var1 = true;
         }
      }

      return var1;
   }

   public static boolean hasVaryAll(Headers var0) {
      return varyFields(var0).contains("*");
   }

   public static boolean hasVaryAll(Response var0) {
      return hasVaryAll(var0.headers());
   }

   public static List parseChallenges(Headers var0, String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var0.values(var1).iterator();

      while(true) {
         while(true) {
            int var4;
            String var8;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var8 = (String)var3.next();
               var4 = var8.indexOf(32);
            } while(var4 == -1);

            Matcher var5 = PARAMETER.matcher(var8);

            for(int var6 = var4; var5.find(var6); var6 = var5.end()) {
               if (var8.regionMatches(true, var5.start(1), "realm", 0, 5)) {
                  var1 = var8.substring(0, var4);
                  String var7 = var5.group(3);
                  if (var7 != null) {
                     var2.add(new Challenge(var1, var7));
                     break;
                  }
               }
            }
         }
      }
   }

   public static int parseSeconds(String var0, int var1) {
      long var2;
      try {
         var2 = Long.parseLong(var0);
      } catch (NumberFormatException var4) {
         return var1;
      }

      if (var2 > 2147483647L) {
         var1 = Integer.MAX_VALUE;
      } else if (var2 < 0L) {
         var1 = 0;
      } else {
         var1 = (int)var2;
      }

      return var1;
   }

   public static void receiveHeaders(CookieJar var0, HttpUrl var1, Headers var2) {
      if (var0 != CookieJar.NO_COOKIES) {
         List var3 = Cookie.parseAll(var1, var2);
         if (!var3.isEmpty()) {
            var0.saveFromResponse(var1, var3);
         }
      }

   }

   public static int skipUntil(String var0, int var1, String var2) {
      while(var1 < var0.length() && var2.indexOf(var0.charAt(var1)) == -1) {
         ++var1;
      }

      return var1;
   }

   public static int skipWhitespace(String var0, int var1) {
      while(var1 < var0.length()) {
         char var2 = var0.charAt(var1);
         if (var2 != ' ' && var2 != '\t') {
            break;
         }

         ++var1;
      }

      return var1;
   }

   private static long stringToLong(String var0) {
      long var1 = -1L;
      long var3;
      if (var0 == null) {
         var3 = var1;
      } else {
         try {
            var3 = Long.parseLong(var0);
         } catch (NumberFormatException var5) {
            var3 = var1;
         }
      }

      return var3;
   }

   public static Set varyFields(Headers var0) {
      Object var1 = Collections.emptySet();
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         if ("Vary".equalsIgnoreCase(var0.name(var2))) {
            String var4 = var0.value(var2);
            Object var5 = var1;
            if (((Set)var1).isEmpty()) {
               var5 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
            }

            String[] var8 = var4.split(",");
            int var6 = var8.length;
            int var7 = 0;

            while(true) {
               var1 = var5;
               if (var7 >= var6) {
                  break;
               }

               ((Set)var5).add(var8[var7].trim());
               ++var7;
            }
         }
      }

      return (Set)var1;
   }

   private static Set varyFields(Response var0) {
      return varyFields(var0.headers());
   }

   public static Headers varyHeaders(Headers var0, Headers var1) {
      Set var6 = varyFields(var1);
      if (var6.isEmpty()) {
         var0 = (new Headers.Builder()).build();
      } else {
         Headers.Builder var2 = new Headers.Builder();
         int var3 = 0;

         for(int var4 = var0.size(); var3 < var4; ++var3) {
            String var5 = var0.name(var3);
            if (var6.contains(var5)) {
               var2.add(var5, var0.value(var3));
            }
         }

         var0 = var2.build();
      }

      return var0;
   }

   public static Headers varyHeaders(Response var0) {
      return varyHeaders(var0.networkResponse().request().headers(), var0.headers());
   }

   public static boolean varyMatches(Response var0, Headers var1, Request var2) {
      Iterator var3 = varyFields(var0).iterator();

      boolean var4;
      while(true) {
         if (var3.hasNext()) {
            String var5 = (String)var3.next();
            if (Util.equal(var1.values(var5), var2.headers(var5))) {
               continue;
            }

            var4 = false;
            break;
         }

         var4 = true;
         break;
      }

      return var4;
   }
}
