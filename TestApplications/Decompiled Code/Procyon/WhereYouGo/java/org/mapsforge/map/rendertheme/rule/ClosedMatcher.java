// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

interface ClosedMatcher
{
    boolean isCoveredBy(final ClosedMatcher p0);
    
    boolean matches(final Closed p0);
}
