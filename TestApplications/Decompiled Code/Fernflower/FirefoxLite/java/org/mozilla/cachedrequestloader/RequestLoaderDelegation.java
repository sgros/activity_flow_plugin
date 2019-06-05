package org.mozilla.cachedrequestloader;

import android.content.Context;

public class RequestLoaderDelegation {
   private Context context;
   private boolean forceNetwork;
   private RequestLoaderDelegation.RequestLoader requestLoader;
   private int socketTag;
   private ResponseData stringLiveData;
   private String subscriptionKey;
   private String subscriptionUrl;
   private String userAgent;

   RequestLoaderDelegation(Context var1, String var2, String var3, String var4, int var5, boolean var6, RequestLoaderDelegation.RequestLoader var7) {
      this.context = var1;
      this.subscriptionKey = var2;
      this.subscriptionUrl = var3;
      this.userAgent = var4;
      this.socketTag = var5;
      this.forceNetwork = var6;
      this.requestLoader = var7;
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

   void writeToCache(String var1) {
      this.requestLoader.writeToCache(var1, this.context, this.subscriptionKey);
   }

   interface RequestLoader {
      void deleteCache(Context var1, String var2);

      void loadFromCache(Context var1, String var2, ResponseData var3);

      void loadFromRemote(Context var1, ResponseData var2, String var3, String var4, int var5);

      void writeToCache(String var1, Context var2, String var3);
   }
}
