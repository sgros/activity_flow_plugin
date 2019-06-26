package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
import org.telegram.messenger.FileLog;

class InstantCameraView$VideoRecorder$2 implements VideoPlayer.VideoPlayerDelegate {
   // $FF: synthetic field
   final InstantCameraView.VideoRecorder this$1;

   InstantCameraView$VideoRecorder$2(InstantCameraView.VideoRecorder var1) {
      this.this$1 = var1;
   }

   public void onError(Exception var1) {
      FileLog.e((Throwable)var1);
   }

   public void onRenderedFirstFrame() {
   }

   public void onStateChanged(boolean var1, int var2) {
      if (InstantCameraView.access$900(this.this$1.this$0) != null) {
         if (InstantCameraView.access$900(this.this$1.this$0).isPlaying() && var2 == 4) {
            VideoPlayer var3 = InstantCameraView.access$900(this.this$1.this$0);
            long var4 = InstantCameraView.access$1000(this.this$1.this$0).startTime;
            long var6 = 0L;
            if (var4 > 0L) {
               var6 = InstantCameraView.access$1000(this.this$1.this$0).startTime;
            }

            var3.seekTo(var6);
         }

      }
   }

   public boolean onSurfaceDestroyed(SurfaceTexture var1) {
      return false;
   }

   public void onSurfaceTextureUpdated(SurfaceTexture var1) {
   }

   public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
   }
}
