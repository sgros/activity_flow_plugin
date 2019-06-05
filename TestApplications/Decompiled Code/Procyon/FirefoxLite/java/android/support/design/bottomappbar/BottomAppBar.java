// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.bottomappbar;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.design.animation.AnimationUtils;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.behavior.HideBottomViewOnScrollBehavior;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.os.Parcelable;
import android.content.res.ColorStateList;
import java.util.Collection;
import java.util.ArrayList;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import java.util.Iterator;
import android.view.View;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import java.util.List;
import android.animation.Animator$AnimatorListener;
import android.support.v7.widget.ActionMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.shape.MaterialShapeDrawable;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;

public class BottomAppBar extends Toolbar implements AttachedBehavior
{
    private Animator attachAnimator;
    private int fabAlignmentMode;
    AnimatorListenerAdapter fabAnimationListener;
    private boolean fabAttached;
    private final int fabOffsetEndMode;
    private boolean hideOnScroll;
    private final MaterialShapeDrawable materialShapeDrawable;
    private Animator menuAnimator;
    private Animator modeAnimator;
    private final BottomAppBarTopEdgeTreatment topEdgeTreatment;
    
    private void addFabAnimationListeners(final FloatingActionButton floatingActionButton) {
        this.removeFabAnimationListeners(floatingActionButton);
        floatingActionButton.addOnHideAnimationListener((Animator$AnimatorListener)this.fabAnimationListener);
        floatingActionButton.addOnShowAnimationListener((Animator$AnimatorListener)this.fabAnimationListener);
    }
    
    private void cancelAnimations() {
        if (this.attachAnimator != null) {
            this.attachAnimator.cancel();
        }
        if (this.menuAnimator != null) {
            this.menuAnimator.cancel();
        }
        if (this.modeAnimator != null) {
            this.modeAnimator.cancel();
        }
    }
    
