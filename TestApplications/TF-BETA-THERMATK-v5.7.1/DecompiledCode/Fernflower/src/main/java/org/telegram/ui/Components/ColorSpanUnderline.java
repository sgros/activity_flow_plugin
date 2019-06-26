package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

public class ColorSpanUnderline extends ForegroundColorSpan {
   public ColorSpanUnderline(int var1) {
      super(var1);
   }

   public void updateDrawState(TextPaint var1) {
      super.updateDrawState(var1);
      var1.setUnderlineText(true);
   }
}
