// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.util;

import org.json.JSONException;
import org.json.JSONObject;

public class StripeJsonUtils
{
    public static String getString(final JSONObject jsonObject, final String s) throws JSONException {
        return nullIfNullOrEmpty(jsonObject.getString(s));
    }
    
    static String nullIfNullOrEmpty(final String s) {
        if (!"null".equals(s)) {
            final String s2 = s;
            if (!"".equals(s)) {
                return s2;
            }
        }
        return null;
    }
    
    public static String optString(final JSONObject jsonObject, final String s) {
        return nullIfNullOrEmpty(jsonObject.optString(s));
    }
}
