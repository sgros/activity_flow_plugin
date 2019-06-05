package org.mozilla.httprequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
   private static BufferedReader createReader(InputStream var0) throws IOException {
      return new BufferedReader(new InputStreamReader(new BufferedInputStream(var0), "utf-8"));
   }

   public static String get(URL param0, int param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   public static String get(URL var0, String var1) {
      return get(var0, 2000, var1);
   }

   private static String readLines(URLConnection param0) throws IOException {
      // $FF: Couldn't be decompiled
   }
}
