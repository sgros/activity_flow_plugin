package org.mozilla.focus.tabs.tabtray;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import com.bumptech.glide.Glide;
import java.util.List;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.nightmode.themed.ThemedRecyclerView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedView;
import org.mozilla.rocket.privately.PrivateMode;
import org.mozilla.rocket.privately.PrivateModeActivity;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.TabsSessionProvider;

public class TabTrayFragment extends DialogFragment implements OnClickListener, TabTrayAdapter.TabClickListener, TabTrayContract.View {
   private TabTrayAdapter adapter;
   private Drawable backgroundDrawable;
   private Drawable backgroundOverlay;
   private View backgroundView;
   private ThemedView bottomDivider;
   private View closeTabsBtn;
   private AlertDialog closeTabsDialog;
   private Runnable dismissRunnable = new _$$Lambda$kuhIqfuSKG_WgA2VV7wXaumfdUw(this);
   private ThemedImageView imgNewTab;
   private ThemedImageView imgPrivateBrowsing;
   private LinearLayoutManager layoutManager;
   private View logoMan;
   private ThemedRelativeLayout newTabBtn;
   private boolean playEnterAnimation = true;
   private TabTrayContract.Presenter presenter;
   private View privateModeBadge;
   private View privateModeBtn;
   private ThemedRecyclerView recyclerView;
   private TabTrayFragment.SlideAnimationCoordinator slideCoordinator = new TabTrayFragment.SlideAnimationCoordinator(this);
   private TabTrayViewModel tabTrayViewModel = null;
   private Handler uiHandler = new Handler(Looper.getMainLooper());

   private InterceptBehavior getBehavior(View var1) {
      LayoutParams var2 = var1.getLayoutParams();
      if (!(var2 instanceof CoordinatorLayout.LayoutParams)) {
         return null;
      } else {
         CoordinatorLayout.Behavior var3 = ((CoordinatorLayout.LayoutParams)var2).getBehavior();
         if (var3 == null) {
            return null;
         } else {
            return var3 instanceof InterceptBehavior ? (InterceptBehavior)var3 : null;
         }
      }
   }

   private int getBottomSheetState() {
      InterceptBehavior var1 = this.getBehavior(this.recyclerView);
      return var1 != null ? var1.getState() : -1;
   }

   private int getCollapseHeight() {
      InterceptBehavior var1 = this.getBehavior(this.recyclerView);
      return var1 != null ? var1.getPeekHeight() : 0;
   }

   private void initRecyclerView() {
      this.initRecyclerViewStyle(this.recyclerView);
      this.setupSwipeToDismiss(this.recyclerView);
      this.adapter.setTabClickListener(this);
      this.recyclerView.setAdapter(this.adapter);
   }

   private void initRecyclerViewStyle(RecyclerView var1) {
      LinearLayoutManager var2 = new LinearLayoutManager(var1.getContext(), 1, false);
      this.layoutManager = var2;
      var1.setLayoutManager(var2);
      RecyclerView.ItemAnimator var3 = var1.getItemAnimator();
      if (var3 instanceof SimpleItemAnimator) {
         ((SimpleItemAnimator)var3).setSupportsChangeAnimations(false);
      }

   }

   private void initWindowBackground(Context var1) {
      Drawable var6 = var1.getDrawable(2131230972);
      if (var6 != null) {
         if (var6 instanceof LayerDrawable) {
            LayerDrawable var2 = (LayerDrawable)var6;
            boolean var3 = Settings.getInstance(this.getContext()).isNightModeEnable();
            int var4 = 0;
            if (var3) {
               this.backgroundDrawable = var2.findDrawableByLayerId(2131296453);
               var2.findDrawableByLayerId(2131296452).setAlpha(0);
            } else {
               this.backgroundDrawable = var2.findDrawableByLayerId(2131296452);
               var2.findDrawableByLayerId(2131296453).setAlpha(0);
            }

            this.backgroundOverlay = var2.findDrawableByLayerId(2131296302);
            int var5 = this.validateBackgroundAlpha(255);
            this.backgroundDrawable.setAlpha(var5);
            Drawable var7 = this.backgroundOverlay;
            if (this.getBottomSheetState() != 4) {
               var4 = (int)((float)var5 * 0.5F);
            }

            var7.setAlpha(var4);
         } else {
            this.backgroundDrawable = var6;
         }

         Window var8 = this.getDialog().getWindow();
         if (var8 != null) {
            var8.setBackgroundDrawable(var6);
         }
      }
   }

