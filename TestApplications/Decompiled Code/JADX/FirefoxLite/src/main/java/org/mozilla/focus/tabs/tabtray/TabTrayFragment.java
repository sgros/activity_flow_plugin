package org.mozilla.focus.tabs.tabtray;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.p001v4.app.DialogFragment;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.view.GestureDetectorCompat;
import android.support.p004v7.util.DiffUtil;
import android.support.p004v7.util.DiffUtil.Callback;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.ItemAnimator;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.support.p004v7.widget.SimpleItemAnimator;
import android.support.p004v7.widget.helper.ItemTouchHelper;
import android.support.p004v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import com.bumptech.glide.Glide;
import java.util.List;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.tabs.tabtray.TabTrayAdapter.TabClickListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.nightmode.themed.ThemedRecyclerView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedView;
import org.mozilla.rocket.privately.PrivateMode;
import org.mozilla.rocket.privately.PrivateModeActivity;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.TabsSessionProvider;

public class TabTrayFragment extends DialogFragment implements OnClickListener, TabClickListener, View {
    private TabTrayAdapter adapter;
    private Drawable backgroundDrawable;
    private Drawable backgroundOverlay;
    private View backgroundView;
    private ThemedView bottomDivider;
    private View closeTabsBtn;
    private AlertDialog closeTabsDialog;
    private Runnable dismissRunnable = new C0523-$$Lambda$kuhIqfuSKG_WgA2VV7wXaumfdUw(this);
    private ThemedImageView imgNewTab;
    private ThemedImageView imgPrivateBrowsing;
    private LinearLayoutManager layoutManager;
    private View logoMan;
    private ThemedRelativeLayout newTabBtn;
    private boolean playEnterAnimation = true;
    private Presenter presenter;
    private View privateModeBadge;
    private View privateModeBtn;
    private ThemedRecyclerView recyclerView;
    private SlideAnimationCoordinator slideCoordinator = new SlideAnimationCoordinator(this);
    private TabTrayViewModel tabTrayViewModel = null;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    /* renamed from: org.mozilla.focus.tabs.tabtray.TabTrayFragment$5 */
    class C05255 extends SimpleOnGestureListener {
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        C05255() {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            TabTrayFragment.this.setBottomSheetState(3);
            return true;
        }
    }

    private static class SlideAnimationCoordinator {
        private float backgroundAlpha = -1.0f;
        private Interpolator backgroundInterpolator = new AccelerateInterpolator();
        private int collapseHeight = -1;
        private TabTrayFragment fragment;
        private float overlayAlpha = -1.0f;
        private Interpolator overlayInterpolator = new AccelerateDecelerateInterpolator();
        private float translationY = -2.14748365E9f;

        SlideAnimationCoordinator(TabTrayFragment tabTrayFragment) {
            this.fragment = tabTrayFragment;
        }

        private void onSlide(float f) {
            float f2;
            float f3;
            float f4 = 0.0f;
            if (f < 0.0f) {
                if (this.collapseHeight < 0) {
                    this.collapseHeight = this.fragment.getCollapseHeight();
                }
                f2 = -f;
                f3 = ((float) this.collapseHeight) * f2;
                f2 = Math.max(0.0f, 1.0f - this.backgroundInterpolator.getInterpolation(f2));
                f4 = f3;
                f3 = 0.0f;
            } else {
                f3 = (-(this.overlayInterpolator.getInterpolation(1.0f - f) * 0.5f)) + 0.5f;
                f2 = 1.0f;
            }
            if (f >= 1.0f) {
                this.fragment.onFullyExpanded();
            }
            if (Float.compare(this.translationY, f4) != 0) {
                this.translationY = f4;
                this.fragment.onTranslateToHidden(f4);
            }
            if (Float.compare(this.backgroundAlpha, f2) != 0) {
                this.backgroundAlpha = f2;
                this.fragment.updateWindowBackground(f2);
            }
            if (Float.compare(this.overlayAlpha, f3) != 0) {
                this.overlayAlpha = f3;
                this.fragment.updateWindowOverlay(f3);
            }
        }
    }

