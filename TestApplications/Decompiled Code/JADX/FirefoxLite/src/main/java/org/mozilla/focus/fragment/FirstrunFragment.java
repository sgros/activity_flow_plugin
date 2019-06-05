package org.mozilla.focus.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.view.PagerAdapter;
import android.support.p001v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import java.util.HashMap;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.firstrun.DefaultFirstrunPagerAdapter;
import org.mozilla.focus.firstrun.UpgradeFirstrunPagerAdapter;
import org.mozilla.focus.navigation.ScreenNavigator.Screen;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.rocket.C0769R;

/* compiled from: FirstrunFragment.kt */
public final class FirstrunFragment extends Fragment implements OnClickListener, Screen {
    public static final Companion Companion = new Companion();
    private HashMap _$_findViewCache;
    private Drawable[] bgDrawables;
    private TransitionDrawable bgTransitionDrawable;
    private boolean isTelemetryValid = true;
    private long telemetryStartTimestamp;
    private ViewPager viewPager;

    /* compiled from: FirstrunFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final FirstrunFragment create() {
            return new FirstrunFragment();
        }
    }

    public static final FirstrunFragment create() {
        return Companion.create();
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

    public static final /* synthetic */ Drawable[] access$getBgDrawables$p(FirstrunFragment firstrunFragment) {
        Drawable[] drawableArr = firstrunFragment.bgDrawables;
        if (drawableArr == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgDrawables");
        }
        return drawableArr;
    }

