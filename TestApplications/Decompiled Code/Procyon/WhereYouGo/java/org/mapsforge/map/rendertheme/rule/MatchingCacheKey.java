// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.core.model.Tag;
import java.util.List;

class MatchingCacheKey
{
    private final Closed closed;
    private final List<Tag> tags;
    private final byte zoomLevel;
    
    MatchingCacheKey(final List<Tag> tags, final byte b, final Closed closed) {
        this.tags = tags;
        this.zoomLevel = b;
        this.closed = closed;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof MatchingCacheKey)) {
                b = false;
            }
            else {
                final MatchingCacheKey matchingCacheKey = (MatchingCacheKey)o;
                if (this.closed != matchingCacheKey.closed) {
                    b = false;
                }
                else {
                    if (this.tags == null) {
                        if (matchingCacheKey.tags != null) {
                            b = false;
                            return b;
                        }
                    }
                    else if (!this.tags.equals(matchingCacheKey.tags)) {
                        b = false;
                        return b;
                    }
                    if (this.zoomLevel != matchingCacheKey.zoomLevel) {
                        b = false;
                    }
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.closed == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.closed.hashCode();
        }
        if (this.tags != null) {
            hashCode = this.tags.hashCode();
        }
        return ((hashCode2 + 31) * 31 + hashCode) * 31 + this.zoomLevel;
    }
}
