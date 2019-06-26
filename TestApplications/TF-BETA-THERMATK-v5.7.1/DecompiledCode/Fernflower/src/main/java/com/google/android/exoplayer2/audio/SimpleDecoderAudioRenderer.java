package com.google.android.exoplayer2.audio;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.List;

public abstract class SimpleDecoderAudioRenderer extends BaseRenderer implements MediaClock {
   private static final int REINITIALIZATION_STATE_NONE = 0;
   private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
   private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
   private boolean allowFirstBufferPositionDiscontinuity;
   private boolean allowPositionDiscontinuity;
   private final AudioSink audioSink;
   private boolean audioTrackNeedsConfigure;
   private long currentPositionUs;
   private SimpleDecoder decoder;
   private DecoderCounters decoderCounters;
   private DrmSession decoderDrmSession;
   private boolean decoderReceivedBuffers;
   private int decoderReinitializationState;
   private final DrmSessionManager drmSessionManager;
   private int encoderDelay;
   private int encoderPadding;
   private final AudioRendererEventListener.EventDispatcher eventDispatcher;
   private final DecoderInputBuffer flagsOnlyBuffer;
   private final FormatHolder formatHolder;
   private DecoderInputBuffer inputBuffer;
   private Format inputFormat;
   private boolean inputStreamEnded;
   private SimpleOutputBuffer outputBuffer;
   private boolean outputStreamEnded;
   private final boolean playClearSamplesWithoutKeys;
   private DrmSession sourceDrmSession;
   private boolean waitingForKeys;

   public SimpleDecoderAudioRenderer() {
      this((Handler)null, (AudioRendererEventListener)null, (AudioProcessor[])());
   }

