package com.google.android.exoplayer2.util;

import android.util.Pair;
import com.google.android.exoplayer2.ParserException;
import java.util.ArrayList;

public final class CodecSpecificDataUtil {
   private static final int AUDIO_OBJECT_TYPE_AAC_LC = 2;
   private static final int AUDIO_OBJECT_TYPE_ER_BSAC = 22;
   private static final int AUDIO_OBJECT_TYPE_ESCAPE = 31;
   private static final int AUDIO_OBJECT_TYPE_PS = 29;
   private static final int AUDIO_OBJECT_TYPE_SBR = 5;
   private static final int AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID = -1;
   private static final int[] AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = new int[]{0, 1, 2, 3, 4, 5, 6, 8, -1, -1, -1, 7, 8, -1, 8, -1};
   private static final int AUDIO_SPECIFIC_CONFIG_FREQUENCY_INDEX_ARBITRARY = 15;
   private static final int[] AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = new int[]{96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350};
   private static final byte[] NAL_START_CODE = new byte[]{0, 0, 0, 1};

   private CodecSpecificDataUtil() {
   }

   public static byte[] buildAacAudioSpecificConfig(int var0, int var1, int var2) {
      return new byte[]{(byte)(var0 << 3 & 248 | var1 >> 1 & 7), (byte)(var1 << 7 & 128 | var2 << 3 & 120)};
   }

   public static byte[] buildAacLcAudioSpecificConfig(int var0, int var1) {
      byte var2 = 0;
      int var3 = 0;
      int var4 = -1;

      while(true) {
         int[] var5 = AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE;
         if (var3 >= var5.length) {
            int var6 = -1;
            var3 = var2;

            while(true) {
               var5 = AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE;
               if (var3 >= var5.length) {
                  if (var0 != -1 && var6 != -1) {
                     return buildAacAudioSpecificConfig(2, var4, var6);
                  }

                  StringBuilder var7 = new StringBuilder();
                  var7.append("Invalid sample rate or number of channels: ");
                  var7.append(var0);
                  var7.append(", ");
                  var7.append(var1);
                  throw new IllegalArgumentException(var7.toString());
               }

               if (var1 == var5[var3]) {
                  var6 = var3;
               }

               ++var3;
            }
         }

         if (var0 == var5[var3]) {
            var4 = var3;
         }

         ++var3;
      }
   }

   public static String buildAvcCodecString(int var0, int var1, int var2) {
      return String.format("avc1.%02X%02X%02X", var0, var1, var2);
   }

   public static byte[] buildNalUnit(byte[] var0, int var1, int var2) {
      byte[] var3 = NAL_START_CODE;
      byte[] var4 = new byte[var3.length + var2];
      System.arraycopy(var3, 0, var4, 0, var3.length);
      System.arraycopy(var0, var1, var4, NAL_START_CODE.length, var2);
      return var4;
   }

   private static int findNalStartCode(byte[] var0, int var1) {
      int var2 = var0.length;

      for(int var3 = NAL_START_CODE.length; var1 <= var2 - var3; ++var1) {
         if (isNalStartCode(var0, var1)) {
            return var1;
         }
      }

      return -1;
   }

   private static int getAacAudioObjectType(ParsableBitArray var0) {
      int var1 = var0.readBits(5);
      int var2 = var1;
      if (var1 == 31) {
         var2 = var0.readBits(6) + 32;
      }

      return var2;
   }

   private static int getAacSamplingFrequency(ParsableBitArray var0) {
      int var1 = var0.readBits(4);
      if (var1 == 15) {
         var1 = var0.readBits(24);
      } else {
         boolean var2;
         if (var1 < 13) {
            var2 = true;
         } else {
            var2 = false;
         }

         Assertions.checkArgument(var2);
         var1 = AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[var1];
      }

      return var1;
   }

   private static boolean isNalStartCode(byte[] var0, int var1) {
      if (var0.length - var1 <= NAL_START_CODE.length) {
         return false;
      } else {
         int var2 = 0;

         while(true) {
            byte[] var3 = NAL_START_CODE;
            if (var2 >= var3.length) {
               return true;
            }

            if (var0[var1 + var2] != var3[var2]) {
               return false;
            }

            ++var2;
         }
      }
   }

   public static Pair parseAacAudioSpecificConfig(ParsableBitArray var0, boolean var1) throws ParserException {
      int var3;
      int var5;
      int var6;
      label56: {
         int var2 = getAacAudioObjectType(var0);
         var3 = getAacSamplingFrequency(var0);
         int var4 = var0.readBits(4);
         if (var2 != 5) {
            var5 = var2;
            var6 = var4;
            if (var2 != 29) {
               break label56;
            }
         }

         var2 = getAacSamplingFrequency(var0);
         int var7 = getAacAudioObjectType(var0);
         var5 = var7;
         var3 = var2;
         var6 = var4;
         if (var7 == 22) {
            var6 = var0.readBits(4);
            var3 = var2;
            var5 = var7;
         }
      }

      boolean var8 = true;
      if (var1) {
         StringBuilder var9;
         if (var5 != 1 && var5 != 2 && var5 != 3 && var5 != 4 && var5 != 6 && var5 != 7 && var5 != 17) {
            switch(var5) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
               break;
            default:
               var9 = new StringBuilder();
               var9.append("Unsupported audio object type: ");
               var9.append(var5);
               throw new ParserException(var9.toString());
            }
         }

         parseGaSpecificConfig(var0, var5, var6);
         switch(var5) {
         case 17:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
            var5 = var0.readBits(2);
            if (var5 == 2 || var5 == 3) {
               var9 = new StringBuilder();
               var9.append("Unsupported epConfig: ");
               var9.append(var5);
               throw new ParserException(var9.toString());
            }
         case 18:
         }
      }

      var5 = AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[var6];
      if (var5 != -1) {
         var1 = var8;
      } else {
         var1 = false;
      }

      Assertions.checkArgument(var1);
      return Pair.create(var3, var5);
   }

   public static Pair parseAacAudioSpecificConfig(byte[] var0) throws ParserException {
      return parseAacAudioSpecificConfig(new ParsableBitArray(var0), false);
   }

   private static void parseGaSpecificConfig(ParsableBitArray var0, int var1, int var2) {
      var0.skipBits(1);
      if (var0.readBit()) {
         var0.skipBits(14);
      }

      boolean var3 = var0.readBit();
      if (var2 == 0) {
         throw new UnsupportedOperationException();
      } else {
         if (var1 == 6 || var1 == 20) {
            var0.skipBits(3);
         }

         if (var3) {
            if (var1 == 22) {
               var0.skipBits(16);
            }

            if (var1 == 17 || var1 == 19 || var1 == 20 || var1 == 23) {
               var0.skipBits(3);
            }

            var0.skipBits(1);
         }

      }
   }

   public static byte[][] splitNalUnits(byte[] var0) {
      if (!isNalStartCode(var0, 0)) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         int var2 = 0;

         int var3;
         do {
            var1.add(var2);
            var3 = findNalStartCode(var0, var2 + NAL_START_CODE.length);
            var2 = var3;
         } while(var3 != -1);

         byte[][] var4 = new byte[var1.size()][];

         for(var2 = 0; var2 < var1.size(); ++var2) {
            int var5 = (Integer)var1.get(var2);
            if (var2 < var1.size() - 1) {
               var3 = (Integer)var1.get(var2 + 1);
            } else {
               var3 = var0.length;
            }

            byte[] var6 = new byte[var3 - var5];
            System.arraycopy(var0, var5, var6, 0, var6.length);
            var4[var2] = var6;
         }

         return var4;
      }
   }
}
