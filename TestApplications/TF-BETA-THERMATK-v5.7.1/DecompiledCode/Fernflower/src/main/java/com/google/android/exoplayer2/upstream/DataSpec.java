package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

public final class DataSpec {
   public final long absoluteStreamPosition;
   public final int flags;
   public final byte[] httpBody;
   public final int httpMethod;
   public final String key;
   public final long length;
   public final long position;
   @Deprecated
   public final byte[] postBody;
   public final Uri uri;

   public DataSpec(Uri var1, int var2) {
      this(var1, 0L, -1L, (String)null, var2);
   }

   public DataSpec(Uri var1, int var2, byte[] var3, long var4, long var6, long var8, String var10, int var11) {
      boolean var12 = true;
      boolean var13;
      if (var4 >= 0L) {
         var13 = true;
      } else {
         var13 = false;
      }

      Assertions.checkArgument(var13);
      if (var6 >= 0L) {
         var13 = true;
      } else {
         var13 = false;
      }

      Assertions.checkArgument(var13);
      var13 = var12;
      if (var8 <= 0L) {
         if (var8 == -1L) {
            var13 = var12;
         } else {
            var13 = false;
         }
      }

      Assertions.checkArgument(var13);
      this.uri = var1;
      this.httpMethod = var2;
      if (var3 == null || var3.length == 0) {
         var3 = null;
      }

      this.httpBody = var3;
      this.postBody = this.httpBody;
      this.absoluteStreamPosition = var4;
      this.position = var6;
      this.length = var8;
      this.key = var10;
      this.flags = var11;
   }

   public DataSpec(Uri var1, long var2, long var4, long var6, String var8, int var9) {
      this(var1, (byte[])null, var2, var4, var6, var8, var9);
   }

   public DataSpec(Uri var1, long var2, long var4, String var6) {
      this(var1, var2, var2, var4, var6, 0);
   }

   public DataSpec(Uri var1, long var2, long var4, String var6, int var7) {
      this(var1, var2, var2, var4, var6, var7);
   }

   public DataSpec(Uri var1, byte[] var2, long var3, long var5, long var7, String var9, int var10) {
      byte var11;
      if (var2 != null) {
         var11 = 2;
      } else {
         var11 = 1;
      }

      this(var1, var11, var2, var3, var5, var7, var9, var10);
   }

   public static String getStringForHttpMethod(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 == 3) {
               return "HEAD";
            } else {
               throw new AssertionError(var0);
            }
         } else {
            return "POST";
         }
      } else {
         return "GET";
      }
   }

   public final String getHttpMethodString() {
      return getStringForHttpMethod(this.httpMethod);
   }

   public boolean isFlagSet(int var1) {
      boolean var2;
      if ((this.flags & var1) == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public DataSpec subrange(long var1) {
      long var3 = this.length;
      long var5 = -1L;
      if (var3 != -1L) {
         var5 = var3 - var1;
      }

      return this.subrange(var1, var5);
   }

   public DataSpec subrange(long var1, long var3) {
      return var1 == 0L && this.length == var3 ? this : new DataSpec(this.uri, this.httpMethod, this.httpBody, this.absoluteStreamPosition + var1, this.position + var1, var3, this.key, this.flags);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DataSpec[");
      var1.append(this.getHttpMethodString());
      var1.append(" ");
      var1.append(this.uri);
      var1.append(", ");
      var1.append(Arrays.toString(this.httpBody));
      var1.append(", ");
      var1.append(this.absoluteStreamPosition);
      var1.append(", ");
      var1.append(this.position);
      var1.append(", ");
      var1.append(this.length);
      var1.append(", ");
      var1.append(this.key);
      var1.append(", ");
      var1.append(this.flags);
      var1.append("]");
      return var1.toString();
   }
}
