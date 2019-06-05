package com.bumptech.glide.load.resource.transcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TranscoderRegistry {
   private final List transcoders = new ArrayList();

   public ResourceTranscoder get(Class var1, Class var2) {
      synchronized(this){}

      ResourceTranscoder var7;
      try {
         if (!var2.isAssignableFrom(var1)) {
            Iterator var3 = this.transcoders.iterator();

            TranscoderRegistry.Entry var4;
            do {
               if (!var3.hasNext()) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("No transcoder registered to transcode from ");
                  var9.append(var1);
                  var9.append(" to ");
                  var9.append(var2);
                  IllegalArgumentException var8 = new IllegalArgumentException(var9.toString());
                  throw var8;
               }

               var4 = (TranscoderRegistry.Entry)var3.next();
            } while(!var4.handles(var1, var2));

            var7 = var4.transcoder;
            return var7;
         }

         var7 = UnitTranscoder.get();
      } finally {
         ;
      }

      return var7;
   }

   public List getTranscodeClasses(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label187: {
         boolean var10001;
         ArrayList var3;
         try {
            var3 = new ArrayList();
            if (var2.isAssignableFrom(var1)) {
               var3.add(var2);
               return var3;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label187;
         }

         Iterator var4;
         try {
            var4 = this.transcoders.iterator();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label187;
         }

         while(true) {
            try {
               while(var4.hasNext()) {
                  if (((TranscoderRegistry.Entry)var4.next()).handles(var1, var2)) {
                     var3.add(var2);
                  }
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }

            return var3;
         }
      }

      Throwable var17 = var10000;
      throw var17;
   }

   public void register(Class var1, Class var2, ResourceTranscoder var3) {
      synchronized(this){}

      try {
         List var4 = this.transcoders;
         TranscoderRegistry.Entry var5 = new TranscoderRegistry.Entry(var1, var2, var3);
         var4.add(var5);
      } finally {
         ;
      }

   }

   private static final class Entry {
      private final Class fromClass;
      private final Class toClass;
      final ResourceTranscoder transcoder;

      Entry(Class var1, Class var2, ResourceTranscoder var3) {
         this.fromClass = var1;
         this.toClass = var2;
         this.transcoder = var3;
      }

      public boolean handles(Class var1, Class var2) {
         boolean var3;
         if (this.fromClass.isAssignableFrom(var1) && var2.isAssignableFrom(this.toClass)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
