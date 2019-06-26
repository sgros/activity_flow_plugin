// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.List;

final class AnyMatcher implements ElementMatcher, AttributeMatcher, ClosedMatcher
{
    static final AnyMatcher INSTANCE;
    
    static {
        INSTANCE = new AnyMatcher();
    }
    
    private AnyMatcher() {
    }
    
    @Override
    public boolean isCoveredBy(final AttributeMatcher attributeMatcher) {
        return attributeMatcher == this;
    }
    
    @Override
    public boolean isCoveredBy(final ClosedMatcher closedMatcher) {
        return closedMatcher == this;
    }
    
    @Override
    public boolean isCoveredBy(final ElementMatcher elementMatcher) {
        return elementMatcher == this;
    }
    
    @Override
    public boolean matches(final List<Tag> list) {
        return true;
    }
    
    @Override
    public boolean matches(final Closed closed) {
        return true;
    }
    
    @Override
    public boolean matches(final Element element) {
        return true;
    }
}