    /* renamed from: org.mozilla.focus.tabs.tabtray.TabTrayFragment$3 */
    class C07443 extends BottomSheetCallback {
        C07443() {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                TabTrayFragment.this.dismissAllowingStateLoss();
            }
        }

        public void onSlide(View view, float f) {
            TabTrayFragment.this.slideCoordinator.onSlide(f);
        }
    }

    public static TabTrayFragment newInstance() {
        return new TabTrayFragment();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, C0769R.style.TabTrayTheme);
        this.adapter = new TabTrayAdapter(Glide.with((Fragment) this));
        this.presenter = new TabTrayPresenter(this, new TabsSessionModel(TabsSessionProvider.getOrThrow(getActivity())));
    }

    public void onStart() {
        if (this.playEnterAnimation) {
            this.playEnterAnimation = false;
            setDialogAnimation(C0769R.style.TabTrayDialogEnterExit);
        } else {
            setDialogAnimation(C0769R.style.TabTrayDialogExit);
        }
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        if (this.closeTabsDialog != null && this.closeTabsDialog.isShowing()) {
            this.closeTabsDialog.dismiss();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i = 0;
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_tab_tray, viewGroup, false);
        this.recyclerView = (ThemedRecyclerView) inflate.findViewById(C0427R.C0426id.tab_tray);
        this.newTabBtn = (ThemedRelativeLayout) inflate.findViewById(C0427R.C0426id.new_tab_button);
        this.closeTabsBtn = inflate.findViewById(C0427R.C0426id.close_all_tabs_btn);
        this.privateModeBtn = inflate.findViewById(C0427R.C0426id.btn_private_browsing);
        this.privateModeBadge = inflate.findViewById(C0427R.C0426id.badge_in_private_mode);
        this.tabTrayViewModel = (TabTrayViewModel) ViewModelProviders.m0of((Fragment) this).get(TabTrayViewModel.class);
        this.tabTrayViewModel.hasPrivateTab().observe(this, new C0739-$$Lambda$TabTrayFragment$hO9J_fRwETvKja4Cxd_fKNe6BIo(this));
        this.privateModeBtn.setVisibility(PrivateMode.isEnable(getContext()) ? 0 : 4);
        this.backgroundView = inflate.findViewById(C0427R.C0426id.root_layout);
        this.logoMan = this.backgroundView.findViewById(C0427R.C0426id.logo_man);
        this.imgPrivateBrowsing = (ThemedImageView) inflate.findViewById(C0427R.C0426id.img_private_browsing);
        this.imgNewTab = (ThemedImageView) inflate.findViewById(C0427R.C0426id.plus_sign);
        this.bottomDivider = (ThemedView) inflate.findViewById(C0427R.C0426id.bottom_divider);
        View findViewById = inflate.findViewById(C0427R.C0426id.star_background);
        if (!Settings.getInstance(getContext()).isNightModeEnable()) {
            i = 8;
        }
        findViewById.setVisibility(i);
        return inflate;
    }

    public static /* synthetic */ void lambda$onCreateView$0(TabTrayFragment tabTrayFragment, Boolean bool) {
        if (tabTrayFragment.privateModeBadge != null) {
            tabTrayFragment.privateModeBadge.setVisibility(bool.booleanValue() ? 0 : 4);
        }
    }

    public void onViewCreated(final View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setNightModeEnabled(Settings.getInstance(view.getContext()).isNightModeEnable());
        initWindowBackground(view.getContext());
        setupBottomSheetCallback();
        prepareExpandAnimation();
        initRecyclerView();
        this.newTabBtn.setOnClickListener(this);
        this.closeTabsBtn.setOnClickListener(this);
        this.privateModeBtn.setOnClickListener(this);
        setupTapBackgroundToExpand();
        view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                TabTrayFragment.this.startExpandAnimation();
                TabTrayFragment.this.presenter.viewReady();
                return false;
            }
        });
    }

    public void onResume() {
        super.onResume();
        this.tabTrayViewModel.hasPrivateTab().setValue(Boolean.valueOf(PrivateMode.hasPrivateSession(getContext())));
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C0427R.C0426id.btn_private_browsing) {
            TelemetryWrapper.privateModeTray();
            startActivity(new Intent(getContext(), PrivateModeActivity.class));
            getActivity().overridePendingTransition(C0769R.anim.pb_enter, C0769R.anim.pb_exit);
        } else if (id == C0427R.C0426id.close_all_tabs_btn) {
            onCloseAllTabsClicked();
        } else if (id == C0427R.C0426id.new_tab_button) {
            onNewTabClicked();
        }
    }

    public void onTabClick(int i) {
        HomeFragmentViewState.reset();
        this.presenter.tabClicked(i);
        TelemetryWrapper.clickTabFromTabTray();
    }

    public void onTabCloseClick(int i) {
        HomeFragmentViewState.reset();
        this.presenter.tabCloseClicked(i);
        TelemetryWrapper.closeTabFromTabTray();
    }

    public void initData(List<Session> list, Session session) {
        this.adapter.setData(list);
        this.adapter.setFocusedTab(session);
    }

    public void refreshData(final List<Session> list, Session session) {
        final List data = this.adapter.getData();
        DiffUtil.calculateDiff(new Callback() {
            public boolean areContentsTheSame(int i, int i2) {
                return true;
            }

            public int getOldListSize() {
                return data.size();
            }

            public int getNewListSize() {
                return list.size();
            }

            public boolean areItemsTheSame(int i, int i2) {
                return ((Session) list.get(i2)).getId().equals(((Session) data.get(i)).getId());
            }
        }, false).dispatchUpdatesTo(this.adapter);
        this.adapter.setData(list);
        waitItemAnimation(new C0516-$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc(this, session));
    }

    public static /* synthetic */ void lambda$refreshData$1(TabTrayFragment tabTrayFragment, Session session) {
        Session focusedTab = tabTrayFragment.adapter.getFocusedTab();
        List data = tabTrayFragment.adapter.getData();
        tabTrayFragment.adapter.notifyItemChanged(data.indexOf(focusedTab));
        tabTrayFragment.adapter.setFocusedTab(session);
        tabTrayFragment.adapter.notifyItemChanged(data.indexOf(session));
    }

    public void refreshTabData(Session session) {
        List data = this.adapter.getData();
        int indexOf = data.indexOf(session);
        if (indexOf >= 0 && indexOf < data.size()) {
            this.adapter.notifyItemChanged(indexOf);
        }
    }

    public void showFocusedTab(int i) {
        this.layoutManager.scrollToPositionWithOffset(i, this.recyclerView.getMeasuredHeight() / 2);
    }

    public void tabSwitched(int i) {
        ScreenNavigator.get(getContext()).raiseBrowserScreen(false);
        postOnNextFrame(this.dismissRunnable);
    }

    public void closeTabTray() {
        postOnNextFrame(this.dismissRunnable);
    }

    public void navigateToHome() {
        ScreenNavigator.get(getContext()).popToHomeScreen(false);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.presenter != null) {
            this.presenter.tabTrayClosed();
        }
    }

    private void setupBottomSheetCallback() {
        InterceptBehavior behavior = getBehavior(this.recyclerView);
        if (behavior != null) {
            behavior.setBottomSheetCallback(new C07443());
        }
    }

    private void initRecyclerView() {
        initRecyclerViewStyle(this.recyclerView);
        setupSwipeToDismiss(this.recyclerView);
        this.adapter.setTabClickListener(this);
        this.recyclerView.setAdapter(this.adapter);
    }

    private void setupSwipeToDismiss(RecyclerView recyclerView) {
        new ItemTouchHelper(new SimpleCallback(0, 48) {
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder viewHolder2) {
                return false;
            }

            public void onSwiped(ViewHolder viewHolder, int i) {
                HomeFragmentViewState.reset();
                TabTrayFragment.this.presenter.tabCloseClicked(viewHolder.getAdapterPosition());
                TelemetryWrapper.swipeTabFromTabTray();
            }

            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, ViewHolder viewHolder, float f, float f2, int i, boolean z) {
                if (i == 1) {
                    viewHolder.itemView.setAlpha(1.0f - (Math.abs(f) / (((float) recyclerView.getWidth()) / 2.0f)));
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void prepareExpandAnimation() {
        setBottomSheetState(5);
        this.slideCoordinator.onSlide(-1.0f);
        this.logoMan.setVisibility(4);
    }

    private void startExpandAnimation() {
        this.uiHandler.postDelayed(new C0517-$$Lambda$TabTrayFragment$0gaofDKVMKnv0FW_SAAsRwerI5A(this, isPositionVisibleWhenCollapse(this.adapter.getData().indexOf(this.adapter.getFocusedTab()))), (long) getResources().getInteger(C0769R.integer.tab_tray_transition_time));
    }

    public static /* synthetic */ void lambda$startExpandAnimation$2(TabTrayFragment tabTrayFragment, boolean z) {
        if (z) {
            tabTrayFragment.setBottomSheetState(4);
            tabTrayFragment.logoMan.setVisibility(0);
            tabTrayFragment.setIntercept(false);
            return;
        }
        tabTrayFragment.setBottomSheetState(3);
        tabTrayFragment.setIntercept(true);
    }

    private boolean isPositionVisibleWhenCollapse(int i) {
        Resources resources = getResources();
        return i < (resources.getDimensionPixelSize(C0769R.dimen.tab_tray_peekHeight) - resources.getDimensionPixelSize(C0769R.dimen.tab_tray_new_tab_btn_height)) / (resources.getDimensionPixelSize(C0769R.dimen.tab_tray_item_height) + resources.getDimensionPixelSize(C0769R.dimen.tab_tray_item_space));
    }

    private void waitItemAnimation(Runnable runnable) {
        this.uiHandler.post(new C0518-$$Lambda$TabTrayFragment$AfLKTEaKgD7Px4b5mb3C4OiuQTw(this, runnable));
    }

    public static /* synthetic */ void lambda$waitItemAnimation$4(TabTrayFragment tabTrayFragment, Runnable runnable) {
        ItemAnimator itemAnimator = tabTrayFragment.recyclerView.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.isRunning(new C0740-$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE(tabTrayFragment, runnable));
        }
    }

    private InterceptBehavior getBehavior(View view) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            return null;
        }
        Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior != null && (behavior instanceof InterceptBehavior)) {
            return (InterceptBehavior) behavior;
        }
        return null;
    }

    private void setBottomSheetState(int i) {
        InterceptBehavior behavior = getBehavior(this.recyclerView);
        if (behavior != null) {
            behavior.setState(i);
        }
    }

    private int getBottomSheetState() {
        InterceptBehavior behavior = getBehavior(this.recyclerView);
        return behavior != null ? behavior.getState() : -1;
    }

    private int getCollapseHeight() {
        InterceptBehavior behavior = getBehavior(this.recyclerView);
        return behavior != null ? behavior.getPeekHeight() : 0;
    }

    private void setIntercept(boolean z) {
        InterceptBehavior behavior = getBehavior(this.recyclerView);
        if (behavior != null) {
            behavior.setIntercept(z);
        }
    }

    private void initRecyclerViewStyle(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);
        ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
    }

    private void setupTapBackgroundToExpand() {
        this.backgroundView.setOnTouchListener(new C0521-$$Lambda$TabTrayFragment$K3-NNo_q4ZDbs7e_K-J7kaUIi8U(new GestureDetectorCompat(getContext(), new C05255())));
    }

    static /* synthetic */ boolean lambda$setupTapBackgroundToExpand$5(GestureDetectorCompat gestureDetectorCompat, View view, MotionEvent motionEvent) {
        boolean onTouchEvent = gestureDetectorCompat.onTouchEvent(motionEvent);
        if (onTouchEvent) {
            view.performClick();
        }
        return onTouchEvent;
    }

    private void onNewTabClicked() {
        HomeFragmentViewState.reset();
        ScreenNavigator.get(getContext()).addHomeScreen(false);
        TelemetryWrapper.clickAddTabTray();
        postOnNextFrame(this.dismissRunnable);
    }

    private void onCloseAllTabsClicked() {
        if (this.closeTabsDialog == null) {
            this.closeTabsDialog = new Builder(getActivity()).setMessage(C0769R.string.tab_tray_close_tabs_dialog_msg).setPositiveButton(C0769R.string.action_ok, new C0519-$$Lambda$TabTrayFragment$Dt-zQsvhR7gYSWlSVgXL4-cs9S0(this)).setNegativeButton(C0769R.string.action_cancel, C0520-$$Lambda$TabTrayFragment$F5I-WDlIouMty4XKZz49qcUNliE.INSTANCE).show();
        } else {
            this.closeTabsDialog.show();
        }
    }

    public static /* synthetic */ void lambda$onCloseAllTabsClicked$6(TabTrayFragment tabTrayFragment, DialogInterface dialogInterface, int i) {
        HomeFragmentViewState.reset();
        tabTrayFragment.presenter.closeAllTabs();
        TelemetryWrapper.closeAllTabFromTabTray();
    }

    private void initWindowBackground(Context context) {
        Drawable drawable = context.getDrawable(2131230972);
        if (drawable != null) {
            if (drawable instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                int i = 0;
                if (Settings.getInstance(getContext()).isNightModeEnable()) {
                    this.backgroundDrawable = layerDrawable.findDrawableByLayerId(C0427R.C0426id.gradient_background_night);
                    layerDrawable.findDrawableByLayerId(C0427R.C0426id.gradient_background).setAlpha(0);
                } else {
                    this.backgroundDrawable = layerDrawable.findDrawableByLayerId(C0427R.C0426id.gradient_background);
                    layerDrawable.findDrawableByLayerId(C0427R.C0426id.gradient_background_night).setAlpha(0);
                }
                this.backgroundOverlay = layerDrawable.findDrawableByLayerId(C0427R.C0426id.background_overlay);
                int validateBackgroundAlpha = validateBackgroundAlpha(255);
                this.backgroundDrawable.setAlpha(validateBackgroundAlpha);
                Drawable drawable2 = this.backgroundOverlay;
                if (getBottomSheetState() != 4) {
                    i = (int) (((float) validateBackgroundAlpha) * 0.5f);
                }
                drawable2.setAlpha(i);
            } else {
                this.backgroundDrawable = drawable;
            }
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setBackgroundDrawable(drawable);
            }
        }
    }

    private int validateBackgroundAlpha(int i) {
        return Math.max(Math.min(i, 254), 1);
    }

    private void setDialogAnimation(int i) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.getAttributes().windowAnimations = i;
                updateWindowAttrs(window);
            }
        }
    }

    private void updateWindowAttrs(Window window) {
        Context context = getContext();
        if (context != null) {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (windowManager != null) {
                View decorView = window.getDecorView();
                if (decorView.isAttachedToWindow()) {
                    windowManager.updateViewLayout(decorView, window.getAttributes());
                }
            }
        }
    }

    private void onTranslateToHidden(float f) {
        this.newTabBtn.setTranslationY(f);
        this.logoMan.setTranslationY(f);
    }

    private void updateWindowBackground(float f) {
        this.backgroundView.setAlpha(f);
        if (this.backgroundDrawable != null) {
            this.backgroundDrawable.setAlpha(validateBackgroundAlpha((int) (f * 255.0f)));
        }
    }

    private void updateWindowOverlay(float f) {
        if (this.backgroundOverlay != null) {
            this.backgroundOverlay.setAlpha(validateBackgroundAlpha((int) (f * 255.0f)));
        }
    }

    private void onFullyExpanded() {
        if (this.logoMan.getVisibility() != 0) {
            this.logoMan.setVisibility(0);
        }
        setIntercept(false);
    }

    private void postOnNextFrame(Runnable runnable) {
        this.uiHandler.post(new C0522-$$Lambda$TabTrayFragment$OseG1yA4-GVe4hzzPjorn8kSPP0(this, runnable));
    }

    private void setNightModeEnabled(boolean z) {
        this.newTabBtn.setNightMode(z);
        this.imgPrivateBrowsing.setNightMode(z);
        this.imgNewTab.setNightMode(z);
        this.bottomDivider.setNightMode(z);
        this.recyclerView.setNightMode(z);
        ViewUtils.updateStatusBarStyle(z ^ 1, getDialog().getWindow());
    }
}
