package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.Tag;

class ValueMatcher implements AttributeMatcher {
   private final List values;

   ValueMatcher(List var1) {
      this.values = var1;
   }

   public boolean isCoveredBy(AttributeMatcher var1) {
      boolean var2;
      if (var1 == this) {
         var2 = true;
      } else {
         ArrayList var3 = new ArrayList(this.values.size());
         int var4 = 0;

         for(int var5 = this.values.size(); var4 < var5; ++var4) {
            var3.add(new Tag((String)null, (String)this.values.get(var4)));
         }

         var2 = var1.matches(var3);
      }

      return var2;
   }

   public boolean matches(List var1) {
      int var2 = 0;
      int var3 = var1.size();

      boolean var4;
      while(true) {
         if (var2 >= var3) {
            var4 = false;
            break;
         }

         if (this.values.contains(((Tag)var1.get(var2)).value)) {
            var4 = true;
            break;
         }

         ++var2;
      }

      return var4;
   }
}
