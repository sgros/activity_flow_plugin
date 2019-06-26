package com.airbnb.lottie.animation.keyframe;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.Collections;

public class TransformKeyframeAnimation {
   private BaseKeyframeAnimation anchorPoint;
   private BaseKeyframeAnimation endOpacity;
   private final Matrix matrix = new Matrix();
   private BaseKeyframeAnimation opacity;
   private BaseKeyframeAnimation position;
   private BaseKeyframeAnimation rotation;
   private BaseKeyframeAnimation scale;
   private FloatKeyframeAnimation skew;
   private FloatKeyframeAnimation skewAngle;
   private final Matrix skewMatrix1;
   private final Matrix skewMatrix2;
   private final Matrix skewMatrix3;
   private final float[] skewValues;
   private BaseKeyframeAnimation startOpacity;

   public TransformKeyframeAnimation(AnimatableTransform var1) {
      BaseKeyframeAnimation var2;
      if (var1.getAnchorPoint() == null) {
         var2 = null;
      } else {
         var2 = var1.getAnchorPoint().createAnimation();
      }

      this.anchorPoint = var2;
      if (var1.getPosition() == null) {
         var2 = null;
      } else {
         var2 = var1.getPosition().createAnimation();
      }

      this.position = var2;
      if (var1.getScale() == null) {
         var2 = null;
      } else {
         var2 = var1.getScale().createAnimation();
      }

      this.scale = var2;
      if (var1.getRotation() == null) {
         var2 = null;
      } else {
         var2 = var1.getRotation().createAnimation();
      }

      this.rotation = var2;
      FloatKeyframeAnimation var3;
      if (var1.getSkew() == null) {
         var3 = null;
      } else {
         var3 = (FloatKeyframeAnimation)var1.getSkew().createAnimation();
      }

      this.skew = var3;
      if (this.skew != null) {
         this.skewMatrix1 = new Matrix();
         this.skewMatrix2 = new Matrix();
         this.skewMatrix3 = new Matrix();
         this.skewValues = new float[9];
      } else {
         this.skewMatrix1 = null;
         this.skewMatrix2 = null;
         this.skewMatrix3 = null;
         this.skewValues = null;
      }

      if (var1.getSkewAngle() == null) {
         var3 = null;
      } else {
         var3 = (FloatKeyframeAnimation)var1.getSkewAngle().createAnimation();
      }

      this.skewAngle = var3;
      if (var1.getOpacity() != null) {
         this.opacity = var1.getOpacity().createAnimation();
      }

      if (var1.getStartOpacity() != null) {
         this.startOpacity = var1.getStartOpacity().createAnimation();
      } else {
         this.startOpacity = null;
      }

      if (var1.getEndOpacity() != null) {
         this.endOpacity = var1.getEndOpacity().createAnimation();
      } else {
         this.endOpacity = null;
      }

   }

   private void clearSkewValues() {
      for(int var1 = 0; var1 < 9; ++var1) {
         this.skewValues[var1] = 0.0F;
      }

   }

   public void addAnimationsToLayer(BaseLayer var1) {
      var1.addAnimation(this.opacity);
      var1.addAnimation(this.startOpacity);
      var1.addAnimation(this.endOpacity);
      var1.addAnimation(this.anchorPoint);
      var1.addAnimation(this.position);
      var1.addAnimation(this.scale);
      var1.addAnimation(this.rotation);
      var1.addAnimation(this.skew);
      var1.addAnimation(this.skewAngle);
   }

   public void addListener(BaseKeyframeAnimation.AnimationListener var1) {
      BaseKeyframeAnimation var2 = this.opacity;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      var2 = this.startOpacity;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      var2 = this.endOpacity;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      var2 = this.anchorPoint;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      var2 = this.position;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      var2 = this.scale;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      var2 = this.rotation;
      if (var2 != null) {
         var2.addUpdateListener(var1);
      }

      FloatKeyframeAnimation var3 = this.skew;
      if (var3 != null) {
         var3.addUpdateListener(var1);
      }

      var3 = this.skewAngle;
      if (var3 != null) {
         var3.addUpdateListener(var1);
      }

   }

