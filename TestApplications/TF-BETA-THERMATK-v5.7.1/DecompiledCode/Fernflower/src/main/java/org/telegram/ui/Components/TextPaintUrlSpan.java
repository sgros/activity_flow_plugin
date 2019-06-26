package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintUrlSpan extends MetricAffectingSpan {
   private int color;
   private String currentUrl;
   private TextPaint textPaint;
   private int textSize;

   public TextPaintUrlSpan(TextPaint var1, String var2) {
      this.textPaint = var1;
      this.currentUrl = var2;
   }

   public TextPaint getTextPaint() {
      return this.textPaint;
   }

   public String getUrl() {
      return this.currentUrl;
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
