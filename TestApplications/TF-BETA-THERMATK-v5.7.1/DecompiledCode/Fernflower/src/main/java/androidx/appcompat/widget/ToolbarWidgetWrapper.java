package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window.Callback;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$drawable;
import androidx.appcompat.R$string;
import androidx.appcompat.R$styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.ActionMenuItem;

public class ToolbarWidgetWrapper implements DecorToolbar {
   private View mCustomView;
   private int mDefaultNavigationContentDescription;
   private Drawable mDefaultNavigationIcon;
   private int mDisplayOpts;
   private CharSequence mHomeDescription;
   private Drawable mIcon;
   private Drawable mLogo;
   boolean mMenuPrepared;
   private Drawable mNavIcon;
   private int mNavigationMode;
   private CharSequence mSubtitle;
   CharSequence mTitle;
   private boolean mTitleSet;
   Toolbar mToolbar;
   Callback mWindowCallback;

   public ToolbarWidgetWrapper(Toolbar var1, boolean var2) {
      this(var1, var2, R$string.abc_action_bar_up_description, R$drawable.abc_ic_ab_back_material);
   }

   public ToolbarWidgetWrapper(Toolbar var1, boolean var2, int var3, int var4) {
      this.mNavigationMode = 0;
      this.mDefaultNavigationContentDescription = 0;
      this.mToolbar = var1;
      this.mTitle = var1.getTitle();
      this.mSubtitle = var1.getSubtitle();
      boolean var5;
      if (this.mTitle != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mTitleSet = var5;
      this.mNavIcon = var1.getNavigationIcon();
      TintTypedArray var8 = TintTypedArray.obtainStyledAttributes(var1.getContext(), (AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      this.mDefaultNavigationIcon = var8.getDrawable(R$styleable.ActionBar_homeAsUpIndicator);
      if (var2) {
         CharSequence var6 = var8.getText(R$styleable.ActionBar_title);
         if (!TextUtils.isEmpty(var6)) {
            this.setTitle(var6);
         }

         var6 = var8.getText(R$styleable.ActionBar_subtitle);
         if (!TextUtils.isEmpty(var6)) {
            this.setSubtitle(var6);
         }

         Drawable var9 = var8.getDrawable(R$styleable.ActionBar_logo);
         if (var9 != null) {
            this.setLogo(var9);
         }

         var9 = var8.getDrawable(R$styleable.ActionBar_icon);
         if (var9 != null) {
            this.setIcon(var9);
         }

         if (this.mNavIcon == null) {
            var9 = this.mDefaultNavigationIcon;
            if (var9 != null) {
               this.setNavigationIcon(var9);
            }
         }

         this.setDisplayOptions(var8.getInt(R$styleable.ActionBar_displayOptions, 0));
         var4 = var8.getResourceId(R$styleable.ActionBar_customNavigationLayout, 0);
         if (var4 != 0) {
            this.setCustomView(LayoutInflater.from(this.mToolbar.getContext()).inflate(var4, this.mToolbar, false));
            this.setDisplayOptions(this.mDisplayOpts | 16);
         }

         var4 = var8.getLayoutDimension(R$styleable.ActionBar_height, 0);
         if (var4 > 0) {
            LayoutParams var10 = this.mToolbar.getLayoutParams();
            var10.height = var4;
            this.mToolbar.setLayoutParams(var10);
         }

         var4 = var8.getDimensionPixelOffset(R$styleable.ActionBar_contentInsetStart, -1);
         int var7 = var8.getDimensionPixelOffset(R$styleable.ActionBar_contentInsetEnd, -1);
         if (var4 >= 0 || var7 >= 0) {
            this.mToolbar.setContentInsetsRelative(Math.max(var4, 0), Math.max(var7, 0));
         }

         var4 = var8.getResourceId(R$styleable.ActionBar_titleTextStyle, 0);
         Toolbar var11;
         if (var4 != 0) {
            var11 = this.mToolbar;
            var11.setTitleTextAppearance(var11.getContext(), var4);
         }

         var4 = var8.getResourceId(R$styleable.ActionBar_subtitleTextStyle, 0);
         if (var4 != 0) {
            var11 = this.mToolbar;
            var11.setSubtitleTextAppearance(var11.getContext(), var4);
         }

         var4 = var8.getResourceId(R$styleable.ActionBar_popupTheme, 0);
         if (var4 != 0) {
            this.mToolbar.setPopupTheme(var4);
         }
      } else {
         this.mDisplayOpts = this.detectDisplayOptions();
      }

      var8.recycle();
      this.setDefaultNavigationContentDescription(var3);
      this.mHomeDescription = this.mToolbar.getNavigationContentDescription();
      this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
         final ActionMenuItem mNavItem;

         {
            this.mNavItem = new ActionMenuItem(ToolbarWidgetWrapper.this.mToolbar.getContext(), 0, 16908332, 0, 0, ToolbarWidgetWrapper.this.mTitle);
         }

         public void onClick(View var1) {
            ToolbarWidgetWrapper var2 = ToolbarWidgetWrapper.this;
            Callback var3 = var2.mWindowCallback;
            if (var3 != null && var2.mMenuPrepared) {
               var3.onMenuItemSelected(0, this.mNavItem);
            }

         }
      });
   }

   private int detectDisplayOptions() {
      byte var1;
      if (this.mToolbar.getNavigationIcon() != null) {
         var1 = 15;
         this.mDefaultNavigationIcon = this.mToolbar.getNavigationIcon();
      } else {
         var1 = 11;
      }

      return var1;
   }

   private void setTitleInt(CharSequence var1) {
      this.mTitle = var1;
      if ((this.mDisplayOpts & 8) != 0) {
         this.mToolbar.setTitle(var1);
      }

   }

   private void updateHomeAccessibility() {
      if ((this.mDisplayOpts & 4) != 0) {
         if (TextUtils.isEmpty(this.mHomeDescription)) {
            this.mToolbar.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
         } else {
            this.mToolbar.setNavigationContentDescription(this.mHomeDescription);
         }
      }

   }

   private void updateNavigationIcon() {
      if ((this.mDisplayOpts & 4) != 0) {
         Toolbar var1 = this.mToolbar;
         Drawable var2 = this.mNavIcon;
         if (var2 == null) {
            var2 = this.mDefaultNavigationIcon;
         }

         var1.setNavigationIcon(var2);
      } else {
         this.mToolbar.setNavigationIcon((Drawable)null);
      }

   }

   private void updateToolbarLogo() {
      int var1 = this.mDisplayOpts;
      Drawable var2;
      if ((var1 & 2) != 0) {
         if ((var1 & 1) != 0) {
            var2 = this.mLogo;
            if (var2 == null) {
               var2 = this.mIcon;
            }
         } else {
            var2 = this.mIcon;
         }
      } else {
         var2 = null;
      }

      this.mToolbar.setLogo(var2);
   }

   public Context getContext() {
      return this.mToolbar.getContext();
   }

   public CharSequence getTitle() {
      return this.mToolbar.getTitle();
   }

   public void setCustomView(View var1) {
      View var2 = this.mCustomView;
      if (var2 != null && (this.mDisplayOpts & 16) != 0) {
         this.mToolbar.removeView(var2);
      }

      this.mCustomView = var1;
      if (var1 != null && (this.mDisplayOpts & 16) != 0) {
         this.mToolbar.addView(this.mCustomView);
      }

   }

   public void setDefaultNavigationContentDescription(int var1) {
      if (var1 != this.mDefaultNavigationContentDescription) {
         this.mDefaultNavigationContentDescription = var1;
         if (TextUtils.isEmpty(this.mToolbar.getNavigationContentDescription())) {
            this.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
         }

      }
   }

   public void setDisplayOptions(int var1) {
      int var2 = this.mDisplayOpts ^ var1;
      this.mDisplayOpts = var1;
      if (var2 != 0) {
         if ((var2 & 4) != 0) {
            if ((var1 & 4) != 0) {
               this.updateHomeAccessibility();
            }

            this.updateNavigationIcon();
         }

         if ((var2 & 3) != 0) {
            this.updateToolbarLogo();
         }

         if ((var2 & 8) != 0) {
            if ((var1 & 8) != 0) {
               this.mToolbar.setTitle(this.mTitle);
               this.mToolbar.setSubtitle(this.mSubtitle);
            } else {
               this.mToolbar.setTitle((CharSequence)null);
               this.mToolbar.setSubtitle((CharSequence)null);
            }
         }

         if ((var2 & 16) != 0) {
            View var3 = this.mCustomView;
            if (var3 != null) {
               if ((var1 & 16) != 0) {
                  this.mToolbar.addView(var3);
               } else {
                  this.mToolbar.removeView(var3);
               }
            }
         }
      }

   }

   public void setIcon(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setIcon(var2);
   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      this.updateToolbarLogo();
   }

   public void setLogo(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setLogo(var2);
   }

   public void setLogo(Drawable var1) {
      this.mLogo = var1;
      this.updateToolbarLogo();
   }

   public void setNavigationContentDescription(int var1) {
      String var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = this.getContext().getString(var1);
      }

      this.setNavigationContentDescription(var2);
   }

   public void setNavigationContentDescription(CharSequence var1) {
      this.mHomeDescription = var1;
      this.updateHomeAccessibility();
   }

   public void setNavigationIcon(Drawable var1) {
      this.mNavIcon = var1;
      this.updateNavigationIcon();
   }

   public void setSubtitle(CharSequence var1) {
      this.mSubtitle = var1;
      if ((this.mDisplayOpts & 8) != 0) {
         this.mToolbar.setSubtitle(var1);
      }

   }

   public void setTitle(CharSequence var1) {
      this.mTitleSet = true;
      this.setTitleInt(var1);
   }

   public void setWindowCallback(Callback var1) {
      this.mWindowCallback = var1;
   }

   public void setWindowTitle(CharSequence var1) {
      if (!this.mTitleSet) {
         this.setTitleInt(var1);
      }

   }
}
