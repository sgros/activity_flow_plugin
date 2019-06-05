// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.cachedrequestloader;

import android.content.Context;

public class RequestLoaderDelegation
{
    private Context context;
    private boolean forceNetwork;
    private RequestLoader requestLoader;
    private int socketTag;
    private ResponseData stringLiveData;
    private String subscriptionKey;
    private String subscriptionUrl;
    private String userAgent;
    
    RequestLoaderDelegation(final Context context, final String subscriptionKey, final String subscriptionUrl, final String userAgent, final int socketTag, final boolean forceNetwork, final RequestLoader requestLoader) {
        this.context = context;
        this.subscriptionKey = subscriptionKey;
        this.subscriptionUrl = subscriptionUrl;
        this.userAgent = userAgent;
        this.socketTag = socketTag;
        this.forceNetwork = forceNetwork;
        this.requestLoader = requestLoader;
    }
    
    void deleteCache() {
        this.requestLoader.deleteCache(this.context, this.subscriptionKey);
    }
    
    ResponseData getStringLiveData() {
        if (this.stringLiveData == null) {
            this.stringLiveData = new ResponseData();
            if (!this.forceNetwork) {
                this.requestLoader.loadFromCache(this.context, this.subscriptionKey, this.stringLiveData);
            }
            this.requestLoader.loadFromRemote(this.context, this.stringLiveData, this.subscriptionUrl, this.userAgent, this.socketTag);
        }
        return this.stringLiveData;
    }
    
    void writeToCache(final String s) {
        this.requestLoader.writeToCache(s, this.context, this.subscriptionKey);
    }
    
    interface RequestLoader
    {
        void deleteCache(final Context p0, final String p1);
        
        void loadFromCache(final Context p0, final String p1, final ResponseData p2);
        
        void loadFromRemote(final Context p0, final ResponseData p1, final String p2, final String p3, final int p4);
        
        void writeToCache(final String p0, final Context p1, final String p2);
    }
}
