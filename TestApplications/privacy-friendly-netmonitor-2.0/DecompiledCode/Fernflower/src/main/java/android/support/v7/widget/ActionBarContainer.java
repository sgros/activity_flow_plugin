package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarContainer extends FrameLayout {
   private View mActionBarView;
   Drawable mBackground;
   private View mContextView;
   private int mHeight;
   boolean mIsSplit;
   boolean mIsStacked;
   private boolean mIsTransitioning;
   Drawable mSplitBackground;
   Drawable mStackedBackground;
   private View mTabContainer;

   public ActionBarContainer(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionBarContainer(Context var1, AttributeSet var2) {
      super(var1, var2);
      Object var3;
      if (VERSION.SDK_INT >= 21) {
         var3 = new ActionBarBackgroundDrawableV21(this);
      } else {
         var3 = new ActionBarBackgroundDrawable(this);
      }

      ViewCompat.setBackground(this, (Drawable)var3);
      TypedArray var6 = var1.obtainStyledAttributes(var2, R.styleable.ActionBar);
      this.mBackground = var6.getDrawable(R.styleable.ActionBar_background);
      this.mStackedBackground = var6.getDrawable(R.styleable.ActionBar_backgroundStacked);
      this.mHeight = var6.getDimensionPixelSize(R.styleable.ActionBar_height, -1);
      if (this.getId() == R.id.split_action_bar) {
         this.mIsSplit = true;
         this.mSplitBackground = var6.getDrawable(R.styleable.ActionBar_backgroundSplit);
      }

      boolean var4;
      label25: {
         var6.recycle();
         var4 = this.mIsSplit;
         boolean var5 = false;
         if (var4) {
            var4 = var5;
            if (this.mSplitBackground != null) {
               break label25;
            }
         } else {
            var4 = var5;
            if (this.mBackground != null) {
               break label25;
            }

            var4 = var5;
            if (this.mStackedBackground != null) {
               break label25;
            }
         }

         var4 = true;
      }

      this.setWillNotDraw(var4);
   }

   private int getMeasuredHeightWithMargins(View var1) {
      LayoutParams var2 = (LayoutParams)var1.getLayoutParams();
      return var1.getMeasuredHeight() + var2.topMargin + var2.bottomMargin;
   }

   private boolean isCollapsed(View var1) {
      boolean var2;
      if (var1 != null && var1.getVisibility() != 8 && var1.getMeasuredHeight() != 0) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      if (this.mBackground != null && this.mBackground.isStateful()) {
         this.mBackground.setState(this.getDrawableState());
      }

      if (this.mStackedBackground != null && this.mStackedBackground.isStateful()) {
         this.mStackedBackground.setState(this.getDrawableState());
      }

      if (this.mSplitBackground != null && this.mSplitBackground.isStateful()) {
         this.mSplitBackground.setState(this.getDrawableState());
      }

   }

   public View getTabContainer() {
      return this.mTabContainer;
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      if (this.mBackground != null) {
         this.mBackground.jumpToCurrentState();
      }

      if (this.mStackedBackground != null) {
         this.mStackedBackground.jumpToCurrentState();
      }

      if (this.mSplitBackground != null) {
         this.mSplitBackground.jumpToCurrentState();
      }

   }

   public void onFinishInflate() {
      super.onFinishInflate();
      this.mActionBarView = this.findViewById(R.id.action_bar);
      this.mContextView = this.findViewById(R.id.action_context_bar);
   }

   public boolean onHoverEvent(MotionEvent var1) {
      super.onHoverEvent(var1);
      return true;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2;
      if (!this.mIsTransitioning && !super.onInterceptTouchEvent(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      View var6 = this.mTabContainer;
      boolean var12 = true;
      boolean var7 = false;
      boolean var11 = false;
      if (var6 != null && var6.getVisibility() != 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var6 != null && var6.getVisibility() != 8) {
         int var8 = this.getMeasuredHeight();
         LayoutParams var9 = (LayoutParams)var6.getLayoutParams();
         var6.layout(var2, var8 - var6.getMeasuredHeight() - var9.bottomMargin, var4, var8 - var9.bottomMargin);
      }

      if (this.mIsSplit) {
         if (this.mSplitBackground != null) {
            this.mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            var11 = var12;
         }
      } else {
         boolean var10 = var7;
         if (this.mBackground != null) {
            if (this.mActionBarView.getVisibility() == 0) {
               this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            } else if (this.mContextView != null && this.mContextView.getVisibility() == 0) {
               this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
            } else {
               this.mBackground.setBounds(0, 0, 0, 0);
            }

            var10 = true;
         }

         this.mIsStacked = var1;
         var11 = var10;
         if (var1) {
            var11 = var10;
            if (this.mStackedBackground != null) {
               this.mStackedBackground.setBounds(var6.getLeft(), var6.getTop(), var6.getRight(), var6.getBottom());
               var11 = var12;
            }
         }
      }

      if (var11) {
         this.invalidate();
      }

   }

   public void onMeasure(int var1, int var2) {
      int var3 = var2;
      if (this.mActionBarView == null) {
         var3 = var2;
         if (MeasureSpec.getMode(var2) == Integer.MIN_VALUE) {
            var3 = var2;
            if (this.mHeight >= 0) {
               var3 = MeasureSpec.makeMeasureSpec(Math.min(this.mHeight, MeasureSpec.getSize(var2)), Integer.MIN_VALUE);
            }
         }
      }

      super.onMeasure(var1, var3);
      if (this.mActionBarView != null) {
         var2 = MeasureSpec.getMode(var3);
         if (this.mTabContainer != null && this.mTabContainer.getVisibility() != 8 && var2 != 1073741824) {
            if (!this.isCollapsed(this.mActionBarView)) {
               var1 = this.getMeasuredHeightWithMargins(this.mActionBarView);
            } else if (!this.isCollapsed(this.mContextView)) {
               var1 = this.getMeasuredHeightWithMargins(this.mContextView);
            } else {
               var1 = 0;
            }

            if (var2 == Integer.MIN_VALUE) {
               var2 = MeasureSpec.getSize(var3);
            } else {
               var2 = Integer.MAX_VALUE;
            }

            this.setMeasuredDimension(this.getMeasuredWidth(), Math.min(var1 + this.getMeasuredHeightWithMargins(this.mTabContainer), var2));
         }

      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      super.onTouchEvent(var1);
      return true;
   }

   public void setPrimaryBackground(Drawable var1) {
      if (this.mBackground != null) {
         this.mBackground.setCallback((Callback)null);
         this.unscheduleDrawable(this.mBackground);
      }

      this.mBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mActionBarView != null) {
            this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
         }
      }

      boolean var2;
      label24: {
         var2 = this.mIsSplit;
         boolean var3 = false;
         if (var2) {
            var2 = var3;
            if (this.mSplitBackground != null) {
               break label24;
            }
         } else {
            var2 = var3;
            if (this.mBackground != null) {
               break label24;
            }

            var2 = var3;
            if (this.mStackedBackground != null) {
               break label24;
            }
         }

         var2 = true;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setSplitBackground(Drawable var1) {
      if (this.mSplitBackground != null) {
         this.mSplitBackground.setCallback((Callback)null);
         this.unscheduleDrawable(this.mSplitBackground);
      }

      this.mSplitBackground = var1;
      boolean var2 = false;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mIsSplit && this.mSplitBackground != null) {
            this.mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
         }
      }

      boolean var3;
      label26: {
         if (this.mIsSplit) {
            var3 = var2;
            if (this.mSplitBackground != null) {
               break label26;
            }
         } else {
            var3 = var2;
            if (this.mBackground != null) {
               break label26;
            }

            var3 = var2;
            if (this.mStackedBackground != null) {
               break label26;
            }
         }

         var3 = true;
      }

      this.setWillNotDraw(var3);
      this.invalidate();
   }

   public void setStackedBackground(Drawable var1) {
      if (this.mStackedBackground != null) {
         this.mStackedBackground.setCallback((Callback)null);
         this.unscheduleDrawable(this.mStackedBackground);
      }

      this.mStackedBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mIsStacked && this.mStackedBackground != null) {
            this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
         }
      }

      boolean var2;
      label26: {
         var2 = this.mIsSplit;
         boolean var3 = false;
         if (var2) {
            var2 = var3;
            if (this.mSplitBackground != null) {
               break label26;
            }
         } else {
            var2 = var3;
            if (this.mBackground != null) {
               break label26;
            }

            var2 = var3;
            if (this.mStackedBackground != null) {
               break label26;
            }
         }

         var2 = true;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setTabContainer(ScrollingTabContainerView var1) {
      if (this.mTabContainer != null) {
         this.removeView(this.mTabContainer);
      }

      this.mTabContainer = var1;
      if (var1 != null) {
         this.addView(var1);
         android.view.ViewGroup.LayoutParams var2 = var1.getLayoutParams();
         var2.width = -1;
         var2.height = -2;
         var1.setAllowCollapse(false);
      }

   }

   public void setTransitioning(boolean var1) {
      this.mIsTransitioning = var1;
      int var2;
      if (var1) {
         var2 = 393216;
      } else {
         var2 = 262144;
      }

      this.setDescendantFocusability(var2);
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.mBackground != null) {
         this.mBackground.setVisible(var2, false);
      }

      if (this.mStackedBackground != null) {
         this.mStackedBackground.setVisible(var2, false);
      }

      if (this.mSplitBackground != null) {
         this.mSplitBackground.setVisible(var2, false);
      }

   }

   public ActionMode startActionModeForChild(View var1, android.view.ActionMode.Callback var2) {
      return null;
   }

   public ActionMode startActionModeForChild(View var1, android.view.ActionMode.Callback var2, int var3) {
      return var3 != 0 ? super.startActionModeForChild(var1, var2, var3) : null;
   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if ((var1 != this.mBackground || this.mIsSplit) && (var1 != this.mStackedBackground || !this.mIsStacked) && (var1 != this.mSplitBackground || !this.mIsSplit) && !super.verifyDrawable(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }
}
