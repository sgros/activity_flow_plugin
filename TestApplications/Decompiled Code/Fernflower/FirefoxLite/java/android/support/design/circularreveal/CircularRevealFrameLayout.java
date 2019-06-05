package android.support.design.circularreveal;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class CircularRevealFrameLayout extends FrameLayout implements CircularRevealWidget {
   private final CircularRevealHelper helper;

   public void actualDraw(Canvas var1) {
      super.draw(var1);
   }

   public boolean actualIsOpaque() {
      return super.isOpaque();
   }

   public void buildCircularRevealCache() {
      this.helper.buildCircularRevealCache();
   }

   public void destroyCircularRevealCache() {
      this.helper.destroyCircularRevealCache();
   }

   @SuppressLint({"MissingSuperCall"})
   public void draw(Canvas var1) {
      if (this.helper != null) {
         this.helper.draw(var1);
      } else {
         super.draw(var1);
      }

   }

   public Drawable getCircularRevealOverlayDrawable() {
      return this.helper.getCircularRevealOverlayDrawable();
   }

   public int getCircularRevealScrimColor() {
      return this.helper.getCircularRevealScrimColor();
   }

   public CircularRevealWidget.RevealInfo getRevealInfo() {
      return this.helper.getRevealInfo();
   }

   public boolean isOpaque() {
      return this.helper != null ? this.helper.isOpaque() : super.isOpaque();
   }

   public void setCircularRevealOverlayDrawable(Drawable var1) {
      this.helper.setCircularRevealOverlayDrawable(var1);
   }

   public void setCircularRevealScrimColor(int var1) {
      this.helper.setCircularRevealScrimColor(var1);
   }

   public void setRevealInfo(CircularRevealWidget.RevealInfo var1) {
      this.helper.setRevealInfo(var1);
   }
}
