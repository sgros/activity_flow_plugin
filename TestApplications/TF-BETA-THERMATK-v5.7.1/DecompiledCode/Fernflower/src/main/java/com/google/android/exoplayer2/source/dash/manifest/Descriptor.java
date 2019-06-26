package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.Util;

public final class Descriptor {
   public final String id;
   public final String schemeIdUri;
   public final String value;

   public Descriptor(String var1, String var2, String var3) {
      this.schemeIdUri = var1;
      this.value = var2;
      this.id = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && Descriptor.class == var1.getClass()) {
         Descriptor var3 = (Descriptor)var1;
         if (!Util.areEqual(this.schemeIdUri, var3.schemeIdUri) || !Util.areEqual(this.value, var3.value) || !Util.areEqual(this.id, var3.id)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.schemeIdUri;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.value;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.id;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return (var3 * 31 + var4) * 31 + var2;
   }
}
