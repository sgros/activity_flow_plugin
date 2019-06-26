package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
   private MenuPresenter.Callback mActionMenuPresenterCallback;
   private boolean mFormatItems;
   private int mFormatItemsWidth;
   private int mGeneratedItemPadding;
   private MenuBuilder mMenu;
   MenuBuilder.Callback mMenuBuilderCallback;
   private int mMinCellSize;
   ActionMenuView.OnMenuItemClickListener mOnMenuItemClickListener;
   private Context mPopupContext;
   private int mPopupTheme;
   private ActionMenuPresenter mPresenter;
   private boolean mReserveOverflow;

   public ActionMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setBaselineAligned(false);
      float var3 = var1.getResources().getDisplayMetrics().density;
      this.mMinCellSize = (int)(56.0F * var3);
      this.mGeneratedItemPadding = (int)(var3 * 4.0F);
      this.mPopupContext = var1;
      this.mPopupTheme = 0;
   }

   static int measureChildForCells(View var0, int var1, int var2, int var3, int var4) {
      ActionMenuView.LayoutParams var5 = (ActionMenuView.LayoutParams)var0.getLayoutParams();
      int var6 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var3) - var4, MeasureSpec.getMode(var3));
      ActionMenuItemView var7;
      if (var0 instanceof ActionMenuItemView) {
         var7 = (ActionMenuItemView)var0;
      } else {
         var7 = null;
      }

      boolean var8 = true;
      boolean var11;
      if (var7 != null && var7.hasText()) {
         var11 = true;
      } else {
         var11 = false;
      }

      byte var12 = 2;
      if (var2 > 0 && (!var11 || var2 >= 2)) {
         var0.measure(MeasureSpec.makeMeasureSpec(var2 * var1, Integer.MIN_VALUE), var6);
         int var9 = var0.getMeasuredWidth();
         int var10 = var9 / var1;
         var2 = var10;
         if (var9 % var1 != 0) {
            var2 = var10 + 1;
         }

         if (var11 && var2 < 2) {
            var2 = var12;
         }
      } else {
         var2 = 0;
      }

      if (var5.isOverflowButton || !var11) {
         var8 = false;
      }

      var5.expandable = var8;
      var5.cellsUsed = var2;
      var0.measure(MeasureSpec.makeMeasureSpec(var1 * var2, 1073741824), var6);
      return var2;
   }

   private void onMeasureExactFormat(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var2);
      int var4 = MeasureSpec.getSize(var1);
      int var5 = MeasureSpec.getSize(var2);
      var1 = this.getPaddingLeft();
      int var6 = this.getPaddingRight();
      int var7 = this.getPaddingTop() + this.getPaddingBottom();
      int var8 = ViewGroup.getChildMeasureSpec(var2, var7, -2);
      int var9 = var4 - (var1 + var6);
      var2 = this.mMinCellSize;
      var1 = var9 / var2;
      if (var1 == 0) {
         this.setMeasuredDimension(var9, 0);
      } else {
         int var10 = var2 + var9 % var2 / var1;
         int var11 = this.getChildCount();
         int var12 = 0;
         var2 = 0;
         boolean var35 = false;
         int var13 = 0;
         int var14 = 0;
         var4 = 0;

         long var15;
         View var17;
         int var19;
         ActionMenuView.LayoutParams var20;
         int var21;
         for(var15 = 0L; var12 < var11; ++var12) {
            var17 = this.getChildAt(var12);
            if (var17.getVisibility() != 8) {
               boolean var18 = var17 instanceof ActionMenuItemView;
               ++var13;
               if (var18) {
                  var19 = this.mGeneratedItemPadding;
                  var17.setPadding(var19, 0, var19, 0);
               }

               var20 = (ActionMenuView.LayoutParams)var17.getLayoutParams();
               var20.expanded = false;
               var20.extraPixels = 0;
               var20.cellsUsed = 0;
               var20.expandable = false;
               var20.leftMargin = 0;
               var20.rightMargin = 0;
               if (var18 && ((ActionMenuItemView)var17).hasText()) {
                  var18 = true;
               } else {
                  var18 = false;
               }

               var20.preventEdgeOffset = var18;
               if (var20.isOverflowButton) {
                  var19 = 1;
               } else {
                  var19 = var1;
               }

               var21 = measureChildForCells(var17, var10, var19, var8, var7);
               var14 = Math.max(var14, var21);
               var19 = var4;
               if (var20.expandable) {
                  var19 = var4 + 1;
               }

               if (var20.isOverflowButton) {
                  var35 = true;
               }

               var1 -= var21;
               var2 = Math.max(var2, var17.getMeasuredHeight());
               if (var21 == 1) {
                  var15 |= (long)(1 << var12);
               }

               var4 = var19;
            }
         }

         boolean var36;
         if (var35 && var13 == 2) {
            var36 = true;
         } else {
            var36 = false;
         }

         boolean var39 = false;
         var21 = var1;

         boolean var34;
         ActionMenuView.LayoutParams var37;
         View var38;
         for(var34 = var39; var4 > 0 && var21 > 0; var34 = true) {
            int var22 = Integer.MAX_VALUE;
            var9 = 0;
            int var23 = 0;

            long var24;
            long var27;
            for(var24 = 0L; var9 < var11; var24 = var27) {
               var20 = (ActionMenuView.LayoutParams)this.getChildAt(var9).getLayoutParams();
               int var26;
               if (!var20.expandable) {
                  var26 = var22;
                  var19 = var23;
                  var27 = var24;
               } else {
                  int var29 = var20.cellsUsed;
                  if (var29 < var22) {
                     var26 = var29;
                     var27 = 1L << var9;
                     var19 = 1;
                  } else {
                     var26 = var22;
                     var19 = var23;
                     var27 = var24;
                     if (var29 == var22) {
                        var27 = var24 | 1L << var9;
                        var19 = var23 + 1;
                        var26 = var22;
                     }
                  }
               }

               ++var9;
               var22 = var26;
               var23 = var19;
            }

            var1 = var2;
            var15 |= var24;
            if (var23 > var21) {
               var2 = var2;
               var34 = var34;
               break;
            }

            var2 = var22 + 1;

            for(var19 = 0; var19 < var11; ++var19) {
               var38 = this.getChildAt(var19);
               var37 = (ActionMenuView.LayoutParams)var38.getLayoutParams();
               long var30 = (long)(1 << var19);
               if ((var24 & var30) == 0L) {
                  var27 = var15;
                  if (var37.cellsUsed == var2) {
                     var27 = var15 | var30;
                  }

                  var15 = var27;
               } else {
                  if (var36 && var37.preventEdgeOffset && var21 == 1) {
                     var9 = this.mGeneratedItemPadding;
                     var38.setPadding(var9 + var10, 0, var9, 0);
                  }

                  ++var37.cellsUsed;
                  var37.expanded = true;
                  --var21;
               }
            }

            var2 = var1;
         }

         if (!var35 && var13 == 1) {
            var35 = true;
         } else {
            var35 = false;
         }

         if (var21 <= 0 || var15 == 0L || var21 >= var13 - 1 && !var35 && var14 <= 1) {
            var39 = var34;
         } else {
            float var32 = (float)Long.bitCount(var15);
            if (!var35) {
               float var33;
               if ((var15 & 1L) != 0L) {
                  var33 = var32;
                  if (!((ActionMenuView.LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                     var33 = var32 - 0.5F;
                  }
               } else {
                  var33 = var32;
               }

               var6 = var11 - 1;
               var32 = var33;
               if ((var15 & (long)(1 << var6)) != 0L) {
                  var32 = var33;
                  if (!((ActionMenuView.LayoutParams)this.getChildAt(var6).getLayoutParams()).preventEdgeOffset) {
                     var32 = var33 - 0.5F;
                  }
               }
            }

            if (var32 > 0.0F) {
               var6 = (int)((float)(var21 * var10) / var32);
            } else {
               var6 = 0;
            }

            var4 = 0;

            while(true) {
               var39 = var34;
               if (var4 >= var11) {
                  break;
               }

               if ((var15 & (long)(1 << var4)) == 0L) {
                  var39 = var34;
               } else {
                  label237: {
                     var17 = this.getChildAt(var4);
                     var20 = (ActionMenuView.LayoutParams)var17.getLayoutParams();
                     if (var17 instanceof ActionMenuItemView) {
                        var20.extraPixels = var6;
                        var20.expanded = true;
                        if (var4 == 0 && !var20.preventEdgeOffset) {
                           var20.leftMargin = -var6 / 2;
                        }
                     } else {
                        if (!var20.isOverflowButton) {
                           if (var4 != 0) {
                              var20.leftMargin = var6 / 2;
                           }

                           var39 = var34;
                           if (var4 != var11 - 1) {
                              var20.rightMargin = var6 / 2;
                              var39 = var34;
                           }
                           break label237;
                        }

                        var20.extraPixels = var6;
                        var20.expanded = true;
                        var20.rightMargin = -var6 / 2;
                     }

                     var39 = true;
                  }
               }

               ++var4;
               var34 = var39;
            }
         }

         var1 = 0;
         if (var39) {
            for(; var1 < var11; ++var1) {
               var38 = this.getChildAt(var1);
               var37 = (ActionMenuView.LayoutParams)var38.getLayoutParams();
               if (var37.expanded) {
                  var38.measure(MeasureSpec.makeMeasureSpec(var37.cellsUsed * var10 + var37.extraPixels, 1073741824), var8);
               }
            }
         }

         if (var3 != 1073741824) {
            var1 = var2;
         } else {
            var1 = var5;
         }

         this.setMeasuredDimension(var9, var1);
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 != null && var1 instanceof ActionMenuView.LayoutParams) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void dismissPopupMenus() {
      ActionMenuPresenter var1 = this.mPresenter;
      if (var1 != null) {
         var1.dismissPopupMenus();
      }

   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      return false;
   }

   protected ActionMenuView.LayoutParams generateDefaultLayoutParams() {
      ActionMenuView.LayoutParams var1 = new ActionMenuView.LayoutParams(-2, -2);
      var1.gravity = 16;
      return var1;
   }

   public ActionMenuView.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ActionMenuView.LayoutParams(this.getContext(), var1);
   }

   protected ActionMenuView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 != null) {
         ActionMenuView.LayoutParams var2;
         if (var1 instanceof ActionMenuView.LayoutParams) {
            var2 = new ActionMenuView.LayoutParams((ActionMenuView.LayoutParams)var1);
         } else {
            var2 = new ActionMenuView.LayoutParams(var1);
         }

         if (var2.gravity <= 0) {
            var2.gravity = 16;
         }

         return var2;
      } else {
         return this.generateDefaultLayoutParams();
      }
   }

   public ActionMenuView.LayoutParams generateOverflowButtonLayoutParams() {
      ActionMenuView.LayoutParams var1 = this.generateDefaultLayoutParams();
      var1.isOverflowButton = true;
      return var1;
   }

   public Menu getMenu() {
      if (this.mMenu == null) {
         Context var1 = this.getContext();
         this.mMenu = new MenuBuilder(var1);
         this.mMenu.setCallback(new ActionMenuView.MenuBuilderCallback());
         this.mPresenter = new ActionMenuPresenter(var1);
         this.mPresenter.setReserveOverflow(true);
         ActionMenuPresenter var2 = this.mPresenter;
         Object var3 = this.mActionMenuPresenterCallback;
         if (var3 == null) {
            var3 = new ActionMenuView.ActionMenuPresenterCallback();
         }

         var2.setCallback((MenuPresenter.Callback)var3);
         this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
         this.mPresenter.setMenuView(this);
      }

      return this.mMenu;
   }

   public Drawable getOverflowIcon() {
      this.getMenu();
      return this.mPresenter.getOverflowIcon();
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   public int getWindowAnimations() {
      return 0;
   }

   protected boolean hasSupportDividerBeforeChildAt(int var1) {
      boolean var2 = false;
      if (var1 == 0) {
         return false;
      } else {
         View var3 = this.getChildAt(var1 - 1);
         View var4 = this.getChildAt(var1);
         boolean var5 = var2;
         if (var1 < this.getChildCount()) {
            var5 = var2;
            if (var3 instanceof ActionMenuView.ActionMenuChildView) {
               var5 = false | ((ActionMenuView.ActionMenuChildView)var3).needsDividerAfter();
            }
         }

         var2 = var5;
         if (var1 > 0) {
            var2 = var5;
            if (var4 instanceof ActionMenuView.ActionMenuChildView) {
               var2 = var5 | ((ActionMenuView.ActionMenuChildView)var4).needsDividerBefore();
            }
         }

         return var2;
      }
   }

   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuPresenter var1 = this.mPresenter;
      boolean var2;
      if (var1 != null && var1.isOverflowMenuShowing()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      ActionMenuPresenter var2 = this.mPresenter;
      if (var2 != null) {
         var2.updateMenuView(false);
         if (this.mPresenter.isOverflowMenuShowing()) {
            this.mPresenter.hideOverflowMenu();
            this.mPresenter.showOverflowMenu();
         }
      }

   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.dismissPopupMenus();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (!this.mFormatItems) {
         super.onLayout(var1, var2, var3, var4, var5);
      } else {
         int var6 = this.getChildCount();
         int var7 = (var5 - var3) / 2;
         int var8 = this.getDividerWidth();
         int var9 = var4 - var2;
         var2 = this.getPaddingRight();
         var3 = this.getPaddingLeft();
         var1 = ViewUtils.isLayoutRtl(this);
         var2 = var9 - var2 - var3;
         var4 = 0;
         byte var18 = 0;

         View var10;
         ActionMenuView.LayoutParams var11;
         int var12;
         int var14;
         for(var3 = 0; var4 < var6; ++var4) {
            var10 = this.getChildAt(var4);
            if (var10.getVisibility() != 8) {
               var11 = (ActionMenuView.LayoutParams)var10.getLayoutParams();
               if (var11.isOverflowButton) {
                  var12 = var10.getMeasuredWidth();
                  var5 = var12;
                  if (this.hasSupportDividerBeforeChildAt(var4)) {
                     var5 = var12 + var8;
                  }

                  int var13 = var10.getMeasuredHeight();
                  if (var1) {
                     var14 = this.getPaddingLeft() + var11.leftMargin;
                     var12 = var14 + var5;
                  } else {
                     var12 = this.getWidth() - this.getPaddingRight() - var11.rightMargin;
                     var14 = var12 - var5;
                  }

                  int var15 = var7 - var13 / 2;
                  var10.layout(var14, var15, var12, var13 + var15);
                  var2 -= var5;
                  var18 = 1;
               } else {
                  var2 -= var10.getMeasuredWidth() + var11.leftMargin + var11.rightMargin;
                  this.hasSupportDividerBeforeChildAt(var4);
                  ++var3;
               }
            }
         }

         if (var6 == 1 && var18 == 0) {
            View var19 = this.getChildAt(0);
            var2 = var19.getMeasuredWidth();
            var3 = var19.getMeasuredHeight();
            var4 = var9 / 2 - var2 / 2;
            var5 = var7 - var3 / 2;
            var19.layout(var4, var5, var2 + var4, var3 + var5);
         } else {
            var3 -= var18 ^ 1;
            if (var3 > 0) {
               var2 /= var3;
            } else {
               var2 = 0;
            }

            byte var17 = 0;
            byte var16 = 0;
            var5 = Math.max(0, var2);
            if (var1) {
               var4 = this.getWidth() - this.getPaddingRight();

               for(var2 = var16; var2 < var6; var4 = var3) {
                  var10 = this.getChildAt(var2);
                  var11 = (ActionMenuView.LayoutParams)var10.getLayoutParams();
                  var3 = var4;
                  if (var10.getVisibility() != 8) {
                     if (var11.isOverflowButton) {
                        var3 = var4;
                     } else {
                        var4 -= var11.rightMargin;
                        var3 = var10.getMeasuredWidth();
                        var12 = var10.getMeasuredHeight();
                        var14 = var7 - var12 / 2;
                        var10.layout(var4 - var3, var14, var4, var12 + var14);
                        var3 = var4 - (var3 + var11.leftMargin + var5);
                     }
                  }

                  ++var2;
               }
            } else {
               var3 = this.getPaddingLeft();

               for(var2 = var17; var2 < var6; var3 = var4) {
                  var10 = this.getChildAt(var2);
                  var11 = (ActionMenuView.LayoutParams)var10.getLayoutParams();
                  var4 = var3;
                  if (var10.getVisibility() != 8) {
                     if (var11.isOverflowButton) {
                        var4 = var3;
                     } else {
                        var14 = var3 + var11.leftMargin;
                        var12 = var10.getMeasuredWidth();
                        var4 = var10.getMeasuredHeight();
                        var3 = var7 - var4 / 2;
                        var10.layout(var14, var3, var14 + var12, var4 + var3);
                        var4 = var14 + var12 + var11.rightMargin + var5;
                     }
                  }

                  ++var2;
               }
            }

         }
      }
   }

   protected void onMeasure(int var1, int var2) {
      boolean var3 = this.mFormatItems;
      boolean var4;
      if (MeasureSpec.getMode(var1) == 1073741824) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mFormatItems = var4;
      if (var3 != this.mFormatItems) {
         this.mFormatItemsWidth = 0;
      }

      int var5 = MeasureSpec.getSize(var1);
      if (this.mFormatItems) {
         MenuBuilder var6 = this.mMenu;
         if (var6 != null && var5 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = var5;
            var6.onItemsChanged(true);
         }
      }

      int var7 = this.getChildCount();
      if (this.mFormatItems && var7 > 0) {
         this.onMeasureExactFormat(var1, var2);
      } else {
         for(var5 = 0; var5 < var7; ++var5) {
            ActionMenuView.LayoutParams var8 = (ActionMenuView.LayoutParams)this.getChildAt(var5).getLayoutParams();
            var8.rightMargin = 0;
            var8.leftMargin = 0;
         }

         super.onMeasure(var1, var2);
      }

   }

   public MenuBuilder peekMenu() {
      return this.mMenu;
   }

   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mPresenter.setExpandedActionViewsExclusive(var1);
   }

   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
   }

   public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(Drawable var1) {
      this.getMenu();
      this.mPresenter.setOverflowIcon(var1);
   }

   public void setOverflowReserved(boolean var1) {
      this.mReserveOverflow = var1;
   }

   public void setPopupTheme(int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
         } else {
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
         }
      }

   }

   public void setPresenter(ActionMenuPresenter var1) {
      this.mPresenter = var1;
      this.mPresenter.setMenuView(this);
   }

   public boolean showOverflowMenu() {
      ActionMenuPresenter var1 = this.mPresenter;
      boolean var2;
      if (var1 != null && var1.showOverflowMenu()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public interface ActionMenuChildView {
      boolean needsDividerAfter();

      boolean needsDividerBefore();
   }

   private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      ActionMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         return false;
      }
   }

   public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
      @ExportedProperty
      public int cellsUsed;
      @ExportedProperty
      public boolean expandable;
      boolean expanded;
      @ExportedProperty
      public int extraPixels;
      @ExportedProperty
      public boolean isOverflowButton;
      @ExportedProperty
      public boolean preventEdgeOffset;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.isOverflowButton = false;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(ActionMenuView.LayoutParams var1) {
         super(var1);
         this.isOverflowButton = var1.isOverflowButton;
      }
   }

   private class MenuBuilderCallback implements MenuBuilder.Callback {
      MenuBuilderCallback() {
      }

      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         ActionMenuView.OnMenuItemClickListener var4 = ActionMenuView.this.mOnMenuItemClickListener;
         boolean var3;
         if (var4 != null && var4.onMenuItemClick(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         MenuBuilder.Callback var2 = ActionMenuView.this.mMenuBuilderCallback;
         if (var2 != null) {
            var2.onMenuModeChange(var1);
         }

      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }
}
