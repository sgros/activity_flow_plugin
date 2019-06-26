package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class MatchingCacheKey {
    private final Closed closed;
    private final List<Tag> tags;
    private final byte zoomLevel;

    MatchingCacheKey(List<Tag> tags, byte zoomLevel, Closed closed) {
        this.tags = tags;
        this.zoomLevel = zoomLevel;
        this.closed = closed;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MatchingCacheKey)) {
            return false;
        }
        MatchingCacheKey other = (MatchingCacheKey) obj;
        if (this.closed != other.closed) {
            return false;
        }
        if (this.tags == null) {
            if (other.tags != null) {
                return false;
            }
        } else if (!this.tags.equals(other.tags)) {
            return false;
        }
        if (this.zoomLevel != other.zoomLevel) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.closed == null ? 0 : this.closed.hashCode()) + 31) * 31;
        if (this.tags != null) {
            i = this.tags.hashCode();
        }
        return ((hashCode + i) * 31) + this.zoomLevel;
    }
}
