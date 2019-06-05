// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import android.support.design.widget.TabLayout;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.view.View;
import android.transition.TransitionInflater;
import org.mozilla.focus.utils.DialogUtils;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import kotlin.TypeCastException;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.firstrun.FirstrunPagerAdapter;
import org.mozilla.focus.firstrun.UpgradeFirstrunPagerAdapter;
import org.mozilla.focus.firstrun.DefaultFirstrunPagerAdapter;
import org.mozilla.focus.utils.NewFeatureNotice;
import android.support.v4.view.PagerAdapter;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import android.support.v4.view.ViewPager;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.Drawable;
import java.util.HashMap;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.view.View$OnClickListener;
import android.support.v4.app.Fragment;

public final class FirstrunFragment extends Fragment implements View$OnClickListener, Screen
{
    public static final Companion Companion;
    private HashMap _$_findViewCache;
    private Drawable[] bgDrawables;
    private TransitionDrawable bgTransitionDrawable;
    private boolean isTelemetryValid;
    private long telemetryStartTimestamp;
    private ViewPager viewPager;
    
    static {
        Companion = new Companion(null);
    }
    
    public FirstrunFragment() {
        this.isTelemetryValid = true;
    }
    
    public static final FirstrunFragment create() {
        return FirstrunFragment.Companion.create();
    }
    
    private final PagerAdapter findPagerAdapter(final Context context, final View$OnClickListener view$OnClickListener) {
        FirstrunPagerAdapter firstrunPagerAdapter;
        if (!NewFeatureNotice.getInstance(this.getContext()).hasShownFirstRun()) {
            firstrunPagerAdapter = new DefaultFirstrunPagerAdapter(context, this.wrapButtonClickListener(view$OnClickListener));
        }
        else {
            UpgradeFirstrunPagerAdapter upgradeFirstrunPagerAdapter;
            if (NewFeatureNotice.getInstance(this.getContext()).shouldShowLiteUpdate()) {
                upgradeFirstrunPagerAdapter = new UpgradeFirstrunPagerAdapter(context, view$OnClickListener);
            }
            else {
                upgradeFirstrunPagerAdapter = null;
            }
            firstrunPagerAdapter = upgradeFirstrunPagerAdapter;
        }
        return firstrunPagerAdapter;
    }
    
    private final void finishFirstrun() {
        NewFeatureNotice.getInstance(this.getContext()).setFirstRunDidShow();
        NewFeatureNotice.getInstance(this.getContext()).setLiteUpdateDidShow();
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            ((MainActivity)activity).firstrunFinished();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.activity.MainActivity");
    }
    
