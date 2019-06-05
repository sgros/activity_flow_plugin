package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.Collections;
import java.util.List;

public class ShapeLayer extends BaseLayer {
   private final ContentGroup contentGroup;

   ShapeLayer(LottieDrawable var1, Layer var2) {
      super(var1, var2);
      this.contentGroup = new ContentGroup(var1, this, new ShapeGroup("__container", var2.getShapes()));
      this.contentGroup.setContents(Collections.emptyList(), Collections.emptyList());
   }

   void drawLayer(Canvas var1, Matrix var2, int var3) {
      this.contentGroup.draw(var1, var2, var3);
   }

   public void getBounds(RectF var1, Matrix var2) {
      super.getBounds(var1, var2);
      this.contentGroup.getBounds(var1, this.boundsMatrix);
   }

   protected void resolveChildKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      this.contentGroup.resolveKeyPath(var1, var2, var3, var4);
   }
}
