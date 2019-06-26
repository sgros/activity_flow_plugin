package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.TrimPathContent;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class ShapeTrimPath implements ContentModel {
   private final AnimatableFloatValue end;
   private final boolean hidden;
   private final String name;
   private final AnimatableFloatValue offset;
   private final AnimatableFloatValue start;
   private final ShapeTrimPath.Type type;

   public ShapeTrimPath(String var1, ShapeTrimPath.Type var2, AnimatableFloatValue var3, AnimatableFloatValue var4, AnimatableFloatValue var5, boolean var6) {
      this.name = var1;
      this.type = var2;
      this.start = var3;
      this.end = var4;
      this.offset = var5;
      this.hidden = var6;
   }

   public AnimatableFloatValue getEnd() {
      return this.end;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableFloatValue getOffset() {
      return this.offset;
   }

   public AnimatableFloatValue getStart() {
      return this.start;
   }

   public ShapeTrimPath.Type getType() {
      return this.type;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new TrimPathContent(var2, this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Trim Path: {start: ");
      var1.append(this.start);
      var1.append(", end: ");
      var1.append(this.end);
      var1.append(", offset: ");
      var1.append(this.offset);
      var1.append("}");
      return var1.toString();
   }

   public static enum Type {
      INDIVIDUALLY,
      SIMULTANEOUSLY;

      public static ShapeTrimPath.Type forId(int var0) {
         if (var0 != 1) {
            if (var0 == 2) {
               return INDIVIDUALLY;
            } else {
               StringBuilder var1 = new StringBuilder();
               var1.append("Unknown trim path type ");
               var1.append(var0);
               throw new IllegalArgumentException(var1.toString());
            }
         } else {
            return SIMULTANEOUSLY;
         }
      }
   }
}
