package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.appcompat.R;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

class DropDownListView extends ListView {
   private ViewPropertyAnimatorCompat mClickAnimation;
   private boolean mDrawsInPressedState;
   private boolean mHijackFocus;
   private Field mIsChildViewEnabled;
   private boolean mListSelectionHidden;
   private int mMotionPosition;
   DropDownListView.ResolveHoverRunnable mResolveHoverRunnable;
   private ListViewAutoScrollHelper mScrollHelper;
   private int mSelectionBottomPadding = 0;
   private int mSelectionLeftPadding = 0;
   private int mSelectionRightPadding = 0;
   private int mSelectionTopPadding = 0;
   private DropDownListView.GateKeeperDrawable mSelector;
   private final Rect mSelectorRect = new Rect();

   DropDownListView(Context var1, boolean var2) {
      super(var1, (AttributeSet)null, R.attr.dropDownListViewStyle);
      this.mHijackFocus = var2;
      this.setCacheColorHint(0);

      try {
         this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
         this.mIsChildViewEnabled.setAccessible(true);
      } catch (NoSuchFieldException var3) {
         var3.printStackTrace();
      }

   }

   private void clearPressedItem() {
      this.mDrawsInPressedState = false;
      this.setPressed(false);
      this.drawableStateChanged();
      View var1 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
      if (var1 != null) {
         var1.setPressed(false);
      }

      if (this.mClickAnimation != null) {
         this.mClickAnimation.cancel();
         this.mClickAnimation = null;
      }

   }

   private void clickPressedItem(View var1, int var2) {
      this.performItemClick(var1, var2, this.getItemIdAtPosition(var2));
   }

   private void drawSelectorCompat(Canvas var1) {
      if (!this.mSelectorRect.isEmpty()) {
         Drawable var2 = this.getSelector();
         if (var2 != null) {
            var2.setBounds(this.mSelectorRect);
            var2.draw(var1);
         }
      }

   }

