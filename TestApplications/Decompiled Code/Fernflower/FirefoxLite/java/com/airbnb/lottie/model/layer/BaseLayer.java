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
import com.airbnb.lottie.animation.content.DrawingContent;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.MaskKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseLayer implements DrawingContent, BaseKeyframeAnimation.AnimationListener, KeyPathElement {
   private final Paint addMaskPaint = new Paint(1);
   private final List animations = new ArrayList();
   final Matrix boundsMatrix = new Matrix();
   private final Paint clearPaint = new Paint();
   private final Paint contentPaint = new Paint(1);
   private final String drawTraceName;
   final Layer layerModel;
   final LottieDrawable lottieDrawable;
   private MaskKeyframeAnimation mask;
   private final RectF maskBoundsRect = new RectF();
   private final Matrix matrix = new Matrix();
   private final RectF matteBoundsRect = new RectF();
   private BaseLayer matteLayer;
   private final Paint mattePaint = new Paint(1);
   private BaseLayer parentLayer;
   private List parentLayers;
   private final Path path = new Path();
   private final RectF rect = new RectF();
   private final Paint subtractMaskPaint = new Paint(1);
   private final RectF tempMaskBoundsRect = new RectF();
   final TransformKeyframeAnimation transform;
   private boolean visible = true;

   BaseLayer(LottieDrawable var1, Layer var2) {
      this.lottieDrawable = var1;
      this.layerModel = var2;
      StringBuilder var3 = new StringBuilder();
      var3.append(var2.getName());
      var3.append("#draw");
      this.drawTraceName = var3.toString();
      this.clearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
      this.addMaskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
      this.subtractMaskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
      if (var2.getMatteType() == Layer.MatteType.Invert) {
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

         Iterator var6 = this.mask.getOpacityAnimations().iterator();

         while(var6.hasNext()) {
            BaseKeyframeAnimation var5 = (BaseKeyframeAnimation)var6.next();
            this.addAnimation(var5);
            var5.addUpdateListener(this);
         }
      }

      this.setupInOutAnimations();
   }

   private void applyMasks(Canvas var1, Matrix var2) {
      this.applyMasks(var1, var2, Mask.MaskMode.MaskModeAdd);
      this.applyMasks(var1, var2, Mask.MaskMode.MaskModeIntersect);
      this.applyMasks(var1, var2, Mask.MaskMode.MaskModeSubtract);
   }

   private void applyMasks(Canvas var1, Matrix var2, Mask.MaskMode var3) {
      int var4 = null.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[var3.ordinal()];
      boolean var5 = true;
      Paint var6;
      if (var4 != 1) {
         var6 = this.addMaskPaint;
      } else {
         var6 = this.subtractMaskPaint;
      }

      int var7 = this.mask.getMasks().size();
      byte var8 = 0;
      var4 = 0;

      boolean var9;
      while(true) {
         if (var4 >= var7) {
            var9 = false;
            break;
         }

         if (((Mask)this.mask.getMasks().get(var4)).getMaskMode() == var3) {
            var9 = var5;
            break;
         }

         ++var4;
      }

      if (var9) {
         L.beginSection("Layer#drawMask");
         L.beginSection("Layer#saveLayer");
         this.saveLayerCompat(var1, this.rect, var6, false);
         L.endSection("Layer#saveLayer");
         this.clearCanvas(var1);

         for(var4 = var8; var4 < var7; ++var4) {
            if (((Mask)this.mask.getMasks().get(var4)).getMaskMode() == var3) {
               Path var10 = (Path)((BaseKeyframeAnimation)this.mask.getMaskAnimations().get(var4)).getValue();
               this.path.set(var10);
               this.path.transform(var2);
               BaseKeyframeAnimation var11 = (BaseKeyframeAnimation)this.mask.getOpacityAnimations().get(var4);
               int var12 = this.contentPaint.getAlpha();
               this.contentPaint.setAlpha((int)((float)(Integer)var11.getValue() * 2.55F));
               var1.drawPath(this.path, this.contentPaint);
               this.contentPaint.setAlpha(var12);
            }
         }

         L.beginSection("Layer#restoreLayer");
         var1.restore();
         L.endSection("Layer#restoreLayer");
         L.endSection("Layer#drawMask");
      }
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
      var1.drawRect(this.rect.left - 1.0F, this.rect.top - 1.0F, this.rect.right + 1.0F, this.rect.bottom + 1.0F, this.clearPaint);
      L.endSection("Layer#clearLayer");
   }

   static BaseLayer forModel(Layer var0, LottieDrawable var1, LottieComposition var2) {
      switch(var0.getLayerType()) {
      case Shape:
         return new ShapeLayer(var1, var0);
      case PreComp:
         return new CompositionLayer(var1, var0, var2.getPrecomps(var0.getRefId()), var2);
      case Solid:
         return new SolidLayer(var1, var0);
      case Image:
         return new ImageLayer(var1, var0);
      case Null:
         return new NullLayer(var1, var0);
      case Text:
         return new TextLayer(var1, var0);
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Unknown layer type ");
         var3.append(var0.getLayerType());
         L.warn(var3.toString());
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
            switch(var5.getMaskMode()) {
            case MaskModeSubtract:
               return;
            case MaskModeIntersect:
               return;
            }

            this.path.computeBounds(this.tempMaskBoundsRect, false);
            if (var4 == 0) {
               this.maskBoundsRect.set(this.tempMaskBoundsRect);
            } else {
               this.maskBoundsRect.set(Math.min(this.maskBoundsRect.left, this.tempMaskBoundsRect.left), Math.min(this.maskBoundsRect.top, this.tempMaskBoundsRect.top), Math.max(this.maskBoundsRect.right, this.tempMaskBoundsRect.right), Math.max(this.maskBoundsRect.bottom, this.tempMaskBoundsRect.bottom));
            }
         }

         var1.set(Math.max(var1.left, this.maskBoundsRect.left), Math.max(var1.top, this.maskBoundsRect.top), Math.min(var1.right, this.maskBoundsRect.right), Math.min(var1.bottom, this.maskBoundsRect.bottom));
      }
   }

   private void intersectBoundsWithMatte(RectF var1, Matrix var2) {
      if (this.hasMatteOnThisLayer()) {
         if (this.layerModel.getMatteType() != Layer.MatteType.Invert) {
            this.matteLayer.getBounds(this.matteBoundsRect, var2);
            var1.set(Math.max(var1.left, this.matteBoundsRect.left), Math.max(var1.top, this.matteBoundsRect.top), Math.min(var1.right, this.matteBoundsRect.right), Math.min(var1.bottom, this.matteBoundsRect.bottom));
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
               if ((Float)var3.getValue() == 1.0F) {
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
      this.animations.add(var1);
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      this.transform.applyValueCallback(var1, var2);
   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      L.beginSection(this.drawTraceName);
      if (!this.visible) {
         L.endSection(this.drawTraceName);
      } else {
         this.buildParentLayerListIfNeeded();
         L.beginSection("Layer#parentMatrix");
         this.matrix.reset();
         this.matrix.set(var2);

         for(int var4 = this.parentLayers.size() - 1; var4 >= 0; --var4) {
            this.matrix.preConcat(((BaseLayer)this.parentLayers.get(var4)).transform.getMatrix());
         }

         L.endSection("Layer#parentMatrix");
         var3 = (int)((float)var3 / 255.0F * (float)(Integer)this.transform.getOpacity().getValue() / 100.0F * 255.0F);
         if (!this.hasMatteOnThisLayer() && !this.hasMasksOnThisLayer()) {
            this.matrix.preConcat(this.transform.getMatrix());
            L.beginSection("Layer#drawLayer");
            this.drawLayer(var1, this.matrix, var3);
            L.endSection("Layer#drawLayer");
            this.recordRenderTime(L.endSection(this.drawTraceName));
         } else {
            L.beginSection("Layer#computeBounds");
            this.rect.set(0.0F, 0.0F, 0.0F, 0.0F);
            this.getBounds(this.rect, this.matrix);
            this.intersectBoundsWithMatte(this.rect, this.matrix);
            this.matrix.preConcat(this.transform.getMatrix());
            this.intersectBoundsWithMask(this.rect, this.matrix);
            this.rect.set(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
            L.endSection("Layer#computeBounds");
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
            this.recordRenderTime(L.endSection(this.drawTraceName));
         }
      }
   }

   abstract void drawLayer(Canvas var1, Matrix var2, int var3);

   public void getBounds(RectF var1, Matrix var2) {
      this.boundsMatrix.set(var2);
      this.boundsMatrix.preConcat(this.transform.getMatrix());
   }

   Layer getLayerModel() {
      return this.layerModel;
   }

   public String getName() {
      return this.layerModel.getName();
   }

   boolean hasMasksOnThisLayer() {
      boolean var1;
      if (this.mask != null && !this.mask.getMaskAnimations().isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
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

      var4 = var3;
      if (this.matteLayer != null) {
         var1 = this.matteLayer.layerModel.getTimeStretch();
         this.matteLayer.setProgress(var1 * var5);
         var4 = var3;
      }

      while(var4 < this.animations.size()) {
         ((BaseKeyframeAnimation)this.animations.get(var4)).setProgress(var5);
         ++var4;
      }

   }
}
