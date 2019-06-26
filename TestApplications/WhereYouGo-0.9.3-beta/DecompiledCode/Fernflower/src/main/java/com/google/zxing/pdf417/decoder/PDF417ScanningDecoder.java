package com.google.zxing.pdf417.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.pdf417.PDF417Common;
import com.google.zxing.pdf417.decoder.ec.ErrorCorrection;
import java.util.ArrayList;
import java.util.Formatter;

public final class PDF417ScanningDecoder {
   private static final int CODEWORD_SKEW_SIZE = 2;
   private static final int MAX_EC_CODEWORDS = 512;
   private static final int MAX_ERRORS = 3;
   private static final ErrorCorrection errorCorrection = new ErrorCorrection();

   private PDF417ScanningDecoder() {
   }

   private static BoundingBox adjustBoundingBox(DetectionResultRowIndicatorColumn var0) throws NotFoundException {
      BoundingBox var1 = null;
      if (var0 != null) {
         int[] var2 = var0.getRowHeights();
         if (var2 != null) {
            int var3 = getMax(var2);
            int var4 = 0;
            int var5 = var2.length;
            int var6 = 0;

            int var7;
            while(true) {
               var7 = var4;
               if (var6 >= var5) {
                  break;
               }

               int var8 = var2[var6];
               var4 += var3 - var8;
               var7 = var4;
               if (var8 > 0) {
                  break;
               }

               ++var6;
            }

            Codeword[] var9 = var0.getCodewords();
            var4 = 0;

            for(var6 = var7; var6 > 0 && var9[var4] == null; ++var4) {
               --var6;
            }

            var4 = 0;
            var5 = var2.length - 1;

            while(true) {
               var7 = var4;
               if (var5 < 0) {
                  break;
               }

               var4 += var3 - var2[var5];
               var7 = var4;
               if (var2[var5] > 0) {
                  break;
               }

               --var5;
            }

            for(var4 = var9.length - 1; var7 > 0 && var9[var4] == null; --var4) {
               --var7;
            }

            var1 = var0.getBoundingBox().addMissingRows(var6, var7, var0.isLeft());
         }
      }

      return var1;
   }

   private static void adjustCodewordCount(DetectionResult var0, BarcodeValue[][] var1) throws NotFoundException {
      int[] var2 = var1[0][1].getValue();
      int var3 = var0.getBarcodeColumnCount() * var0.getBarcodeRowCount() - getNumberOfECCodeWords(var0.getBarcodeECLevel());
      if (var2.length == 0) {
         if (var3 <= 0 || var3 > 928) {
            throw NotFoundException.getNotFoundInstance();
         }

         var1[0][1].setValue(var3);
      } else if (var2[0] != var3) {
         var1[0][1].setValue(var3);
      }

   }

   private static int adjustCodewordStartColumn(BitMatrix var0, int var1, int var2, boolean var3, int var4, int var5) {
      byte var7;
      if (var3) {
         var7 = -1;
      } else {
         var7 = 1;
      }

      int var8 = 0;
      int var9 = var7;

      int var10;
      for(var10 = var4; var8 < 2; ++var8) {
         while(true) {
            if (var3) {
               if (var10 < var1) {
                  break;
               }
            } else if (var10 >= var2) {
               break;
            }

            if (var3 != var0.get(var10, var5)) {
               break;
            }

            if (Math.abs(var4 - var10) > 2) {
               return var4;
            }

            var10 += var9;
         }

         var9 = -var9;
         if (!var3) {
            var3 = true;
         } else {
            var3 = false;
         }
      }

      var4 = var10;
      return var4;
   }