   public boolean applyValueCallback(Object var1, LottieValueCallback var2) {
      BaseKeyframeAnimation var5;
      if (var1 == LottieProperty.TRANSFORM_ANCHOR_POINT) {
         var5 = this.anchorPoint;
         if (var5 == null) {
            this.anchorPoint = new ValueCallbackKeyframeAnimation(var2, new PointF());
         } else {
            var5.setValueCallback(var2);
         }
      } else if (var1 == LottieProperty.TRANSFORM_POSITION) {
         var5 = this.position;
         if (var5 == null) {
            this.position = new ValueCallbackKeyframeAnimation(var2, new PointF());
         } else {
            var5.setValueCallback(var2);
         }
      } else if (var1 == LottieProperty.TRANSFORM_SCALE) {
         var5 = this.scale;
         if (var5 == null) {
            this.scale = new ValueCallbackKeyframeAnimation(var2, new ScaleXY());
         } else {
            var5.setValueCallback(var2);
         }
      } else if (var1 == LottieProperty.TRANSFORM_ROTATION) {
         var5 = this.rotation;
         if (var5 == null) {
            this.rotation = new ValueCallbackKeyframeAnimation(var2, 0.0F);
         } else {
            var5.setValueCallback(var2);
         }
      } else if (var1 == LottieProperty.TRANSFORM_OPACITY) {
         var5 = this.opacity;
         if (var5 == null) {
            this.opacity = new ValueCallbackKeyframeAnimation(var2, 100);
         } else {
            var5.setValueCallback(var2);
         }
      } else {
         BaseKeyframeAnimation var3;
         if (var1 == LottieProperty.TRANSFORM_START_OPACITY) {
            var3 = this.startOpacity;
            if (var3 != null) {
               if (var3 == null) {
                  this.startOpacity = new ValueCallbackKeyframeAnimation(var2, 100);
               } else {
                  var3.setValueCallback(var2);
               }

               return true;
            }
         }

         if (var1 == LottieProperty.TRANSFORM_END_OPACITY) {
            var3 = this.endOpacity;
            if (var3 != null) {
               if (var3 == null) {
                  this.endOpacity = new ValueCallbackKeyframeAnimation(var2, 100);
               } else {
                  var3.setValueCallback(var2);
               }

               return true;
            }
         }

         if (var1 == LottieProperty.TRANSFORM_SKEW) {
            FloatKeyframeAnimation var6 = this.skew;
            if (var6 != null) {
               if (var6 == null) {
                  this.skew = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(0.0F)));
               }

               this.skew.setValueCallback(var2);
               return true;
            }
         }

         if (var1 == LottieProperty.TRANSFORM_SKEW_ANGLE) {
            FloatKeyframeAnimation var4 = this.skewAngle;
            if (var4 != null) {
               if (var4 == null) {
                  this.skewAngle = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(0.0F)));
               }

