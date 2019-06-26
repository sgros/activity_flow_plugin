package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class ExtendedBitmapDrawable extends BitmapDrawable {
   private boolean canInvert;
   private int orientation;

   public ExtendedBitmapDrawable(Bitmap var1, boolean var2, int var3) {
      super(var1);
      this.canInvert = var2;
      this.orientation = var3;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public boolean isCanInvert() {
      return this.canInvert;
   }
}
