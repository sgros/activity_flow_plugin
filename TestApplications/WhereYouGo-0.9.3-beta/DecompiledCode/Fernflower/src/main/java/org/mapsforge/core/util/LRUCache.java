package org.mapsforge.core.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache extends LinkedHashMap {
   private static final float LOAD_FACTOR = 0.6F;
   private static final long serialVersionUID = 1L;
   private final int capacity;

   public LRUCache(int var1) {
      super(calculateInitialCapacity(var1), 0.6F, true);
      this.capacity = var1;
   }

   private static int calculateInitialCapacity(int var0) {
      if (var0 < 0) {
         throw new IllegalArgumentException("capacity must not be negative: " + var0);
      } else {
         return (int)((float)var0 / 0.6F) + 2;
      }
   }

   protected boolean removeEldestEntry(Entry var1) {
      boolean var2;
      if (this.size() > this.capacity) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
