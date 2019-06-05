// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

final class ElementWayMatcher implements ElementMatcher
{
    static final ElementWayMatcher INSTANCE;
    
    static {
        INSTANCE = new ElementWayMatcher();
    }
    
    private ElementWayMatcher() {
    }
    
    @Override
    public boolean isCoveredBy(final ElementMatcher elementMatcher) {
        return elementMatcher.matches(Element.WAY);
    }
    
    @Override
    public boolean matches(final Element element) {
        return element == Element.WAY;
    }
}
