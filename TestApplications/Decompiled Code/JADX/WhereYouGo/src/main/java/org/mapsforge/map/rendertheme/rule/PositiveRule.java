package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class PositiveRule extends Rule {
    final AttributeMatcher keyMatcher;
    final AttributeMatcher valueMatcher;

    PositiveRule(RuleBuilder ruleBuilder, AttributeMatcher keyMatcher, AttributeMatcher valueMatcher) {
        super(ruleBuilder);
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean matchesNode(List<Tag> tags, byte zoomLevel) {
        return this.zoomMin <= zoomLevel && this.zoomMax >= zoomLevel && this.elementMatcher.matches(Element.NODE) && this.keyMatcher.matches(tags) && this.valueMatcher.matches(tags);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean matchesWay(List<Tag> tags, byte zoomLevel, Closed closed) {
        return this.zoomMin <= zoomLevel && this.zoomMax >= zoomLevel && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(closed) && this.keyMatcher.matches(tags) && this.valueMatcher.matches(tags);
    }
}
