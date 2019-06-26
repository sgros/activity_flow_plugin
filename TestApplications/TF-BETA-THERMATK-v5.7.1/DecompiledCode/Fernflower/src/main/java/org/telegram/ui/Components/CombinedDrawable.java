package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;

public class CombinedDrawable extends Drawable implements Callback {
   private int backHeight;
   private int backWidth;
   private Drawable background;
   private boolean fullSize;
   private Drawable icon;
   private int iconHeight;
   private int iconWidth;
   private int left;
   private int offsetX;
   private int offsetY;
   private int top;

   public CombinedDrawable(Drawable var1, Drawable var2) {
      this.background = var1;
      this.icon = var2;
      if (var2 != null) {
         var2.setCallback(this);
      }

   }

   public CombinedDrawable(Drawable var1, Drawable var2, int var3, int var4) {
      this.background = var1;
      this.icon = var2;
      this.left = var3;
      this.top = var4;
      if (var2 != null) {
         var2.setCallback(this);
      }

   }

   public void draw(Canvas var1) {
      this.background.setBounds(this.getBounds());
      this.background.draw(var1);
      Drawable var2 = this.icon;
      if (var2 != null) {
         if (this.fullSize) {
            var2.setBounds(this.getBounds());
         } else {
            int var3;
            int var5;
            if (this.iconWidth != 0) {
               var3 = this.getBounds().centerX() - this.iconWidth / 2 + this.left + this.offsetX;
               int var4 = this.getBounds().centerY();
               var5 = this.iconHeight;
               var4 = var4 - var5 / 2 + this.top + this.offsetY;
               this.icon.setBounds(var3, var4, this.iconWidth + var3, var5 + var4);
            } else {
               var5 = this.getBounds().centerX() - this.icon.getIntrinsicWidth() / 2 + this.left;
               var3 = this.getBounds().centerY() - this.icon.getIntrinsicHeight() / 2 + this.top;
               var2 = this.icon;
               var2.setBounds(var5, var3, var2.getIntrinsicWidth() + var5, this.icon.getIntrinsicHeight() + var3);
            }
         }

         this.icon.draw(var1);
      }

   }

   public Drawable getBackground() {
      return this.background;
   }

   public ConstantState getConstantState() {
      return this.icon.getConstantState();
   }

   public Drawable getIcon() {
      return this.icon;
   }

   public int getIntrinsicHeight() {
      int var1 = this.backHeight;
      if (var1 == 0) {
         var1 = this.background.getIntrinsicHeight();
      }

      return var1;
   }

   public int getIntrinsicWidth() {
      int var1 = this.backWidth;
      if (var1 == 0) {
         var1 = this.background.getIntrinsicWidth();
      }

      return var1;
   }

   public int getMinimumHeight() {
      int var1 = this.backHeight;
      if (var1 == 0) {
         var1 = this.background.getMinimumHeight();
      }

      return var1;
   }

   public int getMinimumWidth() {
      int var1 = this.backWidth;
      if (var1 == 0) {
         var1 = this.background.getMinimumWidth();
      }

      return var1;
   }

   public int getOpacity() {
      return this.icon.getOpacity();
   }

   public int[] getState() {
      return this.icon.getState();
   }

   public void invalidateDrawable(Drawable var1) {
      this.invalidateSelf();
   }

   public boolean isStateful() {
      return this.icon.isStateful();
   }

   public void jumpToCurrentState() {
      this.icon.jumpToCurrentState();
   }

   protected boolean onStateChange(int[] var1) {
      return true;
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      this.scheduleSelf(var2, var3);
   }

   public void setAlpha(int var1) {
      this.icon.setAlpha(var1);
      this.background.setAlpha(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.icon.setColorFilter(var1);
   }

   public void setCustomSize(int var1, int var2) {
      this.backWidth = var1;
      this.backHeight = var2;
   }

   public void setFullsize(boolean var1) {
      this.fullSize = var1;
   }

   public void setIconOffset(int var1, int var2) {
      this.offsetX = var1;
      this.offsetY = var2;
   }

   public void setIconSize(int var1, int var2) {
      this.iconWidth = var1;
      this.iconHeight = var2;
   }

   public boolean setState(int[] var1) {
      this.icon.setState(var1);
      return true;
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      this.unscheduleSelf(var2);
   }
}
