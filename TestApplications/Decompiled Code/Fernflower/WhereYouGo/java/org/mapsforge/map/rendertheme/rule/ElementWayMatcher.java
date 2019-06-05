package org.mapsforge.map.rendertheme.rule;

final class ElementWayMatcher implements ElementMatcher {
   static final ElementWayMatcher INSTANCE = new ElementWayMatcher();

   private ElementWayMatcher() {
   }

   public boolean isCoveredBy(ElementMatcher var1) {
      return var1.matches(Element.WAY);
   }

   public boolean matches(Element var1) {
      boolean var2;
      if (var1 == Element.WAY) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
