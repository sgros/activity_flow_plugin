// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

public interface DashSegmentIndex
{
    long getDurationUs(final long p0, final long p1);
    
    long getFirstSegmentNum();
    
    int getSegmentCount(final long p0);
    
    long getSegmentNum(final long p0, final long p1);
    
    RangedUri getSegmentUrl(final long p0);
    
    long getTimeUs(final long p0);
    
    boolean isExplicit();
}
