package org.mapsforge.map.rendertheme.rule;

import java.util.List;

final class AnyMatcher implements ElementMatcher, AttributeMatcher, ClosedMatcher {
   static final AnyMatcher INSTANCE = new AnyMatcher();

   private AnyMatcher() {
   }

   public boolean isCoveredBy(AttributeMatcher var1) {
      boolean var2;
      if (var1 == this) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isCoveredBy(ClosedMatcher var1) {
      boolean var2;
      if (var1 == this) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isCoveredBy(ElementMatcher var1) {
      boolean var2;
      if (var1 == this) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean matches(List var1) {
      return true;
   }

   public boolean matches(Closed var1) {
      return true;
   }

   public boolean matches(Element var1) {
      return true;
   }
}