    private void createCradleTranslationAnimation(final int n, final List<Animator> list) {
        if (!this.fabAttached) {
            return;
        }
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[] { this.topEdgeTreatment.getHorizontalOffset(), (float)this.getFabTranslationX(n) });
        ofFloat.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                BottomAppBar.this.topEdgeTreatment.setHorizontalOffset((float)valueAnimator.getAnimatedValue());
                BottomAppBar.this.materialShapeDrawable.invalidateSelf();
            }
        });
        ofFloat.setDuration(300L);
        list.add((Animator)ofFloat);
    }
    
    private void createFabTranslationXAnimation(final int n, final List<Animator> list) {
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.findDependentFab(), "translationX", new float[] { (float)this.getFabTranslationX(n) });
        ofFloat.setDuration(300L);
        list.add((Animator)ofFloat);
    }
    
    private void createMenuViewTranslationAnimation(final int n, final boolean b, final List<Animator> list) {
        final ActionMenuView actionMenuView = this.getActionMenuView();
        if (actionMenuView == null) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)actionMenuView, "alpha", new float[] { 1.0f });
        if ((this.fabAttached || (b && this.isVisibleFab())) && (this.fabAlignmentMode == 1 || n == 1)) {
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)actionMenuView, "alpha", new float[] { 0.0f });
            ((Animator)ofFloat2).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public boolean cancelled;
                
                public void onAnimationCancel(final Animator animator) {
                    this.cancelled = true;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    if (!this.cancelled) {
                        BottomAppBar.this.translateActionMenuView(actionMenuView, n, b);
                    }
                }
            });
            final AnimatorSet set = new AnimatorSet();
            set.setDuration(150L);
            set.playSequentially(new Animator[] { (Animator)ofFloat2, (Animator)ofFloat });
            list.add((Animator)set);
        }
        else if (actionMenuView.getAlpha() < 1.0f) {
            list.add((Animator)ofFloat);
        }
    }
    
    private FloatingActionButton findDependentFab() {
        if (!(this.getParent() instanceof CoordinatorLayout)) {
            return null;
        }
        for (final View view : ((CoordinatorLayout)this.getParent()).getDependents((View)this)) {
            if (view instanceof FloatingActionButton) {
                return (FloatingActionButton)view;
            }
        }
        return null;
    }
    
    private ActionMenuView getActionMenuView() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionMenuView) {
                return (ActionMenuView)child;
            }
        }
        return null;
    }
    
    private float getFabTranslationX() {
        return (float)this.getFabTranslationX(this.fabAlignmentMode);
    }
    
    private int getFabTranslationX(int n) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        int n2 = 0;
        final int n3 = 1;
        final boolean b = layoutDirection == 1;
        if (n == 1) {
            final int n4 = this.getMeasuredWidth() / 2;
            final int fabOffsetEndMode = this.fabOffsetEndMode;
            n = n3;
            if (b) {
                n = -1;
            }
            n2 = (n4 - fabOffsetEndMode) * n;
        }
        return n2;
    }
    
    private float getFabTranslationY() {
        return this.getFabTranslationY(this.fabAttached);
    }
    
    private float getFabTranslationY(final boolean b) {
        final FloatingActionButton dependentFab = this.findDependentFab();
        if (dependentFab == null) {
            return 0.0f;
        }
        final Rect rect = new Rect();
        dependentFab.getContentRect(rect);
        float n;
        if ((n = (float)rect.height()) == 0.0f) {
            n = (float)dependentFab.getMeasuredHeight();
        }
        final float n2 = (float)(dependentFab.getHeight() - rect.bottom);
        final float n3 = (float)(dependentFab.getHeight() - rect.height());
        final float n4 = -this.getCradleVerticalOffset();
        final float n5 = n / 2.0f;
        float n6 = n3 - dependentFab.getPaddingBottom();
        final float n7 = (float)(-this.getMeasuredHeight());
        if (b) {
            n6 = n4 + n5 + n2;
        }
        return n7 + n6;
    }
    
    private boolean isAnimationRunning() {
        return (this.attachAnimator != null && this.attachAnimator.isRunning()) || (this.menuAnimator != null && this.menuAnimator.isRunning()) || (this.modeAnimator != null && this.modeAnimator.isRunning());
    }
    
    private boolean isVisibleFab() {
        final FloatingActionButton dependentFab = this.findDependentFab();
        return dependentFab != null && dependentFab.isOrWillBeShown();
    }
    
    private void maybeAnimateMenuView(int n, boolean b) {
        if (!ViewCompat.isLaidOut((View)this)) {
            return;
        }
        if (this.menuAnimator != null) {
            this.menuAnimator.cancel();
        }
        final ArrayList<Animator> list = new ArrayList<Animator>();
        if (!this.isVisibleFab()) {
            n = 0;
            b = false;
        }
        this.createMenuViewTranslationAnimation(n, b, list);
        final AnimatorSet menuAnimator = new AnimatorSet();
        menuAnimator.playTogether((Collection)list);
        (this.menuAnimator = (Animator)menuAnimator).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                BottomAppBar.this.menuAnimator = null;
            }
        });
        this.menuAnimator.start();
    }
    
    private void maybeAnimateModeChange(final int n) {
        if (this.fabAlignmentMode != n && ViewCompat.isLaidOut((View)this)) {
            if (this.modeAnimator != null) {
                this.modeAnimator.cancel();
            }
            final ArrayList<Animator> list = new ArrayList<Animator>();
            this.createCradleTranslationAnimation(n, list);
            this.createFabTranslationXAnimation(n, list);
            final AnimatorSet modeAnimator = new AnimatorSet();
            modeAnimator.playTogether((Collection)list);
            (this.modeAnimator = (Animator)modeAnimator).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    BottomAppBar.this.modeAnimator = null;
                }
            });
            this.modeAnimator.start();
        }
    }
    
    private void removeFabAnimationListeners(final FloatingActionButton floatingActionButton) {
        floatingActionButton.removeOnHideAnimationListener((Animator$AnimatorListener)this.fabAnimationListener);
        floatingActionButton.removeOnShowAnimationListener((Animator$AnimatorListener)this.fabAnimationListener);
    }
    
    private void setCutoutState() {
        this.topEdgeTreatment.setHorizontalOffset(this.getFabTranslationX());
        final FloatingActionButton dependentFab = this.findDependentFab();
        final MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
        float interpolation;
        if (this.fabAttached && this.isVisibleFab()) {
            interpolation = 1.0f;
        }
        else {
            interpolation = 0.0f;
        }
        materialShapeDrawable.setInterpolation(interpolation);
        if (dependentFab != null) {
            dependentFab.setTranslationY(this.getFabTranslationY());
            dependentFab.setTranslationX(this.getFabTranslationX());
        }
        final ActionMenuView actionMenuView = this.getActionMenuView();
        if (actionMenuView != null) {
            actionMenuView.setAlpha(1.0f);
            if (!this.isVisibleFab()) {
                this.translateActionMenuView(actionMenuView, 0, false);
            }
            else {
                this.translateActionMenuView(actionMenuView, this.fabAlignmentMode, this.fabAttached);
            }
        }
    }
    
    private void translateActionMenuView(final ActionMenuView actionMenuView, final int n, final boolean b) {
        final boolean b2 = ViewCompat.getLayoutDirection((View)this) == 1;
        int i = 0;
        int a = 0;
        while (i < this.getChildCount()) {
            final View child = this.getChildAt(i);
            final boolean b3 = child.getLayoutParams() instanceof Toolbar.LayoutParams && (((Toolbar.LayoutParams)child.getLayoutParams()).gravity & 0x800007) == 0x800003;
            int max = a;
            if (b3) {
                int b4;
                if (b2) {
                    b4 = child.getLeft();
                }
                else {
                    b4 = child.getRight();
                }
                max = Math.max(a, b4);
            }
            ++i;
            a = max;
        }
        int n2;
        if (b2) {
            n2 = actionMenuView.getRight();
        }
        else {
            n2 = actionMenuView.getLeft();
        }
        float translationX;
        if (n == 1 && b) {
            translationX = (float)(a - n2);
        }
        else {
            translationX = 0.0f;
        }
        actionMenuView.setTranslationX(translationX);
    }
    
    public ColorStateList getBackgroundTint() {
        return this.materialShapeDrawable.getTintList();
    }
    
    @Override
    public CoordinatorLayout.Behavior<BottomAppBar> getBehavior() {
        return new Behavior();
    }
    
    public float getCradleVerticalOffset() {
        return this.topEdgeTreatment.getCradleVerticalOffset();
    }
    
    public int getFabAlignmentMode() {
        return this.fabAlignmentMode;
    }
    
    public float getFabCradleMargin() {
        return this.topEdgeTreatment.getFabCradleMargin();
    }
    
    public float getFabCradleRoundedCornerRadius() {
        return this.topEdgeTreatment.getFabCradleRoundedCornerRadius();
    }
    
    public boolean getHideOnScroll() {
        return this.hideOnScroll;
    }
    
    @Override
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.cancelAnimations();
        this.setCutoutState();
    }
    
    @Override
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.fabAlignmentMode = savedState.fabAlignmentMode;
        this.fabAttached = savedState.fabAttached;
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.fabAlignmentMode = this.fabAlignmentMode;
        savedState.fabAttached = this.fabAttached;
        return (Parcelable)savedState;
    }
    
    public void setBackgroundTint(final ColorStateList list) {
        DrawableCompat.setTintList(this.materialShapeDrawable, list);
    }
    
    public void setCradleVerticalOffset(final float cradleVerticalOffset) {
        if (cradleVerticalOffset != this.getCradleVerticalOffset()) {
            this.topEdgeTreatment.setCradleVerticalOffset(cradleVerticalOffset);
            this.materialShapeDrawable.invalidateSelf();
        }
    }
    
    public void setFabAlignmentMode(final int fabAlignmentMode) {
        this.maybeAnimateModeChange(fabAlignmentMode);
        this.maybeAnimateMenuView(fabAlignmentMode, this.fabAttached);
        this.fabAlignmentMode = fabAlignmentMode;
    }
    
    public void setFabCradleMargin(final float fabCradleMargin) {
        if (fabCradleMargin != this.getFabCradleMargin()) {
            this.topEdgeTreatment.setFabCradleMargin(fabCradleMargin);
            this.materialShapeDrawable.invalidateSelf();
        }
    }
    
    public void setFabCradleRoundedCornerRadius(final float fabCradleRoundedCornerRadius) {
        if (fabCradleRoundedCornerRadius != this.getFabCradleRoundedCornerRadius()) {
            this.topEdgeTreatment.setFabCradleRoundedCornerRadius(fabCradleRoundedCornerRadius);
            this.materialShapeDrawable.invalidateSelf();
        }
    }
    
    void setFabDiameter(final int n) {
        final float fabDiameter = (float)n;
        if (fabDiameter != this.topEdgeTreatment.getFabDiameter()) {
            this.topEdgeTreatment.setFabDiameter(fabDiameter);
            this.materialShapeDrawable.invalidateSelf();
        }
    }
    
    public void setHideOnScroll(final boolean hideOnScroll) {
        this.hideOnScroll = hideOnScroll;
    }
    
    @Override
    public void setSubtitle(final CharSequence charSequence) {
    }
    
    @Override
    public void setTitle(final CharSequence charSequence) {
    }
    
    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar>
    {
        private final Rect fabContentRect;
        
        public Behavior() {
            this.fabContentRect = new Rect();
        }
        
        public Behavior(final Context context, final AttributeSet set) {
            super(context, set);
            this.fabContentRect = new Rect();
        }
        
        private boolean updateFabPositionAndVisibility(final FloatingActionButton floatingActionButton, final BottomAppBar bottomAppBar) {
            ((CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams()).anchorGravity = 17;
            bottomAppBar.addFabAnimationListeners(floatingActionButton);
            return true;
        }
        
        @Override
        public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final BottomAppBar bottomAppBar, final int n) {
            final FloatingActionButton access$1100 = bottomAppBar.findDependentFab();
            if (access$1100 != null) {
                this.updateFabPositionAndVisibility(access$1100, bottomAppBar);
                access$1100.getMeasuredContentRect(this.fabContentRect);
                bottomAppBar.setFabDiameter(this.fabContentRect.height());
            }
            if (!bottomAppBar.isAnimationRunning()) {
                bottomAppBar.setCutoutState();
            }
            coordinatorLayout.onLayoutChild((View)bottomAppBar, n);
            return super.onLayoutChild(coordinatorLayout, bottomAppBar, n);
        }
        
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final BottomAppBar bottomAppBar, final View view, final View view2, final int n, final int n2) {
            return bottomAppBar.getHideOnScroll() && super.onStartNestedScroll(coordinatorLayout, bottomAppBar, view, view2, n, n2);
        }
        
        @Override
        protected void slideDown(final BottomAppBar bottomAppBar) {
            super.slideDown(bottomAppBar);
            final FloatingActionButton access$1100 = bottomAppBar.findDependentFab();
            if (access$1100 != null) {
                access$1100.getContentRect(this.fabContentRect);
                final float n = (float)(access$1100.getMeasuredHeight() - this.fabContentRect.height());
                access$1100.clearAnimation();
                access$1100.animate().translationY(-access$1100.getPaddingBottom() + n).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setDuration(175L);
            }
        }
        
        @Override
        protected void slideUp(final BottomAppBar bottomAppBar) {
            super.slideUp(bottomAppBar);
            final FloatingActionButton access$1100 = bottomAppBar.findDependentFab();
            if (access$1100 != null) {
                access$1100.clearAnimation();
                access$1100.animate().translationY(bottomAppBar.getFabTranslationY()).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setDuration(225L);
            }
        }
    }
    
    static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int fabAlignmentMode;
        boolean fabAttached;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.fabAlignmentMode = parcel.readInt();
            this.fabAttached = (parcel.readInt() != 0);
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.fabAlignmentMode);
            parcel.writeInt((int)(this.fabAttached ? 1 : 0));
        }
    }
}
