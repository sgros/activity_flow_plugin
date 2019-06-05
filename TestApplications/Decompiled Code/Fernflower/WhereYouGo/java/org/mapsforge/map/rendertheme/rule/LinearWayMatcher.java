package org.mapsforge.map.rendertheme.rule;

final class LinearWayMatcher implements ClosedMatcher {
   static final LinearWayMatcher INSTANCE = new LinearWayMatcher();

   private LinearWayMatcher() {
   }

   public boolean isCoveredBy(ClosedMatcher var1) {
      return var1.matches(Closed.NO);
   }

   public boolean matches(Closed var1) {
      boolean var2;
      if (var1 == Closed.NO) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
