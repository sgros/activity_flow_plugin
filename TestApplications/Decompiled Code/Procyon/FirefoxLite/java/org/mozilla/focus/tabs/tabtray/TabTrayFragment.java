// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.support.v7.util.DiffUtil;
import android.view.ViewTreeObserver$OnPreDrawListener;
import org.mozilla.rocket.privately.PrivateMode;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import android.support.v4.app.Fragment;
import com.bumptech.glide.Glide;
import android.os.Bundle;
import android.content.Intent;
import org.mozilla.rocket.privately.PrivateModeActivity;
import android.view.WindowManager;
import android.view.View$OnTouchListener;
import android.view.GestureDetector$OnGestureListener;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.graphics.Canvas;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.design.widget.BottomSheetBehavior;
import org.mozilla.focus.utils.ViewUtils;
import android.app.Dialog;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;
import java.util.List;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.content.HomeFragmentViewState;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.Window;
import org.mozilla.focus.utils.Settings;
import android.graphics.drawable.LayerDrawable;
import android.content.Context;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import android.support.design.widget.CoordinatorLayout;
import android.os.Looper;
import android.os.Handler;
import org.mozilla.rocket.nightmode.themed.ThemedRecyclerView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import android.app.AlertDialog;
import org.mozilla.rocket.nightmode.themed.ThemedView;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.view.View$OnClickListener;
import android.support.v4.app.DialogFragment;

public class TabTrayFragment extends DialogFragment implements View$OnClickListener, TabClickListener, View
{
    private TabTrayAdapter adapter;
    private Drawable backgroundDrawable;
    private Drawable backgroundOverlay;
    private android.view.View backgroundView;
    private ThemedView bottomDivider;
    private android.view.View closeTabsBtn;
    private AlertDialog closeTabsDialog;
    private Runnable dismissRunnable;
    private ThemedImageView imgNewTab;
    private ThemedImageView imgPrivateBrowsing;
    private LinearLayoutManager layoutManager;
    private android.view.View logoMan;
    private ThemedRelativeLayout newTabBtn;
    private boolean playEnterAnimation;
    private Presenter presenter;
    private android.view.View privateModeBadge;
    private android.view.View privateModeBtn;
    private ThemedRecyclerView recyclerView;
    private SlideAnimationCoordinator slideCoordinator;
    private TabTrayViewModel tabTrayViewModel;
    private Handler uiHandler;
    
    public TabTrayFragment() {
        this.playEnterAnimation = true;
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.slideCoordinator = new SlideAnimationCoordinator(this);
        this.dismissRunnable = new _$$Lambda$kuhIqfuSKG_WgA2VV7wXaumfdUw(this);
        this.tabTrayViewModel = null;
    }
    
