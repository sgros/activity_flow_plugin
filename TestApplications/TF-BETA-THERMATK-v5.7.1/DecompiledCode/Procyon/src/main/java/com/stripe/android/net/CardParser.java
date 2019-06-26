// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

import org.json.JSONException;
import com.stripe.android.util.StripeTextUtils;
import com.stripe.android.util.StripeJsonUtils;
import com.stripe.android.model.Card;
import org.json.JSONObject;

public class CardParser
{
    public static Card parseCard(final JSONObject jsonObject) throws JSONException {
        return new Card(null, jsonObject.getInt("exp_month"), jsonObject.getInt("exp_year"), null, StripeJsonUtils.optString(jsonObject, "name"), StripeJsonUtils.optString(jsonObject, "address_line1"), StripeJsonUtils.optString(jsonObject, "address_line2"), StripeJsonUtils.optString(jsonObject, "address_city"), StripeJsonUtils.optString(jsonObject, "address_state"), StripeJsonUtils.optString(jsonObject, "address_zip"), StripeJsonUtils.optString(jsonObject, "address_country"), StripeTextUtils.asCardBrand(StripeJsonUtils.optString(jsonObject, "brand")), StripeJsonUtils.optString(jsonObject, "last4"), StripeJsonUtils.optString(jsonObject, "fingerprint"), StripeTextUtils.asFundingType(StripeJsonUtils.optString(jsonObject, "funding")), StripeJsonUtils.optString(jsonObject, "country"), StripeJsonUtils.optString(jsonObject, "currency"));
    }
}