    private final void initDrawables() {
        final Resources resources = this.getResources();
        final Context context = this.getContext();
        final Resources$Theme resources$Theme = null;
        Resources$Theme theme;
        if (context != null) {
            theme = context.getTheme();
        }
        else {
            theme = null;
        }
        final Drawable drawable = resources.getDrawable(2131230837, theme);
        Intrinsics.checkExpressionValueIsNotNull(drawable, "resources.getDrawable(R.\u2026en_color, context?.theme)");
        final Resources resources2 = this.getResources();
        final Context context2 = this.getContext();
        Resources$Theme theme2;
        if (context2 != null) {
            theme2 = context2.getTheme();
        }
        else {
            theme2 = null;
        }
        final Drawable drawable2 = resources2.getDrawable(2131230837, theme2);
        Intrinsics.checkExpressionValueIsNotNull(drawable2, "resources.getDrawable(R.\u2026en_color, context?.theme)");
        final Resources resources3 = this.getResources();
        final Context context3 = this.getContext();
        Resources$Theme theme3;
        if (context3 != null) {
            theme3 = context3.getTheme();
        }
        else {
            theme3 = null;
        }
        final Drawable drawable3 = resources3.getDrawable(2131230837, theme3);
        Intrinsics.checkExpressionValueIsNotNull(drawable3, "resources.getDrawable(R.\u2026en_color, context?.theme)");
        final Resources resources4 = this.getResources();
        final Context context4 = this.getContext();
        Resources$Theme theme4 = resources$Theme;
        if (context4 != null) {
            theme4 = context4.getTheme();
        }
        final Drawable drawable4 = resources4.getDrawable(2131230837, theme4);
        Intrinsics.checkExpressionValueIsNotNull(drawable4, "resources.getDrawable(R.\u2026en_color, context?.theme)");
        this.bgDrawables = new Drawable[] { drawable, drawable2, drawable3, drawable4 };
        final Drawable[] bgDrawables = this.bgDrawables;
        if (bgDrawables == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgDrawables");
        }
        this.bgTransitionDrawable = new TransitionDrawable(bgDrawables);
        final TransitionDrawable bgTransitionDrawable = this.bgTransitionDrawable;
        if (bgTransitionDrawable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        bgTransitionDrawable.setId(0, 2131296444);
        final TransitionDrawable bgTransitionDrawable2 = this.bgTransitionDrawable;
        if (bgTransitionDrawable2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        bgTransitionDrawable2.setId(1, 2131296445);
    }
    
    private final boolean isSystemApp() {
        final Context context = this.getContext();
        boolean b2;
        final boolean b = b2 = true;
        if (context != null) {
            final ApplicationInfo applicationInfo = context.getApplicationInfo();
            b2 = b;
            if (applicationInfo != null) {
                b2 = ((applicationInfo.flags & 0x1) != 0x0 && b);
            }
        }
        return b2;
    }
    
    private final FirstrunFragment promoteSetDefaultBrowserIfPreload() {
        if (this.isSystemApp()) {
            DialogUtils.showDefaultSettingNotification(this.getContext());
        }
        return this;
    }
    
    private final View$OnClickListener wrapButtonClickListener(final View$OnClickListener view$OnClickListener) {
        return (View$OnClickListener)new FirstrunFragment$wrapButtonClickListener.FirstrunFragment$wrapButtonClickListener$1(this, view$OnClickListener);
    }
    
    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }
    
    public Fragment getFragment() {
        return this;
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.setExitTransition(TransitionInflater.from(context).inflateTransition(2131951616));
        this.isTelemetryValid = true;
        this.telemetryStartTimestamp = System.currentTimeMillis();
    }
    
    public void onClick(final View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        final int id = view.getId();
        if (id != 2131296443) {
            if (id != 2131296548) {
                if (id != 2131296649) {
                    throw new IllegalArgumentException("Unknown view");
                }
                this.finishFirstrun();
            }
            else {
                final ViewPager viewPager = this.viewPager;
                if (viewPager == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("viewPager");
                }
                final ViewPager viewPager2 = this.viewPager;
                if (viewPager2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("viewPager");
                }
                viewPager.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
        else {
            this.promoteSetDefaultBrowserIfPreload();
            if (this.isTelemetryValid) {
                final NewFeatureNotice instance = NewFeatureNotice.getInstance(this.getContext());
                Intrinsics.checkExpressionValueIsNotNull(instance, "NewFeatureNotice.getInstance(context)");
                TelemetryWrapper.finishFirstRunEvent(System.currentTimeMillis() - this.telemetryStartTimestamp, instance.getLastShownFeatureVersion());
            }
            this.finishFirstrun();
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.initDrawables();
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        final View inflate = layoutInflater.inflate(2131492958, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "view");
        inflate.setClickable(true);
        final View viewById = inflate.findViewById(2131296649);
        final View$OnClickListener onClickListener = (View$OnClickListener)this;
        viewById.setOnClickListener(onClickListener);
        final View viewById2 = inflate.findViewById(2131296301);
        Intrinsics.checkExpressionValueIsNotNull(viewById2, "background");
        final TransitionDrawable bgTransitionDrawable = this.bgTransitionDrawable;
        if (bgTransitionDrawable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bgTransitionDrawable");
        }
        viewById2.setBackground((Drawable)bgTransitionDrawable);
        if (viewGroup == null) {
            Intrinsics.throwNpe();
        }
        final Context context = viewGroup.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "container!!.context");
        final PagerAdapter pagerAdapter = this.findPagerAdapter(context, onClickListener);
        if (pagerAdapter == null) {
            this.finishFirstrun();
            return inflate;
        }
        final View viewById3 = inflate.findViewById(2131296558);
        if (viewById3 != null) {
            this.viewPager = (ViewPager)viewById3;
            final ViewPager viewPager = this.viewPager;
            if (viewPager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager.setPageTransformer(true, (ViewPager.PageTransformer)FirstrunFragment$onCreateView.FirstrunFragment$onCreateView$1.INSTANCE);
            final ViewPager viewPager2 = this.viewPager;
            if (viewPager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager2.setClipToPadding(false);
            final ViewPager viewPager3 = this.viewPager;
            if (viewPager3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager3.setAdapter(pagerAdapter);
            final ViewPager viewPager4 = this.viewPager;
            if (viewPager4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewPager");
            }
            viewPager4.addOnPageChangeListener((ViewPager.OnPageChangeListener)new FirstrunFragment$onCreateView.FirstrunFragment$onCreateView$2(this));
            if (pagerAdapter.getCount() > 1) {
                final View viewById4 = inflate.findViewById(2131296681);
                if (viewById4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.design.widget.TabLayout");
                }
                final TabLayout tabLayout = (TabLayout)viewById4;
                final ViewPager viewPager5 = this.viewPager;
                if (viewPager5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("viewPager");
                }
                tabLayout.setupWithViewPager(viewPager5, true);
            }
            return inflate;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.support.v4.view.ViewPager");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        this.isTelemetryValid = false;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final FirstrunFragment create() {
            return new FirstrunFragment();
        }
    }
}
