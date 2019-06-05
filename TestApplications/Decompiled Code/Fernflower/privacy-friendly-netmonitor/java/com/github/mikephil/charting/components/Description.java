package com.github.mikephil.charting.components;

import android.graphics.Paint.Align;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class Description extends ComponentBase {
   private MPPointF mPosition;
   private Align mTextAlign;
   private String text = "Description Label";

   public Description() {
      this.mTextAlign = Align.RIGHT;
      this.mTextSize = Utils.convertDpToPixel(8.0F);
   }

   public MPPointF getPosition() {
      return this.mPosition;
   }

   public String getText() {
      return this.text;
   }

   public Align getTextAlign() {
      return this.mTextAlign;
   }

   public void setPosition(float var1, float var2) {
      if (this.mPosition == null) {
         this.mPosition = MPPointF.getInstance(var1, var2);
      } else {
         this.mPosition.x = var1;
         this.mPosition.y = var2;
      }

   }

   public void setText(String var1) {
      this.text = var1;
   }

   public void setTextAlign(Align var1) {
      this.mTextAlign = var1;
   }
}
