package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopup;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
   private static final String TAG = "ActionMenuPresenter";
   private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
   ActionMenuPresenter.ActionButtonSubmenu mActionButtonPopup;
   private int mActionItemWidthLimit;
   private boolean mExpandedActionViewsExclusive;
   private int mMaxItems;
   private boolean mMaxItemsSet;
   private int mMinCellSize;
   int mOpenSubMenuId;
   ActionMenuPresenter.OverflowMenuButton mOverflowButton;
   ActionMenuPresenter.OverflowPopup mOverflowPopup;
   private Drawable mPendingOverflowIcon;
   private boolean mPendingOverflowIconSet;
   private ActionMenuPresenter.ActionMenuPopupCallback mPopupCallback;
   final ActionMenuPresenter.PopupPresenterCallback mPopupPresenterCallback = new ActionMenuPresenter.PopupPresenterCallback();
   ActionMenuPresenter.OpenOverflowRunnable mPostedOpenRunnable;
   private boolean mReserveOverflow;
   private boolean mReserveOverflowSet;
   private View mScrapActionButtonView;
   private boolean mStrictWidthLimit;
   private int mWidthLimit;
   private boolean mWidthLimitSet;

   public ActionMenuPresenter(Context var1) {
      super(var1, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
   }

   private View findViewForItem(MenuItem var1) {
      ViewGroup var2 = (ViewGroup)this.mMenuView;
      if (var2 == null) {
         return null;
      } else {
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var5 = var2.getChildAt(var4);
            if (var5 instanceof MenuView.ItemView && ((MenuView.ItemView)var5).getItemData() == var1) {
               return var5;
            }
         }

         return null;
      }
   }

   public void bindItemView(MenuItemImpl var1, MenuView.ItemView var2) {
      var2.initialize(var1, 0);
      ActionMenuView var3 = (ActionMenuView)this.mMenuView;
      ActionMenuItemView var4 = (ActionMenuItemView)var2;
      var4.setItemInvoker(var3);
      if (this.mPopupCallback == null) {
         this.mPopupCallback = new ActionMenuPresenter.ActionMenuPopupCallback();
      }

      var4.setPopupCallback(this.mPopupCallback);
   }

   public boolean dismissPopupMenus() {
      return this.hideOverflowMenu() | this.hideSubMenus();
   }

   public boolean filterLeftoverView(ViewGroup var1, int var2) {
      return var1.getChildAt(var2) == this.mOverflowButton ? false : super.filterLeftoverView(var1, var2);
   }

   public boolean flagActionItems() {
      ActionMenuPresenter var1 = this;
      MenuBuilder var2 = this.mMenu;
      byte var3 = 0;
      int var4;
      ArrayList var21;
      if (var2 != null) {
         var21 = this.mMenu.getVisibleItems();
         var4 = var21.size();
      } else {
         var21 = null;
         var4 = 0;
      }

      int var5 = this.mMaxItems;
      int var6 = this.mActionItemWidthLimit;
      int var7 = MeasureSpec.makeMeasureSpec(0, 0);
      ViewGroup var8 = (ViewGroup)this.mMenuView;
      int var9 = 0;
      int var11 = var9;
      int var13 = var9;

      int var10;
      int var12;
      for(var10 = var9; var10 < var4; var5 = var12) {
         MenuItemImpl var14 = (MenuItemImpl)var21.get(var10);
         if (var14.requiresActionButton()) {
            ++var9;
         } else if (var14.requestsActionButton()) {
            ++var11;
         } else {
            var13 = 1;
         }

         var12 = var5;
         if (var1.mExpandedActionViewsExclusive) {
            var12 = var5;
            if (var14.isActionViewExpanded()) {
               var12 = 0;
            }
         }

         ++var10;
      }

      var10 = var5;
      if (var1.mReserveOverflow) {
         label157: {
            if (var13 == 0) {
               var10 = var5;
               if (var11 + var9 <= var5) {
                  break label157;
               }
            }

            var10 = var5 - 1;
         }
      }

      var11 = var10 - var9;
      SparseBooleanArray var26 = var1.mActionButtonGroups;
      var26.clear();
      if (var1.mStrictWidthLimit) {
         var10 = var6 / var1.mMinCellSize;
         var13 = var1.mMinCellSize;
         var5 = var1.mMinCellSize;
         var12 = var6 % var13 / var10 + var5;
      } else {
         var10 = 0;
         var12 = var10;
      }

      var5 = 0;
      var13 = var6;
      int var15 = var5;
      var6 = var4;

      for(byte var23 = var3; var15 < var6; var5 = var9) {
         MenuItemImpl var16 = (MenuItemImpl)var21.get(var15);
         View var17;
         int var22;
         byte var25;
         if (var16.requiresActionButton()) {
            var17 = this.getItemView(var16, this.mScrapActionButtonView, var8);
            if (this.mScrapActionButtonView == null) {
               this.mScrapActionButtonView = var17;
            }

            if (this.mStrictWidthLimit) {
               var10 -= ActionMenuView.measureChildForCells(var17, var12, var10, var7, var23);
            } else {
               var17.measure(var7, var7);
            }

            var22 = var17.getMeasuredWidth();
            var13 -= var22;
            var9 = var5;
            if (var5 == 0) {
               var9 = var22;
            }

            var5 = var16.getGroupId();
            if (var5 != 0) {
               var26.put(var5, true);
            }

            var16.setIsActionButton(true);
            var25 = var23;
         } else if (!var16.requestsActionButton()) {
            var16.setIsActionButton((boolean)var23);
            var9 = var5;
            var25 = var23;
         } else {
            var22 = var16.getGroupId();
            boolean var18 = var26.get(var22);
            boolean var19;
            if (var11 <= 0 && !var18 || var13 <= 0 || this.mStrictWidthLimit && var10 <= 0) {
               var19 = false;
            } else {
               var19 = true;
            }

            if (var19) {
               var17 = this.getItemView(var16, this.mScrapActionButtonView, var8);
               if (this.mScrapActionButtonView == null) {
                  this.mScrapActionButtonView = var17;
               }

               if (this.mStrictWidthLimit) {
                  var9 = ActionMenuView.measureChildForCells(var17, var12, var10, var7, 0);
                  var4 = var10 - var9;
                  var10 = var4;
                  if (var9 == 0) {
                     var19 = false;
                     var10 = var4;
                  }
               } else {
                  var17.measure(var7, var7);
               }

               var9 = var17.getMeasuredWidth();
               var13 -= var9;
               var4 = var5;
               if (var5 == 0) {
                  var4 = var9;
               }

               boolean var24;
               if (this.mStrictWidthLimit) {
                  if (var13 >= 0) {
                     var24 = true;
                  } else {
                     var24 = false;
                  }

                  var19 &= var24;
               } else {
                  if (var13 + var4 > 0) {
                     var24 = true;
                  } else {
                     var24 = false;
                  }

                  var19 &= var24;
               }
            } else {
               var4 = var5;
            }

            if (var19 && var22 != 0) {
               var26.put(var22, true);
               var5 = var11;
            } else {
               var5 = var11;
               if (var18) {
                  var26.put(var22, false);
                  var9 = 0;

                  while(true) {
                     var5 = var11;
                     if (var9 >= var15) {
                        break;
                     }

                     MenuItemImpl var20 = (MenuItemImpl)var21.get(var9);
                     var5 = var11;
                     if (var20.getGroupId() == var22) {
                        var5 = var11;
                        if (var20.isActionButton()) {
                           var5 = var11 + 1;
                        }

                        var20.setIsActionButton(false);
                     }

                     ++var9;
                     var11 = var5;
                  }
               }
            }

            var11 = var5;
            if (var19) {
               var11 = var5 - 1;
            }

            var16.setIsActionButton(var19);
            var25 = 0;
            var9 = var4;
         }

         ++var15;
         var23 = var25;
      }

      return true;
   }

   public View getItemView(MenuItemImpl var1, View var2, ViewGroup var3) {
      View var4 = var1.getActionView();
      if (var4 == null || var1.hasCollapsibleActionView()) {
         var4 = super.getItemView(var1, var2, var3);
      }

      byte var5;
      if (var1.isActionViewExpanded()) {
         var5 = 8;
      } else {
         var5 = 0;
      }

      var4.setVisibility(var5);
      ActionMenuView var6 = (ActionMenuView)var3;
      LayoutParams var7 = var4.getLayoutParams();
      if (!var6.checkLayoutParams(var7)) {
         var4.setLayoutParams(var6.generateLayoutParams(var7));
      }

      return var4;
   }

   public MenuView getMenuView(ViewGroup var1) {
      MenuView var2 = this.mMenuView;
      MenuView var3 = super.getMenuView(var1);
      if (var2 != var3) {
         ((ActionMenuView)var3).setPresenter(this);
      }

      return var3;
   }

   public Drawable getOverflowIcon() {
      if (this.mOverflowButton != null) {
         return this.mOverflowButton.getDrawable();
      } else {
         return this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null;
      }
   }

   public boolean hideOverflowMenu() {
      if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
         ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
         this.mPostedOpenRunnable = null;
         return true;
      } else {
         ActionMenuPresenter.OverflowPopup var1 = this.mOverflowPopup;
         if (var1 != null) {
            var1.dismiss();
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hideSubMenus() {
      if (this.mActionButtonPopup != null) {
         this.mActionButtonPopup.dismiss();
         return true;
      } else {
         return false;
      }
   }

   public void initForMenu(@NonNull Context var1, @Nullable MenuBuilder var2) {
      super.initForMenu(var1, var2);
      Resources var6 = var1.getResources();
      ActionBarPolicy var5 = ActionBarPolicy.get(var1);
      if (!this.mReserveOverflowSet) {
         this.mReserveOverflow = var5.showsOverflowMenuButton();
      }

      if (!this.mWidthLimitSet) {
         this.mWidthLimit = var5.getEmbeddedMenuWidthLimit();
      }

      if (!this.mMaxItemsSet) {
         this.mMaxItems = var5.getMaxActionButtons();
      }

      int var3 = this.mWidthLimit;
      if (this.mReserveOverflow) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
            if (this.mPendingOverflowIconSet) {
               this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
               this.mPendingOverflowIcon = null;
               this.mPendingOverflowIconSet = false;
            }

            int var4 = MeasureSpec.makeMeasureSpec(0, 0);
            this.mOverflowButton.measure(var4, var4);
         }

         var3 -= this.mOverflowButton.getMeasuredWidth();
      } else {
         this.mOverflowButton = null;
      }

      this.mActionItemWidthLimit = var3;
      this.mMinCellSize = (int)(56.0F * var6.getDisplayMetrics().density);
      this.mScrapActionButtonView = null;
   }

   public boolean isOverflowMenuShowPending() {
      boolean var1;
      if (this.mPostedOpenRunnable == null && !this.isOverflowMenuShowing()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isOverflowMenuShowing() {
      boolean var1;
      if (this.mOverflowPopup != null && this.mOverflowPopup.isShowing()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      this.dismissPopupMenus();
      super.onCloseMenu(var1, var2);
   }

   public void onConfigurationChanged(Configuration var1) {
      if (!this.mMaxItemsSet) {
         this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
      }

      if (this.mMenu != null) {
         this.mMenu.onItemsChanged(true);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof ActionMenuPresenter.SavedState) {
         ActionMenuPresenter.SavedState var2 = (ActionMenuPresenter.SavedState)var1;
         if (var2.openSubMenuId > 0) {
            MenuItem var3 = this.mMenu.findItem(var2.openSubMenuId);
            if (var3 != null) {
               this.onSubMenuSelected((SubMenuBuilder)var3.getSubMenu());
            }
         }

      }
   }

   public Parcelable onSaveInstanceState() {
      ActionMenuPresenter.SavedState var1 = new ActionMenuPresenter.SavedState();
      var1.openSubMenuId = this.mOpenSubMenuId;
      return var1;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      boolean var2 = var1.hasVisibleItems();
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         SubMenuBuilder var4;
         for(var4 = var1; var4.getParentMenu() != this.mMenu; var4 = (SubMenuBuilder)var4.getParentMenu()) {
         }

         View var8 = this.findViewForItem(var4.getItem());
         if (var8 == null) {
            return false;
         } else {
            this.mOpenSubMenuId = var1.getItem().getItemId();
            int var5 = var1.size();
            int var6 = 0;

            while(true) {
               var2 = var3;
               if (var6 >= var5) {
                  break;
               }

               MenuItem var7 = var1.getItem(var6);
               if (var7.isVisible() && var7.getIcon() != null) {
                  var2 = true;
                  break;
               }

               ++var6;
            }

            this.mActionButtonPopup = new ActionMenuPresenter.ActionButtonSubmenu(this.mContext, var1, var8);
            this.mActionButtonPopup.setForceShowIcon(var2);
            this.mActionButtonPopup.show();
            super.onSubMenuSelected(var1);
            return true;
         }
      }
   }

   public void onSubUiVisibilityChanged(boolean var1) {
      if (var1) {
         super.onSubMenuSelected((SubMenuBuilder)null);
      } else if (this.mMenu != null) {
         this.mMenu.close(false);
      }

   }

   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mExpandedActionViewsExclusive = var1;
   }

   public void setItemLimit(int var1) {
      this.mMaxItems = var1;
      this.mMaxItemsSet = true;
   }

   public void setMenuView(ActionMenuView var1) {
      this.mMenuView = var1;
      var1.initialize(this.mMenu);
   }

   public void setOverflowIcon(Drawable var1) {
      if (this.mOverflowButton != null) {
         this.mOverflowButton.setImageDrawable(var1);
      } else {
         this.mPendingOverflowIconSet = true;
         this.mPendingOverflowIcon = var1;
      }

   }

   public void setReserveOverflow(boolean var1) {
      this.mReserveOverflow = var1;
      this.mReserveOverflowSet = true;
   }

   public void setWidthLimit(int var1, boolean var2) {
      this.mWidthLimit = var1;
      this.mStrictWidthLimit = var2;
      this.mWidthLimitSet = true;
   }

   public boolean shouldIncludeItem(int var1, MenuItemImpl var2) {
      return var2.isActionButton();
   }

   public boolean showOverflowMenu() {
      if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
         this.mPostedOpenRunnable = new ActionMenuPresenter.OpenOverflowRunnable(new ActionMenuPresenter.OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true));
         ((View)this.mMenuView).post(this.mPostedOpenRunnable);
         super.onSubMenuSelected((SubMenuBuilder)null);
         return true;
      } else {
         return false;
      }
   }

   public void updateMenuView(boolean var1) {
      super.updateMenuView(var1);
      ((View)this.mMenuView).requestLayout();
      MenuBuilder var2 = this.mMenu;
      boolean var3 = false;
      int var4;
      ArrayList var7;
      if (var2 != null) {
         var7 = this.mMenu.getActionItems();
         var4 = var7.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            ActionProvider var6 = ((MenuItemImpl)var7.get(var5)).getSupportActionProvider();
            if (var6 != null) {
               var6.setSubUiVisibilityListener(this);
            }
         }
      }

      if (this.mMenu != null) {
         var7 = this.mMenu.getNonActionItems();
      } else {
         var7 = null;
      }

      boolean var10 = var3;
      if (this.mReserveOverflow) {
         var10 = var3;
         if (var7 != null) {
            var4 = var7.size();
            if (var4 == 1) {
               var10 = ((MenuItemImpl)var7.get(0)).isActionViewExpanded() ^ true;
            } else {
               var10 = var3;
               if (var4 > 0) {
                  var10 = true;
               }
            }
         }
      }

      if (var10) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
         }

         ViewGroup var8 = (ViewGroup)this.mOverflowButton.getParent();
         if (var8 != this.mMenuView) {
            if (var8 != null) {
               var8.removeView(this.mOverflowButton);
            }

            ActionMenuView var9 = (ActionMenuView)this.mMenuView;
            var9.addView(this.mOverflowButton, var9.generateOverflowButtonLayoutParams());
         }
      } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
         ((ViewGroup)this.mMenuView).removeView(this.mOverflowButton);
      }

      ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
   }

   private class ActionButtonSubmenu extends MenuPopupHelper {
      public ActionButtonSubmenu(Context var2, SubMenuBuilder var3, View var4) {
         super(var2, var3, var4, false, R.attr.actionOverflowMenuStyle);
         if (!((MenuItemImpl)var3.getItem()).isActionButton()) {
            Object var5;
            if (ActionMenuPresenter.this.mOverflowButton == null) {
               var5 = (View)ActionMenuPresenter.this.mMenuView;
            } else {
               var5 = ActionMenuPresenter.this.mOverflowButton;
            }

            this.setAnchorView((View)var5);
         }

         this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      protected void onDismiss() {
         ActionMenuPresenter.this.mActionButtonPopup = null;
         ActionMenuPresenter.this.mOpenSubMenuId = 0;
         super.onDismiss();
      }
   }

   private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
      ActionMenuPopupCallback() {
      }

      public ShowableListMenu getPopup() {
         MenuPopup var1;
         if (ActionMenuPresenter.this.mActionButtonPopup != null) {
            var1 = ActionMenuPresenter.this.mActionButtonPopup.getPopup();
         } else {
            var1 = null;
         }

         return var1;
      }
   }

   private class OpenOverflowRunnable implements Runnable {
      private ActionMenuPresenter.OverflowPopup mPopup;

      public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup var2) {
         this.mPopup = var2;
      }

      public void run() {
         if (ActionMenuPresenter.this.mMenu != null) {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
         }

         View var1 = (View)ActionMenuPresenter.this.mMenuView;
         if (var1 != null && var1.getWindowToken() != null && this.mPopup.tryShow()) {
            ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
         }

         ActionMenuPresenter.this.mPostedOpenRunnable = null;
      }
   }

   private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
      private final float[] mTempPts = new float[2];

      public OverflowMenuButton(Context var2) {
         super(var2, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
         this.setClickable(true);
         this.setFocusable(true);
         this.setVisibility(0);
         this.setEnabled(true);
         TooltipCompat.setTooltipText(this, this.getContentDescription());
         this.setOnTouchListener(new ForwardingListener(this) {
            public ShowableListMenu getPopup() {
               return ActionMenuPresenter.this.mOverflowPopup == null ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup();
            }

            public boolean onForwardingStarted() {
               ActionMenuPresenter.this.showOverflowMenu();
               return true;
            }

            public boolean onForwardingStopped() {
               if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                  return false;
               } else {
                  ActionMenuPresenter.this.hideOverflowMenu();
                  return true;
               }
            }
         });
      }

      public boolean needsDividerAfter() {
         return false;
      }

      public boolean needsDividerBefore() {
         return false;
      }

      public boolean performClick() {
         if (super.performClick()) {
            return true;
         } else {
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
         }
      }

      protected boolean setFrame(int var1, int var2, int var3, int var4) {
         boolean var5 = super.setFrame(var1, var2, var3, var4);
         Drawable var6 = this.getDrawable();
         Drawable var7 = this.getBackground();
         if (var6 != null && var7 != null) {
            int var8 = this.getWidth();
            var2 = this.getHeight();
            var1 = Math.max(var8, var2) / 2;
            int var9 = this.getPaddingLeft();
            int var10 = this.getPaddingRight();
            var3 = this.getPaddingTop();
            var4 = this.getPaddingBottom();
            var10 = (var8 + (var9 - var10)) / 2;
            var2 = (var2 + (var3 - var4)) / 2;
            DrawableCompat.setHotspotBounds(var7, var10 - var1, var2 - var1, var10 + var1, var2 + var1);
         }

         return var5;
      }
   }

   private class OverflowPopup extends MenuPopupHelper {
      public OverflowPopup(Context var2, MenuBuilder var3, View var4, boolean var5) {
         super(var2, var3, var4, var5, R.attr.actionOverflowMenuStyle);
         this.setGravity(8388613);
         this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      protected void onDismiss() {
         if (ActionMenuPresenter.this.mMenu != null) {
            ActionMenuPresenter.this.mMenu.close();
         }

         ActionMenuPresenter.this.mOverflowPopup = null;
         super.onDismiss();
      }
   }

   private class PopupPresenterCallback implements MenuPresenter.Callback {
      PopupPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         if (var1 instanceof SubMenuBuilder) {
            var1.getRootMenu().close(false);
         }

         MenuPresenter.Callback var3 = ActionMenuPresenter.this.getCallback();
         if (var3 != null) {
            var3.onCloseMenu(var1, var2);
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         boolean var2 = false;
         if (var1 == null) {
            return false;
         } else {
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)var1).getItem().getItemId();
            MenuPresenter.Callback var3 = ActionMenuPresenter.this.getCallback();
            if (var3 != null) {
               var2 = var3.onOpenSubMenu(var1);
            }

            return var2;
         }
      }
   }

   private static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public ActionMenuPresenter.SavedState createFromParcel(Parcel var1) {
            return new ActionMenuPresenter.SavedState(var1);
         }

         public ActionMenuPresenter.SavedState[] newArray(int var1) {
            return new ActionMenuPresenter.SavedState[var1];
         }
      };
      public int openSubMenuId;

      SavedState() {
      }

      SavedState(Parcel var1) {
         this.openSubMenuId = var1.readInt();
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.openSubMenuId);
      }
   }
}
