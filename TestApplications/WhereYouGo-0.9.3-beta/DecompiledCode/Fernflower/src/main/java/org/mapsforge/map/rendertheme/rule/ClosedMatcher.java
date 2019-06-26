package org.mapsforge.map.rendertheme.rule;

interface ClosedMatcher {
   boolean isCoveredBy(ClosedMatcher var1);

   boolean matches(Closed var1);
}
