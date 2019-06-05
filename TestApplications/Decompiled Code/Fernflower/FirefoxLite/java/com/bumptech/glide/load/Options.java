package com.bumptech.glide.load;

import android.support.v4.util.ArrayMap;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map.Entry;

public final class Options implements Key {
   private final ArrayMap values = new ArrayMap();

   private static void updateDiskCacheKey(Option var0, Object var1, MessageDigest var2) {
      var0.update(var1, var2);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Options) {
         Options var2 = (Options)var1;
         return this.values.equals(var2.values);
      } else {
         return false;
      }
   }

   public Object get(Option var1) {
      Object var2;
      if (this.values.containsKey(var1)) {
         var2 = this.values.get(var1);
      } else {
         var2 = var1.getDefaultValue();
      }

      return var2;
   }

   public int hashCode() {
      return this.values.hashCode();
   }

   public void putAll(Options var1) {
      this.values.putAll(var1.values);
   }

   public Options set(Option var1, Object var2) {
      this.values.put(var1, var2);
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Options{values=");
      var1.append(this.values);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      Iterator var2 = this.values.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         updateDiskCacheKey((Option)var3.getKey(), var3.getValue(), var1);
      }

   }
}
