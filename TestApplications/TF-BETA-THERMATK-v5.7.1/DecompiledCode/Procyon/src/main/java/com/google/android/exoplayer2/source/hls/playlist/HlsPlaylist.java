// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import java.util.Collections;
import java.util.List;
import com.google.android.exoplayer2.offline.FilterableManifest;

public abstract class HlsPlaylist implements FilterableManifest<HlsPlaylist>
{
    public final String baseUri;
    public final boolean hasIndependentSegments;
    public final List<String> tags;
    
    protected HlsPlaylist(final String baseUri, final List<String> list, final boolean hasIndependentSegments) {
        this.baseUri = baseUri;
        this.tags = Collections.unmodifiableList((List<? extends String>)list);
        this.hasIndependentSegments = hasIndependentSegments;
    }
}
