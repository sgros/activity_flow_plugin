package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class RectangleContent implements KeyPathElementContent, PathContent, BaseKeyframeAnimation.AnimationListener {
   private final BaseKeyframeAnimation cornerRadiusAnimation;
   private boolean isPathValid;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final Path path = new Path();
   private final BaseKeyframeAnimation positionAnimation;
   private final RectF rect = new RectF();
   private final BaseKeyframeAnimation sizeAnimation;
   private TrimPathContent trimPath;

   public RectangleContent(LottieDrawable var1, BaseLayer var2, RectangleShape var3) {
      this.name = var3.getName();
      this.lottieDrawable = var1;
      this.positionAnimation = var3.getPosition().createAnimation();
      this.sizeAnimation = var3.getSize().createAnimation();
      this.cornerRadiusAnimation = var3.getCornerRadius().createAnimation();
      var2.addAnimation(this.positionAnimation);
      var2.addAnimation(this.sizeAnimation);
      var2.addAnimation(this.cornerRadiusAnimation);
      this.positionAnimation.addUpdateListener(this);
      this.sizeAnimation.addUpdateListener(this);
      this.cornerRadiusAnimation.addUpdateListener(this);
   }

   private void invalidate() {
      this.isPathValid = false;
      this.lottieDrawable.invalidateSelf();
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
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
         float var4;
         if (this.cornerRadiusAnimation == null) {
            var4 = 0.0F;
         } else {
            var4 = (Float)this.cornerRadiusAnimation.getValue();
         }

         float var5 = Math.min(var2, var3);
         float var6 = var4;
         if (var4 > var5) {
            var6 = var5;
         }

         var1 = (PointF)this.positionAnimation.getValue();
         this.path.moveTo(var1.x + var2, var1.y - var3 + var6);
         this.path.lineTo(var1.x + var2, var1.y + var3 - var6);
         float var11;
         int var7 = (var11 = var6 - 0.0F) == 0.0F ? 0 : (var11 < 0.0F ? -1 : 1);
         RectF var8;
         if (var7 > 0) {
            var8 = this.rect;
            var5 = var1.x;
            var4 = var6 * 2.0F;
            var8.set(var5 + var2 - var4, var1.y + var3 - var4, var1.x + var2, var1.y + var3);
            this.path.arcTo(this.rect, 0.0F, 90.0F, false);
         }

         this.path.lineTo(var1.x - var2 + var6, var1.y + var3);
         float var9;
         if (var7 > 0) {
            var8 = this.rect;
            var5 = var1.x;
            var4 = var1.y;
            var9 = var6 * 2.0F;
            var8.set(var5 - var2, var4 + var3 - var9, var1.x - var2 + var9, var1.y + var3);
            this.path.arcTo(this.rect, 90.0F, 90.0F, false);
         }

         this.path.lineTo(var1.x - var2, var1.y - var3 + var6);
         if (var7 > 0) {
            var8 = this.rect;
            var4 = var1.x;
            float var10 = var1.y;
            var5 = var1.x;
            var9 = var6 * 2.0F;
            var8.set(var4 - var2, var10 - var3, var5 - var2 + var9, var1.y - var3 + var9);
            this.path.arcTo(this.rect, 180.0F, 90.0F, false);
         }

         this.path.lineTo(var1.x + var2 - var6, var1.y - var3);
         if (var7 > 0) {
            var8 = this.rect;
            var4 = var1.x;
            var6 *= 2.0F;
            var8.set(var4 + var2 - var6, var1.y - var3, var1.x + var2, var1.y - var3 + var6);
            this.path.arcTo(this.rect, 270.0F, 90.0F, false);
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
