// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import android.graphics.SurfaceTexture;

public interface VideoListener
{
    void onRenderedFirstFrame();
    
    boolean onSurfaceDestroyed(final SurfaceTexture p0);
    
    void onSurfaceSizeChanged(final int p0, final int p1);
    
    void onSurfaceTextureUpdated(final SurfaceTexture p0);
    
    void onVideoSizeChanged(final int p0, final int p1, final int p2, final float p3);
}
