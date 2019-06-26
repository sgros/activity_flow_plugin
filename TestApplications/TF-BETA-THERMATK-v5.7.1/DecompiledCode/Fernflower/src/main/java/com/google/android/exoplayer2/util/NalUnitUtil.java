package com.google.android.exoplayer2.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class NalUnitUtil {
   public static final float[] ASPECT_RATIO_IDC_VALUES = new float[]{1.0F, 1.0F, 1.0909091F, 0.90909094F, 1.4545455F, 1.2121212F, 2.1818182F, 1.8181819F, 2.909091F, 2.4242425F, 1.6363636F, 1.3636364F, 1.939394F, 1.6161616F, 1.3333334F, 1.5F, 2.0F};
   public static final int EXTENDED_SAR = 255;
   private static final int H264_NAL_UNIT_TYPE_SEI = 6;
   private static final int H264_NAL_UNIT_TYPE_SPS = 7;
   private static final int H265_NAL_UNIT_TYPE_PREFIX_SEI = 39;
   public static final byte[] NAL_START_CODE = new byte[]{0, 0, 0, 1};
   private static final String TAG = "NalUnitUtil";
   private static int[] scratchEscapePositions = new int[10];
   private static final Object scratchEscapePositionsLock = new Object();

   private NalUnitUtil() {
   }

   public static void clearPrefixFlags(boolean[] var0) {
      var0[0] = false;
      var0[1] = false;
      var0[2] = false;
   }

   public static void discardToSps(ByteBuffer var0) {
      int var1 = var0.position();
      int var2 = 0;
      int var3 = 0;

      while(true) {
         int var4 = var2 + 1;
         if (var4 >= var1) {
            var0.clear();
            return;
         }

         int var5 = var0.get(var2) & 255;
         int var6;
         if (var3 == 3) {
            var6 = var3;
            if (var5 == 1) {
               var6 = var3;
               if ((var0.get(var4) & 31) == 7) {
                  ByteBuffer var7 = var0.duplicate();
                  var7.position(var2 - 3);
                  var7.limit(var1);
                  var0.position(0);
                  var0.put(var7);
                  return;
               }
            }
         } else {
            var6 = var3;
            if (var5 == 0) {
               var6 = var3 + 1;
            }
         }

         var3 = var6;
         if (var5 != 0) {
            var3 = 0;
         }

         var2 = var4;
      }
   }

   public static int findNalUnit(byte[] var0, int var1, int var2, boolean[] var3) {
      int var4 = var2 - var1;
      boolean var5 = false;
      boolean var6;
      if (var4 >= 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      Assertions.checkState(var6);
      if (var4 == 0) {
         return var2;
      } else {
         if (var3 != null) {
            if (var3[0]) {
               clearPrefixFlags(var3);
               return var1 - 3;
            }

            if (var4 > 1 && var3[1] && var0[var1] == 1) {
               clearPrefixFlags(var3);
               return var1 - 2;
            }

            if (var4 > 2 && var3[2] && var0[var1] == 0 && var0[var1 + 1] == 1) {
               clearPrefixFlags(var3);
               return var1 - 1;
            }
         }

         int var7 = var2 - 1;

         for(var1 += 2; var1 < var7; var1 += 3) {
            if ((var0[var1] & 254) == 0) {
               int var8 = var1 - 2;
               if (var0[var8] == 0 && var0[var1 - 1] == 0 && var0[var1] == 1) {
                  if (var3 != null) {
                     clearPrefixFlags(var3);
                  }

                  return var8;
               }

               var1 -= 2;
            }
         }

         if (var3 != null) {
            label114: {
               label113: {
                  if (var4 > 2) {
                     if (var0[var2 - 3] != 0 || var0[var2 - 2] != 0 || var0[var7] != 1) {
                        break label113;
                     }
                  } else if (var4 == 2) {
                     if (!var3[2] || var0[var2 - 2] != 0 || var0[var7] != 1) {
                        break label113;
                     }
                  } else if (!var3[1] || var0[var7] != 1) {
                     break label113;
                  }

                  var6 = true;
                  break label114;
               }

               var6 = false;
            }

            label94: {
               label93: {
                  var3[0] = var6;
                  if (var4 > 1) {
                     if (var0[var2 - 2] == 0 && var0[var7] == 0) {
                        break label93;
                     }
                  } else if (var3[2] && var0[var7] == 0) {
                     break label93;
                  }

                  var6 = false;
                  break label94;
               }

               var6 = true;
            }

            var3[1] = var6;
            var6 = var5;
            if (var0[var7] == 0) {
               var6 = true;
            }

            var3[2] = var6;
         }

         return var2;
      }
   }

   private static int findNextUnescapeIndex(byte[] var0, int var1, int var2) {
      while(var1 < var2 - 2) {
         if (var0[var1] == 0 && var0[var1 + 1] == 0 && var0[var1 + 2] == 3) {
            return var1;
         }

         ++var1;
      }

      return var2;
   }

   public static int getH265NalUnitType(byte[] var0, int var1) {
      return (var0[var1 + 3] & 126) >> 1;
   }

   public static int getNalUnitType(byte[] var0, int var1) {
      return var0[var1 + 3] & 31;
   }

   public static boolean isNalUnitSei(String var0, byte var1) {
      boolean var2 = "video/avc".equals(var0);
      boolean var3 = true;
      if (var2) {
         var2 = var3;
         if ((var1 & 31) == 6) {
            return var2;
         }
      }

      if ("video/hevc".equals(var0) && (var1 & 126) >> 1 == 39) {
         var2 = var3;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static NalUnitUtil.PpsData parsePpsNalUnit(byte[] var0, int var1, int var2) {
      ParsableNalUnitBitArray var3 = new ParsableNalUnitBitArray(var0, var1, var2);
      var3.skipBits(8);
      var2 = var3.readUnsignedExpGolombCodedInt();
      var1 = var3.readUnsignedExpGolombCodedInt();
      var3.skipBit();
      return new NalUnitUtil.PpsData(var2, var1, var3.readBit());
   }

   public static NalUnitUtil.SpsData parseSpsNalUnit(byte[] var0, int var1, int var2) {
      ParsableNalUnitBitArray var25 = new ParsableNalUnitBitArray(var0, var1, var2);
      var25.skipBits(8);
      int var3 = var25.readBits(8);
      int var4 = var25.readBits(8);
      int var5 = var25.readBits(8);
      int var6 = var25.readUnsignedExpGolombCodedInt();
      byte var7 = 1;
      int var8;
      boolean var9;
      int var10;
      boolean var11;
      byte var28;
      if (var3 != 100 && var3 != 110 && var3 != 122 && var3 != 244 && var3 != 44 && var3 != 83 && var3 != 86 && var3 != 118 && var3 != 128 && var3 != 138) {
         var8 = 1;
         var9 = false;
      } else {
         var10 = var25.readUnsignedExpGolombCodedInt();
         if (var10 == 3) {
            var11 = var25.readBit();
         } else {
            var11 = false;
         }

         var25.readUnsignedExpGolombCodedInt();
         var25.readUnsignedExpGolombCodedInt();
         var25.skipBit();
         var8 = var10;
         var9 = var11;
         if (var25.readBit()) {
            if (var10 != 3) {
               var28 = 8;
            } else {
               var28 = 12;
            }

            var2 = 0;

            while(true) {
               var8 = var10;
               var9 = var11;
               if (var2 >= var28) {
                  break;
               }

               if (var25.readBit()) {
                  byte var30;
                  if (var2 < 6) {
                     var30 = 16;
                  } else {
                     var30 = 64;
                  }

                  skipScalingList(var25, var30);
               }

               ++var2;
            }
         }
      }

      int var12;
      int var13;
      label102: {
         var12 = var25.readUnsignedExpGolombCodedInt();
         var13 = var25.readUnsignedExpGolombCodedInt();
         if (var13 == 0) {
            var1 = var25.readUnsignedExpGolombCodedInt() + 4;
         } else {
            if (var13 == 1) {
               var11 = var25.readBit();
               var25.readSignedExpGolombCodedInt();
               var25.readSignedExpGolombCodedInt();
               long var14 = (long)var25.readUnsignedExpGolombCodedInt();

               for(var1 = 0; (long)var1 < var14; ++var1) {
                  var25.readUnsignedExpGolombCodedInt();
               }

               var2 = 0;
               break label102;
            }

            var1 = 0;
         }

         var11 = false;
         var2 = var1;
      }

      var25.readUnsignedExpGolombCodedInt();
      var25.skipBit();
      var10 = var25.readUnsignedExpGolombCodedInt();
      var1 = var25.readUnsignedExpGolombCodedInt();
      byte var16 = var25.readBit();
      if (var16 == 0) {
         var25.skipBit();
      }

      var25.skipBit();
      int var17 = (var10 + 1) * 16;
      int var18 = (2 - var16) * (var1 + 1) * 16;
      var10 = var17;
      var1 = var18;
      if (var25.readBit()) {
         int var19 = var25.readUnsignedExpGolombCodedInt();
         int var20 = var25.readUnsignedExpGolombCodedInt();
         int var21 = var25.readUnsignedExpGolombCodedInt();
         int var22 = var25.readUnsignedExpGolombCodedInt();
         if (var8 == 0) {
            var8 = 2 - var16;
            var28 = 1;
         } else {
            if (var8 == 3) {
               var28 = 1;
            } else {
               var28 = 2;
            }

            byte var31 = var7;
            if (var8 == 1) {
               var31 = 2;
            }

            var8 = var31 * (2 - var16);
         }

         var10 = var17 - (var19 + var20) * var28;
         var1 = var18 - (var21 + var22) * var8;
      }

      float var23 = 1.0F;
      float var24;
      if (var25.readBit() && var25.readBit()) {
         var8 = var25.readBits(8);
         if (var8 == 255) {
            var8 = var25.readBits(16);
            int var29 = var25.readBits(16);
            var24 = var23;
            if (var8 != 0) {
               var24 = var23;
               if (var29 != 0) {
                  var24 = (float)var8 / (float)var29;
                  return new NalUnitUtil.SpsData(var3, var4, var5, var6, var10, var1, var24, var9, (boolean)var16, var12 + 4, var13, var2, var11);
               }
            }

            return new NalUnitUtil.SpsData(var3, var4, var5, var6, var10, var1, var24, var9, (boolean)var16, var12 + 4, var13, var2, var11);
         }

         float[] var26 = ASPECT_RATIO_IDC_VALUES;
         if (var8 < var26.length) {
            var24 = var26[var8];
            return new NalUnitUtil.SpsData(var3, var4, var5, var6, var10, var1, var24, var9, (boolean)var16, var12 + 4, var13, var2, var11);
         }

         StringBuilder var27 = new StringBuilder();
         var27.append("Unexpected aspect_ratio_idc value: ");
         var27.append(var8);
         Log.w("NalUnitUtil", var27.toString());
      }

      var24 = 1.0F;
      return new NalUnitUtil.SpsData(var3, var4, var5, var6, var10, var1, var24, var9, (boolean)var16, var12 + 4, var13, var2, var11);
   }

   private static void skipScalingList(ParsableNalUnitBitArray var0, int var1) {
      int var2 = 8;
      int var3 = 0;

      int var5;
      for(int var4 = 8; var3 < var1; var2 = var5) {
         var5 = var2;
         if (var2 != 0) {
            var5 = (var0.readSignedExpGolombCodedInt() + var4 + 256) % 256;
         }

         if (var5 != 0) {
            var4 = var5;
         }

         ++var3;
      }

   }

   public static int unescapeStream(byte[] var0, int var1) {
      Object var2 = scratchEscapePositionsLock;
      synchronized(var2){}
      int var3 = 0;
      int var4 = 0;

      while(true) {
         int var5;
         Throwable var10000;
         boolean var10001;
         if (var3 < var1) {
            label454: {
               try {
                  var5 = findNextUnescapeIndex(var0, var3, var1);
               } catch (Throwable var48) {
                  var10000 = var48;
                  var10001 = false;
                  break label454;
               }

               var3 = var5;
               if (var5 >= var1) {
                  continue;
               }

               try {
                  if (scratchEscapePositions.length <= var4) {
                     scratchEscapePositions = Arrays.copyOf(scratchEscapePositions, scratchEscapePositions.length * 2);
                  }
               } catch (Throwable var47) {
                  var10000 = var47;
                  var10001 = false;
                  break label454;
               }

               try {
                  scratchEscapePositions[var4] = var5;
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label454;
               }

               var3 = var5 + 3;
               ++var4;
               continue;
            }
         } else {
            label455: {
               int var6 = var1 - var4;
               var3 = 0;
               var5 = 0;

               for(var1 = 0; var3 < var4; ++var3) {
                  int var7;
                  try {
                     var7 = scratchEscapePositions[var3] - var1;
                     System.arraycopy(var0, var1, var0, var5, var7);
                  } catch (Throwable var50) {
                     var10000 = var50;
                     var10001 = false;
                     break label455;
                  }

                  var5 += var7;
                  int var8 = var5 + 1;
                  var0[var5] = (byte)0;
                  var5 = var8 + 1;
                  var0[var8] = (byte)0;
                  var1 += var7 + 3;
               }

               label439:
               try {
                  System.arraycopy(var0, var1, var0, var5, var6 - var5);
                  return var6;
               } catch (Throwable var49) {
                  var10000 = var49;
                  var10001 = false;
                  break label439;
               }
            }
         }

         while(true) {
            Throwable var51 = var10000;

            try {
               throw var51;
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static final class PpsData {
      public final boolean bottomFieldPicOrderInFramePresentFlag;
      public final int picParameterSetId;
      public final int seqParameterSetId;

      public PpsData(int var1, int var2, boolean var3) {
         this.picParameterSetId = var1;
         this.seqParameterSetId = var2;
         this.bottomFieldPicOrderInFramePresentFlag = var3;
      }
   }

   public static final class SpsData {
      public final int constraintsFlagsAndReservedZero2Bits;
      public final boolean deltaPicOrderAlwaysZeroFlag;
      public final boolean frameMbsOnlyFlag;
      public final int frameNumLength;
      public final int height;
      public final int levelIdc;
      public final int picOrderCntLsbLength;
      public final int picOrderCountType;
      public final float pixelWidthAspectRatio;
      public final int profileIdc;
      public final boolean separateColorPlaneFlag;
      public final int seqParameterSetId;
      public final int width;

      public SpsData(int var1, int var2, int var3, int var4, int var5, int var6, float var7, boolean var8, boolean var9, int var10, int var11, int var12, boolean var13) {
         this.profileIdc = var1;
         this.constraintsFlagsAndReservedZero2Bits = var2;
         this.levelIdc = var3;
         this.seqParameterSetId = var4;
         this.width = var5;
         this.height = var6;
         this.pixelWidthAspectRatio = var7;
         this.separateColorPlaneFlag = var8;
         this.frameMbsOnlyFlag = var9;
         this.frameNumLength = var10;
         this.picOrderCountType = var11;
         this.picOrderCntLsbLength = var12;
         this.deltaPicOrderAlwaysZeroFlag = var13;
      }
   }
}
