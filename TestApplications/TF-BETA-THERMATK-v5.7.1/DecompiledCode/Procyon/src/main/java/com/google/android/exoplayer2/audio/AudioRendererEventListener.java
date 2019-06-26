// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Assertions;
import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;

public interface AudioRendererEventListener
{
    void onAudioDecoderInitialized(final String p0, final long p1, final long p2);
    
    void onAudioDisabled(final DecoderCounters p0);
    
    void onAudioEnabled(final DecoderCounters p0);
    
    void onAudioInputFormatChanged(final Format p0);
    
    void onAudioSessionId(final int p0);
    
    void onAudioSinkUnderrun(final int p0, final long p1, final long p2);
    
    public static final class EventDispatcher
    {
        private final Handler handler;
        private final AudioRendererEventListener listener;
        
        public EventDispatcher(Handler handler, final AudioRendererEventListener listener) {
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
        
        public void audioSessionId(final int n) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$AudioRendererEventListener$EventDispatcher$a1B1YBHhPRCtc1MQAc2fSVEo22I(this, n));
            }
        }
        
        public void audioTrackUnderrun(final int n, final long n2, final long n3) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$AudioRendererEventListener$EventDispatcher$oPQKly422CpX1mqIU2N6d76OGxk(this, n, n2, n3));
            }
        }
        
        public void decoderInitialized(final String s, final long n, final long n2) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$AudioRendererEventListener$EventDispatcher$F29t8_xYSK7h_6CpLRlp2y2yb1E(this, s, n, n2));
            }
        }
        
        public void disabled(final DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$AudioRendererEventListener$EventDispatcher$jb22FSnmUl2pGG0LguQS_Wd_LWk(this, decoderCounters));
            }
        }
        
        public void enabled(final DecoderCounters decoderCounters) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$AudioRendererEventListener$EventDispatcher$MUMUaHcEfIpwDLi9gxmScOQxifc(this, decoderCounters));
            }
        }
        
        public void inputFormatChanged(final Format format) {
            if (this.listener != null) {
                this.handler.post((Runnable)new _$$Lambda$AudioRendererEventListener$EventDispatcher$D7KvJbrpXrnWw4qzd_LI9ZtQytw(this, format));
            }
        }
    }
}
