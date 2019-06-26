package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.Tag;

class KeyMatcher implements AttributeMatcher {
    private final List<String> keys;

    KeyMatcher(List<String> keys) {
        this.keys = keys;
    }

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        if (attributeMatcher == this) {
            return true;
        }
        List<Tag> tags = new ArrayList(this.keys.size());
        int n = this.keys.size();
        for (int i = 0; i < n; i++) {
            tags.add(new Tag((String) this.keys.get(i), null));
        }
        return attributeMatcher.matches(tags);
    }

    public boolean matches(List<Tag> tags) {
        int n = tags.size();
        for (int i = 0; i < n; i++) {
            if (this.keys.contains(((Tag) tags.get(i)).key)) {
                return true;
            }
        }
        return false;
    }
}
