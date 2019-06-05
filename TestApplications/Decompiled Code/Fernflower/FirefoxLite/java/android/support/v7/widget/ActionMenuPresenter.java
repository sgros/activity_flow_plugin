package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
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
      ArrayList var2;
      int var3;
      if (this.mMenu != null) {
         var2 = this.mMenu.getVisibleItems();
         var3 = var2.size();
      } else {
         var2 = null;
         var3 = 0;
      }

      int var4 = this.mMaxItems;
      int var5 = this.mActionItemWidthLimit;
      int var6 = MeasureSpec.makeMeasureSpec(0, 0);
      ViewGroup var7 = (ViewGroup)this.mMenuView;
      int var8 = 0;
      int var9 = 0;
      boolean var10 = false;

      int var11;
      int var13;
      for(var11 = 0; var8 < var3; var4 = var13) {
         MenuItemImpl var12 = (MenuItemImpl)var2.get(var8);
         if (var12.requiresActionButton()) {
            ++var9;
         } else if (var12.requestsActionButton()) {
            ++var11;
         } else {
            var10 = true;
         }

         var13 = var4;
         if (var1.mExpandedActionViewsExclusive) {
            var13 = var4;
            if (var12.isActionViewExpanded()) {
               var13 = 0;
            }
         }

         ++var8;
      }

      var8 = var4;
      if (var1.mReserveOverflow) {
         label157: {
            if (!var10) {
               var8 = var4;
               if (var11 + var9 <= var4) {
                  break label157;
               }
            }

            var8 = var4 - 1;
         }
      }

      var4 = var8 - var9;
      SparseBooleanArray var22 = var1.mActionButtonGroups;
      var22.clear();
      int var21;
      if (var1.mStrictWidthLimit) {
         var9 = var5 / var1.mMinCellSize;
         var8 = var1.mMinCellSize;
         var11 = var1.mMinCellSize;
         var21 = var5 % var8 / var9 + var11;
      } else {
         var9 = 0;
         var21 = 0;
      }

      var8 = var5;
      var5 = 0;
      var11 = 0;

      for(var13 = var3; var5 < var13; ++var5) {
         MenuItemImpl var14 = (MenuItemImpl)var2.get(var5);
         View var15;
         if (var14.requiresActionButton()) {
            var15 = this.getItemView(var14, this.mScrapActionButtonView, var7);
            if (this.mScrapActionButtonView == null) {
               this.mScrapActionButtonView = var15;
            }

            if (this.mStrictWidthLimit) {
               var9 -= ActionMenuView.measureChildForCells(var15, var21, var9, var6, 0);
            } else {
               var15.measure(var6, var6);
            }

            var3 = var15.getMeasuredWidth();
            var8 -= var3;
            if (var11 == 0) {
               var11 = var3;
            }

            var3 = var14.getGroupId();
            if (var3 != 0) {
               var22.put(var3, true);
            }

            var14.setIsActionButton(true);
         } else if (!var14.requestsActionButton()) {
            var14.setIsActionButton(false);
         } else {
            int var16 = var14.getGroupId();
            boolean var17 = var22.get(var16);
            boolean var18;
            if (var4 <= 0 && !var17 || var8 <= 0 || this.mStrictWidthLimit && var9 <= 0) {
               var18 = false;
            } else {
               var18 = true;
            }

            int var19;
            if (var18) {
               var15 = this.getItemView(var14, this.mScrapActionButtonView, var7);
               if (this.mScrapActionButtonView == null) {
                  this.mScrapActionButtonView = var15;
               }

               if (this.mStrictWidthLimit) {
                  var19 = ActionMenuView.measureChildForCells(var15, var21, var9, var6, 0);
                  var3 = var9 - var19;
                  var9 = var3;
                  if (var19 == 0) {
                     var18 = false;
                     var9 = var3;
                  }
               } else {
                  var15.measure(var6, var6);
               }

               var19 = var15.getMeasuredWidth();
               var8 -= var19;
               var3 = var11;
               if (var11 == 0) {
                  var3 = var19;
               }

               boolean var23;
               if (this.mStrictWidthLimit) {
                  if (var8 >= 0) {
                     var23 = true;
                  } else {
                     var23 = false;
                  }

                  var18 &= var23;
                  var11 = var3;
               } else {
                  if (var8 + var3 > 0) {
                     var23 = true;
                  } else {
                     var23 = false;
                  }

                  var18 &= var23;
                  var11 = var3;
               }
            }

            if (var18 && var16 != 0) {
               var22.put(var16, true);
               var3 = var4;
            } else {
               var3 = var4;
               if (var17) {
                  var22.put(var16, false);
                  var19 = 0;

                  while(true) {
                     var3 = var4;
                     if (var19 >= var5) {
                        break;
                     }

                     MenuItemImpl var20 = (MenuItemImpl)var2.get(var19);
                     var3 = var4;
                     if (var20.getGroupId() == var16) {
                        var3 = var4;
                        if (var20.isActionButton()) {
                           var3 = var4 + 1;
                        }

                        var20.setIsActionButton(false);
                     }

                     ++var19;
                     var4 = var3;
                  }
               }
            }

            var4 = var3;
            if (var18) {
               var4 = var3 - 1;
            }

            var14.setIsActionButton(var18);
         }
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

   public void initForMenu(Context var1, MenuBuilder var2) {
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
      this.mMinCellSize = (int)(var6.getDisplayMetrics().density * 56.0F);
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

         View var5 = this.findViewForItem(var4.getItem());
         if (var5 == null) {
            return false;
         } else {
            this.mOpenSubMenuId = var1.getItem().getItemId();
            int var6 = var1.size();
            int var7 = 0;

            while(true) {
               var2 = var3;
               if (var7 >= var6) {
                  break;
               }

               MenuItem var8 = var1.getItem(var7);
               if (var8.isVisible() && var8.getIcon() != null) {
                  var2 = true;
                  break;
               }

               ++var7;
            }

            this.mActionButtonPopup = new ActionMenuPresenter.ActionButtonSubmenu(this.mContext, var1, var5);
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
      int var5;
      if (var2 != null) {
         ArrayList var4 = this.mMenu.getActionItems();
         var5 = var4.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            ActionProvider var7 = ((MenuItemImpl)var4.get(var6)).getSupportActionProvider();
            if (var7 != null) {
               var7.setSubUiVisibilityListener(this);
            }
         }
      }

      ArrayList var8;
      if (this.mMenu != null) {
         var8 = this.mMenu.getNonActionItems();
      } else {
         var8 = null;
      }

      boolean var11 = var3;
      if (this.mReserveOverflow) {
         var11 = var3;
         if (var8 != null) {
            var5 = var8.size();
            if (var5 == 1) {
               var11 = ((MenuItemImpl)var8.get(0)).isActionViewExpanded() ^ true;
            } else {
               var11 = var3;
               if (var5 > 0) {
                  var11 = true;
               }
            }
         }
      }

      if (var11) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
         }

         ViewGroup var9 = (ViewGroup)this.mOverflowButton.getParent();
         if (var9 != this.mMenuView) {
            if (var9 != null) {
               var9.removeView(this.mOverflowButton);
            }

            ActionMenuView var10 = (ActionMenuView)this.mMenuView;
            var10.addView(this.mOverflowButton, var10.generateOverflowButtonLayoutParams());
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
            var4 = this.getHeight();
            var1 = Math.max(var8, var4) / 2;
            int var9 = this.getPaddingLeft();
            int var10 = this.getPaddingRight();
            var3 = this.getPaddingTop();
            var2 = this.getPaddingBottom();
            var8 = (var8 + (var9 - var10)) / 2;
            var2 = (var4 + (var3 - var2)) / 2;
            DrawableCompat.setHotspotBounds(var7, var8 - var1, var2 - var1, var8 + var1, var2 + var1);
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
