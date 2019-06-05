package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

public class GlideUrl implements Key {
   private volatile byte[] cacheKeyBytes;
   private int hashCode;
   private final Headers headers;
   private String safeStringUrl;
   private URL safeUrl;
   private final String stringUrl;
   private final URL url;

   public GlideUrl(String var1) {
      this(var1, Headers.DEFAULT);
   }

   public GlideUrl(String var1, Headers var2) {
      this.url = null;
      this.stringUrl = Preconditions.checkNotEmpty(var1);
      this.headers = (Headers)Preconditions.checkNotNull(var2);
   }

   public GlideUrl(URL var1) {
      this(var1, Headers.DEFAULT);
   }

   public GlideUrl(URL var1, Headers var2) {
      this.url = (URL)Preconditions.checkNotNull(var1);
      this.stringUrl = null;
      this.headers = (Headers)Preconditions.checkNotNull(var2);
   }

   private byte[] getCacheKeyBytes() {
      if (this.cacheKeyBytes == null) {
         this.cacheKeyBytes = this.getCacheKey().getBytes(CHARSET);
      }

      return this.cacheKeyBytes;
   }

   private String getSafeStringUrl() {
      if (TextUtils.isEmpty(this.safeStringUrl)) {
         String var1 = this.stringUrl;
         String var2 = var1;
         if (TextUtils.isEmpty(var1)) {
            var2 = this.url.toString();
         }

         this.safeStringUrl = Uri.encode(var2, "@#&=*+-_.,:!?()/~'%");
      }

      return this.safeStringUrl;
   }

   private URL getSafeUrl() throws MalformedURLException {
      if (this.safeUrl == null) {
         this.safeUrl = new URL(this.getSafeStringUrl());
      }

      return this.safeUrl;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof GlideUrl;
      boolean var3 = false;
      if (var2) {
         GlideUrl var4 = (GlideUrl)var1;
         var2 = var3;
         if (this.getCacheKey().equals(var4.getCacheKey())) {
            var2 = var3;
            if (this.headers.equals(var4.headers)) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public String getCacheKey() {
      String var1;
      if (this.stringUrl != null) {
         var1 = this.stringUrl;
      } else {
         var1 = this.url.toString();
      }

      return var1;
   }

   public Map getHeaders() {
      return this.headers.getHeaders();
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = this.getCacheKey().hashCode();
         this.hashCode = this.hashCode * 31 + this.headers.hashCode();
      }

      return this.hashCode;
   }

   public String toString() {
      return this.getCacheKey();
   }

   public URL toURL() throws MalformedURLException {
      return this.getSafeUrl();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(this.getCacheKeyBytes());
   }
}
