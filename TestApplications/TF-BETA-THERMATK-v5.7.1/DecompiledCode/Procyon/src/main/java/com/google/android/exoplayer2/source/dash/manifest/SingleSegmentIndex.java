// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.source.dash.DashSegmentIndex;

final class SingleSegmentIndex implements DashSegmentIndex
{
    private final RangedUri uri;
    
    public SingleSegmentIndex(final RangedUri uri) {
        this.uri = uri;
    }
    
    @Override
    public long getDurationUs(final long n, final long n2) {
        return n2;
    }
    
    @Override
    public long getFirstSegmentNum() {
        return 0L;
    }
    
    @Override
    public int getSegmentCount(final long n) {
        return 1;
    }
    
    @Override
    public long getSegmentNum(final long n, final long n2) {
        return 0L;
    }
    
    @Override
    public RangedUri getSegmentUrl(final long n) {
        return this.uri;
    }
    
    @Override
    public long getTimeUs(final long n) {
        return 0L;
    }
    
    @Override
    public boolean isExplicit() {
        return true;
    }
}
