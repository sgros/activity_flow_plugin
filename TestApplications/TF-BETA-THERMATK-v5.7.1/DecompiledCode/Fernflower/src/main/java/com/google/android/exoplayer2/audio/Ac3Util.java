package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.List;

public final class Ac3Util {
   private static final int[] BITRATE_BY_HALF_FRMSIZECOD = new int[]{32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, 448, 512, 576, 640};
   private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = new int[]{1, 2, 3, 6};
   private static final int[] CHANNEL_COUNT_BY_ACMOD = new int[]{2, 1, 2, 3, 3, 4, 4, 5};
   private static final int[] SAMPLE_RATE_BY_FSCOD = new int[]{48000, 44100, 32000};
   private static final int[] SAMPLE_RATE_BY_FSCOD2 = new int[]{24000, 22050, 16000};
   private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = new int[]{69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};

   public static int findTrueHdSyncframeOffset(ByteBuffer var0) {
      int var1 = var0.position();
      int var2 = var0.limit();

      for(int var3 = var1; var3 <= var2 - 10; ++var3) {
         if ((var0.getInt(var3 + 4) & -16777217) == -1167101192) {
            return var3 - var1;
         }
      }

      return -1;
   }

   public static int getAc3SyncframeAudioSampleCount() {
      return 1536;
   }

   private static int getAc3SyncframeSize(int var0, int var1) {
      int var2 = var1 / 2;
      if (var0 >= 0) {
         int[] var3 = SAMPLE_RATE_BY_FSCOD;
         if (var0 < var3.length && var1 >= 0) {
            int[] var4 = SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1;
            if (var2 < var4.length) {
               var0 = var3[var0];
               if (var0 == 44100) {
                  return (var4[var2] + var1 % 2) * 2;
               }

               var1 = BITRATE_BY_HALF_FRMSIZECOD[var2];
               if (var0 == 32000) {
                  return var1 * 6;
               }

               return var1 * 4;
            }
         }
      }

      return -1;
   }

   public static Format parseAc3AnnexFFormat(ParsableByteArray var0, String var1, String var2, DrmInitData var3) {
      int var4 = var0.readUnsignedByte();
      int var5 = SAMPLE_RATE_BY_FSCOD[(var4 & 192) >> 6];
      int var6 = var0.readUnsignedByte();
      int var7 = CHANNEL_COUNT_BY_ACMOD[(var6 & 56) >> 3];
      var4 = var7;
      if ((var6 & 4) != 0) {
         var4 = var7 + 1;
      }

      return Format.createAudioSampleFormat(var1, "audio/ac3", (String)null, -1, -1, var4, var5, (List)null, var3, 0, var2);
   }

