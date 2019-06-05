package org.mapsforge.map.rendertheme.rule;

import java.util.List;

class NegativeRule extends Rule {
   private final AttributeMatcher attributeMatcher;

   NegativeRule(RuleBuilder var1, AttributeMatcher var2) {
      super(var1);
      this.attributeMatcher = var2;
   }

   boolean matchesNode(List var1, byte var2) {
      boolean var3;
      if (this.zoomMin <= var2 && this.zoomMax >= var2 && this.elementMatcher.matches(Element.NODE) && this.attributeMatcher.matches(var1)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   boolean matchesWay(List var1, byte var2, Closed var3) {
      boolean var4;
      if (this.zoomMin <= var2 && this.zoomMax >= var2 && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(var3) && this.attributeMatcher.matches(var1)) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }
}
