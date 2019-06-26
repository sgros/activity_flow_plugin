package org.telegram.ui.Components.Paint;

import android.graphics.PointF;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RenderState {
   private static final int DEFAULT_STATE_SIZE = 256;
   private int allocatedCount;
   public float alpha;
   public float angle;
   public float baseWeight;
   private ByteBuffer buffer;
   private int count;
   public double remainder;
   public float scale;
   public float spacing;

   public boolean addPoint(PointF var1, float var2, float var3, float var4, int var5) {
      if ((var5 == -1 || var5 < this.allocatedCount) && this.buffer.position() != this.buffer.limit()) {
         if (var5 != -1) {
            this.buffer.position(var5 * 5 * 4);
         }

         this.buffer.putFloat(var1.x);
         this.buffer.putFloat(var1.y);
         this.buffer.putFloat(var2);
         this.buffer.putFloat(var3);
         this.buffer.putFloat(var4);
         return true;
      } else {
         this.resizeBuffer();
         return false;
      }
   }

   public void appendValuesCount(int var1) {
      var1 += this.count;
      if (var1 > this.allocatedCount || this.buffer == null) {
         this.resizeBuffer();
      }

      this.count = var1;
   }

   public int getCount() {
      return this.count;
   }

   public void prepare() {
      this.count = 0;
      if (this.buffer == null) {
         this.allocatedCount = 256;
         this.buffer = ByteBuffer.allocateDirect(this.allocatedCount * 5 * 4);
         this.buffer.order(ByteOrder.nativeOrder());
         this.buffer.position(0);
      }
   }

   public float read() {
      return this.buffer.getFloat();
   }

   public void reset() {
      this.count = 0;
      this.remainder = 0.0D;
      ByteBuffer var1 = this.buffer;
      if (var1 != null) {
         var1.position(0);
      }

   }

   public void resizeBuffer() {
      if (this.buffer != null) {
         this.buffer = null;
      }

      this.allocatedCount = Math.max(this.allocatedCount * 2, 256);
      this.buffer = ByteBuffer.allocateDirect(this.allocatedCount * 5 * 4);
      this.buffer.order(ByteOrder.nativeOrder());
      this.buffer.position(0);
   }

   public void setPosition(int var1) {
      ByteBuffer var2 = this.buffer;
      if (var2 != null && var1 >= 0 && var1 < this.allocatedCount) {
         var2.position(var1 * 5 * 4);
      }

   }
}
