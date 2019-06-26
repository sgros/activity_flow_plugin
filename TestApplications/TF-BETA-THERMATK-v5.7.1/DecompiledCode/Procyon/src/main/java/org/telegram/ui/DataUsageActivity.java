// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.messenger.StatsController;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextSettingsCell;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.RecyclerListView;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.View$MeasureSpec;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.FrameLayout;
import android.view.ViewConfiguration;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.animation.AnimatorSet;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
import android.graphics.Paint;
import android.view.animation.Interpolator;
import org.telegram.ui.ActionBar.BaseFragment;

public class DataUsageActivity extends BaseFragment
{
    private static final Interpolator interpolator;
    private boolean animatingForward;
    private boolean backAnimation;
    private Paint backgroundPaint;
    private int maximumVelocity;
    private ListAdapter mobileAdapter;
    private ListAdapter roamingAdapter;
    private ScrollSlidingTextTabStrip scrollSlidingTextTabStrip;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    private ViewPage[] viewPages;
    private ListAdapter wifiAdapter;
    
    static {
        interpolator = (Interpolator)_$$Lambda$DataUsageActivity$wsORmtBp3T6D_3i_RtGAVFVpGzs.INSTANCE;
    }
    
    public DataUsageActivity() {
        this.backgroundPaint = new Paint();
        this.viewPages = new ViewPage[2];
    }
    
    private void setScrollY(final float translationY) {
        super.actionBar.setTranslationY(translationY);
        int n = 0;
        while (true) {
            final ViewPage[] viewPages = this.viewPages;
            if (n >= viewPages.length) {
                break;
            }
            viewPages[n].listView.setPinnedSectionOffsetY((int)translationY);
            ++n;
        }
        super.fragmentView.invalidate();
    }
    
    private void switchToCurrentSelectedMode(final boolean b) {
        int n = 0;
        ViewPage[] viewPages;
        while (true) {
            viewPages = this.viewPages;
            if (n >= viewPages.length) {
                break;
            }
            viewPages[n].listView.stopScroll();
            ++n;
        }
        final RecyclerView.Adapter adapter = viewPages[b].listView.getAdapter();
        this.viewPages[b].listView.setPinnedHeaderShadowDrawable(null);
        if (this.viewPages[b].selectedType == 0) {
            if (adapter != this.mobileAdapter) {
                this.viewPages[b].listView.setAdapter(this.mobileAdapter);
            }
        }
        else if (this.viewPages[b].selectedType == 1) {
            if (adapter != this.wifiAdapter) {
                this.viewPages[b].listView.setAdapter(this.wifiAdapter);
            }
        }
        else if (this.viewPages[b].selectedType == 2 && adapter != this.roamingAdapter) {
            this.viewPages[b].listView.setAdapter(this.roamingAdapter);
        }
        this.viewPages[b].listView.setVisibility(0);
        if (super.actionBar.getTranslationY() != 0.0f) {
            this.viewPages[b].layoutManager.scrollToPositionWithOffset(0, (int)super.actionBar.getTranslationY());
        }
    }
    
