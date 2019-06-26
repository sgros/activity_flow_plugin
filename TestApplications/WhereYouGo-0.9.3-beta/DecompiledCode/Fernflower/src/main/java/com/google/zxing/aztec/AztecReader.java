package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.aztec.decoder.Decoder;
import com.google.zxing.aztec.detector.Detector;
import com.google.zxing.common.DecoderResult;
import java.util.List;
import java.util.Map;

public final class AztecReader implements Reader {
   public Result decode(BinaryBitmap var1) throws NotFoundException, FormatException {
      return this.decode(var1, (Map)null);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, FormatException {
      NotFoundException var3 = null;
      FormatException var4 = null;
      Detector var5 = new Detector(var1.getBlackMatrix());
      Decoder var6 = null;
      ResultPoint[] var7 = null;
      Object var8 = null;
      ResultPoint[] var9 = var7;
      ResultPoint[] var25 = var6;

      DecoderResult var34;
      label104: {
         DecoderResult var32;
         label103: {
            NotFoundException var36;
            label102: {
               FormatException var10000;
               label108: {
                  AztecDetectorResult var10;
                  boolean var10001;
                  try {
                     var10 = var5.detect(false);
                  } catch (NotFoundException var23) {
                     var36 = var23;
                     var10001 = false;
                     break label102;
                  } catch (FormatException var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label108;
                  }

                  var9 = var7;
                  var25 = var6;

                  try {
                     var7 = var10.getPoints();
                  } catch (NotFoundException var21) {
                     var36 = var21;
                     var10001 = false;
                     break label102;
                  } catch (FormatException var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label108;
                  }

                  var9 = var7;
                  var25 = var7;

                  try {
                     var6 = new Decoder;
                  } catch (NotFoundException var19) {
                     var36 = var19;
                     var10001 = false;
                     break label102;
                  } catch (FormatException var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label108;
                  }

                  var9 = var7;
                  var25 = var7;

                  try {
                     var6.<init>();
                  } catch (NotFoundException var17) {
                     var36 = var17;
                     var10001 = false;
                     break label102;
                  } catch (FormatException var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label108;
                  }

                  var9 = var7;
                  var25 = var7;

                  try {
                     var32 = var6.decode(var10);
                     break label103;
                  } catch (NotFoundException var15) {
                     var36 = var15;
                     var10001 = false;
                     break label102;
                  } catch (FormatException var16) {
                     var10000 = var16;
                     var10001 = false;
                  }
               }

               var4 = var10000;
               var34 = (DecoderResult)var8;
               break label104;
            }

            var3 = var36;
            var25 = var9;
            var34 = (DecoderResult)var8;
            break label104;
         }

         var34 = var32;
         var25 = var7;
      }

      var7 = var25;
      DecoderResult var28 = var34;
      if (var34 == null) {
         label110: {
            Object var29;
            try {
               AztecDetectorResult var30 = var5.detect(true);
               var7 = var30.getPoints();
               Decoder var35 = new Decoder();
               var28 = var35.decode(var30);
               break label110;
            } catch (NotFoundException var13) {
               var29 = var13;
            } catch (FormatException var14) {
               var29 = var14;
            }

            if (var3 != null) {
               throw var3;
            }

            if (var4 != null) {
               throw var4;
            }

            throw var29;
         }
      }

      if (var2 != null) {
         ResultPointCallback var26 = (ResultPointCallback)var2.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
         if (var26 != null) {
            int var11 = var7.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               var26.foundPossibleResultPoint(var7[var12]);
            }
         }
      }

      Result var27 = new Result(var28.getText(), var28.getRawBytes(), var28.getNumBits(), var7, BarcodeFormat.AZTEC, System.currentTimeMillis());
      List var33 = var28.getByteSegments();
      if (var33 != null) {
         var27.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var33);
      }

      String var31 = var28.getECLevel();
      if (var31 != null) {
         var27.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var31);
      }

      return var27;
   }

   public void reset() {
   }
}