   private static boolean checkCodewordSkew(int var0, int var1, int var2) {
      boolean var3;
      if (var1 - 2 <= var0 && var0 <= var2 + 2) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private static int correctErrors(int[] var0, int[] var1, int var2) throws ChecksumException {
      if ((var1 == null || var1.length <= var2 / 2 + 3) && var2 >= 0 && var2 <= 512) {
         return errorCorrection.decode(var0, var2, var1);
      } else {
         throw ChecksumException.getChecksumInstance();
      }
   }

   private static BarcodeValue[][] createBarcodeMatrix(DetectionResult var0) {
      BarcodeValue[][] var1 = new BarcodeValue[var0.getBarcodeRowCount()][var0.getBarcodeColumnCount() + 2];

      int var2;
      int var3;
      for(var2 = 0; var2 < var1.length; ++var2) {
         for(var3 = 0; var3 < var1[var2].length; ++var3) {
            var1[var2][var3] = new BarcodeValue();
         }
      }

      var3 = 0;
      DetectionResultColumn[] var10 = var0.getDetectionResultColumns();
      int var4 = var10.length;

      for(var2 = 0; var2 < var4; ++var2) {
         DetectionResultColumn var5 = var10[var2];
         if (var5 != null) {
            Codeword[] var11 = var5.getCodewords();
            int var6 = var11.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Codeword var8 = var11[var7];
               if (var8 != null) {
                  int var9 = var8.getRowNumber();
                  if (var9 >= 0 && var9 < var1.length) {
                     var1[var9][var3].setValue(var8.getValue());
                  }
               }
            }
         }

         ++var3;
      }

      return var1;
   }

   private static DecoderResult createDecoderResult(DetectionResult var0) throws FormatException, ChecksumException, NotFoundException {
      BarcodeValue[][] var1 = createBarcodeMatrix(var0);
      adjustCodewordCount(var0, var1);
      ArrayList var2 = new ArrayList();
      int[] var3 = new int[var0.getBarcodeRowCount() * var0.getBarcodeColumnCount()];
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      int var6;
      for(var6 = 0; var6 < var0.getBarcodeRowCount(); ++var6) {
         for(int var7 = 0; var7 < var0.getBarcodeColumnCount(); ++var7) {
            int[] var8 = var1[var6][var7 + 1].getValue();
            int var9 = var0.getBarcodeColumnCount() * var6 + var7;
            if (var8.length == 0) {
               var2.add(var9);
            } else if (var8.length == 1) {
               var3[var9] = var8[0];
            } else {
               var5.add(var9);
               var4.add(var8);
            }
         }
      }

      int[][] var10 = new int[var4.size()][];

      for(var6 = 0; var6 < var10.length; ++var6) {
         var10[var6] = (int[])var4.get(var6);
      }

      return createDecoderResultFromAmbiguousValues(var0.getBarcodeECLevel(), var3, PDF417Common.toIntArray(var2), PDF417Common.toIntArray(var5), var10);
   }

