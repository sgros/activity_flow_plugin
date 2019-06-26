// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import android.net.Uri;
import java.util.List;
import java.util.Map;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.StatsDataSource;
import com.google.android.exoplayer2.upstream.Loader;

public abstract class Chunk implements Loadable
{
    protected final StatsDataSource dataSource;
    public final DataSpec dataSpec;
    public final long endTimeUs;
    public final long startTimeUs;
    public final Format trackFormat;
    public final Object trackSelectionData;
    public final int trackSelectionReason;
    public final int type;
    
    public Chunk(final DataSource dataSource, final DataSpec dataSpec, final int type, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long startTimeUs, final long endTimeUs) {
        this.dataSource = new StatsDataSource(dataSource);
        Assertions.checkNotNull(dataSpec);
        this.dataSpec = dataSpec;
        this.type = type;
        this.trackFormat = trackFormat;
        this.trackSelectionReason = trackSelectionReason;
        this.trackSelectionData = trackSelectionData;
        this.startTimeUs = startTimeUs;
        this.endTimeUs = endTimeUs;
    }
    
    public final long bytesLoaded() {
        return this.dataSource.getBytesRead();
    }
    
    public final long getDurationUs() {
        return this.endTimeUs - this.startTimeUs;
    }
    
    public final Map<String, List<String>> getResponseHeaders() {
        return this.dataSource.getLastResponseHeaders();
    }
    
    public final Uri getUri() {
        return this.dataSource.getLastOpenedUri();
    }
}
