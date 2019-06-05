package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import android.util.Log;
import com.journeyapps.barcodescanner.Size;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LegacyPreviewScalingStrategy extends PreviewScalingStrategy {
   private static final String TAG = LegacyPreviewScalingStrategy.class.getSimpleName();

   public static Size scale(Size var0, Size var1) {
      Size var2 = var0;
      var0 = var0;
      Size var3;
      if (!var1.fitsIn(var2)) {
         while(true) {
            var3 = var2.scale(3, 2);
            var0 = var2.scale(2, 1);
            if (var1.fitsIn(var3)) {
               var0 = var3;
               break;
            }

            if (var1.fitsIn(var0)) {
               break;
            }

            var2 = var0;
         }
      } else {
         while(true) {
            var3 = var0.scale(2, 3);
            var2 = var0.scale(1, 2);
            if (!var1.fitsIn(var2)) {
               if (var1.fitsIn(var3)) {
                  var0 = var3;
               }
               break;
            }

            var0 = var2;
         }
      }

      return var0;
   }

   public Size getBestPreviewSize(List var1, final Size var2) {
      Size var3;
      if (var2 == null) {
         var3 = (Size)var1.get(0);
      } else {
         Collections.sort(var1, new Comparator() {
            public int compare(Size var1, Size var2x) {
               byte var3 = -1;
               int var4 = LegacyPreviewScalingStrategy.scale(var1, var2).width - var1.width;
               int var5 = LegacyPreviewScalingStrategy.scale(var2x, var2).width - var2x.width;
               int var6;
               if (var4 == 0 && var5 == 0) {
                  var6 = var1.compareTo(var2x);
               } else {
                  var6 = var3;
                  if (var4 != 0) {
                     if (var5 == 0) {
                        var6 = 1;
                     } else if (var4 < 0 && var5 < 0) {
                        var6 = var1.compareTo(var2x);
                     } else if (var4 > 0 && var5 > 0) {
                        var6 = -var1.compareTo(var2x);
                     } else {
                        var6 = var3;
                        if (var4 >= 0) {
                           var6 = 1;
                        }
                     }
                  }
               }

               return var6;
            }
         });
         Log.i(TAG, "Viewfinder size: " + var2);
         Log.i(TAG, "Preview in order of preference: " + var1);
         var3 = (Size)var1.get(0);
      }

      return var3;
   }

   public Rect scalePreview(Size var1, Size var2) {
      Size var3 = scale(var1, var2);
      Log.i(TAG, "Preview: " + var1 + "; Scaled: " + var3 + "; Want: " + var2);
      int var4 = (var3.width - var2.width) / 2;
      int var5 = (var3.height - var2.height) / 2;
      return new Rect(-var4, -var5, var3.width - var4, var3.height - var5);
   }
}
