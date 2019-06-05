// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

public interface MatchGroupCollection extends Collection<MatchGroup>, KMappedMarker
{
    MatchGroup get(final int p0);
}
