// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.ArrayList;
import java.util.List;

class ValueMatcher implements AttributeMatcher
{
    private final List<String> values;
    
    ValueMatcher(final List<String> values) {
        this.values = values;
    }
    
    @Override
    public boolean isCoveredBy(final AttributeMatcher attributeMatcher) {
        boolean matches;
        if (attributeMatcher == this) {
            matches = true;
        }
        else {
            final ArrayList<Tag> list = new ArrayList<Tag>(this.values.size());
            for (int i = 0; i < this.values.size(); ++i) {
                list.add(new Tag(null, this.values.get(i)));
            }
            matches = attributeMatcher.matches(list);
        }
        return matches;
    }
    
    @Override
    public boolean matches(final List<Tag> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (this.values.contains(list.get(i).value)) {
                return true;
            }
        }
        return false;
    }
}
