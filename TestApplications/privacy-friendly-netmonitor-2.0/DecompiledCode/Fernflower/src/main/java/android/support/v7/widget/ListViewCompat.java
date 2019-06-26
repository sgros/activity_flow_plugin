package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
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

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ListViewCompat extends ListView {
   public static final int INVALID_POSITION = -1;
   public static final int NO_POSITION = -1;
   private static final int[] STATE_SET_NOTHING = new int[]{0};
   private Field mIsChildViewEnabled;
   protected int mMotionPosition;
   int mSelectionBottomPadding;
   int mSelectionLeftPadding;
   int mSelectionRightPadding;
   int mSelectionTopPadding;
   private ListViewCompat.GateKeeperDrawable mSelector;
   final Rect mSelectorRect;

   public ListViewCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ListViewCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ListViewCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSelectorRect = new Rect();
      this.mSelectionLeftPadding = 0;
      this.mSelectionTopPadding = 0;
      this.mSelectionRightPadding = 0;
      this.mSelectionBottomPadding = 0;

      try {
         this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
         this.mIsChildViewEnabled.setAccessible(true);
      } catch (NoSuchFieldException var4) {
         var4.printStackTrace();
      }

   }

   protected void dispatchDraw(Canvas var1) {
      this.drawSelectorCompat(var1);
      super.dispatchDraw(var1);
   }

   protected void drawSelectorCompat(Canvas var1) {
      if (!this.mSelectorRect.isEmpty()) {
         Drawable var2 = this.getSelector();
         if (var2 != null) {
            var2.setBounds(this.mSelectorRect);
            var2.draw(var1);
         }
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.setSelectorEnabled(true);
      this.updateSelectorStateCompat();
   }

   public int lookForSelectablePosition(int var1, boolean var2) {
      ListAdapter var3 = this.getAdapter();
      if (var3 != null && !this.isInTouchMode()) {
         int var4 = var3.getCount();
         if (this.getAdapter().areAllItemsEnabled()) {
            return var1 >= 0 && var1 < var4 ? var1 : -1;
         } else {
            int var5;
            if (var2) {
               var1 = Math.max(0, var1);

               while(true) {
                  var5 = var1;
                  if (var1 >= var4) {
                     break;
                  }

                  var5 = var1;
                  if (var3.isEnabled(var1)) {
                     break;
                  }

                  ++var1;
               }
            } else {
               var1 = Math.min(var1, var4 - 1);

               while(true) {
                  var5 = var1;
                  if (var1 < 0) {
                     break;
                  }

                  var5 = var1;
                  if (var3.isEnabled(var1)) {
                     break;
                  }

                  --var1;
               }
            }

            return var5 >= 0 && var5 < var4 ? var5 : -1;
         }
      } else {
         return -1;
      }
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
         int var10 = 0;
         var2 = var10;
         View var16 = null;

         int var13;
         for(int var12 = var10; var10 < var9; var2 = var13) {
            var13 = var8.getItemViewType(var10);
            int var11 = var12;
            if (var13 != var12) {
               var16 = null;
               var11 = var13;
            }

            View var14 = var8.getView(var10, var16, this);
            LayoutParams var15 = var14.getLayoutParams();
            LayoutParams var17 = var15;
            if (var15 == null) {
               var17 = this.generateDefaultLayoutParams();
               var14.setLayoutParams(var17);
            }

            if (var17.height > 0) {
               var12 = MeasureSpec.makeMeasureSpec(var17.height, 1073741824);
            } else {
               var12 = MeasureSpec.makeMeasureSpec(0, 0);
            }

            var14.measure(var1, var12);
            var14.forceLayout();
            var12 = var3;
            if (var10 > 0) {
               var12 = var3 + var6;
            }

            var3 = var12 + var14.getMeasuredHeight();
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

            var13 = var2;
            if (var5 >= 0) {
               var13 = var2;
               if (var10 >= var5) {
                  var13 = var3;
               }
            }

            ++var10;
            var12 = var11;
            var16 = var14;
         }

         return var3;
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getAction() == 0) {
         this.mMotionPosition = this.pointToPosition((int)var1.getX(), (int)var1.getY());
      }

      return super.onTouchEvent(var1);
   }

   protected void positionSelectorCompat(int var1, View var2) {
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

   protected void positionSelectorLikeFocusCompat(int var1, View var2) {
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

   protected void positionSelectorLikeTouchCompat(int var1, View var2, float var3, float var4) {
      this.positionSelectorLikeFocusCompat(var1, var2);
      Drawable var5 = this.getSelector();
      if (var5 != null && var1 != -1) {
         DrawableCompat.setHotspot(var5, var3, var4);
      }

   }

   public void setSelector(Drawable var1) {
      ListViewCompat.GateKeeperDrawable var2;
      if (var1 != null) {
         var2 = new ListViewCompat.GateKeeperDrawable(var1);
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

   protected void setSelectorEnabled(boolean var1) {
      if (this.mSelector != null) {
         this.mSelector.setEnabled(var1);
      }

   }

   protected boolean shouldShowSelectorCompat() {
      boolean var1;
      if (this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected boolean touchModeDrawsInPressedStateCompat() {
      return false;
   }

   protected void updateSelectorStateCompat() {
      Drawable var1 = this.getSelector();
      if (var1 != null && this.shouldShowSelectorCompat()) {
         var1.setState(this.getDrawableState());
      }

   }

   private static class GateKeeperDrawable extends DrawableWrapper {
      private boolean mEnabled = true;

      public GateKeeperDrawable(Drawable var1) {
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
}
