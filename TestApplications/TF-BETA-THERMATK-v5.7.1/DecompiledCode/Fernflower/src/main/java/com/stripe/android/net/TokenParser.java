package com.stripe.android.net;

import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.util.StripeJsonUtils;
import com.stripe.android.util.StripeTextUtils;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class TokenParser {
   public static Token parseToken(String var0) throws JSONException {
      JSONObject var1 = new JSONObject(var0);
      String var2 = StripeJsonUtils.getString(var1, "id");
      long var3 = var1.getLong("created");
      boolean var5 = var1.getBoolean("livemode");
      var0 = StripeTextUtils.asTokenType(StripeJsonUtils.getString(var1, "type"));
      boolean var6 = var1.getBoolean("used");
      Card var7 = CardParser.parseCard(var1.getJSONObject("card"));
      Date var8 = new Date(Long.valueOf(var3) * 1000L);
      return new Token(var2, Boolean.valueOf(var5), var8, var6, var7, var0);
   }
}
