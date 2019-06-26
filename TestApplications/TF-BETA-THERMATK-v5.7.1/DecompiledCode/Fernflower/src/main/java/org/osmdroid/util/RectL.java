package org.osmdroid.util;

import android.graphics.Rect;

public class RectL {
   public long bottom;
   public long left;
   public long right;
   public long top;

   public RectL() {
   }

   public RectL(long var1, long var3, long var5, long var7) {
      this.set(var1, var3, var5, var7);
   }

   public static Rect getBounds(Rect var0, int var1, int var2, double var3, Rect var5) {
      if (var5 == null) {
         var5 = new Rect();
      }

      if (var3 == 0.0D) {
         var5.top = var0.top;
         var5.left = var0.left;
         var5.bottom = var0.bottom;
         var5.right = var0.right;
         return var5;
      } else {
         double var6 = 3.141592653589793D * var3 / 180.0D;
         var3 = Math.cos(var6);
         var6 = Math.sin(var6);
         int var8 = var0.left;
         int var9 = var0.top;
         long var10 = (long)var8;
         long var12 = (long)var9;
         long var14 = (long)var1;
         long var16 = (long)var2;
         var2 = (int)getRotatedX(var10, var12, var14, var16, var3, var6);
         var1 = (int)getRotatedY(var10, var12, var14, var16, var3, var6);
         var5.bottom = var1;
         var5.top = var1;
         var5.right = var2;
         var5.left = var2;
         var2 = var0.right;
         var1 = var0.top;
         var10 = (long)var2;
         var12 = (long)var1;
         var1 = (int)getRotatedX(var10, var12, var14, var16, var3, var6);
         var2 = (int)getRotatedY(var10, var12, var14, var16, var3, var6);
         if (var5.top > var2) {
            var5.top = var2;
         }

         if (var5.bottom < var2) {
            var5.bottom = var2;
         }

         if (var5.left > var1) {
            var5.left = var1;
         }

         if (var5.right < var1) {
            var5.right = var1;
         }

         var2 = var0.right;
         var1 = var0.bottom;
         var12 = (long)var2;
         var10 = (long)var1;
         var2 = (int)getRotatedX(var12, var10, var14, var16, var3, var6);
         var1 = (int)getRotatedY(var12, var10, var14, var16, var3, var6);
         if (var5.top > var1) {
            var5.top = var1;
         }

         if (var5.bottom < var1) {
            var5.bottom = var1;
         }

         if (var5.left > var2) {
            var5.left = var2;
         }

         if (var5.right < var2) {
            var5.right = var2;
         }

         var2 = var0.left;
         var1 = var0.bottom;
         var10 = (long)var2;
         var12 = (long)var1;
         var2 = (int)getRotatedX(var10, var12, var14, var16, var3, var6);
         var1 = (int)getRotatedY(var10, var12, var14, var16, var3, var6);
         if (var5.top > var1) {
            var5.top = var1;
         }

         if (var5.bottom < var1) {
            var5.bottom = var1;
         }

         if (var5.left > var2) {
            var5.left = var2;
         }

         if (var5.right < var2) {
            var5.right = var2;
         }

         return var5;
      }
   }

   public static long getRotatedX(long var0, long var2, long var4, long var6, double var8, double var10) {
      double var12 = (double)(var0 - var4);
      Double.isNaN(var12);
      double var14 = (double)(var2 - var6);
      Double.isNaN(var14);
      return var4 + Math.round(var12 * var8 - var14 * var10);
   }

   public static long getRotatedY(long var0, long var2, long var4, long var6, double var8, double var10) {
      double var12 = (double)(var0 - var4);
      Double.isNaN(var12);
      double var14 = (double)(var2 - var6);
      Double.isNaN(var14);
      return var6 + Math.round(var12 * var10 + var14 * var8);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && RectL.class == var1.getClass()) {
         RectL var3 = (RectL)var1;
         if (this.left != var3.left || this.top != var3.top || this.right != var3.right || this.bottom != var3.bottom) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)((((this.left * 31L + this.top) * 31L + this.right) * 31L + this.bottom) % 2147483647L);
   }

   public void set(long var1, long var3, long var5, long var7) {
      this.left = var1;
      this.top = var3;
      this.right = var5;
      this.bottom = var7;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RectL(");
      var1.append(this.left);
      var1.append(", ");
      var1.append(this.top);
      var1.append(" - ");
      var1.append(this.right);
      var1.append(", ");
      var1.append(this.bottom);
      var1.append(")");
      return var1.toString();
   }
}
