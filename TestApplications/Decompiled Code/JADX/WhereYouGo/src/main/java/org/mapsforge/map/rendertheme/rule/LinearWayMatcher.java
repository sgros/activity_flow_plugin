package org.mapsforge.map.rendertheme.rule;

final class LinearWayMatcher implements ClosedMatcher {
    static final LinearWayMatcher INSTANCE = new LinearWayMatcher();

    private LinearWayMatcher() {
    }

    public boolean isCoveredBy(ClosedMatcher closedMatcher) {
        return closedMatcher.matches(Closed.NO);
    }

    public boolean matches(Closed closed) {
        return closed == Closed.NO;
    }
}
