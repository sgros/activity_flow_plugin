// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

import org.json.JSONException;
import java.util.Date;
import com.stripe.android.util.StripeTextUtils;
import com.stripe.android.util.StripeJsonUtils;
import org.json.JSONObject;
import com.stripe.android.model.Token;

public class TokenParser
{
    public static Token parseToken(String tokenType) throws JSONException {
        final JSONObject jsonObject = new JSONObject(tokenType);
        final String string = StripeJsonUtils.getString(jsonObject, "id");
        final long long1 = jsonObject.getLong("created");
        final boolean boolean1 = jsonObject.getBoolean("livemode");
        tokenType = StripeTextUtils.asTokenType(StripeJsonUtils.getString(jsonObject, "type"));
        return new Token(string, (boolean)boolean1, new Date(long1 * 1000L), jsonObject.getBoolean("used"), CardParser.parseCard(jsonObject.getJSONObject("card")), tokenType);
    }
}
