package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

interface AttributeMatcher {
    boolean isCoveredBy(AttributeMatcher attributeMatcher);

    boolean matches(List<Tag> list);
}
