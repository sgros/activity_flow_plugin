package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
   final List encoders = new ArrayList();

   public void append(Class var1, ResourceEncoder var2) {
      synchronized(this){}

      try {
         List var3 = this.encoders;
         ResourceEncoderRegistry.Entry var4 = new ResourceEncoderRegistry.Entry(var1, var2);
         var3.add(var4);
      } finally {
         ;
      }

   }

   public ResourceEncoder get(Class var1) {
      synchronized(this){}

      Throwable var10000;
      label110: {
         boolean var10001;
         int var2;
         try {
            var2 = this.encoders.size();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label110;
         }

         int var3 = 0;

         while(true) {
            if (var3 >= var2) {
               return null;
            }

            try {
               ResourceEncoderRegistry.Entry var4 = (ResourceEncoderRegistry.Entry)this.encoders.get(var3);
               if (var4.handles(var1)) {
                  ResourceEncoder var12 = var4.encoder;
                  return var12;
               }
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }

            ++var3;
         }
      }

      Throwable var11 = var10000;
      throw var11;
   }

   private static final class Entry {
      final ResourceEncoder encoder;
      private final Class resourceClass;

      Entry(Class var1, ResourceEncoder var2) {
         this.resourceClass = var1;
         this.encoder = var2;
      }

      boolean handles(Class var1) {
         return this.resourceClass.isAssignableFrom(var1);
      }
   }
}