   public static Ac3Util.SyncFrameInfo parseAc3SyncframeInfo(ParsableBitArray var0) {
      int var1 = var0.getPosition();
      var0.skipBits(40);
      boolean var2;
      if (var0.readBits(5) == 16) {
         var2 = true;
      } else {
         var2 = false;
      }

      var0.setPosition(var1);
      byte var12 = -1;
      int var5;
      int var6;
      int var10;
      String var11;
      int var13;
      byte var16;
      if (var2) {
         var0.skipBits(16);
         var13 = var0.readBits(2);
         byte var14;
         if (var13 != 0) {
            if (var13 != 1) {
               if (var13 != 2) {
                  var14 = var12;
               } else {
                  var14 = 2;
               }
            } else {
               var14 = 1;
            }
         } else {
            var14 = 0;
         }

         var0.skipBits(3);
         int var3 = var0.readBits(11);
         int var4 = var0.readBits(2);
         if (var4 == 3) {
            var1 = SAMPLE_RATE_BY_FSCOD2[var0.readBits(2)];
            var5 = 3;
            var6 = 6;
         } else {
            var5 = var0.readBits(2);
            var6 = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[var5];
            var1 = SAMPLE_RATE_BY_FSCOD[var4];
         }

         int var7 = var0.readBits(3);
         byte var8 = var0.readBit();
         int var9 = CHANNEL_COUNT_BY_ACMOD[var7];
         var0.skipBits(10);
         if (var0.readBit()) {
            var0.skipBits(8);
         }

         if (var7 == 0) {
            var0.skipBits(5);
            if (var0.readBit()) {
               var0.skipBits(8);
            }
         }

         if (var14 == 1 && var0.readBit()) {
            var0.skipBits(16);
         }

         if (var0.readBit()) {
            if (var7 > 2) {
               var0.skipBits(2);
            }

            if ((var7 & 1) != 0 && var7 > 2) {
               var0.skipBits(6);
            }

            if ((var7 & 4) != 0) {
               var0.skipBits(6);
            }

            if (var8 != 0 && var0.readBit()) {
               var0.skipBits(5);
            }

            if (var14 == 0) {
               if (var0.readBit()) {
                  var0.skipBits(6);
               }

               if (var7 == 0 && var0.readBit()) {
                  var0.skipBits(6);
               }

               if (var0.readBit()) {
                  var0.skipBits(6);
               }

               var10 = var0.readBits(2);
               if (var10 == 1) {
                  var0.skipBits(5);
               } else if (var10 == 2) {
                  var0.skipBits(12);
               } else if (var10 == 3) {
                  var10 = var0.readBits(5);
                  if (var0.readBit()) {
                     var0.skipBits(5);
                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        var0.skipBits(4);
                     }

                     if (var0.readBit()) {
                        if (var0.readBit()) {
                           var0.skipBits(4);
                        }

                        if (var0.readBit()) {
                           var0.skipBits(4);
                        }
                     }
                  }

                  if (var0.readBit()) {
                     var0.skipBits(5);
                     if (var0.readBit()) {
                        var0.skipBits(7);
                        if (var0.readBit()) {
                           var0.skipBits(8);
                        }
                     }
                  }

                  var0.skipBits((var10 + 2) * 8);
                  var0.byteAlign();
               }

               if (var7 < 2) {
                  if (var0.readBit()) {
                     var0.skipBits(14);
                  }

                  if (var7 == 0 && var0.readBit()) {
                     var0.skipBits(14);
                  }
               }

               if (var0.readBit()) {
                  if (var5 == 0) {
                     var0.skipBits(5);
                  } else {
                     for(var10 = 0; var10 < var6; ++var10) {
                        if (var0.readBit()) {
                           var0.skipBits(5);
                        }
                     }
                  }
               }
            }
         }

         if (var0.readBit()) {
            var0.skipBits(5);
            if (var7 == 2) {
               var0.skipBits(4);
            }

            if (var7 >= 6) {
               var0.skipBits(2);
            }

            if (var0.readBit()) {
               var0.skipBits(8);
            }

            if (var7 == 0 && var0.readBit()) {
               var0.skipBits(8);
            }

            if (var4 < 3) {
               var0.skipBit();
            }
         }

         if (var14 == 0 && var5 != 3) {
            var0.skipBit();
         }

         if (var14 == 2 && (var5 == 3 || var0.readBit())) {
            var0.skipBits(6);
         }

         if (var0.readBit() && var0.readBits(6) == 1 && var0.readBits(8) == 1) {
            var11 = "audio/eac3-joc";
         } else {
            var11 = "audio/eac3";
         }

         var3 = (var3 + 1) * 2;
         var4 = var6 * 256;
         var10 = var9 + var8;
         var16 = var14;
         var6 = var3;
         var13 = var4;
      } else {
         var0.skipBits(32);
         var13 = var0.readBits(2);
         var6 = getAc3SyncframeSize(var13, var0.readBits(6));
         var0.skipBits(8);
         var5 = var0.readBits(3);
         if ((var5 & 1) != 0 && var5 != 1) {
            var0.skipBits(2);
         }

         if ((var5 & 4) != 0) {
            var0.skipBits(2);
         }

         if (var5 == 2) {
            var0.skipBits(2);
         }

         var1 = SAMPLE_RATE_BY_FSCOD[var13];
         byte var15 = var0.readBit();
         var5 = CHANNEL_COUNT_BY_ACMOD[var5];
         var11 = "audio/ac3";
         var10 = var5 + var15;
         var16 = -1;
         var13 = 1536;
      }

