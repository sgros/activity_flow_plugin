package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.Arrays;
import java.util.List;

public class ShapeGroup implements ContentModel {
   private final boolean hidden;
   private final List items;
   private final String name;

   public ShapeGroup(String var1, List var2, boolean var3) {
      this.name = var1;
      this.items = var2;
      this.hidden = var3;
   }

   public List getItems() {
      return this.items;
   }

   public String getName() {
      return this.name;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new ContentGroup(var1, var2, this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ShapeGroup{name='");
      var1.append(this.name);
      var1.append("' Shapes: ");
      var1.append(Arrays.toString(this.items.toArray()));
      var1.append('}');
      return var1.toString();
   }
}
