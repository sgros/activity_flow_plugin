// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import android.os.Handler;

public interface BandwidthMeter
{
    void addEventListener(final Handler p0, final EventListener p1);
    
    long getBitrateEstimate();
    
    TransferListener getTransferListener();
    
    void removeEventListener(final EventListener p0);
    
    public interface EventListener
    {
        void onBandwidthSample(final int p0, final long p1, final long p2);
    }
}
