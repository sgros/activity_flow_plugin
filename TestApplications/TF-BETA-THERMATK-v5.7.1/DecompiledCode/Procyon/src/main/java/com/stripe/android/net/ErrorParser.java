// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

import org.json.JSONException;
import com.stripe.android.util.StripeJsonUtils;
import org.json.JSONObject;

class ErrorParser
{
    static StripeError parseError(final String s) {
        final StripeError stripeError = new StripeError();
        try {
            final JSONObject jsonObject = new JSONObject(s).getJSONObject("error");
            stripeError.charge = StripeJsonUtils.optString(jsonObject, "charge");
            stripeError.code = StripeJsonUtils.optString(jsonObject, "code");
            stripeError.decline_code = StripeJsonUtils.optString(jsonObject, "decline_code");
            stripeError.message = StripeJsonUtils.optString(jsonObject, "message");
            stripeError.param = StripeJsonUtils.optString(jsonObject, "param");
            stripeError.type = StripeJsonUtils.optString(jsonObject, "type");
        }
        catch (JSONException ex) {
            stripeError.message = "An improperly formatted error response was found.";
        }
        return stripeError;
    }
    
    static class StripeError
    {
        public String charge;
        public String code;
        public String decline_code;
        public String message;
        public String param;
        public String type;
    }
}
