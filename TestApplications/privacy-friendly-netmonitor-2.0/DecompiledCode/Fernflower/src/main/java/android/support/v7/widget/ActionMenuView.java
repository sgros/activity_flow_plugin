package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
   static final int GENERATED_ITEM_PADDING = 4;
   static final int MIN_CELL_SIZE = 56;
   private static final String TAG = "ActionMenuView";
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
      this.mGeneratedItemPadding = (int)(4.0F * var3);
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

      boolean var8 = false;
      boolean var12;
      if (var7 != null && var7.hasText()) {
         var12 = true;
      } else {
         var12 = false;
      }

      byte var13 = 2;
      if (var2 > 0 && (!var12 || var2 >= 2)) {
         var0.measure(MeasureSpec.makeMeasureSpec(var2 * var1, Integer.MIN_VALUE), var6);
         int var9 = var0.getMeasuredWidth();
         int var10 = var9 / var1;
         var2 = var10;
         if (var9 % var1 != 0) {
            var2 = var10 + 1;
         }

         if (var12 && var2 < 2) {
            var2 = var13;
         }
      } else {
         var2 = 0;
      }

      boolean var11 = var8;
      if (!var5.isOverflowButton) {
         var11 = var8;
         if (var12) {
            var11 = true;
         }
      }

      var5.expandable = var11;
      var5.cellsUsed = var2;
      var0.measure(MeasureSpec.makeMeasureSpec(var1 * var2, 1073741824), var6);
      return var2;
   }

   private void onMeasureExactFormat(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var2);
      var1 = MeasureSpec.getSize(var1);
      int var4 = MeasureSpec.getSize(var2);
      int var5 = this.getPaddingLeft();
      int var6 = this.getPaddingRight();
      int var7 = this.getPaddingTop() + this.getPaddingBottom();
      int var8 = getChildMeasureSpec(var2, var7, -2);
      int var9 = var1 - (var5 + var6);
      int var10 = var9 / this.mMinCellSize;
      var1 = this.mMinCellSize;
      if (var10 == 0) {
         this.setMeasuredDimension(var9, 0);
      } else {
         int var11 = this.mMinCellSize + var9 % var1 / var10;
         int var12 = this.getChildCount();
         int var13 = 0;
         int var14 = var13;
         var5 = var13;
         var6 = var13;
         long var15 = 0L;
         int var17 = var13;
         var1 = var10;
         int var18 = var13;

         View var19;
         int var20;
         ActionMenuView.LayoutParams var22;
         long var23;
         for(var2 = var4; var13 < var12; var17 = var20) {
            var19 = this.getChildAt(var13);
            if (var19.getVisibility() == 8) {
               var4 = var18;
               var20 = var17;
            } else {
               boolean var21 = var19 instanceof ActionMenuItemView;
               var4 = var18 + 1;
               if (var21) {
                  var19.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
               }

               var22 = (ActionMenuView.LayoutParams)var19.getLayoutParams();
               var22.expanded = false;
               var22.extraPixels = 0;
               var22.cellsUsed = 0;
               var22.expandable = false;
               var22.leftMargin = 0;
               var22.rightMargin = 0;
               if (var21 && ((ActionMenuItemView)var19).hasText()) {
                  var21 = true;
               } else {
                  var21 = false;
               }

               var22.preventEdgeOffset = var21;
               if (var22.isOverflowButton) {
                  var10 = 1;
               } else {
                  var10 = var1;
               }

               var18 = measureChildForCells(var19, var11, var10, var8, var7);
               var20 = Math.max(var17, var18);
               var10 = var6;
               if (var22.expandable) {
                  var10 = var6 + 1;
               }

               if (var22.isOverflowButton) {
                  var5 = 1;
               }

               var1 -= var18;
               var14 = Math.max(var14, var19.getMeasuredHeight());
               if (var18 == 1) {
                  var23 = (long)(1 << var13);
                  var15 |= var23;
                  var6 = var10;
               } else {
                  var6 = var10;
               }
            }

            ++var13;
            var18 = var4;
         }

         boolean var35;
         if (var5 != 0 && var18 == 2) {
            var35 = true;
         } else {
            var35 = false;
         }

         boolean var37 = false;
         var7 = var1;
         var4 = var12;
         var10 = var8;

         boolean var33;
         ActionMenuView.LayoutParams var36;
         for(var33 = var37; var6 > 0 && var7 > 0; var33 = true) {
            var8 = Integer.MAX_VALUE;
            var12 = 0;
            int var25 = 0;

            long var26;
            for(var26 = 0L; var12 < var4; var26 = var23) {
               var36 = (ActionMenuView.LayoutParams)this.getChildAt(var12).getLayoutParams();
               int var28;
               if (!var36.expandable) {
                  var20 = var25;
                  var28 = var8;
                  var23 = var26;
               } else if (var36.cellsUsed < var8) {
                  var28 = var36.cellsUsed;
                  var23 = (long)(1 << var12);
                  var20 = 1;
               } else {
                  var20 = var25;
                  var28 = var8;
                  var23 = var26;
                  if (var36.cellsUsed == var8) {
                     var23 = (long)(1 << var12);
                     var20 = var25 + 1;
                     var23 |= var26;
                     var28 = var8;
                  }
               }

               ++var12;
               var25 = var20;
               var8 = var28;
            }

            var15 |= var26;
            if (var25 > var7) {
               break;
            }

            for(var20 = 0; var20 < var4; var15 = var23) {
               var19 = this.getChildAt(var20);
               var22 = (ActionMenuView.LayoutParams)var19.getLayoutParams();
               long var29 = (long)(1 << var20);
               if ((var26 & var29) == 0L) {
                  var1 = var7;
                  var23 = var15;
                  if (var22.cellsUsed == var8 + 1) {
                     var23 = var15 | var29;
                     var1 = var7;
                  }
               } else {
                  if (var35 && var22.preventEdgeOffset && var7 == 1) {
                     var19.setPadding(this.mGeneratedItemPadding + var11, 0, this.mGeneratedItemPadding, 0);
                  }

                  ++var22.cellsUsed;
                  var22.expanded = true;
                  var1 = var7 - 1;
                  var23 = var15;
               }

               ++var20;
               var7 = var1;
            }
         }

         boolean var34;
         if (var5 == 0 && var18 == 1) {
            var34 = true;
         } else {
            var34 = false;
         }

         if (var7 > 0 && var15 != 0L && (var7 < var18 - 1 || var34 || var17 > 1)) {
            float var31 = (float)Long.bitCount(var15);
            if (!var34) {
               float var32;
               if ((var15 & 1L) != 0L) {
                  var32 = var31;
                  if (!((ActionMenuView.LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                     var32 = var31 - 0.5F;
                  }
               } else {
                  var32 = var31;
               }

               var5 = var4 - 1;
               var31 = var32;
               if ((var15 & (long)(1 << var5)) != 0L) {
                  var31 = var32;
                  if (!((ActionMenuView.LayoutParams)this.getChildAt(var5).getLayoutParams()).preventEdgeOffset) {
                     var31 = var32 - 0.5F;
                  }
               }
            }

            if (var31 > 0.0F) {
               var6 = (int)((float)(var7 * var11) / var31);
            } else {
               var6 = 0;
            }

            var20 = 0;
            var17 = var4;

            while(true) {
               var34 = var33;
               if (var20 >= var17) {
                  break;
               }

               if ((var15 & (long)(1 << var20)) == 0L) {
                  var34 = var33;
               } else {
                  var19 = this.getChildAt(var20);
                  var22 = (ActionMenuView.LayoutParams)var19.getLayoutParams();
                  if (var19 instanceof ActionMenuItemView) {
                     var22.extraPixels = var6;
                     var22.expanded = true;
                     if (var20 == 0 && !var22.preventEdgeOffset) {
                        var22.leftMargin = -var6 / 2;
                     }

                     var34 = true;
                  } else if (var22.isOverflowButton) {
                     var22.extraPixels = var6;
                     var22.expanded = true;
                     var22.rightMargin = -var6 / 2;
                     var34 = true;
                  } else {
                     if (var20 != 0) {
                        var22.leftMargin = var6 / 2;
                     }

                     var34 = var33;
                     if (var20 != var17 - 1) {
                        var22.rightMargin = var6 / 2;
                        var34 = var33;
                     }
                  }
               }

               ++var20;
               var33 = var34;
            }
         } else {
            var34 = var33;
         }

         var1 = 0;
         if (var34) {
            for(; var1 < var4; ++var1) {
               View var38 = this.getChildAt(var1);
               var36 = (ActionMenuView.LayoutParams)var38.getLayoutParams();
               if (var36.expanded) {
                  var38.measure(MeasureSpec.makeMeasureSpec(var36.cellsUsed * var11 + var36.extraPixels, 1073741824), var10);
               }
            }
         }

         if (var3 != 1073741824) {
            var2 = var14;
         }

         this.setMeasuredDimension(var9, var2);
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
      if (this.mPresenter != null) {
         this.mPresenter.dismissPopupMenus();
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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
         Object var3;
         if (this.mActionMenuPresenterCallback != null) {
            var3 = this.mActionMenuPresenterCallback;
         } else {
            var3 = new ActionMenuView.ActionMenuPresenterCallback();
         }

         var2.setCallback((MenuPresenter.Callback)var3);
         this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
         this.mPresenter.setMenuView(this);
      }

      return this.mMenu;
   }

   @Nullable
   public Drawable getOverflowIcon() {
      this.getMenu();
      return this.mPresenter.getOverflowIcon();
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getWindowAnimations() {
      return 0;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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

   public boolean hideOverflowMenu() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.hideOverflowMenu()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isOverflowMenuShowPending() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isOverflowMenuShowing() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowing()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (this.mPresenter != null) {
         this.mPresenter.updateMenuView(false);
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
         byte var17 = 0;

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
                     var12 = this.getPaddingLeft() + var11.leftMargin;
                     var14 = var12 + var5;
                  } else {
                     var14 = this.getWidth() - this.getPaddingRight() - var11.rightMargin;
                     var12 = var14 - var5;
                  }

                  int var15 = var7 - var13 / 2;
                  var10.layout(var12, var15, var14, var13 + var15);
                  var2 -= var5;
                  var17 = 1;
               } else {
                  var2 -= var10.getMeasuredWidth() + var11.leftMargin + var11.rightMargin;
                  this.hasSupportDividerBeforeChildAt(var4);
                  ++var3;
               }
            }
         }

         View var19;
         if (var6 == 1 && var17 == 0) {
            var19 = this.getChildAt(0);
            var3 = var19.getMeasuredWidth();
            var2 = var19.getMeasuredHeight();
            var4 = var9 / 2 - var3 / 2;
            var5 = var7 - var2 / 2;
            var19.layout(var4, var5, var3 + var4, var2 + var5);
         } else {
            var3 -= var17 ^ 1;
            if (var3 > 0) {
               var2 /= var3;
            } else {
               var2 = 0;
            }

            var17 = 0;
            byte var16 = 0;
            var12 = Math.max(0, var2);
            if (var1) {
               var3 = this.getWidth() - this.getPaddingRight();

               for(var2 = var16; var2 < var6; var3 = var4) {
                  var19 = this.getChildAt(var2);
                  ActionMenuView.LayoutParams var18 = (ActionMenuView.LayoutParams)var19.getLayoutParams();
                  var4 = var3;
                  if (var19.getVisibility() != 8) {
                     if (var18.isOverflowButton) {
                        var4 = var3;
                     } else {
                        var5 = var3 - var18.rightMargin;
                        var4 = var19.getMeasuredWidth();
                        var14 = var19.getMeasuredHeight();
                        var3 = var7 - var14 / 2;
                        var19.layout(var5 - var4, var3, var5, var14 + var3);
                        var4 = var5 - (var4 + var18.leftMargin + var12);
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
                        var5 = var3 + var11.leftMargin;
                        var14 = var10.getMeasuredWidth();
                        var3 = var10.getMeasuredHeight();
                        var4 = var7 - var3 / 2;
                        var10.layout(var5, var4, var5 + var14, var3 + var4);
                        var4 = var5 + var14 + var11.rightMargin + var12;
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
      if (this.mFormatItems && this.mMenu != null && var5 != this.mFormatItemsWidth) {
         this.mFormatItemsWidth = var5;
         this.mMenu.onItemsChanged(true);
      }

      int var6 = this.getChildCount();
      if (this.mFormatItems && var6 > 0) {
         this.onMeasureExactFormat(var1, var2);
      } else {
         for(var5 = 0; var5 < var6; ++var5) {
            ActionMenuView.LayoutParams var7 = (ActionMenuView.LayoutParams)this.getChildAt(var5).getLayoutParams();
            var7.rightMargin = 0;
            var7.leftMargin = 0;
         }

         super.onMeasure(var1, var2);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public MenuBuilder peekMenu() {
      return this.mMenu;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mPresenter.setExpandedActionViewsExclusive(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
   }

   public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(@Nullable Drawable var1) {
      this.getMenu();
      this.mPresenter.setOverflowIcon(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setOverflowReserved(boolean var1) {
      this.mReserveOverflow = var1;
   }

   public void setPopupTheme(@StyleRes int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
         } else {
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setPresenter(ActionMenuPresenter var1) {
      this.mPresenter = var1;
      this.mPresenter.setMenuView(this);
   }

   public boolean showOverflowMenu() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.showOverflowMenu()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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

      LayoutParams(int var1, int var2, boolean var3) {
         super(var1, var2);
         this.isOverflowButton = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(ActionMenuView.LayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.isOverflowButton = var1.isOverflowButton;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }
   }

   private class MenuBuilderCallback implements MenuBuilder.Callback {
      MenuBuilderCallback() {
      }

      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         boolean var3;
         if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if (ActionMenuView.this.mMenuBuilderCallback != null) {
            ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(var1);
         }

      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }
}
