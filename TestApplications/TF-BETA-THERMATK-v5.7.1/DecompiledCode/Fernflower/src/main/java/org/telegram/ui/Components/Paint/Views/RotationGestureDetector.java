package org.telegram.ui.Components.Paint.Views;

import android.view.MotionEvent;

public class RotationGestureDetector {
   private float angle;
   private float fX;
   private float fY;
   private RotationGestureDetector.OnRotationGestureListener mListener;
   private float sX;
   private float sY;
   private float startAngle;

   public RotationGestureDetector(RotationGestureDetector.OnRotationGestureListener var1) {
      this.mListener = var1;
   }

   private float angleBetweenLines(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      var2 = (float)Math.toDegrees((double)((float)Math.atan2((double)(var2 - var4), (double)(var1 - var3)) - (float)Math.atan2((double)(var6 - var8), (double)(var5 - var7)))) % 360.0F;
      var1 = var2;
      if (var2 < -180.0F) {
         var1 = var2 + 360.0F;
      }

      var2 = var1;
      if (var1 > 180.0F) {
         var2 = var1 - 360.0F;
      }

      return var2;
   }

   public float getAngle() {
      return this.angle;
   }

   public float getStartAngle() {
      return this.startAngle;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getPointerCount() != 2) {
         return false;
      } else {
         label53: {
            int var2 = var1.getActionMasked();
            if (var2 != 0) {
               if (var2 == 1) {
                  break label53;
               }

               if (var2 == 2) {
                  float var3 = var1.getX(0);
                  float var4 = var1.getY(0);
                  float var5 = var1.getX(1);
                  float var6 = var1.getY(1);
                  this.angle = this.angleBetweenLines(this.fX, this.fY, this.sX, this.sY, var5, var6, var3, var4);
                  if (this.mListener != null) {
                     if (Float.isNaN(this.startAngle)) {
                        this.startAngle = this.angle;
                        this.mListener.onRotationBegin(this);
                     } else {
                        this.mListener.onRotation(this);
                     }

                     return true;
                  }

                  return true;
               }

               if (var2 == 3) {
                  break label53;
               }

               if (var2 != 5) {
                  if (var2 == 6) {
                     this.startAngle = Float.NaN;
                     RotationGestureDetector.OnRotationGestureListener var7 = this.mListener;
                     if (var7 != null) {
                        var7.onRotationEnd(this);
                        return true;
                     }
                  }

                  return true;
               }
            }

            this.sX = var1.getX(0);
            this.sY = var1.getY(0);
            this.fX = var1.getX(1);
            this.fY = var1.getY(1);
            return true;
         }

         this.startAngle = Float.NaN;
         return true;
      }
   }

   public interface OnRotationGestureListener {
      void onRotation(RotationGestureDetector var1);

      void onRotationBegin(RotationGestureDetector var1);

      void onRotationEnd(RotationGestureDetector var1);
   }
}
