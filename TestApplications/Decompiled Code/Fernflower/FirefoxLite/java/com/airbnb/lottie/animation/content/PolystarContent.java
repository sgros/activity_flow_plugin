package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PolystarContent implements KeyPathElementContent, PathContent, BaseKeyframeAnimation.AnimationListener {
   private final BaseKeyframeAnimation innerRadiusAnimation;
   private final BaseKeyframeAnimation innerRoundednessAnimation;
   private boolean isPathValid;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final BaseKeyframeAnimation outerRadiusAnimation;
   private final BaseKeyframeAnimation outerRoundednessAnimation;
   private final Path path = new Path();
   private final BaseKeyframeAnimation pointsAnimation;
   private final BaseKeyframeAnimation positionAnimation;
   private final BaseKeyframeAnimation rotationAnimation;
   private TrimPathContent trimPath;
   private final PolystarShape.Type type;

   public PolystarContent(LottieDrawable var1, BaseLayer var2, PolystarShape var3) {
      this.lottieDrawable = var1;
      this.name = var3.getName();
      this.type = var3.getType();
      this.pointsAnimation = var3.getPoints().createAnimation();
      this.positionAnimation = var3.getPosition().createAnimation();
      this.rotationAnimation = var3.getRotation().createAnimation();
      this.outerRadiusAnimation = var3.getOuterRadius().createAnimation();
      this.outerRoundednessAnimation = var3.getOuterRoundedness().createAnimation();
      if (this.type == PolystarShape.Type.Star) {
         this.innerRadiusAnimation = var3.getInnerRadius().createAnimation();
         this.innerRoundednessAnimation = var3.getInnerRoundedness().createAnimation();
      } else {
         this.innerRadiusAnimation = null;
         this.innerRoundednessAnimation = null;
      }

      var2.addAnimation(this.pointsAnimation);
      var2.addAnimation(this.positionAnimation);
      var2.addAnimation(this.rotationAnimation);
      var2.addAnimation(this.outerRadiusAnimation);
      var2.addAnimation(this.outerRoundednessAnimation);
      if (this.type == PolystarShape.Type.Star) {
         var2.addAnimation(this.innerRadiusAnimation);
         var2.addAnimation(this.innerRoundednessAnimation);
      }

      this.pointsAnimation.addUpdateListener(this);
      this.positionAnimation.addUpdateListener(this);
      this.rotationAnimation.addUpdateListener(this);
      this.outerRadiusAnimation.addUpdateListener(this);
      this.outerRoundednessAnimation.addUpdateListener(this);
      if (this.type == PolystarShape.Type.Star) {
         this.innerRadiusAnimation.addUpdateListener(this);
         this.innerRoundednessAnimation.addUpdateListener(this);
      }

   }

   private void createPolygonPath() {
      int var1 = (int)Math.floor((double)(Float)this.pointsAnimation.getValue());
      double var2;
      if (this.rotationAnimation == null) {
         var2 = 0.0D;
      } else {
         var2 = (double)(Float)this.rotationAnimation.getValue();
      }

      double var4 = Math.toRadians(var2 - 90.0D);
      double var6 = (double)var1;
      float var8 = (float)(6.283185307179586D / var6);
      float var9 = (Float)this.outerRoundednessAnimation.getValue() / 100.0F;
      float var10 = (Float)this.outerRadiusAnimation.getValue();
      double var11 = (double)var10;
      float var13 = (float)(Math.cos(var4) * var11);
      float var14 = (float)(Math.sin(var4) * var11);
      this.path.moveTo(var13, var14);
      var2 = (double)var8;
      var4 += var2;
      var6 = Math.ceil(var6);

      for(var1 = 0; (double)var1 < var6; var13 = var8) {
         var8 = (float)(Math.cos(var4) * var11);
         float var15 = (float)(var11 * Math.sin(var4));
         if (var9 != 0.0F) {
            double var16 = (double)((float)(Math.atan2((double)var14, (double)var13) - 1.5707963267948966D));
            float var18 = (float)Math.cos(var16);
            float var19 = (float)Math.sin(var16);
            var16 = (double)((float)(Math.atan2((double)var15, (double)var8) - 1.5707963267948966D));
            float var20 = (float)Math.cos(var16);
            float var21 = (float)Math.sin(var16);
            float var22 = var10 * var9 * 0.25F;
            this.path.cubicTo(var13 - var18 * var22, var14 - var19 * var22, var8 + var20 * var22, var15 + var22 * var21, var8, var15);
         } else {
            this.path.lineTo(var8, var15);
         }

         var4 += var2;
         ++var1;
         var14 = var15;
      }

      PointF var23 = (PointF)this.positionAnimation.getValue();
      this.path.offset(var23.x, var23.y);
      this.path.close();
   }

   private void createStarPath() {
      float var1 = (Float)this.pointsAnimation.getValue();
      double var2;
      if (this.rotationAnimation == null) {
         var2 = 0.0D;
      } else {
         var2 = (double)(Float)this.rotationAnimation.getValue();
      }

      double var4 = Math.toRadians(var2 - 90.0D);
      double var6 = (double)var1;
      float var8 = (float)(6.283185307179586D / var6);
      float var9 = var8 / 2.0F;
      float var10 = var1 - (float)((int)var1);
      float var35;
      int var11 = (var35 = var10 - 0.0F) == 0.0F ? 0 : (var35 < 0.0F ? -1 : 1);
      var2 = var4;
      if (var11 != 0) {
         var2 = var4 + (double)((1.0F - var10) * var9);
      }

      float var12 = (Float)this.outerRadiusAnimation.getValue();
      var1 = (Float)this.innerRadiusAnimation.getValue();
      float var13;
      if (this.innerRoundednessAnimation != null) {
         var13 = (Float)this.innerRoundednessAnimation.getValue() / 100.0F;
      } else {
         var13 = 0.0F;
      }

      float var14;
      if (this.outerRoundednessAnimation != null) {
         var14 = (Float)this.outerRoundednessAnimation.getValue() / 100.0F;
      } else {
         var14 = 0.0F;
      }

      float var15;
      float var16;
      float var17;
      if (var11 != 0) {
         var15 = (var12 - var1) * var10 + var1;
         var4 = (double)var15;
         var16 = (float)(var4 * Math.cos(var2));
         var17 = (float)(var4 * Math.sin(var2));
         this.path.moveTo(var16, var17);
         var2 += (double)(var8 * var10 / 2.0F);
      } else {
         var4 = (double)var12;
         var16 = (float)(Math.cos(var2) * var4);
         var17 = (float)(var4 * Math.sin(var2));
         this.path.moveTo(var16, var17);
         var2 += (double)var9;
         var15 = 0.0F;
      }

      var4 = Math.ceil(var6) * 2.0D;
      int var18 = 0;
      boolean var19 = false;
      float var20 = var17;
      float var21 = var16;

      while(true) {
         var6 = (double)var18;
         if (var6 >= var4) {
            PointF var34 = (PointF)this.positionAnimation.getValue();
            this.path.offset(var34.x, var34.y);
            this.path.close();
            return;
         }

         if (var19) {
            var16 = var12;
         } else {
            var16 = var1;
         }

         float var36;
         int var22 = (var36 = var15 - 0.0F) == 0.0F ? 0 : (var36 < 0.0F ? -1 : 1);
         float var23;
         if (var22 != 0 && var6 == var4 - 2.0D) {
            var23 = var8 * var10 / 2.0F;
         } else {
            var23 = var9;
         }

         if (var22 != 0 && var6 == var4 - 1.0D) {
            var16 = var15;
         }

         double var24 = (double)var16;
         float var26 = (float)(var24 * Math.cos(var2));
         float var27 = (float)(var24 * Math.sin(var2));
         if (var13 == 0.0F && var14 == 0.0F) {
            this.path.lineTo(var26, var27);
         } else {
            var16 = var13;
            var24 = (double)var20;
            var24 = (double)((float)(Math.atan2(var24, (double)var21) - 1.5707963267948966D));
            float var28 = (float)Math.cos(var24);
            float var29 = (float)Math.sin(var24);
            var24 = (double)((float)(Math.atan2((double)var27, (double)var26) - 1.5707963267948966D));
            float var30 = (float)Math.cos(var24);
            float var31 = (float)Math.sin(var24);
            float var32;
            if (var19) {
               var32 = var13;
            } else {
               var32 = var14;
            }

            if (var19) {
               var16 = var14;
            }

            float var33;
            if (var19) {
               var33 = var1;
            } else {
               var33 = var12;
            }

            if (var19) {
               var17 = var12;
            } else {
               var17 = var1;
            }

            var32 = var33 * var32 * 0.47829F;
            var28 *= var32;
            var29 = var32 * var29;
            var16 = var17 * var16 * 0.47829F;
            var30 *= var16;
            var31 = var16 * var31;
            var32 = var28;
            var17 = var30;
            var33 = var29;
            var16 = var31;
            if (var11 != 0) {
               if (var18 == 0) {
                  var32 = var28 * var10;
                  var33 = var29 * var10;
                  var17 = var30;
                  var16 = var31;
               } else {
                  var32 = var28;
                  var17 = var30;
                  var33 = var29;
                  var16 = var31;
                  if (var6 == var4 - 1.0D) {
                     var17 = var30 * var10;
                     var16 = var31 * var10;
                     var33 = var29;
                     var32 = var28;
                  }
               }
            }

            this.path.cubicTo(var21 - var32, var20 - var33, var26 + var17, var27 + var16, var26, var27);
         }

         var2 += (double)var23;
         var19 ^= true;
         ++var18;
         var20 = var27;
         var21 = var26;
      }
   }

   private void invalidate() {
      this.isPathValid = false;
      this.lottieDrawable.invalidateSelf();
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.POLYSTAR_POINTS) {
         this.pointsAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POLYSTAR_ROTATION) {
         this.rotationAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POSITION) {
         this.positionAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POLYSTAR_INNER_RADIUS && this.innerRadiusAnimation != null) {
         this.innerRadiusAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POLYSTAR_OUTER_RADIUS) {
         this.outerRadiusAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS && this.innerRoundednessAnimation != null) {
         this.innerRoundednessAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
         this.outerRoundednessAnimation.setValueCallback(var2);
      }

   }

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      if (this.isPathValid) {
         return this.path;
      } else {
         this.path.reset();
         switch(this.type) {
         case Star:
            this.createStarPath();
            break;
         case Polygon:
            this.createPolygonPath();
         }

         this.path.close();
         Utils.applyTrimPathIfNeeded(this.path, this.trimPath);
         this.isPathValid = true;
         return this.path;
      }
   }

   public void onValueChanged() {
      this.invalidate();
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      MiscUtils.resolveKeyPath(var1, var2, var3, var4, this);
   }

   public void setContents(List var1, List var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         Content var4 = (Content)var1.get(var3);
         if (var4 instanceof TrimPathContent) {
            TrimPathContent var5 = (TrimPathContent)var4;
            if (var5.getType() == ShapeTrimPath.Type.Simultaneously) {
               this.trimPath = var5;
               this.trimPath.addListener(this);
            }
         }
      }

   }
}
