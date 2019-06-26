package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.Tag;

class ValueMatcher implements AttributeMatcher {
    private final List<String> values;

    ValueMatcher(List<String> values) {
        this.values = values;
    }

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        if (attributeMatcher == this) {
            return true;
        }
        List<Tag> tags = new ArrayList(this.values.size());
        int n = this.values.size();
        for (int i = 0; i < n; i++) {
            tags.add(new Tag(null, (String) this.values.get(i)));
        }
        return attributeMatcher.matches(tags);
    }

    public boolean matches(List<Tag> tags) {
        int n = tags.size();
        for (int i = 0; i < n; i++) {
            if (this.values.contains(((Tag) tags.get(i)).value)) {
                return true;
            }
        }
        return false;
    }
}
