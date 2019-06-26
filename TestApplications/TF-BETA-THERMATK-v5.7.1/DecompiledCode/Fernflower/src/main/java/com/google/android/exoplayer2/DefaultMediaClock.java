package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.StandaloneMediaClock;

final class DefaultMediaClock implements MediaClock {
   private final DefaultMediaClock.PlaybackParameterListener listener;
   private MediaClock rendererClock;
   private Renderer rendererClockSource;
   private final StandaloneMediaClock standaloneMediaClock;

   public DefaultMediaClock(DefaultMediaClock.PlaybackParameterListener var1, Clock var2) {
      this.listener = var1;
      this.standaloneMediaClock = new StandaloneMediaClock(var2);
   }

   private void ensureSynced() {
      long var1 = this.rendererClock.getPositionUs();
      this.standaloneMediaClock.resetPosition(var1);
      PlaybackParameters var3 = this.rendererClock.getPlaybackParameters();
      if (!var3.equals(this.standaloneMediaClock.getPlaybackParameters())) {
         this.standaloneMediaClock.setPlaybackParameters(var3);
         this.listener.onPlaybackParametersChanged(var3);
      }

   }

   private boolean isUsingRendererClock() {
      Renderer var1 = this.rendererClockSource;
      boolean var2;
      if (var1 == null || var1.isEnded() || !this.rendererClockSource.isReady() && this.rendererClockSource.hasReadStreamToEnd()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public PlaybackParameters getPlaybackParameters() {
      MediaClock var1 = this.rendererClock;
      PlaybackParameters var2;
      if (var1 != null) {
         var2 = var1.getPlaybackParameters();
      } else {
         var2 = this.standaloneMediaClock.getPlaybackParameters();
      }

      return var2;
   }

   public long getPositionUs() {
      return this.isUsingRendererClock() ? this.rendererClock.getPositionUs() : this.standaloneMediaClock.getPositionUs();
   }

   public void onRendererDisabled(Renderer var1) {
      if (var1 == this.rendererClockSource) {
         this.rendererClock = null;
         this.rendererClockSource = null;
      }

   }

   public void onRendererEnabled(Renderer var1) throws ExoPlaybackException {
      MediaClock var2 = var1.getMediaClock();
      if (var2 != null) {
         MediaClock var3 = this.rendererClock;
         if (var2 != var3) {
            if (var3 != null) {
               throw ExoPlaybackException.createForUnexpected(new IllegalStateException("Multiple renderer media clocks enabled."));
            }

            this.rendererClock = var2;
            this.rendererClockSource = var1;
            this.rendererClock.setPlaybackParameters(this.standaloneMediaClock.getPlaybackParameters());
            this.ensureSynced();
         }
      }

   }

   public void resetPosition(long var1) {
      this.standaloneMediaClock.resetPosition(var1);
   }

   public PlaybackParameters setPlaybackParameters(PlaybackParameters var1) {
      MediaClock var2 = this.rendererClock;
      PlaybackParameters var3 = var1;
      if (var2 != null) {
         var3 = var2.setPlaybackParameters(var1);
      }

      this.standaloneMediaClock.setPlaybackParameters(var3);
      this.listener.onPlaybackParametersChanged(var3);
      return var3;
   }

   public void start() {
      this.standaloneMediaClock.start();
   }

   public void stop() {
      this.standaloneMediaClock.stop();
   }

   public long syncAndGetPositionUs() {
      if (this.isUsingRendererClock()) {
         this.ensureSynced();
         return this.rendererClock.getPositionUs();
      } else {
         return this.standaloneMediaClock.getPositionUs();
      }
   }

   public interface PlaybackParameterListener {
      void onPlaybackParametersChanged(PlaybackParameters var1);
   }
}
