package org.mapsforge.map.rendertheme.rule;

interface ClosedMatcher {
    boolean isCoveredBy(ClosedMatcher closedMatcher);

    boolean matches(Closed closed);
}
