// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.List;

interface AttributeMatcher
{
    boolean isCoveredBy(final AttributeMatcher p0);
    
    boolean matches(final List<Tag> p0);
}
