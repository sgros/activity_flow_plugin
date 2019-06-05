package org.mozilla.focus.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import java.util.HashMap;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.firstrun.DefaultFirstrunPagerAdapter;
import org.mozilla.focus.firstrun.UpgradeFirstrunPagerAdapter;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.rocket.periodic.FirstLaunchWorker;
import org.mozilla.rocket.periodic.PeriodicReceiver;

public final class FirstrunFragment extends Fragment implements OnClickListener, ScreenNavigator.Screen {
   public static final FirstrunFragment.Companion Companion = new FirstrunFragment.Companion((DefaultConstructorMarker)null);
   private HashMap _$_findViewCache;
   private Drawable[] bgDrawables;
   private TransitionDrawable bgTransitionDrawable;
   private boolean isTelemetryValid = true;
   private long telemetryStartTimestamp;
   private ViewPager viewPager;

   // $FF: synthetic method
   public static final Drawable[] access$getBgDrawables$p(FirstrunFragment var0) {
      Drawable[] var1 = var0.bgDrawables;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bgDrawables");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final TransitionDrawable access$getBgTransitionDrawable$p(FirstrunFragment var0) {
      TransitionDrawable var1 = var0.bgTransitionDrawable;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
      }

      return var1;
   }

   public static final FirstrunFragment create() {
      return Companion.create();
   }

   private final PagerAdapter findPagerAdapter(Context var1, OnClickListener var2) {
      PagerAdapter var3;
      if (!NewFeatureNotice.getInstance(this.getContext()).hasShownFirstRun()) {
         var3 = (PagerAdapter)(new DefaultFirstrunPagerAdapter(var1, this.wrapButtonClickListener(var2)));
      } else {
         UpgradeFirstrunPagerAdapter var4;
         if (NewFeatureNotice.getInstance(this.getContext()).shouldShowLiteUpdate()) {
            var4 = new UpgradeFirstrunPagerAdapter(var1, var2);
         } else {
            var4 = null;
         }

         var3 = (PagerAdapter)var4;
      }

      return var3;
   }

   private final void finishFirstrun() {
      NewFeatureNotice.getInstance(this.getContext()).setFirstRunDidShow();
      NewFeatureNotice.getInstance(this.getContext()).setLiteUpdateDidShow();
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         ((MainActivity)var1).firstrunFinished();
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.activity.MainActivity");
      }
   }

   private final void initDrawables() {
      Resources var1 = this.getResources();
      Context var2 = this.getContext();
      Object var3 = null;
      Theme var9;
      if (var2 != null) {
         var9 = var2.getTheme();
      } else {
         var9 = null;
      }

      Drawable var8 = var1.getDrawable(2131230837, var9);
      Intrinsics.checkExpressionValueIsNotNull(var8, "resources.getDrawable(R.…en_color, context?.theme)");
      Resources var4 = this.getResources();
      var2 = this.getContext();
      if (var2 != null) {
         var9 = var2.getTheme();
      } else {
         var9 = null;
      }

      Drawable var10 = var4.getDrawable(2131230837, var9);
      Intrinsics.checkExpressionValueIsNotNull(var10, "resources.getDrawable(R.…en_color, context?.theme)");
      Resources var5 = this.getResources();
      var2 = this.getContext();
      if (var2 != null) {
         var9 = var2.getTheme();
      } else {
         var9 = null;
      }

      Drawable var11 = var5.getDrawable(2131230837, var9);
      Intrinsics.checkExpressionValueIsNotNull(var11, "resources.getDrawable(R.…en_color, context?.theme)");
      Resources var6 = this.getResources();
      Context var7 = this.getContext();
      var9 = (Theme)var3;
      if (var7 != null) {
         var9 = var7.getTheme();
      }

      Drawable var12 = var6.getDrawable(2131230837, var9);
      Intrinsics.checkExpressionValueIsNotNull(var12, "resources.getDrawable(R.…en_color, context?.theme)");
      this.bgDrawables = new Drawable[]{var8, var10, var11, var12};
      Drawable[] var13 = this.bgDrawables;
      if (var13 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bgDrawables");
      }

      this.bgTransitionDrawable = new TransitionDrawable(var13);
      TransitionDrawable var14 = this.bgTransitionDrawable;
      if (var14 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
      }

      var14.setId(0, 2131296444);
      var14 = this.bgTransitionDrawable;
      if (var14 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
      }

      var14.setId(1, 2131296445);
   }

   private final boolean isSystemApp() {
      Context var1 = this.getContext();
      boolean var2 = true;
      boolean var3 = var2;
      if (var1 != null) {
         ApplicationInfo var4 = var1.getApplicationInfo();
         var3 = var2;
         if (var4 != null) {
            if ((var4.flags & 1) != 0) {
               var3 = var2;
            } else {
               var3 = false;
            }
         }
      }

      return var3;
   }

   private final FirstrunFragment promoteSetDefaultBrowserIfPreload() {
      if (this.isSystemApp()) {
         DialogUtils.showDefaultSettingNotification(this.getContext());
      }

      return this;
   }

   private final OnClickListener wrapButtonClickListener(final OnClickListener var1) {
      return (OnClickListener)(new OnClickListener() {
         public final void onClick(View var1x) {
            Intrinsics.checkExpressionValueIsNotNull(var1x, "view");
            if (var1x.getId() == 2131296443) {
               FragmentActivity var2 = FirstrunFragment.this.getActivity();
               if (var2 != null) {
                  Intent var3 = new Intent((Context)FirstrunFragment.this.getActivity(), PeriodicReceiver.class);
                  var3.setAction(FirstLaunchWorker.Companion.getACTION());
                  var2.sendBroadcast(var3);
               }
            }

            var1.onClick(var1x);
         }
      });
   }

   public void _$_clearFindViewByIdCache() {
      if (this._$_findViewCache != null) {
         this._$_findViewCache.clear();
      }

   }

   public Fragment getFragment() {
      return (Fragment)this;
   }

   public void onAttach(Context var1) {
      super.onAttach(var1);
      this.setExitTransition(TransitionInflater.from(var1).inflateTransition(2131951616));
      this.isTelemetryValid = true;
      this.telemetryStartTimestamp = System.currentTimeMillis();
   }

   public void onClick(View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "view");
      int var2 = var1.getId();
      if (var2 != 2131296443) {
         if (var2 != 2131296548) {
            if (var2 != 2131296649) {
               throw (Throwable)(new IllegalArgumentException("Unknown view"));
            }

            this.finishFirstrun();
         } else {
            ViewPager var3 = this.viewPager;
            if (var3 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }

            ViewPager var4 = this.viewPager;
            if (var4 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }

            var3.setCurrentItem(var4.getCurrentItem() + 1);
         }
      } else {
         this.promoteSetDefaultBrowserIfPreload();
         if (this.isTelemetryValid) {
            NewFeatureNotice var5 = NewFeatureNotice.getInstance(this.getContext());
            Intrinsics.checkExpressionValueIsNotNull(var5, "NewFeatureNotice.getInstance(context)");
            var2 = var5.getLastShownFeatureVersion();
            TelemetryWrapper.finishFirstRunEvent(System.currentTimeMillis() - this.telemetryStartTimestamp, var2);
         }

         this.finishFirstrun();
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.initDrawables();
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Intrinsics.checkParameterIsNotNull(var1, "inflater");
      View var6 = var1.inflate(2131492958, var2, false);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view");
      var6.setClickable(true);
      View var4 = var6.findViewById(2131296649);
      OnClickListener var10 = (OnClickListener)this;
      var4.setOnClickListener(var10);
      View var5 = var6.findViewById(2131296301);
      Intrinsics.checkExpressionValueIsNotNull(var5, "background");
      TransitionDrawable var14 = this.bgTransitionDrawable;
      if (var14 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
      }

      var5.setBackground((Drawable)var14);
      if (var2 == null) {
         Intrinsics.throwNpe();
      }

      Context var7 = var2.getContext();
      Intrinsics.checkExpressionValueIsNotNull(var7, "container!!.context");
      PagerAdapter var8 = this.findPagerAdapter(var7, var10);
      if (var8 == null) {
         this.finishFirstrun();
         return var6;
      } else {
         View var12 = var6.findViewById(2131296558);
         if (var12 != null) {
            this.viewPager = (ViewPager)var12;
            ViewPager var13 = this.viewPager;
            if (var13 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }

            var13.setPageTransformer(true, (ViewPager.PageTransformer)null.INSTANCE);
            var13 = this.viewPager;
            if (var13 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }

            var13.setClipToPadding(false);
            var13 = this.viewPager;
            if (var13 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }

            var13.setAdapter(var8);
            var13 = this.viewPager;
            if (var13 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }

            var13.addOnPageChangeListener((ViewPager.OnPageChangeListener)(new ViewPager.OnPageChangeListener() {
               public void onPageScrollStateChanged(int var1) {
               }

               public void onPageScrolled(int var1, float var2, int var3) {
               }

               public void onPageSelected(int var1) {
                  Drawable var2 = FirstrunFragment.access$getBgDrawables$p(FirstrunFragment.this)[var1 % FirstrunFragment.access$getBgDrawables$p(FirstrunFragment.this).length];
                  if (var1 % 2 == 0) {
                     FirstrunFragment.access$getBgTransitionDrawable$p(FirstrunFragment.this).setDrawableByLayerId(2131296444, var2);
                     FirstrunFragment.access$getBgTransitionDrawable$p(FirstrunFragment.this).reverseTransition(400);
                  } else {
                     FirstrunFragment.access$getBgTransitionDrawable$p(FirstrunFragment.this).setDrawableByLayerId(2131296445, var2);
                     FirstrunFragment.access$getBgTransitionDrawable$p(FirstrunFragment.this).startTransition(400);
                  }

               }
            }));
            if (var8.getCount() > 1) {
               View var9 = var6.findViewById(2131296681);
               if (var9 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type android.support.design.widget.TabLayout");
               }

               TabLayout var11 = (TabLayout)var9;
               var13 = this.viewPager;
               if (var13 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("viewPager");
               }

               var11.setupWithViewPager(var13, true);
            }

            return var6;
         } else {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v4.view.ViewPager");
         }
      }
   }

   // $FF: synthetic method
   public void onDestroyView() {
      super.onDestroyView();
      this._$_clearFindViewByIdCache();
   }

   public void onPause() {
      super.onPause();
      this.isTelemetryValid = false;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final FirstrunFragment create() {
         return new FirstrunFragment();
      }
   }
}
