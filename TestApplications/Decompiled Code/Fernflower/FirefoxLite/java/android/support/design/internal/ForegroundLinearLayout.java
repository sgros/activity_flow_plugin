package android.support.design.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.design.R;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;

public class ForegroundLinearLayout extends LinearLayoutCompat {
   private Drawable foreground;
   boolean foregroundBoundsChanged;
   private int foregroundGravity;
   protected boolean mForegroundInPadding;
   private final Rect overlayBounds;
   private final Rect selfBounds;

   public ForegroundLinearLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ForegroundLinearLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ForegroundLinearLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.selfBounds = new Rect();
      this.overlayBounds = new Rect();
      this.foregroundGravity = 119;
      this.mForegroundInPadding = true;
      this.foregroundBoundsChanged = false;
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, R.styleable.ForegroundLinearLayout, var3, 0);
      this.foregroundGravity = var4.getInt(R.styleable.ForegroundLinearLayout_android_foregroundGravity, this.foregroundGravity);
      Drawable var5 = var4.getDrawable(R.styleable.ForegroundLinearLayout_android_foreground);
      if (var5 != null) {
         this.setForeground(var5);
      }

      this.mForegroundInPadding = var4.getBoolean(R.styleable.ForegroundLinearLayout_foregroundInsidePadding, true);
      var4.recycle();
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.foreground != null) {
         Drawable var2 = this.foreground;
         if (this.foregroundBoundsChanged) {
            this.foregroundBoundsChanged = false;
            Rect var3 = this.selfBounds;
            Rect var4 = this.overlayBounds;
            int var5 = this.getRight() - this.getLeft();
            int var6 = this.getBottom() - this.getTop();
            if (this.mForegroundInPadding) {
               var3.set(0, 0, var5, var6);
            } else {
               var3.set(this.getPaddingLeft(), this.getPaddingTop(), var5 - this.getPaddingRight(), var6 - this.getPaddingBottom());
            }

            Gravity.apply(this.foregroundGravity, var2.getIntrinsicWidth(), var2.getIntrinsicHeight(), var3, var4);
            var2.setBounds(var4);
         }

         var2.draw(var1);
      }

   }

   @TargetApi(21)
   public void drawableHotspotChanged(float var1, float var2) {
      super.drawableHotspotChanged(var1, var2);
      if (this.foreground != null) {
         this.foreground.setHotspot(var1, var2);
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      if (this.foreground != null && this.foreground.isStateful()) {
         this.foreground.setState(this.getDrawableState());
      }

   }

   public Drawable getForeground() {
      return this.foreground;
   }

   public int getForegroundGravity() {
      return this.foregroundGravity;
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      if (this.foreground != null) {
         this.foreground.jumpToCurrentState();
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.foregroundBoundsChanged |= var1;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      this.foregroundBoundsChanged = true;
   }

   public void setForeground(Drawable var1) {
      if (this.foreground != var1) {
         if (this.foreground != null) {
            this.foreground.setCallback((Callback)null);
            this.unscheduleDrawable(this.foreground);
         }

         this.foreground = var1;
         if (var1 != null) {
            this.setWillNotDraw(false);
            var1.setCallback(this);
            if (var1.isStateful()) {
               var1.setState(this.getDrawableState());
            }

            if (this.foregroundGravity == 119) {
               var1.getPadding(new Rect());
            }
         } else {
            this.setWillNotDraw(true);
         }

         this.requestLayout();
         this.invalidate();
      }

   }

   public void setForegroundGravity(int var1) {
      if (this.foregroundGravity != var1) {
         int var2 = var1;
         if ((8388615 & var1) == 0) {
            var2 = var1 | 8388611;
         }

         var1 = var2;
         if ((var2 & 112) == 0) {
            var1 = var2 | 48;
         }

         this.foregroundGravity = var1;
         if (this.foregroundGravity == 119 && this.foreground != null) {
            Rect var3 = new Rect();
            this.foreground.getPadding(var3);
         }

         this.requestLayout();
      }

   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if (!super.verifyDrawable(var1) && var1 != this.foreground) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }
}
