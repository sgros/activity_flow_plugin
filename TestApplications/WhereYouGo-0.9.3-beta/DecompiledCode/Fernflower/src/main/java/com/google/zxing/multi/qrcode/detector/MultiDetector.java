package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.detector.Detector;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.util.ArrayList;
import java.util.Map;

public final class MultiDetector extends Detector {
   private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

   public MultiDetector(BitMatrix var1) {
      super(var1);
   }

   public DetectorResult[] detectMulti(Map var1) throws NotFoundException {
      BitMatrix var2 = this.getImage();
      ResultPointCallback var3;
      if (var1 == null) {
         var3 = null;
      } else {
         var3 = (ResultPointCallback)var1.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      }

      FinderPatternInfo[] var7 = (new MultiFinderPatternFinder(var2, var3)).findMulti(var1);
      if (var7.length == 0) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         ArrayList var10 = new ArrayList();
         int var4 = var7.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            FinderPatternInfo var9 = var7[var5];

            try {
               var10.add(this.processFinderPatternInfo(var9));
            } catch (ReaderException var6) {
            }
         }

         DetectorResult[] var8;
         if (var10.isEmpty()) {
            var8 = EMPTY_DETECTOR_RESULTS;
         } else {
            var8 = (DetectorResult[])var10.toArray(new DetectorResult[var10.size()]);
         }

         return var8;
      }
   }
}
