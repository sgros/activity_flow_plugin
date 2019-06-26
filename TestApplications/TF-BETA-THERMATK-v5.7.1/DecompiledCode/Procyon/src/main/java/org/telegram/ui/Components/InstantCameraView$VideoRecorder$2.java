// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
import org.telegram.messenger.FileLog;

class InstantCameraView$VideoRecorder$2 implements VideoPlayerDelegate
{
    final /* synthetic */ InstantCameraView.VideoRecorder this$1;
    
    InstantCameraView$VideoRecorder$2(final InstantCameraView.VideoRecorder this$1) {
        this.this$1 = this$1;
    }
    
    @Override
    public void onError(final Exception ex) {
        FileLog.e(ex);
    }
    
    @Override
    public void onRenderedFirstFrame() {
    }
    
    @Override
    public void onStateChanged(final boolean b, final int n) {
        if (this.this$1.this$0.videoPlayer == null) {
            return;
        }
        if (this.this$1.this$0.videoPlayer.isPlaying() && n == 4) {
            final VideoPlayer access$900 = this.this$1.this$0.videoPlayer;
            final long startTime = this.this$1.this$0.videoEditedInfo.startTime;
            long startTime2 = 0L;
            if (startTime > 0L) {
                startTime2 = this.this$1.this$0.videoEditedInfo.startTime;
            }
            access$900.seekTo(startTime2);
        }
    }
    
    @Override
    public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
        return false;
    }
    
    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }
    
    @Override
    public void onVideoSizeChanged(final int n, final int n2, final int n3, final float n4) {
    }
}