   private void positionSelectorCompat(int var1, View var2) {
      Rect var3 = this.mSelectorRect;
      var3.set(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom());
      var3.left -= this.mSelectionLeftPadding;
      var3.top -= this.mSelectionTopPadding;
      var3.right += this.mSelectionRightPadding;
      var3.bottom += this.mSelectionBottomPadding;

      IllegalAccessException var10000;
      label31: {
         boolean var10001;
         try {
            boolean var4 = this.mIsChildViewEnabled.getBoolean(this);
            if (var2.isEnabled() == var4) {
               return;
            }

            this.mIsChildViewEnabled.set(this, var4 ^ true);
         } catch (IllegalAccessException var6) {
            var10000 = var6;
            var10001 = false;
            break label31;
         }

         if (var1 == -1) {
            return;
         }

         try {
            this.refreshDrawableState();
            return;
         } catch (IllegalAccessException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      IllegalAccessException var7 = var10000;
      var7.printStackTrace();
   }

   private void positionSelectorLikeFocusCompat(int var1, View var2) {
      Drawable var3 = this.getSelector();
      boolean var4 = true;
      boolean var5;
      if (var3 != null && var1 != -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         var3.setVisible(false, false);
      }

      this.positionSelectorCompat(var1, var2);
      if (var5) {
         Rect var8 = this.mSelectorRect;
         float var6 = var8.exactCenterX();
         float var7 = var8.exactCenterY();
         if (this.getVisibility() != 0) {
            var4 = false;
         }

         var3.setVisible(var4, false);
         DrawableCompat.setHotspot(var3, var6, var7);
      }

   }

   private void positionSelectorLikeTouchCompat(int var1, View var2, float var3, float var4) {
      this.positionSelectorLikeFocusCompat(var1, var2);
      Drawable var5 = this.getSelector();
      if (var5 != null && var1 != -1) {
         DrawableCompat.setHotspot(var5, var3, var4);
      }

   }

   private void setPressedItem(View var1, int var2, float var3, float var4) {
      this.mDrawsInPressedState = true;
      if (VERSION.SDK_INT >= 21) {
         this.drawableHotspotChanged(var3, var4);
      }

      if (!this.isPressed()) {
         this.setPressed(true);
      }

      this.layoutChildren();
      if (this.mMotionPosition != -1) {
         View var5 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
         if (var5 != null && var5 != var1 && var5.isPressed()) {
            var5.setPressed(false);
         }
      }

      this.mMotionPosition = var2;
      float var6 = (float)var1.getLeft();
      float var7 = (float)var1.getTop();
      if (VERSION.SDK_INT >= 21) {
         var1.drawableHotspotChanged(var3 - var6, var4 - var7);
      }

      if (!var1.isPressed()) {
         var1.setPressed(true);
      }

      this.positionSelectorLikeTouchCompat(var2, var1, var3, var4);
      this.setSelectorEnabled(false);
      this.refreshDrawableState();
   }

   private void setSelectorEnabled(boolean var1) {
      if (this.mSelector != null) {
         this.mSelector.setEnabled(var1);
      }

   }

   private boolean touchModeDrawsInPressedStateCompat() {
      return this.mDrawsInPressedState;
   }

   private void updateSelectorStateCompat() {
      Drawable var1 = this.getSelector();
      if (var1 != null && this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
         var1.setState(this.getDrawableState());
      }

   }

   protected void dispatchDraw(Canvas var1) {
      this.drawSelectorCompat(var1);
      super.dispatchDraw(var1);
   }

   protected void drawableStateChanged() {
      if (this.mResolveHoverRunnable == null) {
         super.drawableStateChanged();
         this.setSelectorEnabled(true);
         this.updateSelectorStateCompat();
      }
   }

   public boolean hasFocus() {
      boolean var1;
      if (!this.mHijackFocus && !super.hasFocus()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean hasWindowFocus() {
      boolean var1;
      if (!this.mHijackFocus && !super.hasWindowFocus()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isFocused() {
      boolean var1;
      if (!this.mHijackFocus && !super.isFocused()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isInTouchMode() {
      boolean var1;
      if ((!this.mHijackFocus || !this.mListSelectionHidden) && !super.isInTouchMode()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public int measureHeightOfChildrenCompat(int var1, int var2, int var3, int var4, int var5) {
      var2 = this.getListPaddingTop();
      var3 = this.getListPaddingBottom();
      this.getListPaddingLeft();
      this.getListPaddingRight();
      int var6 = this.getDividerHeight();
      Drawable var7 = this.getDivider();
      ListAdapter var8 = this.getAdapter();
      if (var8 == null) {
         return var2 + var3;
      } else {
         if (var6 <= 0 || var7 == null) {
            var6 = 0;
         }

         int var9 = var8.getCount();
         var3 += var2;
         View var16 = null;
         int var10 = 0;
         int var11 = 0;

         int var12;
         for(var2 = 0; var10 < var9; var2 = var12) {
            var12 = var8.getItemViewType(var10);
            int var13 = var11;
            if (var12 != var11) {
               var16 = null;
               var13 = var12;
            }

            View var14 = var8.getView(var10, var16, this);
            LayoutParams var15 = var14.getLayoutParams();
            LayoutParams var17 = var15;
            if (var15 == null) {
               var17 = this.generateDefaultLayoutParams();
               var14.setLayoutParams(var17);
            }

            if (var17.height > 0) {
               var11 = MeasureSpec.makeMeasureSpec(var17.height, 1073741824);
            } else {
               var11 = MeasureSpec.makeMeasureSpec(0, 0);
            }

            var14.measure(var1, var11);
            var14.forceLayout();
            var11 = var3;
            if (var10 > 0) {
               var11 = var3 + var6;
            }

            var3 = var11 + var14.getMeasuredHeight();
            if (var3 >= var4) {
               var1 = var4;
               if (var5 >= 0) {
                  var1 = var4;
                  if (var10 > var5) {
                     var1 = var4;
                     if (var2 > 0) {
                        var1 = var4;
                        if (var3 != var4) {
                           var1 = var2;
                        }
                     }
                  }
               }

               return var1;
            }

            var12 = var2;
            if (var5 >= 0) {
               var12 = var2;
               if (var10 >= var5) {
                  var12 = var3;
               }
            }

            ++var10;
            var11 = var13;
            var16 = var14;
         }

         return var3;
      }
   }

   protected void onDetachedFromWindow() {
      this.mResolveHoverRunnable = null;
      super.onDetachedFromWindow();
   }

   public boolean onForwardedEvent(MotionEvent var1, int var2) {
      boolean var4;
      boolean var8;
      label49: {
         label48: {
            label47: {
               int var3 = var1.getActionMasked();
               switch(var3) {
               case 1:
                  var4 = false;
                  break;
               case 2:
                  var4 = true;
                  break;
               case 3:
                  break label47;
               default:
                  break label48;
               }

               int var5 = var1.findPointerIndex(var2);
               if (var5 >= 0) {
                  var2 = (int)var1.getX(var5);
                  var5 = (int)var1.getY(var5);
                  int var6 = this.pointToPosition(var2, var5);
                  if (var6 == -1) {
                     var8 = true;
                     break label49;
                  }

                  View var7 = this.getChildAt(var6 - this.getFirstVisiblePosition());
                  this.setPressedItem(var7, var6, (float)var2, (float)var5);
                  if (var3 == 1) {
                     this.clickPressedItem(var7, var6);
                  }
                  break label48;
               }
            }

            var8 = false;
            var4 = false;
            break label49;
         }

         var8 = false;
         var4 = true;
      }

      if (!var4 || var8) {
         this.clearPressedItem();
      }

      if (var4) {
         if (this.mScrollHelper == null) {
            this.mScrollHelper = new ListViewAutoScrollHelper(this);
         }

         this.mScrollHelper.setEnabled(true);
         this.mScrollHelper.onTouch(this, var1);
      } else if (this.mScrollHelper != null) {
         this.mScrollHelper.setEnabled(false);
      }

      return var4;
   }

   public boolean onHoverEvent(MotionEvent var1) {
      if (VERSION.SDK_INT < 26) {
         return super.onHoverEvent(var1);
      } else {
         int var2 = var1.getActionMasked();
         if (var2 == 10 && this.mResolveHoverRunnable == null) {
            this.mResolveHoverRunnable = new DropDownListView.ResolveHoverRunnable();
            this.mResolveHoverRunnable.post();
         }

         boolean var3 = super.onHoverEvent(var1);
         if (var2 != 9 && var2 != 7) {
            this.setSelection(-1);
         } else {
            var2 = this.pointToPosition((int)var1.getX(), (int)var1.getY());
            if (var2 != -1 && var2 != this.getSelectedItemPosition()) {
               View var4 = this.getChildAt(var2 - this.getFirstVisiblePosition());
               if (var4.isEnabled()) {
                  this.setSelectionFromTop(var2, var4.getTop() - this.getTop());
               }

               this.updateSelectorStateCompat();
            }
         }

         return var3;
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getAction() == 0) {
         this.mMotionPosition = this.pointToPosition((int)var1.getX(), (int)var1.getY());
      }

      if (this.mResolveHoverRunnable != null) {
         this.mResolveHoverRunnable.cancel();
      }

      return super.onTouchEvent(var1);
   }

   void setListSelectionHidden(boolean var1) {
      this.mListSelectionHidden = var1;
   }

   public void setSelector(Drawable var1) {
      DropDownListView.GateKeeperDrawable var2;
      if (var1 != null) {
         var2 = new DropDownListView.GateKeeperDrawable(var1);
      } else {
         var2 = null;
      }

      this.mSelector = var2;
      super.setSelector(this.mSelector);
      Rect var3 = new Rect();
      if (var1 != null) {
         var1.getPadding(var3);
      }

      this.mSelectionLeftPadding = var3.left;
      this.mSelectionTopPadding = var3.top;
      this.mSelectionRightPadding = var3.right;
      this.mSelectionBottomPadding = var3.bottom;
   }

   private static class GateKeeperDrawable extends DrawableWrapper {
      private boolean mEnabled = true;

      GateKeeperDrawable(Drawable var1) {
         super(var1);
      }

      public void draw(Canvas var1) {
         if (this.mEnabled) {
            super.draw(var1);
         }

      }

      void setEnabled(boolean var1) {
         this.mEnabled = var1;
      }

      public void setHotspot(float var1, float var2) {
         if (this.mEnabled) {
            super.setHotspot(var1, var2);
         }

      }

      public void setHotspotBounds(int var1, int var2, int var3, int var4) {
         if (this.mEnabled) {
            super.setHotspotBounds(var1, var2, var3, var4);
         }

      }

      public boolean setState(int[] var1) {
         return this.mEnabled ? super.setState(var1) : false;
      }

      public boolean setVisible(boolean var1, boolean var2) {
         return this.mEnabled ? super.setVisible(var1, var2) : false;
      }
   }

   private class ResolveHoverRunnable implements Runnable {
      ResolveHoverRunnable() {
      }

      public void cancel() {
         DropDownListView.this.mResolveHoverRunnable = null;
         DropDownListView.this.removeCallbacks(this);
      }

      public void post() {
         DropDownListView.this.post(this);
      }

      public void run() {
         DropDownListView.this.mResolveHoverRunnable = null;
         DropDownListView.this.drawableStateChanged();
      }
   }
}
