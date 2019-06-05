package okhttp3;

import java.util.concurrent.TimeUnit;
import okhttp3.internal.http.HttpHeaders;

public final class CacheControl {
   public static final CacheControl FORCE_CACHE;
   public static final CacheControl FORCE_NETWORK = (new CacheControl.Builder()).noCache().build();
   String headerValue;
   private final boolean isPrivate;
   private final boolean isPublic;
   private final int maxAgeSeconds;
   private final int maxStaleSeconds;
   private final int minFreshSeconds;
   private final boolean mustRevalidate;
   private final boolean noCache;
   private final boolean noStore;
   private final boolean noTransform;
   private final boolean onlyIfCached;
   private final int sMaxAgeSeconds;

   static {
      FORCE_CACHE = (new CacheControl.Builder()).onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
   }

   CacheControl(CacheControl.Builder var1) {
      this.noCache = var1.noCache;
      this.noStore = var1.noStore;
      this.maxAgeSeconds = var1.maxAgeSeconds;
      this.sMaxAgeSeconds = -1;
      this.isPrivate = false;
      this.isPublic = false;
      this.mustRevalidate = false;
      this.maxStaleSeconds = var1.maxStaleSeconds;
      this.minFreshSeconds = var1.minFreshSeconds;
      this.onlyIfCached = var1.onlyIfCached;
      this.noTransform = var1.noTransform;
   }

   private CacheControl(boolean var1, boolean var2, int var3, int var4, boolean var5, boolean var6, boolean var7, int var8, int var9, boolean var10, boolean var11, String var12) {
      this.noCache = var1;
      this.noStore = var2;
      this.maxAgeSeconds = var3;
      this.sMaxAgeSeconds = var4;
      this.isPrivate = var5;
      this.isPublic = var6;
      this.mustRevalidate = var7;
      this.maxStaleSeconds = var8;
      this.minFreshSeconds = var9;
      this.onlyIfCached = var10;
      this.noTransform = var11;
      this.headerValue = var12;
   }

   private String headerValue() {
      StringBuilder var1 = new StringBuilder();
      if (this.noCache) {
         var1.append("no-cache, ");
      }

      if (this.noStore) {
         var1.append("no-store, ");
      }

      if (this.maxAgeSeconds != -1) {
         var1.append("max-age=").append(this.maxAgeSeconds).append(", ");
      }

      if (this.sMaxAgeSeconds != -1) {
         var1.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
      }

      if (this.isPrivate) {
         var1.append("private, ");
      }

      if (this.isPublic) {
         var1.append("public, ");
      }

      if (this.mustRevalidate) {
         var1.append("must-revalidate, ");
      }

      if (this.maxStaleSeconds != -1) {
         var1.append("max-stale=").append(this.maxStaleSeconds).append(", ");
      }

      if (this.minFreshSeconds != -1) {
         var1.append("min-fresh=").append(this.minFreshSeconds).append(", ");
      }

      if (this.onlyIfCached) {
         var1.append("only-if-cached, ");
      }

      if (this.noTransform) {
         var1.append("no-transform, ");
      }

      String var2;
      if (var1.length() == 0) {
         var2 = "";
      } else {
         var1.delete(var1.length() - 2, var1.length());
         var2 = var1.toString();
      }

      return var2;
   }

