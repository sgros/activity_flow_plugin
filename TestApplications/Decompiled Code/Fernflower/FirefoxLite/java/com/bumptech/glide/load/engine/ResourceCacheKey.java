package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

final class ResourceCacheKey implements Key {
   private static final LruCache RESOURCE_CLASS_BYTES = new LruCache(50);
   private final Class decodedResourceClass;
   private final int height;
   private final Options options;
   private final Key signature;
   private final Key sourceKey;
   private final Transformation transformation;
   private final int width;

   public ResourceCacheKey(Key var1, Key var2, int var3, int var4, Transformation var5, Class var6, Options var7) {
      this.sourceKey = var1;
      this.signature = var2;
      this.width = var3;
      this.height = var4;
      this.transformation = var5;
      this.decodedResourceClass = var6;
      this.options = var7;
   }

   private byte[] getResourceClassBytes() {
      byte[] var1 = (byte[])RESOURCE_CLASS_BYTES.get(this.decodedResourceClass);
      byte[] var2 = var1;
      if (var1 == null) {
         var2 = this.decodedResourceClass.getName().getBytes(CHARSET);
         RESOURCE_CLASS_BYTES.put(this.decodedResourceClass, var2);
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof ResourceCacheKey;
      boolean var3 = false;
      if (var2) {
         ResourceCacheKey var4 = (ResourceCacheKey)var1;
         var2 = var3;
         if (this.height == var4.height) {
            var2 = var3;
            if (this.width == var4.width) {
               var2 = var3;
               if (Util.bothNullOrEqual(this.transformation, var4.transformation)) {
                  var2 = var3;
                  if (this.decodedResourceClass.equals(var4.decodedResourceClass)) {
                     var2 = var3;
                     if (this.sourceKey.equals(var4.sourceKey)) {
                        var2 = var3;
                        if (this.signature.equals(var4.signature)) {
                           var2 = var3;
                           if (this.options.equals(var4.options)) {
                              var2 = true;
                           }
                        }
                     }
                  }
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = ((this.sourceKey.hashCode() * 31 + this.signature.hashCode()) * 31 + this.width) * 31 + this.height;
      int var2 = var1;
      if (this.transformation != null) {
         var2 = var1 * 31 + this.transformation.hashCode();
      }

      return (var2 * 31 + this.decodedResourceClass.hashCode()) * 31 + this.options.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ResourceCacheKey{sourceKey=");
      var1.append(this.sourceKey);
      var1.append(", signature=");
      var1.append(this.signature);
      var1.append(", width=");
      var1.append(this.width);
      var1.append(", height=");
      var1.append(this.height);
      var1.append(", decodedResourceClass=");
      var1.append(this.decodedResourceClass);
      var1.append(", transformation='");
      var1.append(this.transformation);
      var1.append('\'');
      var1.append(", options=");
      var1.append(this.options);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      byte[] var2 = ByteBuffer.allocate(8).putInt(this.width).putInt(this.height).array();
      this.signature.updateDiskCacheKey(var1);
      this.sourceKey.updateDiskCacheKey(var1);
      var1.update(var2);
      if (this.transformation != null) {
         this.transformation.updateDiskCacheKey(var1);
      }

      this.options.updateDiskCacheKey(var1);
      var1.update(this.getResourceClassBytes());
   }
}
