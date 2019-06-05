package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v4.util.LongSparseArray;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public class CompositionLayer extends BaseLayer {
   private final List layers = new ArrayList();
   private final RectF newClipRect = new RectF();
   private final RectF rect = new RectF();
   private BaseKeyframeAnimation timeRemapping;

   public CompositionLayer(LottieDrawable var1, Layer var2, List var3, LottieComposition var4) {
      super(var1, var2);
      AnimatableFloatValue var11 = var2.getTimeRemapping();
      if (var11 != null) {
         this.timeRemapping = var11.createAnimation();
         this.addAnimation(this.timeRemapping);
         this.timeRemapping.addUpdateListener(this);
      } else {
         this.timeRemapping = null;
      }

      LongSparseArray var5 = new LongSparseArray(var4.getLayers().size());
      int var6 = var3.size() - 1;
      BaseLayer var12 = null;

      while(true) {
         int var7 = 0;
         if (var6 < 0) {
            for(; var7 < var5.size(); ++var7) {
               BaseLayer var10 = (BaseLayer)var5.get(var5.keyAt(var7));
               if (var10 != null) {
                  var12 = (BaseLayer)var5.get(var10.getLayerModel().getParentId());
                  if (var12 != null) {
                     var10.setParentLayer(var12);
                  }
               }
            }

            return;
         }

         Layer var8 = (Layer)var3.get(var6);
         BaseLayer var9 = BaseLayer.forModel(var8, var1, var4);
         if (var9 != null) {
            var5.put(var9.getLayerModel().getId(), var9);
            if (var12 != null) {
               var12.setMatteLayer(var9);
               var12 = null;
            } else {
               this.layers.add(0, var9);
               switch(var8.getMatteType()) {
               case Add:
               case Invert:
                  var12 = var9;
               }
            }
         }

         --var6;
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      super.addValueCallback(var1, var2);
      if (var1 == LottieProperty.TIME_REMAP) {
         if (var2 == null) {
            this.timeRemapping = null;
         } else {
            this.timeRemapping = new ValueCallbackKeyframeAnimation(var2);
            this.addAnimation(this.timeRemapping);
         }
      }

   }

   void drawLayer(Canvas var1, Matrix var2, int var3) {
      L.beginSection("CompositionLayer#draw");
      var1.save();
      this.newClipRect.set(0.0F, 0.0F, (float)this.layerModel.getPreCompWidth(), (float)this.layerModel.getPreCompHeight());
      var2.mapRect(this.newClipRect);

      for(int var4 = this.layers.size() - 1; var4 >= 0; --var4) {
         boolean var5;
         if (!this.newClipRect.isEmpty()) {
            var5 = var1.clipRect(this.newClipRect);
         } else {
            var5 = true;
         }

         if (var5) {
            ((BaseLayer)this.layers.get(var4)).draw(var1, var2, var3);
         }
      }

      var1.restore();
      L.endSection("CompositionLayer#draw");
   }

   public void getBounds(RectF var1, Matrix var2) {
      super.getBounds(var1, var2);
      this.rect.set(0.0F, 0.0F, 0.0F, 0.0F);

      for(int var3 = this.layers.size() - 1; var3 >= 0; --var3) {
         ((BaseLayer)this.layers.get(var3)).getBounds(this.rect, this.boundsMatrix);
         if (var1.isEmpty()) {
            var1.set(this.rect);
         } else {
            var1.set(Math.min(var1.left, this.rect.left), Math.min(var1.top, this.rect.top), Math.max(var1.right, this.rect.right), Math.max(var1.bottom, this.rect.bottom));
         }
      }

   }

   protected void resolveChildKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      for(int var5 = 0; var5 < this.layers.size(); ++var5) {
         ((BaseLayer)this.layers.get(var5)).resolveKeyPath(var1, var2, var3, var4);
      }

   }

   public void setProgress(float var1) {
      super.setProgress(var1);
      if (this.timeRemapping != null) {
         var1 = this.lottieDrawable.getComposition().getDuration();
         var1 = (float)((long)((Float)this.timeRemapping.getValue() * 1000.0F)) / var1;
      }

      float var2 = var1;
      if (this.layerModel.getTimeStretch() != 0.0F) {
         var2 = var1 / this.layerModel.getTimeStretch();
      }

      var1 = this.layerModel.getStartProgress();

      for(int var3 = this.layers.size() - 1; var3 >= 0; --var3) {
         ((BaseLayer)this.layers.get(var3)).setProgress(var2 - var1);
      }

   }
}
