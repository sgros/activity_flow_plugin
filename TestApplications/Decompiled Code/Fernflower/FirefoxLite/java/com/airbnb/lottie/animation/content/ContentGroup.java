package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public class ContentGroup implements DrawingContent, PathContent, BaseKeyframeAnimation.AnimationListener, KeyPathElement {
   private final List contents;
   private final LottieDrawable lottieDrawable;
   private final Matrix matrix;
   private final String name;
   private final Path path;
   private List pathContents;
   private final RectF rect;
   private TransformKeyframeAnimation transformAnimation;

   public ContentGroup(LottieDrawable var1, BaseLayer var2, ShapeGroup var3) {
      this(var1, var2, var3.getName(), contentsFromModels(var1, var2, var3.getItems()), findTransform(var3.getItems()));
   }

   ContentGroup(LottieDrawable var1, BaseLayer var2, String var3, List var4, AnimatableTransform var5) {
      this.matrix = new Matrix();
      this.path = new Path();
      this.rect = new RectF();
      this.name = var3;
      this.lottieDrawable = var1;
      this.contents = var4;
      if (var5 != null) {
         this.transformAnimation = var5.createAnimation();
         this.transformAnimation.addAnimationsToLayer(var2);
         this.transformAnimation.addListener(this);
      }

      ArrayList var8 = new ArrayList();

      int var6;
      for(var6 = var4.size() - 1; var6 >= 0; --var6) {
         Content var7 = (Content)var4.get(var6);
         if (var7 instanceof GreedyContent) {
            var8.add((GreedyContent)var7);
         }
      }

      for(var6 = var8.size() - 1; var6 >= 0; --var6) {
         ((GreedyContent)var8.get(var6)).absorbContent(var4.listIterator(var4.size()));
      }

   }

   private static List contentsFromModels(LottieDrawable var0, BaseLayer var1, List var2) {
      ArrayList var3 = new ArrayList(var2.size());

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         Content var5 = ((ContentModel)var2.get(var4)).toContent(var0, var1);
         if (var5 != null) {
            var3.add(var5);
         }
      }

      return var3;
   }

   static AnimatableTransform findTransform(List var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         ContentModel var2 = (ContentModel)var0.get(var1);
         if (var2 instanceof AnimatableTransform) {
            return (AnimatableTransform)var2;
         }
      }

      return null;
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (this.transformAnimation != null) {
         this.transformAnimation.applyValueCallback(var1, var2);
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      this.matrix.set(var2);
      int var4 = var3;
      if (this.transformAnimation != null) {
         this.matrix.preConcat(this.transformAnimation.getMatrix());
         var4 = (int)((float)(Integer)this.transformAnimation.getOpacity().getValue() / 100.0F * (float)var3 / 255.0F * 255.0F);
      }

      for(var3 = this.contents.size() - 1; var3 >= 0; --var3) {
         Object var5 = this.contents.get(var3);
         if (var5 instanceof DrawingContent) {
            ((DrawingContent)var5).draw(var1, this.matrix, var4);
         }
      }

   }

   public void getBounds(RectF var1, Matrix var2) {
      this.matrix.set(var2);
      if (this.transformAnimation != null) {
         this.matrix.preConcat(this.transformAnimation.getMatrix());
      }

      this.rect.set(0.0F, 0.0F, 0.0F, 0.0F);

      for(int var3 = this.contents.size() - 1; var3 >= 0; --var3) {
         Content var4 = (Content)this.contents.get(var3);
         if (var4 instanceof DrawingContent) {
            ((DrawingContent)var4).getBounds(this.rect, this.matrix);
            if (var1.isEmpty()) {
               var1.set(this.rect);
            } else {
               var1.set(Math.min(var1.left, this.rect.left), Math.min(var1.top, this.rect.top), Math.max(var1.right, this.rect.right), Math.max(var1.bottom, this.rect.bottom));
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      this.matrix.reset();
      if (this.transformAnimation != null) {
         this.matrix.set(this.transformAnimation.getMatrix());
      }

      this.path.reset();

      for(int var1 = this.contents.size() - 1; var1 >= 0; --var1) {
         Content var2 = (Content)this.contents.get(var1);
         if (var2 instanceof PathContent) {
            this.path.addPath(((PathContent)var2).getPath(), this.matrix);
         }
      }

      return this.path;
   }

   List getPathList() {
      if (this.pathContents == null) {
         this.pathContents = new ArrayList();

         for(int var1 = 0; var1 < this.contents.size(); ++var1) {
            Content var2 = (Content)this.contents.get(var1);
            if (var2 instanceof PathContent) {
               this.pathContents.add((PathContent)var2);
            }
         }
      }

      return this.pathContents;
   }

   Matrix getTransformationMatrix() {
      if (this.transformAnimation != null) {
         return this.transformAnimation.getMatrix();
      } else {
         this.matrix.reset();
         return this.matrix;
      }
   }

   public void onValueChanged() {
      this.lottieDrawable.invalidateSelf();
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
            int var6 = var1.incrementDepthBy(this.getName(), var2);

            for(int var7 = 0; var7 < this.contents.size(); ++var7) {
               Content var8 = (Content)this.contents.get(var7);
               if (var8 instanceof KeyPathElement) {
                  ((KeyPathElement)var8).resolveKeyPath(var1, var2 + var6, var3, var5);
               }
            }
         }

      }
   }

   public void setContents(List var1, List var2) {
      ArrayList var5 = new ArrayList(var1.size() + this.contents.size());
      var5.addAll(var1);

      for(int var3 = this.contents.size() - 1; var3 >= 0; --var3) {
         Content var4 = (Content)this.contents.get(var3);
         var4.setContents(var5, this.contents.subList(0, var3));
         var5.add(var4);
      }

   }
}
