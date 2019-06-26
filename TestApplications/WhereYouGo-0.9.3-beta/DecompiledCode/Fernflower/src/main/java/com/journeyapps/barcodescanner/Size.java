package com.journeyapps.barcodescanner;

import android.support.annotation.NonNull;

public class Size implements Comparable {
   public final int height;
   public final int width;

   public Size(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public int compareTo(@NonNull Size var1) {
      int var2 = this.height * this.width;
      int var3 = var1.height * var1.width;
      byte var4;
      if (var3 < var2) {
         var4 = 1;
      } else if (var3 > var2) {
         var4 = -1;
      } else {
         var4 = 0;
      }

      return var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (var1 != null && this.getClass() == var1.getClass()) {
            Size var3 = (Size)var1;
            if (this.width != var3.width || this.height != var3.height) {
               var2 = false;
            }
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public boolean fitsIn(Size var1) {
      boolean var2;
      if (this.width <= var1.width && this.height <= var1.height) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.width * 31 + this.height;
   }

   public Size rotate() {
      return new Size(this.height, this.width);
   }

   public Size scale(int var1, int var2) {
      return new Size(this.width * var1 / var2, this.height * var1 / var2);
   }

   public Size scaleCrop(Size var1) {
      if (this.width * var1.height <= var1.width * this.height) {
         var1 = new Size(var1.width, this.height * var1.width / this.width);
      } else {
         var1 = new Size(this.width * var1.height / this.height, var1.height);
      }

      return var1;
   }

   public Size scaleFit(Size var1) {
      if (this.width * var1.height >= var1.width * this.height) {
         var1 = new Size(var1.width, this.height * var1.width / this.width);
      } else {
         var1 = new Size(this.width * var1.height / this.height, var1.height);
      }

      return var1;
   }

   public String toString() {
      return this.width + "x" + this.height;
   }
}
