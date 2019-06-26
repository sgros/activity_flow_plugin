package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.LottieValueCallback;

public class StrokeContent extends BaseStrokeContent {
   private final BaseKeyframeAnimation colorAnimation;
   private BaseKeyframeAnimation colorFilterAnimation;
   private final boolean hidden;
   private final BaseLayer layer;
   private final String name;

   public StrokeContent(LottieDrawable var1, BaseLayer var2, ShapeStroke var3) {
      super(var1, var2, var3.getCapType().toPaintCap(), var3.getJoinType().toPaintJoin(), var3.getMiterLimit(), var3.getOpacity(), var3.getWidth(), var3.getLineDashPattern(), var3.getDashOffset());
      this.layer = var2;
      this.name = var3.getName();
      this.hidden = var3.isHidden();
      this.colorAnimation = var3.getColor().createAnimation();
      this.colorAnimation.addUpdateListener(this);
      var2.addAnimation(this.colorAnimation);
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      super.addValueCallback(var1, var2);
      if (var1 == LottieProperty.STROKE_COLOR) {
         this.colorAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.COLOR_FILTER) {
         if (var2 == null) {
            this.colorFilterAnimation = null;
         } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(var2);
            this.colorFilterAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorAnimation);
         }
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      if (!this.hidden) {
         super.paint.setColor(((ColorKeyframeAnimation)this.colorAnimation).getIntValue());
         BaseKeyframeAnimation var4 = this.colorFilterAnimation;
         if (var4 != null) {
            super.paint.setColorFilter((ColorFilter)var4.getValue());
         }

         super.draw(var1, var2, var3);
      }
   }

   public String getName() {
      return this.name;
   }
}
