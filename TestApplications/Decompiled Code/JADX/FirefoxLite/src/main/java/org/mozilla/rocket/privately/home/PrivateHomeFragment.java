package org.mozilla.rocket.privately.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.airbnb.lottie.LottieAnimationView;
import java.util.HashMap;
import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.navigation.ScreenNavigator.HomeScreen;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.privately.SharedViewModel;

/* compiled from: PrivateHomeFragment.kt */
public final class PrivateHomeFragment extends LocaleAwareFragment implements HomeScreen {
    public static final Companion Companion = new Companion();
    private HashMap _$_findViewCache;
    private RelativeLayout btnBack;
    private View fakeInput;
    private LottieAnimationView logoMan;
    private LottieAnimationView lottieMask;

    /* compiled from: PrivateHomeFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final PrivateHomeFragment create() {
            return new PrivateHomeFragment();
        }
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_private_homescreen, viewGroup, false);
        View findViewById = inflate.findViewById(C0427R.C0426id.pm_home_back);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.pm_home_back)");
        this.btnBack = (RelativeLayout) findViewById;
        findViewById = inflate.findViewById(C0427R.C0426id.pm_home_mask);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.pm_home_mask)");
        this.lottieMask = (LottieAnimationView) findViewById;
        findViewById = inflate.findViewById(C0427R.C0426id.pm_home_logo);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.pm_home_logo)");
        this.logoMan = (LottieAnimationView) findViewById;
        findViewById = inflate.findViewById(C0427R.C0426id.pm_home_fake_input);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.pm_home_fake_input)");
        this.fakeInput = findViewById;
        RelativeLayout relativeLayout = this.btnBack;
        if (relativeLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnBack");
        }
        relativeLayout.setOnClickListener(new PrivateHomeFragment$onCreateView$1(this));
        findViewById = this.fakeInput;
        if (findViewById == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
        }
        findViewById.setOnClickListener(new PrivateHomeFragment$onCreateView$2(this));
        observeViewModel();
        return inflate;
    }

    public Fragment getFragment() {
        return this;
    }

    private final void observeViewModel() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((SharedViewModel) ViewModelProviders.m2of(activity).get(SharedViewModel.class)).urlInputState().observe(activity, new PrivateHomeFragment$observeViewModel$$inlined$apply$lambda$1(this));
        }
    }

    public void applyLocale() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("An operation is not implemented: ");
        stringBuilder.append("not implemented");
        throw new NotImplementedError(stringBuilder.toString());
    }

    private final void animatePrivateHome() {
        LottieAnimationView lottieAnimationView = this.lottieMask;
        if (lottieAnimationView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lottieMask");
        }
        lottieAnimationView.playAnimation();
        lottieAnimationView = this.logoMan;
        if (lottieAnimationView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logoMan");
        }
        lottieAnimationView.playAnimation();
    }

    public void onUrlInputScreenVisible(boolean z) {
        LottieAnimationView lottieAnimationView;
        View view;
        if (z) {
            lottieAnimationView = this.logoMan;
            if (lottieAnimationView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("logoMan");
            }
            lottieAnimationView.setVisibility(4);
            view = this.fakeInput;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
            }
            view.setVisibility(4);
            return;
        }
        lottieAnimationView = this.logoMan;
        if (lottieAnimationView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logoMan");
        }
        lottieAnimationView.setVisibility(0);
        view = this.fakeInput;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
        }
        view.setVisibility(0);
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        if (!z) {
            return super.onCreateAnimation(i, z, i2);
        }
        Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(), C0769R.anim.pb_enter);
        loadAnimation.setAnimationListener(new PrivateHomeFragment$onCreateAnimation$1(this));
        return loadAnimation;
    }
}
