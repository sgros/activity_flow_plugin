package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.PolystarContent;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class PolystarShape implements ContentModel {
   private final boolean hidden;
   private final AnimatableFloatValue innerRadius;
   private final AnimatableFloatValue innerRoundedness;
   private final String name;
   private final AnimatableFloatValue outerRadius;
   private final AnimatableFloatValue outerRoundedness;
   private final AnimatableFloatValue points;
   private final AnimatableValue position;
   private final AnimatableFloatValue rotation;
   private final PolystarShape.Type type;

   public PolystarShape(String var1, PolystarShape.Type var2, AnimatableFloatValue var3, AnimatableValue var4, AnimatableFloatValue var5, AnimatableFloatValue var6, AnimatableFloatValue var7, AnimatableFloatValue var8, AnimatableFloatValue var9, boolean var10) {
      this.name = var1;
      this.type = var2;
      this.points = var3;
      this.position = var4;
      this.rotation = var5;
      this.innerRadius = var6;
      this.outerRadius = var7;
      this.innerRoundedness = var8;
      this.outerRoundedness = var9;
      this.hidden = var10;
   }

   public AnimatableFloatValue getInnerRadius() {
      return this.innerRadius;
   }

   public AnimatableFloatValue getInnerRoundedness() {
      return this.innerRoundedness;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableFloatValue getOuterRadius() {
      return this.outerRadius;
   }

   public AnimatableFloatValue getOuterRoundedness() {
      return this.outerRoundedness;
   }

   public AnimatableFloatValue getPoints() {
      return this.points;
   }

   public AnimatableValue getPosition() {
      return this.position;
   }

   public AnimatableFloatValue getRotation() {
      return this.rotation;
   }

   public PolystarShape.Type getType() {
      return this.type;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new PolystarContent(var1, var2, this);
   }

   public static enum Type {
      POLYGON(2),
      STAR(1);

      private final int value;

      private Type(int var3) {
         this.value = var3;
      }

      public static PolystarShape.Type forValue(int var0) {
         PolystarShape.Type[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PolystarShape.Type var4 = var1[var3];
            if (var4.value == var0) {
               return var4;
            }
         }

         return null;
      }
   }
}
