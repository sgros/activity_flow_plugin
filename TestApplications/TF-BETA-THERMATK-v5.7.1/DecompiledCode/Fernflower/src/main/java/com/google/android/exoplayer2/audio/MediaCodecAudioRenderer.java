package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

@TargetApi(16)
public class MediaCodecAudioRenderer extends MediaCodecRenderer implements MediaClock {
   private boolean allowFirstBufferPositionDiscontinuity;
   private boolean allowPositionDiscontinuity;
   private final AudioSink audioSink;
   private int channelCount;
   private int codecMaxInputSize;
   private boolean codecNeedsDiscardChannelsWorkaround;
   private boolean codecNeedsEosBufferTimestampWorkaround;
   private final Context context;
   private long currentPositionUs;
   private int encoderDelay;
   private int encoderPadding;
   private final AudioRendererEventListener.EventDispatcher eventDispatcher;
   private long lastInputTimeUs;
   private boolean passthroughEnabled;
   private MediaFormat passthroughMediaFormat;
   private int pcmEncoding;
   private int pendingStreamChangeCount;
   private final long[] pendingStreamChangeTimesUs;

   public MediaCodecAudioRenderer(Context var1, MediaCodecSelector var2, DrmSessionManager var3, boolean var4, Handler var5, AudioRendererEventListener var6, AudioCapabilities var7, AudioProcessor... var8) {
      this(var1, var2, var3, var4, var5, var6, new DefaultAudioSink(var7, var8));
   }

   public MediaCodecAudioRenderer(Context var1, MediaCodecSelector var2, DrmSessionManager var3, boolean var4, Handler var5, AudioRendererEventListener var6, AudioSink var7) {
      super(1, var2, var3, var4, 44100.0F);
      this.context = var1.getApplicationContext();
      this.audioSink = var7;
      this.lastInputTimeUs = -9223372036854775807L;
      this.pendingStreamChangeTimesUs = new long[10];
      this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(var5, var6);
      var7.setListener(new MediaCodecAudioRenderer.AudioSinkListener());
   }

