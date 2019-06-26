package com.airbnb.lottie.animation;

import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.os.LocaleList;

public class LPaint extends Paint {
   public LPaint() {
   }

   public LPaint(int var1) {
      super(var1);
   }

   public LPaint(int var1, Mode var2) {
      super(var1);
      this.setXfermode(new PorterDuffXfermode(var2));
   }

   public LPaint(Mode var1) {
      this.setXfermode(new PorterDuffXfermode(var1));
   }

   public void setTextLocales(LocaleList var1) {
   }
}