    public static final /* synthetic */ TransitionDrawable access$getBgTransitionDrawable$p(FirstrunFragment firstrunFragment) {
        TransitionDrawable transitionDrawable = firstrunFragment.bgTransitionDrawable;
        if (transitionDrawable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        return transitionDrawable;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initDrawables();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        setExitTransition(TransitionInflater.from(context).inflateTransition(2131951616));
        this.isTelemetryValid = true;
        this.telemetryStartTimestamp = System.currentTimeMillis();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_firstrun, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "view");
        inflate.setClickable(true);
        OnClickListener onClickListener = this;
        inflate.findViewById(C0427R.C0426id.skip).setOnClickListener(onClickListener);
        View findViewById = inflate.findViewById(C0427R.C0426id.background);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "background");
        TransitionDrawable transitionDrawable = this.bgTransitionDrawable;
        if (transitionDrawable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        findViewById.setBackground(transitionDrawable);
        if (viewGroup == null) {
            Intrinsics.throwNpe();
        }
        Context context = viewGroup.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "container!!.context");
        PagerAdapter findPagerAdapter = findPagerAdapter(context, onClickListener);
        if (findPagerAdapter == null) {
            finishFirstrun();
            return inflate;
        }
        findViewById = inflate.findViewById(C0427R.C0426id.pager);
        if (findViewById != null) {
            this.viewPager = (ViewPager) findViewById;
            ViewPager viewPager = this.viewPager;
            if (viewPager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager.setPageTransformer(true, FirstrunFragment$onCreateView$1.INSTANCE);
            viewPager = this.viewPager;
            if (viewPager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager.setClipToPadding(false);
            ViewPager viewPager2 = this.viewPager;
            if (viewPager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager2.setAdapter(findPagerAdapter);
            viewPager2 = this.viewPager;
            if (viewPager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager2.addOnPageChangeListener(new FirstrunFragment$onCreateView$2(this));
            if (findPagerAdapter.getCount() > 1) {
                View findViewById2 = inflate.findViewById(C0427R.C0426id.tabs);
                if (findViewById2 != null) {
                    TabLayout tabLayout = (TabLayout) findViewById2;
                    viewPager2 = this.viewPager;
                    if (viewPager2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("viewPager");
                    }
                    tabLayout.setupWithViewPager(viewPager2, true);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.design.widget.TabLayout");
                }
            }
            return inflate;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.support.v4.view.ViewPager");
    }

    public void onPause() {
        super.onPause();
        this.isTelemetryValid = false;
    }

    public void onClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id == C0427R.C0426id.finish) {
            promoteSetDefaultBrowserIfPreload();
            if (this.isTelemetryValid) {
                NewFeatureNotice instance = NewFeatureNotice.getInstance(getContext());
                Intrinsics.checkExpressionValueIsNotNull(instance, "NewFeatureNotice.getInstance(context)");
                TelemetryWrapper.finishFirstRunEvent(System.currentTimeMillis() - this.telemetryStartTimestamp, instance.getLastShownFeatureVersion());
            }
            finishFirstrun();
        } else if (id == C0427R.C0426id.next) {
            ViewPager viewPager = this.viewPager;
            if (viewPager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            ViewPager viewPager2 = this.viewPager;
            if (viewPager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager.setCurrentItem(viewPager2.getCurrentItem() + 1);
        } else if (id == C0427R.C0426id.skip) {
            finishFirstrun();
        } else {
            throw new IllegalArgumentException("Unknown view");
        }
    }

    public Fragment getFragment() {
        return this;
    }

    private final boolean isSystemApp() {
        Context context = getContext();
        if (context == null) {
            return true;
        }
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return applicationInfo == null || (applicationInfo.flags & 1) != 0;
    }

    private final FirstrunFragment promoteSetDefaultBrowserIfPreload() {
        if (isSystemApp()) {
            DialogUtils.showDefaultSettingNotification(getContext());
        }
        return this;
    }

    private final PagerAdapter findPagerAdapter(Context context, OnClickListener onClickListener) {
        if (!NewFeatureNotice.getInstance(getContext()).hasShownFirstRun()) {
            return new DefaultFirstrunPagerAdapter(context, wrapButtonClickListener(onClickListener));
        }
        return NewFeatureNotice.getInstance(getContext()).shouldShowLiteUpdate() ? new UpgradeFirstrunPagerAdapter(context, onClickListener) : null;
    }

    private final OnClickListener wrapButtonClickListener(OnClickListener onClickListener) {
        return new FirstrunFragment$wrapButtonClickListener$1(this, onClickListener);
    }

    private final void finishFirstrun() {
        NewFeatureNotice.getInstance(getContext()).setFirstRunDidShow();
        NewFeatureNotice.getInstance(getContext()).setLiteUpdateDidShow();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((MainActivity) activity).firstrunFinished();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.activity.MainActivity");
    }

    private final void initDrawables() {
        Drawable[] drawableArr = new Drawable[4];
        Resources resources = getResources();
        Context context = getContext();
        Theme theme = null;
        Drawable drawable = resources.getDrawable(2131230837, context != null ? context.getTheme() : null);
        Intrinsics.checkExpressionValueIsNotNull(drawable, "resources.getDrawable(R.…en_color, context?.theme)");
        drawableArr[0] = drawable;
        resources = getResources();
        Context context2 = getContext();
        drawable = resources.getDrawable(2131230837, context2 != null ? context2.getTheme() : null);
        Intrinsics.checkExpressionValueIsNotNull(drawable, "resources.getDrawable(R.…en_color, context?.theme)");
        drawableArr[1] = drawable;
        Resources resources2 = getResources();
        Context context3 = getContext();
        Drawable drawable2 = resources2.getDrawable(2131230837, context3 != null ? context3.getTheme() : null);
        Intrinsics.checkExpressionValueIsNotNull(drawable2, "resources.getDrawable(R.…en_color, context?.theme)");
        drawableArr[2] = drawable2;
        resources2 = getResources();
        context3 = getContext();
        if (context3 != null) {
            theme = context3.getTheme();
        }
        Drawable drawable3 = resources2.getDrawable(2131230837, theme);
        Intrinsics.checkExpressionValueIsNotNull(drawable3, "resources.getDrawable(R.…en_color, context?.theme)");
        drawableArr[3] = drawable3;
        this.bgDrawables = drawableArr;
        Drawable[] drawableArr2 = this.bgDrawables;
        if (drawableArr2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgDrawables");
        }
        this.bgTransitionDrawable = new TransitionDrawable(drawableArr2);
        TransitionDrawable transitionDrawable = this.bgTransitionDrawable;
        if (transitionDrawable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        transitionDrawable.setId(0, C0427R.C0426id.first_run_bg_even);
        transitionDrawable = this.bgTransitionDrawable;
        if (transitionDrawable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        transitionDrawable.setId(1, C0427R.C0426id.first_run_bg_odd);
    }
}
