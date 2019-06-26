package org.mapsforge.map.rendertheme.rule;

interface ElementMatcher {
    boolean isCoveredBy(ElementMatcher elementMatcher);

    boolean matches(Element element);
}
