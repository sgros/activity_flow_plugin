package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.R;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.internal.ThemeEnforcement;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;

public class NavigationView extends ScrimInsetsFrameLayout {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
   NavigationView.OnNavigationItemSelectedListener listener;
   private final int maxWidth;
   private final NavigationMenu menu;
   private MenuInflater menuInflater;
   private final NavigationMenuPresenter presenter;

   public NavigationView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NavigationView(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.navigationViewStyle);
   }

   public NavigationView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.presenter = new NavigationMenuPresenter();
      this.menu = new NavigationMenu(var1);
      TintTypedArray var4 = ThemeEnforcement.obtainTintedStyledAttributes(var1, var2, R.styleable.NavigationView, var3, R.style.Widget_Design_NavigationView);
      ViewCompat.setBackground(this, var4.getDrawable(R.styleable.NavigationView_android_background));
      if (var4.hasValue(R.styleable.NavigationView_elevation)) {
         ViewCompat.setElevation(this, (float)var4.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
      }

      ViewCompat.setFitsSystemWindows(this, var4.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
      this.maxWidth = var4.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
      ColorStateList var5;
      if (var4.hasValue(R.styleable.NavigationView_itemIconTint)) {
         var5 = var4.getColorStateList(R.styleable.NavigationView_itemIconTint);
      } else {
         var5 = this.createDefaultColorStateList(16842808);
      }

      int var6;
      boolean var10;
      if (var4.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
         var6 = var4.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
         var10 = true;
      } else {
         var10 = false;
         var6 = 0;
      }

      ColorStateList var9 = null;
      if (var4.hasValue(R.styleable.NavigationView_itemTextColor)) {
         var9 = var4.getColorStateList(R.styleable.NavigationView_itemTextColor);
      }

      ColorStateList var7 = var9;
      if (!var10) {
         var7 = var9;
         if (var9 == null) {
            var7 = this.createDefaultColorStateList(16842806);
         }
      }

      Drawable var11 = var4.getDrawable(R.styleable.NavigationView_itemBackground);
      int var8;
      if (var4.hasValue(R.styleable.NavigationView_itemHorizontalPadding)) {
         var8 = var4.getDimensionPixelSize(R.styleable.NavigationView_itemHorizontalPadding, 0);
         this.presenter.setItemHorizontalPadding(var8);
      }

      var8 = var4.getDimensionPixelSize(R.styleable.NavigationView_itemIconPadding, 0);
      this.menu.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            boolean var3;
            if (NavigationView.this.listener != null && NavigationView.this.listener.onNavigationItemSelected(var2)) {
               var3 = true;
            } else {
               var3 = false;
            }

            return var3;
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
      this.presenter.setId(1);
      this.presenter.initForMenu(var1, this.menu);
      this.presenter.setItemIconTintList(var5);
      if (var10) {
         this.presenter.setItemTextAppearance(var6);
      }

      this.presenter.setItemTextColor(var7);
      this.presenter.setItemBackground(var11);
      this.presenter.setItemIconPadding(var8);
      this.menu.addMenuPresenter(this.presenter);
      this.addView((View)this.presenter.getMenuView(this));
      if (var4.hasValue(R.styleable.NavigationView_menu)) {
         this.inflateMenu(var4.getResourceId(R.styleable.NavigationView_menu, 0));
      }

      if (var4.hasValue(R.styleable.NavigationView_headerLayout)) {
         this.inflateHeaderView(var4.getResourceId(R.styleable.NavigationView_headerLayout, 0));
      }

      var4.recycle();
   }

   private ColorStateList createDefaultColorStateList(int var1) {
      TypedValue var2 = new TypedValue();
      if (!this.getContext().getTheme().resolveAttribute(var1, var2, true)) {
         return null;
      } else {
         ColorStateList var3 = AppCompatResources.getColorStateList(this.getContext(), var2.resourceId);
         if (!this.getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, var2, true)) {
            return null;
         } else {
            int var4 = var2.data;
            var1 = var3.getDefaultColor();
            int[] var8 = DISABLED_STATE_SET;
            int[] var5 = CHECKED_STATE_SET;
            int[] var6 = EMPTY_STATE_SET;
            int var7 = var3.getColorForState(DISABLED_STATE_SET, var1);
            return new ColorStateList(new int[][]{var8, var5, var6}, new int[]{var7, var4, var1});
         }
      }
   }

   private MenuInflater getMenuInflater() {
      if (this.menuInflater == null) {
         this.menuInflater = new SupportMenuInflater(this.getContext());
      }

      return this.menuInflater;
   }

   public MenuItem getCheckedItem() {
      return this.presenter.getCheckedItem();
   }

   public int getHeaderCount() {
      return this.presenter.getHeaderCount();
   }

   public Drawable getItemBackground() {
      return this.presenter.getItemBackground();
   }

   public int getItemHorizontalPadding() {
      return this.presenter.getItemHorizontalPadding();
   }

   public int getItemIconPadding() {
      return this.presenter.getItemIconPadding();
   }

   public ColorStateList getItemIconTintList() {
      return this.presenter.getItemTintList();
   }

   public ColorStateList getItemTextColor() {
      return this.presenter.getItemTextColor();
   }

   public Menu getMenu() {
      return this.menu;
   }

   public View inflateHeaderView(int var1) {
      return this.presenter.inflateHeaderView(var1);
   }

   public void inflateMenu(int var1) {
      this.presenter.setUpdateSuspended(true);
      this.getMenuInflater().inflate(var1, this.menu);
      this.presenter.setUpdateSuspended(false);
      this.presenter.updateMenuView(false);
   }

   protected void onInsetsChanged(WindowInsetsCompat var1) {
      this.presenter.dispatchApplyWindowInsets(var1);
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      if (var3 != Integer.MIN_VALUE) {
         if (var3 == 0) {
            var1 = MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824);
         }
      } else {
         var1 = MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(var1), this.maxWidth), 1073741824);
      }

      super.onMeasure(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof NavigationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         NavigationView.SavedState var2 = (NavigationView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.menu.restorePresenterStates(var2.menuState);
      }
   }

   protected Parcelable onSaveInstanceState() {
      NavigationView.SavedState var1 = new NavigationView.SavedState(super.onSaveInstanceState());
      var1.menuState = new Bundle();
      this.menu.savePresenterStates(var1.menuState);
      return var1;
   }

   public void setCheckedItem(int var1) {
      MenuItem var2 = this.menu.findItem(var1);
      if (var2 != null) {
         this.presenter.setCheckedItem((MenuItemImpl)var2);
      }

   }

   public void setCheckedItem(MenuItem var1) {
      var1 = this.menu.findItem(var1.getItemId());
      if (var1 != null) {
         this.presenter.setCheckedItem((MenuItemImpl)var1);
      } else {
         throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
      }
   }

   public void setItemBackground(Drawable var1) {
      this.presenter.setItemBackground(var1);
   }

   public void setItemBackgroundResource(int var1) {
      this.setItemBackground(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setItemHorizontalPadding(int var1) {
      this.presenter.setItemHorizontalPadding(var1);
   }

   public void setItemHorizontalPaddingResource(int var1) {
      this.presenter.setItemHorizontalPadding(this.getResources().getDimensionPixelSize(var1));
   }

   public void setItemIconPadding(int var1) {
      this.presenter.setItemIconPadding(var1);
   }

   public void setItemIconPaddingResource(int var1) {
      this.presenter.setItemIconPadding(this.getResources().getDimensionPixelSize(var1));
   }

   public void setItemIconTintList(ColorStateList var1) {
      this.presenter.setItemIconTintList(var1);
   }

   public void setItemTextAppearance(int var1) {
      this.presenter.setItemTextAppearance(var1);
   }

   public void setItemTextColor(ColorStateList var1) {
      this.presenter.setItemTextColor(var1);
   }

   public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener var1) {
      this.listener = var1;
   }

   public interface OnNavigationItemSelectedListener {
      boolean onNavigationItemSelected(MenuItem var1);
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public NavigationView.SavedState createFromParcel(Parcel var1) {
            return new NavigationView.SavedState(var1, (ClassLoader)null);
         }

         public NavigationView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new NavigationView.SavedState(var1, var2);
         }

         public NavigationView.SavedState[] newArray(int var1) {
            return new NavigationView.SavedState[var1];
         }
      };
      public Bundle menuState;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.menuState = var1.readBundle(var2);
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeBundle(this.menuState);
      }
   }
}
