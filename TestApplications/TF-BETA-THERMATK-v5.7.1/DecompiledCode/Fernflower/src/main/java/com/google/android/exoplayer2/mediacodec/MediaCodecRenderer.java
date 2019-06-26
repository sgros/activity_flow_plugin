package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.CodecException;
import android.media.MediaCodec.CryptoException;
import android.media.MediaCodec.CryptoInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.TimedValueQueue;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@TargetApi(16)
public abstract class MediaCodecRenderer extends BaseRenderer {
   private static final byte[] ADAPTATION_WORKAROUND_BUFFER = Util.getBytesFromHexString("0000016742C00BDA259000000168CE0F13200000016588840DCE7118A0002FBF1C31C3275D78");
   private final float assumedMinimumCodecOperatingRate;
   private ArrayDeque availableCodecInfos;
   private final DecoderInputBuffer buffer;
   private MediaCodec codec;
   private int codecAdaptationWorkaroundMode;
   private int codecDrainAction;
   private int codecDrainState;
   private DrmSession codecDrmSession;
   private Format codecFormat;
   private long codecHotswapDeadlineMs;
   private MediaCodecInfo codecInfo;
   private boolean codecNeedsAdaptationWorkaroundBuffer;
   private boolean codecNeedsDiscardToSpsWorkaround;
   private boolean codecNeedsEosFlushWorkaround;
   private boolean codecNeedsEosOutputExceptionWorkaround;
   private boolean codecNeedsEosPropagation;
   private boolean codecNeedsFlushWorkaround;
   private boolean codecNeedsMonoChannelCountWorkaround;
   private boolean codecNeedsReconfigureWorkaround;
   private float codecOperatingRate;
   private boolean codecReceivedBuffers;
   private boolean codecReceivedEos;
   private int codecReconfigurationState;
   private boolean codecReconfigured;
   private final ArrayList decodeOnlyPresentationTimestamps;
   protected DecoderCounters decoderCounters;
   private final DrmSessionManager drmSessionManager;
   private final DecoderInputBuffer flagsOnlyBuffer;
   private final FormatHolder formatHolder;
   private final TimedValueQueue formatQueue;
   private ByteBuffer[] inputBuffers;
   private Format inputFormat;
   private int inputIndex;
   private boolean inputStreamEnded;
   private final MediaCodecSelector mediaCodecSelector;
   private MediaCrypto mediaCrypto;
   private boolean mediaCryptoRequiresSecureDecoder;
   private ByteBuffer outputBuffer;
   private final BufferInfo outputBufferInfo;
   private ByteBuffer[] outputBuffers;
   private Format outputFormat;
   private int outputIndex;
   private boolean outputStreamEnded;
   private final boolean playClearSamplesWithoutKeys;
   private MediaCodecRenderer.DecoderInitializationException preferredDecoderInitializationException;
   private long renderTimeLimitMs;
   private float rendererOperatingRate;
   private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
   private boolean shouldSkipOutputBuffer;
   private DrmSession sourceDrmSession;
   private boolean waitingForFirstSampleInFormat;
   private boolean waitingForFirstSyncSample;
   private boolean waitingForKeys;

   public MediaCodecRenderer(int var1, MediaCodecSelector var2, DrmSessionManager var3, boolean var4, float var5) {
      super(var1);
      boolean var6;
      if (Util.SDK_INT >= 16) {
         var6 = true;
      } else {
         var6 = false;
      }

      Assertions.checkState(var6);
      Assertions.checkNotNull(var2);
      this.mediaCodecSelector = (MediaCodecSelector)var2;
      this.drmSessionManager = var3;
      this.playClearSamplesWithoutKeys = var4;
      this.assumedMinimumCodecOperatingRate = var5;
      this.buffer = new DecoderInputBuffer(0);
      this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
      this.formatHolder = new FormatHolder();
      this.formatQueue = new TimedValueQueue();
      this.decodeOnlyPresentationTimestamps = new ArrayList();
      this.outputBufferInfo = new BufferInfo();
      this.codecReconfigurationState = 0;
      this.codecDrainState = 0;
      this.codecDrainAction = 0;
      this.codecOperatingRate = -1.0F;
      this.rendererOperatingRate = 1.0F;
      this.renderTimeLimitMs = -9223372036854775807L;
   }

   private int codecAdaptationWorkaroundMode(String var1) {
      if (Util.SDK_INT <= 25 && "OMX.Exynos.avc.dec.secure".equals(var1) && (Util.MODEL.startsWith("SM-T585") || Util.MODEL.startsWith("SM-A510") || Util.MODEL.startsWith("SM-A520") || Util.MODEL.startsWith("SM-J700"))) {
         return 2;
      } else {
         return Util.SDK_INT < 24 && ("OMX.Nvidia.h264.decode".equals(var1) || "OMX.Nvidia.h264.decode.secure".equals(var1)) && ("flounder".equals(Util.DEVICE) || "flounder_lte".equals(Util.DEVICE) || "grouper".equals(Util.DEVICE) || "tilapia".equals(Util.DEVICE)) ? 1 : 0;
      }
   }

