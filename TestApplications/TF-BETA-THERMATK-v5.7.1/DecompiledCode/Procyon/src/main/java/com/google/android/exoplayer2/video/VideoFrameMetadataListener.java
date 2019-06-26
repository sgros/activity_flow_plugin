// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.Format;

public interface VideoFrameMetadataListener
{
    void onVideoFrameAboutToBeRendered(final long p0, final long p1, final Format p2);
}
