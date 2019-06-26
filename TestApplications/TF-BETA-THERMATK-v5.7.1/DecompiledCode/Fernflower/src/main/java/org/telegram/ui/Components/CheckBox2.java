package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class CheckBox2 extends View {
   private CheckBoxBase checkBoxBase = new CheckBoxBase(this);

   public CheckBox2(Context var1) {
      super(var1);
   }

   public float getProgress() {
      return this.checkBoxBase.getProgress();
   }

   public boolean isChecked() {
      return this.checkBoxBase.isChecked();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.checkBoxBase.onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.checkBoxBase.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      this.checkBoxBase.draw(var1);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.checkBoxBase.setBounds(0, 0, var4 - var2, var5 - var3);
   }

   public void setChecked(boolean var1, boolean var2) {
      this.checkBoxBase.setChecked(var1, var2);
   }

   public void setColor(String var1, String var2, String var3) {
      this.checkBoxBase.setColor(var1, var2, var3);
   }

   public void setDrawBackgroundAsArc(int var1) {
      this.checkBoxBase.setDrawBackgroundAsArc(var1);
   }

   public void setDrawUnchecked(boolean var1) {
      this.checkBoxBase.setDrawUnchecked(var1);
   }

   public void setEnabled(boolean var1) {
      this.checkBoxBase.setEnabled(var1);
      super.setEnabled(var1);
   }

   public void setProgressDelegate(CheckBoxBase.ProgressDelegate var1) {
      this.checkBoxBase.setProgressDelegate(var1);
   }

   public void setSize(int var1) {
      this.checkBoxBase.setSize(var1);
   }
}
