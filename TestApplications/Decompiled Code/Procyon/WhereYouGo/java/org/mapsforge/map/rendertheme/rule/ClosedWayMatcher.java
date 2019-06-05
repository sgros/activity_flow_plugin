// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

final class ClosedWayMatcher implements ClosedMatcher
{
    static final ClosedWayMatcher INSTANCE;
    
    static {
        INSTANCE = new ClosedWayMatcher();
    }
    
    private ClosedWayMatcher() {
    }
    
    @Override
    public boolean isCoveredBy(final ClosedMatcher closedMatcher) {
        return closedMatcher.matches(Closed.YES);
    }
    
    @Override
    public boolean matches(final Closed closed) {
        return closed == Closed.YES;
    }
}
