package org.mapsforge.map.rendertheme.rule;

final class ElementWayMatcher implements ElementMatcher {
    static final ElementWayMatcher INSTANCE = new ElementWayMatcher();

    private ElementWayMatcher() {
    }

    public boolean isCoveredBy(ElementMatcher elementMatcher) {
        return elementMatcher.matches(Element.WAY);
    }

    public boolean matches(Element element) {
        return element == Element.WAY;
    }
}
