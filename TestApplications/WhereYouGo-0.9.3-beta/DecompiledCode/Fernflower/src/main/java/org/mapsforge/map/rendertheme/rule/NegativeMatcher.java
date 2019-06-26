package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class NegativeMatcher implements AttributeMatcher {
   private final List keyList;
   private final List valueList;

   NegativeMatcher(List var1, List var2) {
      this.keyList = var1;
      this.valueList = var2;
   }

   private boolean keyListDoesNotContainKeys(List var1) {
      int var2 = 0;
      int var3 = var1.size();

      boolean var4;
      while(true) {
         if (var2 >= var3) {
            var4 = true;
            break;
         }

         if (this.keyList.contains(((Tag)var1.get(var2)).key)) {
            var4 = false;
            break;
         }

         ++var2;
      }

      return var4;
   }

   public boolean isCoveredBy(AttributeMatcher var1) {
      return false;
   }

   public boolean matches(List var1) {
      boolean var2;
      if (this.keyListDoesNotContainKeys(var1)) {
         var2 = true;
      } else {
         int var3 = 0;
         int var4 = var1.size();

         while(true) {
            if (var3 >= var4) {
               var2 = false;
               break;
            }

            if (this.valueList.contains(((Tag)var1.get(var3)).value)) {
               var2 = true;
               break;
            }

            ++var3;
         }
      }

      return var2;
   }
}
