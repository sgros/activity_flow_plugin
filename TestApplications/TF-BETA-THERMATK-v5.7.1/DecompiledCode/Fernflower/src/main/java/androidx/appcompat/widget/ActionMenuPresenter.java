package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$layout;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPopup;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
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
      super(var1, R$layout.abc_action_menu_layout, R$layout.abc_action_menu_item_layout);
   }

   // $FF: synthetic method
   static MenuBuilder access$000(ActionMenuPresenter var0) {
      return var0.mMenu;
   }

   // $FF: synthetic method
   static MenuBuilder access$100(ActionMenuPresenter var0) {
      return var0.mMenu;
   }

   // $FF: synthetic method
   static MenuView access$200(ActionMenuPresenter var0) {
      return var0.mMenuView;
   }

   // $FF: synthetic method
   static MenuBuilder access$300(ActionMenuPresenter var0) {
      return var0.mMenu;
   }

   // $FF: synthetic method
   static MenuBuilder access$400(ActionMenuPresenter var0) {
      return var0.mMenu;
   }

   // $FF: synthetic method
   static MenuView access$500(ActionMenuPresenter var0) {
      return var0.mMenuView;
   }

   private View findViewForItem(MenuItem var1) {
      ViewGroup var2 = (ViewGroup)super.mMenuView;
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
      ActionMenuView var3 = (ActionMenuView)super.mMenuView;
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
      MenuBuilder var2 = super.mMenu;
      int var3;
      ArrayList var21;
      if (var2 != null) {
         var21 = var2.getVisibleItems();
         var3 = var21.size();
      } else {
         var21 = null;
         var3 = 0;
      }

      int var4 = this.mMaxItems;
      int var5 = this.mActionItemWidthLimit;
      int var6 = MeasureSpec.makeMeasureSpec(0, 0);
      ViewGroup var7 = (ViewGroup)super.mMenuView;
      int var8 = 0;
      boolean var9 = false;
      int var10 = 0;

      int var11;
      int var13;
      for(var11 = 0; var8 < var3; var4 = var13) {
         MenuItemImpl var12 = (MenuItemImpl)var21.get(var8);
         if (var12.requiresActionButton()) {
            ++var10;
         } else if (var12.requestsActionButton()) {
            ++var11;
         } else {
            var9 = true;
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
         label159: {
            if (!var9) {
               var8 = var4;
               if (var11 + var10 <= var4) {
                  break label159;
               }
            }

            var8 = var4 - 1;
         }
      }

      var4 = var8 - var10;
      SparseBooleanArray var24 = var1.mActionButtonGroups;
      var24.clear();
      int var22;
      if (var1.mStrictWidthLimit) {
         var10 = var1.mMinCellSize;
         var11 = var5 / var10;
         var22 = var10 + var5 % var10 / var11;
      } else {
         var22 = 0;
         var11 = 0;
      }

      var8 = var5;
      var13 = 0;
      var10 = 0;

      for(var5 = var3; var13 < var5; var8 = var3) {
         MenuItemImpl var14 = (MenuItemImpl)var21.get(var13);
         View var15;
         int var16;
         if (var14.requiresActionButton()) {
            var15 = this.getItemView(var14, this.mScrapActionButtonView, var7);
            if (this.mScrapActionButtonView == null) {
               this.mScrapActionButtonView = var15;
            }

            if (this.mStrictWidthLimit) {
               var11 -= ActionMenuView.measureChildForCells(var15, var22, var11, var6, 0);
            } else {
               var15.measure(var6, var6);
            }

            var16 = var15.getMeasuredWidth();
            var3 = var8 - var16;
            if (var10 == 0) {
               var10 = var16;
            }

            var8 = var14.getGroupId();
            if (var8 != 0) {
               var24.put(var8, true);
            }

            var14.setIsActionButton(true);
         } else if (!var14.requestsActionButton()) {
            var14.setIsActionButton(false);
            var3 = var8;
         } else {
            int var17 = var14.getGroupId();
            boolean var18 = var24.get(var17);
            boolean var19;
            if (var4 <= 0 && !var18 || var8 <= 0 || this.mStrictWidthLimit && var11 <= 0) {
               var19 = false;
            } else {
               var19 = true;
            }

            if (var19) {
               var15 = this.getItemView(var14, this.mScrapActionButtonView, var7);
               if (this.mScrapActionButtonView == null) {
                  this.mScrapActionButtonView = var15;
               }

               if (this.mStrictWidthLimit) {
                  var16 = ActionMenuView.measureChildForCells(var15, var22, var11, var6, 0);
                  var3 = var11 - var16;
                  var11 = var3;
                  if (var16 == 0) {
                     var19 = false;
                     var11 = var3;
                  }
               } else {
                  var15.measure(var6, var6);
               }

               var16 = var15.getMeasuredWidth();
               var3 = var8 - var16;
               var8 = var10;
               if (var10 == 0) {
                  var8 = var16;
               }

               boolean var23;
               label129: {
                  label128: {
                     if (this.mStrictWidthLimit) {
                        if (var3 >= 0) {
                           break label128;
                        }
                     } else if (var3 + var8 > 0) {
                        break label128;
                     }

                     var23 = false;
                     break label129;
                  }

                  var23 = true;
               }

               var19 &= var23;
               var10 = var8;
            } else {
               var3 = var8;
            }

            if (var19 && var17 != 0) {
               var24.put(var17, true);
               var8 = var4;
            } else {
               var8 = var4;
               if (var18) {
                  var24.put(var17, false);
                  var16 = 0;

                  while(true) {
                     var8 = var4;
                     if (var16 >= var13) {
                        break;
                     }

                     MenuItemImpl var20 = (MenuItemImpl)var21.get(var16);
                     var8 = var4;
                     if (var20.getGroupId() == var17) {
                        var8 = var4;
                        if (var20.isActionButton()) {
                           var8 = var4 + 1;
                        }

                        var20.setIsActionButton(false);
                     }

                     ++var16;
                     var4 = var8;
                  }
               }
            }

            var4 = var8;
            if (var19) {
               var4 = var8 - 1;
            }

            var14.setIsActionButton(var19);
         }

         ++var13;
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

   public Drawable getOverflowIcon() {
      ActionMenuPresenter.OverflowMenuButton var1 = this.mOverflowButton;
      if (var1 != null) {
         return var1.getDrawable();
      } else {
         return this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null;
      }
   }

   public boolean hideOverflowMenu() {
      ActionMenuPresenter.OpenOverflowRunnable var1 = this.mPostedOpenRunnable;
      if (var1 != null) {
         MenuView var2 = super.mMenuView;
         if (var2 != null) {
            ((View)var2).removeCallbacks(var1);
            this.mPostedOpenRunnable = null;
            return true;
         }
      }

      ActionMenuPresenter.OverflowPopup var3 = this.mOverflowPopup;
      if (var3 != null) {
         var3.dismiss();
         return true;
      } else {
         return false;
      }
   }

   public boolean hideSubMenus() {
      ActionMenuPresenter.ActionButtonSubmenu var1 = this.mActionButtonPopup;
      if (var1 != null) {
         var1.dismiss();
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
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(super.mSystemContext);
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

   public boolean isOverflowMenuShowing() {
      ActionMenuPresenter.OverflowPopup var1 = this.mOverflowPopup;
      boolean var2;
      if (var1 != null && var1.isShowing()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      this.dismissPopupMenus();
      super.onCloseMenu(var1, var2);
   }

   public void onConfigurationChanged(Configuration var1) {
      if (!this.mMaxItemsSet) {
         this.mMaxItems = ActionBarPolicy.get(super.mContext).getMaxActionButtons();
      }

      MenuBuilder var2 = super.mMenu;
      if (var2 != null) {
         var2.onItemsChanged(true);
      }

   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      boolean var2 = var1.hasVisibleItems();
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         SubMenuBuilder var4;
         for(var4 = var1; var4.getParentMenu() != super.mMenu; var4 = (SubMenuBuilder)var4.getParentMenu()) {
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

            this.mActionButtonPopup = new ActionMenuPresenter.ActionButtonSubmenu(super.mContext, var1, var5);
            this.mActionButtonPopup.setForceShowIcon(var2);
            this.mActionButtonPopup.show();
            super.onSubMenuSelected(var1);
            return true;
         }
      }
   }

   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mExpandedActionViewsExclusive = var1;
   }

   public void setMenuView(ActionMenuView var1) {
      super.mMenuView = var1;
      var1.initialize(super.mMenu);
   }

   public void setOverflowIcon(Drawable var1) {
      ActionMenuPresenter.OverflowMenuButton var2 = this.mOverflowButton;
      if (var2 != null) {
         var2.setImageDrawable(var1);
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
      if (this.mReserveOverflow && !this.isOverflowMenuShowing()) {
         MenuBuilder var1 = super.mMenu;
         if (var1 != null && super.mMenuView != null && this.mPostedOpenRunnable == null && !var1.getNonActionItems().isEmpty()) {
            this.mPostedOpenRunnable = new ActionMenuPresenter.OpenOverflowRunnable(new ActionMenuPresenter.OverflowPopup(super.mContext, super.mMenu, this.mOverflowButton, true));
            ((View)super.mMenuView).post(this.mPostedOpenRunnable);
            super.onSubMenuSelected((SubMenuBuilder)null);
            return true;
         }
      }

      return false;
   }

   public void updateMenuView(boolean var1) {
      super.updateMenuView(var1);
      ((View)super.mMenuView).requestLayout();
      MenuBuilder var2 = super.mMenu;
      boolean var3 = false;
      int var5;
      if (var2 != null) {
         ArrayList var4 = var2.getActionItems();
         var5 = var4.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            ActionProvider var7 = ((MenuItemImpl)var4.get(var6)).getSupportActionProvider();
            if (var7 != null) {
               var7.setSubUiVisibilityListener(this);
            }
         }
      }

      var2 = super.mMenu;
      ArrayList var8;
      if (var2 != null) {
         var8 = var2.getNonActionItems();
      } else {
         var8 = null;
      }

      boolean var14 = var3;
      if (this.mReserveOverflow) {
         var14 = var3;
         if (var8 != null) {
            var5 = var8.size();
            if (var5 == 1) {
               var14 = ((MenuItemImpl)var8.get(0)).isActionViewExpanded() ^ true;
            } else {
               var14 = var3;
               if (var5 > 0) {
                  var14 = true;
               }
            }
         }
      }

      if (var14) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(super.mSystemContext);
         }

         ViewGroup var9 = (ViewGroup)this.mOverflowButton.getParent();
         if (var9 != super.mMenuView) {
            if (var9 != null) {
               var9.removeView(this.mOverflowButton);
            }

            ActionMenuView var11 = (ActionMenuView)super.mMenuView;
            var11.addView(this.mOverflowButton, var11.generateOverflowButtonLayoutParams());
         }
      } else {
         ActionMenuPresenter.OverflowMenuButton var12 = this.mOverflowButton;
         if (var12 != null) {
            ViewParent var13 = var12.getParent();
            MenuView var10 = super.mMenuView;
            if (var13 == var10) {
               ((ViewGroup)var10).removeView(this.mOverflowButton);
            }
         }
      }

      ((ActionMenuView)super.mMenuView).setOverflowReserved(this.mReserveOverflow);
   }

   private class ActionButtonSubmenu extends MenuPopupHelper {
      public ActionButtonSubmenu(Context var2, SubMenuBuilder var3, View var4) {
         super(var2, var3, var4, false, R$attr.actionOverflowMenuStyle);
         if (!((MenuItemImpl)var3.getItem()).isActionButton()) {
            ActionMenuPresenter.OverflowMenuButton var6 = ActionMenuPresenter.this.mOverflowButton;
            Object var5 = var6;
            if (var6 == null) {
               var5 = (View)ActionMenuPresenter.access$200(ActionMenuPresenter.this);
            }

            this.setAnchorView((View)var5);
         }

         this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      protected void onDismiss() {
         ActionMenuPresenter var1 = ActionMenuPresenter.this;
         var1.mActionButtonPopup = null;
         var1.mOpenSubMenuId = 0;
         super.onDismiss();
      }
   }

   private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
      ActionMenuPopupCallback() {
      }

      public ShowableListMenu getPopup() {
         ActionMenuPresenter.ActionButtonSubmenu var1 = ActionMenuPresenter.this.mActionButtonPopup;
         MenuPopup var2;
         if (var1 != null) {
            var2 = var1.getPopup();
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   private class OpenOverflowRunnable implements Runnable {
      private ActionMenuPresenter.OverflowPopup mPopup;

      public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup var2) {
         this.mPopup = var2;
      }

      public void run() {
         if (ActionMenuPresenter.access$300(ActionMenuPresenter.this) != null) {
            ActionMenuPresenter.access$400(ActionMenuPresenter.this).changeMenuMode();
         }

         View var1 = (View)ActionMenuPresenter.access$500(ActionMenuPresenter.this);
         if (var1 != null && var1.getWindowToken() != null && this.mPopup.tryShow()) {
            ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
         }

         ActionMenuPresenter.this.mPostedOpenRunnable = null;
      }
   }

   private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
      private final float[] mTempPts = new float[2];

      public OverflowMenuButton(Context var2) {
         super(var2, (AttributeSet)null, R$attr.actionOverflowButtonStyle);
         this.setClickable(true);
         this.setFocusable(true);
         this.setVisibility(0);
         this.setEnabled(true);
         TooltipCompat.setTooltipText(this, this.getContentDescription());
         this.setOnTouchListener(new ForwardingListener(this) {
            public ShowableListMenu getPopup() {
               ActionMenuPresenter.OverflowPopup var1 = ActionMenuPresenter.this.mOverflowPopup;
               return var1 == null ? null : var1.getPopup();
            }

            public boolean onForwardingStarted() {
               ActionMenuPresenter.this.showOverflowMenu();
               return true;
            }

            public boolean onForwardingStopped() {
               ActionMenuPresenter var1 = ActionMenuPresenter.this;
               if (var1.mPostedOpenRunnable != null) {
                  return false;
               } else {
                  var1.hideOverflowMenu();
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
            var4 = this.getPaddingTop();
            var3 = this.getPaddingBottom();
            var10 = (var8 + (var9 - var10)) / 2;
            var2 = (var2 + (var4 - var3)) / 2;
            DrawableCompat.setHotspotBounds(var7, var10 - var1, var2 - var1, var10 + var1, var2 + var1);
         }

         return var5;
      }
   }

   private class OverflowPopup extends MenuPopupHelper {
      public OverflowPopup(Context var2, MenuBuilder var3, View var4, boolean var5) {
         super(var2, var3, var4, var5, R$attr.actionOverflowMenuStyle);
         this.setGravity(8388613);
         this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      protected void onDismiss() {
         if (ActionMenuPresenter.access$000(ActionMenuPresenter.this) != null) {
            ActionMenuPresenter.access$100(ActionMenuPresenter.this).close();
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
}