   private boolean isPositionVisibleWhenCollapse(int var1) {
      Resources var2 = this.getResources();
      boolean var3;
      if (var1 < (var2.getDimensionPixelSize(2131165454) - var2.getDimensionPixelSize(2131165452)) / (var2.getDimensionPixelSize(2131165448) + var2.getDimensionPixelSize(2131165449))) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   // $FF: synthetic method
   public static void lambda$null$3(TabTrayFragment var0, Runnable var1) {
      var0.uiHandler.post(var1);
   }

   // $FF: synthetic method
   public static void lambda$onCloseAllTabsClicked$6(TabTrayFragment var0, DialogInterface var1, int var2) {
      HomeFragmentViewState.reset();
      var0.presenter.closeAllTabs();
      TelemetryWrapper.closeAllTabFromTabTray();
   }

   // $FF: synthetic method
   static void lambda$onCloseAllTabsClicked$7(DialogInterface var0, int var1) {
      var0.dismiss();
   }

   // $FF: synthetic method
   public static void lambda$onCreateView$0(TabTrayFragment var0, Boolean var1) {
      if (var0.privateModeBadge != null) {
         View var3 = var0.privateModeBadge;
         byte var2;
         if (var1) {
            var2 = 0;
         } else {
            var2 = 4;
         }

         var3.setVisibility(var2);
      }

   }

   // $FF: synthetic method
   public static void lambda$postOnNextFrame$8(TabTrayFragment var0, Runnable var1) {
      var0.uiHandler.post(var1);
   }

   // $FF: synthetic method
   public static void lambda$refreshData$1(TabTrayFragment var0, Session var1) {
      Session var2 = var0.adapter.getFocusedTab();
      List var3 = var0.adapter.getData();
      int var4 = var3.indexOf(var2);
      var0.adapter.notifyItemChanged(var4);
      var0.adapter.setFocusedTab(var1);
      var4 = var3.indexOf(var1);
      var0.adapter.notifyItemChanged(var4);
   }

   // $FF: synthetic method
   static boolean lambda$setupTapBackgroundToExpand$5(GestureDetectorCompat var0, View var1, MotionEvent var2) {
      boolean var3 = var0.onTouchEvent(var2);
      if (var3) {
         var1.performClick();
      }

      return var3;
   }

   // $FF: synthetic method
   public static void lambda$startExpandAnimation$2(TabTrayFragment var0, boolean var1) {
      if (var1) {
         var0.setBottomSheetState(4);
         var0.logoMan.setVisibility(0);
         var0.setIntercept(false);
      } else {
         var0.setBottomSheetState(3);
         var0.setIntercept(true);
      }

   }

   // $FF: synthetic method
   public static void lambda$waitItemAnimation$4(TabTrayFragment var0, Runnable var1) {
      RecyclerView.ItemAnimator var2 = var0.recyclerView.getItemAnimator();
      if (var2 != null) {
         var2.isRunning(new _$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE(var0, var1));
      }
   }

   public static TabTrayFragment newInstance() {
      return new TabTrayFragment();
   }

   private void onCloseAllTabsClicked() {
      if (this.closeTabsDialog == null) {
         this.closeTabsDialog = (new Builder(this.getActivity())).setMessage(2131755419).setPositiveButton(2131755061, new _$$Lambda$TabTrayFragment$Dt_zQsvhR7gYSWlSVgXL4_cs9S0(this)).setNegativeButton(2131755060, _$$Lambda$TabTrayFragment$F5I_WDlIouMty4XKZz49qcUNliE.INSTANCE).show();
      } else {
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

   private void onTranslateToHidden(float var1) {
      this.newTabBtn.setTranslationY(var1);
      this.logoMan.setTranslationY(var1);
   }

   private void postOnNextFrame(Runnable var1) {
      this.uiHandler.post(new _$$Lambda$TabTrayFragment$OseG1yA4_GVe4hzzPjorn8kSPP0(this, var1));
   }

   private void prepareExpandAnimation() {
      this.setBottomSheetState(5);
      this.slideCoordinator.onSlide(-1.0F);
      this.logoMan.setVisibility(4);
   }

   private void setBottomSheetState(int var1) {
      InterceptBehavior var2 = this.getBehavior(this.recyclerView);
      if (var2 != null) {
         var2.setState(var1);
      }

   }

   private void setDialogAnimation(int var1) {
      Dialog var2 = this.getDialog();
      if (var2 != null) {
         Window var3 = var2.getWindow();
         if (var3 != null) {
            var3.getAttributes().windowAnimations = var1;
            this.updateWindowAttrs(var3);
         }

      }
   }

   private void setIntercept(boolean var1) {
      InterceptBehavior var2 = this.getBehavior(this.recyclerView);
      if (var2 != null) {
         var2.setIntercept(var1);
      }

   }

   private void setNightModeEnabled(boolean var1) {
      this.newTabBtn.setNightMode(var1);
      this.imgPrivateBrowsing.setNightMode(var1);
      this.imgNewTab.setNightMode(var1);
      this.bottomDivider.setNightMode(var1);
      this.recyclerView.setNightMode(var1);
      ViewUtils.updateStatusBarStyle(var1 ^ true, this.getDialog().getWindow());
   }

   private void setupBottomSheetCallback() {
      InterceptBehavior var1 = this.getBehavior(this.recyclerView);
      if (var1 != null) {
         var1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            public void onSlide(View var1, float var2) {
               TabTrayFragment.this.slideCoordinator.onSlide(var2);
            }

            public void onStateChanged(View var1, int var2) {
               if (var2 == 5) {
                  TabTrayFragment.this.dismissAllowingStateLoss();
               }

            }
         });
      }
   }

   private void setupSwipeToDismiss(RecyclerView var1) {
      (new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 48) {
         public void onChildDraw(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, float var4, float var5, int var6, boolean var7) {
            if (var6 == 1) {
               float var8 = Math.abs(var4) / ((float)var2.getWidth() / 2.0F);
               var3.itemView.setAlpha(1.0F - var8);
            }

            super.onChildDraw(var1, var2, var3, var4, var5, var6, var7);
         }

         public boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3) {
            return false;
         }

         public void onSwiped(RecyclerView.ViewHolder var1, int var2) {
            HomeFragmentViewState.reset();
            TabTrayFragment.this.presenter.tabCloseClicked(var1.getAdapterPosition());
            TelemetryWrapper.swipeTabFromTabTray();
         }
      })).attachToRecyclerView(var1);
   }

   private void setupTapBackgroundToExpand() {
      GestureDetectorCompat var1 = new GestureDetectorCompat(this.getContext(), new SimpleOnGestureListener() {
         public boolean onDown(MotionEvent var1) {
            return true;
         }

         public boolean onSingleTapUp(MotionEvent var1) {
            TabTrayFragment.this.setBottomSheetState(3);
            return true;
         }
      });
      this.backgroundView.setOnTouchListener(new _$$Lambda$TabTrayFragment$K3_NNo_q4ZDbs7e_K_J7kaUIi8U(var1));
   }

   private void startExpandAnimation() {
      boolean var1 = this.isPositionVisibleWhenCollapse(this.adapter.getData().indexOf(this.adapter.getFocusedTab()));
      this.uiHandler.postDelayed(new _$$Lambda$TabTrayFragment$0gaofDKVMKnv0FW_SAAsRwerI5A(this, var1), (long)this.getResources().getInteger(2131361814));
   }

   private void updateWindowAttrs(Window var1) {
      Context var2 = this.getContext();
      if (var2 != null) {
         WindowManager var3 = (WindowManager)var2.getSystemService("window");
         if (var3 != null) {
            View var4 = var1.getDecorView();
            if (var4.isAttachedToWindow()) {
               var3.updateViewLayout(var4, var1.getAttributes());
            }

         }
      }
   }

   private void updateWindowBackground(float var1) {
      this.backgroundView.setAlpha(var1);
      if (this.backgroundDrawable != null) {
         this.backgroundDrawable.setAlpha(this.validateBackgroundAlpha((int)(var1 * 255.0F)));
      }

   }

   private void updateWindowOverlay(float var1) {
      if (this.backgroundOverlay != null) {
         this.backgroundOverlay.setAlpha(this.validateBackgroundAlpha((int)(var1 * 255.0F)));
      }

   }

   private int validateBackgroundAlpha(int var1) {
      return Math.max(Math.min(var1, 254), 1);
   }

   private void waitItemAnimation(Runnable var1) {
      this.uiHandler.post(new _$$Lambda$TabTrayFragment$AfLKTEaKgD7Px4b5mb3C4OiuQTw(this, var1));
   }

   public void closeTabTray() {
      this.postOnNextFrame(this.dismissRunnable);
   }

   public void initData(List var1, Session var2) {
      this.adapter.setData(var1);
      this.adapter.setFocusedTab(var2);
   }

   public void navigateToHome() {
      ScreenNavigator.get(this.getContext()).popToHomeScreen(false);
   }

   public void onClick(View var1) {
      int var2 = var1.getId();
      if (var2 != 2131296355) {
         if (var2 != 2131296371) {
            if (var2 == 2131296537) {
               this.onNewTabClicked();
            }
         } else {
            this.onCloseAllTabsClicked();
         }
      } else {
         TelemetryWrapper.privateModeTray();
         this.startActivity(new Intent(this.getContext(), PrivateModeActivity.class));
         this.getActivity().overridePendingTransition(2130771987, 2130771988);
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setStyle(1, 2131820832);
      this.adapter = new TabTrayAdapter(Glide.with((Fragment)this));
      this.presenter = new TabTrayPresenter(this, new TabsSessionModel(TabsSessionProvider.getOrThrow(this.getActivity())));
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      byte var4 = 0;
      View var6 = var1.inflate(2131492974, var2, false);
      this.recyclerView = (ThemedRecyclerView)var6.findViewById(2131296679);
      this.newTabBtn = (ThemedRelativeLayout)var6.findViewById(2131296537);
      this.closeTabsBtn = var6.findViewById(2131296371);
      this.privateModeBtn = var6.findViewById(2131296355);
      this.privateModeBadge = var6.findViewById(2131296303);
      this.tabTrayViewModel = (TabTrayViewModel)ViewModelProviders.of((Fragment)this).get(TabTrayViewModel.class);
      this.tabTrayViewModel.hasPrivateTab().observe(this, new _$$Lambda$TabTrayFragment$hO9J_fRwETvKja4Cxd_fKNe6BIo(this));
      View var7 = this.privateModeBtn;
      byte var5;
      if (PrivateMode.isEnable(this.getContext())) {
         var5 = 0;
      } else {
         var5 = 4;
      }

      var7.setVisibility(var5);
      this.backgroundView = var6.findViewById(2131296595);
      this.logoMan = this.backgroundView.findViewById(2131296497);
      this.imgPrivateBrowsing = (ThemedImageView)var6.findViewById(2131296481);
      this.imgNewTab = (ThemedImageView)var6.findViewById(2131296568);
      this.bottomDivider = (ThemedView)var6.findViewById(2131296326);
      var7 = var6.findViewById(2131296667);
      if (Settings.getInstance(this.getContext()).isNightModeEnable()) {
         var5 = var4;
      } else {
         var5 = 8;
      }

      var7.setVisibility(var5);
      return var6;
   }

   public void onDismiss(DialogInterface var1) {
      super.onDismiss(var1);
      if (this.presenter != null) {
         this.presenter.tabTrayClosed();
      }

   }

   public void onResume() {
      super.onResume();
      this.tabTrayViewModel.hasPrivateTab().setValue(PrivateMode.hasPrivateSession(this.getContext()));
   }

   public void onStart() {
      if (this.playEnterAnimation) {
         this.playEnterAnimation = false;
         this.setDialogAnimation(2131820830);
      } else {
         this.setDialogAnimation(2131820831);
      }

      super.onStart();
   }

   public void onStop() {
      super.onStop();
      if (this.closeTabsDialog != null && this.closeTabsDialog.isShowing()) {
         this.closeTabsDialog.dismiss();
      }

   }

   public void onTabClick(int var1) {
      HomeFragmentViewState.reset();
      this.presenter.tabClicked(var1);
      TelemetryWrapper.clickTabFromTabTray();
   }

   public void onTabCloseClick(int var1) {
      HomeFragmentViewState.reset();
      this.presenter.tabCloseClicked(var1);
      TelemetryWrapper.closeTabFromTabTray();
   }

   public void onViewCreated(final View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      this.setNightModeEnabled(Settings.getInstance(var1.getContext()).isNightModeEnable());
      this.initWindowBackground(var1.getContext());
      this.setupBottomSheetCallback();
      this.prepareExpandAnimation();
      this.initRecyclerView();
      this.newTabBtn.setOnClickListener(this);
      this.closeTabsBtn.setOnClickListener(this);
      this.privateModeBtn.setOnClickListener(this);
      this.setupTapBackgroundToExpand();
      var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
         public boolean onPreDraw() {
            var1.getViewTreeObserver().removeOnPreDrawListener(this);
            TabTrayFragment.this.startExpandAnimation();
            TabTrayFragment.this.presenter.viewReady();
            return false;
         }
      });
   }

   public void refreshData(final List var1, Session var2) {
      DiffUtil.calculateDiff(new DiffUtil.Callback(this.adapter.getData()) {
         // $FF: synthetic field
         final List val$oldTabs;

         {
            this.val$oldTabs = var2;
         }

         public boolean areContentsTheSame(int var1x, int var2) {
            return true;
         }

         public boolean areItemsTheSame(int var1x, int var2) {
            return ((Session)var1.get(var2)).getId().equals(((Session)this.val$oldTabs.get(var1x)).getId());
         }

         public int getNewListSize() {
            return var1.size();
         }

         public int getOldListSize() {
            return this.val$oldTabs.size();
         }
      }, false).dispatchUpdatesTo((RecyclerView.Adapter)this.adapter);
      this.adapter.setData(var1);
      this.waitItemAnimation(new _$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc(this, var2));
   }

   public void refreshTabData(Session var1) {
      List var2 = this.adapter.getData();
      int var3 = var2.indexOf(var1);
      if (var3 >= 0 && var3 < var2.size()) {
         this.adapter.notifyItemChanged(var3);
      }

   }

   public void showFocusedTab(int var1) {
      this.layoutManager.scrollToPositionWithOffset(var1, this.recyclerView.getMeasuredHeight() / 2);
   }

   public void tabSwitched(int var1) {
      ScreenNavigator.get(this.getContext()).raiseBrowserScreen(false);
      this.postOnNextFrame(this.dismissRunnable);
   }

   private static class SlideAnimationCoordinator {
      private float backgroundAlpha = -1.0F;
      private Interpolator backgroundInterpolator = new AccelerateInterpolator();
      private int collapseHeight = -1;
      private TabTrayFragment fragment;
      private float overlayAlpha = -1.0F;
      private Interpolator overlayInterpolator = new AccelerateDecelerateInterpolator();
      private float translationY = -2.14748365E9F;

      SlideAnimationCoordinator(TabTrayFragment var1) {
         this.fragment = var1;
      }

      private void onSlide(float var1) {
         float var2 = 0.0F;
         float var3;
         float var4;
         if (var1 < 0.0F) {
            if (this.collapseHeight < 0) {
               this.collapseHeight = this.fragment.getCollapseHeight();
            }

            var2 = (float)this.collapseHeight;
            var3 = -var1;
            var4 = Math.max(0.0F, 1.0F - this.backgroundInterpolator.getInterpolation(var3));
            var2 *= var3;
            var3 = 0.0F;
         } else {
            var3 = -(this.overlayInterpolator.getInterpolation(1.0F - var1) * 0.5F) + 0.5F;
            var4 = 1.0F;
         }

         if (var1 >= 1.0F) {
            this.fragment.onFullyExpanded();
         }

         if (Float.compare(this.translationY, var2) != 0) {
            this.translationY = var2;
            this.fragment.onTranslateToHidden(var2);
         }

         if (Float.compare(this.backgroundAlpha, var4) != 0) {
            this.backgroundAlpha = var4;
            this.fragment.updateWindowBackground(var4);
         }

         if (Float.compare(this.overlayAlpha, var3) != 0) {
            this.overlayAlpha = var3;
            this.fragment.updateWindowOverlay(var3);
         }

      }
   }
}
