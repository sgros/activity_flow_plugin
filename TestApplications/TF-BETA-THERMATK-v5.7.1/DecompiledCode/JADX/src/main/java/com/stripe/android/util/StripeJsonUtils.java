package com.stripe.android.util;

import org.json.JSONException;
import org.json.JSONObject;

public class StripeJsonUtils {
    public static String getString(JSONObject jSONObject, String str) throws JSONException {
        return nullIfNullOrEmpty(jSONObject.getString(str));
    }

    public static String optString(JSONObject jSONObject, String str) {
        return nullIfNullOrEmpty(jSONObject.optString(str));
    }

    static String nullIfNullOrEmpty(String str) {
        return ("null".equals(str) || "".equals(str)) ? null : str;
    }
}
