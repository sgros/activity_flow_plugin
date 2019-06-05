package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.Repeater;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class RepeaterContent implements DrawingContent, GreedyContent, KeyPathElementContent, PathContent, BaseKeyframeAnimation.AnimationListener {
   private ContentGroup contentGroup;
   private final BaseKeyframeAnimation copies;
   private final BaseLayer layer;
   private final LottieDrawable lottieDrawable;
   private final Matrix matrix = new Matrix();
   private final String name;
   private final BaseKeyframeAnimation offset;
   private final Path path = new Path();
   private final TransformKeyframeAnimation transform;

   public RepeaterContent(LottieDrawable var1, BaseLayer var2, Repeater var3) {
      this.lottieDrawable = var1;
      this.layer = var2;
      this.name = var3.getName();
      this.copies = var3.getCopies().createAnimation();
      var2.addAnimation(this.copies);
      this.copies.addUpdateListener(this);
      this.offset = var3.getOffset().createAnimation();
      var2.addAnimation(this.offset);
      this.offset.addUpdateListener(this);
      this.transform = var3.getTransform().createAnimation();
      this.transform.addAnimationsToLayer(var2);
      this.transform.addListener(this);
   }

   public void absorbContent(ListIterator var1) {
      if (this.contentGroup == null) {
         while(var1.hasPrevious() && var1.previous() != this) {
         }

         ArrayList var2 = new ArrayList();

         while(var1.hasPrevious()) {
            var2.add(var1.previous());
            var1.remove();
         }

         Collections.reverse(var2);
         this.contentGroup = new ContentGroup(this.lottieDrawable, this.layer, "Repeater", var2, (AnimatableTransform)null);
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (!this.transform.applyValueCallback(var1, var2)) {
         if (var1 == LottieProperty.REPEATER_COPIES) {
            this.copies.setValueCallback(var2);
         } else if (var1 == LottieProperty.REPEATER_OFFSET) {
            this.offset.setValueCallback(var2);
         }

      }
   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      float var4 = (Float)this.copies.getValue();
      float var5 = (Float)this.offset.getValue();
      float var6 = (Float)this.transform.getStartOpacity().getValue() / 100.0F;
      float var7 = (Float)this.transform.getEndOpacity().getValue() / 100.0F;

      for(int var8 = (int)var4 - 1; var8 >= 0; --var8) {
         this.matrix.set(var2);
         Matrix var9 = this.matrix;
         TransformKeyframeAnimation var10 = this.transform;
         float var11 = (float)var8;
         var9.preConcat(var10.getMatrixForRepeater(var11 + var5));
         float var12 = (float)var3;
         var11 = MiscUtils.lerp(var6, var7, var11 / var4);
         this.contentGroup.draw(var1, this.matrix, (int)(var12 * var11));
      }

   }

   public void getBounds(RectF var1, Matrix var2) {
      this.contentGroup.getBounds(var1, var2);
   }

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      Path var1 = this.contentGroup.getPath();
      this.path.reset();
      float var2 = (Float)this.copies.getValue();
      float var3 = (Float)this.offset.getValue();

      for(int var4 = (int)var2 - 1; var4 >= 0; --var4) {
         this.matrix.set(this.transform.getMatrixForRepeater((float)var4 + var3));
         this.path.addPath(var1, this.matrix);
      }

      return this.path;
   }

   public void onValueChanged() {
      this.lottieDrawable.invalidateSelf();
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      MiscUtils.resolveKeyPath(var1, var2, var3, var4, this);
   }

   public void setContents(List var1, List var2) {
      this.contentGroup.setContents(var1, var2);
   }
}
