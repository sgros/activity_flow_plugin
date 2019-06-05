package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MultiTransformation implements Transformation {
   private final Collection transformations;

   @SafeVarargs
   public MultiTransformation(Transformation... var1) {
      if (var1.length >= 1) {
         this.transformations = Arrays.asList(var1);
      } else {
         throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
      }
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MultiTransformation) {
         MultiTransformation var2 = (MultiTransformation)var1;
         return this.transformations.equals(var2.transformations);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.transformations.hashCode();
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      Iterator var5 = this.transformations.iterator();

      Resource var6;
      Resource var7;
      for(var6 = var2; var5.hasNext(); var6 = var7) {
         var7 = ((Transformation)var5.next()).transform(var1, var6, var3, var4);
         if (var6 != null && !var6.equals(var2) && !var6.equals(var7)) {
            var6.recycle();
         }
      }

      return var6;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      Iterator var2 = this.transformations.iterator();

      while(var2.hasNext()) {
         ((Transformation)var2.next()).updateDiskCacheKey(var1);
      }

   }
}
