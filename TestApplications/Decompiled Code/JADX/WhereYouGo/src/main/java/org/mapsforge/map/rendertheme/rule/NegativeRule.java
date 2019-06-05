package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class NegativeRule extends Rule {
    private final AttributeMatcher attributeMatcher;

    NegativeRule(RuleBuilder ruleBuilder, AttributeMatcher attributeMatcher) {
        super(ruleBuilder);
        this.attributeMatcher = attributeMatcher;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean matchesNode(List<Tag> tags, byte zoomLevel) {
        return this.zoomMin <= zoomLevel && this.zoomMax >= zoomLevel && this.elementMatcher.matches(Element.NODE) && this.attributeMatcher.matches(tags);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean matchesWay(List<Tag> tags, byte zoomLevel, Closed closed) {
        return this.zoomMin <= zoomLevel && this.zoomMax >= zoomLevel && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(closed) && this.attributeMatcher.matches(tags);
    }
}
