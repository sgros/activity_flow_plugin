package com.google.android.exoplayer2;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MediaClock;
import java.io.IOException;

public abstract class BaseRenderer implements Renderer, RendererCapabilities {
   private RendererConfiguration configuration;
   private int index;
   private boolean readEndOfStream;
   private int state;
   private SampleStream stream;
   private Format[] streamFormats;
   private boolean streamIsFinal;
   private long streamOffsetUs;
   private final int trackType;

   public BaseRenderer(int var1) {
      this.trackType = var1;
      this.readEndOfStream = true;
   }

   protected static boolean supportsFormatDrm(DrmSessionManager var0, DrmInitData var1) {
      if (var1 == null) {
         return true;
      } else {
         return var0 == null ? false : var0.canAcquireSession(var1);
      }
   }

   public final void disable() {
      int var1 = this.state;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.state = 0;
      this.stream = null;
      this.streamFormats = null;
      this.streamIsFinal = false;
      this.onDisabled();
   }

   public final void enable(RendererConfiguration var1, Format[] var2, SampleStream var3, long var4, boolean var6, long var7) throws ExoPlaybackException {
      boolean var9;
      if (this.state == 0) {
         var9 = true;
      } else {
         var9 = false;
      }

      Assertions.checkState(var9);
      this.configuration = var1;
      this.state = 1;
      this.onEnabled(var6);
      this.replaceStream(var2, var3, var7);
      this.onPositionReset(var4, var6);
   }

   public final RendererCapabilities getCapabilities() {
      return this;
   }

   protected final RendererConfiguration getConfiguration() {
      return this.configuration;
   }

   protected final int getIndex() {
      return this.index;
   }

   public MediaClock getMediaClock() {
      return null;
   }

   public final int getState() {
      return this.state;
   }

   public final SampleStream getStream() {
      return this.stream;
   }

   protected final Format[] getStreamFormats() {
      return this.streamFormats;
   }

   public final int getTrackType() {
      return this.trackType;
   }

   public void handleMessage(int var1, Object var2) throws ExoPlaybackException {
   }

   public final boolean hasReadStreamToEnd() {
      return this.readEndOfStream;
   }

   public final boolean isCurrentStreamFinal() {
      return this.streamIsFinal;
   }

   protected final boolean isSourceReady() {
      boolean var1;
      if (this.readEndOfStream) {
         var1 = this.streamIsFinal;
      } else {
         var1 = this.stream.isReady();
      }

      return var1;
   }

   public final void maybeThrowStreamError() throws IOException {
      this.stream.maybeThrowError();
   }

   protected abstract void onDisabled();

   protected void onEnabled(boolean var1) throws ExoPlaybackException {
   }

   protected abstract void onPositionReset(long var1, boolean var3) throws ExoPlaybackException;

   protected void onReset() {
   }

   protected void onStarted() throws ExoPlaybackException {
   }

   protected void onStopped() throws ExoPlaybackException {
   }

   protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
   }

   protected final int readSource(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
      int var4 = this.stream.readData(var1, var2, var3);
      byte var5 = -4;
      if (var4 == -4) {
         if (var2.isEndOfStream()) {
            this.readEndOfStream = true;
            if (!this.streamIsFinal) {
               var5 = -3;
            }

            return var5;
         }

         var2.timeUs += this.streamOffsetUs;
      } else if (var4 == -5) {
         Format var8 = var1.format;
         long var6 = var8.subsampleOffsetUs;
         if (var6 != Long.MAX_VALUE) {
            var1.format = var8.copyWithSubsampleOffsetUs(var6 + this.streamOffsetUs);
         }
      }

      return var4;
   }

   public final void replaceStream(Format[] var1, SampleStream var2, long var3) throws ExoPlaybackException {
      Assertions.checkState(this.streamIsFinal ^ true);
      this.stream = var2;
      this.readEndOfStream = false;
      this.streamFormats = var1;
      this.streamOffsetUs = var3;
      this.onStreamChanged(var1, var3);
   }

   public final void reset() {
      boolean var1;
      if (this.state == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      Assertions.checkState(var1);
      this.onReset();
   }

   public final void resetPosition(long var1) throws ExoPlaybackException {
      this.streamIsFinal = false;
      this.readEndOfStream = false;
      this.onPositionReset(var1, false);
   }

   public final void setCurrentStreamFinal() {
      this.streamIsFinal = true;
   }

   public final void setIndex(int var1) {
      this.index = var1;
   }

   // $FF: synthetic method
   public void setOperatingRate(float var1) throws ExoPlaybackException {
      Renderer$_CC.$default$setOperatingRate(this, var1);
   }

   protected int skipSource(long var1) {
      return this.stream.skipData(var1 - this.streamOffsetUs);
   }

   public final void start() throws ExoPlaybackException {
      int var1 = this.state;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.state = 2;
      this.onStarted();
   }

   public final void stop() throws ExoPlaybackException {
      boolean var1;
      if (this.state == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      Assertions.checkState(var1);
      this.state = 1;
      this.onStopped();
   }

   public int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
      return 0;
   }
}
