package com.stripe.android.net;

import com.stripe.android.model.Card;
import com.stripe.android.util.StripeJsonUtils;
import com.stripe.android.util.StripeTextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CardParser {
    public static Card parseCard(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = jSONObject;
        return new Card(null, Integer.valueOf(jSONObject2.getInt("exp_month")), Integer.valueOf(jSONObject2.getInt("exp_year")), null, StripeJsonUtils.optString(jSONObject2, "name"), StripeJsonUtils.optString(jSONObject2, "address_line1"), StripeJsonUtils.optString(jSONObject2, "address_line2"), StripeJsonUtils.optString(jSONObject2, "address_city"), StripeJsonUtils.optString(jSONObject2, "address_state"), StripeJsonUtils.optString(jSONObject2, "address_zip"), StripeJsonUtils.optString(jSONObject2, "address_country"), StripeTextUtils.asCardBrand(StripeJsonUtils.optString(jSONObject2, "brand")), StripeJsonUtils.optString(jSONObject2, "last4"), StripeJsonUtils.optString(jSONObject2, "fingerprint"), StripeTextUtils.asFundingType(StripeJsonUtils.optString(jSONObject2, "funding")), StripeJsonUtils.optString(jSONObject2, "country"), StripeJsonUtils.optString(jSONObject2, "currency"));
    }
}
