// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.List;

class PositiveRule extends Rule
{
    final AttributeMatcher keyMatcher;
    final AttributeMatcher valueMatcher;
    
    PositiveRule(final RuleBuilder ruleBuilder, final AttributeMatcher keyMatcher, final AttributeMatcher valueMatcher) {
        super(ruleBuilder);
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }
    
    @Override
    boolean matchesNode(final List<Tag> list, final byte b) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.NODE) && this.keyMatcher.matches(list) && this.valueMatcher.matches(list);
    }
    
    @Override
    boolean matchesWay(final List<Tag> list, final byte b, final Closed closed) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(closed) && this.keyMatcher.matches(list) && this.valueMatcher.matches(list);
    }
}
