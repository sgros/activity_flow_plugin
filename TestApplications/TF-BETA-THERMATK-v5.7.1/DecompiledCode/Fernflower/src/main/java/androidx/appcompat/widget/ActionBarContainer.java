package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import androidx.appcompat.R$id;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;

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
      ViewCompat.setBackground(this, new ActionBarBackgroundDrawable(this));
      TypedArray var5 = var1.obtainStyledAttributes(var2, R$styleable.ActionBar);
      this.mBackground = var5.getDrawable(R$styleable.ActionBar_background);
      this.mStackedBackground = var5.getDrawable(R$styleable.ActionBar_backgroundStacked);
      this.mHeight = var5.getDimensionPixelSize(R$styleable.ActionBar_height, -1);
      if (this.getId() == R$id.split_action_bar) {
         this.mIsSplit = true;
         this.mSplitBackground = var5.getDrawable(R$styleable.ActionBar_backgroundSplit);
      }

      boolean var3;
      label20: {
         var5.recycle();
         var3 = this.mIsSplit;
         boolean var4 = false;
         if (var3) {
            var3 = var4;
            if (this.mSplitBackground != null) {
               break label20;
            }
         } else {
            var3 = var4;
            if (this.mBackground != null) {
               break label20;
            }

            var3 = var4;
            if (this.mStackedBackground != null) {
               break label20;
            }
         }

         var3 = true;
      }

      this.setWillNotDraw(var3);
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
      Drawable var1 = this.mBackground;
      if (var1 != null && var1.isStateful()) {
         this.mBackground.setState(this.getDrawableState());
      }

      var1 = this.mStackedBackground;
      if (var1 != null && var1.isStateful()) {
         this.mStackedBackground.setState(this.getDrawableState());
      }

      var1 = this.mSplitBackground;
      if (var1 != null && var1.isStateful()) {
         this.mSplitBackground.setState(this.getDrawableState());
      }

   }

   public View getTabContainer() {
      return this.mTabContainer;
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      Drawable var1 = this.mBackground;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

      var1 = this.mStackedBackground;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

      var1 = this.mSplitBackground;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

   }

   public void onFinishInflate() {
      super.onFinishInflate();
      this.mActionBarView = this.findViewById(R$id.action_bar);
      this.mContextView = this.findViewById(R$id.action_context_bar);
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
      boolean var10 = true;
      boolean var11 = false;
      if (var6 != null && var6.getVisibility() != 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var6 != null && var6.getVisibility() != 8) {
         int var7 = this.getMeasuredHeight();
         LayoutParams var8 = (LayoutParams)var6.getLayoutParams();
         var6.layout(var2, var7 - var6.getMeasuredHeight() - var8.bottomMargin, var4, var7 - var8.bottomMargin);
      }

      boolean var9;
      if (this.mIsSplit) {
         Drawable var12 = this.mSplitBackground;
         if (var12 != null) {
            var12.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            var9 = var10;
         } else {
            var9 = false;
         }
      } else {
         var9 = var11;
         if (this.mBackground != null) {
            if (this.mActionBarView.getVisibility() == 0) {
               this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            } else {
               View var13 = this.mContextView;
               if (var13 != null && var13.getVisibility() == 0) {
                  this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
               } else {
                  this.mBackground.setBounds(0, 0, 0, 0);
               }
            }

            var9 = true;
         }

         this.mIsStacked = var1;
         if (var1) {
            Drawable var14 = this.mStackedBackground;
            if (var14 != null) {
               var14.setBounds(var6.getLeft(), var6.getTop(), var6.getRight(), var6.getBottom());
               var9 = var10;
            }
         }
      }

      if (var9) {
         this.invalidate();
      }

   }

   public void onMeasure(int var1, int var2) {
      int var3 = var2;
      if (this.mActionBarView == null) {
         var3 = var2;
         if (MeasureSpec.getMode(var2) == Integer.MIN_VALUE) {
            int var4 = this.mHeight;
            var3 = var2;
            if (var4 >= 0) {
               var3 = MeasureSpec.makeMeasureSpec(Math.min(var4, MeasureSpec.getSize(var2)), Integer.MIN_VALUE);
            }
         }
      }

      super.onMeasure(var1, var3);
      if (this.mActionBarView != null) {
         var2 = MeasureSpec.getMode(var3);
         View var5 = this.mTabContainer;
         if (var5 != null && var5.getVisibility() != 8 && var2 != 1073741824) {
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
      Drawable var2 = this.mBackground;
      if (var2 != null) {
         var2.setCallback((Callback)null);
         this.unscheduleDrawable(this.mBackground);
      }

      this.mBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         View var5 = this.mActionBarView;
         if (var5 != null) {
            this.mBackground.setBounds(var5.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
         }
      }

      boolean var4;
      label24: {
         boolean var3 = this.mIsSplit;
         var4 = true;
         if (var3) {
            if (this.mSplitBackground == null) {
               break label24;
            }
         } else if (this.mBackground == null && this.mStackedBackground == null) {
            break label24;
         }

         var4 = false;
      }

      this.setWillNotDraw(var4);
      this.invalidate();
   }

   public void setSplitBackground(Drawable var1) {
      Drawable var2 = this.mSplitBackground;
      if (var2 != null) {
         var2.setCallback((Callback)null);
         this.unscheduleDrawable(this.mSplitBackground);
      }

      this.mSplitBackground = var1;
      boolean var3 = false;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mIsSplit) {
            var1 = this.mSplitBackground;
            if (var1 != null) {
               var1.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            }
         }
      }

      boolean var4;
      label26: {
         if (this.mIsSplit) {
            var4 = var3;
            if (this.mSplitBackground != null) {
               break label26;
            }
         } else {
            var4 = var3;
            if (this.mBackground != null) {
               break label26;
            }

            var4 = var3;
            if (this.mStackedBackground != null) {
               break label26;
            }
         }

         var4 = true;
      }

      this.setWillNotDraw(var4);
      this.invalidate();
   }

   public void setStackedBackground(Drawable var1) {
      Drawable var2 = this.mStackedBackground;
      if (var2 != null) {
         var2.setCallback((Callback)null);
         this.unscheduleDrawable(this.mStackedBackground);
      }

      this.mStackedBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mIsStacked) {
            var1 = this.mStackedBackground;
            if (var1 != null) {
               var1.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
         }
      }

      boolean var4;
      label26: {
         boolean var3 = this.mIsSplit;
         var4 = true;
         if (var3) {
            if (this.mSplitBackground == null) {
               break label26;
            }
         } else if (this.mBackground == null && this.mStackedBackground == null) {
            break label26;
         }

         var4 = false;
      }

      this.setWillNotDraw(var4);
      this.invalidate();
   }

   public void setTabContainer(ScrollingTabContainerView var1) {
      View var2 = this.mTabContainer;
      if (var2 != null) {
         this.removeView(var2);
      }

      this.mTabContainer = var1;
      if (var1 != null) {
         this.addView(var1);
         android.view.ViewGroup.LayoutParams var3 = var1.getLayoutParams();
         var3.width = -1;
         var3.height = -2;
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

      Drawable var3 = this.mBackground;
      if (var3 != null) {
         var3.setVisible(var2, false);
      }

      var3 = this.mStackedBackground;
      if (var3 != null) {
         var3.setVisible(var2, false);
      }

      var3 = this.mSplitBackground;
      if (var3 != null) {
         var3.setVisible(var2, false);
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
