package org.mapsforge.map.rendertheme.rule;

import java.util.List;

interface AttributeMatcher {
   boolean isCoveredBy(AttributeMatcher var1);

   boolean matches(List var1);
}
