package org.mapsforge.map.reader;

import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCacheEntryKey {
   private final int hashCodeValue;
   private final long indexBlockNumber;
   private final SubFileParameter subFileParameter;

   IndexCacheEntryKey(SubFileParameter var1, long var2) {
      this.subFileParameter = var1;
      this.indexBlockNumber = var2;
      this.hashCodeValue = this.calculateHashCode();
   }

   private int calculateHashCode() {
      int var1;
      if (this.subFileParameter == null) {
         var1 = 0;
      } else {
         var1 = this.subFileParameter.hashCode();
      }

      return (var1 + 217) * 31 + (int)(this.indexBlockNumber ^ this.indexBlockNumber >>> 32);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof IndexCacheEntryKey)) {
            var2 = false;
         } else {
            IndexCacheEntryKey var3 = (IndexCacheEntryKey)var1;
            if (this.subFileParameter == null && var3.subFileParameter != null) {
               var2 = false;
            } else if (this.subFileParameter != null && !this.subFileParameter.equals(var3.subFileParameter)) {
               var2 = false;
            } else if (this.indexBlockNumber != var3.indexBlockNumber) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.hashCodeValue;
   }
}
