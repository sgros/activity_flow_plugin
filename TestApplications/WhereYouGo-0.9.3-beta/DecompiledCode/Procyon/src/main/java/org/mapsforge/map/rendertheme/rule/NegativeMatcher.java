// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.List;

class NegativeMatcher implements AttributeMatcher
{
    private final List<String> keyList;
    private final List<String> valueList;
    
    NegativeMatcher(final List<String> keyList, final List<String> valueList) {
        this.keyList = keyList;
        this.valueList = valueList;
    }
    
    private boolean keyListDoesNotContainKeys(final List<Tag> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (this.keyList.contains(list.get(i).key)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isCoveredBy(final AttributeMatcher attributeMatcher) {
        return false;
    }
    
    @Override
    public boolean matches(final List<Tag> list) {
        boolean b;
        if (this.keyListDoesNotContainKeys(list)) {
            b = true;
        }
        else {
            for (int i = 0; i < list.size(); ++i) {
                if (this.valueList.contains(list.get(i).value)) {
                    b = true;
                    return b;
                }
            }
            b = false;
        }
        return b;
    }
}
