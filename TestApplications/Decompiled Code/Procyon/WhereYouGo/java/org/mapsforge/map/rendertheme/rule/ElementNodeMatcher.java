// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

final class ElementNodeMatcher implements ElementMatcher
{
    static final ElementNodeMatcher INSTANCE;
    
    static {
        INSTANCE = new ElementNodeMatcher();
    }
    
    private ElementNodeMatcher() {
    }
    
    @Override
    public boolean isCoveredBy(final ElementMatcher elementMatcher) {
        return elementMatcher.matches(Element.NODE);
    }
    
    @Override
    public boolean matches(final Element element) {
        return element == Element.NODE;
    }
}
