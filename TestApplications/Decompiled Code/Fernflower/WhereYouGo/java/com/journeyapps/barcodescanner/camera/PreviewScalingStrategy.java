package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import android.util.Log;
import com.journeyapps.barcodescanner.Size;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class PreviewScalingStrategy {
   private static final String TAG = PreviewScalingStrategy.class.getSimpleName();

   public List getBestPreviewOrder(List var1, final Size var2) {
      if (var2 != null) {
         Collections.sort(var1, new Comparator() {
            public int compare(Size var1, Size var2x) {
               float var3 = PreviewScalingStrategy.this.getScore(var1, var2);
               return Float.compare(PreviewScalingStrategy.this.getScore(var2x, var2), var3);
            }
         });
      }

      return var1;
   }

   public Size getBestPreviewSize(List var1, Size var2) {
      var1 = this.getBestPreviewOrder(var1, var2);
      Log.i(TAG, "Viewfinder size: " + var2);
      Log.i(TAG, "Preview in order of preference: " + var1);
      return (Size)var1.get(0);
   }

   protected float getScore(Size var1, Size var2) {
      return 0.5F;
   }

   public abstract Rect scalePreview(Size var1, Size var2);
}
