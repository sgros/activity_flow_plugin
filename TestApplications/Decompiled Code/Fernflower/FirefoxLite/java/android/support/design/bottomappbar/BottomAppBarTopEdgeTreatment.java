package android.support.design.bottomappbar;

import android.support.design.shape.EdgeTreatment;
import android.support.design.shape.ShapePath;

public class BottomAppBarTopEdgeTreatment extends EdgeTreatment {
   private float cradleVerticalOffset;
   private float fabDiameter;
   private float fabMargin;
   private float horizontalOffset;
   private float roundedCornerRadius;

   float getCradleVerticalOffset() {
      return this.cradleVerticalOffset;
   }

   public void getEdgePath(float var1, float var2, ShapePath var3) {
      if (this.fabDiameter == 0.0F) {
         var3.lineTo(var1, 0.0F);
      } else {
         float var4 = (this.fabMargin * 2.0F + this.fabDiameter) / 2.0F;
         float var5 = var2 * this.roundedCornerRadius;
         float var6 = var1 / 2.0F + this.horizontalOffset;
         var2 = this.cradleVerticalOffset * var2 + (1.0F - var2) * var4;
         if (var2 / var4 >= 1.0F) {
            var3.lineTo(var1, 0.0F);
         } else {
            float var7 = var4 + var5;
            float var8 = var2 + var5;
            float var9 = (float)Math.sqrt((double)(var7 * var7 - var8 * var8));
            float var10 = var6 - var9;
            var7 = var6 + var9;
            var8 = (float)Math.toDegrees(Math.atan((double)(var9 / var8)));
            float var11 = 90.0F - var8;
            var9 = var10 - var5;
            var3.lineTo(var9, 0.0F);
            float var12 = var5 * 2.0F;
            var3.addArc(var9, 0.0F, var10 + var5, var12, 270.0F, var8);
            var3.addArc(var6 - var4, -var4 - var2, var6 + var4, var4 - var2, 180.0F - var11, var11 * 2.0F - 180.0F);
            var3.addArc(var7 - var5, 0.0F, var7 + var5, var12, 270.0F - var8, var8);
            var3.lineTo(var1, 0.0F);
         }
      }
   }

   float getFabCradleMargin() {
      return this.fabMargin;
   }

   float getFabCradleRoundedCornerRadius() {
      return this.roundedCornerRadius;
   }

   float getFabDiameter() {
      return this.fabDiameter;
   }

   float getHorizontalOffset() {
      return this.horizontalOffset;
   }

   void setCradleVerticalOffset(float var1) {
      this.cradleVerticalOffset = var1;
   }

   void setFabCradleMargin(float var1) {
      this.fabMargin = var1;
   }

   void setFabCradleRoundedCornerRadius(float var1) {
      this.roundedCornerRadius = var1;
   }

   void setFabDiameter(float var1) {
      this.fabDiameter = var1;
   }

   void setHorizontalOffset(float var1) {
      this.horizontalOffset = var1;
   }
}
