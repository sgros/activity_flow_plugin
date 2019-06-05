package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.design.R;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class ScrimInsetsFrameLayout extends FrameLayout {
   Drawable insetForeground;
   Rect insets;
   private Rect tempRect;

   public ScrimInsetsFrameLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ScrimInsetsFrameLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ScrimInsetsFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.tempRect = new Rect();
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, R.styleable.ScrimInsetsFrameLayout, var3, R.style.Widget_Design_ScrimInsetsFrameLayout);
      this.insetForeground = var4.getDrawable(R.styleable.ScrimInsetsFrameLayout_insetForeground);
      var4.recycle();
      this.setWillNotDraw(true);
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            if (ScrimInsetsFrameLayout.this.insets == null) {
               ScrimInsetsFrameLayout.this.insets = new Rect();
            }

            ScrimInsetsFrameLayout.this.insets.set(var2.getSystemWindowInsetLeft(), var2.getSystemWindowInsetTop(), var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
            ScrimInsetsFrameLayout.this.onInsetsChanged(var2);
            ScrimInsetsFrameLayout var4 = ScrimInsetsFrameLayout.this;
            boolean var3;
            if (var2.hasSystemWindowInsets() && ScrimInsetsFrameLayout.this.insetForeground != null) {
               var3 = false;
            } else {
               var3 = true;
            }

            var4.setWillNotDraw(var3);
            ViewCompat.postInvalidateOnAnimation(ScrimInsetsFrameLayout.this);
            return var2.consumeSystemWindowInsets();
         }
      });
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      if (this.insets != null && this.insetForeground != null) {
         int var4 = var1.save();
         var1.translate((float)this.getScrollX(), (float)this.getScrollY());
         this.tempRect.set(0, 0, var2, this.insets.top);
         this.insetForeground.setBounds(this.tempRect);
         this.insetForeground.draw(var1);
         this.tempRect.set(0, var3 - this.insets.bottom, var2, var3);
         this.insetForeground.setBounds(this.tempRect);
         this.insetForeground.draw(var1);
         this.tempRect.set(0, this.insets.top, this.insets.left, var3 - this.insets.bottom);
         this.insetForeground.setBounds(this.tempRect);
         this.insetForeground.draw(var1);
         this.tempRect.set(var2 - this.insets.right, this.insets.top, var2, var3 - this.insets.bottom);
         this.insetForeground.setBounds(this.tempRect);
         this.insetForeground.draw(var1);
         var1.restoreToCount(var4);
      }

   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.insetForeground != null) {
         this.insetForeground.setCallback(this);
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.insetForeground != null) {
         this.insetForeground.setCallback((Callback)null);
      }

   }

   protected void onInsetsChanged(WindowInsetsCompat var1) {
   }
}
