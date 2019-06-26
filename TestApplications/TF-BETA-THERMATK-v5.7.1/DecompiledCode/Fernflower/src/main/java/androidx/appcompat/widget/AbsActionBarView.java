package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

abstract class AbsActionBarView extends ViewGroup {
   protected ActionMenuPresenter mActionMenuPresenter;
   protected int mContentHeight;
   private boolean mEatingHover;
   private boolean mEatingTouch;
   protected ActionMenuView mMenuView;
   protected final Context mPopupContext;
   protected final AbsActionBarView.VisibilityAnimListener mVisAnimListener;
   protected ViewPropertyAnimatorCompat mVisibilityAnim;

   AbsActionBarView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   AbsActionBarView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   AbsActionBarView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mVisAnimListener = new AbsActionBarView.VisibilityAnimListener();
      TypedValue var4 = new TypedValue();
      if (var1.getTheme().resolveAttribute(R$attr.actionBarPopupTheme, var4, true)) {
         var3 = var4.resourceId;
         if (var3 != 0) {
            this.mPopupContext = new ContextThemeWrapper(var1, var3);
            return;
         }
      }

      this.mPopupContext = var1;
   }

   protected static int next(int var0, int var1, boolean var2) {
      if (var2) {
         var0 -= var1;
      } else {
         var0 += var1;
      }

      return var0;
   }

   public int getAnimatedVisibility() {
      return this.mVisibilityAnim != null ? this.mVisAnimListener.mFinalVisibility : this.getVisibility();
   }

   public int getContentHeight() {
      return this.mContentHeight;
   }

   protected int measureChildView(View var1, int var2, int var3, int var4) {
      var1.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), var3);
      return Math.max(0, var2 - var1.getMeasuredWidth() - var4);
   }

   protected void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      TypedArray var2 = this.getContext().obtainStyledAttributes((AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      this.setContentHeight(var2.getLayoutDimension(R$styleable.ActionBar_height, 0));
      var2.recycle();
      ActionMenuPresenter var3 = this.mActionMenuPresenter;
      if (var3 != null) {
         var3.onConfigurationChanged(var1);
      }

   }

   public boolean onHoverEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 9) {
         this.mEatingHover = false;
      }

      if (!this.mEatingHover) {
         boolean var3 = super.onHoverEvent(var1);
         if (var2 == 9 && !var3) {
            this.mEatingHover = true;
         }
      }

      if (var2 == 10 || var2 == 3) {
         this.mEatingHover = false;
      }

      return true;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.mEatingTouch = false;
      }

      if (!this.mEatingTouch) {
         boolean var3 = super.onTouchEvent(var1);
         if (var2 == 0 && !var3) {
            this.mEatingTouch = true;
         }
      }

      if (var2 == 1 || var2 == 3) {
         this.mEatingTouch = false;
      }

      return true;
   }

   protected int positionChild(View var1, int var2, int var3, int var4, boolean var5) {
      int var6 = var1.getMeasuredWidth();
      int var7 = var1.getMeasuredHeight();
      var3 += (var4 - var7) / 2;
      if (var5) {
         var1.layout(var2 - var6, var3, var2, var7 + var3);
      } else {
         var1.layout(var2, var3, var2 + var6, var7 + var3);
      }

      var2 = var6;
      if (var5) {
         var2 = -var6;
      }

      return var2;
   }

   public abstract void setContentHeight(int var1);

   public void setVisibility(int var1) {
      if (var1 != this.getVisibility()) {
         ViewPropertyAnimatorCompat var2 = this.mVisibilityAnim;
         if (var2 != null) {
            var2.cancel();
            throw null;
         }

         super.setVisibility(var1);
      }

   }

   protected class VisibilityAnimListener implements ViewPropertyAnimatorListener {
      private boolean mCanceled = false;
      int mFinalVisibility;
   }
}
