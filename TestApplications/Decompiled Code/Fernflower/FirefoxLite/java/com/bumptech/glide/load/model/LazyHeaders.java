package com.bumptech.glide.load.model;

import android.text.TextUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class LazyHeaders implements Headers {
   private volatile Map combinedHeaders;
   private final Map headers;

   LazyHeaders(Map var1) {
      this.headers = Collections.unmodifiableMap(var1);
   }

   private Map generateHeaders() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.headers.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         StringBuilder var4 = new StringBuilder();
         List var5 = (List)var3.getValue();
         int var6 = var5.size();

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = ((LazyHeaderFactory)var5.get(var7)).buildHeader();
            if (!TextUtils.isEmpty(var8)) {
               var4.append(var8);
               if (var7 != var5.size() - 1) {
                  var4.append(',');
               }
            }
         }

         if (!TextUtils.isEmpty(var4.toString())) {
            var1.put(var3.getKey(), var4.toString());
         }
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof LazyHeaders) {
         LazyHeaders var2 = (LazyHeaders)var1;
         return this.headers.equals(var2.headers);
      } else {
         return false;
      }
   }

   public Map getHeaders() {
      if (this.combinedHeaders == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.combinedHeaders == null) {
                  this.combinedHeaders = Collections.unmodifiableMap(this.generateHeaders());
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return this.combinedHeaders;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      } else {
         return this.combinedHeaders;
      }
   }

   public int hashCode() {
      return this.headers.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("LazyHeaders{headers=");
      var1.append(this.headers);
      var1.append('}');
      return var1.toString();
   }

   public static final class Builder {
      private static final Map DEFAULT_HEADERS;
      private static final String DEFAULT_USER_AGENT = getSanitizedUserAgent();
      private boolean copyOnModify = true;
      private Map headers;
      private boolean isUserAgentDefault;

      static {
         HashMap var0 = new HashMap(2);
         if (!TextUtils.isEmpty(DEFAULT_USER_AGENT)) {
            var0.put("User-Agent", Collections.singletonList(new LazyHeaders.StringHeaderFactory(DEFAULT_USER_AGENT)));
         }

         DEFAULT_HEADERS = Collections.unmodifiableMap(var0);
      }

      public Builder() {
         this.headers = DEFAULT_HEADERS;
         this.isUserAgentDefault = true;
      }

      static String getSanitizedUserAgent() {
         String var0 = System.getProperty("http.agent");
         if (TextUtils.isEmpty(var0)) {
            return var0;
         } else {
            int var1 = var0.length();
            StringBuilder var2 = new StringBuilder(var0.length());

            for(int var3 = 0; var3 < var1; ++var3) {
               char var4 = var0.charAt(var3);
               if ((var4 > 31 || var4 == '\t') && var4 < 127) {
                  var2.append(var4);
               } else {
                  var2.append('?');
               }
            }

            return var2.toString();
         }
      }

      public LazyHeaders build() {
         this.copyOnModify = true;
         return new LazyHeaders(this.headers);
      }
   }

   static final class StringHeaderFactory implements LazyHeaderFactory {
      private final String value;

      StringHeaderFactory(String var1) {
         this.value = var1;
      }

      public String buildHeader() {
         return this.value;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof LazyHeaders.StringHeaderFactory) {
            LazyHeaders.StringHeaderFactory var2 = (LazyHeaders.StringHeaderFactory)var1;
            return this.value.equals(var2.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value.hashCode();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("StringHeaderFactory{value='");
         var1.append(this.value);
         var1.append('\'');
         var1.append('}');
         return var1.toString();
      }
   }
}