   private static boolean codecNeedsDiscardToSpsWorkaround(String var0, Format var1) {
      boolean var2;
      if (Util.SDK_INT < 21 && var1.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(var0)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean codecNeedsEosFlushWorkaround(String var0) {
      boolean var1;
      if ((Util.SDK_INT > 23 || !"OMX.google.vorbis.decoder".equals(var0)) && (Util.SDK_INT > 19 || !"hb2000".equals(Util.DEVICE) && !"stvm8".equals(Util.DEVICE) || !"OMX.amlogic.avc.decoder.awesome".equals(var0) && !"OMX.amlogic.avc.decoder.awesome.secure".equals(var0))) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean codecNeedsEosOutputExceptionWorkaround(String var0) {
      boolean var1;
      if (Util.SDK_INT == 21 && "OMX.google.aac.decoder".equals(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean codecNeedsEosPropagationWorkaround(MediaCodecInfo var0) {
      String var1 = var0.name;
      boolean var2;
      if ((Util.SDK_INT > 17 || !"OMX.rk.video_decoder.avc".equals(var1) && !"OMX.allwinner.video.decoder.avc".equals(var1)) && (!"Amazon".equals(Util.MANUFACTURER) || !"AFTS".equals(Util.MODEL) || !var0.secure)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private static boolean codecNeedsFlushWorkaround(String var0) {
      int var1 = Util.SDK_INT;
      boolean var2;
      if (var1 < 18 || var1 == 18 && ("OMX.SEC.avc.dec".equals(var0) || "OMX.SEC.avc.dec.secure".equals(var0)) || Util.SDK_INT == 19 && Util.MODEL.startsWith("SM-G800") && ("OMX.Exynos.avc.dec".equals(var0) || "OMX.Exynos.avc.dec.secure".equals(var0))) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean codecNeedsMonoChannelCountWorkaround(String var0, Format var1) {
      int var2 = Util.SDK_INT;
      boolean var3 = true;
      if (var2 > 18 || var1.channelCount != 1 || !"OMX.MTK.AUDIO.DECODER.MP3".equals(var0)) {
         var3 = false;
      }

      return var3;
   }

   private static boolean codecNeedsReconfigureWorkaround(String var0) {
      boolean var1;
      if (Util.MODEL.startsWith("SM-T230") && "OMX.MARVELL.VIDEO.HW.CODA7542DECODER".equals(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean deviceNeedsDrmKeysToConfigureCodecWorkaround() {
      boolean var1;
      if (!"Amazon".equals(Util.MANUFACTURER) || !"AFTM".equals(Util.MODEL) && !"AFTB".equals(Util.MODEL)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void drainAndFlushCodec() {
      if (this.codecReceivedBuffers) {
         this.codecDrainState = 1;
         this.codecDrainAction = 1;
      }

   }

   private void drainAndReinitializeCodec() throws ExoPlaybackException {
      if (this.codecReceivedBuffers) {
         this.codecDrainState = 1;
         this.codecDrainAction = 2;
      } else {
         this.releaseCodec();
         this.maybeInitCodec();
      }

   }

   private boolean drainOutputBuffer(long var1, long var3) throws ExoPlaybackException {
      int var5;
      BufferInfo var6;
      ByteBuffer var7;
      if (!this.hasOutputBuffer()) {
         if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
            try {
               var5 = this.codec.dequeueOutputBuffer(this.outputBufferInfo, this.getDequeueOutputBufferTimeoutUs());
            } catch (IllegalStateException var11) {
               this.processEndOfStream();
               if (this.outputStreamEnded) {
                  this.releaseCodec();
               }

               return false;
            }
         } else {
            var5 = this.codec.dequeueOutputBuffer(this.outputBufferInfo, this.getDequeueOutputBufferTimeoutUs());
         }

         if (var5 < 0) {
            if (var5 == -2) {
               this.processOutputFormat();
               return true;
            }

            if (var5 == -3) {
               this.processOutputBuffersChanged();
               return true;
            }

            if (this.codecNeedsEosPropagation && (this.inputStreamEnded || this.codecDrainState == 2)) {
               this.processEndOfStream();
            }

            return false;
         }

         if (this.shouldSkipAdaptationWorkaroundOutputBuffer) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
            this.codec.releaseOutputBuffer(var5, false);
            return true;
         }

         var6 = this.outputBufferInfo;
         if (var6.size == 0 && (var6.flags & 4) != 0) {
            this.processEndOfStream();
            return false;
         }

         this.outputIndex = var5;
         this.outputBuffer = this.getOutputBuffer(var5);
         ByteBuffer var13 = this.outputBuffer;
         if (var13 != null) {
            var13.position(this.outputBufferInfo.offset);
            var7 = this.outputBuffer;
            var6 = this.outputBufferInfo;
            var7.limit(var6.offset + var6.size);
         }

         this.shouldSkipOutputBuffer = this.shouldSkipOutputBuffer(this.outputBufferInfo.presentationTimeUs);
         this.updateOutputFormatForTime(this.outputBufferInfo.presentationTimeUs);
      }

      boolean var8;
      if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
         try {
            var8 = this.processOutputBuffer(var1, var3, this.codec, this.outputBuffer, this.outputIndex, this.outputBufferInfo.flags, this.outputBufferInfo.presentationTimeUs, this.shouldSkipOutputBuffer, this.outputFormat);
         } catch (IllegalStateException var10) {
            this.processEndOfStream();
            if (this.outputStreamEnded) {
               this.releaseCodec();
            }

            return false;
         }
      } else {
         MediaCodec var9 = this.codec;
         var7 = this.outputBuffer;
         var5 = this.outputIndex;
         var6 = this.outputBufferInfo;
         var8 = this.processOutputBuffer(var1, var3, var9, var7, var5, var6.flags, var6.presentationTimeUs, this.shouldSkipOutputBuffer, this.outputFormat);
      }

      if (var8) {
         this.onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
         boolean var12;
         if ((this.outputBufferInfo.flags & 4) != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         this.resetOutputBuffer();
         if (!var12) {
            return true;
         }

         this.processEndOfStream();
      }

      return false;
   }

   private boolean feedInputBuffer() throws ExoPlaybackException {
      MediaCodec var1 = this.codec;
      if (var1 != null && this.codecDrainState != 2 && !this.inputStreamEnded) {
         int var2;
         if (this.inputIndex < 0) {
            this.inputIndex = var1.dequeueInputBuffer(0L);
            var2 = this.inputIndex;
            if (var2 < 0) {
               return false;
            }

            this.buffer.data = this.getInputBuffer(var2);
            this.buffer.clear();
         }

         if (this.codecDrainState == 1) {
            if (!this.codecNeedsEosPropagation) {
               this.codecReceivedEos = true;
               this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
               this.resetInputBuffer();
            }

            this.codecDrainState = 2;
            return false;
         } else if (this.codecNeedsAdaptationWorkaroundBuffer) {
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.buffer.data.put(ADAPTATION_WORKAROUND_BUFFER);
            this.codec.queueInputBuffer(this.inputIndex, 0, ADAPTATION_WORKAROUND_BUFFER.length, 0L, 0);
            this.resetInputBuffer();
            this.codecReceivedBuffers = true;
            return true;
         } else {
            int var3;
            if (this.waitingForKeys) {
               var3 = -4;
               var2 = 0;
            } else {
               if (this.codecReconfigurationState == 1) {
                  for(var2 = 0; var2 < this.codecFormat.initializationData.size(); ++var2) {
                     byte[] var16 = (byte[])this.codecFormat.initializationData.get(var2);
                     this.buffer.data.put(var16);
                  }

                  this.codecReconfigurationState = 2;
               }

               var2 = this.buffer.data.position();
               var3 = this.readSource(this.formatHolder, this.buffer, false);
            }

            if (var3 == -3) {
               return false;
            } else if (var3 == -5) {
               if (this.codecReconfigurationState == 2) {
                  this.buffer.clear();
                  this.codecReconfigurationState = 1;
               }

               this.onInputFormatChanged(this.formatHolder.format);
               return true;
            } else {
               CryptoException var10000;
               boolean var10001;
               CryptoException var18;
               if (this.buffer.isEndOfStream()) {
                  if (this.codecReconfigurationState == 2) {
                     this.buffer.clear();
                     this.codecReconfigurationState = 1;
                  }

                  this.inputStreamEnded = true;
                  if (!this.codecReceivedBuffers) {
                     this.processEndOfStream();
                     return false;
                  } else {
                     label113: {
                        label112: {
                           try {
                              if (this.codecNeedsEosPropagation) {
                                 break label112;
                              }
                           } catch (CryptoException var9) {
                              var10000 = var9;
                              var10001 = false;
                              break label113;
                           }

                           try {
                              this.codecReceivedEos = true;
                              this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                              this.resetInputBuffer();
                           } catch (CryptoException var8) {
                              var10000 = var8;
                              var10001 = false;
                              break label113;
                           }
                        }

                        try {
                           return false;
                        } catch (CryptoException var7) {
                           var10000 = var7;
                           var10001 = false;
                        }
                     }

                     var18 = var10000;
                     throw ExoPlaybackException.createForRenderer(var18, this.getIndex());
                  }
               } else if (this.waitingForFirstSyncSample && !this.buffer.isKeyFrame()) {
                  this.buffer.clear();
                  if (this.codecReconfigurationState == 2) {
                     this.codecReconfigurationState = 1;
                  }

                  return true;
               } else {
                  this.waitingForFirstSyncSample = false;
                  boolean var4 = this.buffer.isEncrypted();
                  this.waitingForKeys = this.shouldWaitForKeys(var4);
                  if (this.waitingForKeys) {
                     return false;
                  } else {
                     if (this.codecNeedsDiscardToSpsWorkaround && !var4) {
                        NalUnitUtil.discardToSps(this.buffer.data);
                        if (this.buffer.data.position() == 0) {
                           return true;
                        }

                        this.codecNeedsDiscardToSpsWorkaround = false;
                     }

                     label173: {
                        long var5;
                        try {
                           var5 = this.buffer.timeUs;
                           if (this.buffer.isDecodeOnly()) {
                              this.decodeOnlyPresentationTimestamps.add(var5);
                           }
                        } catch (CryptoException var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label173;
                        }

                        try {
                           if (this.waitingForFirstSampleInFormat) {
                              this.formatQueue.add(var5, this.inputFormat);
                              this.waitingForFirstSampleInFormat = false;
                           }
                        } catch (CryptoException var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label173;
                        }

                        try {
                           this.buffer.flip();
                           this.onQueueInputBuffer(this.buffer);
                        } catch (CryptoException var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label173;
                        }

                        if (var4) {
                           try {
                              CryptoInfo var17 = getFrameworkCryptoInfo(this.buffer, var2);
                              this.codec.queueSecureInputBuffer(this.inputIndex, 0, var17, var5, 0);
                           } catch (CryptoException var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label173;
                           }
                        } else {
                           try {
                              this.codec.queueInputBuffer(this.inputIndex, 0, this.buffer.data.limit(), var5, 0);
                           } catch (CryptoException var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label173;
                           }
                        }

                        try {
                           this.resetInputBuffer();
                           this.codecReceivedBuffers = true;
                           this.codecReconfigurationState = 0;
                           DecoderCounters var19 = this.decoderCounters;
                           ++var19.inputBufferCount;
                           return true;
                        } catch (CryptoException var10) {
                           var10000 = var10;
                           var10001 = false;
                        }
                     }

                     var18 = var10000;
                     throw ExoPlaybackException.createForRenderer(var18, this.getIndex());
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   private List getAvailableCodecInfos(boolean var1) throws MediaCodecUtil.DecoderQueryException {
      List var2 = this.getDecoderInfos(this.mediaCodecSelector, this.inputFormat, var1);
      List var3 = var2;
      if (var2.isEmpty()) {
         var3 = var2;
         if (var1) {
            var2 = this.getDecoderInfos(this.mediaCodecSelector, this.inputFormat, false);
            var3 = var2;
            if (!var2.isEmpty()) {
               StringBuilder var4 = new StringBuilder();
               var4.append("Drm session requires secure decoder for ");
               var4.append(this.inputFormat.sampleMimeType);
               var4.append(", but no secure decoder available. Trying to proceed with ");
               var4.append(var2);
               var4.append(".");
               Log.w("MediaCodecRenderer", var4.toString());
               var3 = var2;
            }
         }
      }

      return var3;
   }

   private void getCodecBuffers(MediaCodec var1) {
      if (Util.SDK_INT < 21) {
         this.inputBuffers = var1.getInputBuffers();
         this.outputBuffers = var1.getOutputBuffers();
      }

   }

   private static CryptoInfo getFrameworkCryptoInfo(DecoderInputBuffer var0, int var1) {
      CryptoInfo var3 = var0.cryptoInfo.getFrameworkCryptoInfoV16();
      if (var1 == 0) {
         return var3;
      } else {
         if (var3.numBytesOfClearData == null) {
            var3.numBytesOfClearData = new int[1];
         }

         int[] var2 = var3.numBytesOfClearData;
         var2[0] += var1;
         return var3;
      }
   }

   private ByteBuffer getInputBuffer(int var1) {
      return Util.SDK_INT >= 21 ? this.codec.getInputBuffer(var1) : this.inputBuffers[var1];
   }

   private ByteBuffer getOutputBuffer(int var1) {
      return Util.SDK_INT >= 21 ? this.codec.getOutputBuffer(var1) : this.outputBuffers[var1];
   }

   private boolean hasOutputBuffer() {
      boolean var1;
      if (this.outputIndex >= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void initCodec(MediaCodecInfo var1, MediaCrypto var2) throws Exception {
      String var3 = var1.name;
      float var4;
      if (Util.SDK_INT < 23) {
         var4 = -1.0F;
      } else {
         var4 = this.getCodecOperatingRateV23(this.rendererOperatingRate, this.inputFormat, this.getStreamFormats());
      }

      float var5 = var4;
      if (var4 <= this.assumedMinimumCodecOperatingRate) {
         var5 = -1.0F;
      }

      MediaCodec var6 = null;
      MediaCodec var7 = var6;

      long var8;
      long var11;
      label166: {
         Exception var10000;
         label167: {
            boolean var10001;
            try {
               var8 = SystemClock.elapsedRealtime();
            } catch (Exception var31) {
               var10000 = var31;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            StringBuilder var10;
            try {
               var10 = new StringBuilder;
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               var10.<init>();
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               var10.append("createCodec:");
            } catch (Exception var28) {
               var10000 = var28;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               var10.append(var3);
            } catch (Exception var27) {
               var10000 = var27;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               TraceUtil.beginSection(var10.toString());
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               var6 = MediaCodec.createByCodecName(var3);
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               TraceUtil.endSection();
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               TraceUtil.beginSection("configureCodec");
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               this.configureCodec(var1, var6, this.inputFormat, var2, var5);
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               TraceUtil.endSection();
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               TraceUtil.beginSection("startCodec");
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               var6.start();
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               TraceUtil.endSection();
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               var11 = SystemClock.elapsedRealtime();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label167;
            }

            var7 = var6;

            try {
               this.getCodecBuffers(var6);
               break label166;
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
            }
         }

         Exception var32 = var10000;
         if (var7 != null) {
            this.resetCodecBuffers();
            var7.release();
         }

         throw var32;
      }

      this.codec = var6;
      this.codecInfo = var1;
      this.codecOperatingRate = var5;
      this.codecFormat = this.inputFormat;
      this.codecAdaptationWorkaroundMode = this.codecAdaptationWorkaroundMode(var3);
      this.codecNeedsReconfigureWorkaround = codecNeedsReconfigureWorkaround(var3);
      this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(var3, this.codecFormat);
      this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(var3);
      this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(var3);
      this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(var3);
      this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(var3, this.codecFormat);
      boolean var13;
      if (!codecNeedsEosPropagationWorkaround(var1) && !this.getCodecNeedsEosPropagation()) {
         var13 = false;
      } else {
         var13 = true;
      }

      this.codecNeedsEosPropagation = var13;
      this.resetInputBuffer();
      this.resetOutputBuffer();
      long var14;
      if (this.getState() == 2) {
         var14 = SystemClock.elapsedRealtime() + 1000L;
      } else {
         var14 = -9223372036854775807L;
      }

      this.codecHotswapDeadlineMs = var14;
      this.codecReconfigured = false;
      this.codecReconfigurationState = 0;
      this.codecReceivedEos = false;
      this.codecReceivedBuffers = false;
      this.codecDrainState = 0;
      this.codecDrainAction = 0;
      this.codecNeedsAdaptationWorkaroundBuffer = false;
      this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
      this.shouldSkipOutputBuffer = false;
      this.waitingForFirstSyncSample = true;
      DecoderCounters var33 = this.decoderCounters;
      ++var33.decoderInitCount;
      this.onCodecInitialized(var3, var11, var11 - var8);
   }

   private void maybeInitCodecWithFallback(MediaCrypto var1, boolean var2) throws MediaCodecRenderer.DecoderInitializationException {
      if (this.availableCodecInfos == null) {
         try {
            ArrayDeque var3 = new ArrayDeque(this.getAvailableCodecInfos(var2));
            this.availableCodecInfos = var3;
            this.preferredDecoderInitializationException = null;
         } catch (MediaCodecUtil.DecoderQueryException var6) {
            throw new MediaCodecRenderer.DecoderInitializationException(this.inputFormat, var6, var2, -49998);
         }
      }

      if (this.availableCodecInfos.isEmpty()) {
         throw new MediaCodecRenderer.DecoderInitializationException(this.inputFormat, (Throwable)null, var2, -49999);
      } else {
         while(this.codec == null) {
            MediaCodecInfo var4 = (MediaCodecInfo)this.availableCodecInfos.peekFirst();
            if (!this.shouldInitCodec(var4)) {
               return;
            }

            try {
               this.initCodec(var4, var1);
            } catch (Exception var7) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Failed to initialize decoder: ");
               var5.append(var4);
               Log.w("MediaCodecRenderer", var5.toString(), var7);
               this.availableCodecInfos.removeFirst();
               MediaCodecRenderer.DecoderInitializationException var9 = new MediaCodecRenderer.DecoderInitializationException(this.inputFormat, var7, var2, var4.name);
               MediaCodecRenderer.DecoderInitializationException var8 = this.preferredDecoderInitializationException;
               if (var8 == null) {
                  this.preferredDecoderInitializationException = var9;
               } else {
                  this.preferredDecoderInitializationException = var8.copyWithFallbackException(var9);
               }

               if (this.availableCodecInfos.isEmpty()) {
                  throw this.preferredDecoderInitializationException;
               }
            }
         }

         this.availableCodecInfos = null;
      }
   }

   private void processEndOfStream() throws ExoPlaybackException {
      int var1 = this.codecDrainAction;
      if (var1 != 1) {
         if (var1 != 2) {
            this.outputStreamEnded = true;
            this.renderToEndOfStream();
         } else {
            this.releaseCodec();
            this.maybeInitCodec();
         }
      } else {
         this.flushOrReinitCodec();
      }

   }

   private void processOutputBuffersChanged() {
      if (Util.SDK_INT < 21) {
         this.outputBuffers = this.codec.getOutputBuffers();
      }

   }

   private void processOutputFormat() throws ExoPlaybackException {
      MediaFormat var1 = this.codec.getOutputFormat();
      if (this.codecAdaptationWorkaroundMode != 0 && var1.getInteger("width") == 32 && var1.getInteger("height") == 32) {
         this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
      } else {
         if (this.codecNeedsMonoChannelCountWorkaround) {
            var1.setInteger("channel-count", 1);
         }

         this.onOutputFormatChanged(this.codec, var1);
      }
   }

   private void releaseDrmSessionIfUnused(DrmSession var1) {
      if (var1 != null && var1 != this.sourceDrmSession && var1 != this.codecDrmSession) {
         this.drmSessionManager.releaseSession(var1);
      }

   }

   private void resetCodecBuffers() {
      if (Util.SDK_INT < 21) {
         this.inputBuffers = null;
         this.outputBuffers = null;
      }

   }

   private void resetInputBuffer() {
      this.inputIndex = -1;
      this.buffer.data = null;
   }

   private void resetOutputBuffer() {
      this.outputIndex = -1;
      this.outputBuffer = null;
   }

   private void setCodecDrmSession(DrmSession var1) {
      DrmSession var2 = this.codecDrmSession;
      this.codecDrmSession = var1;
      this.releaseDrmSessionIfUnused(var2);
   }

   private void setSourceDrmSession(DrmSession var1) {
      DrmSession var2 = this.sourceDrmSession;
      this.sourceDrmSession = var1;
      this.releaseDrmSessionIfUnused(var2);
   }

   private boolean shouldContinueFeeding(long var1) {
      boolean var3;
      if (this.renderTimeLimitMs != -9223372036854775807L && SystemClock.elapsedRealtime() - var1 >= this.renderTimeLimitMs) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   private boolean shouldSkipOutputBuffer(long var1) {
      int var3 = this.decodeOnlyPresentationTimestamps.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         if ((Long)this.decodeOnlyPresentationTimestamps.get(var4) == var1) {
            this.decodeOnlyPresentationTimestamps.remove(var4);
            return true;
         }
      }

      return false;
   }

   private boolean shouldWaitForKeys(boolean var1) throws ExoPlaybackException {
      if (this.codecDrmSession == null || !var1 && this.playClearSamplesWithoutKeys) {
         return false;
      } else {
         int var2 = this.codecDrmSession.getState();
         var1 = true;
         if (var2 != 1) {
            if (var2 == 4) {
               var1 = false;
            }

            return var1;
         } else {
            throw ExoPlaybackException.createForRenderer(this.codecDrmSession.getError(), this.getIndex());
         }
      }
   }

   private void updateCodecOperatingRate() throws ExoPlaybackException {
      if (Util.SDK_INT >= 23) {
         float var1 = this.getCodecOperatingRateV23(this.rendererOperatingRate, this.codecFormat, this.getStreamFormats());
         float var2 = this.codecOperatingRate;
         if (var2 != var1) {
            if (var1 == -1.0F) {
               this.drainAndReinitializeCodec();
            } else if (var2 != -1.0F || var1 > this.assumedMinimumCodecOperatingRate) {
               Bundle var3 = new Bundle();
               var3.putFloat("operating-rate", var1);
               this.codec.setParameters(var3);
               this.codecOperatingRate = var1;
            }
         }

      }
   }

   protected abstract int canKeepCodec(MediaCodec var1, MediaCodecInfo var2, Format var3, Format var4);

   protected abstract void configureCodec(MediaCodecInfo var1, MediaCodec var2, Format var3, MediaCrypto var4, float var5) throws MediaCodecUtil.DecoderQueryException;

   protected final void flushOrReinitCodec() throws ExoPlaybackException {
      if (this.flushOrReleaseCodec()) {
         this.maybeInitCodec();
      }

   }

   protected boolean flushOrReleaseCodec() {
      if (this.codec == null) {
         return false;
      } else if (this.codecDrainAction == 2 || this.codecNeedsFlushWorkaround || this.codecNeedsEosFlushWorkaround && this.codecReceivedEos) {
         this.releaseCodec();
         return true;
      } else {
         this.codec.flush();
         this.resetInputBuffer();
         this.resetOutputBuffer();
         this.codecHotswapDeadlineMs = -9223372036854775807L;
         this.codecReceivedEos = false;
         this.codecReceivedBuffers = false;
         this.waitingForFirstSyncSample = true;
         this.codecNeedsAdaptationWorkaroundBuffer = false;
         this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
         this.shouldSkipOutputBuffer = false;
         this.waitingForKeys = false;
         this.decodeOnlyPresentationTimestamps.clear();
         this.codecDrainState = 0;
         this.codecDrainAction = 0;
         this.codecReconfigurationState = this.codecReconfigured;
         return false;
      }
   }

   protected final MediaCodec getCodec() {
      return this.codec;
   }

   protected final MediaCodecInfo getCodecInfo() {
      return this.codecInfo;
   }

   protected boolean getCodecNeedsEosPropagation() {
      return false;
   }

   protected abstract float getCodecOperatingRateV23(float var1, Format var2, Format[] var3);

   protected List getDecoderInfos(MediaCodecSelector var1, Format var2, boolean var3) throws MediaCodecUtil.DecoderQueryException {
      return var1.getDecoderInfos(var2.sampleMimeType, var3);
   }

   protected long getDequeueOutputBufferTimeoutUs() {
      return 0L;
   }

   public boolean isEnded() {
      return this.outputStreamEnded;
   }

   public boolean isReady() {
      boolean var1;
      if (this.inputFormat == null || this.waitingForKeys || !this.isSourceReady() && !this.hasOutputBuffer() && (this.codecHotswapDeadlineMs == -9223372036854775807L || SystemClock.elapsedRealtime() >= this.codecHotswapDeadlineMs)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected final void maybeInitCodec() throws ExoPlaybackException {
      if (this.codec == null && this.inputFormat != null) {
         this.setCodecDrmSession(this.sourceDrmSession);
         String var1 = this.inputFormat.sampleMimeType;
         DrmSession var2 = this.codecDrmSession;
         if (var2 != null) {
            if (this.mediaCrypto == null) {
               FrameworkMediaCrypto var8 = (FrameworkMediaCrypto)var2.getMediaCrypto();
               if (var8 == null) {
                  if (this.codecDrmSession.getError() == null) {
                     return;
                  }
               } else {
                  try {
                     MediaCrypto var3 = new MediaCrypto(var8.uuid, var8.sessionId);
                     this.mediaCrypto = var3;
                  } catch (MediaCryptoException var7) {
                     throw ExoPlaybackException.createForRenderer(var7, this.getIndex());
                  }

                  boolean var4;
                  if (!var8.forceAllowInsecureDecoderComponents && this.mediaCrypto.requiresSecureDecoderComponent(var1)) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  this.mediaCryptoRequiresSecureDecoder = var4;
               }
            }

            if (this.deviceNeedsDrmKeysToConfigureCodecWorkaround()) {
               int var5 = this.codecDrmSession.getState();
               if (var5 == 1) {
                  throw ExoPlaybackException.createForRenderer(this.codecDrmSession.getError(), this.getIndex());
               }

               if (var5 != 4) {
                  return;
               }
            }
         }

         try {
            this.maybeInitCodecWithFallback(this.mediaCrypto, this.mediaCryptoRequiresSecureDecoder);
         } catch (MediaCodecRenderer.DecoderInitializationException var6) {
            throw ExoPlaybackException.createForRenderer(var6, this.getIndex());
         }
      }
   }

   protected abstract void onCodecInitialized(String var1, long var2, long var4);

   protected void onDisabled() {
      this.inputFormat = null;
      if (this.sourceDrmSession == null && this.codecDrmSession == null) {
         this.flushOrReleaseCodec();
      } else {
         this.onReset();
      }

   }

   protected void onEnabled(boolean var1) throws ExoPlaybackException {
      this.decoderCounters = new DecoderCounters();
   }

   protected void onInputFormatChanged(Format var1) throws ExoPlaybackException {
      Format var2 = this.inputFormat;
      this.inputFormat = var1;
      boolean var3 = true;
      this.waitingForFirstSampleInFormat = true;
      DrmInitData var4 = var1.drmInitData;
      DrmInitData var7;
      if (var2 == null) {
         var7 = null;
      } else {
         var7 = var2.drmInitData;
      }

      if (Util.areEqual(var4, var7) ^ true) {
         if (var1.drmInitData != null) {
            DrmSessionManager var8 = this.drmSessionManager;
            if (var8 == null) {
               throw ExoPlaybackException.createForRenderer(new IllegalStateException("Media requires a DrmSessionManager"), this.getIndex());
            }

            DrmSession var9 = var8.acquireSession(Looper.myLooper(), var1.drmInitData);
            if (var9 == this.sourceDrmSession || var9 == this.codecDrmSession) {
               this.drmSessionManager.releaseSession(var9);
            }

            this.setSourceDrmSession(var9);
         } else {
            this.setSourceDrmSession((DrmSession)null);
         }
      }

      MediaCodec var10 = this.codec;
      if (var10 == null) {
         this.maybeInitCodec();
      } else {
         if (this.sourceDrmSession != this.codecDrmSession) {
            this.drainAndReinitializeCodec();
         } else {
            int var5 = this.canKeepCodec(var10, this.codecInfo, this.codecFormat, var1);
            if (var5 != 0) {
               if (var5 != 1) {
                  if (var5 != 2) {
                     if (var5 != 3) {
                        throw new IllegalStateException();
                     }

                     this.codecFormat = var1;
                     this.updateCodecOperatingRate();
                  } else if (this.codecNeedsReconfigureWorkaround) {
                     this.drainAndReinitializeCodec();
                  } else {
                     this.codecReconfigured = true;
                     this.codecReconfigurationState = 1;
                     var5 = this.codecAdaptationWorkaroundMode;
                     boolean var6 = var3;
                     if (var5 != 2) {
                        label72: {
                           if (var5 == 1) {
                              var5 = var1.width;
                              var2 = this.codecFormat;
                              if (var5 == var2.width && var1.height == var2.height) {
                                 var6 = var3;
                                 break label72;
                              }
                           }

                           var6 = false;
                        }
                     }

                     this.codecNeedsAdaptationWorkaroundBuffer = var6;
                     this.codecFormat = var1;
                     this.updateCodecOperatingRate();
                  }
               } else {
                  this.drainAndFlushCodec();
                  this.codecFormat = var1;
                  this.updateCodecOperatingRate();
               }
            } else {
               this.drainAndReinitializeCodec();
            }
         }

      }
   }

   protected abstract void onOutputFormatChanged(MediaCodec var1, MediaFormat var2) throws ExoPlaybackException;

   protected void onPositionReset(long var1, boolean var3) throws ExoPlaybackException {
      this.inputStreamEnded = false;
      this.outputStreamEnded = false;
      this.flushOrReinitCodec();
      this.formatQueue.clear();
   }

   protected abstract void onProcessedOutputBuffer(long var1);

   protected abstract void onQueueInputBuffer(DecoderInputBuffer var1);

   protected void onReset() {
      try {
         this.releaseCodec();
      } finally {
         this.setSourceDrmSession((DrmSession)null);
      }

   }

   protected void onStarted() {
   }

   protected void onStopped() {
   }

   protected abstract boolean processOutputBuffer(long var1, long var3, MediaCodec var5, ByteBuffer var6, int var7, int var8, long var9, boolean var11, Format var12) throws ExoPlaybackException;

   protected void releaseCodec() {
      this.availableCodecInfos = null;
      this.codecInfo = null;
      this.codecFormat = null;
      this.resetInputBuffer();
      this.resetOutputBuffer();
      this.resetCodecBuffers();
      this.waitingForKeys = false;
      this.codecHotswapDeadlineMs = -9223372036854775807L;
      this.decodeOnlyPresentationTimestamps.clear();

      label381: {
         Throwable var10000;
         label376: {
            boolean var10001;
            try {
               if (this.codec == null) {
                  break label381;
               }

               DecoderCounters var1 = this.decoderCounters;
               ++var1.decoderReleaseCount;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label376;
            }

            try {
               this.codec.stop();
               break label381;
            } finally {
               label370:
               try {
                  this.codec.release();
               } catch (Throwable var39) {
                  var10000 = var39;
                  var10001 = false;
                  break label370;
               }
            }
         }

         Throwable var42 = var10000;
         this.codec = null;

         try {
            if (this.mediaCrypto != null) {
               this.mediaCrypto.release();
            }
         } finally {
            this.mediaCrypto = null;
            this.mediaCryptoRequiresSecureDecoder = false;
            this.setCodecDrmSession((DrmSession)null);
         }

         throw var42;
      }

      this.codec = null;

      try {
         if (this.mediaCrypto != null) {
            this.mediaCrypto.release();
         }
      } finally {
         this.mediaCrypto = null;
         this.mediaCryptoRequiresSecureDecoder = false;
         this.setCodecDrmSession((DrmSession)null);
      }

   }

   public void render(long var1, long var3) throws ExoPlaybackException {
      if (this.outputStreamEnded) {
         this.renderToEndOfStream();
      } else {
         int var5;
         if (this.inputFormat == null) {
            this.flagsOnlyBuffer.clear();
            var5 = this.readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (var5 != -5) {
               if (var5 == -4) {
                  Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                  this.inputStreamEnded = true;
                  this.processEndOfStream();
               }

               return;
            }

            this.onInputFormatChanged(this.formatHolder.format);
         }

         this.maybeInitCodec();
         if (this.codec != null) {
            long var6 = SystemClock.elapsedRealtime();
            TraceUtil.beginSection("drainAndFeed");

            while(true) {
               if (!this.drainOutputBuffer(var1, var3)) {
                  while(this.feedInputBuffer() && this.shouldContinueFeeding(var6)) {
                  }

                  TraceUtil.endSection();
                  break;
               }
            }
         } else {
            DecoderCounters var8 = this.decoderCounters;
            var8.skippedInputBufferCount += this.skipSource(var1);
            this.flagsOnlyBuffer.clear();
            var5 = this.readSource(this.formatHolder, this.flagsOnlyBuffer, false);
            if (var5 == -5) {
               this.onInputFormatChanged(this.formatHolder.format);
            } else if (var5 == -4) {
               Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
               this.inputStreamEnded = true;
               this.processEndOfStream();
            }
         }

         this.decoderCounters.ensureUpdated();
      }
   }

   protected void renderToEndOfStream() throws ExoPlaybackException {
   }

   public final void setOperatingRate(float var1) throws ExoPlaybackException {
      this.rendererOperatingRate = var1;
      if (this.codec != null && this.codecDrainAction != 2) {
         this.updateCodecOperatingRate();
      }

   }

   protected boolean shouldInitCodec(MediaCodecInfo var1) {
      return true;
   }

   public final int supportsFormat(Format var1) throws ExoPlaybackException {
      try {
         int var2 = this.supportsFormat(this.mediaCodecSelector, this.drmSessionManager, var1);
         return var2;
      } catch (MediaCodecUtil.DecoderQueryException var3) {
         throw ExoPlaybackException.createForRenderer(var3, this.getIndex());
      }
   }

   protected abstract int supportsFormat(MediaCodecSelector var1, DrmSessionManager var2, Format var3) throws MediaCodecUtil.DecoderQueryException;

   public final int supportsMixedMimeTypeAdaptation() {
      return 8;
   }

   protected final Format updateOutputFormatForTime(long var1) {
      Format var3 = (Format)this.formatQueue.pollFloor(var1);
      if (var3 != null) {
         this.outputFormat = var3;
      }

      return var3;
   }

   public static class DecoderInitializationException extends Exception {
      public final String decoderName;
      public final String diagnosticInfo;
      public final MediaCodecRenderer.DecoderInitializationException fallbackDecoderInitializationException;
      public final String mimeType;
      public final boolean secureDecoderRequired;

      public DecoderInitializationException(Format var1, Throwable var2, boolean var3, int var4) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Decoder init failed: [");
         var5.append(var4);
         var5.append("], ");
         var5.append(var1);
         this(var5.toString(), var2, var1.sampleMimeType, var3, (String)null, buildCustomDiagnosticInfo(var4), (MediaCodecRenderer.DecoderInitializationException)null);
      }

      public DecoderInitializationException(Format var1, Throwable var2, boolean var3, String var4) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Decoder init failed: ");
         var5.append(var4);
         var5.append(", ");
         var5.append(var1);
         String var7 = var5.toString();
         String var6 = var1.sampleMimeType;
         String var8;
         if (Util.SDK_INT >= 21) {
            var8 = getDiagnosticInfoV21(var2);
         } else {
            var8 = null;
         }

         this(var7, var2, var6, var3, var4, var8, (MediaCodecRenderer.DecoderInitializationException)null);
      }

      private DecoderInitializationException(String var1, Throwable var2, String var3, boolean var4, String var5, String var6, MediaCodecRenderer.DecoderInitializationException var7) {
         super(var1, var2);
         this.mimeType = var3;
         this.secureDecoderRequired = var4;
         this.decoderName = var5;
         this.diagnosticInfo = var6;
         this.fallbackDecoderInitializationException = var7;
      }

      private static String buildCustomDiagnosticInfo(int var0) {
         String var1;
         if (var0 < 0) {
            var1 = "neg_";
         } else {
            var1 = "";
         }

         StringBuilder var2 = new StringBuilder();
         var2.append("com.google.android.exoplayer.MediaCodecTrackRenderer_");
         var2.append(var1);
         var2.append(Math.abs(var0));
         return var2.toString();
      }

      private MediaCodecRenderer.DecoderInitializationException copyWithFallbackException(MediaCodecRenderer.DecoderInitializationException var1) {
         return new MediaCodecRenderer.DecoderInitializationException(this.getMessage(), this.getCause(), this.mimeType, this.secureDecoderRequired, this.decoderName, this.diagnosticInfo, var1);
      }

      @TargetApi(21)
      private static String getDiagnosticInfoV21(Throwable var0) {
         return var0 instanceof CodecException ? ((CodecException)var0).getDiagnosticInfo() : null;
      }
   }
}
