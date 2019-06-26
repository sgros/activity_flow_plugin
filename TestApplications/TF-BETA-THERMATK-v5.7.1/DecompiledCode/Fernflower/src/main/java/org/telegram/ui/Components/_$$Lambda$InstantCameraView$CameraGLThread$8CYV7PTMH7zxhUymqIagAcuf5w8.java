package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;

// $FF: synthetic class
public final class _$$Lambda$InstantCameraView$CameraGLThread$8CYV7PTMH7zxhUymqIagAcuf5w8 implements OnFrameAvailableListener {
   // $FF: synthetic field
   private final InstantCameraView.CameraGLThread f$0;

   // $FF: synthetic method
   public _$$Lambda$InstantCameraView$CameraGLThread$8CYV7PTMH7zxhUymqIagAcuf5w8(InstantCameraView.CameraGLThread var1) {
      this.f$0 = var1;
   }

   public final void onFrameAvailable(SurfaceTexture var1) {
      this.f$0.lambda$handleMessage$1$InstantCameraView$CameraGLThread(var1);
   }
}
