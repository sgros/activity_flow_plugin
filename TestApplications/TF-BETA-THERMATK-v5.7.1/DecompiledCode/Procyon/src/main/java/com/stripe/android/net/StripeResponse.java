// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

import java.util.List;
import java.util.Map;

public class StripeResponse
{
    private String mResponseBody;
    private int mResponseCode;
    private Map<String, List<String>> mResponseHeaders;
    
    public StripeResponse(final int mResponseCode, final String mResponseBody, final Map<String, List<String>> mResponseHeaders) {
        this.mResponseCode = mResponseCode;
        this.mResponseBody = mResponseBody;
        this.mResponseHeaders = mResponseHeaders;
    }
    
    public String getResponseBody() {
        return this.mResponseBody;
    }
    
    public int getResponseCode() {
        return this.mResponseCode;
    }
    
    public Map<String, List<String>> getResponseHeaders() {
        return this.mResponseHeaders;
    }
}
