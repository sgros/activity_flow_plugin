// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming;

import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.chunk.ChunkSource;

public interface SsChunkSource extends ChunkSource
{
    void updateManifest(final SsManifest p0);
    
    public interface Factory
    {
        SsChunkSource createChunkSource(final LoaderErrorThrower p0, final SsManifest p1, final int p2, final TrackSelection p3, final TransferListener p4);
    }
}
