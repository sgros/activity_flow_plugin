// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

public final class MatchGroup
{
    private final IntRange range;
    private final String value;
    
    public MatchGroup(final String value, final IntRange range) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        Intrinsics.checkParameterIsNotNull(range, "range");
        this.value = value;
        this.range = range;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof MatchGroup) {
                final MatchGroup matchGroup = (MatchGroup)o;
                if (Intrinsics.areEqual(this.value, matchGroup.value) && Intrinsics.areEqual(this.range, matchGroup.range)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public final String getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        final String value = this.value;
        int hashCode = 0;
        int hashCode2;
        if (value != null) {
            hashCode2 = value.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final IntRange range = this.range;
        if (range != null) {
            hashCode = range.hashCode();
        }
        return hashCode2 * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MatchGroup(value=");
        sb.append(this.value);
        sb.append(", range=");
        sb.append(this.range);
        sb.append(")");
        return sb.toString();
    }
}
