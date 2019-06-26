package org.mapsforge.map.rendertheme.rule;

final class ElementNodeMatcher implements ElementMatcher {
    static final ElementNodeMatcher INSTANCE = new ElementNodeMatcher();

    private ElementNodeMatcher() {
    }

    public boolean isCoveredBy(ElementMatcher elementMatcher) {
        return elementMatcher.matches(Element.NODE);
    }

    public boolean matches(Element element) {
        return element == Element.NODE;
    }
}
