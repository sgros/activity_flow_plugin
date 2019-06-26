package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

final class AnyMatcher implements ElementMatcher, AttributeMatcher, ClosedMatcher {
    static final AnyMatcher INSTANCE = new AnyMatcher();

    private AnyMatcher() {
    }

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        return attributeMatcher == this;
    }

    public boolean isCoveredBy(ClosedMatcher closedMatcher) {
        return closedMatcher == this;
    }

    public boolean isCoveredBy(ElementMatcher elementMatcher) {
        return elementMatcher == this;
    }

    public boolean matches(Closed closed) {
        return true;
    }

    public boolean matches(Element element) {
        return true;
    }

    public boolean matches(List<Tag> list) {
        return true;
    }
}
