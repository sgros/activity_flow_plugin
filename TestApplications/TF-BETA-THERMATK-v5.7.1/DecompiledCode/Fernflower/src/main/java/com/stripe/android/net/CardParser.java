package com.stripe.android.net;

import com.stripe.android.model.Card;
import com.stripe.android.util.StripeJsonUtils;
import com.stripe.android.util.StripeTextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CardParser {
   public static Card parseCard(JSONObject var0) throws JSONException {
      return new Card((String)null, var0.getInt("exp_month"), var0.getInt("exp_year"), (String)null, StripeJsonUtils.optString(var0, "name"), StripeJsonUtils.optString(var0, "address_line1"), StripeJsonUtils.optString(var0, "address_line2"), StripeJsonUtils.optString(var0, "address_city"), StripeJsonUtils.optString(var0, "address_state"), StripeJsonUtils.optString(var0, "address_zip"), StripeJsonUtils.optString(var0, "address_country"), StripeTextUtils.asCardBrand(StripeJsonUtils.optString(var0, "brand")), StripeJsonUtils.optString(var0, "last4"), StripeJsonUtils.optString(var0, "fingerprint"), StripeTextUtils.asFundingType(StripeJsonUtils.optString(var0, "funding")), StripeJsonUtils.optString(var0, "country"), StripeJsonUtils.optString(var0, "currency"));
   }
}
