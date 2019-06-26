package com.google.android.exoplayer2.video;

import android.graphics.SurfaceTexture;

public interface VideoListener {
   void onRenderedFirstFrame();

   boolean onSurfaceDestroyed(SurfaceTexture var1);

   void onSurfaceSizeChanged(int var1, int var2);

   void onSurfaceTextureUpdated(SurfaceTexture var1);

   void onVideoSizeChanged(int var1, int var2, int var3, float var4);
}