   public static CacheControl parse(Headers var0) {
      boolean var1 = false;
      boolean var2 = false;
      int var3 = -1;
      int var4 = -1;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      int var8 = -1;
      int var9 = -1;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = true;
      String var13 = null;
      int var14 = 0;

      boolean var31;
      for(int var15 = var0.size(); var14 < var15; var12 = var31) {
         boolean var19;
         boolean var20;
         int var21;
         int var22;
         boolean var23;
         boolean var24;
         boolean var25;
         int var26;
         int var27;
         boolean var28;
         boolean var29;
         String var30;
         label85: {
            String var16 = var0.name(var14);
            String var17 = var0.value(var14);
            if (var16.equalsIgnoreCase("Cache-Control")) {
               if (var13 != null) {
                  var12 = false;
               } else {
                  var13 = var17;
               }
            } else {
               var19 = var1;
               var20 = var2;
               var21 = var3;
               var22 = var4;
               var23 = var5;
               var24 = var6;
               var25 = var7;
               var26 = var8;
               var27 = var9;
               var28 = var10;
               var29 = var11;
               var30 = var13;
               var31 = var12;
               if (!var16.equalsIgnoreCase("Pragma")) {
                  break label85;
               }

               var12 = false;
            }

            int var18 = 0;

            while(true) {
               var19 = var1;
               var20 = var2;
               var21 = var3;
               var22 = var4;
               var23 = var5;
               var24 = var6;
               var25 = var7;
               var26 = var8;
               var27 = var9;
               var28 = var10;
               var29 = var11;
               var30 = var13;
               var31 = var12;
               if (var18 >= var17.length()) {
                  break;
               }

               var22 = HttpHeaders.skipUntil(var17, var18, "=,;");
               var16 = var17.substring(var18, var22).trim();
               if (var22 != var17.length() && var17.charAt(var22) != ',' && var17.charAt(var22) != ';') {
                  var27 = HttpHeaders.skipWhitespace(var17, var22 + 1);
                  if (var27 < var17.length() && var17.charAt(var27) == '"') {
                     ++var27;
                     var22 = HttpHeaders.skipUntil(var17, var27, "\"");
                     var30 = var17.substring(var27, var22);
                     ++var22;
                  } else {
                     var22 = HttpHeaders.skipUntil(var17, var27, ",;");
                     var30 = var17.substring(var27, var22).trim();
                  }
               } else {
                  ++var22;
                  var30 = null;
               }

               if ("no-cache".equalsIgnoreCase(var16)) {
                  var1 = true;
                  var18 = var22;
               } else if ("no-store".equalsIgnoreCase(var16)) {
                  var2 = true;
                  var18 = var22;
               } else if ("max-age".equalsIgnoreCase(var16)) {
                  var3 = HttpHeaders.parseSeconds(var30, -1);
                  var18 = var22;
               } else if ("s-maxage".equalsIgnoreCase(var16)) {
                  var4 = HttpHeaders.parseSeconds(var30, -1);
                  var18 = var22;
               } else if ("private".equalsIgnoreCase(var16)) {
                  var5 = true;
                  var18 = var22;
               } else if ("public".equalsIgnoreCase(var16)) {
                  var6 = true;
                  var18 = var22;
               } else if ("must-revalidate".equalsIgnoreCase(var16)) {
                  var7 = true;
                  var18 = var22;
               } else if ("max-stale".equalsIgnoreCase(var16)) {
                  var8 = HttpHeaders.parseSeconds(var30, Integer.MAX_VALUE);
                  var18 = var22;
               } else if ("min-fresh".equalsIgnoreCase(var16)) {
                  var9 = HttpHeaders.parseSeconds(var30, -1);
                  var18 = var22;
               } else if ("only-if-cached".equalsIgnoreCase(var16)) {
                  var10 = true;
                  var18 = var22;
               } else {
                  var18 = var22;
                  if ("no-transform".equalsIgnoreCase(var16)) {
                     var11 = true;
                     var18 = var22;
                  }
               }
            }
         }

         ++var14;
         var1 = var19;
         var2 = var20;
         var3 = var21;
         var4 = var22;
         var5 = var23;
         var6 = var24;
         var7 = var25;
         var8 = var26;
         var9 = var27;
         var10 = var28;
         var11 = var29;
         var13 = var30;
      }

      if (!var12) {
         var13 = null;
      }

      return new CacheControl(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var13);
   }

   public boolean isPrivate() {
      return this.isPrivate;
   }

   public boolean isPublic() {
      return this.isPublic;
   }

   public int maxAgeSeconds() {
      return this.maxAgeSeconds;
   }

   public int maxStaleSeconds() {
      return this.maxStaleSeconds;
   }

   public int minFreshSeconds() {
      return this.minFreshSeconds;
   }

   public boolean mustRevalidate() {
      return this.mustRevalidate;
   }

   public boolean noCache() {
      return this.noCache;
   }

   public boolean noStore() {
      return this.noStore;
   }

   public boolean noTransform() {
      return this.noTransform;
   }

   public boolean onlyIfCached() {
      return this.onlyIfCached;
   }

   public int sMaxAgeSeconds() {
      return this.sMaxAgeSeconds;
   }

   public String toString() {
      String var1 = this.headerValue;
      if (var1 == null) {
         var1 = this.headerValue();
         this.headerValue = var1;
      }

      return var1;
   }

   public static final class Builder {
      int maxAgeSeconds = -1;
      int maxStaleSeconds = -1;
      int minFreshSeconds = -1;
      boolean noCache;
      boolean noStore;
      boolean noTransform;
      boolean onlyIfCached;

      public CacheControl build() {
         return new CacheControl(this);
      }

      public CacheControl.Builder maxAge(int var1, TimeUnit var2) {
         if (var1 < 0) {
            throw new IllegalArgumentException("maxAge < 0: " + var1);
         } else {
            long var3 = var2.toSeconds((long)var1);
            if (var3 > 2147483647L) {
               var1 = Integer.MAX_VALUE;
            } else {
               var1 = (int)var3;
            }

            this.maxAgeSeconds = var1;
            return this;
         }
      }

      public CacheControl.Builder maxStale(int var1, TimeUnit var2) {
         if (var1 < 0) {
            throw new IllegalArgumentException("maxStale < 0: " + var1);
         } else {
            long var3 = var2.toSeconds((long)var1);
            if (var3 > 2147483647L) {
               var1 = Integer.MAX_VALUE;
            } else {
               var1 = (int)var3;
            }

            this.maxStaleSeconds = var1;
            return this;
         }
      }

      public CacheControl.Builder minFresh(int var1, TimeUnit var2) {
         if (var1 < 0) {
            throw new IllegalArgumentException("minFresh < 0: " + var1);
         } else {
            long var3 = var2.toSeconds((long)var1);
            if (var3 > 2147483647L) {
               var1 = Integer.MAX_VALUE;
            } else {
               var1 = (int)var3;
            }

            this.minFreshSeconds = var1;
            return this;
         }
      }

      public CacheControl.Builder noCache() {
         this.noCache = true;
         return this;
      }

      public CacheControl.Builder noStore() {
         this.noStore = true;
         return this;
      }

      public CacheControl.Builder noTransform() {
         this.noTransform = true;
         return this;
      }

      public CacheControl.Builder onlyIfCached() {
         this.onlyIfCached = true;
         return this;
      }
   }
}
