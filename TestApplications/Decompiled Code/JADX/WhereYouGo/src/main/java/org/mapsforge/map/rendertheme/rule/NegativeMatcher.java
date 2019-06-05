package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class NegativeMatcher implements AttributeMatcher {
    private final List<String> keyList;
    private final List<String> valueList;

    NegativeMatcher(List<String> keyList, List<String> valueList) {
        this.keyList = keyList;
        this.valueList = valueList;
    }

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        return false;
    }

    public boolean matches(List<Tag> tags) {
        if (keyListDoesNotContainKeys(tags)) {
            return true;
        }
        int n = tags.size();
        for (int i = 0; i < n; i++) {
            if (this.valueList.contains(((Tag) tags.get(i)).value)) {
                return true;
            }
        }
        return false;
    }

    private boolean keyListDoesNotContainKeys(List<Tag> tags) {
        int n = tags.size();
        for (int i = 0; i < n; i++) {
            if (this.keyList.contains(((Tag) tags.get(i)).key)) {
                return false;
            }
        }
        return true;
    }
}
