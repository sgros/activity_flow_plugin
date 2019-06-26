package com.stripe.android.util;

import org.json.JSONException;
import org.json.JSONObject;

public class StripeJsonUtils {
   public static String getString(JSONObject var0, String var1) throws JSONException {
      return nullIfNullOrEmpty(var0.getString(var1));
   }

   static String nullIfNullOrEmpty(String var0) {
      String var1;
      if (!"null".equals(var0)) {
         var1 = var0;
         if (!"".equals(var0)) {
            return var1;
         }
      }

      var1 = null;
      return var1;
   }

   public static String optString(JSONObject var0, String var1) {
      return nullIfNullOrEmpty(var0.optString(var1));
   }
}
