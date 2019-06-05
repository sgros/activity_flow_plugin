// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

interface ElementMatcher
{
    boolean isCoveredBy(final ElementMatcher p0);
    
    boolean matches(final Element p0);
}
