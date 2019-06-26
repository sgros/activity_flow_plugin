package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.util.UriUtil;

public final class RangedUri {
   private int hashCode;
   public final long length;
   private final String referenceUri;
   public final long start;

   public RangedUri(String var1, long var2, long var4) {
      String var6 = var1;
      if (var1 == null) {
         var6 = "";
      }

      this.referenceUri = var6;
      this.start = var2;
      this.length = var4;
   }

   public RangedUri attemptMerge(RangedUri var1, String var2) {
      String var3 = this.resolveUriString(var2);
      if (var1 != null && var3.equals(var1.resolveUriString(var2))) {
         long var4 = this.length;
         long var6 = -1L;
         long var8;
         long var10;
         if (var4 != -1L) {
            var8 = this.start;
            if (var8 + var4 == var1.start) {
               var10 = var1.length;
               if (var10 != -1L) {
                  var6 = var4 + var10;
               }

               return new RangedUri(var3, var8, var6);
            }
         }

         var4 = var1.length;
         if (var4 != -1L) {
            var8 = var1.start;
            if (var8 + var4 == this.start) {
               var10 = this.length;
               if (var10 != -1L) {
                  var6 = var4 + var10;
               }

               return new RangedUri(var3, var8, var6);
            }
         }
      }

      return null;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && RangedUri.class == var1.getClass()) {
         RangedUri var3 = (RangedUri)var1;
         if (this.start != var3.start || this.length != var3.length || !this.referenceUri.equals(var3.referenceUri)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = ((527 + (int)this.start) * 31 + (int)this.length) * 31 + this.referenceUri.hashCode();
      }

      return this.hashCode;
   }

   public Uri resolveUri(String var1) {
      return UriUtil.resolveToUri(var1, this.referenceUri);
   }

   public String resolveUriString(String var1) {
      return UriUtil.resolve(var1, this.referenceUri);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RangedUri(referenceUri=");
      var1.append(this.referenceUri);
      var1.append(", start=");
      var1.append(this.start);
      var1.append(", length=");
      var1.append(this.length);
      var1.append(")");
      return var1.toString();
   }
}
