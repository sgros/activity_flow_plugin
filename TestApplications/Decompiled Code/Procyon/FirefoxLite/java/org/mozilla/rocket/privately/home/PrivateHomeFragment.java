// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.privately.home;

import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.content.Context;
import android.view.animation.Animation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import kotlin.NotImplementedError;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.v4.app.FragmentActivity;
import android.arch.lifecycle.ViewModelProviders;
import org.mozilla.rocket.privately.SharedViewModel;
import kotlin.jvm.internal.Intrinsics;
import com.airbnb.lottie.LottieAnimationView;
import android.view.View;
import android.widget.RelativeLayout;
import java.util.HashMap;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.locale.LocaleAwareFragment;

public final class PrivateHomeFragment extends LocaleAwareFragment implements HomeScreen
{
    public static final Companion Companion;
    private HashMap _$_findViewCache;
    private RelativeLayout btnBack;
    private View fakeInput;
    private LottieAnimationView logoMan;
    private LottieAnimationView lottieMask;
    
    static {
        Companion = new Companion(null);
    }
    
    private final void animatePrivateHome() {
        final LottieAnimationView lottieMask = this.lottieMask;
        if (lottieMask == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lottieMask");
        }
        lottieMask.playAnimation();
        final LottieAnimationView logoMan = this.logoMan;
        if (logoMan == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logoMan");
        }
        logoMan.playAnimation();
    }
    
    private final void observeViewModel() {
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            ViewModelProviders.of(activity).get(SharedViewModel.class).urlInputState().observe(activity, (Observer<Boolean>)new PrivateHomeFragment$observeViewModel$$inlined$apply$lambda.PrivateHomeFragment$observeViewModel$$inlined$apply$lambda$1(this));
        }
    }
    
    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }
    
    @Override
    public void applyLocale() {
        final StringBuilder sb = new StringBuilder();
        sb.append("An operation is not implemented: ");
        sb.append("not implemented");
        throw new NotImplementedError(sb.toString());
    }
    
    @Override
    public Fragment getFragment() {
        return this;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public Animation onCreateAnimation(final int n, final boolean b, final int n2) {
        if (b) {
            final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this.getActivity(), 2130771987);
            loadAnimation.setAnimationListener((Animation$AnimationListener)new PrivateHomeFragment$onCreateAnimation.PrivateHomeFragment$onCreateAnimation$1(this));
            return loadAnimation;
        }
        return super.onCreateAnimation(n, b, n2);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        final View inflate = layoutInflater.inflate(2131492972, viewGroup, false);
        final View viewById = inflate.findViewById(2131296569);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "view.findViewById(R.id.pm_home_back)");
        this.btnBack = (RelativeLayout)viewById;
        final View viewById2 = inflate.findViewById(2131296575);
        Intrinsics.checkExpressionValueIsNotNull(viewById2, "view.findViewById(R.id.pm_home_mask)");
        this.lottieMask = (LottieAnimationView)viewById2;
        final View viewById3 = inflate.findViewById(2131296574);
        Intrinsics.checkExpressionValueIsNotNull(viewById3, "view.findViewById(R.id.pm_home_logo)");
        this.logoMan = (LottieAnimationView)viewById3;
        final View viewById4 = inflate.findViewById(2131296573);
        Intrinsics.checkExpressionValueIsNotNull(viewById4, "view.findViewById(R.id.pm_home_fake_input)");
        this.fakeInput = viewById4;
        final RelativeLayout btnBack = this.btnBack;
        if (btnBack == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnBack");
        }
        btnBack.setOnClickListener((View$OnClickListener)new PrivateHomeFragment$onCreateView.PrivateHomeFragment$onCreateView$1(this));
        final View fakeInput = this.fakeInput;
        if (fakeInput == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
        }
        fakeInput.setOnClickListener((View$OnClickListener)new PrivateHomeFragment$onCreateView.PrivateHomeFragment$onCreateView$2(this));
        this.observeViewModel();
        return inflate;
    }
    
    @Override
    public void onUrlInputScreenVisible(final boolean b) {
        if (b) {
            final LottieAnimationView logoMan = this.logoMan;
            if (logoMan == null) {
                Intrinsics.throwUninitializedPropertyAccessException("logoMan");
            }
            logoMan.setVisibility(4);
            final View fakeInput = this.fakeInput;
            if (fakeInput == null) {
                Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
            }
            fakeInput.setVisibility(4);
        }
        else {
            final LottieAnimationView logoMan2 = this.logoMan;
            if (logoMan2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("logoMan");
            }
            logoMan2.setVisibility(0);
            final View fakeInput2 = this.fakeInput;
            if (fakeInput2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("fakeInput");
            }
            fakeInput2.setVisibility(0);
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final PrivateHomeFragment create() {
            return new PrivateHomeFragment();
        }
    }
}
