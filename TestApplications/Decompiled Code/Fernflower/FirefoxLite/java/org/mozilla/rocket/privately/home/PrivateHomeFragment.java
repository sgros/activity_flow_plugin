package org.mozilla.rocket.privately.home;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import com.airbnb.lottie.LottieAnimationView;
import java.util.HashMap;
import kotlin.NotImplementedError;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.rocket.privately.SharedViewModel;

public final class PrivateHomeFragment extends LocaleAwareFragment implements ScreenNavigator.HomeScreen {
   public static final PrivateHomeFragment.Companion Companion = new PrivateHomeFragment.Companion((DefaultConstructorMarker)null);
   private HashMap _$_findViewCache;
   private RelativeLayout btnBack;
   private View fakeInput;
   private LottieAnimationView logoMan;
   private LottieAnimationView lottieMask;

   private final void animatePrivateHome() {
      LottieAnimationView var1 = this.lottieMask;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("lottieMask");
      }

      var1.playAnimation();
      var1 = this.logoMan;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("logoMan");
      }

      var1.playAnimation();
   }

   private final void observeViewModel() {
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         ((SharedViewModel)ViewModelProviders.of(var1).get(SharedViewModel.class)).urlInputState().observe((LifecycleOwner)var1, (Observer)(new Observer() {
            public final void onChanged(Boolean var1) {
               if (var1 != null) {
                  PrivateHomeFragment.this.onUrlInputScreenVisible(var1);
               }

            }
         }));
      }

   }

   public void _$_clearFindViewByIdCache() {
      if (this._$_findViewCache != null) {
         this._$_findViewCache.clear();
      }

   }

   public void applyLocale() {
      StringBuilder var1 = new StringBuilder();
      var1.append("An operation is not implemented: ");
      var1.append("not implemented");
      throw (Throwable)(new NotImplementedError(var1.toString()));
   }

   public Fragment getFragment() {
      return (Fragment)this;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
   }

   public Animation onCreateAnimation(int var1, boolean var2, int var3) {
      if (var2) {
         Animation var4 = AnimationUtils.loadAnimation((Context)this.getActivity(), 2130771987);
         var4.setAnimationListener((AnimationListener)(new AnimationListener() {
            public void onAnimationEnd(Animation var1) {
               PrivateHomeFragment.this.animatePrivateHome();
            }

            public void onAnimationRepeat(Animation var1) {
            }

            public void onAnimationStart(Animation var1) {
            }
         }));
         return var4;
      } else {
         return super.onCreateAnimation(var1, var2, var3);
      }
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Intrinsics.checkParameterIsNotNull(var1, "inflater");
      View var4 = var1.inflate(2131492972, var2, false);
      View var5 = var4.findViewById(2131296569);
      Intrinsics.checkExpressionValueIsNotNull(var5, "view.findViewById(R.id.pm_home_back)");
      this.btnBack = (RelativeLayout)var5;
      var5 = var4.findViewById(2131296575);
      Intrinsics.checkExpressionValueIsNotNull(var5, "view.findViewById(R.id.pm_home_mask)");
      this.lottieMask = (LottieAnimationView)var5;
      var5 = var4.findViewById(2131296574);
      Intrinsics.checkExpressionValueIsNotNull(var5, "view.findViewById(R.id.pm_home_logo)");
      this.logoMan = (LottieAnimationView)var5;
      var5 = var4.findViewById(2131296573);
      Intrinsics.checkExpressionValueIsNotNull(var5, "view.findViewById(R.id.pm_home_fake_input)");
      this.fakeInput = var5;
      RelativeLayout var6 = this.btnBack;
      if (var6 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("btnBack");
      }

      var6.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            FragmentActivity var2 = PrivateHomeFragment.this.getActivity();
            if (var2 instanceof FragmentListener) {
               ((FragmentListener)var2).onNotified((Fragment)PrivateHomeFragment.this, FragmentListener.TYPE.TOGGLE_PRIVATE_MODE, (Object)null);
            }

         }
      }));
      var5 = this.fakeInput;
      if (var5 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
      }

      var5.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            FragmentActivity var2 = PrivateHomeFragment.this.getActivity();
            if (var2 instanceof FragmentListener) {
               ((FragmentListener)var2).onNotified((Fragment)PrivateHomeFragment.this, FragmentListener.TYPE.SHOW_URL_INPUT, (Object)null);
            }

         }
      }));
      this.observeViewModel();
      return var4;
   }

   // $FF: synthetic method
   public void onDestroyView() {
      super.onDestroyView();
      this._$_clearFindViewByIdCache();
   }

   public void onUrlInputScreenVisible(boolean var1) {
      LottieAnimationView var2;
      View var3;
      if (var1) {
         var2 = this.logoMan;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logoMan");
         }

         var2.setVisibility(4);
         var3 = this.fakeInput;
         if (var3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
         }

         var3.setVisibility(4);
      } else {
         var2 = this.logoMan;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logoMan");
         }

         var2.setVisibility(0);
         var3 = this.fakeInput;
         if (var3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
         }

         var3.setVisibility(0);
      }

   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final PrivateHomeFragment create() {
         return new PrivateHomeFragment();
      }
   }
}
