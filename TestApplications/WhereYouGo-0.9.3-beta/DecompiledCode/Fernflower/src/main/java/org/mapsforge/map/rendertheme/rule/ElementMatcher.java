package org.mapsforge.map.rendertheme.rule;

interface ElementMatcher {
   boolean isCoveredBy(ElementMatcher var1);

   boolean matches(Element var1);
}
