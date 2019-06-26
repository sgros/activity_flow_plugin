package com.github.mikephil.charting.buffer;

public abstract class AbstractBuffer {
   public final float[] buffer;
   protected int index = 0;
   protected int mFrom = 0;
   protected int mTo = 0;
   protected float phaseX = 1.0F;
   protected float phaseY = 1.0F;

   public AbstractBuffer(int var1) {
      this.index = 0;
      this.buffer = new float[var1];
   }

   public abstract void feed(Object var1);

   public void limitFrom(int var1) {
      int var2 = var1;
      if (var1 < 0) {
         var2 = 0;
      }

      this.mFrom = var2;
   }

   public void limitTo(int var1) {
      int var2 = var1;
      if (var1 < 0) {
         var2 = 0;
      }

      this.mTo = var2;
   }

   public void reset() {
      this.index = 0;
   }

   public void setPhases(float var1, float var2) {
      this.phaseX = var1;
      this.phaseY = var2;
   }

   public int size() {
      return this.buffer.length;
   }
}
