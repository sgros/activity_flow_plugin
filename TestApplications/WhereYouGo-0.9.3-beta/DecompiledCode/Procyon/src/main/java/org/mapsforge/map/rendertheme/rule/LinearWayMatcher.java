// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

final class LinearWayMatcher implements ClosedMatcher
{
    static final LinearWayMatcher INSTANCE;
    
    static {
        INSTANCE = new LinearWayMatcher();
    }
    
    private LinearWayMatcher() {
    }
    
    @Override
    public boolean isCoveredBy(final ClosedMatcher closedMatcher) {
        return closedMatcher.matches(Closed.NO);
    }
    
    @Override
    public boolean matches(final Closed closed) {
        return closed == Closed.NO;
    }
}