   private static DecoderResult createDecoderResultFromAmbiguousValues(int var0, int[] var1, int[] var2, int[] var3, int[][] var4) throws FormatException, ChecksumException {
      int[] var5 = new int[var3.length];
      int var6 = 100;

      label45:
      while(true) {
         int var7 = var6 - 1;
         if (var6 <= 0) {
            throw ChecksumException.getChecksumInstance();
         }

         for(var6 = 0; var6 < var5.length; ++var6) {
            var1[var3[var6]] = var4[var6][var5[var6]];
         }

         try {
            DecoderResult var8 = decodeCodewords(var1, var0, var2);
            return var8;
         } catch (ChecksumException var9) {
            if (var5.length == 0) {
               throw ChecksumException.getChecksumInstance();
            }

            for(var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6] < var4[var6].length - 1) {
                  int var10002 = var5[var6]++;
                  var6 = var7;
                  continue label45;
               }

               var5[var6] = 0;
               if (var6 == var5.length - 1) {
                  throw ChecksumException.getChecksumInstance();
               }
            }

            var6 = var7;
         }
      }
   }

   public static DecoderResult decode(BitMatrix var0, ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5, int var6) throws NotFoundException, FormatException, ChecksumException {
      BoundingBox var7 = new BoundingBox(var0, var1, var2, var3, var4);
      DetectionResultRowIndicatorColumn var25 = null;
      DetectionResultRowIndicatorColumn var23 = null;
      DetectionResult var8 = null;
      int var9 = 0;

      DetectionResultRowIndicatorColumn var10;
      DetectionResultRowIndicatorColumn var11;
      while(true) {
         var10 = var25;
         var11 = var23;
         if (var9 >= 2) {
            break;
         }

         if (var1 != null) {
            var25 = getRowIndicatorColumn(var0, var7, var1, true, var5, var6);
         }

         if (var3 != null) {
            var23 = getRowIndicatorColumn(var0, var7, var3, false, var5, var6);
         }

         var8 = merge(var25, var23);
         if (var8 == null) {
            throw NotFoundException.getNotFoundInstance();
         }

         if (var9 != 0 || var8.getBoundingBox() == null || var8.getBoundingBox().getMinY() >= var7.getMinY() && var8.getBoundingBox().getMaxY() <= var7.getMaxY()) {
            var8.setBoundingBox(var7);
            var11 = var23;
            var10 = var25;
            break;
         }

         var7 = var8.getBoundingBox();
         ++var9;
      }

      int var12 = var8.getBarcodeColumnCount() + 1;
      var8.setDetectionResultColumn(0, var10);
      var8.setDetectionResultColumn(var12, var11);
      boolean var13;
      if (var10 != null) {
         var13 = true;
      } else {
         var13 = false;
      }

      byte var14 = 1;
      var9 = var5;

      int var17;
      for(var5 = var14; var5 <= var12; var6 = var17) {
         int var15;
         if (var13) {
            var15 = var5;
         } else {
            var15 = var12 - var5;
         }

         int var16 = var9;
         var17 = var6;
         if (var8.getDetectionResultColumn(var15) == null) {
            Object var22;
            if (var15 != 0 && var15 != var12) {
               var22 = new DetectionResultColumn(var7);
            } else {
               boolean var18;
               if (var15 == 0) {
                  var18 = true;
               } else {
                  var18 = false;
               }

               var22 = new DetectionResultRowIndicatorColumn(var7, var18);
            }

            var8.setDetectionResultColumn(var15, (DetectionResultColumn)var22);
            int var26 = -1;
            int var19 = var7.getMinY();

            while(true) {
               var16 = var9;
               var17 = var6;
               if (var19 > var7.getMaxY()) {
                  break;
               }

               int var20;
               int var21;
               label66: {
                  label104: {
                     var17 = getStartColumn(var8, var15, var19, var13);
                     if (var17 >= 0) {
                        var16 = var17;
                        if (var17 <= var7.getMaxX()) {
                           break label104;
                        }
                     }

                     var20 = var26;
                     var21 = var9;
                     var17 = var6;
                     if (var26 == -1) {
                        break label66;
                     }

                     var16 = var26;
                  }

                  Codeword var24 = detectCodeword(var0, var7.getMinX(), var7.getMaxX(), var13, var16, var19, var9, var6);
                  var20 = var26;
                  var21 = var9;
                  var17 = var6;
                  if (var24 != null) {
                     ((DetectionResultColumn)var22).setCodeword(var19, var24);
                     var21 = Math.min(var9, var24.getWidth());
                     var17 = Math.max(var6, var24.getWidth());
                     var20 = var16;
                  }
               }

               ++var19;
               var26 = var20;
               var9 = var21;
               var6 = var17;
            }
         }

         ++var5;
         var9 = var16;
      }

      return createDecoderResult(var8);
   }

   private static DecoderResult decodeCodewords(int[] var0, int var1, int[] var2) throws FormatException, ChecksumException {
      if (var0.length == 0) {
         throw FormatException.getFormatInstance();
      } else {
         int var3 = 1 << var1 + 1;
         int var4 = correctErrors(var0, var2, var3);
         verifyCodewordCount(var0, var3);
         DecoderResult var5 = DecodedBitStreamParser.decode(var0, String.valueOf(var1));
         var5.setErrorsCorrected(var4);
         var5.setErasures(var2.length);
         return var5;
      }
   }

   private static Codeword detectCodeword(BitMatrix var0, int var1, int var2, boolean var3, int var4, int var5, int var6, int var7) {
      var4 = adjustCodewordStartColumn(var0, var1, var2, var3, var4, var5);
      int[] var8 = getModuleBitCount(var0, var1, var2, var3, var4, var5);
      Codeword var9;
      if (var8 == null) {
         var9 = null;
      } else {
         var2 = MathUtils.sum(var8);
         if (var3) {
            var1 = var4 + var2;
         } else {
            for(var1 = 0; var1 < var8.length / 2; ++var1) {
               var5 = var8[var1];
               var8[var1] = var8[var8.length - 1 - var1];
               var8[var8.length - 1 - var1] = var5;
            }

            var1 = var4;
            var4 -= var2;
         }

         if (!checkCodewordSkew(var2, var6, var7)) {
            var9 = null;
         } else {
            var2 = PDF417CodewordDecoder.getDecodedValue(var8);
            var5 = PDF417Common.getCodeword(var2);
            if (var5 == -1) {
               var9 = null;
            } else {
               var9 = new Codeword(var4, var1, getCodewordBucketNumber(var2), var5);
            }
         }
      }

      return var9;
   }

   private static BarcodeMetadata getBarcodeMetadata(DetectionResultRowIndicatorColumn var0, DetectionResultRowIndicatorColumn var1) {
      BarcodeMetadata var3;
      if (var0 != null) {
         BarcodeMetadata var2 = var0.getBarcodeMetadata();
         if (var2 != null) {
            var3 = var2;
            if (var1 != null) {
               BarcodeMetadata var4 = var1.getBarcodeMetadata();
               var3 = var2;
               if (var4 != null) {
                  var3 = var2;
                  if (var2.getColumnCount() != var4.getColumnCount()) {
                     var3 = var2;
                     if (var2.getErrorCorrectionLevel() != var4.getErrorCorrectionLevel()) {
                        var3 = var2;
                        if (var2.getRowCount() != var4.getRowCount()) {
                           var3 = null;
                        }

                        return var3;
                     }
                  }

                  return var3;
               }
            }

            return var3;
         }
      }

      if (var1 == null) {
         var3 = null;
      } else {
         var3 = var1.getBarcodeMetadata();
      }

      return var3;
   }

   private static int[] getBitCountForCodeword(int var0) {
      int[] var1 = new int[8];
      int var2 = 0;
      int var3 = 7;

      while(true) {
         int var4 = var3;
         int var5 = var2;
         if ((var0 & 1) != var2) {
            var5 = var0 & 1;
            var4 = var3 - 1;
            if (var4 < 0) {
               return var1;
            }
         }

         int var10002 = var1[var4]++;
         var0 >>= 1;
         var3 = var4;
         var2 = var5;
      }
   }

   private static int getCodewordBucketNumber(int var0) {
      return getCodewordBucketNumber(getBitCountForCodeword(var0));
   }

   private static int getCodewordBucketNumber(int[] var0) {
      return (var0[0] - var0[2] + var0[4] - var0[6] + 9) % 9;
   }

   private static int getMax(int[] var0) {
      int var1 = -1;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = Math.max(var1, var0[var3]);
      }

      return var1;
   }

   private static int[] getModuleBitCount(BitMatrix var0, int var1, int var2, boolean var3, int var4, int var5) {
      int var6 = var4;
      int[] var7 = new int[8];
      int var8 = 0;
      byte var11;
      if (var3) {
         var11 = 1;
      } else {
         var11 = -1;
      }

      boolean var9 = var3;

      while(true) {
         if (var3) {
            if (var6 >= var2) {
               break;
            }
         } else if (var6 < var1) {
            break;
         }

         if (var8 >= 8) {
            break;
         }

         if (var0.get(var6, var5) == var9) {
            int var10002 = var7[var8]++;
            var6 += var11;
         } else {
            ++var8;
            if (!var9) {
               var9 = true;
            } else {
               var9 = false;
            }
         }
      }

      int[] var10 = var7;
      if (var8 != 8) {
         if (var3) {
            var1 = var2;
         }

         if (var6 == var1 && var8 == 7) {
            var10 = var7;
         } else {
            var10 = null;
         }
      }

      return var10;
   }

   private static int getNumberOfECCodeWords(int var0) {
      return 2 << var0;
   }

   private static DetectionResultRowIndicatorColumn getRowIndicatorColumn(BitMatrix var0, BoundingBox var1, ResultPoint var2, boolean var3, int var4, int var5) {
      DetectionResultRowIndicatorColumn var6 = new DetectionResultRowIndicatorColumn(var1, var3);

      for(int var7 = 0; var7 < 2; ++var7) {
         byte var8;
         if (var7 == 0) {
            var8 = 1;
         } else {
            var8 = -1;
         }

         int var9 = (int)var2.getX();

         for(int var10 = (int)var2.getY(); var10 <= var1.getMaxY() && var10 >= var1.getMinY(); var10 += var8) {
            Codeword var11 = detectCodeword(var0, 0, var0.getWidth(), var3, var9, var10, var4, var5);
            if (var11 != null) {
               var6.setCodeword(var10, var11);
               if (var3) {
                  var9 = var11.getStartX();
               } else {
                  var9 = var11.getEndX();
               }
            }
         }
      }

      return var6;
   }

   private static int getStartColumn(DetectionResult var0, int var1, int var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = -1;
      }

      Codeword var5 = null;
      if (isValidBarcodeColumn(var0, var1 - var4)) {
         var5 = var0.getDetectionResultColumn(var1 - var4).getCodeword(var2);
      }

      if (var5 != null) {
         if (var3) {
            var1 = var5.getEndX();
         } else {
            var1 = var5.getStartX();
         }
      } else {
         var5 = var0.getDetectionResultColumn(var1).getCodewordNearby(var2);
         if (var5 != null) {
            if (var3) {
               var1 = var5.getStartX();
            } else {
               var1 = var5.getEndX();
            }
         } else {
            if (isValidBarcodeColumn(var0, var1 - var4)) {
               var5 = var0.getDetectionResultColumn(var1 - var4).getCodewordNearby(var2);
            }

            if (var5 != null) {
               if (var3) {
                  var1 = var5.getEndX();
               } else {
                  var1 = var5.getStartX();
               }
            } else {
               byte var6 = 0;
               var2 = var1;
               var1 = var6;

               while(true) {
                  if (!isValidBarcodeColumn(var0, var2 - var4)) {
                     if (var3) {
                        var1 = var0.getBoundingBox().getMinX();
                     } else {
                        var1 = var0.getBoundingBox().getMaxX();
                     }
                     break;
                  }

                  int var9 = var2 - var4;
                  Codeword[] var7 = var0.getDetectionResultColumn(var9).getCodewords();
                  int var8 = var7.length;

                  for(var2 = 0; var2 < var8; ++var2) {
                     var5 = var7[var2];
                     if (var5 != null) {
                        if (var3) {
                           var2 = var5.getEndX();
                        } else {
                           var2 = var5.getStartX();
                        }

                        var1 = var2 + var4 * var1 * (var5.getEndX() - var5.getStartX());
                        return var1;
                     }
                  }

                  ++var1;
                  var2 = var9;
               }
            }
         }
      }

      return var1;
   }

   private static boolean isValidBarcodeColumn(DetectionResult var0, int var1) {
      boolean var2;
      if (var1 >= 0 && var1 <= var0.getBarcodeColumnCount() + 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static DetectionResult merge(DetectionResultRowIndicatorColumn var0, DetectionResultRowIndicatorColumn var1) throws NotFoundException {
      DetectionResult var2 = null;
      if (var0 != null || var1 != null) {
         BarcodeMetadata var3 = getBarcodeMetadata(var0, var1);
         if (var3 != null) {
            var2 = new DetectionResult(var3, BoundingBox.merge(adjustBoundingBox(var0), adjustBoundingBox(var1)));
         }
      }

      return var2;
   }

   public static String toString(BarcodeValue[][] var0) {
      Formatter var1 = new Formatter();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.format("Row %2d: ", var2);

         for(int var3 = 0; var3 < var0[var2].length; ++var3) {
            BarcodeValue var4 = var0[var2][var3];
            if (var4.getValue().length == 0) {
               var1.format("        ", (Object[])null);
            } else {
               var1.format("%4d(%2d)", var4.getValue()[0], var4.getConfidence(var4.getValue()[0]));
            }
         }

         var1.format("%n");
      }

      String var5 = var1.toString();
      var1.close();
      return var5;
   }

   private static void verifyCodewordCount(int[] var0, int var1) throws FormatException {
      if (var0.length < 4) {
         throw FormatException.getFormatInstance();
      } else {
         int var2 = var0[0];
         if (var2 > var0.length) {
            throw FormatException.getFormatInstance();
         } else {
            if (var2 == 0) {
               if (var1 >= var0.length) {
                  throw FormatException.getFormatInstance();
               }

               var0[0] = var0.length - var1;
            }

         }
      }
   }
}
