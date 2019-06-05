package org.mapsforge.map.rendertheme.rule;

import java.util.List;

class MatchingCacheKey {
   private final Closed closed;
   private final List tags;
   private final byte zoomLevel;

   MatchingCacheKey(List var1, byte var2, Closed var3) {
      this.tags = var1;
      this.zoomLevel = (byte)var2;
      this.closed = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof MatchingCacheKey)) {
            var2 = false;
         } else {
            MatchingCacheKey var3 = (MatchingCacheKey)var1;
            if (this.closed != var3.closed) {
               var2 = false;
            } else {
               if (this.tags == null) {
                  if (var3.tags != null) {
                     var2 = false;
                     return var2;
                  }
               } else if (!this.tags.equals(var3.tags)) {
                  var2 = false;
                  return var2;
               }

               if (this.zoomLevel != var3.zoomLevel) {
                  var2 = false;
               }
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      int var1 = 0;
      int var2;
      if (this.closed == null) {
         var2 = 0;
      } else {
         var2 = this.closed.hashCode();
      }

      if (this.tags != null) {
         var1 = this.tags.hashCode();
      }

      return ((var2 + 31) * 31 + var1) * 31 + this.zoomLevel;
   }
}
