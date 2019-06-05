package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.design.R;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

public class NavigationMenuItemView extends ForegroundLinearLayout implements MenuView.ItemView {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private final AccessibilityDelegateCompat accessibilityDelegate;
   private FrameLayout actionArea;
   boolean checkable;
   private Drawable emptyDrawable;
   private boolean hasIconTintList;
   private final int iconSize;
   private ColorStateList iconTintList;
   private MenuItemImpl itemData;
   private boolean needsEmptyIcon;
   private final CheckedTextView textView;

   public NavigationMenuItemView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NavigationMenuItemView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public NavigationMenuItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.accessibilityDelegate = new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setCheckable(NavigationMenuItemView.this.checkable);
         }
      };
      this.setOrientation(0);
      LayoutInflater.from(var1).inflate(R.layout.design_navigation_menu_item, this, true);
      this.iconSize = var1.getResources().getDimensionPixelSize(R.dimen.design_navigation_icon_size);
      this.textView = (CheckedTextView)this.findViewById(R.id.design_menu_item_text);
      this.textView.setDuplicateParentStateEnabled(true);
      ViewCompat.setAccessibilityDelegate(this.textView, this.accessibilityDelegate);
   }

   private void adjustAppearance() {
      LinearLayoutCompat.LayoutParams var1;
      if (this.shouldExpandActionArea()) {
         this.textView.setVisibility(8);
         if (this.actionArea != null) {
            var1 = (LinearLayoutCompat.LayoutParams)this.actionArea.getLayoutParams();
            var1.width = -1;
            this.actionArea.setLayoutParams(var1);
         }
      } else {
         this.textView.setVisibility(0);
         if (this.actionArea != null) {
            var1 = (LinearLayoutCompat.LayoutParams)this.actionArea.getLayoutParams();
            var1.width = -2;
            this.actionArea.setLayoutParams(var1);
         }
      }

   }

   private StateListDrawable createDefaultBackground() {
      TypedValue var1 = new TypedValue();
      if (this.getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorControlHighlight, var1, true)) {
         StateListDrawable var2 = new StateListDrawable();
         var2.addState(CHECKED_STATE_SET, new ColorDrawable(var1.data));
         var2.addState(EMPTY_STATE_SET, new ColorDrawable(0));
         return var2;
      } else {
         return null;
      }
   }

   private void setActionView(View var1) {
      if (var1 != null) {
         if (this.actionArea == null) {
            this.actionArea = (FrameLayout)((ViewStub)this.findViewById(R.id.design_menu_item_action_area_stub)).inflate();
         }

         this.actionArea.removeAllViews();
         this.actionArea.addView(var1);
      }

   }

   private boolean shouldExpandActionArea() {
      boolean var1;
      if (this.itemData.getTitle() == null && this.itemData.getIcon() == null && this.itemData.getActionView() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public MenuItemImpl getItemData() {
      return this.itemData;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.itemData = var1;
      byte var3;
      if (var1.isVisible()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      this.setVisibility(var3);
      if (this.getBackground() == null) {
         ViewCompat.setBackground(this, this.createDefaultBackground());
      }

      this.setCheckable(var1.isCheckable());
      this.setChecked(var1.isChecked());
      this.setEnabled(var1.isEnabled());
      this.setTitle(var1.getTitle());
      this.setIcon(var1.getIcon());
      this.setActionView(var1.getActionView());
      this.setContentDescription(var1.getContentDescription());
      TooltipCompat.setTooltipText(this, var1.getTooltipText());
      this.adjustAppearance();
   }

   protected int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 1);
      if (this.itemData != null && this.itemData.isCheckable() && this.itemData.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      return var2;
   }

   public boolean prefersCondensedTitle() {
      return false;
   }

   public void recycle() {
      if (this.actionArea != null) {
         this.actionArea.removeAllViews();
      }

      this.textView.setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
   }

   public void setCheckable(boolean var1) {
      this.refreshDrawableState();
      if (this.checkable != var1) {
         this.checkable = var1;
         this.accessibilityDelegate.sendAccessibilityEvent(this.textView, 2048);
      }

   }

   public void setChecked(boolean var1) {
      this.refreshDrawableState();
      this.textView.setChecked(var1);
   }

   public void setHorizontalPadding(int var1) {
      this.setPadding(var1, 0, var1, 0);
   }

   public void setIcon(Drawable var1) {
      if (var1 != null) {
         Drawable var2 = var1;
         if (this.hasIconTintList) {
            ConstantState var3 = var1.getConstantState();
            if (var3 != null) {
               var1 = var3.newDrawable();
            }

            var2 = DrawableCompat.wrap(var1).mutate();
            DrawableCompat.setTintList(var2, this.iconTintList);
         }

         var2.setBounds(0, 0, this.iconSize, this.iconSize);
         var1 = var2;
      } else if (this.needsEmptyIcon) {
         if (this.emptyDrawable == null) {
            this.emptyDrawable = ResourcesCompat.getDrawable(this.getResources(), R.drawable.navigation_empty_icon, this.getContext().getTheme());
            if (this.emptyDrawable != null) {
               this.emptyDrawable.setBounds(0, 0, this.iconSize, this.iconSize);
            }
         }

         var1 = this.emptyDrawable;
      }

      TextViewCompat.setCompoundDrawablesRelative(this.textView, var1, (Drawable)null, (Drawable)null, (Drawable)null);
   }

   public void setIconPadding(int var1) {
      this.textView.setCompoundDrawablePadding(var1);
   }

   void setIconTintList(ColorStateList var1) {
      this.iconTintList = var1;
      boolean var2;
      if (this.iconTintList != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.hasIconTintList = var2;
      if (this.itemData != null) {
         this.setIcon(this.itemData.getIcon());
      }

   }

   public void setNeedsEmptyIcon(boolean var1) {
      this.needsEmptyIcon = var1;
   }

   public void setTextAppearance(int var1) {
      TextViewCompat.setTextAppearance(this.textView, var1);
   }

   public void setTextColor(ColorStateList var1) {
      this.textView.setTextColor(var1);
   }

   public void setTitle(CharSequence var1) {
      this.textView.setText(var1);
   }
}
