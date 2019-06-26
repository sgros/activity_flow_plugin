package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintMarkSpan extends MetricAffectingSpan {
   private TextPaint textPaint;

   public TextPaintMarkSpan(TextPaint var1) {
      this.textPaint = var1;
   }

   public TextPaint getTextPaint() {
      return this.textPaint;
   }

   public void updateDrawState(TextPaint var1) {
      TextPaint var2 = this.textPaint;
      if (var2 != null) {
         var1.setColor(var2.getColor());
         var1.setTypeface(this.textPaint.getTypeface());
         var1.setFlags(this.textPaint.getFlags());
         var1.setTextSize(this.textPaint.getTextSize());
         var2 = this.textPaint;
         var1.baselineShift = var2.baselineShift;
         var1.bgColor = var2.bgColor;
      }

   }

   public void updateMeasureState(TextPaint var1) {
      TextPaint var2 = this.textPaint;
      if (var2 != null) {
         var1.setColor(var2.getColor());
         var1.setTypeface(this.textPaint.getTypeface());
         var1.setFlags(this.textPaint.getFlags());
         var1.setTextSize(this.textPaint.getTextSize());
         var2 = this.textPaint;
         var1.baselineShift = var2.baselineShift;
         var1.bgColor = var2.bgColor;
      }

   }
}
