package org.mapsforge.map.rendertheme.rule;

final class ClosedWayMatcher implements ClosedMatcher {
   static final ClosedWayMatcher INSTANCE = new ClosedWayMatcher();

   private ClosedWayMatcher() {
   }

   public boolean isCoveredBy(ClosedMatcher var1) {
      return var1.matches(Closed.YES);
   }

   public boolean matches(Closed var1) {
      boolean var2;
      if (var1 == Closed.YES) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
