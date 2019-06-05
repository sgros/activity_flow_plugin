package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EncoderRegistry {
   private final List encoders = new ArrayList();

   public void append(Class var1, Encoder var2) {
      synchronized(this){}

      try {
         List var3 = this.encoders;
         EncoderRegistry.Entry var4 = new EncoderRegistry.Entry(var1, var2);
         var3.add(var4);
      } finally {
         ;
      }

   }

   public Encoder getEncoder(Class var1) {
      synchronized(this){}

      try {
         Iterator var2 = this.encoders.iterator();

         while(var2.hasNext()) {
            EncoderRegistry.Entry var3 = (EncoderRegistry.Entry)var2.next();
            if (var3.handles(var1)) {
               Encoder var6 = var3.encoder;
               return var6;
            }
         }
      } finally {
         ;
      }

      return null;
   }

   private static final class Entry {
      private final Class dataClass;
      final Encoder encoder;

      public Entry(Class var1, Encoder var2) {
         this.dataClass = var1;
         this.encoder = var2;
      }

      public boolean handles(Class var1) {
         return this.dataClass.isAssignableFrom(var1);
      }
   }
}
