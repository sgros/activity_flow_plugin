// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.List;

class NegativeRule extends Rule
{
    private final AttributeMatcher attributeMatcher;
    
    NegativeRule(final RuleBuilder ruleBuilder, final AttributeMatcher attributeMatcher) {
        super(ruleBuilder);
        this.attributeMatcher = attributeMatcher;
    }
    
    @Override
    boolean matchesNode(final List<Tag> list, final byte b) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.NODE) && this.attributeMatcher.matches(list);
    }
    
    @Override
    boolean matchesWay(final List<Tag> list, final byte b, final Closed closed) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(closed) && this.attributeMatcher.matches(list);
    }
}
