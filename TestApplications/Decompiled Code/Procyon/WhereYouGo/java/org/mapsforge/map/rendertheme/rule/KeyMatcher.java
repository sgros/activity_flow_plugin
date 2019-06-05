// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.ArrayList;
import java.util.List;

class KeyMatcher implements AttributeMatcher
{
    private final List<String> keys;
    
    KeyMatcher(final List<String> keys) {
        this.keys = keys;
    }
    
    @Override
    public boolean isCoveredBy(final AttributeMatcher attributeMatcher) {
        boolean matches;
        if (attributeMatcher == this) {
            matches = true;
        }
        else {
            final ArrayList<Tag> list = new ArrayList<Tag>(this.keys.size());
            for (int i = 0; i < this.keys.size(); ++i) {
                list.add(new Tag(this.keys.get(i), null));
            }
            matches = attributeMatcher.matches(list);
        }
        return matches;
    }
    
    @Override
    public boolean matches(final List<Tag> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (this.keys.contains(list.get(i).key)) {
                return true;
            }
        }
        return false;
    }
}