   public SimpleDecoderAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioCapabilities var3) {
      this(var1, var2, var3, (DrmSessionManager)null, false);
   }

   public SimpleDecoderAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioCapabilities var3, DrmSessionManager var4, boolean var5, AudioProcessor... var6) {
      this(var1, var2, var4, var5, new DefaultAudioSink(var3, var6));
   }

   public SimpleDecoderAudioRenderer(Handler var1, AudioRendererEventListener var2, DrmSessionManager var3, boolean var4, AudioSink var5) {
      super(1);
      this.drmSessionManager = var3;
      this.playClearSamplesWithoutKeys = var4;
      this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(var1, var2);
      this.audioSink = var5;
      var5.setListener(new SimpleDecoderAudioRenderer.AudioSinkListener());
      this.formatHolder = new FormatHolder();
      this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
      this.decoderReinitializationState = 0;
      this.audioTrackNeedsConfigure = true;
   }

   public SimpleDecoderAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioProcessor... var3) {
      this(var1, var2, (AudioCapabilities)null, (DrmSessionManager)null, false, var3);
   }

   private boolean drainOutputBuffer() throws ExoPlaybackException, AudioDecoderException, AudioSink.ConfigurationException, AudioSink.InitializationException, AudioSink.WriteException {
      DecoderCounters var4;
      if (this.outputBuffer == null) {
         this.outputBuffer = (SimpleOutputBuffer)this.decoder.dequeueOutputBuffer();
         SimpleOutputBuffer var1 = this.outputBuffer;
         if (var1 == null) {
            return false;
         }

         int var2 = var1.skippedOutputBufferCount;
         if (var2 > 0) {
            var4 = this.decoderCounters;
            var4.skippedOutputBufferCount += var2;
            this.audioSink.handleDiscontinuity();
         }
      }

      if (this.outputBuffer.isEndOfStream()) {
         if (this.decoderReinitializationState == 2) {
            this.releaseDecoder();
            this.maybeInitDecoder();
            this.audioTrackNeedsConfigure = true;
         } else {
            this.outputBuffer.release();
            this.outputBuffer = null;
            this.processEndOfStream();
         }

         return false;
      } else {
         if (this.audioTrackNeedsConfigure) {
            Format var5 = this.getOutputFormat();
            this.audioSink.configure(var5.pcmEncoding, var5.channelCount, var5.sampleRate, 0, (int[])null, this.encoderDelay, this.encoderPadding);
            this.audioTrackNeedsConfigure = false;
         }

         AudioSink var6 = this.audioSink;
         SimpleOutputBuffer var3 = this.outputBuffer;
         if (var6.handleBuffer(var3.data, var3.timeUs)) {
            var4 = this.decoderCounters;
            ++var4.renderedOutputBufferCount;
            this.outputBuffer.release();
            this.outputBuffer = null;
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean feedInputBuffer() throws AudioDecoderException, ExoPlaybackException {
      SimpleDecoder var1 = this.decoder;
      if (var1 != null && this.decoderReinitializationState != 2 && !this.inputStreamEnded) {
         if (this.inputBuffer == null) {
            this.inputBuffer = var1.dequeueInputBuffer();
            if (this.inputBuffer == null) {
               return false;
            }
         }

         if (this.decoderReinitializationState == 1) {
            this.inputBuffer.setFlags(4);
            this.decoder.queueInputBuffer(this.inputBuffer);
            this.inputBuffer = null;
            this.decoderReinitializationState = 2;
            return false;
         } else {
            int var2;
            if (this.waitingForKeys) {
               var2 = -4;
            } else {
               var2 = this.readSource(this.formatHolder, this.inputBuffer, false);
            }

            if (var2 == -3) {
               return false;
            } else if (var2 == -5) {
               this.onInputFormatChanged(this.formatHolder.format);
               return true;
            } else if (this.inputBuffer.isEndOfStream()) {
               this.inputStreamEnded = true;
               this.decoder.queueInputBuffer(this.inputBuffer);
               this.inputBuffer = null;
               return false;
            } else {
               this.waitingForKeys = this.shouldWaitForKeys(this.inputBuffer.isEncrypted());
               if (this.waitingForKeys) {
                  return false;
               } else {
                  this.inputBuffer.flip();
                  this.onQueueInputBuffer(this.inputBuffer);
                  this.decoder.queueInputBuffer(this.inputBuffer);
                  this.decoderReceivedBuffers = true;
                  DecoderCounters var3 = this.decoderCounters;
                  ++var3.inputBufferCount;
                  this.inputBuffer = null;
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   private void flushDecoder() throws ExoPlaybackException {
      this.waitingForKeys = false;
      if (this.decoderReinitializationState != 0) {
         this.releaseDecoder();
         this.maybeInitDecoder();
      } else {
         this.inputBuffer = null;
         SimpleOutputBuffer var1 = this.outputBuffer;
         if (var1 != null) {
            var1.release();
            this.outputBuffer = null;
         }

         this.decoder.flush();
         this.decoderReceivedBuffers = false;
      }

   }

   private void maybeInitDecoder() throws ExoPlaybackException {
      if (this.decoder == null) {
         this.setDecoderDrmSession(this.sourceDrmSession);
         ExoMediaCrypto var1 = null;
         DrmSession var2 = this.decoderDrmSession;
         if (var2 != null) {
            ExoMediaCrypto var8 = var2.getMediaCrypto();
            var1 = var8;
            if (var8 == null) {
               if (this.decoderDrmSession.getError() == null) {
                  return;
               }

               var1 = var8;
            }
         }

         try {
            long var3 = SystemClock.elapsedRealtime();
            TraceUtil.beginSection("createAudioDecoder");
            this.decoder = this.createDecoder(this.inputFormat, var1);
            TraceUtil.endSection();
            long var5 = SystemClock.elapsedRealtime();
            this.eventDispatcher.decoderInitialized(this.decoder.getName(), var5, var5 - var3);
            DecoderCounters var9 = this.decoderCounters;
            ++var9.decoderInitCount;
         } catch (AudioDecoderException var7) {
            throw ExoPlaybackException.createForRenderer(var7, this.getIndex());
         }
      }
   }

   private void onInputFormatChanged(Format var1) throws ExoPlaybackException {
      Format var2 = this.inputFormat;
      this.inputFormat = var1;
      DrmInitData var3 = this.inputFormat.drmInitData;
      DrmInitData var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = var2.drmInitData;
      }

      if (Util.areEqual(var3, var4) ^ true) {
         if (this.inputFormat.drmInitData != null) {
            DrmSessionManager var5 = this.drmSessionManager;
            if (var5 == null) {
               throw ExoPlaybackException.createForRenderer(new IllegalStateException("Media requires a DrmSessionManager"), this.getIndex());
            }

            DrmSession var6 = var5.acquireSession(Looper.myLooper(), var1.drmInitData);
            if (var6 == this.decoderDrmSession || var6 == this.sourceDrmSession) {
               this.drmSessionManager.releaseSession(var6);
            }

            this.setSourceDrmSession(var6);
         } else {
            this.setSourceDrmSession((DrmSession)null);
         }
      }

      if (this.decoderReceivedBuffers) {
         this.decoderReinitializationState = 1;
      } else {
         this.releaseDecoder();
         this.maybeInitDecoder();
         this.audioTrackNeedsConfigure = true;
      }

      this.encoderDelay = var1.encoderDelay;
      this.encoderPadding = var1.encoderPadding;
      this.eventDispatcher.inputFormatChanged(var1);
   }

   private void onQueueInputBuffer(DecoderInputBuffer var1) {
      if (this.allowFirstBufferPositionDiscontinuity && !var1.isDecodeOnly()) {
         if (Math.abs(var1.timeUs - this.currentPositionUs) > 500000L) {
            this.currentPositionUs = var1.timeUs;
         }

         this.allowFirstBufferPositionDiscontinuity = false;
      }

   }

   private void processEndOfStream() throws ExoPlaybackException {
      this.outputStreamEnded = true;

      try {
         this.audioSink.playToEndOfStream();
      } catch (AudioSink.WriteException var2) {
         throw ExoPlaybackException.createForRenderer(var2, this.getIndex());
      }
   }

   private void releaseDecoder() {
      this.inputBuffer = null;
      this.outputBuffer = null;
      this.decoderReinitializationState = 0;
      this.decoderReceivedBuffers = false;
      SimpleDecoder var1 = this.decoder;
      if (var1 != null) {
         var1.release();
         this.decoder = null;
         DecoderCounters var2 = this.decoderCounters;
         ++var2.decoderReleaseCount;
      }

      this.setDecoderDrmSession((DrmSession)null);
   }

   private void releaseDrmSessionIfUnused(DrmSession var1) {
      if (var1 != null && var1 != this.decoderDrmSession && var1 != this.sourceDrmSession) {
         this.drmSessionManager.releaseSession(var1);
      }

   }

   private void setDecoderDrmSession(DrmSession var1) {
      DrmSession var2 = this.decoderDrmSession;
      this.decoderDrmSession = var1;
      this.releaseDrmSessionIfUnused(var2);
   }

   private void setSourceDrmSession(DrmSession var1) {
      DrmSession var2 = this.sourceDrmSession;
      this.sourceDrmSession = var1;
      this.releaseDrmSessionIfUnused(var2);
   }

   private boolean shouldWaitForKeys(boolean var1) throws ExoPlaybackException {
      if (this.decoderDrmSession == null || !var1 && this.playClearSamplesWithoutKeys) {
         return false;
      } else {
         int var2 = this.decoderDrmSession.getState();
         var1 = true;
         if (var2 != 1) {
            if (var2 == 4) {
               var1 = false;
            }

            return var1;
         } else {
            throw ExoPlaybackException.createForRenderer(this.decoderDrmSession.getError(), this.getIndex());
         }
      }
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

   protected abstract SimpleDecoder createDecoder(Format var1, ExoMediaCrypto var2) throws AudioDecoderException;

   public MediaClock getMediaClock() {
      return this;
   }

   protected Format getOutputFormat() {
      Format var1 = this.inputFormat;
      return Format.createAudioSampleFormat((String)null, "audio/raw", (String)null, -1, -1, var1.channelCount, var1.sampleRate, 2, (List)null, (DrmInitData)null, 0, (String)null);
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
      if (this.outputStreamEnded && this.audioSink.isEnded()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isReady() {
      boolean var1;
      if (this.audioSink.hasPendingData() || this.inputFormat != null && !this.waitingForKeys && (this.isSourceReady() || this.outputBuffer != null)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void onAudioSessionId(int var1) {
   }

   protected void onAudioTrackPositionDiscontinuity() {
   }

   protected void onAudioTrackUnderrun(int var1, long var2, long var4) {
   }

   protected void onDisabled() {
      this.inputFormat = null;
      this.audioTrackNeedsConfigure = true;
      this.waitingForKeys = false;

      try {
         this.setSourceDrmSession((DrmSession)null);
         this.releaseDecoder();
         this.audioSink.reset();
      } finally {
         this.eventDispatcher.disabled(this.decoderCounters);
      }

   }

   protected void onEnabled(boolean var1) throws ExoPlaybackException {
      this.decoderCounters = new DecoderCounters();
      this.eventDispatcher.enabled(this.decoderCounters);
      int var2 = this.getConfiguration().tunnelingAudioSessionId;
      if (var2 != 0) {
         this.audioSink.enableTunnelingV21(var2);
      } else {
         this.audioSink.disableTunneling();
      }

   }

   protected void onPositionReset(long var1, boolean var3) throws ExoPlaybackException {
      this.audioSink.flush();
      this.currentPositionUs = var1;
      this.allowFirstBufferPositionDiscontinuity = true;
      this.allowPositionDiscontinuity = true;
      this.inputStreamEnded = false;
      this.outputStreamEnded = false;
      if (this.decoder != null) {
         this.flushDecoder();
      }

   }

   protected void onStarted() {
      this.audioSink.play();
   }

   protected void onStopped() {
      this.updateCurrentPosition();
      this.audioSink.pause();
   }

   public void render(long var1, long var3) throws ExoPlaybackException {
      if (this.outputStreamEnded) {
         try {
            this.audioSink.playToEndOfStream();
         } catch (AudioSink.WriteException var7) {
            throw ExoPlaybackException.createForRenderer(var7, this.getIndex());
         }
      } else {
         if (this.inputFormat == null) {
            this.flagsOnlyBuffer.clear();
            int var6 = this.readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (var6 != -5) {
               if (var6 == -4) {
                  Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                  this.inputStreamEnded = true;
                  this.processEndOfStream();
               }

               return;
            }

            this.onInputFormatChanged(this.formatHolder.format);
         }

         this.maybeInitDecoder();
         if (this.decoder != null) {
            label119: {
               Object var5;
               AudioDecoderException var26;
               label120: {
                  AudioSink.ConfigurationException var25;
                  label121: {
                     AudioSink.InitializationException var24;
                     label122: {
                        AudioSink.WriteException var10000;
                        label106: {
                           boolean var10001;
                           try {
                              TraceUtil.beginSection("drainAndFeed");
                           } catch (AudioDecoderException var12) {
                              var26 = var12;
                              var10001 = false;
                              break label120;
                           } catch (AudioSink.ConfigurationException var13) {
                              var25 = var13;
                              var10001 = false;
                              break label121;
                           } catch (AudioSink.InitializationException var14) {
                              var24 = var14;
                              var10001 = false;
                              break label122;
                           } catch (AudioSink.WriteException var15) {
                              var10000 = var15;
                              var10001 = false;
                              break label106;
                           }

                           while(true) {
                              try {
                                 if (!this.drainOutputBuffer()) {
                                    break;
                                 }
                              } catch (AudioDecoderException var20) {
                                 var26 = var20;
                                 var10001 = false;
                                 break label120;
                              } catch (AudioSink.ConfigurationException var21) {
                                 var25 = var21;
                                 var10001 = false;
                                 break label121;
                              } catch (AudioSink.InitializationException var22) {
                                 var24 = var22;
                                 var10001 = false;
                                 break label122;
                              } catch (AudioSink.WriteException var23) {
                                 var10000 = var23;
                                 var10001 = false;
                                 break label106;
                              }
                           }

                           while(true) {
                              try {
                                 if (this.feedInputBuffer()) {
                                    continue;
                                 }
                              } catch (AudioDecoderException var16) {
                                 var26 = var16;
                                 var10001 = false;
                                 break label120;
                              } catch (AudioSink.ConfigurationException var17) {
                                 var25 = var17;
                                 var10001 = false;
                                 break label121;
                              } catch (AudioSink.InitializationException var18) {
                                 var24 = var18;
                                 var10001 = false;
                                 break label122;
                              } catch (AudioSink.WriteException var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 TraceUtil.endSection();
                                 break label119;
                              } catch (AudioDecoderException var8) {
                                 var26 = var8;
                                 var10001 = false;
                                 break label120;
                              } catch (AudioSink.ConfigurationException var9) {
                                 var25 = var9;
                                 var10001 = false;
                                 break label121;
                              } catch (AudioSink.InitializationException var10) {
                                 var24 = var10;
                                 var10001 = false;
                                 break label122;
                              } catch (AudioSink.WriteException var11) {
                                 var10000 = var11;
                                 var10001 = false;
                                 break;
                              }
                           }
                        }

                        var5 = var10000;
                        throw ExoPlaybackException.createForRenderer((Exception)var5, this.getIndex());
                     }

                     var5 = var24;
                     throw ExoPlaybackException.createForRenderer((Exception)var5, this.getIndex());
                  }

                  var5 = var25;
                  throw ExoPlaybackException.createForRenderer((Exception)var5, this.getIndex());
               }

               var5 = var26;
               throw ExoPlaybackException.createForRenderer((Exception)var5, this.getIndex());
            }

            this.decoderCounters.ensureUpdated();
         }

      }
   }

   public PlaybackParameters setPlaybackParameters(PlaybackParameters var1) {
      return this.audioSink.setPlaybackParameters(var1);
   }

   public final int supportsFormat(Format var1) {
      boolean var2 = MimeTypes.isAudio(var1.sampleMimeType);
      byte var3 = 0;
      if (!var2) {
         return 0;
      } else {
         int var4 = this.supportsFormatInternal(this.drmSessionManager, var1);
         if (var4 <= 2) {
            return var4;
         } else {
            if (Util.SDK_INT >= 21) {
               var3 = 32;
            }

            return var4 | var3 | 8;
         }
      }
   }

   protected abstract int supportsFormatInternal(DrmSessionManager var1, Format var2);

   protected final boolean supportsOutput(int var1, int var2) {
      return this.audioSink.supportsOutput(var1, var2);
   }

   private final class AudioSinkListener implements AudioSink.Listener {
      private AudioSinkListener() {
      }

      // $FF: synthetic method
      AudioSinkListener(Object var2) {
         this();
      }

      public void onAudioSessionId(int var1) {
         SimpleDecoderAudioRenderer.this.eventDispatcher.audioSessionId(var1);
         SimpleDecoderAudioRenderer.this.onAudioSessionId(var1);
      }

      public void onPositionDiscontinuity() {
         SimpleDecoderAudioRenderer.this.onAudioTrackPositionDiscontinuity();
         SimpleDecoderAudioRenderer.this.allowPositionDiscontinuity = true;
      }

      public void onUnderrun(int var1, long var2, long var4) {
         SimpleDecoderAudioRenderer.this.eventDispatcher.audioTrackUnderrun(var1, var2, var4);
         SimpleDecoderAudioRenderer.this.onAudioTrackUnderrun(var1, var2, var4);
      }
   }
}
