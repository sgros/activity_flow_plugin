package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class AnchorSpan extends MetricAffectingSpan {
   private String name;

   public AnchorSpan(String var1) {
      this.name = var1.toLowerCase();
   }

   public String getName() {
      return this.name;
   }

   public void updateDrawState(TextPaint var1) {
   }

   public void updateMeasureState(TextPaint var1) {
   }
}