    private InterceptBehavior getBehavior(final android.view.View view) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            return null;
        }
        final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior();
        if (behavior == null) {
            return null;
        }
        if (behavior instanceof InterceptBehavior) {
            return (InterceptBehavior)behavior;
        }
        return null;
    }
    
    private int getBottomSheetState() {
        final InterceptBehavior behavior = this.getBehavior((android.view.View)this.recyclerView);
        if (behavior != null) {
            return behavior.getState();
        }
        return -1;
    }
    
    private int getCollapseHeight() {
        final InterceptBehavior behavior = this.getBehavior((android.view.View)this.recyclerView);
        if (behavior != null) {
            return behavior.getPeekHeight();
        }
        return 0;
    }
    
    private void initRecyclerView() {
        this.initRecyclerViewStyle(this.recyclerView);
        this.setupSwipeToDismiss(this.recyclerView);
        this.adapter.setTabClickListener((TabTrayAdapter.TabClickListener)this);
        this.recyclerView.setAdapter((RecyclerView.Adapter)this.adapter);
    }
    
    private void initRecyclerViewStyle(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(recyclerView.getContext(), 1, false)));
        final RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator)itemAnimator).setSupportsChangeAnimations(false);
        }
    }
    
    private void initWindowBackground(final Context context) {
        final Drawable drawable = context.getDrawable(2131230972);
        if (drawable == null) {
            return;
        }
        if (drawable instanceof LayerDrawable) {
            final LayerDrawable layerDrawable = (LayerDrawable)drawable;
            final boolean nightModeEnable = Settings.getInstance(this.getContext()).isNightModeEnable();
            int alpha = 0;
            if (nightModeEnable) {
                this.backgroundDrawable = layerDrawable.findDrawableByLayerId(2131296453);
                layerDrawable.findDrawableByLayerId(2131296452).setAlpha(0);
            }
            else {
                this.backgroundDrawable = layerDrawable.findDrawableByLayerId(2131296452);
                layerDrawable.findDrawableByLayerId(2131296453).setAlpha(0);
            }
            this.backgroundOverlay = layerDrawable.findDrawableByLayerId(2131296302);
            final int validateBackgroundAlpha = this.validateBackgroundAlpha(255);
            this.backgroundDrawable.setAlpha(validateBackgroundAlpha);
            final Drawable backgroundOverlay = this.backgroundOverlay;
            if (this.getBottomSheetState() != 4) {
                alpha = (int)(validateBackgroundAlpha * 0.5f);
            }
            backgroundOverlay.setAlpha(alpha);
        }
        else {
            this.backgroundDrawable = drawable;
        }
        final Window window = this.getDialog().getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(drawable);
    }
    
    private boolean isPositionVisibleWhenCollapse(final int n) {
        final Resources resources = this.getResources();
        return n < (resources.getDimensionPixelSize(2131165454) - resources.getDimensionPixelSize(2131165452)) / (resources.getDimensionPixelSize(2131165448) + resources.getDimensionPixelSize(2131165449));
    }
    
    public static TabTrayFragment newInstance() {
        return new TabTrayFragment();
    }
    
    private void onCloseAllTabsClicked() {
        if (this.closeTabsDialog == null) {
            this.closeTabsDialog = new AlertDialog$Builder((Context)this.getActivity()).setMessage(2131755419).setPositiveButton(2131755061, (DialogInterface$OnClickListener)new _$$Lambda$TabTrayFragment$Dt_zQsvhR7gYSWlSVgXL4_cs9S0(this)).setNegativeButton(2131755060, (DialogInterface$OnClickListener)_$$Lambda$TabTrayFragment$F5I_WDlIouMty4XKZz49qcUNliE.INSTANCE).show();
        }
        else {
            this.closeTabsDialog.show();
        }
    }
    
    private void onFullyExpanded() {
        if (this.logoMan.getVisibility() != 0) {
            this.logoMan.setVisibility(0);
        }
        this.setIntercept(false);
    }
    
    private void onNewTabClicked() {
        HomeFragmentViewState.reset();
        ScreenNavigator.get(this.getContext()).addHomeScreen(false);
        TelemetryWrapper.clickAddTabTray();
        this.postOnNextFrame(this.dismissRunnable);
    }
    
    private void onTranslateToHidden(final float n) {
        this.newTabBtn.setTranslationY(n);
        this.logoMan.setTranslationY(n);
    }
    
    private void postOnNextFrame(final Runnable runnable) {
        this.uiHandler.post((Runnable)new _$$Lambda$TabTrayFragment$OseG1yA4_GVe4hzzPjorn8kSPP0(this, runnable));
    }
    
    private void prepareExpandAnimation() {
        this.setBottomSheetState(5);
        this.slideCoordinator.onSlide(-1.0f);
        this.logoMan.setVisibility(4);
    }
    
    private void setBottomSheetState(final int state) {
        final InterceptBehavior behavior = this.getBehavior((android.view.View)this.recyclerView);
        if (behavior != null) {
            behavior.setState(state);
        }
    }
    
    private void setDialogAnimation(final int windowAnimations) {
        final Dialog dialog = this.getDialog();
        if (dialog == null) {
            return;
        }
        final Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = windowAnimations;
            this.updateWindowAttrs(window);
        }
    }
    
    private void setIntercept(final boolean intercept) {
        final InterceptBehavior behavior = this.getBehavior((android.view.View)this.recyclerView);
        if (behavior != null) {
            behavior.setIntercept(intercept);
        }
    }
    
    private void setNightModeEnabled(final boolean nightMode) {
        this.newTabBtn.setNightMode(nightMode);
        this.imgPrivateBrowsing.setNightMode(nightMode);
        this.imgNewTab.setNightMode(nightMode);
        this.bottomDivider.setNightMode(nightMode);
        this.recyclerView.setNightMode(nightMode);
        ViewUtils.updateStatusBarStyle(nightMode ^ true, this.getDialog().getWindow());
    }
    
    private void setupBottomSheetCallback() {
        final InterceptBehavior behavior = this.getBehavior((android.view.View)this.recyclerView);
        if (behavior == null) {
            return;
        }
        behavior.setBottomSheetCallback((BottomSheetBehavior.BottomSheetCallback)new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onSlide(final android.view.View view, final float n) {
                TabTrayFragment.this.slideCoordinator.onSlide(n);
            }
            
            @Override
            public void onStateChanged(final android.view.View view, final int n) {
                if (n == 5) {
                    TabTrayFragment.this.dismissAllowingStateLoss();
                }
            }
        });
    }
    
    private void setupSwipeToDismiss(final RecyclerView recyclerView) {
        new ItemTouchHelper((ItemTouchHelper.Callback)new ItemTouchHelper.SimpleCallback(0, 48) {
            @Override
            public void onChildDraw(final Canvas canvas, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float a, final float n, final int n2, final boolean b) {
                if (n2 == 1) {
                    viewHolder.itemView.setAlpha(1.0f - Math.abs(a) / (recyclerView.getWidth() / 2.0f));
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, a, n, n2, b);
            }
            
            @Override
            public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder viewHolder2) {
                return false;
            }
            
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int n) {
                HomeFragmentViewState.reset();
                TabTrayFragment.this.presenter.tabCloseClicked(viewHolder.getAdapterPosition());
                TelemetryWrapper.swipeTabFromTabTray();
            }
        }).attachToRecyclerView(recyclerView);
    }
    
    private void setupTapBackgroundToExpand() {
        this.backgroundView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$TabTrayFragment$K3_NNo_q4ZDbs7e_K_J7kaUIi8U(new GestureDetectorCompat(this.getContext(), (GestureDetector$OnGestureListener)new GestureDetector$SimpleOnGestureListener() {
            public boolean onDown(final MotionEvent motionEvent) {
                return true;
            }
            
            public boolean onSingleTapUp(final MotionEvent motionEvent) {
                TabTrayFragment.this.setBottomSheetState(3);
                return true;
            }
        })));
    }
    
    private void startExpandAnimation() {
        this.uiHandler.postDelayed((Runnable)new _$$Lambda$TabTrayFragment$0gaofDKVMKnv0FW_SAAsRwerI5A(this, this.isPositionVisibleWhenCollapse(this.adapter.getData().indexOf(this.adapter.getFocusedTab()))), (long)this.getResources().getInteger(2131361814));
    }
    
    private void updateWindowAttrs(final Window window) {
        final Context context = this.getContext();
        if (context == null) {
            return;
        }
        final WindowManager windowManager = (WindowManager)context.getSystemService("window");
        if (windowManager == null) {
            return;
        }
        final android.view.View decorView = window.getDecorView();
        if (decorView.isAttachedToWindow()) {
            windowManager.updateViewLayout(decorView, (ViewGroup$LayoutParams)window.getAttributes());
        }
    }
    
    private void updateWindowBackground(final float alpha) {
        this.backgroundView.setAlpha(alpha);
        if (this.backgroundDrawable != null) {
            this.backgroundDrawable.setAlpha(this.validateBackgroundAlpha((int)(alpha * 255.0f)));
        }
    }
    
    private void updateWindowOverlay(final float n) {
        if (this.backgroundOverlay != null) {
            this.backgroundOverlay.setAlpha(this.validateBackgroundAlpha((int)(n * 255.0f)));
        }
    }
    
    private int validateBackgroundAlpha(final int a) {
        return Math.max(Math.min(a, 254), 1);
    }
    
    private void waitItemAnimation(final Runnable runnable) {
        this.uiHandler.post((Runnable)new _$$Lambda$TabTrayFragment$AfLKTEaKgD7Px4b5mb3C4OiuQTw(this, runnable));
    }
    
    public void closeTabTray() {
        this.postOnNextFrame(this.dismissRunnable);
    }
    
    public void initData(final List<Session> data, final Session focusedTab) {
        this.adapter.setData(data);
        this.adapter.setFocusedTab(focusedTab);
    }
    
    public void navigateToHome() {
        ScreenNavigator.get(this.getContext()).popToHomeScreen(false);
    }
    
    public void onClick(final android.view.View view) {
        final int id = view.getId();
        if (id != 2131296355) {
            if (id != 2131296371) {
                if (id == 2131296537) {
                    this.onNewTabClicked();
                }
            }
            else {
                this.onCloseAllTabsClicked();
            }
        }
        else {
            TelemetryWrapper.privateModeTray();
            this.startActivity(new Intent(this.getContext(), (Class)PrivateModeActivity.class));
            this.getActivity().overridePendingTransition(2130771987, 2130771988);
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setStyle(1, 2131820832);
        this.adapter = new TabTrayAdapter(Glide.with(this));
        this.presenter = new TabTrayPresenter(this, new TabsSessionModel(TabsSessionProvider.getOrThrow(this.getActivity())));
    }
    
    public android.view.View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final int n = 0;
        final android.view.View inflate = layoutInflater.inflate(2131492974, viewGroup, false);
        this.recyclerView = (ThemedRecyclerView)inflate.findViewById(2131296679);
        this.newTabBtn = (ThemedRelativeLayout)inflate.findViewById(2131296537);
        this.closeTabsBtn = inflate.findViewById(2131296371);
        this.privateModeBtn = inflate.findViewById(2131296355);
        this.privateModeBadge = inflate.findViewById(2131296303);
        this.tabTrayViewModel = ViewModelProviders.of(this).get(TabTrayViewModel.class);
        this.tabTrayViewModel.hasPrivateTab().observe(this, new _$$Lambda$TabTrayFragment$hO9J_fRwETvKja4Cxd_fKNe6BIo(this));
        final android.view.View privateModeBtn = this.privateModeBtn;
        int visibility;
        if (PrivateMode.isEnable(this.getContext())) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        privateModeBtn.setVisibility(visibility);
        this.backgroundView = inflate.findViewById(2131296595);
        this.logoMan = this.backgroundView.findViewById(2131296497);
        this.imgPrivateBrowsing = (ThemedImageView)inflate.findViewById(2131296481);
        this.imgNewTab = (ThemedImageView)inflate.findViewById(2131296568);
        this.bottomDivider = (ThemedView)inflate.findViewById(2131296326);
        final android.view.View viewById = inflate.findViewById(2131296667);
        int visibility2;
        if (Settings.getInstance(this.getContext()).isNightModeEnable()) {
            visibility2 = n;
        }
        else {
            visibility2 = 8;
        }
        viewById.setVisibility(visibility2);
        return inflate;
    }
    
    @Override
    public void onDismiss(final DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.presenter != null) {
            this.presenter.tabTrayClosed();
        }
    }
    
    public void onResume() {
        super.onResume();
        this.tabTrayViewModel.hasPrivateTab().setValue(PrivateMode.hasPrivateSession(this.getContext()));
    }
    
    @Override
    public void onStart() {
        if (this.playEnterAnimation) {
            this.playEnterAnimation = false;
            this.setDialogAnimation(2131820830);
        }
        else {
            this.setDialogAnimation(2131820831);
        }
        super.onStart();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (this.closeTabsDialog != null && this.closeTabsDialog.isShowing()) {
            this.closeTabsDialog.dismiss();
        }
    }
    
    public void onTabClick(final int n) {
        HomeFragmentViewState.reset();
        this.presenter.tabClicked(n);
        TelemetryWrapper.clickTabFromTabTray();
    }
    
    public void onTabCloseClick(final int n) {
        HomeFragmentViewState.reset();
        this.presenter.tabCloseClicked(n);
        TelemetryWrapper.closeTabFromTabTray();
    }
    
    public void onViewCreated(final android.view.View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.setNightModeEnabled(Settings.getInstance(view.getContext()).isNightModeEnable());
        this.initWindowBackground(view.getContext());
        this.setupBottomSheetCallback();
        this.prepareExpandAnimation();
        this.initRecyclerView();
        this.newTabBtn.setOnClickListener((View$OnClickListener)this);
        this.closeTabsBtn.setOnClickListener((View$OnClickListener)this);
        this.privateModeBtn.setOnClickListener((View$OnClickListener)this);
        this.setupTapBackgroundToExpand();
        view.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                TabTrayFragment.this.startExpandAnimation();
                TabTrayFragment.this.presenter.viewReady();
                return false;
            }
        });
    }
    
    public void refreshData(final List<Session> data, final Session session) {
        DiffUtil.calculateDiff((DiffUtil.Callback)new DiffUtil.Callback() {
            final /* synthetic */ List val$oldTabs = TabTrayFragment.this.adapter.getData();
            
            @Override
            public boolean areContentsTheSame(final int n, final int n2) {
                return true;
            }
            
            @Override
            public boolean areItemsTheSame(final int n, final int n2) {
                return data.get(n2).getId().equals(this.val$oldTabs.get(n).getId());
            }
            
            @Override
            public int getNewListSize() {
                return data.size();
            }
            
            @Override
            public int getOldListSize() {
                return this.val$oldTabs.size();
            }
        }, false).dispatchUpdatesTo(this.adapter);
        this.adapter.setData(data);
        this.waitItemAnimation(new _$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc(this, session));
    }
    
    public void refreshTabData(final Session session) {
        final List<Session> data = this.adapter.getData();
        final int index = data.indexOf(session);
        if (index >= 0 && index < data.size()) {
            ((RecyclerView.Adapter)this.adapter).notifyItemChanged(index);
        }
    }
    
    public void showFocusedTab(final int n) {
        this.layoutManager.scrollToPositionWithOffset(n, this.recyclerView.getMeasuredHeight() / 2);
    }
    
    public void tabSwitched(final int n) {
        ScreenNavigator.get(this.getContext()).raiseBrowserScreen(false);
        this.postOnNextFrame(this.dismissRunnable);
    }
    
    private static class SlideAnimationCoordinator
    {
        private float backgroundAlpha;
        private Interpolator backgroundInterpolator;
        private int collapseHeight;
        private TabTrayFragment fragment;
        private float overlayAlpha;
        private Interpolator overlayInterpolator;
        private float translationY;
        
        SlideAnimationCoordinator(final TabTrayFragment fragment) {
            this.backgroundInterpolator = (Interpolator)new AccelerateInterpolator();
            this.overlayInterpolator = (Interpolator)new AccelerateDecelerateInterpolator();
            this.collapseHeight = -1;
            this.translationY = -2.14748365E9f;
            this.backgroundAlpha = -1.0f;
            this.overlayAlpha = -1.0f;
            this.fragment = fragment;
        }
        
        private void onSlide(final float n) {
            float n2 = 0.0f;
            float max;
            float n5;
            if (n < 0.0f) {
                if (this.collapseHeight < 0) {
                    this.collapseHeight = this.fragment.getCollapseHeight();
                }
                final float n3 = (float)this.collapseHeight;
                final float n4 = -n;
                max = Math.max(0.0f, 1.0f - this.backgroundInterpolator.getInterpolation(n4));
                n2 = n3 * n4;
                n5 = 0.0f;
            }
            else {
                n5 = -(this.overlayInterpolator.getInterpolation(1.0f - n) * 0.5f) + 0.5f;
                max = 1.0f;
            }
            if (n >= 1.0f) {
                this.fragment.onFullyExpanded();
            }
            if (Float.compare(this.translationY, n2) != 0) {
                this.translationY = n2;
                this.fragment.onTranslateToHidden(n2);
            }
            if (Float.compare(this.backgroundAlpha, max) != 0) {
                this.backgroundAlpha = max;
                this.fragment.updateWindowBackground(max);
            }
            if (Float.compare(this.overlayAlpha, n5) != 0) {
                this.overlayAlpha = n5;
                this.fragment.updateWindowOverlay(n5);
            }
        }
    }
}
