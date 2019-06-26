package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.ParsableBitArray;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public final class DtsUtil {
   private static final int[] CHANNELS_BY_AMODE = new int[]{1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};
   private static final int[] SAMPLE_RATE_BY_SFREQ = new int[]{-1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, 48000, -1, -1};
   private static final int[] TWICE_BITRATE_KBPS_BY_RATE = new int[]{64, 112, 128, 192, 224, 256, 384, 448, 512, 640, 768, 896, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, 4096, 6144, 7680};

   public static int getDtsFrameSize(byte[] var0) {
      int var5;
      boolean var6;
      label29: {
         boolean var1 = false;
         byte var2 = var0[0];
         byte var3;
         if (var2 != -2) {
            label23: {
               if (var2 != -1) {
                  if (var2 != 31) {
                     var5 = (var0[5] & 3) << 12 | (var0[6] & 255) << 4;
                     var3 = var0[7];
                     break label23;
                  }

                  var5 = (var0[6] & 3) << 12 | (var0[7] & 255) << 4;
                  var3 = var0[8];
               } else {
                  var5 = (var0[7] & 3) << 12 | (var0[6] & 255) << 4;
                  var3 = var0[9];
               }

               var5 = ((var3 & 60) >> 2 | var5) + 1;
               var6 = true;
               break label29;
            }
         } else {
            var5 = (var0[4] & 3) << 12 | (var0[7] & 255) << 4;
            var3 = var0[6];
         }

         var5 = ((var3 & 240) >> 4 | var5) + 1;
         var6 = var1;
      }

      int var4 = var5;
      if (var6) {
         var4 = var5 * 16 / 14;
      }

      return var4;
   }

   private static ParsableBitArray getNormalizedFrameHeader(byte[] var0) {
      if (var0[0] == 127) {
         return new ParsableBitArray(var0);
      } else {
         byte[] var1 = Arrays.copyOf(var0, var0.length);
         if (isLittleEndianFrameHeader(var1)) {
            for(int var2 = 0; var2 < var1.length - 1; var2 += 2) {
               byte var3 = var1[var2];
               int var4 = var2 + 1;
               var1[var2] = (byte)var1[var4];
               var1[var4] = (byte)var3;
            }
         }

         ParsableBitArray var6 = new ParsableBitArray(var1);
         if (var1[0] == 31) {
            ParsableBitArray var5 = new ParsableBitArray(var1);

            while(var5.bitsLeft() >= 16) {
               var5.skipBits(2);
               var6.putInt(var5.readBits(14), 14);
            }
         }

         var6.reset(var1);
         return var6;
      }
   }

   private static boolean isLittleEndianFrameHeader(byte[] var0) {
      boolean var1 = false;
      if (var0[0] == -2 || var0[0] == -1) {
         var1 = true;
      }

      return var1;
   }

   public static boolean isSyncWord(int var0) {
      boolean var1;
      if (var0 != 2147385345 && var0 != -25230976 && var0 != 536864768 && var0 != -14745368) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static int parseDtsAudioSampleCount(ByteBuffer var0) {
      int var1 = var0.position();
      byte var2 = var0.get(var1);
      byte var3;
      int var4;
      if (var2 != -2) {
         label30: {
            if (var2 != -1) {
               if (var2 != 31) {
                  var4 = (var0.get(var1 + 4) & 1) << 6;
                  var3 = var0.get(var1 + 5);
                  break label30;
               }

               var4 = (var0.get(var1 + 5) & 7) << 4;
               var3 = var0.get(var1 + 6);
            } else {
               var4 = (var0.get(var1 + 4) & 7) << 4;
               var3 = var0.get(var1 + 7);
            }

            var1 = var3 & 60;
            return ((var1 >> 2 | var4) + 1) * 32;
         }
      } else {
         var4 = (var0.get(var1 + 5) & 1) << 6;
         var3 = var0.get(var1 + 4);
      }

      var1 = var3 & 252;
      return ((var1 >> 2 | var4) + 1) * 32;
   }

   public static int parseDtsAudioSampleCount(byte[] var0) {
      byte var1 = var0[0];
      byte var2;
      int var3;
      int var4;
      if (var1 != -2) {
         label30: {
            if (var1 != -1) {
               if (var1 != 31) {
                  var3 = (var0[4] & 1) << 6;
                  var2 = var0[5];
                  break label30;
               }

               var3 = (var0[5] & 7) << 4;
               var2 = var0[6];
            } else {
               var3 = (var0[4] & 7) << 4;
               var2 = var0[7];
            }

            var4 = var2 & 60;
            return ((var4 >> 2 | var3) + 1) * 32;
         }
      } else {
         var3 = (var0[5] & 1) << 6;
         var2 = var0[4];
      }

      var4 = var2 & 252;
      return ((var4 >> 2 | var3) + 1) * 32;
   }

   public static Format parseDtsFormat(byte[] var0, String var1, String var2, DrmInitData var3) {
      ParsableBitArray var9 = getNormalizedFrameHeader(var0);
      var9.skipBits(60);
      int var4 = var9.readBits(6);
      int var5 = CHANNELS_BY_AMODE[var4];
      var4 = var9.readBits(4);
      int var6 = SAMPLE_RATE_BY_SFREQ[var4];
      var4 = var9.readBits(5);
      int[] var7 = TWICE_BITRATE_KBPS_BY_RATE;
      if (var4 >= var7.length) {
         var4 = -1;
      } else {
         var4 = var7[var4] * 1000 / 2;
      }

      var9.skipBits(10);
      byte var8;
      if (var9.readBits(2) > 0) {
         var8 = 1;
      } else {
         var8 = 0;
      }

      return Format.createAudioSampleFormat(var1, "audio/vnd.dts", (String)null, var4, -1, var5 + var8, var6, (List)null, var3, 0, var2);
   }
}
