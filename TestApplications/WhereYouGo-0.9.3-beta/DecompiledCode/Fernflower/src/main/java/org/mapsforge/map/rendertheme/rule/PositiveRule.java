package org.mapsforge.map.rendertheme.rule;

import java.util.List;

class PositiveRule extends Rule {
   final AttributeMatcher keyMatcher;
   final AttributeMatcher valueMatcher;

   PositiveRule(RuleBuilder var1, AttributeMatcher var2, AttributeMatcher var3) {
      super(var1);
      this.keyMatcher = var2;
      this.valueMatcher = var3;
   }

   boolean matchesNode(List var1, byte var2) {
      boolean var3;
      if (this.zoomMin <= var2 && this.zoomMax >= var2 && this.elementMatcher.matches(Element.NODE) && this.keyMatcher.matches(var1) && this.valueMatcher.matches(var1)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   boolean matchesWay(List var1, byte var2, Closed var3) {
      boolean var4;
      if (this.zoomMin <= var2 && this.zoomMax >= var2 && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(var3) && this.keyMatcher.matches(var1) && this.valueMatcher.matches(var1)) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }
}
