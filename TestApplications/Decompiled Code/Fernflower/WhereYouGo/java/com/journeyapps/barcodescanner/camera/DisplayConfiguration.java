package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import com.journeyapps.barcodescanner.Size;
import java.util.List;

public class DisplayConfiguration {
   private static final String TAG = DisplayConfiguration.class.getSimpleName();
   private boolean center = false;
   private PreviewScalingStrategy previewScalingStrategy = new FitCenterStrategy();
   private int rotation;
   private Size viewfinderSize;

   public DisplayConfiguration(int var1) {
      this.rotation = var1;
   }

   public DisplayConfiguration(int var1, Size var2) {
      this.rotation = var1;
      this.viewfinderSize = var2;
   }

   public Size getBestPreviewSize(List var1, boolean var2) {
      Size var3 = this.getDesiredPreviewSize(var2);
      return this.previewScalingStrategy.getBestPreviewSize(var1, var3);
   }

   public Size getDesiredPreviewSize(boolean var1) {
      Size var2;
      if (this.viewfinderSize == null) {
         var2 = null;
      } else if (var1) {
         var2 = this.viewfinderSize.rotate();
      } else {
         var2 = this.viewfinderSize;
      }

      return var2;
   }

   public PreviewScalingStrategy getPreviewScalingStrategy() {
      return this.previewScalingStrategy;
   }

   public int getRotation() {
      return this.rotation;
   }

   public Size getViewfinderSize() {
      return this.viewfinderSize;
   }

   public Rect scalePreview(Size var1) {
      return this.previewScalingStrategy.scalePreview(var1, this.viewfinderSize);
   }

   public void setPreviewScalingStrategy(PreviewScalingStrategy var1) {
      this.previewScalingStrategy = var1;
   }
}
