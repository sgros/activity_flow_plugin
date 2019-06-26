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
   private final boolean hidden;
   private final LottieDrawable lottieDrawable;
   private final Matrix matrix;
   private final String name;
   private final Path path;
   private List pathContents;
   private final RectF rect;
   private TransformKeyframeAnimation transformAnimation;

   public ContentGroup(LottieDrawable var1, BaseLayer var2, ShapeGroup var3) {
      this(var1, var2, var3.getName(), var3.isHidden(), contentsFromModels(var1, var2, var3.getItems()), findTransform(var3.getItems()));
   }

   ContentGroup(LottieDrawable var1, BaseLayer var2, String var3, boolean var4, List var5, AnimatableTransform var6) {
      this.matrix = new Matrix();
      this.path = new Path();
      this.rect = new RectF();
      this.name = var3;
      this.lottieDrawable = var1;
      this.hidden = var4;
      this.contents = var5;
      if (var6 != null) {
         this.transformAnimation = var6.createAnimation();
         this.transformAnimation.addAnimationsToLayer(var2);
         this.transformAnimation.addListener(this);
      }

      ArrayList var9 = new ArrayList();

      int var7;
      for(var7 = var5.size() - 1; var7 >= 0; --var7) {
         Content var8 = (Content)var5.get(var7);
         if (var8 instanceof GreedyContent) {
            var9.add((GreedyContent)var8);
         }
      }

      for(var7 = var9.size() - 1; var7 >= 0; --var7) {
         ((GreedyContent)var9.get(var7)).absorbContent(var5.listIterator(var5.size()));
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
      TransformKeyframeAnimation var3 = this.transformAnimation;
      if (var3 != null) {
         var3.applyValueCallback(var1, var2);
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      if (!this.hidden) {
         this.matrix.set(var2);
         TransformKeyframeAnimation var5 = this.transformAnimation;
         int var4 = var3;
         if (var5 != null) {
            this.matrix.preConcat(var5.getMatrix());
            if (this.transformAnimation.getOpacity() == null) {
               var4 = 100;
            } else {
               var4 = (Integer)this.transformAnimation.getOpacity().getValue();
            }

            var4 = (int)((float)var4 / 100.0F * (float)var3 / 255.0F * 255.0F);
         }

         for(var3 = this.contents.size() - 1; var3 >= 0; --var3) {
            Object var6 = this.contents.get(var3);
            if (var6 instanceof DrawingContent) {
               ((DrawingContent)var6).draw(var1, this.matrix, var4);
            }
         }

      }
   }

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      this.matrix.set(var2);
      TransformKeyframeAnimation var5 = this.transformAnimation;
      if (var5 != null) {
         this.matrix.preConcat(var5.getMatrix());
      }

      this.rect.set(0.0F, 0.0F, 0.0F, 0.0F);

      for(int var4 = this.contents.size() - 1; var4 >= 0; --var4) {
         Content var6 = (Content)this.contents.get(var4);
         if (var6 instanceof DrawingContent) {
            ((DrawingContent)var6).getBounds(this.rect, this.matrix, var3);
            var1.union(this.rect);
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      this.matrix.reset();
      TransformKeyframeAnimation var1 = this.transformAnimation;
      if (var1 != null) {
         this.matrix.set(var1.getMatrix());
      }

      this.path.reset();
      if (this.hidden) {
         return this.path;
      } else {
         for(int var2 = this.contents.size() - 1; var2 >= 0; --var2) {
            Content var3 = (Content)this.contents.get(var2);
            if (var3 instanceof PathContent) {
               this.path.addPath(((PathContent)var3).getPath(), this.matrix);
            }
         }

         return this.path;
      }
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
      TransformKeyframeAnimation var1 = this.transformAnimation;
      if (var1 != null) {
         return var1.getMatrix();
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
