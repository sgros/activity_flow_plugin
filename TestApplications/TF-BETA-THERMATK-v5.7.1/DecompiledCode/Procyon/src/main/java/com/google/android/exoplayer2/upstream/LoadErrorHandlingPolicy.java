// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.IOException;

public interface LoadErrorHandlingPolicy
{
    long getBlacklistDurationMsFor(final int p0, final long p1, final IOException p2, final int p3);
    
    int getMinimumLoadableRetryCount(final int p0);
    
    long getRetryDelayMsFor(final int p0, final long p1, final IOException p2, final int p3);
}
