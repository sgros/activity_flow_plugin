package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.CircleShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class EllipseContent implements KeyPathElementContent, PathContent, BaseKeyframeAnimation.AnimationListener {
   private final CircleShape circleShape;
   private boolean isPathValid;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final Path path = new Path();
   private final BaseKeyframeAnimation positionAnimation;
   private final BaseKeyframeAnimation sizeAnimation;
   private TrimPathContent trimPath;

   public EllipseContent(LottieDrawable var1, BaseLayer var2, CircleShape var3) {
      this.name = var3.getName();
      this.lottieDrawable = var1;
      this.sizeAnimation = var3.getSize().createAnimation();
      this.positionAnimation = var3.getPosition().createAnimation();
      this.circleShape = var3;
      var2.addAnimation(this.sizeAnimation);
      var2.addAnimation(this.positionAnimation);
      this.sizeAnimation.addUpdateListener(this);
      this.positionAnimation.addUpdateListener(this);
   }

   private void invalidate() {
      this.isPathValid = false;
      this.lottieDrawable.invalidateSelf();
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.ELLIPSE_SIZE) {
         this.sizeAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POSITION) {
         this.positionAnimation.setValueCallback(var2);
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
         PointF var1 = (PointF)this.sizeAnimation.getValue();
         float var2 = var1.x / 2.0F;
         float var3 = var1.y / 2.0F;
         float var4 = var2 * 0.55228F;
         float var5 = 0.55228F * var3;
         this.path.reset();
         float var6;
         float var7;
         float var9;
         Path var10;
         if (this.circleShape.isReversed()) {
            var10 = this.path;
            var6 = -var3;
            var10.moveTo(0.0F, var6);
            var10 = this.path;
            var7 = 0.0F - var4;
            float var8 = -var2;
            var9 = 0.0F - var5;
            var10.cubicTo(var7, var6, var8, var9, var8, 0.0F);
            var10 = this.path;
            var5 += 0.0F;
            var10.cubicTo(var8, var5, var7, var3, 0.0F, var3);
            var10 = this.path;
            var4 += 0.0F;
            var10.cubicTo(var4, var3, var2, var5, var2, 0.0F);
            this.path.cubicTo(var2, var9, var4, var6, 0.0F, var6);
         } else {
            var10 = this.path;
            var6 = -var3;
            var10.moveTo(0.0F, var6);
            var10 = this.path;
            var7 = var4 + 0.0F;
            var9 = 0.0F - var5;
            var10.cubicTo(var7, var6, var2, var9, var2, 0.0F);
            var10 = this.path;
            var5 += 0.0F;
            var10.cubicTo(var2, var5, var7, var3, 0.0F, var3);
            var10 = this.path;
            var4 = 0.0F - var4;
            var2 = -var2;
            var10.cubicTo(var4, var3, var2, var5, var2, 0.0F);
            this.path.cubicTo(var2, var9, var4, var6, 0.0F, var6);
         }

         var1 = (PointF)this.positionAnimation.getValue();
         this.path.offset(var1.x, var1.y);
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
