// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.chunk.ChunkSource;

public interface DashChunkSource extends ChunkSource
{
    void updateManifest(final DashManifest p0, final int p1);
    
    public interface Factory
    {
        DashChunkSource createDashChunkSource(final LoaderErrorThrower p0, final DashManifest p1, final int p2, final int[] p3, final TrackSelection p4, final int p5, final long p6, final boolean p7, final boolean p8, final PlayerEmsgHandler.PlayerTrackEmsgHandler p9, final TransferListener p10);
    }
}
