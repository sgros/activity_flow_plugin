package org.mozilla.cachedrequestloader;

import android.content.Context;

public class RequestLoaderDelegation {
    private Context context;
    private boolean forceNetwork;
    private RequestLoader requestLoader;
    private int socketTag;
    private ResponseData stringLiveData;
    private String subscriptionKey;
    private String subscriptionUrl;
    private String userAgent;

    interface RequestLoader {
        void deleteCache(Context context, String str);

        void loadFromCache(Context context, String str, ResponseData responseData);

        void loadFromRemote(Context context, ResponseData responseData, String str, String str2, int i);

        void writeToCache(String str, Context context, String str2);
    }

    RequestLoaderDelegation(Context context, String str, String str2, String str3, int i, boolean z, RequestLoader requestLoader) {
        this.context = context;
        this.subscriptionKey = str;
        this.subscriptionUrl = str2;
        this.userAgent = str3;
        this.socketTag = i;
        this.forceNetwork = z;
        this.requestLoader = requestLoader;
    }

    /* Access modifiers changed, original: 0000 */
    public ResponseData getStringLiveData() {
        if (this.stringLiveData == null) {
            this.stringLiveData = new ResponseData();
            if (!this.forceNetwork) {
                this.requestLoader.loadFromCache(this.context, this.subscriptionKey, this.stringLiveData);
            }
            this.requestLoader.loadFromRemote(this.context, this.stringLiveData, this.subscriptionUrl, this.userAgent, this.socketTag);
        }
        return this.stringLiveData;
    }

    /* Access modifiers changed, original: 0000 */
    public void writeToCache(String str) {
        this.requestLoader.writeToCache(str, this.context, this.subscriptionKey);
    }

    /* Access modifiers changed, original: 0000 */
    public void deleteCache() {
        this.requestLoader.deleteCache(this.context, this.subscriptionKey);
    }
}
