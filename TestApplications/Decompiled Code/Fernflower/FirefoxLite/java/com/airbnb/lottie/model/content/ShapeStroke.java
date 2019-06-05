package com.airbnb.lottie.model.content;

import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.StrokeContent;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.List;

public class ShapeStroke implements ContentModel {
   private final ShapeStroke.LineCapType capType;
   private final AnimatableColorValue color;
   private final ShapeStroke.LineJoinType joinType;
   private final List lineDashPattern;
   private final float miterLimit;
   private final String name;
   private final AnimatableFloatValue offset;
   private final AnimatableIntegerValue opacity;
   private final AnimatableFloatValue width;

   public ShapeStroke(String var1, AnimatableFloatValue var2, List var3, AnimatableColorValue var4, AnimatableIntegerValue var5, AnimatableFloatValue var6, ShapeStroke.LineCapType var7, ShapeStroke.LineJoinType var8, float var9) {
      this.name = var1;
      this.offset = var2;
      this.lineDashPattern = var3;
      this.color = var4;
      this.opacity = var5;
      this.width = var6;
      this.capType = var7;
      this.joinType = var8;
      this.miterLimit = var9;
   }

   public ShapeStroke.LineCapType getCapType() {
      return this.capType;
   }

   public AnimatableColorValue getColor() {
      return this.color;
   }

   public AnimatableFloatValue getDashOffset() {
      return this.offset;
   }

   public ShapeStroke.LineJoinType getJoinType() {
      return this.joinType;
   }

   public List getLineDashPattern() {
      return this.lineDashPattern;
   }

   public float getMiterLimit() {
      return this.miterLimit;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableIntegerValue getOpacity() {
      return this.opacity;
   }

   public AnimatableFloatValue getWidth() {
      return this.width;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new StrokeContent(var1, var2, this);
   }

   public static enum LineCapType {
      Butt,
      Round,
      Unknown;

      public Cap toPaintCap() {
         switch(this) {
         case Butt:
            return Cap.BUTT;
         case Round:
            return Cap.ROUND;
         default:
            return Cap.SQUARE;
         }
      }
   }

   public static enum LineJoinType {
      Bevel,
      Miter,
      Round;

      public Join toPaintJoin() {
         switch(this) {
         case Bevel:
            return Join.BEVEL;
         case Miter:
            return Join.MITER;
         case Round:
            return Join.ROUND;
         default:
            return null;
         }
      }
   }
}
