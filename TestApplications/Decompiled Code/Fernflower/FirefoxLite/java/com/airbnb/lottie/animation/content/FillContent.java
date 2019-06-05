package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.ShapeFill;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public class FillContent implements DrawingContent, KeyPathElementContent, BaseKeyframeAnimation.AnimationListener {
   private final BaseKeyframeAnimation colorAnimation;
   private BaseKeyframeAnimation colorFilterAnimation;
   private final BaseLayer layer;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final BaseKeyframeAnimation opacityAnimation;
   private final Paint paint = new Paint(1);
   private final Path path = new Path();
   private final List paths = new ArrayList();

   public FillContent(LottieDrawable var1, BaseLayer var2, ShapeFill var3) {
      this.layer = var2;
      this.name = var3.getName();
      this.lottieDrawable = var1;
      if (var3.getColor() != null && var3.getOpacity() != null) {
         this.path.setFillType(var3.getFillType());
         this.colorAnimation = var3.getColor().createAnimation();
         this.colorAnimation.addUpdateListener(this);
         var2.addAnimation(this.colorAnimation);
         this.opacityAnimation = var3.getOpacity().createAnimation();
         this.opacityAnimation.addUpdateListener(this);
         var2.addAnimation(this.opacityAnimation);
      } else {
         this.colorAnimation = null;
         this.opacityAnimation = null;
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.COLOR) {
         this.colorAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.OPACITY) {
         this.opacityAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.COLOR_FILTER) {
         if (var2 == null) {
            this.colorFilterAnimation = null;
         } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(var2);
            this.colorFilterAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorFilterAnimation);
         }
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      L.beginSection("FillContent#draw");
      this.paint.setColor((Integer)this.colorAnimation.getValue());
      int var4 = (int)((float)var3 / 255.0F * (float)(Integer)this.opacityAnimation.getValue() / 100.0F * 255.0F);
      Paint var5 = this.paint;
      var3 = 0;
      var5.setAlpha(MiscUtils.clamp(var4, 0, 255));
      if (this.colorFilterAnimation != null) {
         this.paint.setColorFilter((ColorFilter)this.colorFilterAnimation.getValue());
      }

      this.path.reset();

      while(var3 < this.paths.size()) {
         this.path.addPath(((PathContent)this.paths.get(var3)).getPath(), var2);
         ++var3;
      }

      var1.drawPath(this.path, this.paint);
      L.endSection("FillContent#draw");
   }

   public void getBounds(RectF var1, Matrix var2) {
      this.path.reset();

      for(int var3 = 0; var3 < this.paths.size(); ++var3) {
         this.path.addPath(((PathContent)this.paths.get(var3)).getPath(), var2);
      }

      this.path.computeBounds(var1, false);
      var1.set(var1.left - 1.0F, var1.top - 1.0F, var1.right + 1.0F, var1.bottom + 1.0F);
   }

   public String getName() {
      return this.name;
   }

   public void onValueChanged() {
      this.lottieDrawable.invalidateSelf();
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      MiscUtils.resolveKeyPath(var1, var2, var3, var4, this);
   }

   public void setContents(List var1, List var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Content var4 = (Content)var2.get(var3);
         if (var4 instanceof PathContent) {
            this.paths.add((PathContent)var4);
         }
      }

   }
}
