package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;
import java.util.Map;

class EngineKey implements Key {
   private int hashCode;
   private final int height;
   private final Object model;
   private final Options options;
   private final Class resourceClass;
   private final Key signature;
   private final Class transcodeClass;
   private final Map transformations;
   private final int width;

   public EngineKey(Object var1, Key var2, int var3, int var4, Map var5, Class var6, Class var7, Options var8) {
      this.model = Preconditions.checkNotNull(var1);
      this.signature = (Key)Preconditions.checkNotNull(var2, "Signature must not be null");
      this.width = var3;
      this.height = var4;
      this.transformations = (Map)Preconditions.checkNotNull(var5);
      this.resourceClass = (Class)Preconditions.checkNotNull(var6, "Resource class must not be null");
      this.transcodeClass = (Class)Preconditions.checkNotNull(var7, "Transcode class must not be null");
      this.options = (Options)Preconditions.checkNotNull(var8);
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof EngineKey;
      boolean var3 = false;
      if (var2) {
         EngineKey var4 = (EngineKey)var1;
         var2 = var3;
         if (this.model.equals(var4.model)) {
            var2 = var3;
            if (this.signature.equals(var4.signature)) {
               var2 = var3;
               if (this.height == var4.height) {
                  var2 = var3;
                  if (this.width == var4.width) {
                     var2 = var3;
                     if (this.transformations.equals(var4.transformations)) {
                        var2 = var3;
                        if (this.resourceClass.equals(var4.resourceClass)) {
                           var2 = var3;
                           if (this.transcodeClass.equals(var4.transcodeClass)) {
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
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = this.model.hashCode();
         this.hashCode = this.hashCode * 31 + this.signature.hashCode();
         this.hashCode = this.hashCode * 31 + this.width;
         this.hashCode = this.hashCode * 31 + this.height;
         this.hashCode = this.hashCode * 31 + this.transformations.hashCode();
         this.hashCode = this.hashCode * 31 + this.resourceClass.hashCode();
         this.hashCode = this.hashCode * 31 + this.transcodeClass.hashCode();
         this.hashCode = this.hashCode * 31 + this.options.hashCode();
      }

      return this.hashCode;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("EngineKey{model=");
      var1.append(this.model);
      var1.append(", width=");
      var1.append(this.width);
      var1.append(", height=");
      var1.append(this.height);
      var1.append(", resourceClass=");
      var1.append(this.resourceClass);
      var1.append(", transcodeClass=");
      var1.append(this.transcodeClass);
      var1.append(", signature=");
      var1.append(this.signature);
      var1.append(", hashCode=");
      var1.append(this.hashCode);
      var1.append(", transformations=");
      var1.append(this.transformations);
      var1.append(", options=");
      var1.append(this.options);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      throw new UnsupportedOperationException();
   }
}
