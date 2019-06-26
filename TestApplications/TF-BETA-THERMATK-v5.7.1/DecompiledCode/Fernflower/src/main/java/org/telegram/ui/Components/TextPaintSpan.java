package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintSpan extends MetricAffectingSpan {
   private TextPaint textPaint;

   public TextPaintSpan(TextPaint var1) {
      this.textPaint = var1;
   }

   public void updateDrawState(TextPaint var1) {
      var1.setColor(this.textPaint.getColor());
      var1.setTypeface(this.textPaint.getTypeface());
      var1.setFlags(this.textPaint.getFlags());
      var1.setTextSize(this.textPaint.getTextSize());
      TextPaint var2 = this.textPaint;
      var1.baselineShift = var2.baselineShift;
      var1.bgColor = var2.bgColor;
   }

   public void updateMeasureState(TextPaint var1) {
      var1.setColor(this.textPaint.getColor());
      var1.setTypeface(this.textPaint.getTypeface());
      var1.setFlags(this.textPaint.getFlags());
      var1.setTextSize(this.textPaint.getTextSize());
      TextPaint var2 = this.textPaint;
      var1.baselineShift = var2.baselineShift;
      var1.bgColor = var2.bgColor;
   }
}