   private static boolean codecNeedsDiscardChannelsWorkaround(String var0) {
      boolean var1;
      if (Util.SDK_INT >= 24 || !"OMX.SEC.aac.dec".equals(var0) || !"samsung".equals(Util.MANUFACTURER) || !Util.DEVICE.startsWith("zeroflte") && !Util.DEVICE.startsWith("herolte") && !Util.DEVICE.startsWith("heroqlte")) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean codecNeedsEosBufferTimestampWorkaround(String var0) {
      boolean var1;
      if (Util.SDK_INT >= 21 || !"OMX.SEC.mp3.dec".equals(var0) || !"samsung".equals(Util.MANUFACTURER) || !Util.DEVICE.startsWith("baffin") && !Util.DEVICE.startsWith("grand") && !Util.DEVICE.startsWith("fortuna") && !Util.DEVICE.startsWith("gprimelte") && !Util.DEVICE.startsWith("j2y18lte") && !Util.DEVICE.startsWith("ms01")) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private int getCodecMaxInputSize(MediaCodecInfo var1, Format var2) {
      if ("OMX.google.raw.decoder".equals(var1.name)) {
         int var3 = Util.SDK_INT;
         if (var3 < 24 && (var3 != 23 || !Util.isTv(this.context))) {
            return -1;
         }
      }

      return var2.maxInputSize;
   }

   private void updateCurrentPosition() {
      long var1 = this.audioSink.getCurrentPositionUs(this.isEnded());
      if (var1 != Long.MIN_VALUE) {
         if (!this.allowPositionDiscontinuity) {
            var1 = Math.max(this.currentPositionUs, var1);
         }

         this.currentPositionUs = var1;
         this.allowPositionDiscontinuity = false;
      }

   }

   protected boolean allowPassthrough(int var1, String var2) {
      return this.audioSink.supportsOutput(var1, MimeTypes.getEncoding(var2));
   }

   protected boolean areCodecConfigurationCompatible(Format var1, Format var2) {
      boolean var3;
      if (Util.areEqual(var1.sampleMimeType, var2.sampleMimeType) && var1.channelCount == var2.channelCount && var1.sampleRate == var2.sampleRate && var1.initializationDataEquals(var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   protected int canKeepCodec(MediaCodec var1, MediaCodecInfo var2, Format var3, Format var4) {
      if (this.getCodecMaxInputSize(var2, var4) <= this.codecMaxInputSize && var3.encoderDelay == 0 && var3.encoderPadding == 0 && var4.encoderDelay == 0 && var4.encoderPadding == 0) {
         if (var2.isSeamlessAdaptationSupported(var3, var4, true)) {
            return 3;
         }

         if (this.areCodecConfigurationCompatible(var3, var4)) {
            return 1;
         }
      }

      return 0;
   }

   protected void configureCodec(MediaCodecInfo var1, MediaCodec var2, Format var3, MediaCrypto var4, float var5) {
      this.codecMaxInputSize = this.getCodecMaxInputSize(var1, var3, this.getStreamFormats());
      this.codecNeedsDiscardChannelsWorkaround = codecNeedsDiscardChannelsWorkaround(var1.name);
      this.codecNeedsEosBufferTimestampWorkaround = codecNeedsEosBufferTimestampWorkaround(var1.name);
      this.passthroughEnabled = var1.passthrough;
      String var6;
      if (this.passthroughEnabled) {
         var6 = "audio/raw";
      } else {
         var6 = var1.mimeType;
      }

      MediaFormat var7 = this.getMediaFormat(var3, var6, this.codecMaxInputSize, var5);
      var2.configure(var7, (Surface)null, var4, 0);
      if (this.passthroughEnabled) {
         this.passthroughMediaFormat = var7;
         this.passthroughMediaFormat.setString("mime", var3.sampleMimeType);
      } else {
         this.passthroughMediaFormat = null;
      }

   }

   protected int getCodecMaxInputSize(MediaCodecInfo var1, Format var2, Format[] var3) {
      int var4 = this.getCodecMaxInputSize(var1, var2);
      if (var3.length == 1) {
         return var4;
      } else {
         int var5 = var3.length;

         int var8;
         for(int var6 = 0; var6 < var5; var4 = var8) {
            Format var7 = var3[var6];
            var8 = var4;
            if (var1.isSeamlessAdaptationSupported(var2, var7, false)) {
               var8 = Math.max(var4, this.getCodecMaxInputSize(var1, var7));
            }

            ++var6;
         }

         return var4;
      }
   }

   protected float getCodecOperatingRateV23(float var1, Format var2, Format[] var3) {
      int var4 = var3.length;
      int var5 = 0;

      int var6;
      int var8;
      for(var6 = -1; var5 < var4; var6 = var8) {
         int var7 = var3[var5].sampleRate;
         var8 = var6;
         if (var7 != -1) {
            var8 = Math.max(var6, var7);
         }

         ++var5;
      }

      if (var6 == -1) {
         var1 = -1.0F;
      } else {
         var1 *= (float)var6;
      }

      return var1;
   }

   protected List getDecoderInfos(MediaCodecSelector var1, Format var2, boolean var3) throws MediaCodecUtil.DecoderQueryException {
      if (this.allowPassthrough(var2.channelCount, var2.sampleMimeType)) {
         MediaCodecInfo var4 = var1.getPassthroughDecoderInfo();
         if (var4 != null) {
            return Collections.singletonList(var4);
         }
      }

      return super.getDecoderInfos(var1, var2, var3);
   }

   public MediaClock getMediaClock() {
      return this;
   }

   @SuppressLint({"InlinedApi"})
   protected MediaFormat getMediaFormat(Format var1, String var2, int var3, float var4) {
      MediaFormat var5 = new MediaFormat();
      var5.setString("mime", var2);
      var5.setInteger("channel-count", var1.channelCount);
      var5.setInteger("sample-rate", var1.sampleRate);
      MediaFormatUtil.setCsdBuffers(var5, var1.initializationData);
      MediaFormatUtil.maybeSetInteger(var5, "max-input-size", var3);
      if (Util.SDK_INT >= 23) {
         var5.setInteger("priority", 0);
         if (var4 != -1.0F) {
            var5.setFloat("operating-rate", var4);
         }
      }

      return var5;
   }

   public PlaybackParameters getPlaybackParameters() {
      return this.audioSink.getPlaybackParameters();
   }

   public long getPositionUs() {
      if (this.getState() == 2) {
         this.updateCurrentPosition();
      }

      return this.currentPositionUs;
   }

   public void handleMessage(int var1, Object var2) throws ExoPlaybackException {
      if (var1 != 2) {
         if (var1 != 3) {
            if (var1 != 5) {
               super.handleMessage(var1, var2);
            } else {
               AuxEffectInfo var3 = (AuxEffectInfo)var2;
               this.audioSink.setAuxEffectInfo(var3);
            }
         } else {
            AudioAttributes var4 = (AudioAttributes)var2;
            this.audioSink.setAudioAttributes(var4);
         }
      } else {
         this.audioSink.setVolume((Float)var2);
      }

   }

   public boolean isEnded() {
      boolean var1;
      if (super.isEnded() && this.audioSink.isEnded()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isReady() {
      boolean var1;
      if (!this.audioSink.hasPendingData() && !super.isReady()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected void onAudioSessionId(int var1) {
   }

   protected void onAudioTrackPositionDiscontinuity() {
   }

   protected void onAudioTrackUnderrun(int var1, long var2, long var4) {
   }

   protected void onCodecInitialized(String var1, long var2, long var4) {
      this.eventDispatcher.decoderInitialized(var1, var2, var4);
   }

   protected void onDisabled() {
      try {
         this.lastInputTimeUs = -9223372036854775807L;
         this.pendingStreamChangeCount = 0;
         this.audioSink.flush();
      } finally {
         try {
            super.onDisabled();
         } finally {
            this.eventDispatcher.disabled(super.decoderCounters);
         }
      }

   }

   protected void onEnabled(boolean var1) throws ExoPlaybackException {
      super.onEnabled(var1);
      this.eventDispatcher.enabled(super.decoderCounters);
      int var2 = this.getConfiguration().tunnelingAudioSessionId;
      if (var2 != 0) {
         this.audioSink.enableTunnelingV21(var2);
      } else {
         this.audioSink.disableTunneling();
      }

   }

   protected void onInputFormatChanged(Format var1) throws ExoPlaybackException {
      super.onInputFormatChanged(var1);
      this.eventDispatcher.inputFormatChanged(var1);
      int var2;
      if ("audio/raw".equals(var1.sampleMimeType)) {
         var2 = var1.pcmEncoding;
      } else {
         var2 = 2;
      }

      this.pcmEncoding = var2;
      this.channelCount = var1.channelCount;
      this.encoderDelay = var1.encoderDelay;
      this.encoderPadding = var1.encoderPadding;
   }

   protected void onOutputFormatChanged(MediaCodec var1, MediaFormat var2) throws ExoPlaybackException {
      MediaFormat var8 = this.passthroughMediaFormat;
      int var3;
      if (var8 != null) {
         var3 = MimeTypes.getEncoding(var8.getString("mime"));
         var2 = this.passthroughMediaFormat;
      } else {
         var3 = this.pcmEncoding;
      }

      int var4;
      int var5;
      int[] var9;
      label30: {
         var4 = var2.getInteger("channel-count");
         var5 = var2.getInteger("sample-rate");
         if (this.codecNeedsDiscardChannelsWorkaround && var4 == 6) {
            int var6 = this.channelCount;
            if (var6 < 6) {
               int[] var10 = new int[var6];
               var6 = 0;

               while(true) {
                  var9 = var10;
                  if (var6 >= this.channelCount) {
                     break label30;
                  }

                  var10[var6] = var6++;
               }
            }
         }

         var9 = null;
      }

      try {
         this.audioSink.configure(var3, var4, var5, 0, var9, this.encoderDelay, this.encoderPadding);
      } catch (AudioSink.ConfigurationException var7) {
         throw ExoPlaybackException.createForRenderer(var7, this.getIndex());
      }
   }

   protected void onPositionReset(long var1, boolean var3) throws ExoPlaybackException {
      super.onPositionReset(var1, var3);
      this.audioSink.flush();
      this.currentPositionUs = var1;
      this.allowFirstBufferPositionDiscontinuity = true;
      this.allowPositionDiscontinuity = true;
      this.lastInputTimeUs = -9223372036854775807L;
      this.pendingStreamChangeCount = 0;
   }

   protected void onProcessedOutputBuffer(long var1) {
      while(this.pendingStreamChangeCount != 0 && var1 >= this.pendingStreamChangeTimesUs[0]) {
         this.audioSink.handleDiscontinuity();
         --this.pendingStreamChangeCount;
         long[] var3 = this.pendingStreamChangeTimesUs;
         System.arraycopy(var3, 1, var3, 0, this.pendingStreamChangeCount);
      }

   }

   protected void onQueueInputBuffer(DecoderInputBuffer var1) {
      if (this.allowFirstBufferPositionDiscontinuity && !var1.isDecodeOnly()) {
         if (Math.abs(var1.timeUs - this.currentPositionUs) > 500000L) {
            this.currentPositionUs = var1.timeUs;
         }

         this.allowFirstBufferPositionDiscontinuity = false;
      }

      this.lastInputTimeUs = Math.max(var1.timeUs, this.lastInputTimeUs);
   }

   protected void onReset() {
      try {
         super.onReset();
      } finally {
         this.audioSink.reset();
      }

   }

   protected void onStarted() {
      super.onStarted();
      this.audioSink.play();
   }

   protected void onStopped() {
      this.updateCurrentPosition();
      this.audioSink.pause();
      super.onStopped();
   }

   protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
      super.onStreamChanged(var1, var2);
      if (this.lastInputTimeUs != -9223372036854775807L) {
         int var4 = this.pendingStreamChangeCount;
         if (var4 == this.pendingStreamChangeTimesUs.length) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Too many stream changes, so dropping change at ");
            var5.append(this.pendingStreamChangeTimesUs[this.pendingStreamChangeCount - 1]);
            Log.w("MediaCodecAudioRenderer", var5.toString());
         } else {
            this.pendingStreamChangeCount = var4 + 1;
         }

         this.pendingStreamChangeTimesUs[this.pendingStreamChangeCount - 1] = this.lastInputTimeUs;
      }

   }

   protected boolean processOutputBuffer(long var1, long var3, MediaCodec var5, ByteBuffer var6, int var7, int var8, long var9, boolean var11, Format var12) throws ExoPlaybackException {
      if (this.codecNeedsEosBufferTimestampWorkaround && var9 == 0L && (var8 & 4) != 0) {
         var1 = this.lastInputTimeUs;
         if (var1 != -9223372036854775807L) {
            var9 = var1;
         }
      }

      if (this.passthroughEnabled && (var8 & 2) != 0) {
         var5.releaseOutputBuffer(var7, false);
         return true;
      } else {
         DecoderCounters var16;
         if (var11) {
            var5.releaseOutputBuffer(var7, false);
            var16 = super.decoderCounters;
            ++var16.skippedOutputBufferCount;
            this.audioSink.handleDiscontinuity();
            return true;
         } else {
            Object var15;
            try {
               if (this.audioSink.handleBuffer(var6, var9)) {
                  var5.releaseOutputBuffer(var7, false);
                  var16 = super.decoderCounters;
                  ++var16.renderedOutputBufferCount;
                  return true;
               }

               return false;
            } catch (AudioSink.InitializationException var13) {
               var15 = var13;
            } catch (AudioSink.WriteException var14) {
               var15 = var14;
            }

            throw ExoPlaybackException.createForRenderer((Exception)var15, this.getIndex());
         }
      }
   }

   protected void renderToEndOfStream() throws ExoPlaybackException {
      try {
         this.audioSink.playToEndOfStream();
      } catch (AudioSink.WriteException var2) {
         throw ExoPlaybackException.createForRenderer(var2, this.getIndex());
      }
   }

   public PlaybackParameters setPlaybackParameters(PlaybackParameters var1) {
      return this.audioSink.setPlaybackParameters(var1);
   }

   protected int supportsFormat(MediaCodecSelector var1, DrmSessionManager var2, Format var3) throws MediaCodecUtil.DecoderQueryException {
      String var4 = var3.sampleMimeType;
      if (!MimeTypes.isAudio(var4)) {
         return 0;
      } else {
         byte var5;
         if (Util.SDK_INT >= 21) {
            var5 = 32;
         } else {
            var5 = 0;
         }

         boolean var6 = BaseRenderer.supportsFormatDrm(var2, var3.drmInitData);
         byte var7 = 4;
         byte var8 = 8;
         if (var6 && this.allowPassthrough(var3.channelCount, var4) && var1.getPassthroughDecoderInfo() != null) {
            return var5 | 8 | 4;
         } else {
            boolean var9 = "audio/raw".equals(var4);
            byte var10 = 1;
            if ((!var9 || this.audioSink.supportsOutput(var3.channelCount, var3.pcmEncoding)) && this.audioSink.supportsOutput(var3.channelCount, 2)) {
               DrmInitData var14 = var3.drmInitData;
               boolean var12;
               if (var14 != null) {
                  int var11 = 0;
                  var9 = false;

                  while(true) {
                     var12 = var9;
                     if (var11 >= var14.schemeDataCount) {
                        break;
                     }

                     var9 |= var14.get(var11).requiresSecureDecryption;
                     ++var11;
                  }
               } else {
                  var12 = false;
               }

               List var15 = var1.getDecoderInfos(var3.sampleMimeType, var12);
               if (var15.isEmpty()) {
                  var5 = var10;
                  if (var12) {
                     var5 = var10;
                     if (!var1.getDecoderInfos(var3.sampleMimeType, false).isEmpty()) {
                        var5 = 2;
                     }
                  }

                  return var5;
               } else if (!var6) {
                  return 2;
               } else {
                  MediaCodecInfo var13 = (MediaCodecInfo)var15.get(0);
                  var9 = var13.isFormatSupported(var3);
                  byte var16 = var8;
                  if (var9) {
                     var16 = var8;
                     if (var13.isSeamlessAdaptationSupported(var3)) {
                        var16 = 16;
                     }
                  }

                  if (!var9) {
                     var7 = 3;
                  }

                  return var16 | var5 | var7;
               }
            } else {
               return 1;
            }
         }
      }
   }

   private final class AudioSinkListener implements AudioSink.Listener {
      private AudioSinkListener() {
      }

      // $FF: synthetic method
      AudioSinkListener(Object var2) {
         this();
      }

      public void onAudioSessionId(int var1) {
         MediaCodecAudioRenderer.this.eventDispatcher.audioSessionId(var1);
         MediaCodecAudioRenderer.this.onAudioSessionId(var1);
      }

      public void onPositionDiscontinuity() {
         MediaCodecAudioRenderer.this.onAudioTrackPositionDiscontinuity();
         MediaCodecAudioRenderer.this.allowPositionDiscontinuity = true;
      }

      public void onUnderrun(int var1, long var2, long var4) {
         MediaCodecAudioRenderer.this.eventDispatcher.audioTrackUnderrun(var1, var2, var4);
         MediaCodecAudioRenderer.this.onAudioTrackUnderrun(var1, var2, var4);
      }
   }
}
