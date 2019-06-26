package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

@Deprecated
public class ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
   private static final int ID_HOME = 16908332;
   private static final ActionBarDrawerToggle.ActionBarDrawerToggleImpl IMPL;
   private static final float TOGGLE_DRAWABLE_OFFSET = 0.33333334F;
   final Activity mActivity;
   private final ActionBarDrawerToggle.Delegate mActivityImpl;
   private final int mCloseDrawerContentDescRes;
   private Drawable mDrawerImage;
   private final int mDrawerImageResource;
   private boolean mDrawerIndicatorEnabled;
   private final DrawerLayout mDrawerLayout;
   private boolean mHasCustomUpIndicator;
   private Drawable mHomeAsUpIndicator;
   private final int mOpenDrawerContentDescRes;
   private Object mSetIndicatorInfo;
   private ActionBarDrawerToggle.SlideDrawable mSlider;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 18) {
         IMPL = new ActionBarDrawerToggle.ActionBarDrawerToggleImplJellybeanMR2();
      } else if (var0 >= 11) {
         IMPL = new ActionBarDrawerToggle.ActionBarDrawerToggleImplHC();
      } else {
         IMPL = new ActionBarDrawerToggle.ActionBarDrawerToggleImplBase();
      }

   }

   public ActionBarDrawerToggle(Activity var1, DrawerLayout var2, @DrawableRes int var3, @StringRes int var4, @StringRes int var5) {
      boolean var6;
      if (!assumeMaterial(var1)) {
         var6 = true;
      } else {
         var6 = false;
      }

      this(var1, var2, var6, var3, var4, var5);
   }

   public ActionBarDrawerToggle(Activity var1, DrawerLayout var2, boolean var3, @DrawableRes int var4, @StringRes int var5, @StringRes int var6) {
      this.mDrawerIndicatorEnabled = true;
      this.mActivity = var1;
      if (var1 instanceof ActionBarDrawerToggle.DelegateProvider) {
         this.mActivityImpl = ((ActionBarDrawerToggle.DelegateProvider)var1).getDrawerToggleDelegate();
      } else {
         this.mActivityImpl = null;
      }

      this.mDrawerLayout = var2;
      this.mDrawerImageResource = var4;
      this.mOpenDrawerContentDescRes = var5;
      this.mCloseDrawerContentDescRes = var6;
      this.mHomeAsUpIndicator = this.getThemeUpIndicator();
      this.mDrawerImage = ContextCompat.getDrawable(var1, var4);
      this.mSlider = new ActionBarDrawerToggle.SlideDrawable(this.mDrawerImage);
      ActionBarDrawerToggle.SlideDrawable var8 = this.mSlider;
      float var7;
      if (var3) {
         var7 = 0.33333334F;
      } else {
         var7 = 0.0F;
      }

      var8.setOffset(var7);
   }

   private static boolean assumeMaterial(Context var0) {
      boolean var1;
      if (var0.getApplicationInfo().targetSdkVersion >= 21 && VERSION.SDK_INT >= 21) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   Drawable getThemeUpIndicator() {
      Drawable var1;
      if (this.mActivityImpl != null) {
         var1 = this.mActivityImpl.getThemeUpIndicator();
      } else {
         var1 = IMPL.getThemeUpIndicator(this.mActivity);
      }

      return var1;
   }

   public boolean isDrawerIndicatorEnabled() {
      return this.mDrawerIndicatorEnabled;
   }

   public void onConfigurationChanged(Configuration var1) {
      if (!this.mHasCustomUpIndicator) {
         this.mHomeAsUpIndicator = this.getThemeUpIndicator();
      }

      this.mDrawerImage = ContextCompat.getDrawable(this.mActivity, this.mDrawerImageResource);
      this.syncState();
   }

   public void onDrawerClosed(View var1) {
      this.mSlider.setPosition(0.0F);
      if (this.mDrawerIndicatorEnabled) {
         this.setActionBarDescription(this.mOpenDrawerContentDescRes);
      }

   }

   public void onDrawerOpened(View var1) {
      this.mSlider.setPosition(1.0F);
      if (this.mDrawerIndicatorEnabled) {
         this.setActionBarDescription(this.mCloseDrawerContentDescRes);
      }

   }

   public void onDrawerSlide(View var1, float var2) {
      float var3 = this.mSlider.getPosition();
      if (var2 > 0.5F) {
         var2 = Math.max(var3, Math.max(0.0F, var2 - 0.5F) * 2.0F);
      } else {
         var2 = Math.min(var3, var2 * 2.0F);
      }

      this.mSlider.setPosition(var2);
   }

   public void onDrawerStateChanged(int var1) {
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2;
      if (var1 != null && var1.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
         if (this.mDrawerLayout.isDrawerVisible(8388611)) {
            this.mDrawerLayout.closeDrawer(8388611);
         } else {
            this.mDrawerLayout.openDrawer(8388611);
         }

         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void setActionBarDescription(int var1) {
      if (this.mActivityImpl != null) {
         this.mActivityImpl.setActionBarDescription(var1);
      } else {
         this.mSetIndicatorInfo = IMPL.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, var1);
      }

   }

   void setActionBarUpIndicator(Drawable var1, int var2) {
      if (this.mActivityImpl != null) {
         this.mActivityImpl.setActionBarUpIndicator(var1, var2);
      } else {
         this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, var1, var2);
      }

   }

   public void setDrawerIndicatorEnabled(boolean var1) {
      if (var1 != this.mDrawerIndicatorEnabled) {
         if (var1) {
            ActionBarDrawerToggle.SlideDrawable var2 = this.mSlider;
            int var3;
            if (this.mDrawerLayout.isDrawerOpen(8388611)) {
               var3 = this.mCloseDrawerContentDescRes;
            } else {
               var3 = this.mOpenDrawerContentDescRes;
            }

            this.setActionBarUpIndicator(var2, var3);
         } else {
            this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
         }

         this.mDrawerIndicatorEnabled = var1;
      }

   }

   public void setHomeAsUpIndicator(int var1) {
      Drawable var2 = null;
      if (var1 != 0) {
         var2 = ContextCompat.getDrawable(this.mActivity, var1);
      }

      this.setHomeAsUpIndicator(var2);
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      if (var1 == null) {
         this.mHomeAsUpIndicator = this.getThemeUpIndicator();
         this.mHasCustomUpIndicator = false;
      } else {
         this.mHomeAsUpIndicator = var1;
         this.mHasCustomUpIndicator = true;
      }

      if (!this.mDrawerIndicatorEnabled) {
         this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
      }

   }

   public void syncState() {
      if (this.mDrawerLayout.isDrawerOpen(8388611)) {
         this.mSlider.setPosition(1.0F);
      } else {
         this.mSlider.setPosition(0.0F);
      }

      if (this.mDrawerIndicatorEnabled) {
         ActionBarDrawerToggle.SlideDrawable var1 = this.mSlider;
         int var2;
         if (this.mDrawerLayout.isDrawerOpen(8388611)) {
            var2 = this.mCloseDrawerContentDescRes;
         } else {
            var2 = this.mOpenDrawerContentDescRes;
         }

         this.setActionBarUpIndicator(var1, var2);
      }

   }

   private interface ActionBarDrawerToggleImpl {
      Drawable getThemeUpIndicator(Activity var1);

      Object setActionBarDescription(Object var1, Activity var2, int var3);

      Object setActionBarUpIndicator(Object var1, Activity var2, Drawable var3, int var4);
   }

   private static class ActionBarDrawerToggleImplBase implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
      ActionBarDrawerToggleImplBase() {
      }

      public Drawable getThemeUpIndicator(Activity var1) {
         return null;
      }

      public Object setActionBarDescription(Object var1, Activity var2, int var3) {
         return var1;
      }

      public Object setActionBarUpIndicator(Object var1, Activity var2, Drawable var3, int var4) {
         return var1;
      }
   }

   @TargetApi(11)
   @RequiresApi(11)
   private static class ActionBarDrawerToggleImplHC implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
      ActionBarDrawerToggleImplHC() {
      }

      public Drawable getThemeUpIndicator(Activity var1) {
         return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(var1);
      }

      public Object setActionBarDescription(Object var1, Activity var2, int var3) {
         return ActionBarDrawerToggleHoneycomb.setActionBarDescription(var1, var2, var3);
      }

      public Object setActionBarUpIndicator(Object var1, Activity var2, Drawable var3, int var4) {
         return ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(var1, var2, var3, var4);
      }
   }

   @TargetApi(18)
   @RequiresApi(18)
   private static class ActionBarDrawerToggleImplJellybeanMR2 implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
      ActionBarDrawerToggleImplJellybeanMR2() {
      }

      public Drawable getThemeUpIndicator(Activity var1) {
         return ActionBarDrawerToggleJellybeanMR2.getThemeUpIndicator(var1);
      }

      public Object setActionBarDescription(Object var1, Activity var2, int var3) {
         return ActionBarDrawerToggleJellybeanMR2.setActionBarDescription(var1, var2, var3);
      }

      public Object setActionBarUpIndicator(Object var1, Activity var2, Drawable var3, int var4) {
         return ActionBarDrawerToggleJellybeanMR2.setActionBarUpIndicator(var1, var2, var3, var4);
      }
   }

   public interface Delegate {
      @Nullable
      Drawable getThemeUpIndicator();

      void setActionBarDescription(@StringRes int var1);

      void setActionBarUpIndicator(Drawable var1, @StringRes int var2);
   }

   public interface DelegateProvider {
      @Nullable
      ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
   }

   private class SlideDrawable extends InsetDrawable implements Callback {
      private final boolean mHasMirroring;
      private float mOffset;
      private float mPosition;
      private final Rect mTmpRect;

      SlideDrawable(Drawable var2) {
         boolean var3 = false;
         super(var2, 0);
         if (VERSION.SDK_INT > 18) {
            var3 = true;
         }

         this.mHasMirroring = var3;
         this.mTmpRect = new Rect();
      }

      public void draw(Canvas var1) {
         byte var2 = 1;
         this.copyBounds(this.mTmpRect);
         var1.save();
         boolean var3;
         if (ViewCompat.getLayoutDirection(ActionBarDrawerToggle.this.mActivity.getWindow().getDecorView()) == 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var3) {
            var2 = -1;
         }

         int var4 = this.mTmpRect.width();
         var1.translate(-this.mOffset * (float)var4 * this.mPosition * (float)var2, 0.0F);
         if (var3 && !this.mHasMirroring) {
            var1.translate((float)var4, 0.0F);
            var1.scale(-1.0F, 1.0F);
         }

         super.draw(var1);
         var1.restore();
      }

      public float getPosition() {
         return this.mPosition;
      }

      public void setOffset(float var1) {
         this.mOffset = var1;
         this.invalidateSelf();
      }

      public void setPosition(float var1) {
         this.mPosition = var1;
         this.invalidateSelf();
      }
   }
}
