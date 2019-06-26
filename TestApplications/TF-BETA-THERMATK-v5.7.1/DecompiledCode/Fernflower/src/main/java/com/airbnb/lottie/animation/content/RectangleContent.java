package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class RectangleContent implements BaseKeyframeAnimation.AnimationListener, KeyPathElementContent, PathContent {
   private final BaseKeyframeAnimation cornerRadiusAnimation;
   private final boolean hidden;
   private boolean isPathValid;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final Path path = new Path();
   private final BaseKeyframeAnimation positionAnimation;
   private final RectF rect = new RectF();
   private final BaseKeyframeAnimation sizeAnimation;
   private CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();

   public RectangleContent(LottieDrawable var1, BaseLayer var2, RectangleShape var3) {
      this.name = var3.getName();
      this.hidden = var3.isHidden();
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
      if (var1 == LottieProperty.RECTANGLE_SIZE) {
         this.sizeAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POSITION) {
         this.positionAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.CORNER_RADIUS) {
         this.cornerRadiusAnimation.setValueCallback(var2);
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
         if (this.hidden) {
            this.isPathValid = true;
            return this.path;
         } else {
            PointF var1 = (PointF)this.sizeAnimation.getValue();
            float var2 = var1.x / 2.0F;
            float var3 = var1.y / 2.0F;
            BaseKeyframeAnimation var9 = this.cornerRadiusAnimation;
            float var4;
            if (var9 == null) {
               var4 = 0.0F;
            } else {
               var4 = ((FloatKeyframeAnimation)var9).getFloatValue();
            }

            float var5 = Math.min(var2, var3);
            float var6 = var4;
            if (var4 > var5) {
               var6 = var5;
            }

            var1 = (PointF)this.positionAnimation.getValue();
            this.path.moveTo(var1.x + var2, var1.y - var3 + var6);
            this.path.lineTo(var1.x + var2, var1.y + var3 - var6);
            RectF var7;
            float var8;
            if (var6 > 0.0F) {
               var7 = this.rect;
               var4 = var1.x;
               var5 = var6 * 2.0F;
               var8 = var1.y;
               var7.set(var4 + var2 - var5, var8 + var3 - var5, var4 + var2, var8 + var3);
               this.path.arcTo(this.rect, 0.0F, 90.0F, false);
            }

            this.path.lineTo(var1.x - var2 + var6, var1.y + var3);
            if (var6 > 0.0F) {
               var7 = this.rect;
               var8 = var1.x;
               var4 = var1.y;
               var5 = var6 * 2.0F;
               var7.set(var8 - var2, var4 + var3 - var5, var8 - var2 + var5, var4 + var3);
               this.path.arcTo(this.rect, 90.0F, 90.0F, false);
            }

            this.path.lineTo(var1.x - var2, var1.y - var3 + var6);
            if (var6 > 0.0F) {
               var7 = this.rect;
               var4 = var1.x;
               var8 = var1.y;
               var5 = var6 * 2.0F;
               var7.set(var4 - var2, var8 - var3, var4 - var2 + var5, var8 - var3 + var5);
               this.path.arcTo(this.rect, 180.0F, 90.0F, false);
            }

            this.path.lineTo(var1.x + var2 - var6, var1.y - var3);
            if (var6 > 0.0F) {
               var7 = this.rect;
               var4 = var1.x;
               var6 *= 2.0F;
               var5 = var1.y;
               var7.set(var4 + var2 - var6, var5 - var3, var4 + var2, var5 - var3 + var6);
               this.path.arcTo(this.rect, 270.0F, 90.0F, false);
            }

            this.path.close();
            this.trimPaths.apply(this.path);
            this.isPathValid = true;
            return this.path;
         }
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
            if (var5.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
               this.trimPaths.addTrimPath(var5);
               var5.addListener(this);
            }
         }
      }

   }
}
