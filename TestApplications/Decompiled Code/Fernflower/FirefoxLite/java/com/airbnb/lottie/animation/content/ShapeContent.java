package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.Path.FillType;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.content.ShapePath;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.Utils;
import java.util.List;

public class ShapeContent implements PathContent, BaseKeyframeAnimation.AnimationListener {
   private boolean isPathValid;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final Path path = new Path();
   private final BaseKeyframeAnimation shapeAnimation;
   private TrimPathContent trimPath;

   public ShapeContent(LottieDrawable var1, BaseLayer var2, ShapePath var3) {
      this.name = var3.getName();
      this.lottieDrawable = var1;
      this.shapeAnimation = var3.getShapePath().createAnimation();
      var2.addAnimation(this.shapeAnimation);
      this.shapeAnimation.addUpdateListener(this);
   }

   private void invalidate() {
      this.isPathValid = false;
      this.lottieDrawable.invalidateSelf();
   }

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      if (this.isPathValid) {
         return this.path;
      } else {
         this.path.reset();
         this.path.set((Path)this.shapeAnimation.getValue());
         this.path.setFillType(FillType.EVEN_ODD);
         Utils.applyTrimPathIfNeeded(this.path, this.trimPath);
         this.isPathValid = true;
         return this.path;
      }
   }

   public void onValueChanged() {
      this.invalidate();
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
