package org.mapsforge.map.rendertheme.rule;

final class ElementNodeMatcher implements ElementMatcher {
   static final ElementNodeMatcher INSTANCE = new ElementNodeMatcher();

   private ElementNodeMatcher() {
   }

   public boolean isCoveredBy(ElementMatcher var1) {
      return var1.matches(Element.NODE);
   }

   public boolean matches(Element var1) {
      boolean var2;
      if (var1 == Element.NODE) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
