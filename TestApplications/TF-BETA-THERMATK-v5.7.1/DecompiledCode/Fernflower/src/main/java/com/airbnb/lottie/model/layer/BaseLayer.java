package com.airbnb.lottie.model.layer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.content.DrawingContent;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.MaskKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseLayer implements DrawingContent, BaseKeyframeAnimation.AnimationListener, KeyPathElement {
   private final List animations;
   final Matrix boundsMatrix;
   private final Paint clearPaint;
   private final Paint contentPaint = new LPaint(1);
   private final String drawTraceName;
   private final Paint dstInPaint;
   private final Paint dstOutPaint;
   final Layer layerModel;
   final LottieDrawable lottieDrawable;
   private MaskKeyframeAnimation mask;
   private final RectF maskBoundsRect;
   private final Matrix matrix = new Matrix();
   private final RectF matteBoundsRect;
   private BaseLayer matteLayer;
   private final Paint mattePaint;
   private BaseLayer parentLayer;
   private List parentLayers;
   private final Path path = new Path();
   private final RectF rect;
   private final RectF tempMaskBoundsRect;
   final TransformKeyframeAnimation transform;
   private boolean visible;

   BaseLayer(LottieDrawable var1, Layer var2) {
      this.dstInPaint = new LPaint(1, Mode.DST_IN);
      this.dstOutPaint = new LPaint(1, Mode.DST_OUT);
      this.mattePaint = new LPaint(1);
      this.clearPaint = new LPaint(Mode.CLEAR);
      this.rect = new RectF();
      this.maskBoundsRect = new RectF();
      this.matteBoundsRect = new RectF();
      this.tempMaskBoundsRect = new RectF();
      this.boundsMatrix = new Matrix();
      this.animations = new ArrayList();
      this.visible = true;
      this.lottieDrawable = var1;
      this.layerModel = var2;
      StringBuilder var3 = new StringBuilder();
      var3.append(var2.getName());
      var3.append("#draw");
      this.drawTraceName = var3.toString();
      if (var2.getMatteType() == Layer.MatteType.INVERT) {
         this.mattePaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
      } else {
         this.mattePaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
      }

      this.transform = var2.getTransform().createAnimation();
      this.transform.addListener(this);
      if (var2.getMasks() != null && !var2.getMasks().isEmpty()) {
         this.mask = new MaskKeyframeAnimation(var2.getMasks());
         Iterator var4 = this.mask.getMaskAnimations().iterator();

         while(var4.hasNext()) {
            ((BaseKeyframeAnimation)var4.next()).addUpdateListener(this);
         }

         var4 = this.mask.getOpacityAnimations().iterator();

         while(var4.hasNext()) {
            BaseKeyframeAnimation var5 = (BaseKeyframeAnimation)var4.next();
            this.addAnimation(var5);
            var5.addUpdateListener(this);
         }
      }

      this.setupInOutAnimations();
   }

   private void applyAddMask(Canvas var1, Matrix var2, Mask var3, BaseKeyframeAnimation var4, BaseKeyframeAnimation var5) {
      Path var6 = (Path)var4.getValue();
      this.path.set(var6);
      this.path.transform(var2);
      this.contentPaint.setAlpha((int)((float)(Integer)var5.getValue() * 2.55F));
      var1.drawPath(this.path, this.contentPaint);
   }

   private void applyIntersectMask(Canvas var1, Matrix var2, Mask var3, BaseKeyframeAnimation var4, BaseKeyframeAnimation var5) {
      this.saveLayerCompat(var1, this.rect, this.dstInPaint, true);
      Path var6 = (Path)var4.getValue();
      this.path.set(var6);
      this.path.transform(var2);
      this.contentPaint.setAlpha((int)((float)(Integer)var5.getValue() * 2.55F));
      var1.drawPath(this.path, this.contentPaint);
      var1.restore();
   }

   private void applyInvertedAddMask(Canvas var1, Matrix var2, Mask var3, BaseKeyframeAnimation var4, BaseKeyframeAnimation var5) {
      this.saveLayerCompat(var1, this.rect, this.contentPaint, true);
      var1.drawRect(this.rect, this.contentPaint);
      Path var6 = (Path)var4.getValue();
      this.path.set(var6);
      this.path.transform(var2);
      this.contentPaint.setAlpha((int)((float)(Integer)var5.getValue() * 2.55F));
      var1.drawPath(this.path, this.dstOutPaint);
      var1.restore();
   }

   private void applyInvertedIntersectMask(Canvas var1, Matrix var2, Mask var3, BaseKeyframeAnimation var4, BaseKeyframeAnimation var5) {
      this.saveLayerCompat(var1, this.rect, this.dstInPaint, true);
      var1.drawRect(this.rect, this.contentPaint);
      this.dstOutPaint.setAlpha((int)((float)(Integer)var5.getValue() * 2.55F));
      Path var6 = (Path)var4.getValue();
      this.path.set(var6);
      this.path.transform(var2);
      var1.drawPath(this.path, this.dstOutPaint);
      var1.restore();
   }

   private void applyInvertedSubtractMask(Canvas var1, Matrix var2, Mask var3, BaseKeyframeAnimation var4, BaseKeyframeAnimation var5) {
      this.saveLayerCompat(var1, this.rect, this.dstOutPaint, true);
      var1.drawRect(this.rect, this.contentPaint);
      this.dstOutPaint.setAlpha((int)((float)(Integer)var5.getValue() * 2.55F));
      Path var6 = (Path)var4.getValue();
      this.path.set(var6);
      this.path.transform(var2);
      var1.drawPath(this.path, this.dstOutPaint);
      var1.restore();
   }

   private void applyMasks(Canvas var1, Matrix var2) {
      L.beginSection("Layer#saveLayer");
      RectF var3 = this.rect;
      Paint var4 = this.dstInPaint;
      int var5 = 0;
      this.saveLayerCompat(var1, var3, var4, false);
      L.endSection("Layer#saveLayer");

      for(; var5 < this.mask.getMasks().size(); ++var5) {
         Mask var6 = (Mask)this.mask.getMasks().get(var5);
         BaseKeyframeAnimation var10 = (BaseKeyframeAnimation)this.mask.getMaskAnimations().get(var5);
         BaseKeyframeAnimation var7 = (BaseKeyframeAnimation)this.mask.getOpacityAnimations().get(var5);
         int var8 = null.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[var6.getMaskMode().ordinal()];
         if (var8 != 1) {
            if (var8 != 2) {
               if (var8 == 3) {
                  if (var6.isInverted()) {
                     this.applyInvertedAddMask(var1, var2, var6, var10, var7);
                  } else {
                     this.applyAddMask(var1, var2, var6, var10, var7);
                  }
               }
            } else if (var6.isInverted()) {
               this.applyInvertedIntersectMask(var1, var2, var6, var10, var7);
            } else {
               this.applyIntersectMask(var1, var2, var6, var10, var7);
            }
         } else {
            if (var5 == 0) {
               Paint var9 = new Paint();
               var9.setColor(-16777216);
               var1.drawRect(this.rect, var9);
            }

            if (var6.isInverted()) {
               this.applyInvertedSubtractMask(var1, var2, var6, var10, var7);
            } else {
               this.applySubtractMask(var1, var2, var6, var10, var7);
            }
         }
      }

      L.beginSection("Layer#restoreLayer");
      var1.restore();
      L.endSection("Layer#restoreLayer");
   }

   private void applySubtractMask(Canvas var1, Matrix var2, Mask var3, BaseKeyframeAnimation var4, BaseKeyframeAnimation var5) {
      Path var6 = (Path)var4.getValue();
      this.path.set(var6);
      this.path.transform(var2);
      var1.drawPath(this.path, this.dstOutPaint);
   }

   private void buildParentLayerListIfNeeded() {
      if (this.parentLayers == null) {
         if (this.parentLayer == null) {
            this.parentLayers = Collections.emptyList();
         } else {
            this.parentLayers = new ArrayList();

            for(BaseLayer var1 = this.parentLayer; var1 != null; var1 = var1.parentLayer) {
               this.parentLayers.add(var1);
            }

         }
      }
   }

   private void clearCanvas(Canvas var1) {
      L.beginSection("Layer#clearLayer");
      RectF var2 = this.rect;
      var1.drawRect(var2.left - 1.0F, var2.top - 1.0F, var2.right + 1.0F, var2.bottom + 1.0F, this.clearPaint);
      L.endSection("Layer#clearLayer");
   }

   static BaseLayer forModel(Layer var0, LottieDrawable var1, LottieComposition var2) {
      switch(var0.getLayerType()) {
      case SHAPE:
         return new ShapeLayer(var1, var0);
      case PRE_COMP:
         return new CompositionLayer(var1, var0, var2.getPrecomps(var0.getRefId()), var2);
      case SOLID:
         return new SolidLayer(var1, var0);
      case IMAGE:
         return new ImageLayer(var1, var0);
      case NULL:
         return new NullLayer(var1, var0);
      case TEXT:
         return new TextLayer(var1, var0);
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Unknown layer type ");
         var3.append(var0.getLayerType());
         Logger.warning(var3.toString());
         return null;
      }
   }

   private void intersectBoundsWithMask(RectF var1, Matrix var2) {
      this.maskBoundsRect.set(0.0F, 0.0F, 0.0F, 0.0F);
      if (this.hasMasksOnThisLayer()) {
         int var3 = this.mask.getMasks().size();

         for(int var4 = 0; var4 < var3; ++var4) {
            Mask var5 = (Mask)this.mask.getMasks().get(var4);
            Path var6 = (Path)((BaseKeyframeAnimation)this.mask.getMaskAnimations().get(var4)).getValue();
            this.path.set(var6);
            this.path.transform(var2);
            int var7 = null.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[var5.getMaskMode().ordinal()];
            if (var7 == 1) {
               return;
            }

            if ((var7 == 2 || var7 == 3) && var5.isInverted()) {
               return;
            }

            this.path.computeBounds(this.tempMaskBoundsRect, false);
            if (var4 == 0) {
               this.maskBoundsRect.set(this.tempMaskBoundsRect);
            } else {
               RectF var8 = this.maskBoundsRect;
               var8.set(Math.min(var8.left, this.tempMaskBoundsRect.left), Math.min(this.maskBoundsRect.top, this.tempMaskBoundsRect.top), Math.max(this.maskBoundsRect.right, this.tempMaskBoundsRect.right), Math.max(this.maskBoundsRect.bottom, this.tempMaskBoundsRect.bottom));
            }
         }

         if (!var1.intersect(this.maskBoundsRect)) {
            var1.set(0.0F, 0.0F, 0.0F, 0.0F);
         }

      }
   }

   private void intersectBoundsWithMatte(RectF var1, Matrix var2) {
      if (this.hasMatteOnThisLayer()) {
         if (this.layerModel.getMatteType() != Layer.MatteType.INVERT) {
            this.matteBoundsRect.set(0.0F, 0.0F, 0.0F, 0.0F);
            this.matteLayer.getBounds(this.matteBoundsRect, var2, true);
            if (!var1.intersect(this.matteBoundsRect)) {
               var1.set(0.0F, 0.0F, 0.0F, 0.0F);
            }

         }
      }
   }

   private void invalidateSelf() {
      this.lottieDrawable.invalidateSelf();
   }

   private void recordRenderTime(float var1) {
      this.lottieDrawable.getComposition().getPerformanceTracker().recordRenderTime(this.layerModel.getName(), var1);
   }

   @SuppressLint({"WrongConstant"})
   private void saveLayerCompat(Canvas var1, RectF var2, Paint var3, boolean var4) {
      if (VERSION.SDK_INT < 23) {
         byte var5;
         if (var4) {
            var5 = 31;
         } else {
            var5 = 19;
         }

         var1.saveLayer(var2, var3, var5);
      } else {
         var1.saveLayer(var2, var3);
      }

   }

   private void setVisible(boolean var1) {
      if (var1 != this.visible) {
         this.visible = var1;
         this.invalidateSelf();
      }

   }

   private void setupInOutAnimations() {
      boolean var1 = this.layerModel.getInOutKeyframes().isEmpty();
      boolean var2 = true;
      if (!var1) {
         final FloatKeyframeAnimation var3 = new FloatKeyframeAnimation(this.layerModel.getInOutKeyframes());
         var3.setIsDiscrete();
         var3.addUpdateListener(new BaseKeyframeAnimation.AnimationListener() {
            public void onValueChanged() {
               BaseLayer var1 = BaseLayer.this;
               boolean var2;
               if (var3.getFloatValue() == 1.0F) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               var1.setVisible(var2);
            }
         });
         if ((Float)var3.getValue() != 1.0F) {
            var2 = false;
         }

         this.setVisible(var2);
         this.addAnimation(var3);
      } else {
         this.setVisible(true);
      }

   }

   public void addAnimation(BaseKeyframeAnimation var1) {
      if (var1 != null) {
         this.animations.add(var1);
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      this.transform.applyValueCallback(var1, var2);
   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      L.beginSection(this.drawTraceName);
      if (this.visible && !this.layerModel.isHidden()) {
         this.buildParentLayerListIfNeeded();
         L.beginSection("Layer#parentMatrix");
         this.matrix.reset();
         this.matrix.set(var2);

         int var4;
         for(var4 = this.parentLayers.size() - 1; var4 >= 0; --var4) {
            this.matrix.preConcat(((BaseLayer)this.parentLayers.get(var4)).transform.getMatrix());
         }

         L.endSection("Layer#parentMatrix");
         if (this.transform.getOpacity() == null) {
            var4 = 100;
         } else {
            var4 = (Integer)this.transform.getOpacity().getValue();
         }

         var3 = (int)((float)var3 / 255.0F * (float)var4 / 100.0F * 255.0F);
         if (!this.hasMatteOnThisLayer() && !this.hasMasksOnThisLayer()) {
            this.matrix.preConcat(this.transform.getMatrix());
            L.beginSection("Layer#drawLayer");
            this.drawLayer(var1, this.matrix, var3);
            L.endSection("Layer#drawLayer");
            this.recordRenderTime(L.endSection(this.drawTraceName));
         } else {
            L.beginSection("Layer#computeBounds");
            this.getBounds(this.rect, this.matrix, false);
            this.intersectBoundsWithMatte(this.rect, var2);
            this.matrix.preConcat(this.transform.getMatrix());
            this.intersectBoundsWithMask(this.rect, this.matrix);
            L.endSection("Layer#computeBounds");
            if (!this.rect.isEmpty()) {
               L.beginSection("Layer#saveLayer");
               this.saveLayerCompat(var1, this.rect, this.contentPaint, true);
               L.endSection("Layer#saveLayer");
               this.clearCanvas(var1);
               L.beginSection("Layer#drawLayer");
               this.drawLayer(var1, this.matrix, var3);
               L.endSection("Layer#drawLayer");
               if (this.hasMasksOnThisLayer()) {
                  this.applyMasks(var1, this.matrix);
               }

               if (this.hasMatteOnThisLayer()) {
                  L.beginSection("Layer#drawMatte");
                  L.beginSection("Layer#saveLayer");
                  this.saveLayerCompat(var1, this.rect, this.mattePaint, false);
                  L.endSection("Layer#saveLayer");
                  this.clearCanvas(var1);
                  this.matteLayer.draw(var1, var2, var3);
                  L.beginSection("Layer#restoreLayer");
                  var1.restore();
                  L.endSection("Layer#restoreLayer");
                  L.endSection("Layer#drawMatte");
               }

               L.beginSection("Layer#restoreLayer");
               var1.restore();
               L.endSection("Layer#restoreLayer");
            }

            this.recordRenderTime(L.endSection(this.drawTraceName));
         }
      } else {
         L.endSection(this.drawTraceName);
      }
   }

   abstract void drawLayer(Canvas var1, Matrix var2, int var3);

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      this.rect.set(0.0F, 0.0F, 0.0F, 0.0F);
      this.buildParentLayerListIfNeeded();
      this.boundsMatrix.set(var2);
      if (var3) {
         List var5 = this.parentLayers;
         if (var5 != null) {
            for(int var4 = var5.size() - 1; var4 >= 0; --var4) {
               this.boundsMatrix.preConcat(((BaseLayer)this.parentLayers.get(var4)).transform.getMatrix());
            }
         } else {
            BaseLayer var6 = this.parentLayer;
            if (var6 != null) {
               this.boundsMatrix.preConcat(var6.transform.getMatrix());
            }
         }
      }

      this.boundsMatrix.preConcat(this.transform.getMatrix());
   }

   Layer getLayerModel() {
      return this.layerModel;
   }

   public String getName() {
      return this.layerModel.getName();
   }

   boolean hasMasksOnThisLayer() {
      MaskKeyframeAnimation var1 = this.mask;
      boolean var2;
      if (var1 != null && !var1.getMaskAnimations().isEmpty()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean hasMatteOnThisLayer() {
      boolean var1;
      if (this.matteLayer != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void onValueChanged() {
      this.invalidateSelf();
   }

   public void removeAnimation(BaseKeyframeAnimation var1) {
      this.animations.remove(var1);
   }

   void resolveChildKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      if (var1.matches(this.getName(), var2)) {
         KeyPath var5 = var4;
         if (!"__container".equals(this.getName())) {
            var4 = var4.addKey(this.getName());
            var5 = var4;
            if (var1.fullyResolvesTo(this.getName(), var2)) {
               var3.add(var4.resolve(this));
               var5 = var4;
            }
         }

         if (var1.propagateToChildren(this.getName(), var2)) {
            this.resolveChildKeyPath(var1, var2 + var1.incrementDepthBy(this.getName(), var2), var3, var5);
         }

      }
   }

   public void setContents(List var1, List var2) {
   }

   void setMatteLayer(BaseLayer var1) {
      this.matteLayer = var1;
   }

   void setParentLayer(BaseLayer var1) {
      this.parentLayer = var1;
   }

   void setProgress(float var1) {
      this.transform.setProgress(var1);
      MaskKeyframeAnimation var2 = this.mask;
      byte var3 = 0;
      int var4;
      if (var2 != null) {
         for(var4 = 0; var4 < this.mask.getMaskAnimations().size(); ++var4) {
            ((BaseKeyframeAnimation)this.mask.getMaskAnimations().get(var4)).setProgress(var1);
         }
      }

      float var5 = var1;
      if (this.layerModel.getTimeStretch() != 0.0F) {
         var5 = var1 / this.layerModel.getTimeStretch();
      }

      BaseLayer var6 = this.matteLayer;
      var4 = var3;
      if (var6 != null) {
         var1 = var6.layerModel.getTimeStretch();
         this.matteLayer.setProgress(var1 * var5);
         var4 = var3;
      }

      while(var4 < this.animations.size()) {
         ((BaseKeyframeAnimation)this.animations.get(var4)).setProgress(var5);
         ++var4;
      }

   }
}
