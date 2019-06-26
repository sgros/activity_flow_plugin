package org.telegram.ui.Components.Paint;

import java.util.Arrays;
import java.util.Vector;

public class Path {
   private float baseWeight;
   private Brush brush;
   private int color;
   private Vector points = new Vector();
   public double remainder;

   public Path(Point var1) {
      this.points.add(var1);
   }

   public Path(Point[] var1) {
      this.points.addAll(Arrays.asList(var1));
   }

   public float getBaseWeight() {
      return this.baseWeight;
   }

   public Brush getBrush() {
      return this.brush;
   }

   public int getColor() {
      return this.color;
   }

   public int getLength() {
      Vector var1 = this.points;
      return var1 == null ? 0 : var1.size();
   }

   public Point[] getPoints() {
      Point[] var1 = new Point[this.points.size()];
      this.points.toArray(var1);
      return var1;
   }

   public void setup(int var1, float var2, Brush var3) {
      this.color = var1;
      this.baseWeight = var2;
      this.brush = var3;
   }
}
