package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;

public abstract class UPCEANReader extends OneDReader {
   static final int[] END_PATTERN = new int[]{1, 1, 1, 1, 1, 1};
   static final int[][] L_AND_G_PATTERNS = new int[20][];
   static final int[][] L_PATTERNS = new int[][]{{3, 2, 1, 1}, {2, 2, 2, 1}, {2, 1, 2, 2}, {1, 4, 1, 1}, {1, 1, 3, 2}, {1, 2, 3, 1}, {1, 1, 1, 4}, {1, 3, 1, 2}, {1, 2, 1, 3}, {3, 1, 1, 2}};
   private static final float MAX_AVG_VARIANCE = 0.48F;
   private static final float MAX_INDIVIDUAL_VARIANCE = 0.7F;
   static final int[] MIDDLE_PATTERN = new int[]{1, 1, 1, 1, 1};
   static final int[] START_END_PATTERN = new int[]{1, 1, 1};
   private final StringBuilder decodeRowStringBuffer = new StringBuilder(20);
   private final EANManufacturerOrgSupport eanManSupport = new EANManufacturerOrgSupport();
   private final UPCEANExtensionSupport extensionReader = new UPCEANExtensionSupport();

   static {
      System.arraycopy(L_PATTERNS, 0, L_AND_G_PATTERNS, 0, 10);

      for(int var0 = 10; var0 < 20; ++var0) {
         int[] var1 = L_PATTERNS[var0 - 10];
         int[] var2 = new int[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var1.length - var3 - 1];
         }

         L_AND_G_PATTERNS[var0] = var2;
      }

   }

   protected UPCEANReader() {
   }

   static boolean checkStandardUPCEANChecksum(CharSequence var0) throws FormatException {
      boolean var1 = false;
      int var2 = var0.length();
      if (var2 != 0) {
         int var3 = 0;
         int var4 = var2 - 2;

         while(true) {
            if (var4 < 0) {
               var3 *= 3;

               for(var4 = var2 - 1; var4 >= 0; var4 -= 2) {
                  var2 = var0.charAt(var4) - 48;
                  if (var2 < 0 || var2 > 9) {
                     throw FormatException.getFormatInstance();
                  }

                  var3 += var2;
               }

               if (var3 % 10 == 0) {
                  var1 = true;
               }
               break;
            }

            int var5 = var0.charAt(var4) - 48;
            if (var5 < 0 || var5 > 9) {
               throw FormatException.getFormatInstance();
            }

            var3 += var5;
            var4 -= 2;
         }
      }

      return var1;
   }

   static int decodeDigit(BitArray var0, int[] var1, int var2, int[][] var3) throws NotFoundException {
      recordPattern(var0, var2, var1);
      float var4 = 0.48F;
      int var5 = -1;
      int var6 = var3.length;

      float var8;
      for(var2 = 0; var2 < var6; var4 = var8) {
         float var7 = patternMatchVariance(var1, var3[var2], 0.7F);
         var8 = var4;
         if (var7 < var4) {
            var8 = var7;
            var5 = var2;
         }

         ++var2;
      }

      if (var5 >= 0) {
         return var5;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   static int[] findGuardPattern(BitArray var0, int var1, boolean var2, int[] var3) throws NotFoundException {
      return findGuardPattern(var0, var1, var2, var3, new int[var3.length]);
   }

   private static int[] findGuardPattern(BitArray var0, int var1, boolean var2, int[] var3, int[] var4) throws NotFoundException {
      int var5 = var0.getSize();
      if (var2) {
         var1 = var0.getNextUnset(var1);
      } else {
         var1 = var0.getNextSet(var1);
      }

      byte var6 = 0;
      int var8 = var3.length;
      int var9 = var1;
      var1 = var1;

      for(int var7 = var6; var9 < var5; ++var9) {
         if (var0.get(var9) ^ var2) {
            int var10002 = var4[var7]++;
         } else {
            if (var7 == var8 - 1) {
               if (patternMatchVariance(var4, var3, 0.7F) < 0.48F) {
                  return new int[]{var1, var9};
               }

               var1 += var4[0] + var4[1];
               System.arraycopy(var4, 2, var4, 0, var8 - 2);
               var4[var8 - 2] = 0;
               var4[var8 - 1] = 0;
               --var7;
            } else {
               ++var7;
            }

            var4[var7] = 1;
            if (!var2) {
               var2 = true;
            } else {
               var2 = false;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   static int[] findStartGuardPattern(BitArray var0) throws NotFoundException {
      boolean var1 = false;
      int[] var2 = null;
      int var3 = 0;
      int[] var4 = new int[START_END_PATTERN.length];

      while(!var1) {
         Arrays.fill(var4, 0, START_END_PATTERN.length, 0);
         int[] var5 = findGuardPattern(var0, var3, false, START_END_PATTERN, var4);
         int var6 = var5[0];
         int var7 = var5[1];
         int var8 = var6 - (var7 - var6);
         var3 = var7;
         var2 = var5;
         if (var8 >= 0) {
            var1 = var0.isRange(var8, var6, false);
            var3 = var7;
            var2 = var5;
         }
      }

      return var2;
   }

   boolean checkChecksum(String var1) throws FormatException {
      return checkStandardUPCEANChecksum(var1);
   }

   int[] decodeEnd(BitArray var1, int var2) throws NotFoundException {
      return findGuardPattern(var1, var2, false, START_END_PATTERN);
   }

   protected abstract int decodeMiddle(BitArray var1, int[] var2, StringBuilder var3) throws NotFoundException;

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, ChecksumException, FormatException {
      return this.decodeRow(var1, var2, findStartGuardPattern(var2), var3);
   }

   public Result decodeRow(int var1, BitArray var2, int[] var3, Map var4) throws NotFoundException, ChecksumException, FormatException {
      ResultPointCallback var5;
      if (var4 == null) {
         var5 = null;
      } else {
         var5 = (ResultPointCallback)var4.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      }

      if (var5 != null) {
         var5.foundPossibleResultPoint(new ResultPoint((float)(var3[0] + var3[1]) / 2.0F, (float)var1));
      }

      StringBuilder var6 = this.decodeRowStringBuffer;
      var6.setLength(0);
      int var7 = this.decodeMiddle(var2, var3, var6);
      if (var5 != null) {
         var5.foundPossibleResultPoint(new ResultPoint((float)var7, (float)var1));
      }

      int[] var8 = this.decodeEnd(var2, var7);
      if (var5 != null) {
         var5.foundPossibleResultPoint(new ResultPoint((float)(var8[0] + var8[1]) / 2.0F, (float)var1));
      }

      var7 = var8[1];
      int var9 = var7 + (var7 - var8[0]);
      if (var9 < var2.getSize() && var2.isRange(var7, var9, false)) {
         String var19 = var6.toString();
         if (var19.length() < 8) {
            throw FormatException.getFormatInstance();
         } else if (!this.checkChecksum(var19)) {
            throw ChecksumException.getChecksumInstance();
         } else {
            float var10 = (float)(var3[1] + var3[0]) / 2.0F;
            float var11 = (float)(var8[1] + var8[0]) / 2.0F;
            BarcodeFormat var20 = this.getBarcodeFormat();
            Result var17 = new Result(var19, (byte[])null, new ResultPoint[]{new ResultPoint(var10, (float)var1), new ResultPoint(var11, (float)var1)}, var20);
            byte var21 = 0;

            try {
               Result var15 = this.extensionReader.decodeRow(var1, var2, var8[1]);
               var17.putMetadata(ResultMetadataType.UPC_EAN_EXTENSION, var15.getText());
               var17.putAllMetadata(var15.getResultMetadata());
               var17.addResultPoints(var15.getResultPoints());
               var1 = var15.getText().length();
            } catch (ReaderException var14) {
               var1 = var21;
            }

            int[] var16;
            if (var4 == null) {
               var16 = null;
            } else {
               var16 = (int[])var4.get(DecodeHintType.ALLOWED_EAN_EXTENSIONS);
            }

            if (var16 != null) {
               boolean var12 = false;
               int var13 = var16.length;
               var7 = 0;

               boolean var22;
               while(true) {
                  var22 = var12;
                  if (var7 >= var13) {
                     break;
                  }

                  if (var1 == var16[var7]) {
                     var22 = true;
                     break;
                  }

                  ++var7;
               }

               if (!var22) {
                  throw NotFoundException.getNotFoundInstance();
               }
            }

            if (var20 == BarcodeFormat.EAN_13 || var20 == BarcodeFormat.UPC_A) {
               String var18 = this.eanManSupport.lookupCountryIdentifier(var19);
               if (var18 != null) {
                  var17.putMetadata(ResultMetadataType.POSSIBLE_COUNTRY, var18);
               }
            }

            return var17;
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   abstract BarcodeFormat getBarcodeFormat();
}
