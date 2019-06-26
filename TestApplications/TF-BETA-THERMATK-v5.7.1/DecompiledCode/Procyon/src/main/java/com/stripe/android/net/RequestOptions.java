// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

public class RequestOptions
{
    private final String mApiVersion;
    private final String mIdempotencyKey;
    private final String mPublishableApiKey;
    
    private RequestOptions(final String mApiVersion, final String mIdempotencyKey, final String mPublishableApiKey) {
        this.mApiVersion = mApiVersion;
        this.mIdempotencyKey = mIdempotencyKey;
        this.mPublishableApiKey = mPublishableApiKey;
    }
    
    public static RequestOptionsBuilder builder(final String s) {
        return new RequestOptionsBuilder(s);
    }
    
    public String getApiVersion() {
        return this.mApiVersion;
    }
    
    public String getIdempotencyKey() {
        return this.mIdempotencyKey;
    }
    
    public String getPublishableApiKey() {
        return this.mPublishableApiKey;
    }
    
    public static final class RequestOptionsBuilder
    {
        private String apiVersion;
        private String idempotencyKey;
        private String publishableApiKey;
        
        public RequestOptionsBuilder(final String publishableApiKey) {
            this.publishableApiKey = publishableApiKey;
        }
        
        public RequestOptions build() {
            return new RequestOptions(this.apiVersion, this.idempotencyKey, this.publishableApiKey, null);
        }
    }
}
