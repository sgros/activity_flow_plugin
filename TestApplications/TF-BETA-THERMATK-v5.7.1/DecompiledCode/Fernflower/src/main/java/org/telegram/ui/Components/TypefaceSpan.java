package org.telegram.ui.Components;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import org.telegram.messenger.AndroidUtilities;

public class TypefaceSpan extends MetricAffectingSpan {
   private int color;
   private int textSize;
   private Typeface typeface;

   public TypefaceSpan(Typeface var1) {
      this.typeface = var1;
   }

   public TypefaceSpan(Typeface var1, int var2) {
      this.typeface = var1;
      this.textSize = var2;
   }

   public TypefaceSpan(Typeface var1, int var2, int var3) {
      this.typeface = var1;
      if (var2 > 0) {
         this.textSize = var2;
      }

      this.color = var3;
   }

   public Typeface getTypeface() {
      return this.typeface;
   }

   public boolean isBold() {
      boolean var1;
      if (this.typeface == AndroidUtilities.getTypeface("fonts/rmedium.ttf")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isItalic() {
      boolean var1;
      if (this.typeface == AndroidUtilities.getTypeface("fonts/ritalic.ttf")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isMono() {
      boolean var1;
      if (this.typeface == Typeface.MONOSPACE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setColor(int var1) {
      this.color = var1;
   }

   public void updateDrawState(TextPaint var1) {
      Typeface var2 = this.typeface;
      if (var2 != null) {
         var1.setTypeface(var2);
      }

      int var3 = this.textSize;
      if (var3 != 0) {
         var1.setTextSize((float)var3);
      }

      var3 = this.color;
      if (var3 != 0) {
         var1.setColor(var3);
      }

      var1.setFlags(var1.getFlags() | 128);
   }

   public void updateMeasureState(TextPaint var1) {
      Typeface var2 = this.typeface;
      if (var2 != null) {
         var1.setTypeface(var2);
      }

      int var3 = this.textSize;
      if (var3 != 0) {
         var1.setTextSize((float)var3);
      }

      var1.setFlags(var1.getFlags() | 128);
   }
}
