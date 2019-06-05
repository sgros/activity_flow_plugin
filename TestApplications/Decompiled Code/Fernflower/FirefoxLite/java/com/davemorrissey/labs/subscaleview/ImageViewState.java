package com.davemorrissey.labs.subscaleview;

import android.graphics.PointF;
import java.io.Serializable;

public class ImageViewState implements Serializable {
   public static final ImageViewState ALIGN_TOP = new ImageViewState(0.0F, new PointF(0.0F, 0.0F), 0);
   private float centerX;
   private float centerY;
   private int orientation;
   private float scale;

   public ImageViewState(float var1, PointF var2, int var3) {
      this.scale = var1;
      this.centerX = var2.x;
      this.centerY = var2.y;
      this.orientation = var3;
   }

   public PointF getCenter() {
      return new PointF(this.centerX, this.centerY);
   }

   public int getOrientation() {
      return this.orientation;
   }

   public float getScale() {
      return this.scale;
   }
}