    private void updateTabs() {
        final ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = this.scrollSlidingTextTabStrip;
        if (scrollSlidingTextTabStrip == null) {
            return;
        }
        scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString("NetworkUsageMobile", 2131559890));
        this.scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString("NetworkUsageWiFi", 2131559893));
        this.scrollSlidingTextTabStrip.addTextTab(2, LocaleController.getString("NetworkUsageRoaming", 2131559891));
        this.scrollSlidingTextTabStrip.setVisibility(0);
        super.actionBar.setExtraHeight(AndroidUtilities.dp(44.0f));
        final int currentTabId = this.scrollSlidingTextTabStrip.getCurrentTabId();
        if (currentTabId >= 0) {
            this.viewPages[0].selectedType = currentTabId;
        }
        this.scrollSlidingTextTabStrip.finishAddingTabs();
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setTitle(LocaleController.getString("NetworkUsage", 2131559889));
        final boolean tablet = AndroidUtilities.isTablet();
        boolean swipeBackEnabled = false;
        if (tablet) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setExtraHeight(AndroidUtilities.dp(44.0f));
        super.actionBar.setAllowOverlayTitle(false);
        super.actionBar.setAddToContainer(false);
        super.actionBar.setClipContent(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    DataUsageActivity.this.finishFragment();
                }
            }
        });
        super.hasOwnBackground = true;
        this.mobileAdapter = new ListAdapter(context, 0);
        this.wifiAdapter = new ListAdapter(context, 1);
        this.roamingAdapter = new ListAdapter(context, 2);
        (this.scrollSlidingTextTabStrip = new ScrollSlidingTextTabStrip(context)).setUseSameWidth(true);
        super.actionBar.addView((View)this.scrollSlidingTextTabStrip, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 44, 83));
        this.scrollSlidingTextTabStrip.setDelegate((ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate)new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() {
            @Override
            public void onPageScrolled(final float n) {
                if (n == 1.0f && DataUsageActivity.this.viewPages[1].getVisibility() != 0) {
                    return;
                }
                if (DataUsageActivity.this.animatingForward) {
                    DataUsageActivity.this.viewPages[0].setTranslationX(-n * DataUsageActivity.this.viewPages[0].getMeasuredWidth());
                    DataUsageActivity.this.viewPages[1].setTranslationX(DataUsageActivity.this.viewPages[0].getMeasuredWidth() - DataUsageActivity.this.viewPages[0].getMeasuredWidth() * n);
                }
                else {
                    DataUsageActivity.this.viewPages[0].setTranslationX(DataUsageActivity.this.viewPages[0].getMeasuredWidth() * n);
                    DataUsageActivity.this.viewPages[1].setTranslationX(DataUsageActivity.this.viewPages[0].getMeasuredWidth() * n - DataUsageActivity.this.viewPages[0].getMeasuredWidth());
                }
                if (n == 1.0f) {
                    final ViewPage viewPage = DataUsageActivity.this.viewPages[0];
                    DataUsageActivity.this.viewPages[0] = DataUsageActivity.this.viewPages[1];
                    (DataUsageActivity.this.viewPages[1] = viewPage).setVisibility(8);
                }
            }
            
            @Override
            public void onPageSelected(final int n, final boolean b) {
                if (DataUsageActivity.this.viewPages[0].selectedType == n) {
                    return;
                }
                final DataUsageActivity this$0 = DataUsageActivity.this;
                this$0.swipeBackEnabled = (n == this$0.scrollSlidingTextTabStrip.getFirstTabId());
                DataUsageActivity.this.viewPages[1].selectedType = n;
                DataUsageActivity.this.viewPages[1].setVisibility(0);
                DataUsageActivity.this.switchToCurrentSelectedMode(true);
                DataUsageActivity.this.animatingForward = b;
            }
        });
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        final FrameLayout fragmentView = new FrameLayout(context) {
            private boolean globalIgnoreLayout;
            private boolean maybeStartTracking;
            private boolean startedTracking;
            private int startedTrackingPointerId;
            private int startedTrackingX;
            private int startedTrackingY;
            private VelocityTracker velocityTracker;
            
            private boolean prepareForMoving(final MotionEvent motionEvent, final boolean b) {
                final int nextPageId = DataUsageActivity.this.scrollSlidingTextTabStrip.getNextPageId(b);
                if (nextPageId < 0) {
                    return false;
                }
                this.getParent().requestDisallowInterceptTouchEvent(true);
                this.maybeStartTracking = false;
                this.startedTracking = true;
                this.startedTrackingX = (int)motionEvent.getX();
                DataUsageActivity.this.actionBar.setEnabled(false);
                DataUsageActivity.this.scrollSlidingTextTabStrip.setEnabled(false);
                DataUsageActivity.this.viewPages[1].selectedType = nextPageId;
                DataUsageActivity.this.viewPages[1].setVisibility(0);
                DataUsageActivity.this.animatingForward = b;
                DataUsageActivity.this.switchToCurrentSelectedMode(true);
                if (b) {
                    DataUsageActivity.this.viewPages[1].setTranslationX((float)DataUsageActivity.this.viewPages[0].getMeasuredWidth());
                }
                else {
                    DataUsageActivity.this.viewPages[1].setTranslationX((float)(-DataUsageActivity.this.viewPages[0].getMeasuredWidth()));
                }
                return true;
            }
            
            public boolean checkTabsAnimationInProgress() {
                if (DataUsageActivity.this.tabsAnimationInProgress) {
                    final boolean access$1600 = DataUsageActivity.this.backAnimation;
                    int n = -1;
                    final boolean b = true;
                    int n2 = 0;
                    Label_0201: {
                        if (access$1600) {
                            if (Math.abs(DataUsageActivity.this.viewPages[0].getTranslationX()) < 1.0f) {
                                DataUsageActivity.this.viewPages[0].setTranslationX(0.0f);
                                final ViewPage viewPage = DataUsageActivity.this.viewPages[1];
                                final int measuredWidth = DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                                if (DataUsageActivity.this.animatingForward) {
                                    n = 1;
                                }
                                viewPage.setTranslationX((float)(measuredWidth * n));
                                n2 = (b ? 1 : 0);
                                break Label_0201;
                            }
                        }
                        else if (Math.abs(DataUsageActivity.this.viewPages[1].getTranslationX()) < 1.0f) {
                            final ViewPage viewPage2 = DataUsageActivity.this.viewPages[0];
                            final int measuredWidth2 = DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                            if (!DataUsageActivity.this.animatingForward) {
                                n = 1;
                            }
                            viewPage2.setTranslationX((float)(measuredWidth2 * n));
                            DataUsageActivity.this.viewPages[1].setTranslationX(0.0f);
                            n2 = (b ? 1 : 0);
                            break Label_0201;
                        }
                        n2 = 0;
                    }
                    if (n2 != 0) {
                        if (DataUsageActivity.this.tabsAnimation != null) {
                            DataUsageActivity.this.tabsAnimation.cancel();
                            DataUsageActivity.this.tabsAnimation = null;
                        }
                        DataUsageActivity.this.tabsAnimationInProgress = false;
                    }
                    return DataUsageActivity.this.tabsAnimationInProgress;
                }
                return false;
            }
            
            protected void dispatchDraw(final Canvas canvas) {
                super.dispatchDraw(canvas);
                if (DataUsageActivity.this.parentLayout != null) {
                    DataUsageActivity.this.parentLayout.drawHeaderShadow(canvas, DataUsageActivity.this.actionBar.getMeasuredHeight() + (int)DataUsageActivity.this.actionBar.getTranslationY());
                }
            }
            
            public void forceHasOverlappingRendering(final boolean b) {
                super.forceHasOverlappingRendering(b);
            }
            
            protected void onDraw(final Canvas canvas) {
                DataUsageActivity.this.backgroundPaint.setColor(Theme.getColor("windowBackgroundGray"));
                canvas.drawRect(0.0f, DataUsageActivity.this.actionBar.getMeasuredHeight() + DataUsageActivity.this.actionBar.getTranslationY(), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), DataUsageActivity.this.backgroundPaint);
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                return this.checkTabsAnimationInProgress() || DataUsageActivity.this.scrollSlidingTextTabStrip.isAnimatingIndicator() || this.onTouchEvent(motionEvent);
            }
            
            protected void onMeasure(final int n, final int n2) {
                this.setMeasuredDimension(View$MeasureSpec.getSize(n), View$MeasureSpec.getSize(n2));
                this.measureChildWithMargins((View)DataUsageActivity.this.actionBar, n, 0, n2, 0);
                final int measuredHeight = DataUsageActivity.this.actionBar.getMeasuredHeight();
                this.globalIgnoreLayout = true;
                final int n3 = 0;
                for (int i = 0; i < DataUsageActivity.this.viewPages.length; ++i) {
                    if (DataUsageActivity.this.viewPages[i] != null) {
                        if (DataUsageActivity.this.viewPages[i].listView != null) {
                            DataUsageActivity.this.viewPages[i].listView.setPadding(0, measuredHeight, 0, AndroidUtilities.dp(4.0f));
                        }
                    }
                }
                this.globalIgnoreLayout = false;
                for (int childCount = this.getChildCount(), j = n3; j < childCount; ++j) {
                    final View child = this.getChildAt(j);
                    if (child != null && child.getVisibility() != 8) {
                        if (child != DataUsageActivity.this.actionBar) {
                            this.measureChildWithMargins(child, n, 0, n2, 0);
                        }
                    }
                }
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (!DataUsageActivity.this.parentLayout.checkTransitionAnimation() && !this.checkTabsAnimationInProgress()) {
                    final boolean b = true;
                    if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                        this.startedTrackingPointerId = motionEvent.getPointerId(0);
                        this.maybeStartTracking = true;
                        this.startedTrackingX = (int)motionEvent.getX();
                        this.startedTrackingY = (int)motionEvent.getY();
                        final VelocityTracker velocityTracker = this.velocityTracker;
                        if (velocityTracker != null) {
                            velocityTracker.clear();
                        }
                    }
                    else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                        if (this.velocityTracker == null) {
                            this.velocityTracker = VelocityTracker.obtain();
                        }
                        final int a = (int)(motionEvent.getX() - this.startedTrackingX);
                        final int abs = Math.abs((int)motionEvent.getY() - this.startedTrackingY);
                        this.velocityTracker.addMovement(motionEvent);
                        if (this.startedTracking && ((DataUsageActivity.this.animatingForward && a > 0) || (!DataUsageActivity.this.animatingForward && a < 0)) && !this.prepareForMoving(motionEvent, a < 0)) {
                            this.maybeStartTracking = true;
                            this.startedTracking = false;
                        }
                        if (this.maybeStartTracking && !this.startedTracking) {
                            if (Math.abs(a) >= AndroidUtilities.getPixelsInCM(0.3f, true) && Math.abs(a) / 3 > abs) {
                                this.prepareForMoving(motionEvent, a < 0 && b);
                            }
                        }
                        else if (this.startedTracking) {
                            if (DataUsageActivity.this.animatingForward) {
                                DataUsageActivity.this.viewPages[0].setTranslationX((float)a);
                                DataUsageActivity.this.viewPages[1].setTranslationX((float)(DataUsageActivity.this.viewPages[0].getMeasuredWidth() + a));
                            }
                            else {
                                DataUsageActivity.this.viewPages[0].setTranslationX((float)a);
                                DataUsageActivity.this.viewPages[1].setTranslationX((float)(a - DataUsageActivity.this.viewPages[0].getMeasuredWidth()));
                            }
                            DataUsageActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DataUsageActivity.this.viewPages[1].selectedType, Math.abs(a) / (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth());
                        }
                    }
                    else if (motionEvent != null && motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6)) {
                        if (this.velocityTracker == null) {
                            this.velocityTracker = VelocityTracker.obtain();
                        }
                        this.velocityTracker.computeCurrentVelocity(1000, (float)DataUsageActivity.this.maximumVelocity);
                        if (!this.startedTracking) {
                            final float xVelocity = this.velocityTracker.getXVelocity();
                            final float yVelocity = this.velocityTracker.getYVelocity();
                            if (Math.abs(xVelocity) >= 3000.0f && Math.abs(xVelocity) > Math.abs(yVelocity)) {
                                this.prepareForMoving(motionEvent, xVelocity < 0.0f);
                            }
                        }
                        if (this.startedTracking) {
                            final float x = DataUsageActivity.this.viewPages[0].getX();
                            DataUsageActivity.this.tabsAnimation = new AnimatorSet();
                            final float xVelocity2 = this.velocityTracker.getXVelocity();
                            final float yVelocity2 = this.velocityTracker.getYVelocity();
                            DataUsageActivity.this.backAnimation = (Math.abs(x) < DataUsageActivity.this.viewPages[0].getMeasuredWidth() / 3.0f && (Math.abs(xVelocity2) < 3500.0f || Math.abs(xVelocity2) < Math.abs(yVelocity2)));
                            float abs2;
                            if (DataUsageActivity.this.backAnimation) {
                                abs2 = Math.abs(x);
                                if (DataUsageActivity.this.animatingForward) {
                                    DataUsageActivity.this.tabsAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[] { (float)DataUsageActivity.this.viewPages[1].getMeasuredWidth() }) });
                                }
                                else {
                                    DataUsageActivity.this.tabsAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[] { (float)(-DataUsageActivity.this.viewPages[1].getMeasuredWidth()) }) });
                                }
                            }
                            else {
                                abs2 = DataUsageActivity.this.viewPages[0].getMeasuredWidth() - Math.abs(x);
                                if (DataUsageActivity.this.animatingForward) {
                                    DataUsageActivity.this.tabsAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[] { (float)(-DataUsageActivity.this.viewPages[0].getMeasuredWidth()) }), (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[] { 0.0f }) });
                                }
                                else {
                                    DataUsageActivity.this.tabsAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[] { (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() }), (Animator)ObjectAnimator.ofFloat((Object)DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[] { 0.0f }) });
                                }
                            }
                            DataUsageActivity.this.tabsAnimation.setInterpolator((TimeInterpolator)DataUsageActivity.interpolator);
                            final int measuredWidth = this.getMeasuredWidth();
                            final int n = measuredWidth / 2;
                            final float min = Math.min(1.0f, abs2 * 1.0f / measuredWidth);
                            final float n2 = (float)n;
                            final float distanceInfluenceForSnapDuration = AndroidUtilities.distanceInfluenceForSnapDuration(min);
                            final float abs3 = Math.abs(xVelocity2);
                            int a2;
                            if (abs3 > 0.0f) {
                                a2 = Math.round(Math.abs((n2 + distanceInfluenceForSnapDuration * n2) / abs3) * 1000.0f) * 4;
                            }
                            else {
                                a2 = (int)((abs2 / this.getMeasuredWidth() + 1.0f) * 100.0f);
                            }
                            DataUsageActivity.this.tabsAnimation.setDuration((long)Math.max(150, Math.min(a2, 600)));
                            DataUsageActivity.this.tabsAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationEnd(final Animator animator) {
                                    DataUsageActivity.this.tabsAnimation = null;
                                    if (DataUsageActivity.this.backAnimation) {
                                        DataUsageActivity.this.viewPages[1].setVisibility(8);
                                    }
                                    else {
                                        final ViewPage viewPage = DataUsageActivity.this.viewPages[0];
                                        DataUsageActivity.this.viewPages[0] = DataUsageActivity.this.viewPages[1];
                                        (DataUsageActivity.this.viewPages[1] = viewPage).setVisibility(8);
                                        final DataUsageActivity this$0 = DataUsageActivity.this;
                                        this$0.swipeBackEnabled = (this$0.viewPages[0].selectedType == DataUsageActivity.this.scrollSlidingTextTabStrip.getFirstTabId());
                                        DataUsageActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DataUsageActivity.this.viewPages[0].selectedType, 1.0f);
                                    }
                                    DataUsageActivity.this.tabsAnimationInProgress = false;
                                    FrameLayout.this.maybeStartTracking = false;
                                    FrameLayout.this.startedTracking = false;
                                    DataUsageActivity.this.actionBar.setEnabled(true);
                                    DataUsageActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                                }
                            });
                            DataUsageActivity.this.tabsAnimation.start();
                            DataUsageActivity.this.tabsAnimationInProgress = true;
                        }
                        else {
                            this.maybeStartTracking = false;
                            this.startedTracking = false;
                            DataUsageActivity.this.actionBar.setEnabled(true);
                            DataUsageActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                        }
                        final VelocityTracker velocityTracker2 = this.velocityTracker;
                        if (velocityTracker2 != null) {
                            velocityTracker2.recycle();
                            this.velocityTracker = null;
                        }
                    }
                    return this.startedTracking;
                }
                return false;
            }
            
            public void requestLayout() {
                if (this.globalIgnoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        ((FrameLayout)(super.fragmentView = (View)fragmentView)).setWillNotDraw(false);
        int n = 0;
        int n2 = -1;
        int n3 = 0;
        while (true) {
            final ViewPage[] viewPages = this.viewPages;
            if (n >= viewPages.length) {
                break;
            }
            int firstVisibleItemPosition = n2;
            int top = n3;
            Label_0381: {
                if (n == 0) {
                    firstVisibleItemPosition = n2;
                    top = n3;
                    if (viewPages[n] != null) {
                        firstVisibleItemPosition = n2;
                        top = n3;
                        if (viewPages[n].layoutManager != null) {
                            firstVisibleItemPosition = this.viewPages[n].layoutManager.findFirstVisibleItemPosition();
                            if (firstVisibleItemPosition != ((RecyclerView.LayoutManager)this.viewPages[n].layoutManager).getItemCount() - 1) {
                                final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.viewPages[n].listView.findViewHolderForAdapterPosition(firstVisibleItemPosition);
                                if (holder != null) {
                                    top = holder.itemView.getTop();
                                    break Label_0381;
                                }
                            }
                            firstVisibleItemPosition = -1;
                            top = n3;
                        }
                    }
                }
            }
            final ViewPage viewPage = new ViewPage(context) {
                public void setTranslationX(float translationX) {
                    super.setTranslationX(translationX);
                    if (DataUsageActivity.this.tabsAnimationInProgress && DataUsageActivity.this.viewPages[0] == this) {
                        translationX = Math.abs(DataUsageActivity.this.viewPages[0].getTranslationX()) / DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                        DataUsageActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DataUsageActivity.this.viewPages[1].selectedType, translationX);
                    }
                }
            };
            fragmentView.addView((View)viewPage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            final ViewPage[] viewPages2 = this.viewPages;
            viewPages2[n] = (ViewPage)viewPage;
            final ViewPage viewPage2 = viewPages2[n];
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context, 1, false) {
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            };
            viewPage2.layoutManager = layoutManager;
            final RecyclerListView recyclerListView = new RecyclerListView(context);
            this.viewPages[n].listView = recyclerListView;
            this.viewPages[n].listView.setItemAnimator(null);
            this.viewPages[n].listView.setClipToPadding(false);
            this.viewPages[n].listView.setSectionsType(2);
            this.viewPages[n].listView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
            final ViewPage[] viewPages3 = this.viewPages;
            viewPages3[n].addView((View)viewPages3[n].listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.viewPages[n].listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$DataUsageActivity$NwaR7lgfeGPKETAnvYA4hx0PZOY(this, recyclerListView));
            this.viewPages[n].listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, int currentActionBarHeight) {
                    if (currentActionBarHeight != 1) {
                        final int n = (int)(-DataUsageActivity.this.actionBar.getTranslationY());
                        currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                        if (n != 0 && n != currentActionBarHeight) {
                            if (n < currentActionBarHeight / 2) {
                                DataUsageActivity.this.viewPages[0].listView.smoothScrollBy(0, -n);
                            }
                            else {
                                DataUsageActivity.this.viewPages[0].listView.smoothScrollBy(0, currentActionBarHeight - n);
                            }
                        }
                    }
                }
                
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                    if (recyclerView == DataUsageActivity.this.viewPages[0].listView) {
                        final float translationY = DataUsageActivity.this.actionBar.getTranslationY();
                        final float n3 = translationY - n2;
                        float n4;
                        if (n3 < -ActionBar.getCurrentActionBarHeight()) {
                            n4 = (float)(-ActionBar.getCurrentActionBarHeight());
                        }
                        else {
                            n4 = n3;
                            if (n3 > 0.0f) {
                                n4 = 0.0f;
                            }
                        }
                        if (n4 != translationY) {
                            DataUsageActivity.this.setScrollY(n4);
                        }
                    }
                }
            });
            if (n == 0 && firstVisibleItemPosition != -1) {
                layoutManager.scrollToPositionWithOffset(firstVisibleItemPosition, top);
            }
            if (n != 0) {
                this.viewPages[n].setVisibility(8);
            }
            ++n;
            n2 = firstVisibleItemPosition;
            n3 = top;
        }
        fragmentView.addView((View)super.actionBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        this.updateTabs();
        this.switchToCurrentSelectedMode(false);
        if (this.scrollSlidingTextTabStrip.getCurrentTabId() == this.scrollSlidingTextTabStrip.getFirstTabId()) {
            swipeBackEnabled = true;
        }
        super.swipeBackEnabled = swipeBackEnabled;
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
        list.add(new ThemeDescription(super.fragmentView, 0, null, null, null, null, "windowBackgroundGray"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        list.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { TextView.class }, null, null, null, "actionBarTabActiveText"));
        list.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { TextView.class }, null, null, null, "actionBarTabUnactiveText"));
        list.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[] { TextView.class }, null, null, null, "actionBarTabLine"));
        list.add(new ThemeDescription(null, 0, null, null, new Drawable[] { this.scrollSlidingTextTabStrip.getSelectorDrawable() }, null, "actionBarTabSelector"));
        int n = 0;
        while (true) {
            final ViewPage[] viewPages = this.viewPages;
            if (n >= viewPages.length) {
                break;
            }
            list.add(new ThemeDescription((View)viewPages[n].listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, HeaderCell.class }, null, null, null, "windowBackgroundWhite"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"));
            list.add(new ThemeDescription((View)this.viewPages[n].listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText2"));
            ++n;
        }
        return list.toArray(new ThemeDescription[0]);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter mobileAdapter = this.mobileAdapter;
        if (mobileAdapter != null) {
            ((RecyclerView.Adapter)mobileAdapter).notifyDataSetChanged();
        }
        final ListAdapter wifiAdapter = this.wifiAdapter;
        if (wifiAdapter != null) {
            ((RecyclerView.Adapter)wifiAdapter).notifyDataSetChanged();
        }
        final ListAdapter roamingAdapter = this.roamingAdapter;
        if (roamingAdapter != null) {
            ((RecyclerView.Adapter)roamingAdapter).notifyDataSetChanged();
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private int audiosBytesReceivedRow;
        private int audiosBytesSentRow;
        private int audiosReceivedRow;
        private int audiosSection2Row;
        private int audiosSectionRow;
        private int audiosSentRow;
        private int callsBytesReceivedRow;
        private int callsBytesSentRow;
        private int callsReceivedRow;
        private int callsSection2Row;
        private int callsSectionRow;
        private int callsSentRow;
        private int callsTotalTimeRow;
        private int currentType;
        private int filesBytesReceivedRow;
        private int filesBytesSentRow;
        private int filesReceivedRow;
        private int filesSection2Row;
        private int filesSectionRow;
        private int filesSentRow;
        private Context mContext;
        private int messagesBytesReceivedRow;
        private int messagesBytesSentRow;
        private int messagesReceivedRow;
        private int messagesSection2Row;
        private int messagesSectionRow;
        private int messagesSentRow;
        private int photosBytesReceivedRow;
        private int photosBytesSentRow;
        private int photosReceivedRow;
        private int photosSection2Row;
        private int photosSectionRow;
        private int photosSentRow;
        private int resetRow;
        private int resetSection2Row;
        private int rowCount;
        private int totalBytesReceivedRow;
        private int totalBytesSentRow;
        private int totalSection2Row;
        private int totalSectionRow;
        private int videosBytesReceivedRow;
        private int videosBytesSentRow;
        private int videosReceivedRow;
        private int videosSection2Row;
        private int videosSectionRow;
        private int videosSentRow;
        
        public ListAdapter(final Context mContext, int n) {
            this.mContext = mContext;
            this.currentType = n;
            this.rowCount = 0;
            n = this.rowCount++;
            this.photosSectionRow = n;
            n = this.rowCount++;
            this.photosSentRow = n;
            n = this.rowCount++;
            this.photosReceivedRow = n;
            n = this.rowCount++;
            this.photosBytesSentRow = n;
            n = this.rowCount++;
            this.photosBytesReceivedRow = n;
            n = this.rowCount++;
            this.photosSection2Row = n;
            n = this.rowCount++;
            this.videosSectionRow = n;
            n = this.rowCount++;
            this.videosSentRow = n;
            n = this.rowCount++;
            this.videosReceivedRow = n;
            n = this.rowCount++;
            this.videosBytesSentRow = n;
            n = this.rowCount++;
            this.videosBytesReceivedRow = n;
            n = this.rowCount++;
            this.videosSection2Row = n;
            n = this.rowCount++;
            this.audiosSectionRow = n;
            n = this.rowCount++;
            this.audiosSentRow = n;
            n = this.rowCount++;
            this.audiosReceivedRow = n;
            n = this.rowCount++;
            this.audiosBytesSentRow = n;
            n = this.rowCount++;
            this.audiosBytesReceivedRow = n;
            n = this.rowCount++;
            this.audiosSection2Row = n;
            n = this.rowCount++;
            this.filesSectionRow = n;
            n = this.rowCount++;
            this.filesSentRow = n;
            n = this.rowCount++;
            this.filesReceivedRow = n;
            n = this.rowCount++;
            this.filesBytesSentRow = n;
            n = this.rowCount++;
            this.filesBytesReceivedRow = n;
            n = this.rowCount++;
            this.filesSection2Row = n;
            n = this.rowCount++;
            this.callsSectionRow = n;
            n = this.rowCount++;
            this.callsSentRow = n;
            n = this.rowCount++;
            this.callsReceivedRow = n;
            n = this.rowCount++;
            this.callsBytesSentRow = n;
            n = this.rowCount++;
            this.callsBytesReceivedRow = n;
            n = this.rowCount++;
            this.callsTotalTimeRow = n;
            n = this.rowCount++;
            this.callsSection2Row = n;
            n = this.rowCount++;
            this.messagesSectionRow = n;
            this.messagesSentRow = -1;
            this.messagesReceivedRow = -1;
            n = this.rowCount++;
            this.messagesBytesSentRow = n;
            n = this.rowCount++;
            this.messagesBytesReceivedRow = n;
            n = this.rowCount++;
            this.messagesSection2Row = n;
            n = this.rowCount++;
            this.totalSectionRow = n;
            n = this.rowCount++;
            this.totalBytesSentRow = n;
            n = this.rowCount++;
            this.totalBytesReceivedRow = n;
            n = this.rowCount++;
            this.totalSection2Row = n;
            n = this.rowCount++;
            this.resetRow = n;
            n = this.rowCount++;
            this.resetSection2Row = n;
        }
        
        @Override
        public int getItemCount() {
            return this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == this.resetSection2Row) {
                return 3;
            }
            if (n == this.callsSection2Row || n == this.filesSection2Row || n == this.audiosSection2Row || n == this.videosSection2Row || n == this.photosSection2Row || n == this.messagesSection2Row || n == this.totalSection2Row) {
                return 0;
            }
            if (n != this.totalSectionRow && n != this.callsSectionRow && n != this.filesSectionRow && n != this.audiosSectionRow && n != this.videosSectionRow && n != this.photosSectionRow && n != this.messagesSectionRow) {
                return 1;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() == this.resetRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                boolean b = false;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                            textInfoPrivacyCell.setText(LocaleController.formatString("NetworkUsageSince", 2131559892, LocaleController.getInstance().formatterStats.format(StatsController.getInstance(DataUsageActivity.this.currentAccount).getResetStatsDate(this.currentType))));
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (i == this.totalSectionRow) {
                            headerCell.setText(LocaleController.getString("TotalDataUsage", 2131560915));
                        }
                        else if (i == this.callsSectionRow) {
                            headerCell.setText(LocaleController.getString("CallsDataUsage", 2131558889));
                        }
                        else if (i == this.filesSectionRow) {
                            headerCell.setText(LocaleController.getString("FilesDataUsage", 2131559483));
                        }
                        else if (i == this.audiosSectionRow) {
                            headerCell.setText(LocaleController.getString("LocalAudioCache", 2131559770));
                        }
                        else if (i == this.videosSectionRow) {
                            headerCell.setText(LocaleController.getString("LocalVideoCache", 2131559779));
                        }
                        else if (i == this.photosSectionRow) {
                            headerCell.setText(LocaleController.getString("LocalPhotoCache", 2131559778));
                        }
                        else if (i == this.messagesSectionRow) {
                            headerCell.setText(LocaleController.getString("MessagesDataUsage", 2131559855));
                        }
                    }
                }
                else {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    if (i == this.resetRow) {
                        textSettingsCell.setTag((Object)"windowBackgroundWhiteRedText2");
                        textSettingsCell.setText(LocaleController.getString("ResetStatistics", 2131560604), false);
                        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText2"));
                    }
                    else {
                        textSettingsCell.setTag((Object)"windowBackgroundWhiteBlackText");
                        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                        int n;
                        if (i != this.callsSentRow && i != this.callsReceivedRow && i != this.callsBytesSentRow && i != this.callsBytesReceivedRow) {
                            if (i != this.messagesSentRow && i != this.messagesReceivedRow && i != this.messagesBytesSentRow && i != this.messagesBytesReceivedRow) {
                                if (i != this.photosSentRow && i != this.photosReceivedRow && i != this.photosBytesSentRow && i != this.photosBytesReceivedRow) {
                                    if (i != this.audiosSentRow && i != this.audiosReceivedRow && i != this.audiosBytesSentRow && i != this.audiosBytesReceivedRow) {
                                        if (i != this.videosSentRow && i != this.videosReceivedRow && i != this.videosBytesSentRow && i != this.videosBytesReceivedRow) {
                                            if (i != this.filesSentRow && i != this.filesReceivedRow && i != this.filesBytesSentRow && i != this.filesBytesReceivedRow) {
                                                n = 6;
                                            }
                                            else {
                                                n = 5;
                                            }
                                        }
                                        else {
                                            n = 2;
                                        }
                                    }
                                    else {
                                        n = 3;
                                    }
                                }
                                else {
                                    n = 4;
                                }
                            }
                            else {
                                n = 1;
                            }
                        }
                        else {
                            n = 0;
                        }
                        if (i == this.callsSentRow) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("OutgoingCalls", 2131560133), String.format("%d", StatsController.getInstance(DataUsageActivity.this.currentAccount).getSentItemsCount(this.currentType, n)), true);
                        }
                        else if (i == this.callsReceivedRow) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("IncomingCalls", 2131559662), String.format("%d", StatsController.getInstance(DataUsageActivity.this.currentAccount).getRecivedItemsCount(this.currentType, n)), true);
                        }
                        else if (i == this.callsTotalTimeRow) {
                            final int callsTotalTime = StatsController.getInstance(DataUsageActivity.this.currentAccount).getCallsTotalTime(this.currentType);
                            i = callsTotalTime / 3600;
                            final int n2 = callsTotalTime - i * 3600;
                            final int n3 = n2 / 60;
                            final int n4 = n2 - n3 * 60;
                            String s;
                            if (i != 0) {
                                s = String.format("%d:%02d:%02d", i, n3, n4);
                            }
                            else {
                                s = String.format("%d:%02d", n3, n4);
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString("CallsTotalTime", 2131558890), s, false);
                        }
                        else if (i != this.messagesSentRow && i != this.photosSentRow && i != this.videosSentRow && i != this.audiosSentRow && i != this.filesSentRow) {
                            if (i != this.messagesReceivedRow && i != this.photosReceivedRow && i != this.videosReceivedRow && i != this.audiosReceivedRow && i != this.filesReceivedRow) {
                                if (i != this.messagesBytesSentRow && i != this.photosBytesSentRow && i != this.videosBytesSentRow && i != this.audiosBytesSentRow && i != this.filesBytesSentRow && i != this.callsBytesSentRow && i != this.totalBytesSentRow) {
                                    if (i == this.messagesBytesReceivedRow || i == this.photosBytesReceivedRow || i == this.videosBytesReceivedRow || i == this.audiosBytesReceivedRow || i == this.filesBytesReceivedRow || i == this.callsBytesReceivedRow || i == this.totalBytesReceivedRow) {
                                        final String string = LocaleController.getString("BytesReceived", 2131558864);
                                        final String formatFileSize = AndroidUtilities.formatFileSize(StatsController.getInstance(DataUsageActivity.this.currentAccount).getReceivedBytesCount(this.currentType, n));
                                        if (i != this.totalBytesReceivedRow) {
                                            b = true;
                                        }
                                        textSettingsCell.setTextAndValue(string, formatFileSize, b);
                                    }
                                }
                                else {
                                    textSettingsCell.setTextAndValue(LocaleController.getString("BytesSent", 2131558865), AndroidUtilities.formatFileSize(StatsController.getInstance(DataUsageActivity.this.currentAccount).getSentBytesCount(this.currentType, n)), true);
                                }
                            }
                            else {
                                textSettingsCell.setTextAndValue(LocaleController.getString("CountReceived", 2131559165), String.format("%d", StatsController.getInstance(DataUsageActivity.this.currentAccount).getRecivedItemsCount(this.currentType, n)), true);
                            }
                        }
                        else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("CountSent", 2131559166), String.format("%d", StatsController.getInstance(DataUsageActivity.this.currentAccount).getSentItemsCount(this.currentType, n)), true);
                        }
                    }
                }
            }
            else if (i == this.resetSection2Row) {
                viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
            else {
                viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            o = null;
                        }
                        else {
                            o = new TextInfoPrivacyCell(this.mContext);
                        }
                    }
                    else {
                        o = new HeaderCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    o = new TextSettingsCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                o = new ShadowSectionCell(this.mContext);
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class ViewPage extends FrameLayout
    {
        private LinearLayoutManager layoutManager;
        private ListAdapter listAdapter;
        private RecyclerListView listView;
        private int selectedType;
        
        public ViewPage(final Context context) {
            super(context);
        }
    }
}
