package com.google.android.exoplayer2.extractor;

public final class MpegAudioHeader {
   private static final int[] BITRATE_V1_L1 = new int[]{32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000};
   private static final int[] BITRATE_V1_L2 = new int[]{32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000};
   private static final int[] BITRATE_V1_L3 = new int[]{32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000};
   private static final int[] BITRATE_V2 = new int[]{8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000};
   private static final int[] BITRATE_V2_L1 = new int[]{32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000};
   private static final String[] MIME_TYPE_BY_LAYER = new String[]{"audio/mpeg-L1", "audio/mpeg-L2", "audio/mpeg"};
   private static final int[] SAMPLING_RATE_V1 = new int[]{44100, 48000, 32000};
   public int bitrate;
   public int channels;
   public int frameSize;
   public String mimeType;
   public int sampleRate;
   public int samplesPerFrame;
   public int version;

   public static int getFrameSize(int var0) {
      if ((var0 & -2097152) != -2097152) {
         return -1;
      } else {
         int var1 = var0 >>> 19 & 3;
         if (var1 == 1) {
            return -1;
         } else {
            int var2 = var0 >>> 17 & 3;
            if (var2 == 0) {
               return -1;
            } else {
               int var3 = var0 >>> 12 & 15;
               if (var3 != 0 && var3 != 15) {
                  int var4 = var0 >>> 10 & 3;
                  if (var4 == 3) {
                     return -1;
                  } else {
                     int var5 = SAMPLING_RATE_V1[var4];
                     if (var1 == 2) {
                        var4 = var5 / 2;
                     } else {
                        var4 = var5;
                        if (var1 == 0) {
                           var4 = var5 / 4;
                        }
                     }

                     int var6 = var0 >>> 9 & 1;
                     if (var2 == 3) {
                        if (var1 == 3) {
                           var0 = BITRATE_V1_L1[var3 - 1];
                        } else {
                           var0 = BITRATE_V2_L1[var3 - 1];
                        }

                        return (var0 * 12 / var4 + var6) * 4;
                     } else {
                        if (var1 == 3) {
                           if (var2 == 2) {
                              var0 = BITRATE_V1_L2[var3 - 1];
                           } else {
                              var0 = BITRATE_V1_L3[var3 - 1];
                           }
                        } else {
                           var0 = BITRATE_V2[var3 - 1];
                        }

                        short var7 = 144;
                        if (var1 == 3) {
                           return var0 * 144 / var4 + var6;
                        } else {
                           if (var2 == 1) {
                              var7 = 72;
                           }

                           return var7 * var0 / var4 + var6;
                        }
                     }
                  }
               } else {
                  return -1;
               }
            }
         }
      }
   }

   public static boolean populateHeader(int var0, MpegAudioHeader var1) {
      if ((var0 & -2097152) != -2097152) {
         return false;
      } else {
         int var2 = var0 >>> 19 & 3;
         if (var2 == 1) {
            return false;
         } else {
            int var3 = var0 >>> 17 & 3;
            if (var3 == 0) {
               return false;
            } else {
               int var4 = var0 >>> 12 & 15;
               if (var4 != 0 && var4 != 15) {
                  int var5 = var0 >>> 10 & 3;
                  if (var5 == 3) {
                     return false;
                  } else {
                     int var6 = SAMPLING_RATE_V1[var5];
                     if (var2 == 2) {
                        var5 = var6 / 2;
                     } else {
                        var5 = var6;
                        if (var2 == 0) {
                           var5 = var6 / 4;
                        }
                     }

                     int var7 = var0 >>> 9 & 1;
                     short var11 = 1152;
                     short var8;
                     if (var3 == 3) {
                        if (var2 == 3) {
                           var6 = BITRATE_V1_L1[var4 - 1];
                        } else {
                           var6 = BITRATE_V2_L1[var4 - 1];
                        }

                        var6 = (var6 * 12 / var5 + var7) * 4;
                        var8 = 384;
                     } else {
                        var8 = 144;
                        if (var2 == 3) {
                           if (var3 == 2) {
                              var6 = BITRATE_V1_L2[var4 - 1];
                           } else {
                              var6 = BITRATE_V1_L3[var4 - 1];
                           }

                           var6 = var6 * 144 / var5 + var7;
                           var8 = 1152;
                        } else {
                           var4 = BITRATE_V2[var4 - 1];
                           if (var3 == 1) {
                              var11 = 576;
                           }

                           if (var3 == 1) {
                              var8 = 72;
                           }

                           var7 += var8 * var4 / var5;
                           var8 = var11;
                           var6 = var7;
                        }
                     }

                     var7 = var6 * 8 * var5 / var8;
                     String var9 = MIME_TYPE_BY_LAYER[3 - var3];
                     byte var10;
                     if ((var0 >> 6 & 3) == 3) {
                        var10 = 1;
                     } else {
                        var10 = 2;
                     }

                     var1.setValues(var2, var9, var6, var5, var10, var7, var8);
                     return true;
                  }
               } else {
                  return false;
               }
            }
         }
      }
   }

   private void setValues(int var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      this.version = var1;
      this.mimeType = var2;
      this.frameSize = var3;
      this.sampleRate = var4;
      this.channels = var5;
      this.bitrate = var6;
      this.samplesPerFrame = var7;
   }
}
