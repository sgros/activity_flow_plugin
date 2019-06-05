package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

public abstract class ActionBar {
   public boolean closeOptionsMenu() {
      return false;
   }

   public boolean collapseActionView() {
      return false;
   }

   public void dispatchMenuVisibilityChanged(boolean var1) {
   }

   public abstract int getDisplayOptions();

   public Context getThemedContext() {
      return null;
   }

   public boolean invalidateOptionsMenu() {
      return false;
   }

   public void onConfigurationChanged(Configuration var1) {
   }

   void onDestroy() {
   }

   public boolean onKeyShortcut(int var1, KeyEvent var2) {
      return false;
   }

   public boolean onMenuKeyEvent(KeyEvent var1) {
      return false;
   }

   public boolean openOptionsMenu() {
      return false;
   }

   public void setDefaultDisplayHomeAsUpEnabled(boolean var1) {
   }

   public abstract void setDisplayHomeAsUpEnabled(boolean var1);

   public abstract void setDisplayShowHomeEnabled(boolean var1);

   public void setElevation(float var1) {
      if (var1 != 0.0F) {
         throw new UnsupportedOperationException("Setting a non-zero elevation is not supported in this action bar configuration.");
      }
   }

   public void setHideOnContentScrollEnabled(boolean var1) {
      if (var1) {
         throw new UnsupportedOperationException("Hide on content scroll is not supported in this action bar configuration.");
      }
   }

   public void setHomeAsUpIndicator(Drawable var1) {
   }

   public void setHomeButtonEnabled(boolean var1) {
   }

   public void setShowHideAnimationEnabled(boolean var1) {
   }

   public void setWindowTitle(CharSequence var1) {
   }

   public ActionMode startActionMode(ActionMode.Callback var1) {
      return null;
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int gravity = 0;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.gravity = 8388627;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.ActionBarLayout);
         this.gravity = var3.getInt(R.styleable.ActionBarLayout_android_layout_gravity, 0);
         var3.recycle();
      }

      public LayoutParams(ActionBar.LayoutParams var1) {
         super(var1);
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }
   }

   public interface OnMenuVisibilityListener {
      void onMenuVisibilityChanged(boolean var1);
   }

   @Deprecated
   public abstract static class Tab {
      public abstract CharSequence getContentDescription();

      public abstract View getCustomView();

      public abstract Drawable getIcon();

      public abstract CharSequence getText();

      public abstract void select();
   }
}
