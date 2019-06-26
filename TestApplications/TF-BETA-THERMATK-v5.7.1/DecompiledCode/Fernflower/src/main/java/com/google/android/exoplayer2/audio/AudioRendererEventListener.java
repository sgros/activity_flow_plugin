package com.google.android.exoplayer2.audio;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;

public interface AudioRendererEventListener {
   void onAudioDecoderInitialized(String var1, long var2, long var4);

   void onAudioDisabled(DecoderCounters var1);

   void onAudioEnabled(DecoderCounters var1);

   void onAudioInputFormatChanged(Format var1);

   void onAudioSessionId(int var1);

   void onAudioSinkUnderrun(int var1, long var2, long var4);

   public static final class EventDispatcher {
      private final Handler handler;
      private final AudioRendererEventListener listener;

      public EventDispatcher(Handler var1, AudioRendererEventListener var2) {
         if (var2 != null) {
            Assertions.checkNotNull(var1);
            var1 = (Handler)var1;
         } else {
            var1 = null;
         }

         this.handler = var1;
         this.listener = var2;
      }

      public void audioSessionId(int var1) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$AudioRendererEventListener$EventDispatcher$a1B1YBHhPRCtc1MQAc2fSVEo22I(this, var1));
         }

      }

      public void audioTrackUnderrun(int var1, long var2, long var4) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$AudioRendererEventListener$EventDispatcher$oPQKly422CpX1mqIU2N6d76OGxk(this, var1, var2, var4));
         }

      }

      public void decoderInitialized(String var1, long var2, long var4) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$AudioRendererEventListener$EventDispatcher$F29t8_xYSK7h_6CpLRlp2y2yb1E(this, var1, var2, var4));
         }

      }

      public void disabled(DecoderCounters var1) {
         var1.ensureUpdated();
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$AudioRendererEventListener$EventDispatcher$jb22FSnmUl2pGG0LguQS_Wd_LWk(this, var1));
         }

      }

      public void enabled(DecoderCounters var1) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$AudioRendererEventListener$EventDispatcher$MUMUaHcEfIpwDLi9gxmScOQxifc(this, var1));
         }

      }

      public void inputFormatChanged(Format var1) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$AudioRendererEventListener$EventDispatcher$D7KvJbrpXrnWw4qzd_LI9ZtQytw(this, var1));
         }

      }

      // $FF: synthetic method
      public void lambda$audioSessionId$5$AudioRendererEventListener$EventDispatcher(int var1) {
         this.listener.onAudioSessionId(var1);
      }

      // $FF: synthetic method
      public void lambda$audioTrackUnderrun$3$AudioRendererEventListener$EventDispatcher(int var1, long var2, long var4) {
         this.listener.onAudioSinkUnderrun(var1, var2, var4);
      }

      // $FF: synthetic method
      public void lambda$decoderInitialized$1$AudioRendererEventListener$EventDispatcher(String var1, long var2, long var4) {
         this.listener.onAudioDecoderInitialized(var1, var2, var4);
      }

      // $FF: synthetic method
      public void lambda$disabled$4$AudioRendererEventListener$EventDispatcher(DecoderCounters var1) {
         var1.ensureUpdated();
         this.listener.onAudioDisabled(var1);
      }

      // $FF: synthetic method
      public void lambda$enabled$0$AudioRendererEventListener$EventDispatcher(DecoderCounters var1) {
         this.listener.onAudioEnabled(var1);
      }

      // $FF: synthetic method
      public void lambda$inputFormatChanged$2$AudioRendererEventListener$EventDispatcher(Format var1) {
         this.listener.onAudioInputFormatChanged(var1);
      }
   }
}
