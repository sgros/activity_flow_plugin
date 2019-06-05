package org.mapsforge.map.rendertheme.rule;

final class ClosedWayMatcher implements ClosedMatcher {
    static final ClosedWayMatcher INSTANCE = new ClosedWayMatcher();

    private ClosedWayMatcher() {
    }

    public boolean isCoveredBy(ClosedMatcher closedMatcher) {
        return closedMatcher.matches(Closed.YES);
    }

    public boolean matches(Closed closed) {
        return closed == Closed.YES;
    }
}
