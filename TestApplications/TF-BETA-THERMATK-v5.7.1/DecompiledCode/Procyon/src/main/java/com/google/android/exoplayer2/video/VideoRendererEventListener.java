// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.util.Assertions;
import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import android.view.Surface;

public interface VideoRendererEventListener
{
    void onDroppedFrames(final int p0, final long p1);
    
    void onRenderedFirstFrame(final Surface p0);
    
    void onVideoDecoderInitialized(final String p0, final long p1, final long p2);
    
    void onVideoDisabled(final DecoderCounters p0);
    
    void onVideoEnabled(final DecoderCounters p0);
    
    void onVideoInputFormatChanged(final Format p0);
    
    void onVideoSizeChanged(final int p0, final int p1, final int p2, final float p3);
    
    public static final class EventDispatcher
    {
        private final Handler handler;
        private final VideoRendererEventListener listener;
        
        public EventDispatcher(Handler handler, final VideoRendererEventListener listener) {
            if (listener != null) {
                Assertions.checkNotNull(handler);
                handler = handler;
            }
            else {
                handler = null;
            }
            this.handler = handler;
            this.listener = listener;
        }
        
        public void decoderInitialized(final String s, final long n, final long n2) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$Y232CA7hogfrRJjYu2VeUSxg0VQ(this, s, n, n2));
            }
        }
        
        public void disabled(final DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$qTQ_0WnG_WelRJ9iR8L0OaiS0Go(this, decoderCounters));
            }
        }
        
        public void droppedFrames(final int n, final long n2) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$wpJzum9Nim_WREQi3I6t6RZgGzs(this, n, n2));
            }
        }
        
        public void enabled(final DecoderCounters decoderCounters) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$Zf6ofdxzBBJ5SL288lE0HglRj8g(this, decoderCounters));
            }
        }
        
        public void inputFormatChanged(final Format format) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$26y6c6BFFT4OL6bJiMmdsfxDEMQ(this, format));
            }
        }
        
        public void renderedFirstFrame(final Surface surface) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$SFK5uUI0PHTm3Dg6Wdc1eRaQ9xk(this, surface));
            }
        }
        
        public void videoSizeChanged(final int n, final int n2, final int n3, final float n4) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$VideoRendererEventListener$EventDispatcher$TaBV3X3b5lKElsQ7tczViKAyQ3w(this, n, n2, n3, n4));
            }
        }
    }
}
