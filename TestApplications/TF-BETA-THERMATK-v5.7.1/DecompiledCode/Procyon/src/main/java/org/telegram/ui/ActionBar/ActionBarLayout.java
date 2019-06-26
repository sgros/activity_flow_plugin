// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.graphics.Rect;
import android.widget.LinearLayout;
import android.content.Intent;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.Color;
import android.annotation.TargetApi;
import android.graphics.Outline;
import android.app.Dialog;
import android.content.res.Configuration;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.view.ViewOutlineProvider;
import java.util.Iterator;
import android.widget.FrameLayout$LayoutParams;
import androidx.annotation.Keep;
import android.view.Menu;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.animation.TimeInterpolator;
import java.util.Collection;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import android.animation.ObjectAnimator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.MotionEvent;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.drawable.ColorDrawable;
import android.app.Activity;
import java.util.ArrayList;
import android.view.animation.DecelerateInterpolator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class ActionBarLayout extends FrameLayout
{
    private static Drawable headerShadowDrawable;
    private static Drawable layerShadowDrawable;
    private static Paint scrimPaint;
    private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
    private int[][] animateEndColors;
    private Theme.ThemeInfo animateSetThemeAfterAnimation;
    private boolean animateSetThemeNightAfterAnimation;
    private int[][] animateStartColors;
    private boolean animateThemeAfterAnimation;
    protected boolean animationInProgress;
    private float animationProgress;
    private Runnable animationRunnable;
    private View backgroundView;
    private boolean beginTrackingSent;
    private LinearLayoutContainer containerView;
    private LinearLayoutContainer containerViewBack;
    private ActionBar currentActionBar;
    private AnimatorSet currentAnimation;
    private DecelerateInterpolator decelerateInterpolator;
    private Runnable delayedOpenAnimationRunnable;
    private ActionBarLayoutDelegate delegate;
    private DrawerLayoutContainer drawerLayoutContainer;
    public ArrayList<BaseFragment> fragmentsStack;
    private boolean inActionMode;
    private boolean inPreviewMode;
    public float innerTranslationX;
    private long lastFrameTime;
    private boolean maybeStartTracking;
    private Runnable onCloseAnimationEndRunnable;
    private Runnable onOpenAnimationEndRunnable;
    private Runnable overlayAction;
    protected Activity parentActivity;
    private ColorDrawable previewBackgroundDrawable;
    private boolean rebuildAfterAnimation;
    private boolean rebuildLastAfterAnimation;
    private boolean removeActionBarExtraHeight;
    private boolean showLastAfterAnimation;
    protected boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private float themeAnimationValue;
    private ThemeDescription.ThemeDescriptionDelegate[] themeAnimatorDelegate;
    private ThemeDescription[][] themeAnimatorDescriptions;
    private AnimatorSet themeAnimatorSet;
    private String titleOverlayText;
    private int titleOverlayTextId;
    private boolean transitionAnimationInProgress;
    private boolean transitionAnimationPreviewMode;
    private long transitionAnimationStartTime;
    private boolean useAlphaAnimations;
    private VelocityTracker velocityTracker;
    private Runnable waitingForKeyboardCloseRunnable;
    
    public ActionBarLayout(final Context context) {
        super(context);
        this.decelerateInterpolator = new DecelerateInterpolator(1.5f);
        this.accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        this.animateStartColors = new int[2][];
        this.animateEndColors = new int[2][];
        this.themeAnimatorDescriptions = new ThemeDescription[2][];
        this.themeAnimatorDelegate = new ThemeDescription.ThemeDescriptionDelegate[2];
        this.parentActivity = (Activity)context;
        if (ActionBarLayout.layerShadowDrawable == null) {
            ActionBarLayout.layerShadowDrawable = this.getResources().getDrawable(2131165521);
            ActionBarLayout.headerShadowDrawable = this.getResources().getDrawable(2131165407).mutate();
            ActionBarLayout.scrimPaint = new Paint();
        }
    }
    
    private void checkNeedRebuild() {
        if (this.rebuildAfterAnimation) {
            this.rebuildAllFragmentViews(this.rebuildLastAfterAnimation, this.showLastAfterAnimation);
            this.rebuildAfterAnimation = false;
        }
        else if (this.animateThemeAfterAnimation) {
            this.animateThemedValues(this.animateSetThemeAfterAnimation, this.animateSetThemeNightAfterAnimation);
            this.animateSetThemeAfterAnimation = null;
            this.animateThemeAfterAnimation = false;
        }
    }
    
    private void closeLastFragmentInternalRemoveOld(final BaseFragment o) {
        o.onPause();
        o.onFragmentDestroy();
        o.setParentLayout(null);
        this.fragmentsStack.remove(o);
        this.containerViewBack.setVisibility(8);
        this.bringChildToFront((View)this.containerView);
    }
    
    private void onAnimationEndCheck(final boolean b) {
        this.onCloseAnimationEnd();
        this.onOpenAnimationEnd();
        final Runnable waitingForKeyboardCloseRunnable = this.waitingForKeyboardCloseRunnable;
        if (waitingForKeyboardCloseRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(waitingForKeyboardCloseRunnable);
            this.waitingForKeyboardCloseRunnable = null;
        }
        final AnimatorSet currentAnimation = this.currentAnimation;
        if (currentAnimation != null) {
            if (b) {
                currentAnimation.cancel();
            }
            this.currentAnimation = null;
        }
        final Runnable animationRunnable = this.animationRunnable;
        if (animationRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(animationRunnable);
            this.animationRunnable = null;
        }
        this.setAlpha(1.0f);
        this.containerView.setAlpha(1.0f);
        this.containerView.setScaleX(1.0f);
        this.containerView.setScaleY(1.0f);
        this.containerViewBack.setAlpha(1.0f);
        this.containerViewBack.setScaleX(1.0f);
        this.containerViewBack.setScaleY(1.0f);
    }
    
    private void onCloseAnimationEnd() {
        if (this.transitionAnimationInProgress) {
            final Runnable onCloseAnimationEndRunnable = this.onCloseAnimationEndRunnable;
            if (onCloseAnimationEndRunnable != null) {
                this.transitionAnimationInProgress = false;
                this.transitionAnimationPreviewMode = false;
                this.transitionAnimationStartTime = 0L;
                onCloseAnimationEndRunnable.run();
                this.onCloseAnimationEndRunnable = null;
                this.checkNeedRebuild();
            }
        }
    }
    
    private void onOpenAnimationEnd() {
        if (this.transitionAnimationInProgress) {
            final Runnable onOpenAnimationEndRunnable = this.onOpenAnimationEndRunnable;
            if (onOpenAnimationEndRunnable != null) {
                this.transitionAnimationInProgress = false;
                this.transitionAnimationPreviewMode = false;
                this.transitionAnimationStartTime = 0L;
                onOpenAnimationEndRunnable.run();
                this.onOpenAnimationEndRunnable = null;
                this.checkNeedRebuild();
            }
        }
    }
    
    private void onSlideAnimationEnd(final boolean b) {
        if (!b) {
            if (this.fragmentsStack.size() < 2) {
                return;
            }
            final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
            final BaseFragment baseFragment = fragmentsStack.get(fragmentsStack.size() - 1);
            baseFragment.onPause();
            baseFragment.onFragmentDestroy();
            baseFragment.setParentLayout(null);
            final ArrayList<BaseFragment> fragmentsStack2 = this.fragmentsStack;
            fragmentsStack2.remove(fragmentsStack2.size() - 1);
            final LinearLayoutContainer containerView = this.containerView;
            this.containerView = this.containerViewBack;
            this.containerViewBack = containerView;
            this.bringChildToFront((View)this.containerView);
            final ArrayList<BaseFragment> fragmentsStack3 = this.fragmentsStack;
            final BaseFragment baseFragment2 = fragmentsStack3.get(fragmentsStack3.size() - 1);
            this.currentActionBar = baseFragment2.actionBar;
            baseFragment2.onResume();
            baseFragment2.onBecomeFullyVisible();
        }
        else if (this.fragmentsStack.size() >= 2) {
            final ArrayList<BaseFragment> fragmentsStack4 = this.fragmentsStack;
            final BaseFragment baseFragment3 = fragmentsStack4.get(fragmentsStack4.size() - 2);
            baseFragment3.onPause();
            final View fragmentView = baseFragment3.fragmentView;
            if (fragmentView != null) {
                final ViewGroup viewGroup = (ViewGroup)fragmentView.getParent();
                if (viewGroup != null) {
                    baseFragment3.onRemoveFromParent();
                    viewGroup.removeView(baseFragment3.fragmentView);
                }
            }
            final ActionBar actionBar = baseFragment3.actionBar;
            if (actionBar != null && actionBar.getAddToContainer()) {
                final ViewGroup viewGroup2 = (ViewGroup)baseFragment3.actionBar.getParent();
                if (viewGroup2 != null) {
                    viewGroup2.removeView((View)baseFragment3.actionBar);
                }
            }
        }
        this.containerViewBack.setVisibility(8);
        this.startedTracking = false;
        this.animationInProgress = false;
        this.containerView.setTranslationX(0.0f);
        this.containerViewBack.setTranslationX(0.0f);
        this.setInnerTranslationX(0.0f);
    }
    
    private void prepareForMoving(final MotionEvent motionEvent) {
        this.maybeStartTracking = false;
        this.startedTracking = true;
        this.startedTrackingX = (int)motionEvent.getX();
        this.containerViewBack.setVisibility(0);
        this.beginTrackingSent = false;
        final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
        final BaseFragment baseFragment = fragmentsStack.get(fragmentsStack.size() - 2);
        View view;
        if ((view = baseFragment.fragmentView) == null) {
            view = baseFragment.createView((Context)this.parentActivity);
        }
        final ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup != null) {
            baseFragment.onRemoveFromParent();
            viewGroup.removeView(view);
        }
        final ActionBar actionBar = baseFragment.actionBar;
        if (actionBar != null && actionBar.getAddToContainer()) {
            final ViewGroup viewGroup2 = (ViewGroup)baseFragment.actionBar.getParent();
            if (viewGroup2 != null) {
                viewGroup2.removeView((View)baseFragment.actionBar);
            }
            if (this.removeActionBarExtraHeight) {
                baseFragment.actionBar.setOccupyStatusBar(false);
            }
            this.containerViewBack.addView((View)baseFragment.actionBar);
            baseFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
        }
        this.containerViewBack.addView(view);
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)view.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        layoutParams.topMargin = 0;
        view.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        if (!baseFragment.hasOwnBackground && view.getBackground() == null) {
            view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        }
        baseFragment.onResume();
    }
    
    private void presentFragmentInternalRemoveOld(final boolean b, final BaseFragment o) {
        if (o == null) {
            return;
        }
        o.onBecomeFullyHidden();
        o.onPause();
        if (b) {
            o.onFragmentDestroy();
            o.setParentLayout(null);
            this.fragmentsStack.remove(o);
        }
        else {
            final View fragmentView = o.fragmentView;
            if (fragmentView != null) {
                final ViewGroup viewGroup = (ViewGroup)fragmentView.getParent();
                if (viewGroup != null) {
                    o.onRemoveFromParent();
                    viewGroup.removeView(o.fragmentView);
                }
            }
            final ActionBar actionBar = o.actionBar;
            if (actionBar != null && actionBar.getAddToContainer()) {
                final ViewGroup viewGroup2 = (ViewGroup)o.actionBar.getParent();
                if (viewGroup2 != null) {
                    viewGroup2.removeView((View)o.actionBar);
                }
            }
        }
        this.containerViewBack.setVisibility(8);
    }
    
    private void removeFragmentFromStackInternal(final BaseFragment o) {
        o.onPause();
        o.onFragmentDestroy();
        o.setParentLayout(null);
        this.fragmentsStack.remove(o);
    }
    
    private void startLayoutAnimation(final boolean b, final boolean b2, final boolean b3) {
        if (b2) {
            this.animationProgress = 0.0f;
            this.lastFrameTime = System.nanoTime() / 1000000L;
        }
        AndroidUtilities.runOnUIThread(this.animationRunnable = new Runnable() {
            @Override
            public void run() {
                if (ActionBarLayout.this.animationRunnable != this) {
                    return;
                }
                ActionBarLayout.this.animationRunnable = null;
                if (b2) {
                    ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
                }
                final long n = System.nanoTime() / 1000000L;
                long n2;
                if ((n2 = n - ActionBarLayout.this.lastFrameTime) > 18L) {
                    n2 = 18L;
                }
                ActionBarLayout.this.lastFrameTime = n;
                final ActionBarLayout this$0 = ActionBarLayout.this;
                this$0.animationProgress += n2 / 150.0f;
                if (ActionBarLayout.this.animationProgress > 1.0f) {
                    ActionBarLayout.this.animationProgress = 1.0f;
                }
                final float interpolation = ActionBarLayout.this.decelerateInterpolator.getInterpolation(ActionBarLayout.this.animationProgress);
                if (b) {
                    ActionBarLayout.this.containerView.setAlpha(interpolation);
                    if (b3) {
                        final LinearLayoutContainer access$200 = ActionBarLayout.this.containerView;
                        final float n3 = 0.1f * interpolation + 0.9f;
                        access$200.setScaleX(n3);
                        ActionBarLayout.this.containerView.setScaleY(n3);
                        ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int)(128.0f * interpolation));
                        Theme.moveUpDrawable.setAlpha((int)(interpolation * 255.0f));
                        ActionBarLayout.this.containerView.invalidate();
                        ActionBarLayout.this.invalidate();
                    }
                    else {
                        ActionBarLayout.this.containerView.setTranslationX(AndroidUtilities.dp(48.0f) * (1.0f - interpolation));
                    }
                }
                else {
                    final LinearLayoutContainer access$201 = ActionBarLayout.this.containerViewBack;
                    final float alpha = 1.0f - interpolation;
                    access$201.setAlpha(alpha);
                    if (b3) {
                        final LinearLayoutContainer access$202 = ActionBarLayout.this.containerViewBack;
                        final float n4 = 0.1f * alpha + 0.9f;
                        access$202.setScaleX(n4);
                        ActionBarLayout.this.containerViewBack.setScaleY(n4);
                        ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int)(128.0f * alpha));
                        Theme.moveUpDrawable.setAlpha((int)(alpha * 255.0f));
                        ActionBarLayout.this.containerView.invalidate();
                        ActionBarLayout.this.invalidate();
                    }
                    else {
                        ActionBarLayout.this.containerViewBack.setTranslationX(AndroidUtilities.dp(48.0f) * interpolation);
                    }
                }
                if (ActionBarLayout.this.animationProgress < 1.0f) {
                    ActionBarLayout.this.startLayoutAnimation(b, false, b3);
                }
                else {
                    ActionBarLayout.this.onAnimationEndCheck(false);
                }
            }
        });
    }
    
    public boolean addFragmentToStack(final BaseFragment baseFragment) {
        return this.addFragmentToStack(baseFragment, -1);
    }
    
    public boolean addFragmentToStack(final BaseFragment baseFragment, final int index) {
        final ActionBarLayoutDelegate delegate = this.delegate;
        if ((delegate != null && !delegate.needAddFragmentToStack(baseFragment, this)) || !baseFragment.onFragmentCreate()) {
            return false;
        }
        baseFragment.setParentLayout(this);
        if (index == -1) {
            if (!this.fragmentsStack.isEmpty()) {
                final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                final BaseFragment baseFragment2 = fragmentsStack.get(fragmentsStack.size() - 1);
                baseFragment2.onPause();
                final ActionBar actionBar = baseFragment2.actionBar;
                if (actionBar != null && actionBar.getAddToContainer()) {
                    final ViewGroup viewGroup = (ViewGroup)baseFragment2.actionBar.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView((View)baseFragment2.actionBar);
                    }
                }
                final View fragmentView = baseFragment2.fragmentView;
                if (fragmentView != null) {
                    final ViewGroup viewGroup2 = (ViewGroup)fragmentView.getParent();
                    if (viewGroup2 != null) {
                        baseFragment2.onRemoveFromParent();
                        viewGroup2.removeView(baseFragment2.fragmentView);
                    }
                }
            }
            this.fragmentsStack.add(baseFragment);
        }
        else {
            this.fragmentsStack.add(index, baseFragment);
        }
        return true;
    }
    
    public void animateThemedValues(final Theme.ThemeInfo animateSetThemeAfterAnimation, final boolean animateSetThemeNightAfterAnimation) {
        if (!this.transitionAnimationInProgress && !this.startedTracking) {
            final AnimatorSet themeAnimatorSet = this.themeAnimatorSet;
            if (themeAnimatorSet != null) {
                themeAnimatorSet.cancel();
                this.themeAnimatorSet = null;
            }
            int i = 0;
            boolean b = false;
            while (i < 2) {
                Label_0327: {
                    BaseFragment lastFragment;
                    if (i == 0) {
                        lastFragment = this.getLastFragment();
                    }
                    else {
                        if ((!this.inPreviewMode && !this.transitionAnimationPreviewMode) || this.fragmentsStack.size() <= 1) {
                            this.themeAnimatorDescriptions[i] = null;
                            this.animateStartColors[i] = null;
                            this.animateEndColors[i] = null;
                            this.themeAnimatorDelegate[i] = null;
                            break Label_0327;
                        }
                        final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                        lastFragment = fragmentsStack.get(fragmentsStack.size() - 2);
                    }
                    if (lastFragment != null) {
                        this.themeAnimatorDescriptions[i] = lastFragment.getThemeDescriptions();
                        this.animateStartColors[i] = new int[this.themeAnimatorDescriptions[i].length];
                        int n = 0;
                        while (true) {
                            final ThemeDescription[][] themeAnimatorDescriptions = this.themeAnimatorDescriptions;
                            if (n >= themeAnimatorDescriptions[i].length) {
                                break;
                            }
                            this.animateStartColors[i][n] = themeAnimatorDescriptions[i][n].getSetColor();
                            final ThemeDescription.ThemeDescriptionDelegate setDelegateDisabled = this.themeAnimatorDescriptions[i][n].setDelegateDisabled();
                            final ThemeDescription.ThemeDescriptionDelegate[] themeAnimatorDelegate = this.themeAnimatorDelegate;
                            if (themeAnimatorDelegate[i] == null && setDelegateDisabled != null) {
                                themeAnimatorDelegate[i] = setDelegateDisabled;
                            }
                            ++n;
                        }
                        if (i == 0) {
                            Theme.applyTheme(animateSetThemeAfterAnimation, animateSetThemeNightAfterAnimation);
                        }
                        this.animateEndColors[i] = new int[this.themeAnimatorDescriptions[i].length];
                        int n2 = 0;
                        while (true) {
                            final ThemeDescription[][] themeAnimatorDescriptions2 = this.themeAnimatorDescriptions;
                            if (n2 >= themeAnimatorDescriptions2[i].length) {
                                break;
                            }
                            this.animateEndColors[i][n2] = themeAnimatorDescriptions2[i][n2].getSetColor();
                            ++n2;
                        }
                        b = true;
                    }
                }
                ++i;
            }
            if (b) {
                (this.themeAnimatorSet = new AnimatorSet()).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator animator) {
                        if (animator.equals(ActionBarLayout.this.themeAnimatorSet)) {
                            for (int i = 0; i < 2; ++i) {
                                ActionBarLayout.this.themeAnimatorDescriptions[i] = null;
                                ActionBarLayout.this.animateStartColors[i] = null;
                                ActionBarLayout.this.animateEndColors[i] = null;
                                ActionBarLayout.this.themeAnimatorDelegate[i] = null;
                            }
                            Theme.setAnimatingColor(false);
                            ActionBarLayout.this.themeAnimatorSet = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(ActionBarLayout.this.themeAnimatorSet)) {
                            for (int i = 0; i < 2; ++i) {
                                ActionBarLayout.this.themeAnimatorDescriptions[i] = null;
                                ActionBarLayout.this.animateStartColors[i] = null;
                                ActionBarLayout.this.animateEndColors[i] = null;
                                ActionBarLayout.this.themeAnimatorDelegate[i] = null;
                            }
                            Theme.setAnimatingColor(false);
                            ActionBarLayout.this.themeAnimatorSet = null;
                        }
                    }
                });
                final int size = this.fragmentsStack.size();
                int n3;
                if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
                    n3 = 1;
                }
                else {
                    n3 = 2;
                }
                for (int j = 0; j < size - n3; ++j) {
                    final BaseFragment baseFragment = this.fragmentsStack.get(j);
                    baseFragment.clearViews();
                    baseFragment.setParentLayout(this);
                }
                Theme.setAnimatingColor(true);
                this.themeAnimatorSet.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "themeAnimationValue", new float[] { 0.0f, 1.0f }) });
                this.themeAnimatorSet.setDuration(200L);
                this.themeAnimatorSet.start();
            }
            return;
        }
        this.animateThemeAfterAnimation = true;
        this.animateSetThemeAfterAnimation = animateSetThemeAfterAnimation;
        this.animateSetThemeNightAfterAnimation = animateSetThemeNightAfterAnimation;
    }
    
    public boolean checkTransitionAnimation() {
        if (this.transitionAnimationPreviewMode) {
            return false;
        }
        if (this.transitionAnimationInProgress && this.transitionAnimationStartTime < System.currentTimeMillis() - 1500L) {
            this.onAnimationEndCheck(true);
        }
        return this.transitionAnimationInProgress;
    }
    
    public void closeLastFragment(final boolean b) {
        final ActionBarLayoutDelegate delegate = this.delegate;
        if ((delegate == null || delegate.needCloseLastFragment(this)) && !this.checkTransitionAnimation()) {
            if (!this.fragmentsStack.isEmpty()) {
                if (this.parentActivity.getCurrentFocus() != null) {
                    AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                }
                this.setInnerTranslationX(0.0f);
                final boolean b2 = this.inPreviewMode || this.transitionAnimationPreviewMode || (b && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true));
                final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                final BaseFragment baseFragment = fragmentsStack.get(fragmentsStack.size() - 1);
                final int size = this.fragmentsStack.size();
                final AnimatorSet set = null;
                BaseFragment baseFragment2;
                if (size > 1) {
                    final ArrayList<BaseFragment> fragmentsStack2 = this.fragmentsStack;
                    baseFragment2 = fragmentsStack2.get(fragmentsStack2.size() - 2);
                }
                else {
                    baseFragment2 = null;
                }
                if (baseFragment2 != null) {
                    final LinearLayoutContainer containerView = this.containerView;
                    this.containerView = this.containerViewBack;
                    this.containerViewBack = containerView;
                    baseFragment2.setParentLayout(this);
                    View view;
                    if ((view = baseFragment2.fragmentView) == null) {
                        view = baseFragment2.createView((Context)this.parentActivity);
                    }
                    if (!this.inPreviewMode) {
                        this.containerView.setVisibility(0);
                        final ActionBar actionBar = baseFragment2.actionBar;
                        if (actionBar != null && actionBar.getAddToContainer()) {
                            if (this.removeActionBarExtraHeight) {
                                baseFragment2.actionBar.setOccupyStatusBar(false);
                            }
                            final ViewGroup viewGroup = (ViewGroup)baseFragment2.actionBar.getParent();
                            if (viewGroup != null) {
                                viewGroup.removeView((View)baseFragment2.actionBar);
                            }
                            this.containerView.addView((View)baseFragment2.actionBar);
                            baseFragment2.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
                        }
                        final ViewGroup viewGroup2 = (ViewGroup)view.getParent();
                        if (viewGroup2 != null) {
                            baseFragment2.onRemoveFromParent();
                            try {
                                viewGroup2.removeView(view);
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                        }
                        this.containerView.addView(view);
                        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)view.getLayoutParams();
                        layoutParams.width = -1;
                        layoutParams.height = -1;
                        layoutParams.leftMargin = 0;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        layoutParams.topMargin = 0;
                        view.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    }
                    baseFragment2.onTransitionAnimationStart(true, true);
                    baseFragment.onTransitionAnimationStart(false, false);
                    baseFragment2.onResume();
                    this.currentActionBar = baseFragment2.actionBar;
                    if (!baseFragment2.hasOwnBackground && view.getBackground() == null) {
                        view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                    if (!b2) {
                        this.closeLastFragmentInternalRemoveOld(baseFragment);
                    }
                    if (b2) {
                        this.transitionAnimationStartTime = System.currentTimeMillis();
                        this.transitionAnimationInProgress = true;
                        this.onCloseAnimationEndRunnable = new _$$Lambda$ActionBarLayout$pBlWjaMhbocc2CQiKAJuI1kS_Ds(this, baseFragment, baseFragment2);
                        AnimatorSet onCustomTransitionAnimation = set;
                        if (!this.inPreviewMode) {
                            onCustomTransitionAnimation = set;
                            if (!this.transitionAnimationPreviewMode) {
                                onCustomTransitionAnimation = baseFragment.onCustomTransitionAnimation(false, new _$$Lambda$ActionBarLayout$r7PgXBy38d_b4XaDN8FoveXk1BE(this));
                            }
                        }
                        if (onCustomTransitionAnimation == null) {
                            if (!this.containerView.isKeyboardVisible && !this.containerViewBack.isKeyboardVisible) {
                                this.startLayoutAnimation(false, true, this.inPreviewMode || this.transitionAnimationPreviewMode);
                            }
                            else {
                                AndroidUtilities.runOnUIThread(this.waitingForKeyboardCloseRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != this) {
                                            return;
                                        }
                                        ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                                        ActionBarLayout.this.startLayoutAnimation(false, true, false);
                                    }
                                }, 200L);
                            }
                        }
                        else {
                            this.currentAnimation = onCustomTransitionAnimation;
                        }
                    }
                    else {
                        baseFragment.onTransitionAnimationEnd(false, false);
                        baseFragment2.onTransitionAnimationEnd(true, true);
                        baseFragment2.onBecomeFullyVisible();
                    }
                }
                else if (this.useAlphaAnimations) {
                    this.transitionAnimationStartTime = System.currentTimeMillis();
                    this.transitionAnimationInProgress = true;
                    this.onCloseAnimationEndRunnable = new _$$Lambda$ActionBarLayout$S9HTTIgsI9OBg6Q7_NNccIiX628(this, baseFragment);
                    final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                    list.add(ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 1.0f, 0.0f }));
                    final View backgroundView = this.backgroundView;
                    if (backgroundView != null) {
                        list.add(ObjectAnimator.ofFloat((Object)backgroundView, "alpha", new float[] { 1.0f, 0.0f }));
                    }
                    (this.currentAnimation = new AnimatorSet()).playTogether((Collection)list);
                    this.currentAnimation.setInterpolator((TimeInterpolator)this.accelerateDecelerateInterpolator);
                    this.currentAnimation.setDuration(200L);
                    this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            ActionBarLayout.this.onAnimationEndCheck(false);
                        }
                        
                        public void onAnimationStart(final Animator animator) {
                            ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
                        }
                    });
                    this.currentAnimation.start();
                }
                else {
                    this.removeFragmentFromStackInternal(baseFragment);
                    this.setVisibility(8);
                    final View backgroundView2 = this.backgroundView;
                    if (backgroundView2 != null) {
                        backgroundView2.setVisibility(8);
                    }
                }
            }
        }
    }
    
    public void dismissDialogs() {
        if (!this.fragmentsStack.isEmpty()) {
            final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
            fragmentsStack.get(fragmentsStack.size() - 1).dismissCurrentDialig();
        }
    }
    
    public boolean dispatchKeyEventPreIme(final KeyEvent keyEvent) {
        if (keyEvent != null && keyEvent.getKeyCode() == 4) {
            final int action = keyEvent.getAction();
            final boolean b = true;
            if (action == 1) {
                final ActionBarLayoutDelegate delegate = this.delegate;
                if (delegate != null) {
                    final boolean b2 = b;
                    if (delegate.onPreIme()) {
                        return b2;
                    }
                }
                return super.dispatchKeyEventPreIme(keyEvent) && b;
            }
        }
        return super.dispatchKeyEventPreIme(keyEvent);
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final int n2 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
        final int n3 = (int)this.innerTranslationX + this.getPaddingRight();
        int paddingLeft = this.getPaddingLeft();
        final int n4 = this.getPaddingLeft() + n2;
        int n5;
        if (view == this.containerViewBack) {
            n5 = n3;
        }
        else {
            n5 = n4;
            if (view == this.containerView) {
                paddingLeft = n3;
                n5 = n4;
            }
        }
        final int save = canvas.save();
        final boolean transitionAnimationInProgress = this.transitionAnimationInProgress;
        int n6 = 0;
        if (!transitionAnimationInProgress && !this.inPreviewMode) {
            canvas.clipRect(paddingLeft, 0, n5, this.getHeight());
        }
        if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
            final LinearLayoutContainer containerView = this.containerView;
            if (view == containerView) {
                final View child = containerView.getChildAt(0);
                if (child != null) {
                    this.previewBackgroundDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                    this.previewBackgroundDrawable.draw(canvas);
                    final int n7 = (this.getMeasuredWidth() - AndroidUtilities.dp(24.0f)) / 2;
                    final float n8 = (float)child.getTop();
                    final float translationY = this.containerView.getTranslationY();
                    if (Build$VERSION.SDK_INT < 21) {
                        n6 = 20;
                    }
                    final int n9 = (int)(n8 + translationY - AndroidUtilities.dp((float)(n6 + 12)));
                    Theme.moveUpDrawable.setBounds(n7, n9, AndroidUtilities.dp(24.0f) + n7, AndroidUtilities.dp(24.0f) + n9);
                    Theme.moveUpDrawable.draw(canvas);
                }
            }
        }
        final boolean drawChild = super.drawChild(canvas, view, n);
        canvas.restoreToCount(save);
        if (n3 != 0) {
            if (view == this.containerView) {
                final float max = Math.max(0.0f, Math.min((n2 - n3) / (float)AndroidUtilities.dp(20.0f), 1.0f));
                final Drawable layerShadowDrawable = ActionBarLayout.layerShadowDrawable;
                layerShadowDrawable.setBounds(n3 - layerShadowDrawable.getIntrinsicWidth(), view.getTop(), n3, view.getBottom());
                ActionBarLayout.layerShadowDrawable.setAlpha((int)(max * 255.0f));
                ActionBarLayout.layerShadowDrawable.draw(canvas);
            }
            else if (view == this.containerViewBack) {
                float min;
                if ((min = Math.min(0.8f, (n2 - n3) / (float)n2)) < 0.0f) {
                    min = 0.0f;
                }
                ActionBarLayout.scrimPaint.setColor((int)(min * 153.0f) << 24);
                canvas.drawRect((float)paddingLeft, 0.0f, (float)n5, (float)this.getHeight(), ActionBarLayout.scrimPaint);
            }
        }
        return drawChild;
    }
    
    public void drawHeaderShadow(final Canvas canvas, final int n) {
        final Drawable headerShadowDrawable = ActionBarLayout.headerShadowDrawable;
        if (headerShadowDrawable != null) {
            headerShadowDrawable.setBounds(0, n, this.getMeasuredWidth(), ActionBarLayout.headerShadowDrawable.getIntrinsicHeight() + n);
            ActionBarLayout.headerShadowDrawable.draw(canvas);
        }
    }
    
    public boolean extendActionMode(final Menu menu) {
        final boolean empty = this.fragmentsStack.isEmpty();
        boolean b = true;
        if (!empty) {
            final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
            if (fragmentsStack.get(fragmentsStack.size() - 1).extendActionMode(menu)) {
                return b;
            }
        }
        b = false;
        return b;
    }
    
    public void finishPreviewFragment() {
        if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
            return;
        }
        this.closeLastFragment(true);
    }
    
    public DrawerLayoutContainer getDrawerLayoutContainer() {
        return this.drawerLayoutContainer;
    }
    
    @Keep
    public float getInnerTranslationX() {
        return this.innerTranslationX;
    }
    
    public BaseFragment getLastFragment() {
        if (this.fragmentsStack.isEmpty()) {
            return null;
        }
        final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
        return fragmentsStack.get(fragmentsStack.size() - 1);
    }
    
    @Keep
    public float getThemeAnimationValue() {
        return this.themeAnimationValue;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public void init(final ArrayList<BaseFragment> fragmentsStack) {
        this.fragmentsStack = fragmentsStack;
        this.addView((View)(this.containerViewBack = new LinearLayoutContainer((Context)this.parentActivity)));
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.containerViewBack.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 51;
        this.containerViewBack.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.addView((View)(this.containerView = new LinearLayoutContainer((Context)this.parentActivity)));
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.containerView.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        layoutParams2.gravity = 51;
        this.containerView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        final Iterator<BaseFragment> iterator = this.fragmentsStack.iterator();
        while (iterator.hasNext()) {
            iterator.next().setParentLayout(this);
        }
    }
    
    public boolean isInPreviewMode() {
        return this.inPreviewMode || this.transitionAnimationPreviewMode;
    }
    
    public void movePreviewFragment(float translationY) {
        if (this.inPreviewMode) {
            if (!this.transitionAnimationPreviewMode) {
                final float translationY2 = this.containerView.getTranslationY();
                translationY = -translationY;
                final float n = 0.0f;
                if (translationY > 0.0f) {
                    translationY = n;
                }
                else if (translationY < -AndroidUtilities.dp(60.0f)) {
                    this.inPreviewMode = false;
                    final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                    final BaseFragment baseFragment = fragmentsStack.get(fragmentsStack.size() - 2);
                    final ArrayList<BaseFragment> fragmentsStack2 = this.fragmentsStack;
                    final BaseFragment baseFragment2 = fragmentsStack2.get(fragmentsStack2.size() - 1);
                    if (Build$VERSION.SDK_INT >= 21) {
                        baseFragment2.fragmentView.setOutlineProvider((ViewOutlineProvider)null);
                        baseFragment2.fragmentView.setClipToOutline(false);
                    }
                    final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)baseFragment2.fragmentView.getLayoutParams();
                    layoutParams.leftMargin = 0;
                    layoutParams.rightMargin = 0;
                    layoutParams.bottomMargin = 0;
                    layoutParams.topMargin = 0;
                    baseFragment2.fragmentView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.presentFragmentInternalRemoveOld(false, baseFragment);
                    final AnimatorSet set = new AnimatorSet();
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)baseFragment2.fragmentView, "scaleX", new float[] { 1.0f, 1.05f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)baseFragment2.fragmentView, "scaleY", new float[] { 1.0f, 1.05f, 1.0f }) });
                    set.setDuration(200L);
                    set.setInterpolator((TimeInterpolator)new CubicBezierInterpolator(0.42, 0.0, 0.58, 1.0));
                    set.start();
                    this.performHapticFeedback(3);
                    baseFragment2.setInPreviewMode(false);
                    translationY = n;
                }
                if (translationY2 != translationY) {
                    this.containerView.setTranslationY(translationY);
                    this.invalidate();
                }
            }
        }
    }
    
    public void onActionModeFinished(final Object o) {
        final ActionBar currentActionBar = this.currentActionBar;
        if (currentActionBar != null) {
            currentActionBar.setVisibility(0);
        }
        this.inActionMode = false;
    }
    
    public void onActionModeStarted(final Object o) {
        final ActionBar currentActionBar = this.currentActionBar;
        if (currentActionBar != null) {
            currentActionBar.setVisibility(8);
        }
        this.inActionMode = true;
    }
    
    public void onBackPressed() {
        if (!this.transitionAnimationPreviewMode && !this.startedTracking && !this.checkTransitionAnimation()) {
            if (!this.fragmentsStack.isEmpty()) {
                if (!this.currentActionBar.isActionModeShowed()) {
                    final ActionBar currentActionBar = this.currentActionBar;
                    if (currentActionBar != null && currentActionBar.isSearchFieldVisible) {
                        currentActionBar.closeSearchField();
                        return;
                    }
                }
                final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                if (fragmentsStack.get(fragmentsStack.size() - 1).onBackPressed() && !this.fragmentsStack.isEmpty()) {
                    this.closeLastFragment(true);
                }
            }
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!this.fragmentsStack.isEmpty()) {
            final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
            final BaseFragment baseFragment = fragmentsStack.get(fragmentsStack.size() - 1);
            baseFragment.onConfigurationChanged(configuration);
            final Dialog visibleDialog = baseFragment.visibleDialog;
            if (visibleDialog instanceof BottomSheet) {
                ((BottomSheet)visibleDialog).onConfigurationChanged(configuration);
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.animationInProgress || this.checkTransitionAnimation() || this.onTouchEvent(motionEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        if (n == 82 && !this.checkTransitionAnimation() && !this.startedTracking) {
            final ActionBar currentActionBar = this.currentActionBar;
            if (currentActionBar != null) {
                currentActionBar.onMenuButtonPressed();
            }
        }
        return super.onKeyUp(n, keyEvent);
    }
    
    public void onLowMemory() {
        final Iterator<BaseFragment> iterator = this.fragmentsStack.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLowMemory();
        }
    }
    
    public void onPause() {
        if (!this.fragmentsStack.isEmpty()) {
            final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
            fragmentsStack.get(fragmentsStack.size() - 1).onPause();
        }
    }
    
    public void onResume() {
        if (this.transitionAnimationInProgress) {
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
                this.currentAnimation = null;
            }
            if (this.onCloseAnimationEndRunnable != null) {
                this.onCloseAnimationEnd();
            }
            else if (this.onOpenAnimationEndRunnable != null) {
                this.onOpenAnimationEnd();
            }
        }
        if (!this.fragmentsStack.isEmpty()) {
            final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
            fragmentsStack.get(fragmentsStack.size() - 1).onResume();
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.checkTransitionAnimation() && !this.inActionMode && !this.animationInProgress) {
            if (this.fragmentsStack.size() > 1) {
                if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                    final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                    if (!fragmentsStack.get(fragmentsStack.size() - 1).swipeBackEnabled) {
                        return false;
                    }
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
                    final int max = Math.max(0, (int)(motionEvent.getX() - this.startedTrackingX));
                    final int abs = Math.abs((int)motionEvent.getY() - this.startedTrackingY);
                    this.velocityTracker.addMovement(motionEvent);
                    if (this.maybeStartTracking && !this.startedTracking && max >= AndroidUtilities.getPixelsInCM(0.4f, true) && Math.abs(max) / 3 > abs) {
                        final ArrayList<BaseFragment> fragmentsStack2 = this.fragmentsStack;
                        if (fragmentsStack2.get(fragmentsStack2.size() - 1).canBeginSlide()) {
                            this.prepareForMoving(motionEvent);
                        }
                        else {
                            this.maybeStartTracking = false;
                        }
                    }
                    else if (this.startedTracking) {
                        if (!this.beginTrackingSent) {
                            if (this.parentActivity.getCurrentFocus() != null) {
                                AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                            }
                            final ArrayList<BaseFragment> fragmentsStack3 = this.fragmentsStack;
                            fragmentsStack3.get(fragmentsStack3.size() - 1).onBeginSlide();
                            this.beginTrackingSent = true;
                        }
                        final LinearLayoutContainer containerView = this.containerView;
                        final float n = (float)max;
                        containerView.setTranslationX(n);
                        this.setInnerTranslationX(n);
                    }
                }
                else if (motionEvent != null && motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6)) {
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    this.velocityTracker.computeCurrentVelocity(1000);
                    final ArrayList<BaseFragment> fragmentsStack4 = this.fragmentsStack;
                    final BaseFragment baseFragment = fragmentsStack4.get(fragmentsStack4.size() - 1);
                    if (!this.inPreviewMode && !this.transitionAnimationPreviewMode && !this.startedTracking && baseFragment.swipeBackEnabled) {
                        final float xVelocity = this.velocityTracker.getXVelocity();
                        final float yVelocity = this.velocityTracker.getYVelocity();
                        if (xVelocity >= 3500.0f && xVelocity > Math.abs(yVelocity) && baseFragment.canBeginSlide()) {
                            this.prepareForMoving(motionEvent);
                            if (!this.beginTrackingSent) {
                                if (((Activity)this.getContext()).getCurrentFocus() != null) {
                                    AndroidUtilities.hideKeyboard(((Activity)this.getContext()).getCurrentFocus());
                                }
                                this.beginTrackingSent = true;
                            }
                        }
                    }
                    if (this.startedTracking) {
                        float x = this.containerView.getX();
                        final AnimatorSet set = new AnimatorSet();
                        final float xVelocity2 = this.velocityTracker.getXVelocity();
                        final float yVelocity2 = this.velocityTracker.getYVelocity();
                        final boolean b = x < this.containerView.getMeasuredWidth() / 3.0f && (xVelocity2 < 3500.0f || xVelocity2 < yVelocity2);
                        if (!b) {
                            x = this.containerView.getMeasuredWidth() - x;
                            final LinearLayoutContainer containerView2 = this.containerView;
                            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)containerView2, "translationX", new float[] { (float)containerView2.getMeasuredWidth() }), (Animator)ObjectAnimator.ofFloat((Object)this, "innerTranslationX", new float[] { (float)this.containerView.getMeasuredWidth() }) });
                        }
                        else {
                            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.containerView, "translationX", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, "innerTranslationX", new float[] { 0.0f }) });
                        }
                        set.setDuration((long)Math.max((int)(200.0f / this.containerView.getMeasuredWidth() * x), 50));
                        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                ActionBarLayout.this.onSlideAnimationEnd(b);
                            }
                        });
                        set.start();
                        this.animationInProgress = true;
                    }
                    else {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                    }
                    final VelocityTracker velocityTracker2 = this.velocityTracker;
                    if (velocityTracker2 != null) {
                        velocityTracker2.recycle();
                        this.velocityTracker = null;
                    }
                }
                else if (motionEvent == null) {
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    final VelocityTracker velocityTracker3 = this.velocityTracker;
                    if (velocityTracker3 != null) {
                        velocityTracker3.recycle();
                        this.velocityTracker = null;
                    }
                }
            }
            return this.startedTracking;
        }
        return false;
    }
    
    public boolean presentFragment(final BaseFragment baseFragment) {
        return this.presentFragment(baseFragment, false, false, true, false);
    }
    
    public boolean presentFragment(final BaseFragment baseFragment, final boolean b) {
        return this.presentFragment(baseFragment, b, false, true, false);
    }
    
    public boolean presentFragment(final BaseFragment e, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        if (!this.checkTransitionAnimation()) {
            final ActionBarLayoutDelegate delegate = this.delegate;
            if (delegate == null || !b3 || delegate.needPresentFragment(e, b, b2, this)) {
                if (e.onFragmentCreate()) {
                    e.setInPreviewMode(b4);
                    if (this.parentActivity.getCurrentFocus() != null) {
                        AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                    }
                    final boolean b5 = b4 || (!b2 && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true));
                    BaseFragment baseFragment;
                    if (!this.fragmentsStack.isEmpty()) {
                        final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
                        baseFragment = fragmentsStack.get(fragmentsStack.size() - 1);
                    }
                    else {
                        baseFragment = null;
                    }
                    e.setParentLayout(this);
                    final View fragmentView = e.fragmentView;
                    View view;
                    if (fragmentView == null) {
                        view = e.createView((Context)this.parentActivity);
                    }
                    else {
                        final ViewGroup viewGroup = (ViewGroup)fragmentView.getParent();
                        view = fragmentView;
                        if (viewGroup != null) {
                            e.onRemoveFromParent();
                            viewGroup.removeView(fragmentView);
                            view = fragmentView;
                        }
                    }
                    final ActionBar actionBar = e.actionBar;
                    if (actionBar != null && actionBar.getAddToContainer()) {
                        if (this.removeActionBarExtraHeight) {
                            e.actionBar.setOccupyStatusBar(false);
                        }
                        final ViewGroup viewGroup2 = (ViewGroup)e.actionBar.getParent();
                        if (viewGroup2 != null) {
                            viewGroup2.removeView((View)e.actionBar);
                        }
                        this.containerViewBack.addView((View)e.actionBar);
                        e.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
                    }
                    this.containerViewBack.addView(view);
                    final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)view.getLayoutParams();
                    layoutParams.width = -1;
                    layoutParams.height = -1;
                    if (b4) {
                        final int dp = AndroidUtilities.dp(8.0f);
                        layoutParams.leftMargin = dp;
                        layoutParams.rightMargin = dp;
                        final int dp2 = AndroidUtilities.dp(46.0f);
                        layoutParams.bottomMargin = dp2;
                        layoutParams.topMargin = dp2;
                        layoutParams.topMargin += AndroidUtilities.statusBarHeight;
                    }
                    else {
                        layoutParams.leftMargin = 0;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        layoutParams.topMargin = 0;
                    }
                    view.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.fragmentsStack.add(e);
                    e.onResume();
                    this.currentActionBar = e.actionBar;
                    if (!e.hasOwnBackground && view.getBackground() == null) {
                        view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                    final LinearLayoutContainer containerView = this.containerView;
                    this.containerView = this.containerViewBack;
                    this.containerViewBack = containerView;
                    this.containerView.setVisibility(0);
                    this.setInnerTranslationX(0.0f);
                    this.containerView.setTranslationY(0.0f);
                    if (b4) {
                        if (Build$VERSION.SDK_INT >= 21) {
                            view.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                                @TargetApi(21)
                                public void getOutline(final View view, final Outline outline) {
                                    outline.setRoundRect(0, AndroidUtilities.statusBarHeight, view.getMeasuredWidth(), view.getMeasuredHeight(), (float)AndroidUtilities.dp(6.0f));
                                }
                            });
                            view.setClipToOutline(true);
                            view.setElevation((float)AndroidUtilities.dp(4.0f));
                        }
                        if (this.previewBackgroundDrawable == null) {
                            this.previewBackgroundDrawable = new ColorDrawable(Integer.MIN_VALUE);
                        }
                        this.previewBackgroundDrawable.setAlpha(0);
                        Theme.moveUpDrawable.setAlpha(0);
                    }
                    this.bringChildToFront((View)this.containerView);
                    if (!b5) {
                        this.presentFragmentInternalRemoveOld(b, baseFragment);
                        final View backgroundView = this.backgroundView;
                        if (backgroundView != null) {
                            backgroundView.setVisibility(0);
                        }
                    }
                    if (!b5 && !b4) {
                        final View backgroundView2 = this.backgroundView;
                        if (backgroundView2 != null) {
                            backgroundView2.setAlpha(1.0f);
                            this.backgroundView.setVisibility(0);
                        }
                        e.onTransitionAnimationStart(true, false);
                        e.onTransitionAnimationEnd(true, false);
                        e.onBecomeFullyVisible();
                    }
                    else if (this.useAlphaAnimations && this.fragmentsStack.size() == 1) {
                        this.presentFragmentInternalRemoveOld(b, baseFragment);
                        this.transitionAnimationStartTime = System.currentTimeMillis();
                        this.transitionAnimationInProgress = true;
                        this.onOpenAnimationEndRunnable = new _$$Lambda$ActionBarLayout$pYh6HgDiwfydlsh9Xn7223Bd_IA(e);
                        final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                        list.add(ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 0.0f, 1.0f }));
                        final View backgroundView3 = this.backgroundView;
                        if (backgroundView3 != null) {
                            backgroundView3.setVisibility(0);
                            list.add(ObjectAnimator.ofFloat((Object)this.backgroundView, "alpha", new float[] { 0.0f, 1.0f }));
                        }
                        e.onTransitionAnimationStart(true, false);
                        (this.currentAnimation = new AnimatorSet()).playTogether((Collection)list);
                        this.currentAnimation.setInterpolator((TimeInterpolator)this.accelerateDecelerateInterpolator);
                        this.currentAnimation.setDuration(200L);
                        this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                ActionBarLayout.this.onAnimationEndCheck(false);
                            }
                        });
                        this.currentAnimation.start();
                    }
                    else {
                        this.transitionAnimationPreviewMode = b4;
                        this.transitionAnimationStartTime = System.currentTimeMillis();
                        this.transitionAnimationInProgress = true;
                        this.onOpenAnimationEndRunnable = new _$$Lambda$ActionBarLayout$gS41pICx_migujhqTs_lczeRv1Y(this, b4, b, baseFragment, e);
                        e.onTransitionAnimationStart(true, false);
                        AnimatorSet onCustomTransitionAnimation;
                        if (!b4) {
                            onCustomTransitionAnimation = e.onCustomTransitionAnimation(true, new _$$Lambda$ActionBarLayout$ZVBs3Yp413UBaNwwGzpjbW7oZTc(this));
                        }
                        else {
                            onCustomTransitionAnimation = null;
                        }
                        if (onCustomTransitionAnimation == null) {
                            this.containerView.setAlpha(0.0f);
                            if (b4) {
                                this.containerView.setTranslationX(0.0f);
                                this.containerView.setScaleX(0.9f);
                                this.containerView.setScaleY(0.9f);
                            }
                            else {
                                this.containerView.setTranslationX(48.0f);
                                this.containerView.setScaleX(1.0f);
                                this.containerView.setScaleY(1.0f);
                            }
                            if (!this.containerView.isKeyboardVisible && !this.containerViewBack.isKeyboardVisible) {
                                if (e.needDelayOpenAnimation()) {
                                    AndroidUtilities.runOnUIThread(this.delayedOpenAnimationRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ActionBarLayout.this.delayedOpenAnimationRunnable != this) {
                                                return;
                                            }
                                            ActionBarLayout.this.delayedOpenAnimationRunnable = null;
                                            ActionBarLayout.this.startLayoutAnimation(true, true, b4);
                                        }
                                    }, 200L);
                                }
                                else {
                                    this.startLayoutAnimation(true, true, b4);
                                }
                            }
                            else {
                                AndroidUtilities.runOnUIThread(this.waitingForKeyboardCloseRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != this) {
                                            return;
                                        }
                                        ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                                        ActionBarLayout.this.startLayoutAnimation(true, true, b4);
                                    }
                                }, 200L);
                            }
                        }
                        else {
                            this.containerView.setAlpha(1.0f);
                            this.containerView.setTranslationX(0.0f);
                            this.currentAnimation = onCustomTransitionAnimation;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean presentFragmentAsPreview(final BaseFragment baseFragment) {
        return this.presentFragment(baseFragment, false, false, true, true);
    }
    
    public void rebuildAllFragmentViews(final boolean rebuildLastAfterAnimation, final boolean showLastAfterAnimation) {
        if (!this.transitionAnimationInProgress && !this.startedTracking) {
            int size;
            final int n = size = this.fragmentsStack.size();
            if (!rebuildLastAfterAnimation) {
                size = n - 1;
            }
            int n2 = size;
            if (this.inPreviewMode) {
                n2 = size - 1;
            }
            for (int i = 0; i < n2; ++i) {
                this.fragmentsStack.get(i).clearViews();
                this.fragmentsStack.get(i).setParentLayout(this);
            }
            final ActionBarLayoutDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onRebuildAllFragments(this, rebuildLastAfterAnimation);
            }
            if (showLastAfterAnimation) {
                this.showLastFragment();
            }
            return;
        }
        this.rebuildAfterAnimation = true;
        this.rebuildLastAfterAnimation = rebuildLastAfterAnimation;
        this.showLastAfterAnimation = showLastAfterAnimation;
    }
    
    public void removeAllFragments() {
        while (this.fragmentsStack.size() > 0) {
            this.removeFragmentFromStackInternal(this.fragmentsStack.get(0));
        }
    }
    
    public void removeFragmentFromStack(final int index) {
        if (index >= this.fragmentsStack.size()) {
            return;
        }
        this.removeFragmentFromStackInternal(this.fragmentsStack.get(index));
    }
    
    public void removeFragmentFromStack(final BaseFragment baseFragment) {
        if (this.useAlphaAnimations && this.fragmentsStack.size() == 1 && AndroidUtilities.isTablet()) {
            this.closeLastFragment(true);
        }
        else {
            this.removeFragmentFromStackInternal(baseFragment);
        }
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        this.onTouchEvent(null);
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void resumeDelayedFragmentAnimation() {
        final Runnable delayedOpenAnimationRunnable = this.delayedOpenAnimationRunnable;
        if (delayedOpenAnimationRunnable == null) {
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(delayedOpenAnimationRunnable);
        this.delayedOpenAnimationRunnable.run();
        this.delayedOpenAnimationRunnable = null;
    }
    
    public void setBackgroundView(final View backgroundView) {
        this.backgroundView = backgroundView;
    }
    
    public void setDelegate(final ActionBarLayoutDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setDrawerLayoutContainer(final DrawerLayoutContainer drawerLayoutContainer) {
        this.drawerLayoutContainer = drawerLayoutContainer;
    }
    
    @Keep
    public void setInnerTranslationX(final float innerTranslationX) {
        this.innerTranslationX = innerTranslationX;
        this.invalidate();
    }
    
    public void setRemoveActionBarExtraHeight(final boolean removeActionBarExtraHeight) {
        this.removeActionBarExtraHeight = removeActionBarExtraHeight;
    }
    
    @Keep
    public void setThemeAnimationValue(final float themeAnimationValue) {
        this.themeAnimationValue = themeAnimationValue;
        for (int i = 0; i < 2; ++i) {
            if (this.themeAnimatorDescriptions[i] != null) {
                for (int j = 0; j < this.themeAnimatorDescriptions[i].length; ++j) {
                    final int red = Color.red(this.animateEndColors[i][j]);
                    final int green = Color.green(this.animateEndColors[i][j]);
                    final int blue = Color.blue(this.animateEndColors[i][j]);
                    final int alpha = Color.alpha(this.animateEndColors[i][j]);
                    final int red2 = Color.red(this.animateStartColors[i][j]);
                    final int green2 = Color.green(this.animateStartColors[i][j]);
                    final int blue2 = Color.blue(this.animateStartColors[i][j]);
                    final int alpha2 = Color.alpha(this.animateStartColors[i][j]);
                    final int argb = Color.argb(Math.min(255, (int)(alpha2 + (alpha - alpha2) * themeAnimationValue)), Math.min(255, (int)(red2 + (red - red2) * themeAnimationValue)), Math.min(255, (int)(green2 + (green - green2) * themeAnimationValue)), Math.min(255, (int)(blue2 + (blue - blue2) * themeAnimationValue)));
                    Theme.setAnimatedColor(this.themeAnimatorDescriptions[i][j].getCurrentKey(), argb);
                    this.themeAnimatorDescriptions[i][j].setColor(argb, false, false);
                }
                final ThemeDescription.ThemeDescriptionDelegate[] themeAnimatorDelegate = this.themeAnimatorDelegate;
                if (themeAnimatorDelegate[i] != null) {
                    themeAnimatorDelegate[i].didSetColor();
                }
            }
        }
    }
    
    public void setTitleOverlayText(final String titleOverlayText, int i, final Runnable overlayAction) {
        this.titleOverlayText = titleOverlayText;
        this.titleOverlayTextId = i;
        this.overlayAction = overlayAction;
        ActionBar actionBar;
        for (i = 0; i < this.fragmentsStack.size(); ++i) {
            actionBar = this.fragmentsStack.get(i).actionBar;
            if (actionBar != null) {
                actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, overlayAction);
            }
        }
    }
    
    public void setUseAlphaAnimations(final boolean useAlphaAnimations) {
        this.useAlphaAnimations = useAlphaAnimations;
    }
    
    public void showLastFragment() {
        if (this.fragmentsStack.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.fragmentsStack.size() - 1; ++i) {
            final BaseFragment baseFragment = this.fragmentsStack.get(i);
            final ActionBar actionBar = baseFragment.actionBar;
            if (actionBar != null && actionBar.getAddToContainer()) {
                final ViewGroup viewGroup = (ViewGroup)baseFragment.actionBar.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView((View)baseFragment.actionBar);
                }
            }
            final View fragmentView = baseFragment.fragmentView;
            if (fragmentView != null) {
                final ViewGroup viewGroup2 = (ViewGroup)fragmentView.getParent();
                if (viewGroup2 != null) {
                    baseFragment.onPause();
                    baseFragment.onRemoveFromParent();
                    viewGroup2.removeView(baseFragment.fragmentView);
                }
            }
        }
        final ArrayList<BaseFragment> fragmentsStack = this.fragmentsStack;
        final BaseFragment baseFragment2 = fragmentsStack.get(fragmentsStack.size() - 1);
        baseFragment2.setParentLayout(this);
        final View fragmentView2 = baseFragment2.fragmentView;
        View view;
        if (fragmentView2 == null) {
            view = baseFragment2.createView((Context)this.parentActivity);
        }
        else {
            final ViewGroup viewGroup3 = (ViewGroup)fragmentView2.getParent();
            view = fragmentView2;
            if (viewGroup3 != null) {
                baseFragment2.onRemoveFromParent();
                viewGroup3.removeView(fragmentView2);
                view = fragmentView2;
            }
        }
        final ActionBar actionBar2 = baseFragment2.actionBar;
        if (actionBar2 != null && actionBar2.getAddToContainer()) {
            if (this.removeActionBarExtraHeight) {
                baseFragment2.actionBar.setOccupyStatusBar(false);
            }
            final ViewGroup viewGroup4 = (ViewGroup)baseFragment2.actionBar.getParent();
            if (viewGroup4 != null) {
                viewGroup4.removeView((View)baseFragment2.actionBar);
            }
            this.containerView.addView((View)baseFragment2.actionBar);
            baseFragment2.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
        }
        this.containerView.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1));
        baseFragment2.onResume();
        this.currentActionBar = baseFragment2.actionBar;
        if (!baseFragment2.hasOwnBackground && view.getBackground() == null) {
            view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        }
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        final Activity parentActivity = this.parentActivity;
        if (parentActivity == null) {
            return;
        }
        if (this.transitionAnimationInProgress) {
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
                this.currentAnimation = null;
            }
            if (this.onCloseAnimationEndRunnable != null) {
                this.onCloseAnimationEnd();
            }
            else if (this.onOpenAnimationEndRunnable != null) {
                this.onOpenAnimationEnd();
            }
            this.containerView.invalidate();
            if (intent != null) {
                this.parentActivity.startActivityForResult(intent, n);
            }
        }
        else if (intent != null) {
            parentActivity.startActivityForResult(intent, n);
        }
    }
    
    public interface ActionBarLayoutDelegate
    {
        boolean needAddFragmentToStack(final BaseFragment p0, final ActionBarLayout p1);
        
        boolean needCloseLastFragment(final ActionBarLayout p0);
        
        boolean needPresentFragment(final BaseFragment p0, final boolean p1, final boolean p2, final ActionBarLayout p3);
        
        boolean onPreIme();
        
        void onRebuildAllFragments(final ActionBarLayout p0, final boolean p1);
    }
    
    public class LinearLayoutContainer extends LinearLayout
    {
        private boolean isKeyboardVisible;
        private Rect rect;
        
        public LinearLayoutContainer(final Context context) {
            super(context);
            this.rect = new Rect();
            this.setOrientation(1);
        }
        
        public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
            final boolean access$400 = ActionBarLayout.this.inPreviewMode;
            final boolean b = false;
            if (access$400 || ActionBarLayout.this.transitionAnimationPreviewMode) {
                if (motionEvent.getActionMasked() == 0) {
                    return false;
                }
                if (motionEvent.getActionMasked() == 5) {
                    return false;
                }
            }
            try {
                if (ActionBarLayout.this.inPreviewMode) {
                    final boolean b2 = b;
                    if (this == ActionBarLayout.this.containerView) {
                        return b2;
                    }
                }
                final boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
                boolean b2 = b;
                if (dispatchTouchEvent) {
                    b2 = true;
                }
                return b2;
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
            return false;
        }
        
        protected boolean drawChild(final Canvas canvas, final View view, final long n) {
            if (view instanceof ActionBar) {
                return super.drawChild(canvas, view, n);
            }
            while (true) {
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    if (child != view) {
                        if (child instanceof ActionBar && child.getVisibility() == 0) {
                            if (((ActionBar)child).getCastShadows()) {
                                final int measuredHeight = child.getMeasuredHeight();
                                final boolean drawChild = super.drawChild(canvas, view, n);
                                if (measuredHeight != 0 && ActionBarLayout.headerShadowDrawable != null) {
                                    ActionBarLayout.headerShadowDrawable.setBounds(0, measuredHeight, this.getMeasuredWidth(), ActionBarLayout.headerShadowDrawable.getIntrinsicHeight() + measuredHeight);
                                    ActionBarLayout.headerShadowDrawable.draw(canvas);
                                }
                                return drawChild;
                            }
                            break;
                        }
                    }
                }
                final int measuredHeight = 0;
                continue;
            }
        }
        
        public boolean hasOverlappingRendering() {
            return Build$VERSION.SDK_INT >= 28;
        }
        
        protected void onLayout(final boolean b, int n, int height, int viewInset, final int n2) {
            super.onLayout(b, n, height, viewInset, n2);
            final View rootView = this.getRootView();
            this.getWindowVisibleDisplayFrame(this.rect);
            height = rootView.getHeight();
            n = this.rect.top;
            boolean isKeyboardVisible = false;
            if (n != 0) {
                n = AndroidUtilities.statusBarHeight;
            }
            else {
                n = 0;
            }
            viewInset = AndroidUtilities.getViewInset(rootView);
            final Rect rect = this.rect;
            if (height - n - viewInset - (rect.bottom - rect.top) > 0) {
                isKeyboardVisible = true;
            }
            this.isKeyboardVisible = isKeyboardVisible;
            if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != null && !ActionBarLayout.this.containerView.isKeyboardVisible && !ActionBarLayout.this.containerViewBack.isKeyboardVisible) {
                AndroidUtilities.cancelRunOnUIThread(ActionBarLayout.this.waitingForKeyboardCloseRunnable);
                ActionBarLayout.this.waitingForKeyboardCloseRunnable.run();
                ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
            }
        }
    }
}
