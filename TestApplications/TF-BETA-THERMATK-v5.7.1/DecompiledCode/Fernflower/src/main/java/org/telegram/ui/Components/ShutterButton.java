package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;

public class ShutterButton extends View {
   private static final int LONG_PRESS_TIME = 800;
   private ShutterButton.ShutterButtonDelegate delegate;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private long lastUpdateTime;
   private Runnable longPressed = new Runnable() {
      public void run() {
         if (ShutterButton.this.delegate != null && !ShutterButton.this.delegate.shutterLongPressed()) {
            ShutterButton.this.processRelease = false;
         }

      }
   };
   private boolean pressed;
   private boolean processRelease;
   private Paint redPaint;
   private float redProgress;
   private Drawable shadowDrawable = this.getResources().getDrawable(2131165334);
   private ShutterButton.State state;
   private long totalTime;
   private Paint whitePaint = new Paint(1);

   public ShutterButton(Context var1) {
      super(var1);
      this.whitePaint.setStyle(Style.FILL);
      this.whitePaint.setColor(-1);
      this.redPaint = new Paint(1);
      this.redPaint.setStyle(Style.FILL);
      this.redPaint.setColor(-3324089);
      this.state = ShutterButton.State.DEFAULT;
   }

   private void setHighlighted(boolean var1) {
      AnimatorSet var2 = new AnimatorSet();
      if (var1) {
         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.06F}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.06F})});
      } else {
         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.0F})});
         var2.setStartDelay(40L);
      }

      var2.setDuration(120L);
      var2.setInterpolator(this.interpolator);
      var2.start();
   }

   public ShutterButton.ShutterButtonDelegate getDelegate() {
      return this.delegate;
   }

   public ShutterButton.State getState() {
      return this.state;
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.getMeasuredWidth() / 2;
      int var3 = this.getMeasuredHeight() / 2;
      this.shadowDrawable.setBounds(var2 - AndroidUtilities.dp(36.0F), var3 - AndroidUtilities.dp(36.0F), AndroidUtilities.dp(36.0F) + var2, AndroidUtilities.dp(36.0F) + var3);
      this.shadowDrawable.draw(var1);
      if (!this.pressed && this.getScaleX() == 1.0F) {
         if (this.redProgress != 0.0F) {
            this.redProgress = 0.0F;
         }
      } else {
         float var4 = (this.getScaleX() - 1.0F) / 0.06F;
         this.whitePaint.setAlpha((int)(255.0F * var4));
         float var5 = (float)var2;
         float var6 = (float)var3;
         var1.drawCircle(var5, var6, (float)AndroidUtilities.dp(26.0F), this.whitePaint);
         if (this.state == ShutterButton.State.RECORDING) {
            if (this.redProgress != 1.0F) {
               long var7 = Math.abs(System.currentTimeMillis() - this.lastUpdateTime);
               long var9 = var7;
               if (var7 > 17L) {
                  var9 = 17L;
               }

               this.totalTime += var9;
               if (this.totalTime > 120L) {
                  this.totalTime = 120L;
               }

               this.redProgress = this.interpolator.getInterpolation((float)this.totalTime / 120.0F);
               this.invalidate();
            }

            var1.drawCircle(var5, var6, (float)AndroidUtilities.dp(26.0F) * var4 * this.redProgress, this.redPaint);
         } else if (this.redProgress != 0.0F) {
            var1.drawCircle(var5, var6, (float)AndroidUtilities.dp(26.0F) * var4, this.redPaint);
         }
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.Button");
      var1.setClickable(true);
      var1.setLongClickable(true);
      if (VERSION.SDK_INT >= 21) {
         var1.addAction(new AccessibilityAction(AccessibilityAction.ACTION_CLICK.getId(), LocaleController.getString("AccActionTakePicture", 2131558411)));
         var1.addAction(new AccessibilityAction(AccessibilityAction.ACTION_LONG_CLICK.getId(), LocaleController.getString("AccActionRecordVideo", 2131558410)));
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(AndroidUtilities.dp(84.0F), AndroidUtilities.dp(84.0F));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      float var2 = var1.getX();
      float var3 = var1.getX();
      int var4 = var1.getAction();
      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 == 3) {
                  this.setHighlighted(false);
                  this.pressed = false;
               }
            } else if (var2 < 0.0F || var3 < 0.0F || var2 > (float)this.getMeasuredWidth() || var3 > (float)this.getMeasuredHeight()) {
               AndroidUtilities.cancelRunOnUIThread(this.longPressed);
               if (this.state == ShutterButton.State.RECORDING) {
                  this.setHighlighted(false);
                  this.delegate.shutterCancel();
                  this.setState(ShutterButton.State.DEFAULT, true);
               }
            }
         } else {
            this.setHighlighted(false);
            AndroidUtilities.cancelRunOnUIThread(this.longPressed);
            if (this.processRelease && var2 >= 0.0F && var3 >= 0.0F && var2 <= (float)this.getMeasuredWidth() && var3 <= (float)this.getMeasuredHeight()) {
               this.delegate.shutterReleased();
            }
         }
      } else {
         AndroidUtilities.runOnUIThread(this.longPressed, 800L);
         this.pressed = true;
         this.processRelease = true;
         this.setHighlighted(true);
      }

      return true;
   }

   public void setDelegate(ShutterButton.ShutterButtonDelegate var1) {
      this.delegate = var1;
   }

   public void setScaleX(float var1) {
      super.setScaleX(var1);
      this.invalidate();
   }

   public void setState(ShutterButton.State var1, boolean var2) {
      if (this.state != var1) {
         this.state = var1;
         if (var2) {
            this.lastUpdateTime = System.currentTimeMillis();
            this.totalTime = 0L;
            if (this.state != ShutterButton.State.RECORDING) {
               this.redProgress = 0.0F;
            }
         } else if (this.state == ShutterButton.State.RECORDING) {
            this.redProgress = 1.0F;
         } else {
            this.redProgress = 0.0F;
         }

         this.invalidate();
      }

   }

   public interface ShutterButtonDelegate {
      void shutterCancel();

      boolean shutterLongPressed();

      void shutterReleased();
   }

   public static enum State {
      DEFAULT,
      RECORDING;
   }
}
