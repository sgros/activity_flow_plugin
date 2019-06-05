package org.mozilla.rocket.content.data;

import kotlin.jvm.internal.Intrinsics;

public final class ShoppingLink {
   private final String image;
   private final String name;
   private final String source;
   private final String url;

   public ShoppingLink(String var1, String var2, String var3, String var4) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      Intrinsics.checkParameterIsNotNull(var2, "name");
      Intrinsics.checkParameterIsNotNull(var3, "image");
      Intrinsics.checkParameterIsNotNull(var4, "source");
      super();
      this.url = var1;
      this.name = var2;
      this.image = var3;
      this.source = var4;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof ShoppingLink)) {
            return false;
         }

         ShoppingLink var2 = (ShoppingLink)var1;
         if (!Intrinsics.areEqual(this.url, var2.url) || !Intrinsics.areEqual(this.name, var2.name) || !Intrinsics.areEqual(this.image, var2.image) || !Intrinsics.areEqual(this.source, var2.source)) {
            return false;
         }
      }

      return true;
   }

   public final String getImage() {
      return this.image;
   }

   public final String getName() {
      return this.name;
   }

   public final String getSource() {
      return this.source;
   }

   public final String getUrl() {
      return this.url;
   }

   public int hashCode() {
      String var1 = this.url;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.name;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.image;
      int var5;
      if (var1 != null) {
         var5 = var1.hashCode();
      } else {
         var5 = 0;
      }

      var1 = this.source;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return ((var3 * 31 + var4) * 31 + var5) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ShoppingLink(url=");
      var1.append(this.url);
      var1.append(", name=");
      var1.append(this.name);
      var1.append(", image=");
      var1.append(this.image);
      var1.append(", source=");
      var1.append(this.source);
      var1.append(")");
      return var1.toString();
   }
}
