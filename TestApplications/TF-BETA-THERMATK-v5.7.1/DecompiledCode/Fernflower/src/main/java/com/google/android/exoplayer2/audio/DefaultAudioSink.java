package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.AudioAttributes.Builder;
import android.os.ConditionVariable;
import android.os.SystemClock;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class DefaultAudioSink implements AudioSink {
   public static boolean enablePreV21AudioSessionWorkaround;
   public static boolean failOnSpuriousAudioTimestamp;
   private AudioProcessor[] activeAudioProcessors;
   private PlaybackParameters afterDrainPlaybackParameters;
   private AudioAttributes audioAttributes;
   private final AudioCapabilities audioCapabilities;
   private final DefaultAudioSink.AudioProcessorChain audioProcessorChain;
   private int audioSessionId;
   private AudioTrack audioTrack;
   private final AudioTrackPositionTracker audioTrackPositionTracker;
   private AuxEffectInfo auxEffectInfo;
   private ByteBuffer avSyncHeader;
   private int bufferSize;
   private int bytesUntilNextAvSync;
   private boolean canApplyPlaybackParameters;
   private final ChannelMappingAudioProcessor channelMappingAudioProcessor;
   private int drainingAudioProcessorIndex;
   private final boolean enableConvertHighResIntPcmToFloat;
   private int framesPerEncodedSample;
   private boolean handledEndOfStream;
   private ByteBuffer inputBuffer;
   private int inputSampleRate;
   private boolean isInputPcm;
   private AudioTrack keepSessionIdAudioTrack;
   private long lastFeedElapsedRealtimeMs;
   private AudioSink.Listener listener;
   private ByteBuffer outputBuffer;
   private ByteBuffer[] outputBuffers;
   private int outputChannelConfig;
   private int outputEncoding;
   private int outputPcmFrameSize;
   private int outputSampleRate;
   private int pcmFrameSize;
   private PlaybackParameters playbackParameters;
   private final ArrayDeque playbackParametersCheckpoints;
   private long playbackParametersOffsetUs;
   private long playbackParametersPositionUs;
   private boolean playing;
   private byte[] preV21OutputBuffer;
   private int preV21OutputBufferOffset;
   private boolean processingEnabled;
   private final ConditionVariable releasingConditionVariable;
   private boolean shouldConvertHighResIntPcmToFloat;
   private int startMediaTimeState;
   private long startMediaTimeUs;
   private long submittedEncodedFrames;
   private long submittedPcmBytes;
   private final AudioProcessor[] toFloatPcmAvailableAudioProcessors;
   private final AudioProcessor[] toIntPcmAvailableAudioProcessors;
   private final TrimmingAudioProcessor trimmingAudioProcessor;
   private boolean tunneling;
   private float volume;
   private long writtenEncodedFrames;
   private long writtenPcmBytes;

   public DefaultAudioSink(AudioCapabilities var1, DefaultAudioSink.AudioProcessorChain var2, boolean var3) {
      this.audioCapabilities = var1;
      Assertions.checkNotNull(var2);
      this.audioProcessorChain = (DefaultAudioSink.AudioProcessorChain)var2;
      this.enableConvertHighResIntPcmToFloat = var3;
      this.releasingConditionVariable = new ConditionVariable(true);
      this.audioTrackPositionTracker = new AudioTrackPositionTracker(new DefaultAudioSink.PositionTrackerListener());
      this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
      this.trimmingAudioProcessor = new TrimmingAudioProcessor();
      ArrayList var4 = new ArrayList();
      Collections.addAll(var4, new AudioProcessor[]{new ResamplingAudioProcessor(), this.channelMappingAudioProcessor, this.trimmingAudioProcessor});
      Collections.addAll(var4, var2.getAudioProcessors());
      this.toIntPcmAvailableAudioProcessors = (AudioProcessor[])var4.toArray(new AudioProcessor[0]);
      this.toFloatPcmAvailableAudioProcessors = new AudioProcessor[]{new FloatResamplingAudioProcessor()};
      this.volume = 1.0F;
      this.startMediaTimeState = 0;
      this.audioAttributes = AudioAttributes.DEFAULT;
      this.audioSessionId = 0;
      this.auxEffectInfo = new AuxEffectInfo(0, 0.0F);
      this.playbackParameters = PlaybackParameters.DEFAULT;
      this.drainingAudioProcessorIndex = -1;
      this.activeAudioProcessors = new AudioProcessor[0];
      this.outputBuffers = new ByteBuffer[0];
      this.playbackParametersCheckpoints = new ArrayDeque();
   }

   public DefaultAudioSink(AudioCapabilities var1, AudioProcessor[] var2) {
      this(var1, var2, false);
   }

   public DefaultAudioSink(AudioCapabilities var1, AudioProcessor[] var2, boolean var3) {
      this(var1, (DefaultAudioSink.AudioProcessorChain)(new DefaultAudioSink.DefaultAudioProcessorChain(var2)), var3);
   }

   private long applySkipping(long var1) {
      return var1 + this.framesToDurationUs(this.audioProcessorChain.getSkippedOutputFrameCount());
   }

   private long applySpeedup(long var1) {
      DefaultAudioSink.PlaybackParametersCheckpoint var3;
      for(var3 = null; !this.playbackParametersCheckpoints.isEmpty() && var1 >= ((DefaultAudioSink.PlaybackParametersCheckpoint)this.playbackParametersCheckpoints.getFirst()).positionUs; var3 = (DefaultAudioSink.PlaybackParametersCheckpoint)this.playbackParametersCheckpoints.remove()) {
      }

      if (var3 != null) {
         this.playbackParameters = var3.playbackParameters;
         this.playbackParametersPositionUs = var3.positionUs;
         this.playbackParametersOffsetUs = var3.mediaTimeUs - this.startMediaTimeUs;
      }

      if (this.playbackParameters.speed == 1.0F) {
         return var1 + this.playbackParametersOffsetUs - this.playbackParametersPositionUs;
      } else {
         long var4;
         long var6;
         if (this.playbackParametersCheckpoints.isEmpty()) {
            var4 = this.playbackParametersOffsetUs;
            var6 = this.audioProcessorChain.getMediaDuration(var1 - this.playbackParametersPositionUs);
            var1 = var4;
         } else {
            var4 = this.playbackParametersOffsetUs;
            var6 = Util.getMediaDurationForPlayoutDuration(var1 - this.playbackParametersPositionUs, this.playbackParameters.speed);
            var1 = var4;
         }

         return var1 + var6;
      }
   }

   @TargetApi(21)
   private AudioTrack createAudioTrackV21() {
      android.media.AudioAttributes var1;
      if (this.tunneling) {
         var1 = (new Builder()).setContentType(3).setFlags(16).setUsage(1).build();
      } else {
         var1 = this.audioAttributes.getAudioAttributesV21();
      }

      AudioFormat var2 = (new android.media.AudioFormat.Builder()).setChannelMask(this.outputChannelConfig).setEncoding(this.outputEncoding).setSampleRate(this.outputSampleRate).build();
      int var3 = this.audioSessionId;
      if (var3 == 0) {
         var3 = 0;
      }

      return new AudioTrack(var1, var2, this.bufferSize, 1, var3);
   }

   private boolean drainAudioProcessorsToEndOfStream() throws AudioSink.WriteException {
      boolean var4;
      if (this.drainingAudioProcessorIndex == -1) {
         int var1;
         if (this.processingEnabled) {
            var1 = 0;
         } else {
            var1 = this.activeAudioProcessors.length;
         }

         this.drainingAudioProcessorIndex = var1;
         var4 = true;
      } else {
         var4 = false;
      }

      while(true) {
         int var2 = this.drainingAudioProcessorIndex;
         AudioProcessor[] var3 = this.activeAudioProcessors;
         if (var2 >= var3.length) {
            ByteBuffer var6 = this.outputBuffer;
            if (var6 != null) {
               this.writeBuffer(var6, -9223372036854775807L);
               if (this.outputBuffer != null) {
                  return false;
               }
            }

            this.drainingAudioProcessorIndex = -1;
            return true;
         }

         AudioProcessor var5 = var3[var2];
         if (var4) {
            var5.queueEndOfStream();
         }

         this.processBuffers(-9223372036854775807L);
         if (!var5.isEnded()) {
            return false;
         }

         ++this.drainingAudioProcessorIndex;
         var4 = true;
      }
   }

   private long durationUsToFrames(long var1) {
      return var1 * (long)this.outputSampleRate / 1000000L;
   }

   private void flushAudioProcessors() {
      int var1 = 0;

      while(true) {
         AudioProcessor[] var2 = this.activeAudioProcessors;
         if (var1 >= var2.length) {
            return;
         }

         AudioProcessor var3 = var2[var1];
         var3.flush();
         this.outputBuffers[var1] = var3.getOutput();
         ++var1;
      }
   }

   private long framesToDurationUs(long var1) {
      return var1 * 1000000L / (long)this.outputSampleRate;
   }

   private AudioProcessor[] getAvailableAudioProcessors() {
      AudioProcessor[] var1;
      if (this.shouldConvertHighResIntPcmToFloat) {
         var1 = this.toFloatPcmAvailableAudioProcessors;
      } else {
         var1 = this.toIntPcmAvailableAudioProcessors;
      }

      return var1;
   }

   private static int getChannelConfig(int var0, boolean var1) {
      int var2 = var0;
      if (Util.SDK_INT <= 28) {
         var2 = var0;
         if (!var1) {
            if (var0 == 7) {
               var2 = 8;
            } else {
               label27: {
                  if (var0 != 3 && var0 != 4) {
                     var2 = var0;
                     if (var0 != 5) {
                        break label27;
                     }
                  }

                  var2 = 6;
               }
            }
         }
      }

      var0 = var2;
      if (Util.SDK_INT <= 26) {
         var0 = var2;
         if ("fugu".equals(Util.DEVICE)) {
            var0 = var2;
            if (!var1) {
               var0 = var2;
               if (var2 == 1) {
                  var0 = 2;
               }
            }
         }
      }

      return Util.getAudioTrackChannelConfig(var0);
   }

   private int getDefaultBufferSize() {
      int var1;
      if (this.isInputPcm) {
         var1 = AudioTrack.getMinBufferSize(this.outputSampleRate, this.outputChannelConfig, this.outputEncoding);
         boolean var2;
         if (var1 != -2) {
            var2 = true;
         } else {
            var2 = false;
         }

         Assertions.checkState(var2);
         return Util.constrainValue(var1 * 4, (int)this.durationUsToFrames(250000L) * this.outputPcmFrameSize, (int)Math.max((long)var1, this.durationUsToFrames(750000L) * (long)this.outputPcmFrameSize));
      } else {
         int var3 = getMaximumEncodedRateBytesPerSecond(this.outputEncoding);
         var1 = var3;
         if (this.outputEncoding == 5) {
            var1 = var3 * 2;
         }

         return (int)((long)var1 * 250000L / 1000000L);
      }
   }

   private static int getFramesPerEncodedSample(int var0, ByteBuffer var1) {
      if (var0 != 7 && var0 != 8) {
         if (var0 == 5) {
            return Ac3Util.getAc3SyncframeAudioSampleCount();
         } else if (var0 == 6) {
            return Ac3Util.parseEAc3SyncframeAudioSampleCount(var1);
         } else if (var0 == 14) {
            var0 = Ac3Util.findTrueHdSyncframeOffset(var1);
            if (var0 == -1) {
               var0 = 0;
            } else {
               var0 = Ac3Util.parseTrueHdSyncframeAudioSampleCount(var1, var0) * 16;
            }

            return var0;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Unexpected audio encoding: ");
            var2.append(var0);
            throw new IllegalStateException(var2.toString());
         }
      } else {
         return DtsUtil.parseDtsAudioSampleCount(var1);
      }
   }

   private static int getMaximumEncodedRateBytesPerSecond(int var0) {
      if (var0 != 5) {
         if (var0 != 6) {
            if (var0 != 7) {
               if (var0 != 8) {
                  if (var0 == 14) {
                     return 3062500;
                  } else {
                     throw new IllegalArgumentException();
                  }
               } else {
                  return 2250000;
               }
            } else {
               return 192000;
            }
         } else {
            return 768000;
         }
      } else {
         return 80000;
      }
   }

   private long getSubmittedFrames() {
      long var1;
      if (this.isInputPcm) {
         var1 = this.submittedPcmBytes / (long)this.pcmFrameSize;
      } else {
         var1 = this.submittedEncodedFrames;
      }

      return var1;
   }

   private long getWrittenFrames() {
      long var1;
      if (this.isInputPcm) {
         var1 = this.writtenPcmBytes / (long)this.outputPcmFrameSize;
      } else {
         var1 = this.writtenEncodedFrames;
      }

      return var1;
   }

   private void initialize() throws AudioSink.InitializationException {
      this.releasingConditionVariable.block();
      this.audioTrack = this.initializeAudioTrack();
      int var1 = this.audioTrack.getAudioSessionId();
      if (enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
         AudioTrack var2 = this.keepSessionIdAudioTrack;
         if (var2 != null && var1 != var2.getAudioSessionId()) {
            this.releaseKeepSessionIdAudioTrack();
         }

         if (this.keepSessionIdAudioTrack == null) {
            this.keepSessionIdAudioTrack = this.initializeKeepSessionIdAudioTrack(var1);
         }
      }

      if (this.audioSessionId != var1) {
         this.audioSessionId = var1;
         AudioSink.Listener var3 = this.listener;
         if (var3 != null) {
            var3.onAudioSessionId(var1);
         }
      }

      PlaybackParameters var4;
      if (this.canApplyPlaybackParameters) {
         var4 = this.audioProcessorChain.applyPlaybackParameters(this.playbackParameters);
      } else {
         var4 = PlaybackParameters.DEFAULT;
      }

      this.playbackParameters = var4;
      this.setupAudioProcessors();
      this.audioTrackPositionTracker.setAudioTrack(this.audioTrack, this.outputEncoding, this.outputPcmFrameSize, this.bufferSize);
      this.setVolumeInternal();
      var1 = this.auxEffectInfo.effectId;
      if (var1 != 0) {
         this.audioTrack.attachAuxEffect(var1);
         this.audioTrack.setAuxEffectSendLevel(this.auxEffectInfo.sendLevel);
      }

   }

   private AudioTrack initializeAudioTrack() throws AudioSink.InitializationException {
      AudioTrack var1;
      int var3;
      if (Util.SDK_INT >= 21) {
         var1 = this.createAudioTrackV21();
      } else {
         int var2 = Util.getStreamTypeForAudioUsage(this.audioAttributes.usage);
         var3 = this.audioSessionId;
         if (var3 == 0) {
            var1 = new AudioTrack(var2, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, this.bufferSize, 1);
         } else {
            var1 = new AudioTrack(var2, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, this.bufferSize, 1, var3);
         }
      }

      var3 = var1.getState();
      if (var3 == 1) {
         return var1;
      } else {
         try {
            var1.release();
         } catch (Exception var4) {
         }

         throw new AudioSink.InitializationException(var3, this.outputSampleRate, this.outputChannelConfig, this.bufferSize);
      }
   }

   private AudioTrack initializeKeepSessionIdAudioTrack(int var1) {
      return new AudioTrack(3, 4000, 4, 2, 2, 0, var1);
   }

   private long inputFramesToDurationUs(long var1) {
      return var1 * 1000000L / (long)this.inputSampleRate;
   }

   private boolean isInitialized() {
      boolean var1;
      if (this.audioTrack != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void processBuffers(long var1) throws AudioSink.WriteException {
      int var3 = this.activeAudioProcessors.length;
      int var4 = var3;

      while(true) {
         while(var4 >= 0) {
            ByteBuffer var5;
            if (var4 > 0) {
               var5 = this.outputBuffers[var4 - 1];
            } else {
               var5 = this.inputBuffer;
               if (var5 == null) {
                  var5 = AudioProcessor.EMPTY_BUFFER;
               }
            }

            if (var4 == var3) {
               this.writeBuffer(var5, var1);
            } else {
               AudioProcessor var6 = this.activeAudioProcessors[var4];
               var6.queueInput(var5);
               ByteBuffer var7 = var6.getOutput();
               this.outputBuffers[var4] = var7;
               if (var7.hasRemaining()) {
                  ++var4;
                  continue;
               }
            }

            if (var5.hasRemaining()) {
               return;
            }

            --var4;
         }

         return;
      }
   }

   private void releaseKeepSessionIdAudioTrack() {
      final AudioTrack var1 = this.keepSessionIdAudioTrack;
      if (var1 != null) {
         this.keepSessionIdAudioTrack = null;
         (new Thread() {
            public void run() {
               var1.release();
            }
         }).start();
      }
   }

   private void setVolumeInternal() {
      if (this.isInitialized()) {
         if (Util.SDK_INT >= 21) {
            setVolumeInternalV21(this.audioTrack, this.volume);
         } else {
            setVolumeInternalV3(this.audioTrack, this.volume);
         }
      }

   }

   @TargetApi(21)
   private static void setVolumeInternalV21(AudioTrack var0, float var1) {
      var0.setVolume(var1);
   }

   private static void setVolumeInternalV3(AudioTrack var0, float var1) {
      var0.setStereoVolume(var1, var1);
   }

   private void setupAudioProcessors() {
      ArrayList var1 = new ArrayList();
      AudioProcessor[] var2 = this.getAvailableAudioProcessors();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         AudioProcessor var5 = var2[var4];
         if (var5.isActive()) {
            var1.add(var5);
         } else {
            var5.flush();
         }
      }

      var4 = var1.size();
      this.activeAudioProcessors = (AudioProcessor[])var1.toArray(new AudioProcessor[var4]);
      this.outputBuffers = new ByteBuffer[var4];
      this.flushAudioProcessors();
   }

   private void writeBuffer(ByteBuffer var1, long var2) throws AudioSink.WriteException {
      if (var1.hasRemaining()) {
         ByteBuffer var4 = this.outputBuffer;
         boolean var5 = true;
         int var6 = 0;
         boolean var7;
         int var8;
         int var9;
         if (var4 != null) {
            if (var4 == var1) {
               var7 = true;
            } else {
               var7 = false;
            }

            Assertions.checkArgument(var7);
         } else {
            this.outputBuffer = var1;
            if (Util.SDK_INT < 21) {
               var8 = var1.remaining();
               byte[] var10 = this.preV21OutputBuffer;
               if (var10 == null || var10.length < var8) {
                  this.preV21OutputBuffer = new byte[var8];
               }

               var9 = var1.position();
               var1.get(this.preV21OutputBuffer, 0, var8);
               var1.position(var9);
               this.preV21OutputBufferOffset = 0;
            }
         }

         var8 = var1.remaining();
         if (Util.SDK_INT < 21) {
            var9 = this.audioTrackPositionTracker.getAvailableBufferSize(this.writtenPcmBytes);
            if (var9 > 0) {
               var6 = Math.min(var8, var9);
               var9 = this.audioTrack.write(this.preV21OutputBuffer, this.preV21OutputBufferOffset, var6);
               var6 = var9;
               if (var9 > 0) {
                  this.preV21OutputBufferOffset += var9;
                  var1.position(var1.position() + var9);
                  var6 = var9;
               }
            }
         } else if (this.tunneling) {
            if (var2 != -9223372036854775807L) {
               var7 = var5;
            } else {
               var7 = false;
            }

            Assertions.checkState(var7);
            var6 = this.writeNonBlockingWithAvSyncV21(this.audioTrack, var1, var8, var2);
         } else {
            var6 = writeNonBlockingV21(this.audioTrack, var1, var8);
         }

         this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
         if (var6 >= 0) {
            if (this.isInputPcm) {
               this.writtenPcmBytes += (long)var6;
            }

            if (var6 == var8) {
               if (!this.isInputPcm) {
                  this.writtenEncodedFrames += (long)this.framesPerEncodedSample;
               }

               this.outputBuffer = null;
            }

         } else {
            throw new AudioSink.WriteException(var6);
         }
      }
   }

   @TargetApi(21)
   private static int writeNonBlockingV21(AudioTrack var0, ByteBuffer var1, int var2) {
      return var0.write(var1, var2, 1);
   }

   @TargetApi(21)
   private int writeNonBlockingWithAvSyncV21(AudioTrack var1, ByteBuffer var2, int var3, long var4) {
      if (this.avSyncHeader == null) {
         this.avSyncHeader = ByteBuffer.allocate(16);
         this.avSyncHeader.order(ByteOrder.BIG_ENDIAN);
         this.avSyncHeader.putInt(1431633921);
      }

      if (this.bytesUntilNextAvSync == 0) {
         this.avSyncHeader.putInt(4, var3);
         this.avSyncHeader.putLong(8, var4 * 1000L);
         this.avSyncHeader.position(0);
         this.bytesUntilNextAvSync = var3;
      }

      int var6 = this.avSyncHeader.remaining();
      if (var6 > 0) {
         int var7 = var1.write(this.avSyncHeader, var6, 1);
         if (var7 < 0) {
            this.bytesUntilNextAvSync = 0;
            return var7;
         }

         if (var7 < var6) {
            return 0;
         }
      }

      var3 = writeNonBlockingV21(var1, var2, var3);
      if (var3 < 0) {
         this.bytesUntilNextAvSync = 0;
         return var3;
      } else {
         this.bytesUntilNextAvSync -= var3;
         return var3;
      }
   }

   public void configure(int var1, int var2, int var3, int var4, int[] var5, int var6, int var7) throws AudioSink.ConfigurationException {
      this.inputSampleRate = var3;
      this.isInputPcm = Util.isEncodingLinearPcm(var1);
      boolean var8 = this.enableConvertHighResIntPcmToFloat;
      boolean var9 = true;
      byte var10 = 0;
      if (var8 && this.supportsOutput(var2, 1073741824) && Util.isEncodingHighResolutionIntegerPcm(var1)) {
         var8 = true;
      } else {
         var8 = false;
      }

      this.shouldConvertHighResIntPcmToFloat = var8;
      if (this.isInputPcm) {
         this.pcmFrameSize = Util.getPcmFrameSize(var1, var2);
      }

      if (this.isInputPcm && var1 != 4) {
         var8 = true;
      } else {
         var8 = false;
      }

      if (!var8 || this.shouldConvertHighResIntPcmToFloat) {
         var9 = false;
      }

      this.canApplyPlaybackParameters = var9;
      int[] var11 = var5;
      int var12;
      if (Util.SDK_INT < 21) {
         var11 = var5;
         if (var2 == 8) {
            var11 = var5;
            if (var5 == null) {
               var5 = new int[6];
               var12 = 0;

               while(true) {
                  var11 = var5;
                  if (var12 >= var5.length) {
                     break;
                  }

                  var5[var12] = var12++;
               }
            }
         }
      }

      boolean var14;
      int var15;
      int var21;
      if (var8) {
         this.trimmingAudioProcessor.setTrimFrameCount(var6, var7);
         this.channelMappingAudioProcessor.setChannelMap(var11);
         AudioProcessor[] var17 = this.getAvailableAudioProcessors();
         int var13 = var17.length;
         var6 = var3;
         var3 = var1;
         boolean var20 = false;
         var1 = var6;
         boolean var19 = var20;
         var7 = var10;

         while(true) {
            var14 = var19;
            var15 = var2;
            var21 = var3;
            var12 = var1;
            if (var7 >= var13) {
               break;
            }

            AudioProcessor var22 = var17[var7];

            try {
               var9 = var22.configure(var1, var2, var3);
            } catch (AudioProcessor.UnhandledFormatException var16) {
               throw new AudioSink.ConfigurationException(var16);
            }

            var19 |= var9;
            if (var22.isActive()) {
               var2 = var22.getOutputChannelCount();
               var1 = var22.getOutputSampleRateHz();
               var3 = var22.getOutputEncoding();
            }

            ++var7;
         }
      } else {
         var14 = false;
         var12 = var3;
         var21 = var1;
         var15 = var2;
      }

      var1 = getChannelConfig(var15, this.isInputPcm);
      if (var1 != 0) {
         if (var14 || !this.isInitialized() || this.outputEncoding != var21 || this.outputSampleRate != var12 || this.outputChannelConfig != var1) {
            this.flush();
            this.processingEnabled = var8;
            this.outputSampleRate = var12;
            this.outputChannelConfig = var1;
            this.outputEncoding = var21;
            if (this.isInputPcm) {
               var1 = Util.getPcmFrameSize(this.outputEncoding, var15);
            } else {
               var1 = -1;
            }

            this.outputPcmFrameSize = var1;
            if (var4 == 0) {
               var4 = this.getDefaultBufferSize();
            }

            this.bufferSize = var4;
         }
      } else {
         StringBuilder var18 = new StringBuilder();
         var18.append("Unsupported channel count: ");
         var18.append(var15);
         throw new AudioSink.ConfigurationException(var18.toString());
      }
   }

   public void disableTunneling() {
      if (this.tunneling) {
         this.tunneling = false;
         this.audioSessionId = 0;
         this.flush();
      }

   }

   public void enableTunnelingV21(int var1) {
      boolean var2;
      if (Util.SDK_INT >= 21) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      if (!this.tunneling || this.audioSessionId != var1) {
         this.tunneling = true;
         this.audioSessionId = var1;
         this.flush();
      }

   }

   public void flush() {
      if (this.isInitialized()) {
         this.submittedPcmBytes = 0L;
         this.submittedEncodedFrames = 0L;
         this.writtenPcmBytes = 0L;
         this.writtenEncodedFrames = 0L;
         this.framesPerEncodedSample = 0;
         PlaybackParameters var1 = this.afterDrainPlaybackParameters;
         if (var1 != null) {
            this.playbackParameters = var1;
            this.afterDrainPlaybackParameters = null;
         } else if (!this.playbackParametersCheckpoints.isEmpty()) {
            this.playbackParameters = ((DefaultAudioSink.PlaybackParametersCheckpoint)this.playbackParametersCheckpoints.getLast()).playbackParameters;
         }

         this.playbackParametersCheckpoints.clear();
         this.playbackParametersOffsetUs = 0L;
         this.playbackParametersPositionUs = 0L;
         this.trimmingAudioProcessor.resetTrimmedFrameCount();
         this.inputBuffer = null;
         this.outputBuffer = null;
         this.flushAudioProcessors();
         this.handledEndOfStream = false;
         this.drainingAudioProcessorIndex = -1;
         this.avSyncHeader = null;
         this.bytesUntilNextAvSync = 0;
         this.startMediaTimeState = 0;
         if (this.audioTrackPositionTracker.isPlaying()) {
            this.audioTrack.pause();
         }

         final AudioTrack var2 = this.audioTrack;
         this.audioTrack = null;
         this.audioTrackPositionTracker.reset();
         this.releasingConditionVariable.close();
         (new Thread() {
            public void run() {
               try {
                  var2.flush();
                  var2.release();
               } finally {
                  DefaultAudioSink.this.releasingConditionVariable.open();
               }

            }
         }).start();
      }

   }

   public long getCurrentPositionUs(boolean var1) {
      if (this.isInitialized() && this.startMediaTimeState != 0) {
         long var2 = Math.min(this.audioTrackPositionTracker.getCurrentPositionUs(var1), this.framesToDurationUs(this.getWrittenFrames()));
         return this.startMediaTimeUs + this.applySkipping(this.applySpeedup(var2));
      } else {
         return Long.MIN_VALUE;
      }
   }

   public PlaybackParameters getPlaybackParameters() {
      return this.playbackParameters;
   }

   public boolean handleBuffer(ByteBuffer var1, long var2) throws AudioSink.InitializationException, AudioSink.WriteException {
      ByteBuffer var4 = this.inputBuffer;
      boolean var5;
      if (var4 != null && var1 != var4) {
         var5 = false;
      } else {
         var5 = true;
      }

      Assertions.checkArgument(var5);
      if (!this.isInitialized()) {
         this.initialize();
         if (this.playing) {
            this.play();
         }
      }

      if (!this.audioTrackPositionTracker.mayHandleBuffer(this.getWrittenFrames())) {
         return false;
      } else {
         if (this.inputBuffer == null) {
            if (!var1.hasRemaining()) {
               return true;
            }

            if (!this.isInputPcm && this.framesPerEncodedSample == 0) {
               this.framesPerEncodedSample = getFramesPerEncodedSample(this.outputEncoding, var1);
               if (this.framesPerEncodedSample == 0) {
                  return true;
               }
            }

            if (this.afterDrainPlaybackParameters != null) {
               if (!this.drainAudioProcessorsToEndOfStream()) {
                  return false;
               }

               PlaybackParameters var8 = this.afterDrainPlaybackParameters;
               this.afterDrainPlaybackParameters = null;
               var8 = this.audioProcessorChain.applyPlaybackParameters(var8);
               this.playbackParametersCheckpoints.add(new DefaultAudioSink.PlaybackParametersCheckpoint(var8, Math.max(0L, var2), this.framesToDurationUs(this.getWrittenFrames())));
               this.setupAudioProcessors();
            }

            if (this.startMediaTimeState == 0) {
               this.startMediaTimeUs = Math.max(0L, var2);
               this.startMediaTimeState = 1;
            } else {
               long var6 = this.startMediaTimeUs + this.inputFramesToDurationUs(this.getSubmittedFrames() - this.trimmingAudioProcessor.getTrimmedFrameCount());
               if (this.startMediaTimeState == 1 && Math.abs(var6 - var2) > 200000L) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Discontinuity detected [expected ");
                  var9.append(var6);
                  var9.append(", got ");
                  var9.append(var2);
                  var9.append("]");
                  Log.e("AudioTrack", var9.toString());
                  this.startMediaTimeState = 2;
               }

               if (this.startMediaTimeState == 2) {
                  var6 = var2 - var6;
                  this.startMediaTimeUs += var6;
                  this.startMediaTimeState = 1;
                  AudioSink.Listener var10 = this.listener;
                  if (var10 != null && var6 != 0L) {
                     var10.onPositionDiscontinuity();
                  }
               }
            }

            if (this.isInputPcm) {
               this.submittedPcmBytes += (long)var1.remaining();
            } else {
               this.submittedEncodedFrames += (long)this.framesPerEncodedSample;
            }

            this.inputBuffer = var1;
         }

         if (this.processingEnabled) {
            this.processBuffers(var2);
         } else {
            this.writeBuffer(this.inputBuffer, var2);
         }

         if (!this.inputBuffer.hasRemaining()) {
            this.inputBuffer = null;
            return true;
         } else if (this.audioTrackPositionTracker.isStalled(this.getWrittenFrames())) {
            Log.w("AudioTrack", "Resetting stalled audio track");
            this.flush();
            return true;
         } else {
            return false;
         }
      }
   }

   public void handleDiscontinuity() {
      if (this.startMediaTimeState == 1) {
         this.startMediaTimeState = 2;
      }

   }

   public boolean hasPendingData() {
      boolean var1;
      if (this.isInitialized() && this.audioTrackPositionTracker.hasPendingData(this.getWrittenFrames())) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isEnded() {
      boolean var1;
      if (!this.isInitialized() || this.handledEndOfStream && !this.hasPendingData()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void pause() {
      this.playing = false;
      if (this.isInitialized() && this.audioTrackPositionTracker.pause()) {
         this.audioTrack.pause();
      }

   }

   public void play() {
      this.playing = true;
      if (this.isInitialized()) {
         this.audioTrackPositionTracker.start();
         this.audioTrack.play();
      }

   }

   public void playToEndOfStream() throws AudioSink.WriteException {
      if (!this.handledEndOfStream && this.isInitialized() && this.drainAudioProcessorsToEndOfStream()) {
         this.audioTrackPositionTracker.handleEndOfStream(this.getWrittenFrames());
         this.audioTrack.stop();
         this.bytesUntilNextAvSync = 0;
         this.handledEndOfStream = true;
      }

   }

   public void reset() {
      this.flush();
      this.releaseKeepSessionIdAudioTrack();
      AudioProcessor[] var1 = this.toIntPcmAvailableAudioProcessors;
      int var2 = var1.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset();
      }

      var1 = this.toFloatPcmAvailableAudioProcessors;
      var2 = var1.length;

      for(var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset();
      }

      this.audioSessionId = 0;
      this.playing = false;
   }

   public void setAudioAttributes(AudioAttributes var1) {
      if (!this.audioAttributes.equals(var1)) {
         this.audioAttributes = var1;
         if (!this.tunneling) {
            this.flush();
            this.audioSessionId = 0;
         }
      }
   }

   public void setAuxEffectInfo(AuxEffectInfo var1) {
      if (!this.auxEffectInfo.equals(var1)) {
         int var2 = var1.effectId;
         float var3 = var1.sendLevel;
         AudioTrack var4 = this.audioTrack;
         if (var4 != null) {
            if (this.auxEffectInfo.effectId != var2) {
               var4.attachAuxEffect(var2);
            }

            if (var2 != 0) {
               this.audioTrack.setAuxEffectSendLevel(var3);
            }
         }

         this.auxEffectInfo = var1;
      }
   }

   public void setListener(AudioSink.Listener var1) {
      this.listener = var1;
   }

   public PlaybackParameters setPlaybackParameters(PlaybackParameters var1) {
      if (this.isInitialized() && !this.canApplyPlaybackParameters) {
         this.playbackParameters = PlaybackParameters.DEFAULT;
         return this.playbackParameters;
      } else {
         PlaybackParameters var2 = this.afterDrainPlaybackParameters;
         if (var2 == null) {
            if (!this.playbackParametersCheckpoints.isEmpty()) {
               var2 = ((DefaultAudioSink.PlaybackParametersCheckpoint)this.playbackParametersCheckpoints.getLast()).playbackParameters;
            } else {
               var2 = this.playbackParameters;
            }
         }

         if (!var1.equals(var2)) {
            if (this.isInitialized()) {
               this.afterDrainPlaybackParameters = var1;
            } else {
               this.playbackParameters = this.audioProcessorChain.applyPlaybackParameters(var1);
            }
         }

         return this.playbackParameters;
      }
   }

   public void setVolume(float var1) {
      if (this.volume != var1) {
         this.volume = var1;
         this.setVolumeInternal();
      }

   }

   public boolean supportsOutput(int var1, int var2) {
      boolean var3 = Util.isEncodingLinearPcm(var2);
      boolean var4 = true;
      boolean var5 = true;
      if (var3) {
         var3 = var5;
         if (var2 == 4) {
            if (Util.SDK_INT >= 21) {
               var3 = var5;
            } else {
               var3 = false;
            }
         }

         return var3;
      } else {
         AudioCapabilities var6 = this.audioCapabilities;
         if (var6 != null && var6.supportsEncoding(var2)) {
            var3 = var4;
            if (var1 == -1) {
               return var3;
            }

            if (var1 <= this.audioCapabilities.getMaxChannelCount()) {
               var3 = var4;
               return var3;
            }
         }

         var3 = false;
         return var3;
      }
   }

   public interface AudioProcessorChain {
      PlaybackParameters applyPlaybackParameters(PlaybackParameters var1);

      AudioProcessor[] getAudioProcessors();

      long getMediaDuration(long var1);

      long getSkippedOutputFrameCount();
   }

   public static class DefaultAudioProcessorChain implements DefaultAudioSink.AudioProcessorChain {
      private final AudioProcessor[] audioProcessors;
      private final SilenceSkippingAudioProcessor silenceSkippingAudioProcessor;
      private final SonicAudioProcessor sonicAudioProcessor;

      public DefaultAudioProcessorChain(AudioProcessor... var1) {
         this.audioProcessors = (AudioProcessor[])Arrays.copyOf(var1, var1.length + 2);
         this.silenceSkippingAudioProcessor = new SilenceSkippingAudioProcessor();
         this.sonicAudioProcessor = new SonicAudioProcessor();
         AudioProcessor[] var2 = this.audioProcessors;
         var2[var1.length] = this.silenceSkippingAudioProcessor;
         var2[var1.length + 1] = this.sonicAudioProcessor;
      }

      public PlaybackParameters applyPlaybackParameters(PlaybackParameters var1) {
         this.silenceSkippingAudioProcessor.setEnabled(var1.skipSilence);
         return new PlaybackParameters(this.sonicAudioProcessor.setSpeed(var1.speed), this.sonicAudioProcessor.setPitch(var1.pitch), var1.skipSilence);
      }

      public AudioProcessor[] getAudioProcessors() {
         return this.audioProcessors;
      }

      public long getMediaDuration(long var1) {
         return this.sonicAudioProcessor.scaleDurationForSpeedup(var1);
      }

      public long getSkippedOutputFrameCount() {
         return this.silenceSkippingAudioProcessor.getSkippedFrames();
      }
   }

   public static final class InvalidAudioTrackTimestampException extends RuntimeException {
      private InvalidAudioTrackTimestampException(String var1) {
         super(var1);
      }

      // $FF: synthetic method
      InvalidAudioTrackTimestampException(String var1, Object var2) {
         this(var1);
      }
   }

   private static final class PlaybackParametersCheckpoint {
      private final long mediaTimeUs;
      private final PlaybackParameters playbackParameters;
      private final long positionUs;

      private PlaybackParametersCheckpoint(PlaybackParameters var1, long var2, long var4) {
         this.playbackParameters = var1;
         this.mediaTimeUs = var2;
         this.positionUs = var4;
      }

      // $FF: synthetic method
      PlaybackParametersCheckpoint(PlaybackParameters var1, long var2, long var4, Object var6) {
         this(var1, var2, var4);
      }
   }

   private final class PositionTrackerListener implements AudioTrackPositionTracker.Listener {
      private PositionTrackerListener() {
      }

      // $FF: synthetic method
      PositionTrackerListener(Object var2) {
         this();
      }

      public void onInvalidLatency(long var1) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Ignoring impossibly large audio latency: ");
         var3.append(var1);
         Log.w("AudioTrack", var3.toString());
      }

      public void onPositionFramesMismatch(long var1, long var3, long var5, long var7) {
         StringBuilder var9 = new StringBuilder();
         var9.append("Spurious audio timestamp (frame position mismatch): ");
         var9.append(var1);
         var9.append(", ");
         var9.append(var3);
         var9.append(", ");
         var9.append(var5);
         var9.append(", ");
         var9.append(var7);
         var9.append(", ");
         var9.append(DefaultAudioSink.this.getSubmittedFrames());
         var9.append(", ");
         var9.append(DefaultAudioSink.this.getWrittenFrames());
         String var10 = var9.toString();
         if (!DefaultAudioSink.failOnSpuriousAudioTimestamp) {
            Log.w("AudioTrack", var10);
         } else {
            throw new DefaultAudioSink.InvalidAudioTrackTimestampException(var10);
         }
      }

      public void onSystemTimeUsMismatch(long var1, long var3, long var5, long var7) {
         StringBuilder var9 = new StringBuilder();
         var9.append("Spurious audio timestamp (system clock mismatch): ");
         var9.append(var1);
         var9.append(", ");
         var9.append(var3);
         var9.append(", ");
         var9.append(var5);
         var9.append(", ");
         var9.append(var7);
         var9.append(", ");
         var9.append(DefaultAudioSink.this.getSubmittedFrames());
         var9.append(", ");
         var9.append(DefaultAudioSink.this.getWrittenFrames());
         String var10 = var9.toString();
         if (!DefaultAudioSink.failOnSpuriousAudioTimestamp) {
            Log.w("AudioTrack", var10);
         } else {
            throw new DefaultAudioSink.InvalidAudioTrackTimestampException(var10);
         }
      }

      public void onUnderrun(int var1, long var2) {
         if (DefaultAudioSink.this.listener != null) {
            long var4 = SystemClock.elapsedRealtime();
            long var6 = DefaultAudioSink.this.lastFeedElapsedRealtimeMs;
            DefaultAudioSink.this.listener.onUnderrun(var1, var2, var4 - var6);
         }

      }
   }
}
