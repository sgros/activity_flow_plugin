package com.stripe.android.net;

import com.stripe.android.util.StripeJsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

class ErrorParser {
   static ErrorParser.StripeError parseError(String var0) {
      ErrorParser.StripeError var1 = new ErrorParser.StripeError();

      try {
         JSONObject var2 = new JSONObject(var0);
         JSONObject var4 = var2.getJSONObject("error");
         var1.charge = StripeJsonUtils.optString(var4, "charge");
         var1.code = StripeJsonUtils.optString(var4, "code");
         var1.decline_code = StripeJsonUtils.optString(var4, "decline_code");
         var1.message = StripeJsonUtils.optString(var4, "message");
         var1.param = StripeJsonUtils.optString(var4, "param");
         var1.type = StripeJsonUtils.optString(var4, "type");
      } catch (JSONException var3) {
         var1.message = "An improperly formatted error response was found.";
      }

      return var1;
   }

   static class StripeError {
      public String charge;
      public String code;
      public String decline_code;
      public String message;
      public String param;
      public String type;
   }
}