               this.skewAngle.setValueCallback(var2);
               return true;
            }
         }

         return false;
      }

      return true;
   }

   public BaseKeyframeAnimation getEndOpacity() {
      return this.endOpacity;
   }

   public Matrix getMatrix() {
      this.matrix.reset();
      BaseKeyframeAnimation var1 = this.position;
      PointF var6;
      if (var1 != null) {
         var6 = (PointF)var1.getValue();
         if (var6.x != 0.0F || var6.y != 0.0F) {
            this.matrix.preTranslate(var6.x, var6.y);
         }
      }

      var1 = this.rotation;
      float var2;
      if (var1 != null) {
         if (var1 instanceof ValueCallbackKeyframeAnimation) {
            var2 = (Float)var1.getValue();
         } else {
            var2 = ((FloatKeyframeAnimation)var1).getFloatValue();
         }

         if (var2 != 0.0F) {
            this.matrix.preRotate(var2);
         }
      }

      if (this.skew != null) {
         FloatKeyframeAnimation var7 = this.skewAngle;
         if (var7 == null) {
            var2 = 0.0F;
         } else {
            var2 = (float)Math.cos(Math.toRadians((double)(-var7.getFloatValue() + 90.0F)));
         }

         var7 = this.skewAngle;
         float var3;
         if (var7 == null) {
            var3 = 1.0F;
         } else {
            var3 = (float)Math.sin(Math.toRadians((double)(-var7.getFloatValue() + 90.0F)));
         }

         float var4 = (float)Math.tan(Math.toRadians((double)this.skew.getFloatValue()));
         this.clearSkewValues();
         float[] var8 = this.skewValues;
         var8[0] = var2;
         var8[1] = var3;
         float var5 = -var3;
         var8[3] = var5;
         var8[4] = var2;
         var8[8] = 1.0F;
         this.skewMatrix1.setValues(var8);
         this.clearSkewValues();
         var8 = this.skewValues;
         var8[0] = 1.0F;
         var8[3] = var4;
         var8[4] = 1.0F;
         var8[8] = 1.0F;
         this.skewMatrix2.setValues(var8);
         this.clearSkewValues();
         var8 = this.skewValues;
         var8[0] = var2;
         var8[1] = var5;
         var8[3] = var3;
         var8[4] = var2;
         var8[8] = 1.0F;
         this.skewMatrix3.setValues(var8);
         this.skewMatrix2.preConcat(this.skewMatrix1);
         this.skewMatrix3.preConcat(this.skewMatrix2);
         this.matrix.preConcat(this.skewMatrix3);
      }

      var1 = this.scale;
      if (var1 != null) {
         ScaleXY var9 = (ScaleXY)var1.getValue();
         if (var9.getScaleX() != 1.0F || var9.getScaleY() != 1.0F) {
            this.matrix.preScale(var9.getScaleX(), var9.getScaleY());
         }
      }

      var1 = this.anchorPoint;
      if (var1 != null) {
         var6 = (PointF)var1.getValue();
         if (var6.x != 0.0F || var6.y != 0.0F) {
            this.matrix.preTranslate(-var6.x, -var6.y);
         }
      }

      return this.matrix;
   }

   public Matrix getMatrixForRepeater(float var1) {
      BaseKeyframeAnimation var2 = this.position;
      Object var3 = null;
      PointF var12;
      if (var2 == null) {
         var12 = null;
      } else {
         var12 = (PointF)var2.getValue();
      }

      BaseKeyframeAnimation var4 = this.scale;
      ScaleXY var14;
      if (var4 == null) {
         var14 = null;
      } else {
         var14 = (ScaleXY)var4.getValue();
      }

      this.matrix.reset();
      if (var12 != null) {
         this.matrix.preTranslate(var12.x * var1, var12.y * var1);
      }

      if (var14 != null) {
         Matrix var13 = this.matrix;
         double var5 = (double)var14.getScaleX();
         double var7 = (double)var1;
         var13.preScale((float)Math.pow(var5, var7), (float)Math.pow((double)var14.getScaleY(), var7));
      }

      var2 = this.rotation;
      if (var2 != null) {
         float var9 = (Float)var2.getValue();
         var2 = this.anchorPoint;
         if (var2 == null) {
            var12 = (PointF)var3;
         } else {
            var12 = (PointF)var2.getValue();
         }

         Matrix var15 = this.matrix;
         float var10 = 0.0F;
         float var11;
         if (var12 == null) {
            var11 = 0.0F;
         } else {
            var11 = var12.x;
         }

         if (var12 != null) {
            var10 = var12.y;
         }

         var15.preRotate(var9 * var1, var11, var10);
      }

      return this.matrix;
   }

   public BaseKeyframeAnimation getOpacity() {
      return this.opacity;
   }

   public BaseKeyframeAnimation getStartOpacity() {
      return this.startOpacity;
   }

   public void setProgress(float var1) {
      BaseKeyframeAnimation var2 = this.opacity;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      var2 = this.startOpacity;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      var2 = this.endOpacity;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      var2 = this.anchorPoint;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      var2 = this.position;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      var2 = this.scale;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      var2 = this.rotation;
      if (var2 != null) {
         var2.setProgress(var1);
      }

      FloatKeyframeAnimation var3 = this.skew;
      if (var3 != null) {
         var3.setProgress(var1);
      }

      var3 = this.skewAngle;
      if (var3 != null) {
         var3.setProgress(var1);
      }

   }
}
