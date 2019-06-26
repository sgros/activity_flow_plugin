package com.google.android.exoplayer2.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaCodec.OnFrameRenderedListener;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Surface;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.List;

@TargetApi(16)
public class MediaCodecVideoRenderer extends MediaCodecRenderer {
   private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = new int[]{1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
   private static boolean deviceNeedsSetOutputSurfaceWorkaround;
   private static boolean evaluatedDeviceNeedsSetOutputSurfaceWorkaround;
   private final long allowedJoiningTimeMs;
   private int buffersInCodecCount;
   private MediaCodecVideoRenderer.CodecMaxValues codecMaxValues;
   private boolean codecNeedsSetOutputSurfaceWorkaround;
   private int consecutiveDroppedFrameCount;
   private final Context context;
   private int currentHeight;
   private float currentPixelWidthHeightRatio;
   private int currentUnappliedRotationDegrees;
   private int currentWidth;
   private final boolean deviceNeedsNoPostProcessWorkaround;
   private long droppedFrameAccumulationStartTimeMs;
   private int droppedFrames;
   private Surface dummySurface;
   private final VideoRendererEventListener.EventDispatcher eventDispatcher;
   private VideoFrameMetadataListener frameMetadataListener;
   private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
   private long initialPositionUs;
   private long joiningDeadlineMs;
   private long lastInputTimeUs;
   private long lastRenderTimeUs;
   private final int maxDroppedFramesToNotify;
   private long outputStreamOffsetUs;
   private int pendingOutputStreamOffsetCount;
   private final long[] pendingOutputStreamOffsetsUs;
   private final long[] pendingOutputStreamSwitchTimesUs;
   private float pendingPixelWidthHeightRatio;
   private int pendingRotationDegrees;
   private boolean renderedFirstFrame;
   private int reportedHeight;
   private float reportedPixelWidthHeightRatio;
   private int reportedUnappliedRotationDegrees;
   private int reportedWidth;
   private int scalingMode;
   private Surface surface;
   private boolean tunneling;
   private int tunnelingAudioSessionId;
   MediaCodecVideoRenderer.OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;

   public MediaCodecVideoRenderer(Context var1, MediaCodecSelector var2, long var3, DrmSessionManager var5, boolean var6, Handler var7, VideoRendererEventListener var8, int var9) {
      super(2, var2, var5, var6, 30.0F);
      this.allowedJoiningTimeMs = var3;
      this.maxDroppedFramesToNotify = var9;
      this.context = var1.getApplicationContext();
      this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(this.context);
      this.eventDispatcher = new VideoRendererEventListener.EventDispatcher(var7, var8);
      this.deviceNeedsNoPostProcessWorkaround = deviceNeedsNoPostProcessWorkaround();
      this.pendingOutputStreamOffsetsUs = new long[10];
      this.pendingOutputStreamSwitchTimesUs = new long[10];
      this.outputStreamOffsetUs = -9223372036854775807L;
      this.lastInputTimeUs = -9223372036854775807L;
      this.joiningDeadlineMs = -9223372036854775807L;
      this.currentWidth = -1;
      this.currentHeight = -1;
      this.currentPixelWidthHeightRatio = -1.0F;
      this.pendingPixelWidthHeightRatio = -1.0F;
      this.scalingMode = 1;
      this.clearReportedVideoSize();
   }

   private void clearRenderedFirstFrame() {
      this.renderedFirstFrame = false;
      if (Util.SDK_INT >= 23 && this.tunneling) {
         MediaCodec var1 = this.getCodec();
         if (var1 != null) {
            this.tunnelingOnFrameRenderedListener = new MediaCodecVideoRenderer.OnFrameRenderedListenerV23(var1);
         }
      }

   }

   private void clearReportedVideoSize() {
      this.reportedWidth = -1;
      this.reportedHeight = -1;
      this.reportedPixelWidthHeightRatio = -1.0F;
      this.reportedUnappliedRotationDegrees = -1;
   }

   @TargetApi(21)
   private static void configureTunnelingV21(MediaFormat var0, int var1) {
      var0.setFeatureEnabled("tunneled-playback", true);
      var0.setInteger("audio-session-id", var1);
   }

   private static boolean deviceNeedsNoPostProcessWorkaround() {
      return "NVIDIA".equals(Util.MANUFACTURER);
   }

   private static int getCodecMaxInputSize(MediaCodecInfo var0, String var1, int var2, int var3) {
      if (var2 != -1 && var3 != -1) {
         byte var5;
         byte var7;
         label76: {
            int var4 = var1.hashCode();
            var5 = 4;
            switch(var4) {
            case -1664118616:
               if (var1.equals("video/3gpp")) {
                  var7 = 0;
                  break label76;
               }
               break;
            case -1662541442:
               if (var1.equals("video/hevc")) {
                  var7 = 4;
                  break label76;
               }
               break;
            case 1187890754:
               if (var1.equals("video/mp4v-es")) {
                  var7 = 1;
                  break label76;
               }
               break;
            case 1331836730:
               if (var1.equals("video/avc")) {
                  var7 = 2;
                  break label76;
               }
               break;
            case 1599127256:
               if (var1.equals("video/x-vnd.on2.vp8")) {
                  var7 = 3;
                  break label76;
               }
               break;
            case 1599127257:
               if (var1.equals("video/x-vnd.on2.vp9")) {
                  var7 = 5;
                  break label76;
               }
            }

            var7 = -1;
         }

         byte var6;
         label62: {
            if (var7 != 0 && var7 != 1) {
               if (var7 == 2) {
                  if ("BRAVIA 4K 2015".equals(Util.MODEL) || "Amazon".equals(Util.MANUFACTURER) && ("KFSOWI".equals(Util.MODEL) || "AFTS".equals(Util.MODEL) && var0.secure)) {
                     return -1;
                  }

                  var2 = Util.ceilDivide(var2, 16) * Util.ceilDivide(var3, 16) * 16 * 16;
                  break label62;
               }

               if (var7 != 3) {
                  if (var7 != 4 && var7 != 5) {
                     return -1;
                  }

                  var2 *= var3;
                  var6 = var5;
                  return var2 * 3 / (var6 * 2);
               }
            }

            var2 *= var3;
         }

         var6 = 2;
         return var2 * 3 / (var6 * 2);
      } else {
         return -1;
      }
   }

   private static Point getCodecMaxSize(MediaCodecInfo var0, Format var1) throws MediaCodecUtil.DecoderQueryException {
      int var2 = var1.height;
      int var3 = var1.width;
      int var4 = 0;
      boolean var14;
      if (var2 > var3) {
         var14 = true;
      } else {
         var14 = false;
      }

      if (var14) {
         var3 = var1.height;
      } else {
         var3 = var1.width;
      }

      int var5;
      if (var14) {
         var5 = var1.width;
      } else {
         var5 = var1.height;
      }

      float var6 = (float)var5 / (float)var3;
      int[] var7 = STANDARD_LONG_EDGE_VIDEO_PX;

      for(int var8 = var7.length; var4 < var8; ++var4) {
         int var9 = var7[var4];
         int var10 = (int)((float)var9 * var6);
         if (var9 <= var3 || var10 <= var5) {
            break;
         }

         int var11;
         if (Util.SDK_INT >= 21) {
            if (var14) {
               var11 = var10;
            } else {
               var11 = var9;
            }

            if (!var14) {
               var9 = var10;
            }

            Point var12 = var0.alignVideoSizeV21(var11, var9);
            float var13 = var1.frameRate;
            if (var0.isVideoSizeAndRateSupportedV21(var12.x, var12.y, (double)var13)) {
               return var12;
            }
         } else {
            var11 = Util.ceilDivide(var9, 16) * 16;
            var10 = Util.ceilDivide(var10, 16) * 16;
            if (var11 * var10 <= MediaCodecUtil.maxH264DecodableFrameSize()) {
               if (var14) {
                  var3 = var10;
               } else {
                  var3 = var11;
               }

               if (var14) {
                  var10 = var11;
               }

               return new Point(var3, var10);
            }
         }
      }

      return null;
   }

   private static int getMaxInputSize(MediaCodecInfo var0, Format var1) {
      if (var1.maxInputSize == -1) {
         return getCodecMaxInputSize(var0, var1.sampleMimeType, var1.width, var1.height);
      } else {
         int var2 = var1.initializationData.size();
         int var3 = 0;

         int var4;
         for(var4 = 0; var3 < var2; ++var3) {
            var4 += ((byte[])var1.initializationData.get(var3)).length;
         }

         return var1.maxInputSize + var4;
      }
   }

   private static boolean isBufferLate(long var0) {
      boolean var2;
      if (var0 < -30000L) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean isBufferVeryLate(long var0) {
      boolean var2;
      if (var0 < -500000L) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void maybeNotifyDroppedFrames() {
      if (this.droppedFrames > 0) {
         long var1 = SystemClock.elapsedRealtime();
         long var3 = this.droppedFrameAccumulationStartTimeMs;
         this.eventDispatcher.droppedFrames(this.droppedFrames, var1 - var3);
         this.droppedFrames = 0;
         this.droppedFrameAccumulationStartTimeMs = var1;
      }

   }

   private void maybeNotifyVideoSizeChanged() {
      if ((this.currentWidth != -1 || this.currentHeight != -1) && (this.reportedWidth != this.currentWidth || this.reportedHeight != this.currentHeight || this.reportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.reportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio)) {
         this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
         this.reportedWidth = this.currentWidth;
         this.reportedHeight = this.currentHeight;
         this.reportedUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
         this.reportedPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
      }

   }

   private void maybeRenotifyRenderedFirstFrame() {
      if (this.renderedFirstFrame) {
         this.eventDispatcher.renderedFirstFrame(this.surface);
      }

   }

   private void maybeRenotifyVideoSizeChanged() {
      if (this.reportedWidth != -1 || this.reportedHeight != -1) {
         this.eventDispatcher.videoSizeChanged(this.reportedWidth, this.reportedHeight, this.reportedUnappliedRotationDegrees, this.reportedPixelWidthHeightRatio);
      }

   }

   private void notifyFrameMetadataListener(long var1, long var3, Format var5) {
      VideoFrameMetadataListener var6 = this.frameMetadataListener;
      if (var6 != null) {
         var6.onVideoFrameAboutToBeRendered(var1, var3, var5);
      }

   }

   private void processOutputFormat(MediaCodec var1, int var2, int var3) {
      this.currentWidth = var2;
      this.currentHeight = var3;
      this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
      if (Util.SDK_INT >= 21) {
         var2 = this.pendingRotationDegrees;
         if (var2 == 90 || var2 == 270) {
            var2 = this.currentWidth;
            this.currentWidth = this.currentHeight;
            this.currentHeight = var2;
            this.currentPixelWidthHeightRatio = 1.0F / this.currentPixelWidthHeightRatio;
         }
      } else {
         this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
      }

      var1.setVideoScalingMode(this.scalingMode);
   }

   private void setJoiningDeadlineMs() {
      long var1;
      if (this.allowedJoiningTimeMs > 0L) {
         var1 = SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs;
      } else {
         var1 = -9223372036854775807L;
      }

      this.joiningDeadlineMs = var1;
   }

   @TargetApi(23)
   private static void setOutputSurfaceV23(MediaCodec var0, Surface var1) {
      var0.setOutputSurface(var1);
   }

   private void setSurface(Surface var1) throws ExoPlaybackException {
      Surface var2 = var1;
      if (var1 == null) {
         var2 = this.dummySurface;
         if (var2 == null) {
            MediaCodecInfo var3 = this.getCodecInfo();
            var2 = var1;
            if (var3 != null) {
               var2 = var1;
               if (this.shouldUseDummySurface(var3)) {
                  this.dummySurface = DummySurface.newInstanceV17(this.context, var3.secure);
                  var2 = this.dummySurface;
               }
            }
         }
      }

      if (this.surface != var2) {
         this.surface = var2;
         int var4 = this.getState();
         MediaCodec var5 = this.getCodec();
         if (var5 != null) {
            if (Util.SDK_INT >= 23 && var2 != null && !this.codecNeedsSetOutputSurfaceWorkaround) {
               setOutputSurfaceV23(var5, var2);
            } else {
               this.releaseCodec();
               this.maybeInitCodec();
            }
         }

         if (var2 != null && var2 != this.dummySurface) {
            this.maybeRenotifyVideoSizeChanged();
            this.clearRenderedFirstFrame();
            if (var4 == 2) {
               this.setJoiningDeadlineMs();
            }
         } else {
            this.clearReportedVideoSize();
            this.clearRenderedFirstFrame();
         }
      } else if (var2 != null && var2 != this.dummySurface) {
         this.maybeRenotifyVideoSizeChanged();
         this.maybeRenotifyRenderedFirstFrame();
      }

   }

   private boolean shouldUseDummySurface(MediaCodecInfo var1) {
      boolean var2;
      if (Util.SDK_INT < 23 || this.tunneling || this.codecNeedsSetOutputSurfaceWorkaround(var1.name) || var1.secure && !DummySurface.isSecureSupported(this.context)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected int canKeepCodec(MediaCodec var1, MediaCodecInfo var2, Format var3, Format var4) {
      if (var2.isSeamlessAdaptationSupported(var3, var4, true)) {
         int var5 = var4.width;
         MediaCodecVideoRenderer.CodecMaxValues var6 = this.codecMaxValues;
         if (var5 <= var6.width && var4.height <= var6.height && getMaxInputSize(var2, var4) <= this.codecMaxValues.inputSize) {
            byte var7;
            if (var3.initializationDataEquals(var4)) {
               var7 = 3;
            } else {
               var7 = 2;
            }

            return var7;
         }
      }

      return 0;
   }

   protected boolean codecNeedsSetOutputSurfaceWorkaround(String var1) {
      boolean var2 = var1.startsWith("OMX.google");
      byte var3 = 0;
      if (var2) {
         return false;
      } else {
         synchronized(MediaCodecVideoRenderer.class){}

         Throwable var10000;
         boolean var10001;
         label201632: {
            label201631: {
               label201770: {
                  try {
                     if (evaluatedDeviceNeedsSetOutputSurfaceWorkaround) {
                        break label201631;
                     }

                     if (Util.SDK_INT <= 27 && "dangal".equals(Util.DEVICE)) {
                        deviceNeedsSetOutputSurfaceWorkaround = true;
                        break label201770;
                     }
                  } catch (Throwable var18364) {
                     var10000 = var18364;
                     var10001 = false;
                     break label201632;
                  }

                  try {
                     if (Util.SDK_INT >= 27) {
                        break label201770;
                     }
                  } catch (Throwable var18363) {
                     var10000 = var18363;
                     var10001 = false;
                     break label201632;
                  }

                  byte var4;
                  label201613: {
                     label201612: {
                        label201611: {
                           label201610: {
                              label201643: {
                                 label201644: {
                                    label201645: {
                                       label201646: {
                                          label201647: {
                                             label201648: {
                                                label201649: {
                                                   label201650: {
                                                      label201651: {
                                                         label201652: {
                                                            label201653: {
                                                               label201654: {
                                                                  label201655: {
                                                                     label201656: {
                                                                        label201657: {
                                                                           label201658: {
                                                                              label201659: {
                                                                                 label201660: {
                                                                                    label201661: {
                                                                                       label201662: {
                                                                                          label201663: {
                                                                                             label201664: {
                                                                                                label201665: {
                                                                                                   label201666: {
                                                                                                      label201667: {
                                                                                                         label201668: {
                                                                                                            label201669: {
                                                                                                               label201670: {
                                                                                                                  label201671: {
                                                                                                                     label201672: {
                                                                                                                        label201673: {
                                                                                                                           label201674: {
                                                                                                                              label201675: {
                                                                                                                                 label201676: {
                                                                                                                                    label201677: {
                                                                                                                                       label201678: {
                                                                                                                                          label201679: {
                                                                                                                                             label201680: {
                                                                                                                                                label201681: {
                                                                                                                                                   label201682: {
                                                                                                                                                      label201683: {
                                                                                                                                                         label201684: {
                                                                                                                                                            label201685: {
                                                                                                                                                               label201686: {
                                                                                                                                                                  label201687: {
                                                                                                                                                                     label201688: {
                                                                                                                                                                        label201689: {
                                                                                                                                                                           label201690: {
                                                                                                                                                                              label201691: {
                                                                                                                                                                                 label201692: {
                                                                                                                                                                                    label201693: {
                                                                                                                                                                                       label201694: {
                                                                                                                                                                                          label201695: {
                                                                                                                                                                                             label201696: {
                                                                                                                                                                                                label201697: {
                                                                                                                                                                                                   label201698: {
                                                                                                                                                                                                      label201699: {
                                                                                                                                                                                                         label201700: {
                                                                                                                                                                                                            label201701: {
                                                                                                                                                                                                               label201702: {
                                                                                                                                                                                                                  label201703: {
                                                                                                                                                                                                                     label201704: {
                                                                                                                                                                                                                        label201705: {
                                                                                                                                                                                                                           label201706: {
                                                                                                                                                                                                                              label201707: {
                                                                                                                                                                                                                                 label201708: {
                                                                                                                                                                                                                                    label201709: {
                                                                                                                                                                                                                                       label201710: {
                                                                                                                                                                                                                                          label201711: {
                                                                                                                                                                                                                                             label201712: {
                                                                                                                                                                                                                                                label201713: {
                                                                                                                                                                                                                                                   label201714: {
                                                                                                                                                                                                                                                      label201715: {
                                                                                                                                                                                                                                                         label201716: {
                                                                                                                                                                                                                                                            label201717: {
                                                                                                                                                                                                                                                               label201718: {
                                                                                                                                                                                                                                                                  label201719: {
                                                                                                                                                                                                                                                                     label201720: {
                                                                                                                                                                                                                                                                        label201721: {
                                                                                                                                                                                                                                                                           label201722: {
                                                                                                                                                                                                                                                                              label201723: {
                                                                                                                                                                                                                                                                                 label201724: {
                                                                                                                                                                                                                                                                                    label201725: {
                                                                                                                                                                                                                                                                                       label201726: {
                                                                                                                                                                                                                                                                                          label201727: {
                                                                                                                                                                                                                                                                                             label201728: {
                                                                                                                                                                                                                                                                                                label201729: {
                                                                                                                                                                                                                                                                                                   label201730: {
                                                                                                                                                                                                                                                                                                      label201731: {
                                                                                                                                                                                                                                                                                                         label201732: {
                                                                                                                                                                                                                                                                                                            label201733: {
                                                                                                                                                                                                                                                                                                               label201734: {
                                                                                                                                                                                                                                                                                                                  label201735: {
                                                                                                                                                                                                                                                                                                                     label201736: {
                                                                                                                                                                                                                                                                                                                        label201737: {
                                                                                                                                                                                                                                                                                                                           label201738: {
                                                                                                                                                                                                                                                                                                                              label201739: {
                                                                                                                                                                                                                                                                                                                                 label201740: {
                                                                                                                                                                                                                                                                                                                                    label201741: {
                                                                                                                                                                                                                                                                                                                                       label201742: {
                                                                                                                                                                                                                                                                                                                                          label201743: {
                                                                                                                                                                                                                                                                                                                                             label201744: {
                                                                                                                                                                                                                                                                                                                                                label201745: {
                                                                                                                                                                                                                                                                                                                                                   label201746: {
                                                                                                                                                                                                                                                                                                                                                      label201747: {
                                                                                                                                                                                                                                                                                                                                                         label201748: {
                                                                                                                                                                                                                                                                                                                                                            label201749: {
                                                                                                                                                                                                                                                                                                                                                               label201750: {
                                                                                                                                                                                                                                                                                                                                                                  label201751: {
                                                                                                                                                                                                                                                                                                                                                                     label201752: {
                                                                                                                                                                                                                                                                                                                                                                        label201753: {
                                                                                                                                                                                                                                                                                                                                                                           label201754: {
                                                                                                                                                                                                                                                                                                                                                                              label201755: {
                                                                                                                                                                                                                                                                                                                                                                                 label201756: {
                                                                                                                                                                                                                                                                                                                                                                                    label201757: {
                                                                                                                                                                                                                                                                                                                                                                                       label201758: {
                                                                                                                                                                                                                                                                                                                                                                                          label201759: {
                                                                                                                                                                                                                                                                                                                                                                                             label201760: {
                                                                                                                                                                                                                                                                                                                                                                                                label201761: {
                                                                                                                                                                                                                                                                                                                                                                                                   label201762: {
                                                                                                                                                                                                                                                                                                                                                                                                      label201763: {
                                                                                                                                                                                                                                                                                                                                                                                                         label201488: {
                                                                                                                                                                                                                                                                                                                                                                                                            label201487: {
                                                                                                                                                                                                                                                                                                                                                                                                               label201486: {
                                                                                                                                                                                                                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                                                                                                                                                                                                                     var1 = Util.DEVICE;
                                                                                                                                                                                                                                                                                                                                                                                                                     switch(var1.hashCode()) {
                                                                                                                                                                                                                                                                                                                                                                                                                     case -2144781245:
                                                                                                                                                                                                                                                                                                                                                                                                                        break;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -2144781185:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201486;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -2144781160:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201487;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -2097309513:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201729;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -2022874474:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201726;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1978993182:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201722;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1978990237:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201724;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1936688988:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201721;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1936688066:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201718;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1936688065:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201714;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1931988508:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201716;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1696512866:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201713;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1680025915:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201710;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1615810839:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201706;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1554255044:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201708;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1481772737:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201705;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1481772730:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201702;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1481772729:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201698;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1320080169:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201700;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1217592143:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201697;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1180384755:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201694;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1139198265:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201690;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -1052835013:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201692;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -993250464:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201689;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -965403638:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201686;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -958336948:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201682;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -879245230:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201684;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -842500323:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201681;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -821392978:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201678;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -797483286:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201674;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -794946968:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201676;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -788334647:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201673;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -782144577:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201670;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -575125681:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201666;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -521118391:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201668;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -430914369:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201665;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -290434366:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201662;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -282781963:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201658;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -277133239:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201660;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -173639913:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201657;
                                                                                                                                                                                                                                                                                                                                                                                                                     case -56598463:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201656;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2126:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201654;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2564:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201655;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2715:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201653;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2719:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201652;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 3483:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201650;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 73405:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201651;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 75739:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201649;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 76779:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201648;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 78669:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201646;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 79305:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201647;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 80618:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201645;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 88274:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201644;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 98846:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201763;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 98848:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201488;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 99329:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201762;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 101481:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201761;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1513190:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201759;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1514184:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201760;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1514185:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201758;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2436959:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201757;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2463773:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201755;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2464648:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201756;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2689555:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201754;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 3154429:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201753;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 3284551:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201751;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 3351335:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201752;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 3386211:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201750;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 41325051:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201749;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 55178625:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201747;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 61542055:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201748;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 65355429:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201746;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66214468:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201745;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66214470:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201743;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66214473:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201744;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66215429:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201742;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66215431:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201741;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66215433:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201739;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 66216390:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201740;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 76402249:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201738;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 76404105:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201737;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 76404911:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201735;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 80963634:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201736;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 82882791:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201734;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 98715550:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201733;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 102844228:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201731;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 165221241:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201732;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 182191441:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201730;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 245388979:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201728;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 287431619:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201725;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 307593612:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201727;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 308517133:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201723;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 316215098:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201720;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 316215116:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201717;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 316246811:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201719;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 316246818:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201715;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 407160593:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201712;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 507412548:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201709;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 793982701:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201711;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 794038622:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201707;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 794040393:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201704;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 835649806:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201701;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 917340916:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201703;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 958008161:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201699;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1060579533:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201696;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1150207623:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201693;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1176899427:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201695;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1280332038:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201691;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1306947716:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201688;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1349174697:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201685;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1522194893:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201687;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1691543273:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201683;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1709443163:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201680;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1865889110:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201677;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1906253259:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201679;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 1977196784:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201675;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2006372676:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201672;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2029784656:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201669;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2030379515:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201671;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2033393791:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201667;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2047190025:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201664;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2047252157:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201661;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2048319463:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201663;
                                                                                                                                                                                                                                                                                                                                                                                                                     case 2048855701:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201659;
                                                                                                                                                                                                                                                                                                                                                                                                                     default:
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                                                                                                                                                                  } catch (Throwable var18362) {
                                                                                                                                                                                                                                                                                                                                                                                                                     var10000 = var18362;
                                                                                                                                                                                                                                                                                                                                                                                                                     var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                                     break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                                                                                                                                                                                                                     if (var1.equals("GIONEE_SWW1609")) {
                                                                                                                                                                                                                                                                                                                                                                                                                        break label201610;
                                                                                                                                                                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                                                                                                                                                                     break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                                  } catch (Throwable var18240) {
                                                                                                                                                                                                                                                                                                                                                                                                                     var10000 = var18240;
                                                                                                                                                                                                                                                                                                                                                                                                                     var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                                     break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                                                                                                                               try {
                                                                                                                                                                                                                                                                                                                                                                                                                  if (var1.equals("GIONEE_SWW1627")) {
                                                                                                                                                                                                                                                                                                                                                                                                                     break label201611;
                                                                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                                                                                  break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                               } catch (Throwable var18239) {
                                                                                                                                                                                                                                                                                                                                                                                                                  var10000 = var18239;
                                                                                                                                                                                                                                                                                                                                                                                                                  var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                                  break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                                                                                               if (var1.equals("GIONEE_SWW1631")) {
                                                                                                                                                                                                                                                                                                                                                                                                                  break label201612;
                                                                                                                                                                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                                                                                                                                                                               break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                            } catch (Throwable var18238) {
                                                                                                                                                                                                                                                                                                                                                                                                               var10000 = var18238;
                                                                                                                                                                                                                                                                                                                                                                                                               var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                               break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                                                                                                                         try {
                                                                                                                                                                                                                                                                                                                                                                                                            if (!var1.equals("cv3")) {
                                                                                                                                                                                                                                                                                                                                                                                                               break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                         } catch (Throwable var18310) {
                                                                                                                                                                                                                                                                                                                                                                                                            var10000 = var18310;
                                                                                                                                                                                                                                                                                                                                                                                                            var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                            break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                                                                                                                         var4 = 22;
                                                                                                                                                                                                                                                                                                                                                                                                         break label201613;
                                                                                                                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                                                                                                                      try {
                                                                                                                                                                                                                                                                                                                                                                                                         if (!var1.equals("cv1")) {
                                                                                                                                                                                                                                                                                                                                                                                                            break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                                                                                                                                                                      } catch (Throwable var18311) {
                                                                                                                                                                                                                                                                                                                                                                                                         var10000 = var18311;
                                                                                                                                                                                                                                                                                                                                                                                                         var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                         break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                                                                                                                      var4 = 21;
                                                                                                                                                                                                                                                                                                                                                                                                      break label201613;
                                                                                                                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                                                                                                                   try {
                                                                                                                                                                                                                                                                                                                                                                                                      if (!var1.equals("deb")) {
                                                                                                                                                                                                                                                                                                                                                                                                         break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                                                                   } catch (Throwable var18309) {
                                                                                                                                                                                                                                                                                                                                                                                                      var10000 = var18309;
                                                                                                                                                                                                                                                                                                                                                                                                      var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                      break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                                                                                                                   var4 = 23;
                                                                                                                                                                                                                                                                                                                                                                                                   break label201613;
                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                                                                                                   if (!var1.equals("flo")) {
                                                                                                                                                                                                                                                                                                                                                                                                      break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                                                                                                                                                                } catch (Throwable var18308) {
                                                                                                                                                                                                                                                                                                                                                                                                   var10000 = var18308;
                                                                                                                                                                                                                                                                                                                                                                                                   var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                   break label201632;
                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                var4 = 37;
                                                                                                                                                                                                                                                                                                                                                                                                break label201613;
                                                                                                                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                                                                                                                             try {
                                                                                                                                                                                                                                                                                                                                                                                                if (!var1.equals("1713")) {
                                                                                                                                                                                                                                                                                                                                                                                                   break label201643;
                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                             } catch (Throwable var18306) {
                                                                                                                                                                                                                                                                                                                                                                                                var10000 = var18306;
                                                                                                                                                                                                                                                                                                                                                                                                var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                                break label201632;
                                                                                                                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                                                                                                                             var4 = 1;
                                                                                                                                                                                                                                                                                                                                                                                             break label201613;
                                                                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                                                                          try {
                                                                                                                                                                                                                                                                                                                                                                                             if (!var1.equals("1601")) {
                                                                                                                                                                                                                                                                                                                                                                                                break label201643;
                                                                                                                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                                                                                                                          } catch (Throwable var18307) {
                                                                                                                                                                                                                                                                                                                                                                                             var10000 = var18307;
                                                                                                                                                                                                                                                                                                                                                                                             var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                             break label201632;
                                                                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                                                                          var4 = 0;
                                                                                                                                                                                                                                                                                                                                                                                          break label201613;
                                                                                                                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                                                                                                                       try {
                                                                                                                                                                                                                                                                                                                                                                                          if (!var1.equals("1714")) {
                                                                                                                                                                                                                                                                                                                                                                                             break label201643;
                                                                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                                                                       } catch (Throwable var18305) {
                                                                                                                                                                                                                                                                                                                                                                                          var10000 = var18305;
                                                                                                                                                                                                                                                                                                                                                                                          var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                          break label201632;
                                                                                                                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                                                                                                                       var4 = 2;
                                                                                                                                                                                                                                                                                                                                                                                       break label201613;
                                                                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                                                                                       if (!var1.equals("P681")) {
                                                                                                                                                                                                                                                                                                                                                                                          break label201643;
                                                                                                                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                                                                                                                    } catch (Throwable var18304) {
                                                                                                                                                                                                                                                                                                                                                                                       var10000 = var18304;
                                                                                                                                                                                                                                                                                                                                                                                       var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                       break label201632;
                                                                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                                                                    var4 = 79;
                                                                                                                                                                                                                                                                                                                                                                                    break label201613;
                                                                                                                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                                                                                                                 try {
                                                                                                                                                                                                                                                                                                                                                                                    if (!var1.equals("Q427")) {
                                                                                                                                                                                                                                                                                                                                                                                       break label201643;
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                 } catch (Throwable var18302) {
                                                                                                                                                                                                                                                                                                                                                                                    var10000 = var18302;
                                                                                                                                                                                                                                                                                                                                                                                    var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                    break label201632;
                                                                                                                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                                                                                                                 var4 = 96;
                                                                                                                                                                                                                                                                                                                                                                                 break label201613;
                                                                                                                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                                                                                                                              try {
                                                                                                                                                                                                                                                                                                                                                                                 if (!var1.equals("Q350")) {
                                                                                                                                                                                                                                                                                                                                                                                    break label201643;
                                                                                                                                                                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                                                                                                                                                              } catch (Throwable var18303) {
                                                                                                                                                                                                                                                                                                                                                                                 var10000 = var18303;
                                                                                                                                                                                                                                                                                                                                                                                 var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                                 break label201632;
                                                                                                                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                                                                                                                              var4 = 94;
                                                                                                                                                                                                                                                                                                                                                                              break label201613;
                                                                                                                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                                                                                                                           try {
                                                                                                                                                                                                                                                                                                                                                                              if (!var1.equals("XE2X")) {
                                                                                                                                                                                                                                                                                                                                                                                 break label201643;
                                                                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                                                                           } catch (Throwable var18301) {
                                                                                                                                                                                                                                                                                                                                                                              var10000 = var18301;
                                                                                                                                                                                                                                                                                                                                                                              var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                              break label201632;
                                                                                                                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                                                                                                                           var4 = 120;
                                                                                                                                                                                                                                                                                                                                                                           break label201613;
                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                                                                           if (!var1.equals("fugu")) {
                                                                                                                                                                                                                                                                                                                                                                              break label201643;
                                                                                                                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                                                                                                                        } catch (Throwable var18300) {
                                                                                                                                                                                                                                                                                                                                                                           var10000 = var18300;
                                                                                                                                                                                                                                                                                                                                                                           var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                           break label201632;
                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                        var4 = 38;
                                                                                                                                                                                                                                                                                                                                                                        break label201613;
                                                                                                                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                                                                                                                     try {
                                                                                                                                                                                                                                                                                                                                                                        if (!var1.equals("mido")) {
                                                                                                                                                                                                                                                                                                                                                                           break label201643;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                     } catch (Throwable var18298) {
                                                                                                                                                                                                                                                                                                                                                                        var10000 = var18298;
                                                                                                                                                                                                                                                                                                                                                                        var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                        break label201632;
                                                                                                                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                                                                                                                     var4 = 71;
                                                                                                                                                                                                                                                                                                                                                                     break label201613;
                                                                                                                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                                                                                                                                                                     if (!var1.equals("kate")) {
                                                                                                                                                                                                                                                                                                                                                                        break label201643;
                                                                                                                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                                                                                                                  } catch (Throwable var18299) {
                                                                                                                                                                                                                                                                                                                                                                     var10000 = var18299;
                                                                                                                                                                                                                                                                                                                                                                     var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                     break label201632;
                                                                                                                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                                                                                                                  var4 = 63;
                                                                                                                                                                                                                                                                                                                                                                  break label201613;
                                                                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                                                                               try {
                                                                                                                                                                                                                                                                                                                                                                  if (!var1.equals("p212")) {
                                                                                                                                                                                                                                                                                                                                                                     break label201643;
                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                               } catch (Throwable var18297) {
                                                                                                                                                                                                                                                                                                                                                                  var10000 = var18297;
                                                                                                                                                                                                                                                                                                                                                                  var10001 = false;
                                                                                                                                                                                                                                                                                                                                                                  break label201632;
                                                                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                                                                               var4 = 78;
                                                                                                                                                                                                                                                                                                                                                               break label201613;
                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                                               if (!var1.equals("MEIZU_M5")) {
                                                                                                                                                                                                                                                                                                                                                                  break label201643;
                                                                                                                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                                                                                                                            } catch (Throwable var18296) {
                                                                                                                                                                                                                                                                                                                                                               var10000 = var18296;
                                                                                                                                                                                                                                                                                                                                                               var10001 = false;
                                                                                                                                                                                                                                                                                                                                                               break label201632;
                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                            var4 = 69;
                                                                                                                                                                                                                                                                                                                                                            break label201613;
                                                                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                                                                         try {
                                                                                                                                                                                                                                                                                                                                                            if (!var1.equals("A1601")) {
                                                                                                                                                                                                                                                                                                                                                               break label201643;
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                         } catch (Throwable var18294) {
                                                                                                                                                                                                                                                                                                                                                            var10000 = var18294;
                                                                                                                                                                                                                                                                                                                                                            var10001 = false;
                                                                                                                                                                                                                                                                                                                                                            break label201632;
                                                                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                                                                         var4 = 4;
                                                                                                                                                                                                                                                                                                                                                         break label201613;
                                                                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                                                                      try {
                                                                                                                                                                                                                                                                                                                                                         if (!var1.equals("Aura_Note_2")) {
                                                                                                                                                                                                                                                                                                                                                            break label201643;
                                                                                                                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                                                                                                                      } catch (Throwable var18295) {
                                                                                                                                                                                                                                                                                                                                                         var10000 = var18295;
                                                                                                                                                                                                                                                                                                                                                         var10001 = false;
                                                                                                                                                                                                                                                                                                                                                         break label201632;
                                                                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                                                                      var4 = 12;
                                                                                                                                                                                                                                                                                                                                                      break label201613;
                                                                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                                                                   try {
                                                                                                                                                                                                                                                                                                                                                      if (!var1.equals("E5643")) {
                                                                                                                                                                                                                                                                                                                                                         break label201643;
                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                   } catch (Throwable var18293) {
                                                                                                                                                                                                                                                                                                                                                      var10000 = var18293;
                                                                                                                                                                                                                                                                                                                                                      var10001 = false;
                                                                                                                                                                                                                                                                                                                                                      break label201632;
                                                                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                                                                   var4 = 24;
                                                                                                                                                                                                                                                                                                                                                   break label201613;
                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                                                   if (!var1.equals("F3111")) {
                                                                                                                                                                                                                                                                                                                                                      break label201643;
                                                                                                                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                                                                                                                } catch (Throwable var18292) {
                                                                                                                                                                                                                                                                                                                                                   var10000 = var18292;
                                                                                                                                                                                                                                                                                                                                                   var10001 = false;
                                                                                                                                                                                                                                                                                                                                                   break label201632;
                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                var4 = 30;
                                                                                                                                                                                                                                                                                                                                                break label201613;
                                                                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                                                                             try {
                                                                                                                                                                                                                                                                                                                                                if (!var1.equals("F3116")) {
                                                                                                                                                                                                                                                                                                                                                   break label201643;
                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                             } catch (Throwable var18290) {
                                                                                                                                                                                                                                                                                                                                                var10000 = var18290;
                                                                                                                                                                                                                                                                                                                                                var10001 = false;
                                                                                                                                                                                                                                                                                                                                                break label201632;
                                                                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                                                                             var4 = 32;
                                                                                                                                                                                                                                                                                                                                             break label201613;
                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                          try {
                                                                                                                                                                                                                                                                                                                                             if (!var1.equals("F3113")) {
                                                                                                                                                                                                                                                                                                                                                break label201643;
                                                                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                                                                          } catch (Throwable var18291) {
                                                                                                                                                                                                                                                                                                                                             var10000 = var18291;
                                                                                                                                                                                                                                                                                                                                             var10001 = false;
                                                                                                                                                                                                                                                                                                                                             break label201632;
                                                                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                                                                          var4 = 31;
                                                                                                                                                                                                                                                                                                                                          break label201613;
                                                                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                                                                       try {
                                                                                                                                                                                                                                                                                                                                          if (!var1.equals("F3211")) {
                                                                                                                                                                                                                                                                                                                                             break label201643;
                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                       } catch (Throwable var18289) {
                                                                                                                                                                                                                                                                                                                                          var10000 = var18289;
                                                                                                                                                                                                                                                                                                                                          var10001 = false;
                                                                                                                                                                                                                                                                                                                                          break label201632;
                                                                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                                                                       var4 = 33;
                                                                                                                                                                                                                                                                                                                                       break label201613;
                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                                       if (!var1.equals("F3213")) {
                                                                                                                                                                                                                                                                                                                                          break label201643;
                                                                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                                                                    } catch (Throwable var18288) {
                                                                                                                                                                                                                                                                                                                                       var10000 = var18288;
                                                                                                                                                                                                                                                                                                                                       var10001 = false;
                                                                                                                                                                                                                                                                                                                                       break label201632;
                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                    var4 = 34;
                                                                                                                                                                                                                                                                                                                                    break label201613;
                                                                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                                                                 try {
                                                                                                                                                                                                                                                                                                                                    if (!var1.equals("F3311")) {
                                                                                                                                                                                                                                                                                                                                       break label201643;
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                 } catch (Throwable var18286) {
                                                                                                                                                                                                                                                                                                                                    var10000 = var18286;
                                                                                                                                                                                                                                                                                                                                    var10001 = false;
                                                                                                                                                                                                                                                                                                                                    break label201632;
                                                                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                                                                 var4 = 36;
                                                                                                                                                                                                                                                                                                                                 break label201613;
                                                                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                                                                              try {
                                                                                                                                                                                                                                                                                                                                 if (!var1.equals("F3215")) {
                                                                                                                                                                                                                                                                                                                                    break label201643;
                                                                                                                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                                                                                                              } catch (Throwable var18287) {
                                                                                                                                                                                                                                                                                                                                 var10000 = var18287;
                                                                                                                                                                                                                                                                                                                                 var10001 = false;
                                                                                                                                                                                                                                                                                                                                 break label201632;
                                                                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                                                                              var4 = 35;
                                                                                                                                                                                                                                                                                                                              break label201613;
                                                                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                                                                           try {
                                                                                                                                                                                                                                                                                                                              if (!var1.equals("PRO7S")) {
                                                                                                                                                                                                                                                                                                                                 break label201643;
                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                           } catch (Throwable var18285) {
                                                                                                                                                                                                                                                                                                                              var10000 = var18285;
                                                                                                                                                                                                                                                                                                                              var10001 = false;
                                                                                                                                                                                                                                                                                                                              break label201632;
                                                                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                                                                           var4 = 93;
                                                                                                                                                                                                                                                                                                                           break label201613;
                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                           if (!var1.equals("Q4260")) {
                                                                                                                                                                                                                                                                                                                              break label201643;
                                                                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                                                                        } catch (Throwable var18284) {
                                                                                                                                                                                                                                                                                                                           var10000 = var18284;
                                                                                                                                                                                                                                                                                                                           var10001 = false;
                                                                                                                                                                                                                                                                                                                           break label201632;
                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                        var4 = 95;
                                                                                                                                                                                                                                                                                                                        break label201613;
                                                                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                                                                     try {
                                                                                                                                                                                                                                                                                                                        if (!var1.equals("V23GB")) {
                                                                                                                                                                                                                                                                                                                           break label201643;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                     } catch (Throwable var18282) {
                                                                                                                                                                                                                                                                                                                        var10000 = var18282;
                                                                                                                                                                                                                                                                                                                        var10001 = false;
                                                                                                                                                                                                                                                                                                                        break label201632;
                                                                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                                                                     var4 = 112;
                                                                                                                                                                                                                                                                                                                     break label201613;
                                                                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                                                                                                                     if (!var1.equals("Q4310")) {
                                                                                                                                                                                                                                                                                                                        break label201643;
                                                                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                                                                  } catch (Throwable var18283) {
                                                                                                                                                                                                                                                                                                                     var10000 = var18283;
                                                                                                                                                                                                                                                                                                                     var10001 = false;
                                                                                                                                                                                                                                                                                                                     break label201632;
                                                                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                                                                  var4 = 97;
                                                                                                                                                                                                                                                                                                                  break label201613;
                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                               try {
                                                                                                                                                                                                                                                                                                                  if (!var1.equals("X3_HK")) {
                                                                                                                                                                                                                                                                                                                     break label201643;
                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                               } catch (Throwable var18281) {
                                                                                                                                                                                                                                                                                                                  var10000 = var18281;
                                                                                                                                                                                                                                                                                                                  var10001 = false;
                                                                                                                                                                                                                                                                                                                  break label201632;
                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                               var4 = 119;
                                                                                                                                                                                                                                                                                                               break label201613;
                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                               if (!var1.equals("i9031")) {
                                                                                                                                                                                                                                                                                                                  break label201643;
                                                                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                                                                            } catch (Throwable var18280) {
                                                                                                                                                                                                                                                                                                               var10000 = var18280;
                                                                                                                                                                                                                                                                                                               var10001 = false;
                                                                                                                                                                                                                                                                                                               break label201632;
                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                            var4 = 55;
                                                                                                                                                                                                                                                                                                            break label201613;
                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                         try {
                                                                                                                                                                                                                                                                                                            if (!var1.equals("A2016a40")) {
                                                                                                                                                                                                                                                                                                               break label201643;
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                         } catch (Throwable var18278) {
                                                                                                                                                                                                                                                                                                            var10000 = var18278;
                                                                                                                                                                                                                                                                                                            var10001 = false;
                                                                                                                                                                                                                                                                                                            break label201632;
                                                                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                                                                         var4 = 5;
                                                                                                                                                                                                                                                                                                         break label201613;
                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                      try {
                                                                                                                                                                                                                                                                                                         if (!var1.equals("le_x6")) {
                                                                                                                                                                                                                                                                                                            break label201643;
                                                                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                                                                      } catch (Throwable var18279) {
                                                                                                                                                                                                                                                                                                         var10000 = var18279;
                                                                                                                                                                                                                                                                                                         var10001 = false;
                                                                                                                                                                                                                                                                                                         break label201632;
                                                                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                                                                      var4 = 64;
                                                                                                                                                                                                                                                                                                      break label201613;
                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                   try {
                                                                                                                                                                                                                                                                                                      if (!var1.equals("CPY83_I00")) {
                                                                                                                                                                                                                                                                                                         break label201643;
                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                   } catch (Throwable var18277) {
                                                                                                                                                                                                                                                                                                      var10000 = var18277;
                                                                                                                                                                                                                                                                                                      var10001 = false;
                                                                                                                                                                                                                                                                                                      break label201632;
                                                                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                                                                   var4 = 20;
                                                                                                                                                                                                                                                                                                   break label201613;
                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                   if (!var1.equals("K50a40")) {
                                                                                                                                                                                                                                                                                                      break label201643;
                                                                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                                                                } catch (Throwable var18361) {
                                                                                                                                                                                                                                                                                                   var10000 = var18361;
                                                                                                                                                                                                                                                                                                   var10001 = false;
                                                                                                                                                                                                                                                                                                   break label201632;
                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                var4 = 62;
                                                                                                                                                                                                                                                                                                break label201613;
                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                             try {
                                                                                                                                                                                                                                                                                                if (!var1.equals("marino_f")) {
                                                                                                                                                                                                                                                                                                   break label201643;
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                             } catch (Throwable var18276) {
                                                                                                                                                                                                                                                                                                var10000 = var18276;
                                                                                                                                                                                                                                                                                                var10001 = false;
                                                                                                                                                                                                                                                                                                break label201632;
                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                             var4 = 68;
                                                                                                                                                                                                                                                                                             break label201613;
                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                          try {
                                                                                                                                                                                                                                                                                             if (!var1.equals("A7010a48")) {
                                                                                                                                                                                                                                                                                                break label201643;
                                                                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                                                                          } catch (Throwable var18274) {
                                                                                                                                                                                                                                                                                             var10000 = var18274;
                                                                                                                                                                                                                                                                                             var10001 = false;
                                                                                                                                                                                                                                                                                             break label201632;
                                                                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                                                                          var4 = 8;
                                                                                                                                                                                                                                                                                          break label201613;
                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                       try {
                                                                                                                                                                                                                                                                                          if (!var1.equals("CP8676_I02")) {
                                                                                                                                                                                                                                                                                             break label201643;
                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                       } catch (Throwable var18360) {
                                                                                                                                                                                                                                                                                          var10000 = var18360;
                                                                                                                                                                                                                                                                                          var10001 = false;
                                                                                                                                                                                                                                                                                          break label201632;
                                                                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                                                                       var4 = 18;
                                                                                                                                                                                                                                                                                       break label201613;
                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                       if (!var1.equals("griffin")) {
                                                                                                                                                                                                                                                                                          break label201643;
                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                    } catch (Throwable var18275) {
                                                                                                                                                                                                                                                                                       var10000 = var18275;
                                                                                                                                                                                                                                                                                       var10001 = false;
                                                                                                                                                                                                                                                                                       break label201632;
                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                    var4 = 48;
                                                                                                                                                                                                                                                                                    break label201613;
                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                 try {
                                                                                                                                                                                                                                                                                    if (!var1.equals("NX573J")) {
                                                                                                                                                                                                                                                                                       break label201643;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                 } catch (Throwable var18358) {
                                                                                                                                                                                                                                                                                    var10000 = var18358;
                                                                                                                                                                                                                                                                                    var10001 = false;
                                                                                                                                                                                                                                                                                    break label201632;
                                                                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                                                                 var4 = 76;
                                                                                                                                                                                                                                                                                 break label201613;
                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                              try {
                                                                                                                                                                                                                                                                                 if (!var1.equals("A7020a48")) {
                                                                                                                                                                                                                                                                                    break label201643;
                                                                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                                                                              } catch (Throwable var18273) {
                                                                                                                                                                                                                                                                                 var10000 = var18273;
                                                                                                                                                                                                                                                                                 var10001 = false;
                                                                                                                                                                                                                                                                                 break label201632;
                                                                                                                                                                                                                                                                              }

                                                                                                                                                                                                                                                                              var4 = 9;
                                                                                                                                                                                                                                                                              break label201613;
                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                           try {
                                                                                                                                                                                                                                                                              if (!var1.equals("NX541J")) {
                                                                                                                                                                                                                                                                                 break label201643;
                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                           } catch (Throwable var18359) {
                                                                                                                                                                                                                                                                              var10000 = var18359;
                                                                                                                                                                                                                                                                              var10001 = false;
                                                                                                                                                                                                                                                                              break label201632;
                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                           var4 = 75;
                                                                                                                                                                                                                                                                           break label201613;
                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                           if (!var1.equals("PGN528")) {
                                                                                                                                                                                                                                                                              break label201643;
                                                                                                                                                                                                                                                                           }
                                                                                                                                                                                                                                                                        } catch (Throwable var18357) {
                                                                                                                                                                                                                                                                           var10000 = var18357;
                                                                                                                                                                                                                                                                           var10001 = false;
                                                                                                                                                                                                                                                                           break label201632;
                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                        var4 = 86;
                                                                                                                                                                                                                                                                        break label201613;
                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                     try {
                                                                                                                                                                                                                                                                        if (!var1.equals("TB3-730F")) {
                                                                                                                                                                                                                                                                           break label201643;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                     } catch (Throwable var18272) {
                                                                                                                                                                                                                                                                        var10000 = var18272;
                                                                                                                                                                                                                                                                        var10001 = false;
                                                                                                                                                                                                                                                                        break label201632;
                                                                                                                                                                                                                                                                     }

                                                                                                                                                                                                                                                                     var4 = 106;
                                                                                                                                                                                                                                                                     break label201613;
                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                                                                     if (!var1.equals("TB3-850F")) {
                                                                                                                                                                                                                                                                        break label201643;
                                                                                                                                                                                                                                                                     }
                                                                                                                                                                                                                                                                  } catch (Throwable var18270) {
                                                                                                                                                                                                                                                                     var10000 = var18270;
                                                                                                                                                                                                                                                                     var10001 = false;
                                                                                                                                                                                                                                                                     break label201632;
                                                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                                                                  var4 = 108;
                                                                                                                                                                                                                                                                  break label201613;
                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                               try {
                                                                                                                                                                                                                                                                  if (!var1.equals("PGN610")) {
                                                                                                                                                                                                                                                                     break label201643;
                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                               } catch (Throwable var18356) {
                                                                                                                                                                                                                                                                  var10000 = var18356;
                                                                                                                                                                                                                                                                  var10001 = false;
                                                                                                                                                                                                                                                                  break label201632;
                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                               var4 = 87;
                                                                                                                                                                                                                                                               break label201613;
                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                               if (!var1.equals("TB3-730X")) {
                                                                                                                                                                                                                                                                  break label201643;
                                                                                                                                                                                                                                                               }
                                                                                                                                                                                                                                                            } catch (Throwable var18271) {
                                                                                                                                                                                                                                                               var10000 = var18271;
                                                                                                                                                                                                                                                               var10001 = false;
                                                                                                                                                                                                                                                               break label201632;
                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                            var4 = 107;
                                                                                                                                                                                                                                                            break label201613;
                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                         try {
                                                                                                                                                                                                                                                            if (!var1.equals("AquaPowerM")) {
                                                                                                                                                                                                                                                               break label201643;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                         } catch (Throwable var18354) {
                                                                                                                                                                                                                                                            var10000 = var18354;
                                                                                                                                                                                                                                                            var10001 = false;
                                                                                                                                                                                                                                                            break label201632;
                                                                                                                                                                                                                                                         }

                                                                                                                                                                                                                                                         var4 = 10;
                                                                                                                                                                                                                                                         break label201613;
                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                      try {
                                                                                                                                                                                                                                                         if (!var1.equals("TB3-850M")) {
                                                                                                                                                                                                                                                            break label201643;
                                                                                                                                                                                                                                                         }
                                                                                                                                                                                                                                                      } catch (Throwable var18269) {
                                                                                                                                                                                                                                                         var10000 = var18269;
                                                                                                                                                                                                                                                         var10001 = false;
                                                                                                                                                                                                                                                         break label201632;
                                                                                                                                                                                                                                                      }

                                                                                                                                                                                                                                                      var4 = 109;
                                                                                                                                                                                                                                                      break label201613;
                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                   try {
                                                                                                                                                                                                                                                      if (!var1.equals("PGN611")) {
                                                                                                                                                                                                                                                         break label201643;
                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                   } catch (Throwable var18355) {
                                                                                                                                                                                                                                                      var10000 = var18355;
                                                                                                                                                                                                                                                      var10001 = false;
                                                                                                                                                                                                                                                      break label201632;
                                                                                                                                                                                                                                                   }

                                                                                                                                                                                                                                                   var4 = 88;
                                                                                                                                                                                                                                                   break label201613;
                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                   if (!var1.equals("XT1663")) {
                                                                                                                                                                                                                                                      break label201643;
                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                } catch (Throwable var18353) {
                                                                                                                                                                                                                                                   var10000 = var18353;
                                                                                                                                                                                                                                                   var10001 = false;
                                                                                                                                                                                                                                                   break label201632;
                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                var4 = 121;
                                                                                                                                                                                                                                                break label201613;
                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                             try {
                                                                                                                                                                                                                                                if (!var1.equals("Pixi5-10_4G")) {
                                                                                                                                                                                                                                                   break label201643;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                             } catch (Throwable var18268) {
                                                                                                                                                                                                                                                var10000 = var18268;
                                                                                                                                                                                                                                                var10001 = false;
                                                                                                                                                                                                                                                break label201632;
                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                             var4 = 91;
                                                                                                                                                                                                                                             break label201613;
                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                          try {
                                                                                                                                                                                                                                             if (!var1.equals("GIONEE_WBL5708")) {
                                                                                                                                                                                                                                                break label201643;
                                                                                                                                                                                                                                             }
                                                                                                                                                                                                                                          } catch (Throwable var18266) {
                                                                                                                                                                                                                                             var10000 = var18266;
                                                                                                                                                                                                                                             var10001 = false;
                                                                                                                                                                                                                                             break label201632;
                                                                                                                                                                                                                                          }

                                                                                                                                                                                                                                          var4 = 45;
                                                                                                                                                                                                                                          break label201613;
                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                       try {
                                                                                                                                                                                                                                          if (!var1.equals("ComioS1")) {
                                                                                                                                                                                                                                             break label201643;
                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                       } catch (Throwable var18352) {
                                                                                                                                                                                                                                          var10000 = var18352;
                                                                                                                                                                                                                                          var10001 = false;
                                                                                                                                                                                                                                          break label201632;
                                                                                                                                                                                                                                       }

                                                                                                                                                                                                                                       var4 = 17;
                                                                                                                                                                                                                                       break label201613;
                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                       if (!var1.equals("QM16XE_U")) {
                                                                                                                                                                                                                                          break label201643;
                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                    } catch (Throwable var18267) {
                                                                                                                                                                                                                                       var10000 = var18267;
                                                                                                                                                                                                                                       var10001 = false;
                                                                                                                                                                                                                                       break label201632;
                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                    var4 = 99;
                                                                                                                                                                                                                                    break label201613;
                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                 try {
                                                                                                                                                                                                                                    if (!var1.equals("vernee_M5")) {
                                                                                                                                                                                                                                       break label201643;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                 } catch (Throwable var18350) {
                                                                                                                                                                                                                                    var10000 = var18350;
                                                                                                                                                                                                                                    var10001 = false;
                                                                                                                                                                                                                                    break label201632;
                                                                                                                                                                                                                                 }

                                                                                                                                                                                                                                 var4 = 114;
                                                                                                                                                                                                                                 break label201613;
                                                                                                                                                                                                                              }

                                                                                                                                                                                                                              try {
                                                                                                                                                                                                                                 if (!var1.equals("GIONEE_WBL7365")) {
                                                                                                                                                                                                                                    break label201643;
                                                                                                                                                                                                                                 }
                                                                                                                                                                                                                              } catch (Throwable var18265) {
                                                                                                                                                                                                                                 var10000 = var18265;
                                                                                                                                                                                                                                 var10001 = false;
                                                                                                                                                                                                                                 break label201632;
                                                                                                                                                                                                                              }

                                                                                                                                                                                                                              var4 = 46;
                                                                                                                                                                                                                              break label201613;
                                                                                                                                                                                                                           }

                                                                                                                                                                                                                           try {
                                                                                                                                                                                                                              if (!var1.equals("Phantom6")) {
                                                                                                                                                                                                                                 break label201643;
                                                                                                                                                                                                                              }
                                                                                                                                                                                                                           } catch (Throwable var18351) {
                                                                                                                                                                                                                              var10000 = var18351;
                                                                                                                                                                                                                              var10001 = false;
                                                                                                                                                                                                                              break label201632;
                                                                                                                                                                                                                           }

                                                                                                                                                                                                                           var4 = 89;
                                                                                                                                                                                                                           break label201613;
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                           if (!var1.equals("panell_dl")) {
                                                                                                                                                                                                                              break label201643;
                                                                                                                                                                                                                           }
                                                                                                                                                                                                                        } catch (Throwable var18349) {
                                                                                                                                                                                                                           var10000 = var18349;
                                                                                                                                                                                                                           var10001 = false;
                                                                                                                                                                                                                           break label201632;
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                        var4 = 82;
                                                                                                                                                                                                                        break label201613;
                                                                                                                                                                                                                     }

                                                                                                                                                                                                                     try {
                                                                                                                                                                                                                        if (!var1.equals("GIONEE_WBL7519")) {
                                                                                                                                                                                                                           break label201643;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                     } catch (Throwable var18264) {
                                                                                                                                                                                                                        var10000 = var18264;
                                                                                                                                                                                                                        var10001 = false;
                                                                                                                                                                                                                        break label201632;
                                                                                                                                                                                                                     }

                                                                                                                                                                                                                     var4 = 47;
                                                                                                                                                                                                                     break label201613;
                                                                                                                                                                                                                  }

                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                     if (!var1.equals("A7000plus")) {
                                                                                                                                                                                                                        break label201643;
                                                                                                                                                                                                                     }
                                                                                                                                                                                                                  } catch (Throwable var18262) {
                                                                                                                                                                                                                     var10000 = var18262;
                                                                                                                                                                                                                     var10001 = false;
                                                                                                                                                                                                                     break label201632;
                                                                                                                                                                                                                  }

                                                                                                                                                                                                                  var4 = 7;
                                                                                                                                                                                                                  break label201613;
                                                                                                                                                                                                               }

                                                                                                                                                                                                               try {
                                                                                                                                                                                                                  if (!var1.equals("panell_ds")) {
                                                                                                                                                                                                                     break label201643;
                                                                                                                                                                                                                  }
                                                                                                                                                                                                               } catch (Throwable var18348) {
                                                                                                                                                                                                                  var10000 = var18348;
                                                                                                                                                                                                                  var10001 = false;
                                                                                                                                                                                                                  break label201632;
                                                                                                                                                                                                               }

                                                                                                                                                                                                               var4 = 83;
                                                                                                                                                                                                               break label201613;
                                                                                                                                                                                                            }

                                                                                                                                                                                                            try {
                                                                                                                                                                                                               if (!var1.equals("manning")) {
                                                                                                                                                                                                                  break label201643;
                                                                                                                                                                                                               }
                                                                                                                                                                                                            } catch (Throwable var18263) {
                                                                                                                                                                                                               var10000 = var18263;
                                                                                                                                                                                                               var10001 = false;
                                                                                                                                                                                                               break label201632;
                                                                                                                                                                                                            }

                                                                                                                                                                                                            var4 = 67;
                                                                                                                                                                                                            break label201613;
                                                                                                                                                                                                         }

                                                                                                                                                                                                         try {
                                                                                                                                                                                                            if (!var1.equals("GiONEE_GBL7319")) {
                                                                                                                                                                                                               break label201643;
                                                                                                                                                                                                            }
                                                                                                                                                                                                         } catch (Throwable var18346) {
                                                                                                                                                                                                            var10000 = var18346;
                                                                                                                                                                                                            var10001 = false;
                                                                                                                                                                                                            break label201632;
                                                                                                                                                                                                         }

                                                                                                                                                                                                         var4 = 40;
                                                                                                                                                                                                         break label201613;
                                                                                                                                                                                                      }

                                                                                                                                                                                                      try {
                                                                                                                                                                                                         if (!var1.equals("j2xlteins")) {
                                                                                                                                                                                                            break label201643;
                                                                                                                                                                                                         }
                                                                                                                                                                                                      } catch (Throwable var18261) {
                                                                                                                                                                                                         var10000 = var18261;
                                                                                                                                                                                                         var10001 = false;
                                                                                                                                                                                                         break label201632;
                                                                                                                                                                                                      }

                                                                                                                                                                                                      var4 = 60;
                                                                                                                                                                                                      break label201613;
                                                                                                                                                                                                   }

                                                                                                                                                                                                   try {
                                                                                                                                                                                                      if (!var1.equals("panell_dt")) {
                                                                                                                                                                                                         break label201643;
                                                                                                                                                                                                      }
                                                                                                                                                                                                   } catch (Throwable var18347) {
                                                                                                                                                                                                      var10000 = var18347;
                                                                                                                                                                                                      var10001 = false;
                                                                                                                                                                                                      break label201632;
                                                                                                                                                                                                   }

                                                                                                                                                                                                   var4 = 84;
                                                                                                                                                                                                   break label201613;
                                                                                                                                                                                                }

                                                                                                                                                                                                try {
                                                                                                                                                                                                   if (!var1.equals("BRAVIA_ATV2")) {
                                                                                                                                                                                                      break label201643;
                                                                                                                                                                                                   }
                                                                                                                                                                                                } catch (Throwable var18345) {
                                                                                                                                                                                                   var10000 = var18345;
                                                                                                                                                                                                   var10001 = false;
                                                                                                                                                                                                   break label201632;
                                                                                                                                                                                                }

                                                                                                                                                                                                var4 = 14;
                                                                                                                                                                                                break label201613;
                                                                                                                                                                                             }

                                                                                                                                                                                             try {
                                                                                                                                                                                                if (!var1.equals("panell_d")) {
                                                                                                                                                                                                   break label201643;
                                                                                                                                                                                                }
                                                                                                                                                                                             } catch (Throwable var18260) {
                                                                                                                                                                                                var10000 = var18260;
                                                                                                                                                                                                var10001 = false;
                                                                                                                                                                                                break label201632;
                                                                                                                                                                                             }

                                                                                                                                                                                             var4 = 81;
                                                                                                                                                                                             break label201613;
                                                                                                                                                                                          }

                                                                                                                                                                                          try {
                                                                                                                                                                                             if (!var1.equals("itel_S41")) {
                                                                                                                                                                                                break label201643;
                                                                                                                                                                                             }
                                                                                                                                                                                          } catch (Throwable var18258) {
                                                                                                                                                                                             var10000 = var18258;
                                                                                                                                                                                             var10001 = false;
                                                                                                                                                                                             break label201632;
                                                                                                                                                                                          }

                                                                                                                                                                                          var4 = 59;
                                                                                                                                                                                          break label201613;
                                                                                                                                                                                       }

                                                                                                                                                                                       try {
                                                                                                                                                                                          if (!var1.equals("iris60")) {
                                                                                                                                                                                             break label201643;
                                                                                                                                                                                          }
                                                                                                                                                                                       } catch (Throwable var18344) {
                                                                                                                                                                                          var10000 = var18344;
                                                                                                                                                                                          var10001 = false;
                                                                                                                                                                                          break label201632;
                                                                                                                                                                                       }

                                                                                                                                                                                       var4 = 58;
                                                                                                                                                                                       break label201613;
                                                                                                                                                                                    }

                                                                                                                                                                                    try {
                                                                                                                                                                                       if (!var1.equals("LS-5017")) {
                                                                                                                                                                                          break label201643;
                                                                                                                                                                                       }
                                                                                                                                                                                    } catch (Throwable var18259) {
                                                                                                                                                                                       var10000 = var18259;
                                                                                                                                                                                       var10001 = false;
                                                                                                                                                                                       break label201632;
                                                                                                                                                                                    }

                                                                                                                                                                                    var4 = 65;
                                                                                                                                                                                    break label201613;
                                                                                                                                                                                 }

                                                                                                                                                                                 try {
                                                                                                                                                                                    if (!var1.equals("namath")) {
                                                                                                                                                                                       break label201643;
                                                                                                                                                                                    }
                                                                                                                                                                                 } catch (Throwable var18342) {
                                                                                                                                                                                    var10000 = var18342;
                                                                                                                                                                                    var10001 = false;
                                                                                                                                                                                    break label201632;
                                                                                                                                                                                 }

                                                                                                                                                                                 var4 = 73;
                                                                                                                                                                                 break label201613;
                                                                                                                                                                              }

                                                                                                                                                                              try {
                                                                                                                                                                                 if (!var1.equals("hwALE-H")) {
                                                                                                                                                                                    break label201643;
                                                                                                                                                                                 }
                                                                                                                                                                              } catch (Throwable var18257) {
                                                                                                                                                                                 var10000 = var18257;
                                                                                                                                                                                 var10001 = false;
                                                                                                                                                                                 break label201632;
                                                                                                                                                                              }

                                                                                                                                                                              var4 = 50;
                                                                                                                                                                              break label201613;
                                                                                                                                                                           }

                                                                                                                                                                           try {
                                                                                                                                                                              if (!var1.equals("Slate_Pro")) {
                                                                                                                                                                                 break label201643;
                                                                                                                                                                              }
                                                                                                                                                                           } catch (Throwable var18343) {
                                                                                                                                                                              var10000 = var18343;
                                                                                                                                                                              var10001 = false;
                                                                                                                                                                              break label201632;
                                                                                                                                                                           }

                                                                                                                                                                           var4 = 102;
                                                                                                                                                                           break label201613;
                                                                                                                                                                        }

                                                                                                                                                                        try {
                                                                                                                                                                           if (!var1.equals("A10-70F")) {
                                                                                                                                                                              break label201643;
                                                                                                                                                                           }
                                                                                                                                                                        } catch (Throwable var18341) {
                                                                                                                                                                           var10000 = var18341;
                                                                                                                                                                           var10001 = false;
                                                                                                                                                                           break label201632;
                                                                                                                                                                        }

                                                                                                                                                                        var4 = 3;
                                                                                                                                                                        break label201613;
                                                                                                                                                                     }

                                                                                                                                                                     try {
                                                                                                                                                                        if (!var1.equals("EverStar_S")) {
                                                                                                                                                                           break label201643;
                                                                                                                                                                        }
                                                                                                                                                                     } catch (Throwable var18256) {
                                                                                                                                                                        var10000 = var18256;
                                                                                                                                                                        var10001 = false;
                                                                                                                                                                        break label201632;
                                                                                                                                                                     }

                                                                                                                                                                     var4 = 29;
                                                                                                                                                                     break label201613;
                                                                                                                                                                  }

                                                                                                                                                                  try {
                                                                                                                                                                     if (!var1.equals("woods_f")) {
                                                                                                                                                                        break label201643;
                                                                                                                                                                     }
                                                                                                                                                                  } catch (Throwable var18254) {
                                                                                                                                                                     var10000 = var18254;
                                                                                                                                                                     var10001 = false;
                                                                                                                                                                     break label201632;
                                                                                                                                                                  }

                                                                                                                                                                  var4 = 117;
                                                                                                                                                                  break label201613;
                                                                                                                                                               }

                                                                                                                                                               try {
                                                                                                                                                                  if (!var1.equals("s905x018")) {
                                                                                                                                                                     break label201643;
                                                                                                                                                                  }
                                                                                                                                                               } catch (Throwable var18340) {
                                                                                                                                                                  var10000 = var18340;
                                                                                                                                                                  var10001 = false;
                                                                                                                                                                  break label201632;
                                                                                                                                                               }

                                                                                                                                                               var4 = 104;
                                                                                                                                                               break label201613;
                                                                                                                                                            }

                                                                                                                                                            try {
                                                                                                                                                               if (!var1.equals("htc_e56ml_dtul")) {
                                                                                                                                                                  break label201643;
                                                                                                                                                               }
                                                                                                                                                            } catch (Throwable var18255) {
                                                                                                                                                               var10000 = var18255;
                                                                                                                                                               var10001 = false;
                                                                                                                                                               break label201632;
                                                                                                                                                            }

                                                                                                                                                            var4 = 49;
                                                                                                                                                            break label201613;
                                                                                                                                                         }

                                                                                                                                                         try {
                                                                                                                                                            if (!var1.equals("tcl_eu")) {
                                                                                                                                                               break label201643;
                                                                                                                                                            }
                                                                                                                                                         } catch (Throwable var18338) {
                                                                                                                                                            var10000 = var18338;
                                                                                                                                                            var10001 = false;
                                                                                                                                                            break label201632;
                                                                                                                                                         }

                                                                                                                                                         var4 = 110;
                                                                                                                                                         break label201613;
                                                                                                                                                      }

                                                                                                                                                      try {
                                                                                                                                                         if (!var1.equals("CPH1609")) {
                                                                                                                                                            break label201643;
                                                                                                                                                         }
                                                                                                                                                      } catch (Throwable var18253) {
                                                                                                                                                         var10000 = var18253;
                                                                                                                                                         var10001 = false;
                                                                                                                                                         break label201632;
                                                                                                                                                      }

                                                                                                                                                      var4 = 19;
                                                                                                                                                      break label201613;
                                                                                                                                                   }

                                                                                                                                                   try {
                                                                                                                                                      if (!var1.equals("ELUGA_Ray_X")) {
                                                                                                                                                         break label201643;
                                                                                                                                                      }
                                                                                                                                                   } catch (Throwable var18339) {
                                                                                                                                                      var10000 = var18339;
                                                                                                                                                      var10001 = false;
                                                                                                                                                      break label201632;
                                                                                                                                                   }

                                                                                                                                                   var4 = 28;
                                                                                                                                                   break label201613;
                                                                                                                                                }

                                                                                                                                                try {
                                                                                                                                                   if (!var1.equals("nicklaus_f")) {
                                                                                                                                                      break label201643;
                                                                                                                                                   }
                                                                                                                                                } catch (Throwable var18337) {
                                                                                                                                                   var10000 = var18337;
                                                                                                                                                   var10001 = false;
                                                                                                                                                   break label201632;
                                                                                                                                                }

                                                                                                                                                var4 = 74;
                                                                                                                                                break label201613;
                                                                                                                                             }

                                                                                                                                             try {
                                                                                                                                                if (!var1.equals("iball8735_9806")) {
                                                                                                                                                   break label201643;
                                                                                                                                                }
                                                                                                                                             } catch (Throwable var18252) {
                                                                                                                                                var10000 = var18252;
                                                                                                                                                var10001 = false;
                                                                                                                                                break label201632;
                                                                                                                                             }

                                                                                                                                             var4 = 56;
                                                                                                                                             break label201613;
                                                                                                                                          }

                                                                                                                                          try {
                                                                                                                                             if (!var1.equals("PB2-670M")) {
                                                                                                                                                break label201643;
                                                                                                                                             }
                                                                                                                                          } catch (Throwable var18250) {
                                                                                                                                             var10000 = var18250;
                                                                                                                                             var10001 = false;
                                                                                                                                             break label201632;
                                                                                                                                          }

                                                                                                                                          var4 = 85;
                                                                                                                                          break label201613;
                                                                                                                                       }

                                                                                                                                       try {
                                                                                                                                          if (!var1.equals("A7000-a")) {
                                                                                                                                             break label201643;
                                                                                                                                          }
                                                                                                                                       } catch (Throwable var18336) {
                                                                                                                                          var10000 = var18336;
                                                                                                                                          var10001 = false;
                                                                                                                                          break label201632;
                                                                                                                                       }

                                                                                                                                       var4 = 6;
                                                                                                                                       break label201613;
                                                                                                                                    }

                                                                                                                                    try {
                                                                                                                                       if (!var1.equals("santoni")) {
                                                                                                                                          break label201643;
                                                                                                                                       }
                                                                                                                                    } catch (Throwable var18251) {
                                                                                                                                       var10000 = var18251;
                                                                                                                                       var10001 = false;
                                                                                                                                       break label201632;
                                                                                                                                    }

                                                                                                                                    var4 = 101;
                                                                                                                                    break label201613;
                                                                                                                                 }

                                                                                                                                 try {
                                                                                                                                    if (!var1.equals("watson")) {
                                                                                                                                       break label201643;
                                                                                                                                    }
                                                                                                                                 } catch (Throwable var18334) {
                                                                                                                                    var10000 = var18334;
                                                                                                                                    var10001 = false;
                                                                                                                                    break label201632;
                                                                                                                                 }

                                                                                                                                 var4 = 115;
                                                                                                                                 break label201613;
                                                                                                                              }

                                                                                                                              try {
                                                                                                                                 if (!var1.equals("Infinix-X572")) {
                                                                                                                                    break label201643;
                                                                                                                                 }
                                                                                                                              } catch (Throwable var18249) {
                                                                                                                                 var10000 = var18249;
                                                                                                                                 var10001 = false;
                                                                                                                                 break label201632;
                                                                                                                              }

                                                                                                                              var4 = 57;
                                                                                                                              break label201613;
                                                                                                                           }

                                                                                                                           try {
                                                                                                                              if (!var1.equals("SVP-DTV15")) {
                                                                                                                                 break label201643;
                                                                                                                              }
                                                                                                                           } catch (Throwable var18335) {
                                                                                                                              var10000 = var18335;
                                                                                                                              var10001 = false;
                                                                                                                              break label201632;
                                                                                                                           }

                                                                                                                           var4 = 103;
                                                                                                                           break label201613;
                                                                                                                        }

                                                                                                                        try {
                                                                                                                           if (!var1.equals("whyred")) {
                                                                                                                              break label201643;
                                                                                                                           }
                                                                                                                        } catch (Throwable var18333) {
                                                                                                                           var10000 = var18333;
                                                                                                                           var10001 = false;
                                                                                                                           break label201632;
                                                                                                                        }

                                                                                                                        var4 = 116;
                                                                                                                        break label201613;
                                                                                                                     }

                                                                                                                     try {
                                                                                                                        if (!var1.equals("BRAVIA_ATV3_4K")) {
                                                                                                                           break label201643;
                                                                                                                        }
                                                                                                                     } catch (Throwable var18248) {
                                                                                                                        var10000 = var18248;
                                                                                                                        var10001 = false;
                                                                                                                        break label201632;
                                                                                                                     }

                                                                                                                     var4 = 15;
                                                                                                                     break label201613;
                                                                                                                  }

                                                                                                                  try {
                                                                                                                     if (!var1.equals("HWCAM-H")) {
                                                                                                                        break label201643;
                                                                                                                     }
                                                                                                                  } catch (Throwable var18246) {
                                                                                                                     var10000 = var18246;
                                                                                                                     var10001 = false;
                                                                                                                     break label201632;
                                                                                                                  }

                                                                                                                  var4 = 52;
                                                                                                                  break label201613;
                                                                                                               }

                                                                                                               try {
                                                                                                                  if (!var1.equals("OnePlus5T")) {
                                                                                                                     break label201643;
                                                                                                                  }
                                                                                                               } catch (Throwable var18332) {
                                                                                                                  var10000 = var18332;
                                                                                                                  var10001 = false;
                                                                                                                  break label201632;
                                                                                                               }

                                                                                                               var4 = 77;
                                                                                                               break label201613;
                                                                                                            }

                                                                                                            try {
                                                                                                               if (!var1.equals("HWBLN-H")) {
                                                                                                                  break label201643;
                                                                                                               }
                                                                                                            } catch (Throwable var18247) {
                                                                                                               var10000 = var18247;
                                                                                                               var10001 = false;
                                                                                                               break label201632;
                                                                                                            }

                                                                                                            var4 = 51;
                                                                                                            break label201613;
                                                                                                         }

                                                                                                         try {
                                                                                                            if (!var1.equals("GIONEE_GBL7360")) {
                                                                                                               break label201643;
                                                                                                            }
                                                                                                         } catch (Throwable var18330) {
                                                                                                            var10000 = var18330;
                                                                                                            var10001 = false;
                                                                                                            break label201632;
                                                                                                         }

                                                                                                         var4 = 41;
                                                                                                         break label201613;
                                                                                                      }

                                                                                                      try {
                                                                                                         if (!var1.equals("ASUS_X00AD_2")) {
                                                                                                            break label201643;
                                                                                                         }
                                                                                                      } catch (Throwable var18245) {
                                                                                                         var10000 = var18245;
                                                                                                         var10001 = false;
                                                                                                         break label201632;
                                                                                                      }

                                                                                                      var4 = 11;
                                                                                                      break label201613;
                                                                                                   }

                                                                                                   try {
                                                                                                      if (!var1.equals("GiONEE_CBL7513")) {
                                                                                                         break label201643;
                                                                                                      }
                                                                                                   } catch (Throwable var18331) {
                                                                                                      var10000 = var18331;
                                                                                                      var10001 = false;
                                                                                                      break label201632;
                                                                                                   }

                                                                                                   var4 = 39;
                                                                                                   break label201613;
                                                                                                }

                                                                                                try {
                                                                                                   if (!var1.equals("Pixi4-7_3G")) {
                                                                                                      break label201643;
                                                                                                   }
                                                                                                } catch (Throwable var18329) {
                                                                                                   var10000 = var18329;
                                                                                                   var10001 = false;
                                                                                                   break label201632;
                                                                                                }

                                                                                                var4 = 90;
                                                                                                break label201613;
                                                                                             }

                                                                                             try {
                                                                                                if (!var1.equals("ELUGA_Note")) {
                                                                                                   break label201643;
                                                                                                }
                                                                                             } catch (Throwable var18244) {
                                                                                                var10000 = var18244;
                                                                                                var10001 = false;
                                                                                                break label201632;
                                                                                             }

                                                                                             var4 = 26;
                                                                                             break label201613;
                                                                                          }

                                                                                          try {
                                                                                             if (!var1.equals("HWVNS-H")) {
                                                                                                break label201643;
                                                                                             }
                                                                                          } catch (Throwable var18242) {
                                                                                             var10000 = var18242;
                                                                                             var10001 = false;
                                                                                             break label201632;
                                                                                          }

                                                                                          var4 = 53;
                                                                                          break label201613;
                                                                                       }

                                                                                       try {
                                                                                          if (!var1.equals("taido_row")) {
                                                                                             break label201643;
                                                                                          }
                                                                                       } catch (Throwable var18328) {
                                                                                          var10000 = var18328;
                                                                                          var10001 = false;
                                                                                          break label201632;
                                                                                       }

                                                                                       var4 = 105;
                                                                                       break label201613;
                                                                                    }

                                                                                    try {
                                                                                       if (!var1.equals("ELUGA_Prim")) {
                                                                                          break label201643;
                                                                                       }
                                                                                    } catch (Throwable var18243) {
                                                                                       var10000 = var18243;
                                                                                       var10001 = false;
                                                                                       break label201632;
                                                                                    }

                                                                                    var4 = 27;
                                                                                    break label201613;
                                                                                 }

                                                                                 try {
                                                                                    if (!var1.equals("Z12_PRO")) {
                                                                                       break label201643;
                                                                                    }
                                                                                 } catch (Throwable var18326) {
                                                                                    var10000 = var18326;
                                                                                    var10001 = false;
                                                                                    break label201632;
                                                                                 }

                                                                                 var4 = 122;
                                                                                 break label201613;
                                                                              }

                                                                              try {
                                                                                 if (!var1.equals("HWWAS-H")) {
                                                                                    break label201643;
                                                                                 }
                                                                              } catch (Throwable var18241) {
                                                                                 var10000 = var18241;
                                                                                 var10001 = false;
                                                                                 break label201632;
                                                                              }

                                                                              var4 = 54;
                                                                              break label201613;
                                                                           }

                                                                           try {
                                                                              if (!var1.equals("BLACK-1X")) {
                                                                                 break label201643;
                                                                              }
                                                                           } catch (Throwable var18327) {
                                                                              var10000 = var18327;
                                                                              var10001 = false;
                                                                              break label201632;
                                                                           }

                                                                           var4 = 13;
                                                                           break label201613;
                                                                        }

                                                                        try {
                                                                           if (!var1.equals("ELUGA_A3_Pro")) {
                                                                              break label201643;
                                                                           }
                                                                        } catch (Throwable var18325) {
                                                                           var10000 = var18325;
                                                                           var10001 = false;
                                                                           break label201632;
                                                                        }

                                                                        var4 = 25;
                                                                        break label201613;
                                                                     }

                                                                     try {
                                                                        if (!var1.equals("woods_fn")) {
                                                                           break label201643;
                                                                        }
                                                                     } catch (Throwable var18324) {
                                                                        var10000 = var18324;
                                                                        var10001 = false;
                                                                        break label201632;
                                                                     }

                                                                     var4 = 118;
                                                                     break label201613;
                                                                  }

                                                                  try {
                                                                     if (!var1.equals("Q5")) {
                                                                        break label201643;
                                                                     }
                                                                  } catch (Throwable var18322) {
                                                                     var10000 = var18322;
                                                                     var10001 = false;
                                                                     break label201632;
                                                                  }

                                                                  var4 = 98;
                                                                  break label201613;
                                                               }

                                                               try {
                                                                  if (!var1.equals("C1")) {
                                                                     break label201643;
                                                                  }
                                                               } catch (Throwable var18323) {
                                                                  var10000 = var18323;
                                                                  var10001 = false;
                                                                  break label201632;
                                                               }

                                                               var4 = 16;
                                                               break label201613;
                                                            }

                                                            try {
                                                               if (!var1.equals("V1")) {
                                                                  break label201643;
                                                               }
                                                            } catch (Throwable var18321) {
                                                               var10000 = var18321;
                                                               var10001 = false;
                                                               break label201632;
                                                            }

                                                            var4 = 111;
                                                            break label201613;
                                                         }

                                                         try {
                                                            if (!var1.equals("V5")) {
                                                               break label201643;
                                                            }
                                                         } catch (Throwable var18320) {
                                                            var10000 = var18320;
                                                            var10001 = false;
                                                            break label201632;
                                                         }

                                                         var4 = 113;
                                                         break label201613;
                                                      }

                                                      try {
                                                         if (!var1.equals("JGZ")) {
                                                            break label201643;
                                                         }
                                                      } catch (Throwable var18318) {
                                                         var10000 = var18318;
                                                         var10001 = false;
                                                         break label201632;
                                                      }

                                                      var4 = 61;
                                                      break label201613;
                                                   }

                                                   try {
                                                      if (!var1.equals("mh")) {
                                                         break label201643;
                                                      }
                                                   } catch (Throwable var18319) {
                                                      var10000 = var18319;
                                                      var10001 = false;
                                                      break label201632;
                                                   }

                                                   var4 = 70;
                                                   break label201613;
                                                }

                                                try {
                                                   if (!var1.equals("M5c")) {
                                                      break label201643;
                                                   }
                                                } catch (Throwable var18317) {
                                                   var10000 = var18317;
                                                   var10001 = false;
                                                   break label201632;
                                                }

                                                var4 = 66;
                                                break label201613;
                                             }

                                             try {
                                                if (!var1.equals("MX6")) {
                                                   break label201643;
                                                }
                                             } catch (Throwable var18316) {
                                                var10000 = var18316;
                                                var10001 = false;
                                                break label201632;
                                             }

                                             var4 = 72;
                                             break label201613;
                                          }

                                          try {
                                             if (!var1.equals("PLE")) {
                                                break label201643;
                                             }
                                          } catch (Throwable var18314) {
                                             var10000 = var18314;
                                             var10001 = false;
                                             break label201632;
                                          }

                                          var4 = 92;
                                          break label201613;
                                       }

                                       try {
                                          if (!var1.equals("P85")) {
                                             break label201643;
                                          }
                                       } catch (Throwable var18315) {
                                          var10000 = var18315;
                                          var10001 = false;
                                          break label201632;
                                       }

                                       var4 = 80;
                                       break label201613;
                                    }

                                    try {
                                       if (!var1.equals("QX1")) {
                                          break label201643;
                                       }
                                    } catch (Throwable var18313) {
                                       var10000 = var18313;
                                       var10001 = false;
                                       break label201632;
                                    }

                                    var4 = 100;
                                    break label201613;
                                 }

                                 try {
                                    if (!var1.equals("Z80")) {
                                       break label201643;
                                    }
                                 } catch (Throwable var18312) {
                                    var10000 = var18312;
                                    var10001 = false;
                                    break label201632;
                                 }

                                 var4 = 123;
                                 break label201613;
                              }

                              var4 = -1;
                              break label201613;
                           }

                           var4 = 42;
                           break label201613;
                        }

                        var4 = 43;
                        break label201613;
                     }

                     var4 = 44;
                  }

                  switch(var4) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                  case 9:
                  case 10:
                  case 11:
                  case 12:
                  case 13:
                  case 14:
                  case 15:
                  case 16:
                  case 17:
                  case 18:
                  case 19:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                  case 28:
                  case 29:
                  case 30:
                  case 31:
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 61:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 80:
                  case 81:
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 101:
                  case 102:
                  case 103:
                  case 104:
                  case 105:
                  case 106:
                  case 107:
                  case 108:
                  case 109:
                  case 110:
                  case 111:
                  case 112:
                  case 113:
                  case 114:
                  case 115:
                  case 116:
                  case 117:
                  case 118:
                  case 119:
                  case 120:
                  case 121:
                  case 122:
                  case 123:
                     try {
                        deviceNeedsSetOutputSurfaceWorkaround = true;
                     } catch (Throwable var18235) {
                        var10000 = var18235;
                        var10001 = false;
                        break label201632;
                     }
                  default:
                     int var18366;
                     try {
                        var1 = Util.MODEL;
                        var18366 = var1.hashCode();
                     } catch (Throwable var18234) {
                        var10000 = var18234;
                        var10001 = false;
                        break label201632;
                     }

                     label200732: {
                        label201769: {
                           if (var18366 != 2006354) {
                              if (var18366 == 2006367) {
                                 label201768: {
                                    try {
                                       if (!var1.equals("AFTN")) {
                                          break label201768;
                                       }
                                    } catch (Throwable var18237) {
                                       var10000 = var18237;
                                       var10001 = false;
                                       break label201632;
                                    }

                                    var4 = 1;
                                    break label200732;
                                 }
                              }
                           } else {
                              try {
                                 if (var1.equals("AFTA")) {
                                    break label201769;
                                 }
                              } catch (Throwable var18236) {
                                 var10000 = var18236;
                                 var10001 = false;
                                 break label201632;
                              }
                           }

                           var4 = -1;
                           break label200732;
                        }

                        var4 = var3;
                     }

                     if (var4 == 0 || var4 == 1) {
                        try {
                           deviceNeedsSetOutputSurfaceWorkaround = true;
                        } catch (Throwable var18233) {
                           var10000 = var18233;
                           var10001 = false;
                           break label201632;
                        }
                     }
                  }
               }

               try {
                  evaluatedDeviceNeedsSetOutputSurfaceWorkaround = true;
               } catch (Throwable var18232) {
                  var10000 = var18232;
                  var10001 = false;
                  break label201632;
               }
            }

            label200707:
            try {
               return deviceNeedsSetOutputSurfaceWorkaround;
            } catch (Throwable var18231) {
               var10000 = var18231;
               var10001 = false;
               break label200707;
            }
         }

         while(true) {
            Throwable var18365 = var10000;

            try {
               throw var18365;
            } catch (Throwable var18230) {
               var10000 = var18230;
               var10001 = false;
               continue;
            }
         }
      }
   }

   protected void configureCodec(MediaCodecInfo var1, MediaCodec var2, Format var3, MediaCrypto var4, float var5) throws MediaCodecUtil.DecoderQueryException {
      this.codecMaxValues = this.getCodecMaxValues(var1, var3, this.getStreamFormats());
      MediaFormat var6 = this.getMediaFormat(var3, this.codecMaxValues, var5, this.deviceNeedsNoPostProcessWorkaround, this.tunnelingAudioSessionId);
      if (this.surface == null) {
         Assertions.checkState(this.shouldUseDummySurface(var1));
         if (this.dummySurface == null) {
            this.dummySurface = DummySurface.newInstanceV17(this.context, var1.secure);
         }

         this.surface = this.dummySurface;
      }

      var2.configure(var6, this.surface, var4, 0);
      if (Util.SDK_INT >= 23 && this.tunneling) {
         this.tunnelingOnFrameRenderedListener = new MediaCodecVideoRenderer.OnFrameRenderedListenerV23(var2);
      }

   }

   protected void dropOutputBuffer(MediaCodec var1, int var2, long var3) {
      TraceUtil.beginSection("dropVideoBuffer");
      var1.releaseOutputBuffer(var2, false);
      TraceUtil.endSection();
      this.updateDroppedBufferCounters(1);
   }

   protected boolean flushOrReleaseCodec() {
      boolean var1;
      try {
         var1 = super.flushOrReleaseCodec();
      } finally {
         this.buffersInCodecCount = 0;
      }

      return var1;
   }

   protected MediaCodecVideoRenderer.CodecMaxValues getCodecMaxValues(MediaCodecInfo var1, Format var2, Format[] var3) throws MediaCodecUtil.DecoderQueryException {
      int var4 = var2.width;
      int var5 = var2.height;
      int var6 = getMaxInputSize(var1, var2);
      int var7;
      int var8;
      if (var3.length == 1) {
         var7 = var6;
         if (var6 != -1) {
            var8 = getCodecMaxInputSize(var1, var2.sampleMimeType, var2.width, var2.height);
            var7 = var6;
            if (var8 != -1) {
               var7 = Math.min((int)((float)var6 * 1.5F), var8);
            }
         }

         return new MediaCodecVideoRenderer.CodecMaxValues(var4, var5, var7);
      } else {
         int var9 = var3.length;
         var7 = var5;
         boolean var18 = false;

         int var13;
         int var14;
         for(var8 = 0; var8 < var9; var6 = var14) {
            Format var10 = var3[var8];
            boolean var11 = var18;
            int var12 = var4;
            var13 = var7;
            var14 = var6;
            if (var1.isSeamlessAdaptationSupported(var2, var10, false)) {
               boolean var19;
               if (var10.width != -1 && var10.height != -1) {
                  var19 = false;
               } else {
                  var19 = true;
               }

               var11 = var18 | var19;
               var12 = Math.max(var4, var10.width);
               var13 = Math.max(var7, var10.height);
               var14 = Math.max(var6, getMaxInputSize(var1, var10));
            }

            ++var8;
            var18 = var11;
            var4 = var12;
            var7 = var13;
         }

         var13 = var4;
         var14 = var7;
         var8 = var6;
         if (var18) {
            StringBuilder var16 = new StringBuilder();
            var16.append("Resolutions unknown. Codec max resolution: ");
            var16.append(var4);
            var16.append("x");
            var16.append(var7);
            Log.w("MediaCodecVideoRenderer", var16.toString());
            Point var17 = getCodecMaxSize(var1, var2);
            var13 = var4;
            var14 = var7;
            var8 = var6;
            if (var17 != null) {
               var13 = Math.max(var4, var17.x);
               var14 = Math.max(var7, var17.y);
               var8 = Math.max(var6, getCodecMaxInputSize(var1, var2.sampleMimeType, var13, var14));
               StringBuilder var15 = new StringBuilder();
               var15.append("Codec max resolution adjusted to: ");
               var15.append(var13);
               var15.append("x");
               var15.append(var14);
               Log.w("MediaCodecVideoRenderer", var15.toString());
            }
         }

         return new MediaCodecVideoRenderer.CodecMaxValues(var13, var14, var8);
      }
   }

   protected boolean getCodecNeedsEosPropagation() {
      return this.tunneling;
   }

   protected float getCodecOperatingRateV23(float var1, Format var2, Format[] var3) {
      int var4 = var3.length;
      float var5 = -1.0F;
      int var6 = 0;

      float var7;
      float var9;
      for(var7 = -1.0F; var6 < var4; var7 = var9) {
         float var8 = var3[var6].frameRate;
         var9 = var7;
         if (var8 != -1.0F) {
            var9 = Math.max(var7, var8);
         }

         ++var6;
      }

      if (var7 == -1.0F) {
         var1 = var5;
      } else {
         var1 = var7 * var1;
      }

      return var1;
   }

   @SuppressLint({"InlinedApi"})
   protected MediaFormat getMediaFormat(Format var1, MediaCodecVideoRenderer.CodecMaxValues var2, float var3, boolean var4, int var5) {
      MediaFormat var6 = new MediaFormat();
      var6.setString("mime", var1.sampleMimeType);
      var6.setInteger("width", var1.width);
      var6.setInteger("height", var1.height);
      MediaFormatUtil.setCsdBuffers(var6, var1.initializationData);
      MediaFormatUtil.maybeSetFloat(var6, "frame-rate", var1.frameRate);
      MediaFormatUtil.maybeSetInteger(var6, "rotation-degrees", var1.rotationDegrees);
      MediaFormatUtil.maybeSetColorInfo(var6, var1.colorInfo);
      var6.setInteger("max-width", var2.width);
      var6.setInteger("max-height", var2.height);
      MediaFormatUtil.maybeSetInteger(var6, "max-input-size", var2.inputSize);
      if (Util.SDK_INT >= 23) {
         var6.setInteger("priority", 0);
         if (var3 != -1.0F) {
            var6.setFloat("operating-rate", var3);
         }
      }

      if (var4) {
         var6.setInteger("no-post-process", 1);
         var6.setInteger("auto-frc", 0);
      }

      if (var5 != 0) {
         configureTunnelingV21(var6, var5);
      }

      return var6;
   }

   public void handleMessage(int var1, Object var2) throws ExoPlaybackException {
      if (var1 == 1) {
         this.setSurface((Surface)var2);
      } else if (var1 == 4) {
         this.scalingMode = (Integer)var2;
         MediaCodec var3 = this.getCodec();
         if (var3 != null) {
            var3.setVideoScalingMode(this.scalingMode);
         }
      } else if (var1 == 6) {
         this.frameMetadataListener = (VideoFrameMetadataListener)var2;
      } else {
         super.handleMessage(var1, var2);
      }

   }

   public boolean isReady() {
      if (super.isReady()) {
         label40: {
            if (!this.renderedFirstFrame) {
               Surface var1 = this.dummySurface;
               if ((var1 == null || this.surface != var1) && this.getCodec() != null && !this.tunneling) {
                  break label40;
               }
            }

            this.joiningDeadlineMs = -9223372036854775807L;
            return true;
         }
      }

      if (this.joiningDeadlineMs == -9223372036854775807L) {
         return false;
      } else if (SystemClock.elapsedRealtime() < this.joiningDeadlineMs) {
         return true;
      } else {
         this.joiningDeadlineMs = -9223372036854775807L;
         return false;
      }
   }

   protected boolean maybeDropBuffersToKeyframe(MediaCodec var1, int var2, long var3, long var5) throws ExoPlaybackException {
      var2 = this.skipSource(var5);
      if (var2 == 0) {
         return false;
      } else {
         DecoderCounters var7 = super.decoderCounters;
         ++var7.droppedToKeyframeCount;
         this.updateDroppedBufferCounters(this.buffersInCodecCount + var2);
         this.flushOrReinitCodec();
         return true;
      }
   }

   void maybeNotifyRenderedFirstFrame() {
      if (!this.renderedFirstFrame) {
         this.renderedFirstFrame = true;
         this.eventDispatcher.renderedFirstFrame(this.surface);
      }

   }

   protected void onCodecInitialized(String var1, long var2, long var4) {
      this.eventDispatcher.decoderInitialized(var1, var2, var4);
      this.codecNeedsSetOutputSurfaceWorkaround = this.codecNeedsSetOutputSurfaceWorkaround(var1);
   }

   protected void onDisabled() {
      this.lastInputTimeUs = -9223372036854775807L;
      this.outputStreamOffsetUs = -9223372036854775807L;
      this.pendingOutputStreamOffsetCount = 0;
      this.clearReportedVideoSize();
      this.clearRenderedFirstFrame();
      this.frameReleaseTimeHelper.disable();
      this.tunnelingOnFrameRenderedListener = null;

      try {
         super.onDisabled();
      } finally {
         this.eventDispatcher.disabled(super.decoderCounters);
      }

   }

   protected void onEnabled(boolean var1) throws ExoPlaybackException {
      super.onEnabled(var1);
      int var2 = this.tunnelingAudioSessionId;
      this.tunnelingAudioSessionId = this.getConfiguration().tunnelingAudioSessionId;
      if (this.tunnelingAudioSessionId != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.tunneling = var1;
      if (this.tunnelingAudioSessionId != var2) {
         this.releaseCodec();
      }

      this.eventDispatcher.enabled(super.decoderCounters);
      this.frameReleaseTimeHelper.enable();
   }

   protected void onInputFormatChanged(Format var1) throws ExoPlaybackException {
      super.onInputFormatChanged(var1);
      this.eventDispatcher.inputFormatChanged(var1);
      this.pendingPixelWidthHeightRatio = var1.pixelWidthHeightRatio;
      this.pendingRotationDegrees = var1.rotationDegrees;
   }

   protected void onOutputFormatChanged(MediaCodec var1, MediaFormat var2) {
      boolean var3;
      if (var2.containsKey("crop-right") && var2.containsKey("crop-left") && var2.containsKey("crop-bottom") && var2.containsKey("crop-top")) {
         var3 = true;
      } else {
         var3 = false;
      }

      int var4;
      if (var3) {
         var4 = var2.getInteger("crop-right") - var2.getInteger("crop-left") + 1;
      } else {
         var4 = var2.getInteger("width");
      }

      int var5;
      if (var3) {
         var5 = var2.getInteger("crop-bottom") - var2.getInteger("crop-top") + 1;
      } else {
         var5 = var2.getInteger("height");
      }

      this.processOutputFormat(var1, var4, var5);
   }

   protected void onPositionReset(long var1, boolean var3) throws ExoPlaybackException {
      super.onPositionReset(var1, var3);
      this.clearRenderedFirstFrame();
      this.initialPositionUs = -9223372036854775807L;
      this.consecutiveDroppedFrameCount = 0;
      this.lastInputTimeUs = -9223372036854775807L;
      int var4 = this.pendingOutputStreamOffsetCount;
      if (var4 != 0) {
         this.outputStreamOffsetUs = this.pendingOutputStreamOffsetsUs[var4 - 1];
         this.pendingOutputStreamOffsetCount = 0;
      }

      if (var3) {
         this.setJoiningDeadlineMs();
      } else {
         this.joiningDeadlineMs = -9223372036854775807L;
      }

   }

   protected void onProcessedOutputBuffer(long var1) {
      --this.buffersInCodecCount;

      while(true) {
         int var3 = this.pendingOutputStreamOffsetCount;
         if (var3 == 0 || var1 < this.pendingOutputStreamSwitchTimesUs[0]) {
            return;
         }

         long[] var4 = this.pendingOutputStreamOffsetsUs;
         this.outputStreamOffsetUs = var4[0];
         this.pendingOutputStreamOffsetCount = var3 - 1;
         System.arraycopy(var4, 1, var4, 0, this.pendingOutputStreamOffsetCount);
         var4 = this.pendingOutputStreamSwitchTimesUs;
         System.arraycopy(var4, 1, var4, 0, this.pendingOutputStreamOffsetCount);
      }
   }

   protected void onProcessedTunneledBuffer(long var1) {
      Format var3 = this.updateOutputFormatForTime(var1);
      if (var3 != null) {
         this.processOutputFormat(this.getCodec(), var3.width, var3.height);
      }

      this.maybeNotifyVideoSizeChanged();
      this.maybeNotifyRenderedFirstFrame();
      this.onProcessedOutputBuffer(var1);
   }

   protected void onQueueInputBuffer(DecoderInputBuffer var1) {
      ++this.buffersInCodecCount;
      this.lastInputTimeUs = Math.max(var1.timeUs, this.lastInputTimeUs);
      if (Util.SDK_INT < 23 && this.tunneling) {
         this.onProcessedTunneledBuffer(var1.timeUs);
      }

   }

   protected void onReset() {
      try {
         super.onReset();
      } finally {
         Surface var1 = this.dummySurface;
         if (var1 != null) {
            if (this.surface == var1) {
               this.surface = null;
            }

            this.dummySurface.release();
            this.dummySurface = null;
         }

      }

   }

   protected void onStarted() {
      super.onStarted();
      this.droppedFrames = 0;
      this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
      this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000L;
   }

   protected void onStopped() {
      this.joiningDeadlineMs = -9223372036854775807L;
      this.maybeNotifyDroppedFrames();
      super.onStopped();
   }

   protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
      if (this.outputStreamOffsetUs == -9223372036854775807L) {
         this.outputStreamOffsetUs = var2;
      } else {
         int var4 = this.pendingOutputStreamOffsetCount;
         if (var4 == this.pendingOutputStreamOffsetsUs.length) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Too many stream changes, so dropping offset: ");
            var5.append(this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
            Log.w("MediaCodecVideoRenderer", var5.toString());
         } else {
            this.pendingOutputStreamOffsetCount = var4 + 1;
         }

         long[] var6 = this.pendingOutputStreamOffsetsUs;
         var4 = this.pendingOutputStreamOffsetCount;
         var6[var4 - 1] = var2;
         this.pendingOutputStreamSwitchTimesUs[var4 - 1] = this.lastInputTimeUs;
      }

      super.onStreamChanged(var1, var2);
   }

   protected boolean processOutputBuffer(long var1, long var3, MediaCodec var5, ByteBuffer var6, int var7, int var8, long var9, boolean var11, Format var12) throws ExoPlaybackException {
      if (this.initialPositionUs == -9223372036854775807L) {
         this.initialPositionUs = var1;
      }

      long var13 = var9 - this.outputStreamOffsetUs;
      if (var11) {
         this.skipOutputBuffer(var5, var7, var13);
         return true;
      } else {
         long var15 = var9 - var1;
         if (this.surface == this.dummySurface) {
            if (isBufferLate(var15)) {
               this.skipOutputBuffer(var5, var7, var13);
               return true;
            } else {
               return false;
            }
         } else {
            long var17 = SystemClock.elapsedRealtime() * 1000L;
            boolean var22;
            if (this.getState() == 2) {
               var22 = true;
            } else {
               var22 = false;
            }

            if (this.renderedFirstFrame && (!var22 || !this.shouldForceRenderOutputBuffer(var15, var17 - this.lastRenderTimeUs))) {
               if (var22 && var1 != this.initialPositionUs) {
                  long var19 = System.nanoTime();
                  var9 = this.frameReleaseTimeHelper.adjustReleaseTime(var9, (var15 - (var17 - var3)) * 1000L + var19);
                  var19 = (var9 - var19) / 1000L;
                  if (this.shouldDropBuffersToKeyframe(var19, var3) && this.maybeDropBuffersToKeyframe(var5, var7, var13, var1)) {
                     return false;
                  } else {
                     if (this.shouldDropOutputBuffer(var19, var3)) {
                        this.dropOutputBuffer(var5, var7, var13);
                     } else if (Util.SDK_INT >= 21) {
                        if (var19 >= 50000L) {
                           return false;
                        }

                        this.notifyFrameMetadataListener(var13, var9, var12);
                        this.renderOutputBufferV21(var5, var7, var13, var9);
                     } else {
                        if (var19 >= 30000L) {
                           return false;
                        }

                        if (var19 > 11000L) {
                           try {
                              Thread.sleep((var19 - 10000L) / 1000L);
                           } catch (InterruptedException var21) {
                              Thread.currentThread().interrupt();
                              return false;
                           }
                        }

                        this.notifyFrameMetadataListener(var13, var9, var12);
                        this.renderOutputBuffer(var5, var7, var13);
                     }

                     return true;
                  }
               } else {
                  return false;
               }
            } else {
               var1 = System.nanoTime();
               this.notifyFrameMetadataListener(var13, var1, var12);
               if (Util.SDK_INT >= 21) {
                  this.renderOutputBufferV21(var5, var7, var13, var1);
               } else {
                  this.renderOutputBuffer(var5, var7, var13);
               }

               return true;
            }
         }
      }
   }

   protected void releaseCodec() {
      try {
         super.releaseCodec();
      } finally {
         this.buffersInCodecCount = 0;
      }

   }

   protected void renderOutputBuffer(MediaCodec var1, int var2, long var3) {
      this.maybeNotifyVideoSizeChanged();
      TraceUtil.beginSection("releaseOutputBuffer");
      var1.releaseOutputBuffer(var2, true);
      TraceUtil.endSection();
      this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000L;
      DecoderCounters var5 = super.decoderCounters;
      ++var5.renderedOutputBufferCount;
      this.consecutiveDroppedFrameCount = 0;
      this.maybeNotifyRenderedFirstFrame();
   }

   @TargetApi(21)
   protected void renderOutputBufferV21(MediaCodec var1, int var2, long var3, long var5) {
      this.maybeNotifyVideoSizeChanged();
      TraceUtil.beginSection("releaseOutputBuffer");
      var1.releaseOutputBuffer(var2, var5);
      TraceUtil.endSection();
      this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000L;
      DecoderCounters var7 = super.decoderCounters;
      ++var7.renderedOutputBufferCount;
      this.consecutiveDroppedFrameCount = 0;
      this.maybeNotifyRenderedFirstFrame();
   }

   protected boolean shouldDropBuffersToKeyframe(long var1, long var3) {
      return isBufferVeryLate(var1);
   }

   protected boolean shouldDropOutputBuffer(long var1, long var3) {
      return isBufferLate(var1);
   }

   protected boolean shouldForceRenderOutputBuffer(long var1, long var3) {
      boolean var5;
      if (isBufferLate(var1) && var3 > 100000L) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   protected boolean shouldInitCodec(MediaCodecInfo var1) {
      boolean var2;
      if (this.surface == null && !this.shouldUseDummySurface(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected void skipOutputBuffer(MediaCodec var1, int var2, long var3) {
      TraceUtil.beginSection("skipVideoBuffer");
      var1.releaseOutputBuffer(var2, false);
      TraceUtil.endSection();
      DecoderCounters var5 = super.decoderCounters;
      ++var5.skippedOutputBufferCount;
   }

   protected int supportsFormat(MediaCodecSelector var1, DrmSessionManager var2, Format var3) throws MediaCodecUtil.DecoderQueryException {
      boolean var4 = MimeTypes.isVideo(var3.sampleMimeType);
      byte var5 = 0;
      if (!var4) {
         return 0;
      } else {
         DrmInitData var6 = var3.drmInitData;
         boolean var8;
         if (var6 != null) {
            int var7 = 0;
            var4 = false;

            while(true) {
               var8 = var4;
               if (var7 >= var6.schemeDataCount) {
                  break;
               }

               var4 |= var6.get(var7).requiresSecureDecryption;
               ++var7;
            }
         } else {
            var8 = false;
         }

         List var9 = var1.getDecoderInfos(var3.sampleMimeType, var8);
         var4 = var9.isEmpty();
         byte var12 = 2;
         if (!var4) {
            if (!BaseRenderer.supportsFormatDrm(var2, var6)) {
               return 2;
            } else {
               MediaCodecInfo var11 = (MediaCodecInfo)var9.get(0);
               var4 = var11.isFormatSupported(var3);
               if (var11.isSeamlessAdaptationSupported(var3)) {
                  var12 = 16;
               } else {
                  var12 = 8;
               }

               if (var11.tunneling) {
                  var5 = 32;
               }

               byte var10;
               if (var4) {
                  var10 = 4;
               } else {
                  var10 = 3;
               }

               return var10 | var12 | var5;
            }
         } else {
            if (!var8 || var1.getDecoderInfos(var3.sampleMimeType, false).isEmpty()) {
               var12 = 1;
            }

            return var12;
         }
      }
   }

   protected void updateDroppedBufferCounters(int var1) {
      DecoderCounters var2 = super.decoderCounters;
      var2.droppedBufferCount += var1;
      this.droppedFrames += var1;
      this.consecutiveDroppedFrameCount += var1;
      var2.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, var2.maxConsecutiveDroppedBufferCount);
      var1 = this.maxDroppedFramesToNotify;
      if (var1 > 0 && this.droppedFrames >= var1) {
         this.maybeNotifyDroppedFrames();
      }

   }

   protected static final class CodecMaxValues {
      public final int height;
      public final int inputSize;
      public final int width;

      public CodecMaxValues(int var1, int var2, int var3) {
         this.width = var1;
         this.height = var2;
         this.inputSize = var3;
      }
   }

   @TargetApi(23)
   private final class OnFrameRenderedListenerV23 implements OnFrameRenderedListener {
      private OnFrameRenderedListenerV23(MediaCodec var2) {
         var2.setOnFrameRenderedListener(this, new Handler());
      }

      // $FF: synthetic method
      OnFrameRenderedListenerV23(MediaCodec var2, Object var3) {
         this(var2);
      }

      public void onFrameRendered(MediaCodec var1, long var2, long var4) {
         MediaCodecVideoRenderer var6 = MediaCodecVideoRenderer.this;
         if (this == var6.tunnelingOnFrameRenderedListener) {
            var6.onProcessedTunneledBuffer(var2);
         }
      }
   }
}
