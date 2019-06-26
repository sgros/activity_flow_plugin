package com.google.android.exoplayer2.video;

import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;

public interface VideoRendererEventListener {
   void onDroppedFrames(int var1, long var2);

   void onRenderedFirstFrame(Surface var1);

   void onVideoDecoderInitialized(String var1, long var2, long var4);

   void onVideoDisabled(DecoderCounters var1);

   void onVideoEnabled(DecoderCounters var1);

   void onVideoInputFormatChanged(Format var1);

   void onVideoSizeChanged(int var1, int var2, int var3, float var4);

   public static final class EventDispatcher {
      private final Handler handler;
      private final VideoRendererEventListener listener;

      public EventDispatcher(Handler var1, VideoRendererEventListener var2) {
         if (var2 != null) {
            Assertions.checkNotNull(var1);
            var1 = (Handler)var1;
         } else {
            var1 = null;
         }

         this.handler = var1;
         this.listener = var2;
      }

      public void decoderInitialized(String var1, long var2, long var4) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$Y232CA7hogfrRJjYu2VeUSxg0VQ(this, var1, var2, var4));
         }

      }

      public void disabled(DecoderCounters var1) {
         var1.ensureUpdated();
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$qTQ_0WnG_WelRJ9iR8L0OaiS0Go(this, var1));
         }

      }

      public void droppedFrames(int var1, long var2) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$wpJzum9Nim_WREQi3I6t6RZgGzs(this, var1, var2));
         }

      }

      public void enabled(DecoderCounters var1) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$Zf6ofdxzBBJ5SL288lE0HglRj8g(this, var1));
         }

      }

      public void inputFormatChanged(Format var1) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$26y6c6BFFT4OL6bJiMmdsfxDEMQ(this, var1));
         }

      }

      // $FF: synthetic method
      public void lambda$decoderInitialized$1$VideoRendererEventListener$EventDispatcher(String var1, long var2, long var4) {
         this.listener.onVideoDecoderInitialized(var1, var2, var4);
      }

      // $FF: synthetic method
      public void lambda$disabled$6$VideoRendererEventListener$EventDispatcher(DecoderCounters var1) {
         var1.ensureUpdated();
         this.listener.onVideoDisabled(var1);
      }

      // $FF: synthetic method
      public void lambda$droppedFrames$3$VideoRendererEventListener$EventDispatcher(int var1, long var2) {
         this.listener.onDroppedFrames(var1, var2);
      }

      // $FF: synthetic method
      public void lambda$enabled$0$VideoRendererEventListener$EventDispatcher(DecoderCounters var1) {
         this.listener.onVideoEnabled(var1);
      }

      // $FF: synthetic method
      public void lambda$inputFormatChanged$2$VideoRendererEventListener$EventDispatcher(Format var1) {
         this.listener.onVideoInputFormatChanged(var1);
      }

      // $FF: synthetic method
      public void lambda$renderedFirstFrame$5$VideoRendererEventListener$EventDispatcher(Surface var1) {
         this.listener.onRenderedFirstFrame(var1);
      }

      // $FF: synthetic method
      public void lambda$videoSizeChanged$4$VideoRendererEventListener$EventDispatcher(int var1, int var2, int var3, float var4) {
         this.listener.onVideoSizeChanged(var1, var2, var3, var4);
      }

      public void renderedFirstFrame(Surface var1) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$SFK5uUI0PHTm3Dg6Wdc1eRaQ9xk(this, var1));
         }

      }

      public void videoSizeChanged(int var1, int var2, int var3, float var4) {
         if (this.listener != null) {
            this.handler.post(new _$$Lambda$VideoRendererEventListener$EventDispatcher$TaBV3X3b5lKElsQ7tczViKAyQ3w(this, var1, var2, var3, var4));
         }

      }
   }
}
