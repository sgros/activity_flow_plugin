package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.media.MediaCodecInfo.AudioCapabilities;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecInfo.VideoCapabilities;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

@TargetApi(16)
public final class MediaCodecInfo {
   public final boolean adaptive;
   public final CodecCapabilities capabilities;
   private final boolean isVideo;
   public final String mimeType;
   public final String name;
   public final boolean passthrough;
   public final boolean secure;
   public final boolean tunneling;

   private MediaCodecInfo(String var1, String var2, CodecCapabilities var3, boolean var4, boolean var5, boolean var6) {
      Assertions.checkNotNull(var1);
      this.name = (String)var1;
      this.mimeType = var2;
      this.capabilities = var3;
      this.passthrough = var4;
      boolean var7 = true;
      if (!var5 && var3 != null && isAdaptive(var3)) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.adaptive = var4;
      if (var3 != null && isTunneling(var3)) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.tunneling = var4;
      var4 = var7;
      if (!var6) {
         if (var3 != null && isSecure(var3)) {
            var4 = var7;
         } else {
            var4 = false;
         }
      }

      this.secure = var4;
      this.isVideo = MimeTypes.isVideo(var2);
   }

   private static int adjustMaxInputChannelCount(String var0, String var1, int var2) {
      if (var2 <= 1 && (Util.SDK_INT < 26 || var2 <= 0) && !"audio/mpeg".equals(var1) && !"audio/3gpp".equals(var1) && !"audio/amr-wb".equals(var1) && !"audio/mp4a-latm".equals(var1) && !"audio/vorbis".equals(var1) && !"audio/opus".equals(var1) && !"audio/raw".equals(var1) && !"audio/flac".equals(var1) && !"audio/g711-alaw".equals(var1) && !"audio/g711-mlaw".equals(var1) && !"audio/gsm".equals(var1)) {
         byte var3;
         if ("audio/ac3".equals(var1)) {
            var3 = 6;
         } else if ("audio/eac3".equals(var1)) {
            var3 = 16;
         } else {
            var3 = 30;
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("AssumedMaxChannelAdjustment: ");
         var4.append(var0);
         var4.append(", [");
         var4.append(var2);
         var4.append(" to ");
         var4.append(var3);
         var4.append("]");
         Log.w("MediaCodecInfo", var4.toString());
         return var3;
      } else {
         return var2;
      }
   }

   @TargetApi(21)
   private static boolean areSizeAndRateSupportedV21(VideoCapabilities var0, int var1, int var2, double var3) {
      boolean var5;
      if (var3 != -1.0D && var3 > 0.0D) {
         var5 = var0.areSizeAndRateSupported(var1, var2, var3);
      } else {
         var5 = var0.isSizeSupported(var1, var2);
      }

      return var5;
   }

   private static boolean isAdaptive(CodecCapabilities var0) {
      boolean var1;
      if (Util.SDK_INT >= 19 && isAdaptiveV19(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @TargetApi(19)
   private static boolean isAdaptiveV19(CodecCapabilities var0) {
      return var0.isFeatureSupported("adaptive-playback");
   }

   private static boolean isSecure(CodecCapabilities var0) {
      boolean var1;
      if (Util.SDK_INT >= 21 && isSecureV21(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @TargetApi(21)
   private static boolean isSecureV21(CodecCapabilities var0) {
      return var0.isFeatureSupported("secure-playback");
   }

   private static boolean isTunneling(CodecCapabilities var0) {
      boolean var1;
      if (Util.SDK_INT >= 21 && isTunnelingV21(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @TargetApi(21)
   private static boolean isTunnelingV21(CodecCapabilities var0) {
      return var0.isFeatureSupported("tunneled-playback");
   }

   private void logAssumedSupport(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("AssumedSupport [");
      var2.append(var1);
      var2.append("] [");
      var2.append(this.name);
      var2.append(", ");
      var2.append(this.mimeType);
      var2.append("] [");
      var2.append(Util.DEVICE_DEBUG_INFO);
      var2.append("]");
      Log.d("MediaCodecInfo", var2.toString());
   }

   private void logNoSupport(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("NoSupport [");
      var2.append(var1);
      var2.append("] [");
      var2.append(this.name);
      var2.append(", ");
      var2.append(this.mimeType);
      var2.append("] [");
      var2.append(Util.DEVICE_DEBUG_INFO);
      var2.append("]");
      Log.d("MediaCodecInfo", var2.toString());
   }

   public static MediaCodecInfo newInstance(String var0, String var1, CodecCapabilities var2, boolean var3, boolean var4) {
      return new MediaCodecInfo(var0, var1, var2, false, var3, var4);
   }

   public static MediaCodecInfo newPassthroughInstance(String var0) {
      return new MediaCodecInfo(var0, (String)null, (CodecCapabilities)null, true, false, false);
   }

   @TargetApi(21)
   public Point alignVideoSizeV21(int var1, int var2) {
      CodecCapabilities var3 = this.capabilities;
      if (var3 == null) {
         this.logNoSupport("align.caps");
         return null;
      } else {
         VideoCapabilities var6 = var3.getVideoCapabilities();
         if (var6 == null) {
            this.logNoSupport("align.vCaps");
            return null;
         } else {
            int var4 = var6.getWidthAlignment();
            int var5 = var6.getHeightAlignment();
            return new Point(Util.ceilDivide(var1, var4) * var4, Util.ceilDivide(var2, var5) * var5);
         }
      }
   }

   public CodecProfileLevel[] getProfileLevels() {
      CodecCapabilities var1 = this.capabilities;
      CodecProfileLevel[] var3;
      if (var1 != null) {
         CodecProfileLevel[] var2 = var1.profileLevels;
         var3 = var2;
         if (var2 != null) {
            return var3;
         }
      }

      var3 = new CodecProfileLevel[0];
      return var3;
   }

   @TargetApi(21)
   public boolean isAudioChannelCountSupportedV21(int var1) {
      CodecCapabilities var2 = this.capabilities;
      if (var2 == null) {
         this.logNoSupport("channelCount.caps");
         return false;
      } else {
         AudioCapabilities var3 = var2.getAudioCapabilities();
         if (var3 == null) {
            this.logNoSupport("channelCount.aCaps");
            return false;
         } else if (adjustMaxInputChannelCount(this.name, this.mimeType, var3.getMaxInputChannelCount()) < var1) {
            StringBuilder var4 = new StringBuilder();
            var4.append("channelCount.support, ");
            var4.append(var1);
            this.logNoSupport(var4.toString());
            return false;
         } else {
            return true;
         }
      }
   }

   @TargetApi(21)
   public boolean isAudioSampleRateSupportedV21(int var1) {
      CodecCapabilities var2 = this.capabilities;
      if (var2 == null) {
         this.logNoSupport("sampleRate.caps");
         return false;
      } else {
         AudioCapabilities var3 = var2.getAudioCapabilities();
         if (var3 == null) {
            this.logNoSupport("sampleRate.aCaps");
            return false;
         } else if (!var3.isSampleRateSupported(var1)) {
            StringBuilder var4 = new StringBuilder();
            var4.append("sampleRate.support, ");
            var4.append(var1);
            this.logNoSupport(var4.toString());
            return false;
         } else {
            return true;
         }
      }
   }

   public boolean isCodecSupported(String var1) {
      if (var1 != null && this.mimeType != null) {
         String var2 = MimeTypes.getMediaMimeType(var1);
         if (var2 == null) {
            return true;
         } else {
            StringBuilder var10;
            if (!this.mimeType.equals(var2)) {
               var10 = new StringBuilder();
               var10.append("codec.mime ");
               var10.append(var1);
               var10.append(", ");
               var10.append(var2);
               this.logNoSupport(var10.toString());
               return false;
            } else {
               Pair var3 = MediaCodecUtil.getCodecProfileAndLevel(var1);
               if (var3 == null) {
                  return true;
               } else {
                  int var4 = (Integer)var3.first;
                  int var5 = (Integer)var3.second;
                  if (!this.isVideo && var4 != 42) {
                     return true;
                  } else {
                     CodecProfileLevel[] var6 = this.getProfileLevels();
                     int var7 = var6.length;

                     for(int var8 = 0; var8 < var7; ++var8) {
                        CodecProfileLevel var9 = var6[var8];
                        if (var9.profile == var4 && var9.level >= var5) {
                           return true;
                        }
                     }

                     var10 = new StringBuilder();
                     var10.append("codec.profileLevel, ");
                     var10.append(var1);
                     var10.append(", ");
                     var10.append(var2);
                     this.logNoSupport(var10.toString());
                     return false;
                  }
               }
            }
         }
      } else {
         return true;
      }
   }

   public boolean isFormatSupported(Format var1) throws MediaCodecUtil.DecoderQueryException {
      boolean var2 = this.isCodecSupported(var1.codecs);
      boolean var3 = false;
      boolean var4 = false;
      if (!var2) {
         return false;
      } else {
         int var6;
         if (this.isVideo) {
            int var5 = var1.width;
            if (var5 > 0) {
               var6 = var1.height;
               if (var6 > 0) {
                  if (Util.SDK_INT >= 21) {
                     return this.isVideoSizeAndRateSupportedV21(var5, var6, (double)var1.frameRate);
                  }

                  if (var5 * var6 <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                     var4 = true;
                  }

                  if (!var4) {
                     StringBuilder var7 = new StringBuilder();
                     var7.append("legacyFrameSize, ");
                     var7.append(var1.width);
                     var7.append("x");
                     var7.append(var1.height);
                     this.logNoSupport(var7.toString());
                  }

                  return var4;
               }
            }

            return true;
         } else {
            if (Util.SDK_INT >= 21) {
               var6 = var1.sampleRate;
               if (var6 != -1) {
                  var4 = var3;
                  if (!this.isAudioSampleRateSupportedV21(var6)) {
                     return var4;
                  }
               }

               var6 = var1.channelCount;
               if (var6 != -1) {
                  var4 = var3;
                  if (!this.isAudioChannelCountSupportedV21(var6)) {
                     return var4;
                  }
               }
            }

            var4 = true;
            return var4;
         }
      }
   }

   public boolean isSeamlessAdaptationSupported(Format var1) {
      if (this.isVideo) {
         return this.adaptive;
      } else {
         Pair var3 = MediaCodecUtil.getCodecProfileAndLevel(var1.codecs);
         boolean var2;
         if (var3 != null && (Integer)var3.first == 42) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public boolean isSeamlessAdaptationSupported(Format var1, Format var2, boolean var3) {
      boolean var4 = this.isVideo;
      boolean var5 = true;
      boolean var6 = true;
      if (var4) {
         if (var1.sampleMimeType.equals(var2.sampleMimeType) && var1.rotationDegrees == var2.rotationDegrees && (this.adaptive || var1.width == var2.width && var1.height == var2.height)) {
            if (!var3) {
               var3 = var6;
               if (var2.colorInfo == null) {
                  return var3;
               }
            }

            if (Util.areEqual(var1.colorInfo, var2.colorInfo)) {
               var3 = var6;
               return var3;
            }
         }

         var3 = false;
         return var3;
      } else {
         if ("audio/mp4a-latm".equals(this.mimeType) && var1.sampleMimeType.equals(var2.sampleMimeType) && var1.channelCount == var2.channelCount && var1.sampleRate == var2.sampleRate) {
            Pair var9 = MediaCodecUtil.getCodecProfileAndLevel(var1.codecs);
            Pair var10 = MediaCodecUtil.getCodecProfileAndLevel(var2.codecs);
            if (var9 != null && var10 != null) {
               int var7 = (Integer)var9.first;
               int var8 = (Integer)var10.first;
               if (var7 == 42 && var8 == 42) {
                  var3 = var5;
               } else {
                  var3 = false;
               }

               return var3;
            }
         }

         return false;
      }
   }

   @TargetApi(21)
   public boolean isVideoSizeAndRateSupportedV21(int var1, int var2, double var3) {
      CodecCapabilities var5 = this.capabilities;
      if (var5 == null) {
         this.logNoSupport("sizeAndRate.caps");
         return false;
      } else {
         VideoCapabilities var6 = var5.getVideoCapabilities();
         if (var6 == null) {
            this.logNoSupport("sizeAndRate.vCaps");
            return false;
         } else {
            if (!areSizeAndRateSupportedV21(var6, var1, var2, var3)) {
               StringBuilder var7;
               if (var1 >= var2 || !areSizeAndRateSupportedV21(var6, var2, var1, var3)) {
                  var7 = new StringBuilder();
                  var7.append("sizeAndRate.support, ");
                  var7.append(var1);
                  var7.append("x");
                  var7.append(var2);
                  var7.append("x");
                  var7.append(var3);
                  this.logNoSupport(var7.toString());
                  return false;
               }

               var7 = new StringBuilder();
               var7.append("sizeAndRate.rotated, ");
               var7.append(var1);
               var7.append("x");
               var7.append(var2);
               var7.append("x");
               var7.append(var3);
               this.logAssumedSupport(var7.toString());
            }

            return true;
         }
      }
   }

   public String toString() {
      return this.name;
   }
}
