package org.mozilla.focus.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class ShiftDrawable extends DrawableWrapper {
   private final ValueAnimator mAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
   private Path mPath;
   private final Rect mVisibleRect = new Rect();

   public ShiftDrawable(Drawable var1, int var2, Interpolator var3) {
      super(var1);
      this.mAnimator.setDuration((long)var2);
      this.mAnimator.setRepeatCount(-1);
      ValueAnimator var4 = this.mAnimator;
      Object var5 = var3;
      if (var3 == null) {
         var5 = new LinearInterpolator();
      }

      var4.setInterpolator((TimeInterpolator)var5);
      this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            ShiftDrawable.this.invalidateSelf();
         }
      });
   }

   private void updateBounds() {
      Rect var1 = this.getBounds();
      int var2 = (int)((float)var1.width() * (float)this.getLevel() / 10000.0F);
      float var3 = (float)var1.height() / 2.0F;
      this.mVisibleRect.set(var1.left, var1.top, var1.left + var2, var1.height());
      this.mPath = new Path();
      this.mPath.addRect((float)var1.left, (float)var1.top, (float)(var1.left + var2) - var3, (float)var1.height(), Direction.CCW);
      this.mPath.addCircle((float)(var1.left + var2) - var3, var3, var3, Direction.CCW);
   }

   public void draw(Canvas var1) {
      Drawable var2 = this.getWrappedDrawable();
      float var3 = this.mAnimator.getAnimatedFraction();
      int var4 = this.mVisibleRect.width();
      int var5 = (int)((float)var4 * var3);
      int var6 = var1.save();
      if (this.mPath != null) {
         var1.clipPath(this.mPath);
      }

      var1.save();
      var1.translate((float)(-var5), 0.0F);
      var2.draw(var1);
      var1.restore();
      var1.save();
      var1.translate((float)(var4 - var5), 0.0F);
      var2.draw(var1);
      var1.restore();
      var1.restoreToCount(var6);
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.updateBounds();
   }

   protected boolean onLevelChange(int var1) {
      boolean var2 = super.onLevelChange(var1);
      this.updateBounds();
      return var2;
   }

   public boolean setVisible(boolean var1, boolean var2) {
      var2 = super.setVisible(var1, var2);
      if (!var1) {
         this.stop();
      }

      return var2;
   }

   public void start() {
      if (!this.mAnimator.isRunning()) {
         this.mAnimator.start();
      }
   }

   public void stop() {
      if (this.mAnimator.isRunning()) {
         this.mAnimator.end();
      }
   }
}
