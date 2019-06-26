package org.telegram.ui.ActionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;

public class ActionBarMenu extends LinearLayout {
   protected boolean isActionMode;
   protected ActionBar parentActionBar;

   public ActionBarMenu(Context var1) {
      super(var1);
   }

   public ActionBarMenu(Context var1, ActionBar var2) {
      super(var1);
      this.setOrientation(0);
      this.parentActionBar = var2;
   }

   public ActionBarMenuItem addItem(int var1, int var2) {
      int var3;
      if (this.isActionMode) {
         var3 = this.parentActionBar.itemsActionModeBackgroundColor;
      } else {
         var3 = this.parentActionBar.itemsBackgroundColor;
      }

      return this.addItem(var1, var2, var3);
   }

   public ActionBarMenuItem addItem(int var1, int var2, int var3) {
      return this.addItem(var1, var2, var3, (Drawable)null, AndroidUtilities.dp(48.0F), (CharSequence)null);
   }

   public ActionBarMenuItem addItem(int var1, int var2, int var3, Drawable var4, int var5, CharSequence var6) {
      Context var7 = this.getContext();
      int var8;
      if (this.isActionMode) {
         var8 = this.parentActionBar.itemsActionModeColor;
      } else {
         var8 = this.parentActionBar.itemsColor;
      }

      ActionBarMenuItem var9 = new ActionBarMenuItem(var7, this, var3, var8);
      var9.setTag(var1);
      if (var4 != null) {
         var9.iconView.setImageDrawable(var4);
      } else if (var2 != 0) {
         var9.iconView.setImageResource(var2);
      }

      this.addView(var9, new LayoutParams(var5, -1));
      var9.setOnClickListener(new _$$Lambda$ActionBarMenu$ppo9UED664gE_YCecAHKNZM7u90(this));
      if (var6 != null) {
         var9.setContentDescription(var6);
      }

      return var9;
   }

   public ActionBarMenuItem addItem(int var1, Drawable var2) {
      int var3;
      if (this.isActionMode) {
         var3 = this.parentActionBar.itemsActionModeBackgroundColor;
      } else {
         var3 = this.parentActionBar.itemsBackgroundColor;
      }

      return this.addItem(var1, 0, var3, var2, AndroidUtilities.dp(48.0F), (CharSequence)null);
   }

   public ActionBarMenuItem addItemWithWidth(int var1, int var2, int var3) {
      int var4;
      if (this.isActionMode) {
         var4 = this.parentActionBar.itemsActionModeBackgroundColor;
      } else {
         var4 = this.parentActionBar.itemsBackgroundColor;
      }

      return this.addItem(var1, var2, var4, (Drawable)null, var3, (CharSequence)null);
   }

   public ActionBarMenuItem addItemWithWidth(int var1, int var2, int var3, CharSequence var4) {
      int var5;
      if (this.isActionMode) {
         var5 = this.parentActionBar.itemsActionModeBackgroundColor;
      } else {
         var5 = this.parentActionBar.itemsBackgroundColor;
      }

      return this.addItem(var1, var2, var5, (Drawable)null, var3, var4);
   }

   public void clearItems() {
      this.removeAllViews();
   }

   public void closeSearchField(boolean var1) {
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.getChildAt(var3);
         if (var4 instanceof ActionBarMenuItem) {
            ActionBarMenuItem var5 = (ActionBarMenuItem)var4;
            if (var5.isSearchField()) {
               this.parentActionBar.onSearchFieldVisibilityChanged(false);
               var5.toggleSearch(var1);
               break;
            }
         }
      }

   }

   public ActionBarMenuItem getItem(int var1) {
      View var2 = this.findViewWithTag(var1);
      return var2 instanceof ActionBarMenuItem ? (ActionBarMenuItem)var2 : null;
   }

   public void hideAllPopupMenus() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3 instanceof ActionBarMenuItem) {
            ((ActionBarMenuItem)var3).closeSubMenu();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$addItem$0$ActionBarMenu(View var1) {
      ActionBarMenuItem var2 = (ActionBarMenuItem)var1;
      if (var2.hasSubMenu()) {
         if (this.parentActionBar.actionBarMenuOnItemClick.canOpenMenu()) {
            var2.toggleSubMenu();
         }
      } else if (var2.isSearchField()) {
         this.parentActionBar.onSearchFieldVisibilityChanged(var2.toggleSearch(true));
      } else {
         this.onItemClick((Integer)var1.getTag());
      }

   }

   public void onItemClick(int var1) {
      ActionBar.ActionBarMenuOnItemClick var2 = this.parentActionBar.actionBarMenuOnItemClick;
      if (var2 != null) {
         var2.onItemClick(var1);
      }

   }

   public void onMenuButtonPressed() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3 instanceof ActionBarMenuItem) {
            ActionBarMenuItem var4 = (ActionBarMenuItem)var3;
            if (var4.getVisibility() == 0) {
               if (var4.hasSubMenu()) {
                  var4.toggleSubMenu();
                  break;
               }

               if (var4.overrideMenuClick) {
                  this.onItemClick((Integer)var4.getTag());
                  break;
               }
            }
         }
      }

   }

   public void openSearchField(boolean var1, String var2, boolean var3) {
      int var4 = this.getChildCount();

      for(int var5 = 0; var5 < var4; ++var5) {
         View var6 = this.getChildAt(var5);
         if (var6 instanceof ActionBarMenuItem) {
            ActionBarMenuItem var7 = (ActionBarMenuItem)var6;
            if (var7.isSearchField()) {
               if (var1) {
                  this.parentActionBar.onSearchFieldVisibilityChanged(var7.toggleSearch(true));
               }

               var7.setSearchFieldText(var2, var3);
               var7.getSearchField().setSelection(var2.length());
               break;
            }
         }
      }

   }

   protected void redrawPopup(int var1) {
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.getChildAt(var3);
         if (var4 instanceof ActionBarMenuItem) {
            ((ActionBarMenuItem)var4).redrawPopup(var1);
         }
      }

   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.getChildAt(var3).setEnabled(var1);
      }

   }

   protected void setPopupItemsColor(int var1, boolean var2) {
      int var3 = this.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = this.getChildAt(var4);
         if (var5 instanceof ActionBarMenuItem) {
            ((ActionBarMenuItem)var5).setPopupItemsColor(var1, var2);
         }
      }

   }

   public void setSearchTextColor(int var1, boolean var2) {
      int var3 = this.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = this.getChildAt(var4);
         if (var5 instanceof ActionBarMenuItem) {
            ActionBarMenuItem var6 = (ActionBarMenuItem)var5;
            if (var6.isSearchField()) {
               if (var2) {
                  var6.getSearchField().setHintTextColor(var1);
               } else {
                  var6.getSearchField().setTextColor(var1);
               }
               break;
            }
         }
      }

   }

   protected void updateItemsBackgroundColor() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3 instanceof ActionBarMenuItem) {
            int var4;
            if (this.isActionMode) {
               var4 = this.parentActionBar.itemsActionModeBackgroundColor;
            } else {
               var4 = this.parentActionBar.itemsBackgroundColor;
            }

            var3.setBackgroundDrawable(Theme.createSelectorDrawable(var4));
         }
      }

   }

   protected void updateItemsColor() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3 instanceof ActionBarMenuItem) {
            ActionBarMenuItem var5 = (ActionBarMenuItem)var3;
            int var4;
            if (this.isActionMode) {
               var4 = this.parentActionBar.itemsActionModeColor;
            } else {
               var4 = this.parentActionBar.itemsColor;
            }

            var5.setIconColor(var4);
         }
      }

   }
}