      return new Ac3Util.SyncFrameInfo(var11, var16, var10, var1, var6, var13);
   }

   public static int parseAc3SyncframeSize(byte[] var0) {
      if (var0.length < 6) {
         return -1;
      } else {
         boolean var1;
         if ((var0[5] & 255) >> 3 == 16) {
            var1 = true;
         } else {
            var1 = false;
         }

         if (var1) {
            byte var2 = var0[2];
            return ((var0[3] & 255 | (var2 & 7) << 8) + 1) * 2;
         } else {
            return getAc3SyncframeSize((var0[4] & 192) >> 6, var0[4] & 63);
         }
      }
   }

   public static Format parseEAc3AnnexFFormat(ParsableByteArray var0, String var1, String var2, DrmInitData var3) {
      var0.skipBytes(2);
      int var4 = var0.readUnsignedByte();
      int var5 = SAMPLE_RATE_BY_FSCOD[(var4 & 192) >> 6];
      int var6 = var0.readUnsignedByte();
      int var7 = CHANNEL_COUNT_BY_ACMOD[(var6 & 14) >> 1];
      var4 = var7;
      if ((var6 & 1) != 0) {
         var4 = var7 + 1;
      }

      var7 = var4;
      if ((var0.readUnsignedByte() & 30) >> 1 > 0) {
         var7 = var4;
         if ((2 & var0.readUnsignedByte()) != 0) {
            var7 = var4 + 2;
         }
      }

      String var8;
      if (var0.bytesLeft() > 0 && (var0.readUnsignedByte() & 1) != 0) {
         var8 = "audio/eac3-joc";
      } else {
         var8 = "audio/eac3";
      }

      return Format.createAudioSampleFormat(var1, var8, (String)null, -1, -1, var7, var5, (List)null, var3, 0, var2);
   }

   public static int parseEAc3SyncframeAudioSampleCount(ByteBuffer var0) {
      byte var1 = var0.get(var0.position() + 4);
      int var2 = 6;
      if ((var1 & 192) >> 6 != 3) {
         var2 = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(var0.get(var0.position() + 4) & 48) >> 4];
      }

      return var2 * 256;
   }

   public static int parseTrueHdSyncframeAudioSampleCount(ByteBuffer var0, int var1) {
      boolean var2;
      if ((var0.get(var0.position() + var1 + 7) & 255) == 187) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = var0.position();
      byte var4;
      if (var2) {
         var4 = 9;
      } else {
         var4 = 8;
      }

      return 40 << (var0.get(var3 + var1 + var4) >> 4 & 7);
   }

   public static int parseTrueHdSyncframeAudioSampleCount(byte[] var0) {
      byte var1 = var0[4];
      boolean var2 = false;
      if (var1 == -8 && var0[5] == 114 && var0[6] == 111 && (var0[7] & 254) == 186) {
         if ((var0[7] & 255) == 187) {
            var2 = true;
         }

         byte var3;
         if (var2) {
            var3 = 9;
         } else {
            var3 = 8;
         }

         return 40 << (var0[var3] >> 4 & 7);
      } else {
         return 0;
      }
   }

   public static final class SyncFrameInfo {
      public final int channelCount;
      public final int frameSize;
      public final String mimeType;
      public final int sampleCount;
      public final int sampleRate;
      public final int streamType;

      private SyncFrameInfo(String var1, int var2, int var3, int var4, int var5, int var6) {
         this.mimeType = var1;
         this.streamType = var2;
         this.channelCount = var3;
         this.sampleRate = var4;
         this.frameSize = var5;
         this.sampleCount = var6;
      }

      // $FF: synthetic method
      SyncFrameInfo(String var1, int var2, int var3, int var4, int var5, int var6, Object var7) {
         this(var1, var2, var3, var4, var5, var6);
      }
   }
}
