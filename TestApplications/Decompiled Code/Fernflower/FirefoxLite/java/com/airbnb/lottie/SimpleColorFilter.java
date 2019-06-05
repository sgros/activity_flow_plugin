package com.airbnb.lottie;

import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;

public class SimpleColorFilter extends PorterDuffColorFilter {
   public SimpleColorFilter(int var1) {
      super(var1, Mode.SRC_ATOP);
   }
}
