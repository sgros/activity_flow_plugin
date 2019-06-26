// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.support.annotation.RequiresApi;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.v4.math.MathUtils;
import android.view.animation.Interpolator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import java.lang.ref.WeakReference;
import android.support.v4.util.ObjectsCompat;
import android.support.annotation.VisibleForTesting;
import android.view.ViewGroup$MarginLayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.content.res.TypedArray;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.design.R;
import android.view.View;
import android.os.Build$VERSION;
import android.util.AttributeSet;
import android.content.Context;
import java.util.List;
import android.support.v4.view.WindowInsetsCompat;
import android.widget.LinearLayout;

@CoordinatorLayout.DefaultBehavior(Behavior.class)
public class AppBarLayout extends LinearLayout
{
    private static final int INVALID_SCROLL_RANGE = -1;
    static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    static final int PENDING_ACTION_COLLAPSED = 2;
    static final int PENDING_ACTION_EXPANDED = 1;
    static final int PENDING_ACTION_FORCE = 8;
    static final int PENDING_ACTION_NONE = 0;
    private boolean mCollapsed;
    private boolean mCollapsible;
    private int mDownPreScrollRange;
    private int mDownScrollRange;
    private boolean mHaveChildWithInterpolator;
    private WindowInsetsCompat mLastInsets;
    private List<OnOffsetChangedListener> mListeners;
    private int mPendingAction;
    private int[] mTmpStatesArray;
    private int mTotalScrollRange;
    
    public AppBarLayout(final Context context) {
        this(context, null);
    }
    
    public AppBarLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownScrollRange = -1;
        this.mPendingAction = 0;
        this.setOrientation(1);
        ThemeUtils.checkAppCompatTheme(context);
        if (Build$VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setBoundsViewOutlineProvider((View)this);
            ViewUtilsLollipop.setStateListAnimatorFromAttrs((View)this, set, 0, R.style.Widget_Design_AppBarLayout);
        }
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout);
        ViewCompat.setBackground((View)this, obtainStyledAttributes.getDrawable(R.styleable.AppBarLayout_android_background));
        if (obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_expanded)) {
            this.setExpanded(obtainStyledAttributes.getBoolean(R.styleable.AppBarLayout_expanded, false), false, false);
        }
        if (Build$VERSION.SDK_INT >= 21 && obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_elevation)) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, (float)obtainStyledAttributes.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0));
        }
        if (Build$VERSION.SDK_INT >= 26) {
            if (obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_android_keyboardNavigationCluster)) {
                this.setKeyboardNavigationCluster(obtainStyledAttributes.getBoolean(R.styleable.AppBarLayout_android_keyboardNavigationCluster, false));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_android_touchscreenBlocksFocus)) {
                this.setTouchscreenBlocksFocus(obtainStyledAttributes.getBoolean(R.styleable.AppBarLayout_android_touchscreenBlocksFocus, false));
            }
        }
        obtainStyledAttributes.recycle();
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                return AppBarLayout.this.onWindowInsetChanged(windowInsetsCompat);
            }
        });
    }
    
    private void invalidateScrollRanges() {
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownScrollRange = -1;
    }
    
    private boolean setCollapsibleState(final boolean mCollapsible) {
        if (this.mCollapsible != mCollapsible) {
            this.mCollapsible = mCollapsible;
            this.refreshDrawableState();
            return true;
        }
        return false;
    }
    
    private void setExpanded(final boolean b, final boolean b2, final boolean b3) {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        int n2 = 0;
        int n3;
        if (b2) {
            n3 = 4;
        }
        else {
            n3 = 0;
        }
        if (b3) {
            n2 = 8;
        }
        this.mPendingAction = (n | n3 | n2);
        this.requestLayout();
    }
    
    private void updateCollapsible() {
        final int childCount = this.getChildCount();
        final boolean b = false;
        int n = 0;
        boolean collapsibleState;
        while (true) {
            collapsibleState = b;
            if (n >= childCount) {
                break;
            }
            if (((LayoutParams)this.getChildAt(n).getLayoutParams()).isCollapsible()) {
                collapsibleState = true;
                break;
            }
            ++n;
        }
        this.setCollapsibleState(collapsibleState);
    }
    
    public void addOnOffsetChangedListener(final OnOffsetChangedListener onOffsetChangedListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<OnOffsetChangedListener>();
        }
        if (onOffsetChangedListener != null && !this.mListeners.contains(onOffsetChangedListener)) {
            this.mListeners.add(onOffsetChangedListener);
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    void dispatchOffsetUpdates(final int n) {
        if (this.mListeners != null) {
            for (int i = 0; i < this.mListeners.size(); ++i) {
                final OnOffsetChangedListener onOffsetChangedListener = this.mListeners.get(i);
                if (onOffsetChangedListener != null) {
                    onOffsetChangedListener.onOffsetChanged(this, n);
                }
            }
        }
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (Build$VERSION.SDK_INT >= 19 && viewGroup$LayoutParams instanceof LinearLayout$LayoutParams) {
            return new LayoutParams((LinearLayout$LayoutParams)viewGroup$LayoutParams);
        }
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    int getDownNestedPreScrollRange() {
        if (this.mDownPreScrollRange != -1) {
            return this.mDownPreScrollRange;
        }
        int i = this.getChildCount() - 1;
        int b = 0;
        while (i >= 0) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            final int measuredHeight = child.getMeasuredHeight();
            final int mScrollFlags = layoutParams.mScrollFlags;
            int n2;
            if ((mScrollFlags & 0x5) == 0x5) {
                final int n = b + (layoutParams.topMargin + layoutParams.bottomMargin);
                if ((mScrollFlags & 0x8) != 0x0) {
                    n2 = n + ViewCompat.getMinimumHeight(child);
                }
                else if ((mScrollFlags & 0x2) != 0x0) {
                    n2 = n + (measuredHeight - ViewCompat.getMinimumHeight(child));
                }
                else {
                    n2 = n + (measuredHeight - this.getTopInset());
                }
            }
            else if ((n2 = b) > 0) {
                break;
            }
            --i;
            b = n2;
        }
        return this.mDownPreScrollRange = Math.max(0, b);
    }
    
    int getDownNestedScrollRange() {
        if (this.mDownScrollRange != -1) {
            return this.mDownScrollRange;
        }
        final int childCount = this.getChildCount();
        int n2;
        int n = n2 = 0;
        int b;
        while (true) {
            b = n2;
            if (n >= childCount) {
                break;
            }
            final View child = this.getChildAt(n);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            final int measuredHeight = child.getMeasuredHeight();
            final int topMargin = layoutParams.topMargin;
            final int bottomMargin = layoutParams.bottomMargin;
            final int mScrollFlags = layoutParams.mScrollFlags;
            b = n2;
            if ((mScrollFlags & 0x1) == 0x0) {
                break;
            }
            n2 += measuredHeight + (topMargin + bottomMargin);
            if ((mScrollFlags & 0x2) != 0x0) {
                b = n2 - (ViewCompat.getMinimumHeight(child) + this.getTopInset());
                break;
            }
            ++n;
        }
        return this.mDownScrollRange = Math.max(0, b);
    }
    
    final int getMinimumHeightForVisibleOverlappingContent() {
        final int topInset = this.getTopInset();
        final int minimumHeight = ViewCompat.getMinimumHeight((View)this);
        if (minimumHeight != 0) {
            return minimumHeight * 2 + topInset;
        }
        final int childCount = this.getChildCount();
        int minimumHeight2;
        if (childCount >= 1) {
            minimumHeight2 = ViewCompat.getMinimumHeight(this.getChildAt(childCount - 1));
        }
        else {
            minimumHeight2 = 0;
        }
        if (minimumHeight2 != 0) {
            return minimumHeight2 * 2 + topInset;
        }
        return this.getHeight() / 3;
    }
    
    int getPendingAction() {
        return this.mPendingAction;
    }
    
    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }
    
    @VisibleForTesting
    final int getTopInset() {
        int systemWindowInsetTop;
        if (this.mLastInsets != null) {
            systemWindowInsetTop = this.mLastInsets.getSystemWindowInsetTop();
        }
        else {
            systemWindowInsetTop = 0;
        }
        return systemWindowInsetTop;
    }
    
    public final int getTotalScrollRange() {
        if (this.mTotalScrollRange != -1) {
            return this.mTotalScrollRange;
        }
        final int childCount = this.getChildCount();
        int n2;
        int n = n2 = 0;
        int n3;
        while (true) {
            n3 = n2;
            if (n >= childCount) {
                break;
            }
            final View child = this.getChildAt(n);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            final int measuredHeight = child.getMeasuredHeight();
            final int mScrollFlags = layoutParams.mScrollFlags;
            n3 = n2;
            if ((mScrollFlags & 0x1) == 0x0) {
                break;
            }
            n2 += measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
            if ((mScrollFlags & 0x2) != 0x0) {
                n3 = n2 - ViewCompat.getMinimumHeight(child);
                break;
            }
            ++n;
        }
        return this.mTotalScrollRange = Math.max(0, n3 - this.getTopInset());
    }
    
    int getUpNestedPreScrollRange() {
        return this.getTotalScrollRange();
    }
    
    boolean hasChildWithInterpolator() {
        return this.mHaveChildWithInterpolator;
    }
    
    boolean hasScrollableChildren() {
        return this.getTotalScrollRange() != 0;
    }
    
    protected int[] onCreateDrawableState(int n) {
        if (this.mTmpStatesArray == null) {
            this.mTmpStatesArray = new int[2];
        }
        final int[] mTmpStatesArray = this.mTmpStatesArray;
        final int[] onCreateDrawableState = super.onCreateDrawableState(n + mTmpStatesArray.length);
        if (this.mCollapsible) {
            n = R.attr.state_collapsible;
        }
        else {
            n = -R.attr.state_collapsible;
        }
        mTmpStatesArray[0] = n;
        if (this.mCollapsible && this.mCollapsed) {
            n = R.attr.state_collapsed;
        }
        else {
            n = -R.attr.state_collapsed;
        }
        mTmpStatesArray[1] = n;
        return mergeDrawableStates(onCreateDrawableState, mTmpStatesArray);
    }
    
    protected void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
        super.onLayout(b, i, childCount, n, n2);
        this.invalidateScrollRanges();
        i = 0;
        this.mHaveChildWithInterpolator = false;
        for (childCount = this.getChildCount(); i < childCount; ++i) {
            if (((LayoutParams)this.getChildAt(i).getLayoutParams()).getScrollInterpolator() != null) {
                this.mHaveChildWithInterpolator = true;
                break;
            }
        }
        this.updateCollapsible();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.invalidateScrollRanges();
    }
    
    WindowInsetsCompat onWindowInsetChanged(final WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat mLastInsets;
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            mLastInsets = windowInsetsCompat;
        }
        else {
            mLastInsets = null;
        }
        if (!ObjectsCompat.equals(this.mLastInsets, mLastInsets)) {
            this.mLastInsets = mLastInsets;
            this.invalidateScrollRanges();
        }
        return windowInsetsCompat;
    }
    
    public void removeOnOffsetChangedListener(final OnOffsetChangedListener onOffsetChangedListener) {
        if (this.mListeners != null && onOffsetChangedListener != null) {
            this.mListeners.remove(onOffsetChangedListener);
        }
    }
    
    void resetPendingAction() {
        this.mPendingAction = 0;
    }
    
    boolean setCollapsedState(final boolean mCollapsed) {
        if (this.mCollapsed != mCollapsed) {
            this.mCollapsed = mCollapsed;
            this.refreshDrawableState();
            return true;
        }
        return false;
    }
    
    public void setExpanded(final boolean b) {
        this.setExpanded(b, ViewCompat.isLaidOut((View)this));
    }
    
    public void setExpanded(final boolean b, final boolean b2) {
        this.setExpanded(b, b2, true);
    }
    
    public void setOrientation(final int orientation) {
        if (orientation != 1) {
            throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
        }
        super.setOrientation(orientation);
    }
    
    @Deprecated
    public void setTargetElevation(final float n) {
        if (Build$VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, n);
        }
    }
    
    public static class Behavior extends HeaderBehavior<AppBarLayout>
    {
        private static final int INVALID_POSITION = -1;
        private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
        private WeakReference<View> mLastNestedScrollingChildRef;
        private ValueAnimator mOffsetAnimator;
        private int mOffsetDelta;
        private int mOffsetToChildIndexOnLayout;
        private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
        private float mOffsetToChildIndexOnLayoutPerc;
        private DragCallback mOnDragCallback;
        
        public Behavior() {
            this.mOffsetToChildIndexOnLayout = -1;
        }
        
        public Behavior(final Context context, final AttributeSet set) {
            super(context, set);
            this.mOffsetToChildIndexOnLayout = -1;
        }
        
        private void animateOffsetTo(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final int n, float abs) {
            final int abs2 = Math.abs(this.getTopBottomOffsetForScrollingSibling() - n);
            abs = Math.abs(abs);
            int n2;
            if (abs > 0.0f) {
                n2 = 3 * Math.round(1000.0f * (abs2 / abs));
            }
            else {
                n2 = (int)((abs2 / (float)appBarLayout.getHeight() + 1.0f) * 150.0f);
            }
            this.animateOffsetWithDuration(coordinatorLayout, appBarLayout, n, n2);
        }
        
        private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final int n, final int a) {
            final int topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
            if (topBottomOffsetForScrollingSibling == n) {
                if (this.mOffsetAnimator != null && this.mOffsetAnimator.isRunning()) {
                    this.mOffsetAnimator.cancel();
                }
                return;
            }
            if (this.mOffsetAnimator == null) {
                (this.mOffsetAnimator = new ValueAnimator()).setInterpolator((TimeInterpolator)AnimationUtils.DECELERATE_INTERPOLATOR);
                this.mOffsetAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                        Behavior.this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, (int)valueAnimator.getAnimatedValue());
                    }
                });
            }
            else {
                this.mOffsetAnimator.cancel();
            }
            this.mOffsetAnimator.setDuration((long)Math.min(a, 600));
            this.mOffsetAnimator.setIntValues(new int[] { topBottomOffsetForScrollingSibling, n });
            this.mOffsetAnimator.start();
        }
        
        private static boolean checkFlag(final int n, final int n2) {
            return (n & n2) == n2;
        }
        
        private static View getAppBarChildOnOffset(final AppBarLayout appBarLayout, int i) {
            final int abs = Math.abs(i);
            int childCount;
            View child;
            for (childCount = appBarLayout.getChildCount(), i = 0; i < childCount; ++i) {
                child = appBarLayout.getChildAt(i);
                if (abs >= child.getTop() && abs <= child.getBottom()) {
                    return child;
                }
            }
            return null;
        }
        
        private int getChildIndexOnOffset(final AppBarLayout appBarLayout, final int n) {
            for (int childCount = appBarLayout.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = appBarLayout.getChildAt(i);
                final int top = child.getTop();
                final int n2 = -n;
                if (top <= n2 && child.getBottom() >= n2) {
                    return i;
                }
            }
            return -1;
        }
        
        private int interpolateOffset(final AppBarLayout appBarLayout, final int n) {
            final int abs = Math.abs(n);
            final int childCount = appBarLayout.getChildCount();
            final int n2 = 0;
            int i = 0;
            while (i < childCount) {
                final View child = appBarLayout.getChildAt(i);
                final AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)child.getLayoutParams();
                final Interpolator scrollInterpolator = layoutParams.getScrollInterpolator();
                if (abs >= child.getTop() && abs <= child.getBottom()) {
                    if (scrollInterpolator == null) {
                        break;
                    }
                    final int scrollFlags = layoutParams.getScrollFlags();
                    int n3 = n2;
                    if ((scrollFlags & 0x1) != 0x0) {
                        final int n4 = n3 = 0 + (child.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                        if ((scrollFlags & 0x2) != 0x0) {
                            n3 = n4 - ViewCompat.getMinimumHeight(child);
                        }
                    }
                    int n5 = n3;
                    if (ViewCompat.getFitsSystemWindows(child)) {
                        n5 = n3 - appBarLayout.getTopInset();
                    }
                    if (n5 > 0) {
                        final int top = child.getTop();
                        final float n6 = (float)n5;
                        return Integer.signum(n) * (child.getTop() + Math.round(n6 * scrollInterpolator.getInterpolation((abs - top) / n6)));
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
            return n;
        }
        
        private boolean shouldJumpElevationState(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout) {
            final List<View> dependents = coordinatorLayout.getDependents((View)appBarLayout);
            final int size = dependents.size();
            boolean b = false;
            for (int i = 0; i < size; ++i) {
                final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)dependents.get(i).getLayoutParams()).getBehavior();
                if (behavior instanceof ScrollingViewBehavior) {
                    if (((ScrollingViewBehavior)behavior).getOverlayTop() != 0) {
                        b = true;
                    }
                    return b;
                }
            }
            return false;
        }
        
        private void snapToChildIfNeeded(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout) {
            final int topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
            final int childIndexOnOffset = this.getChildIndexOnOffset(appBarLayout, topBottomOffsetForScrollingSibling);
            if (childIndexOnOffset >= 0) {
                final View child = appBarLayout.getChildAt(childIndexOnOffset);
                final int scrollFlags = ((AppBarLayout.LayoutParams)child.getLayoutParams()).getScrollFlags();
                if ((scrollFlags & 0x11) == 0x11) {
                    final int n = -child.getTop();
                    int n3;
                    final int n2 = n3 = -child.getBottom();
                    if (childIndexOnOffset == appBarLayout.getChildCount() - 1) {
                        n3 = n2 + appBarLayout.getTopInset();
                    }
                    int n4;
                    int n5;
                    if (checkFlag(scrollFlags, 2)) {
                        n4 = n3 + ViewCompat.getMinimumHeight(child);
                        n5 = n;
                    }
                    else {
                        n5 = n;
                        n4 = n3;
                        if (checkFlag(scrollFlags, 5)) {
                            n4 = ViewCompat.getMinimumHeight(child) + n3;
                            if (topBottomOffsetForScrollingSibling < n4) {
                                n5 = n4;
                                n4 = n3;
                            }
                            else {
                                n5 = n;
                            }
                        }
                    }
                    int n6 = n5;
                    if (topBottomOffsetForScrollingSibling < (n4 + n5) / 2) {
                        n6 = n4;
                    }
                    this.animateOffsetTo(coordinatorLayout, appBarLayout, MathUtils.clamp(n6, -appBarLayout.getTotalScrollRange(), 0), 0.0f);
                }
            }
        }
        
        private void updateAppBarLayoutDrawableState(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final int n, final int n2, final boolean b) {
            final View appBarChildOnOffset = getAppBarChildOnOffset(appBarLayout, n);
            if (appBarChildOnOffset != null) {
                final int scrollFlags = ((AppBarLayout.LayoutParams)appBarChildOnOffset.getLayoutParams()).getScrollFlags();
                boolean collapsedState;
                final boolean b2 = collapsedState = false;
                Label_0123: {
                    if ((scrollFlags & 0x1) != 0x0) {
                        final int minimumHeight = ViewCompat.getMinimumHeight(appBarChildOnOffset);
                        if (n2 > 0 && (scrollFlags & 0xC) != 0x0) {
                            collapsedState = b2;
                            if (-n < appBarChildOnOffset.getBottom() - minimumHeight - appBarLayout.getTopInset()) {
                                break Label_0123;
                            }
                        }
                        else {
                            collapsedState = b2;
                            if ((scrollFlags & 0x2) == 0x0) {
                                break Label_0123;
                            }
                            collapsedState = b2;
                            if (-n < appBarChildOnOffset.getBottom() - minimumHeight - appBarLayout.getTopInset()) {
                                break Label_0123;
                            }
                        }
                        collapsedState = true;
                    }
                }
                final boolean setCollapsedState = appBarLayout.setCollapsedState(collapsedState);
                if (Build$VERSION.SDK_INT >= 11 && (b || (setCollapsedState && this.shouldJumpElevationState(coordinatorLayout, appBarLayout)))) {
                    appBarLayout.jumpDrawablesToCurrentState();
                }
            }
        }
        
        @Override
        boolean canDragView(final AppBarLayout appBarLayout) {
            if (this.mOnDragCallback != null) {
                return this.mOnDragCallback.canDrag(appBarLayout);
            }
            final WeakReference<View> mLastNestedScrollingChildRef = this.mLastNestedScrollingChildRef;
            boolean b = true;
            if (mLastNestedScrollingChildRef != null) {
                final View view = this.mLastNestedScrollingChildRef.get();
                if (view == null || !view.isShown() || view.canScrollVertically(-1)) {
                    b = false;
                }
                return b;
            }
            return true;
        }
        
        @Override
        int getMaxDragOffset(final AppBarLayout appBarLayout) {
            return -appBarLayout.getDownNestedScrollRange();
        }
        
        @Override
        int getScrollRangeForDragFling(final AppBarLayout appBarLayout) {
            return appBarLayout.getTotalScrollRange();
        }
        
        @Override
        int getTopBottomOffsetForScrollingSibling() {
            return this.getTopAndBottomOffset() + this.mOffsetDelta;
        }
        
        @VisibleForTesting
        boolean isOffsetAnimatorRunning() {
            return this.mOffsetAnimator != null && this.mOffsetAnimator.isRunning();
        }
        
        @Override
        void onFlingFinished(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout) {
            this.snapToChildIfNeeded(coordinatorLayout, appBarLayout);
        }
        
        @Override
        public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, int n) {
            final boolean onLayoutChild = super.onLayoutChild(coordinatorLayout, appBarLayout, n);
            final int pendingAction = appBarLayout.getPendingAction();
            if (this.mOffsetToChildIndexOnLayout >= 0 && (pendingAction & 0x8) == 0x0) {
                final View child = appBarLayout.getChildAt(this.mOffsetToChildIndexOnLayout);
                n = -child.getBottom();
                if (this.mOffsetToChildIndexOnLayoutIsMinHeight) {
                    n += ViewCompat.getMinimumHeight(child) + appBarLayout.getTopInset();
                }
                else {
                    n += Math.round(child.getHeight() * this.mOffsetToChildIndexOnLayoutPerc);
                }
                this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, n);
            }
            else if (pendingAction != 0) {
                if ((pendingAction & 0x4) != 0x0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                if ((pendingAction & 0x2) != 0x0) {
                    final int n2 = -appBarLayout.getUpNestedPreScrollRange();
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, appBarLayout, n2, 0.0f);
                    }
                    else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, n2);
                    }
                }
                else if ((pendingAction & 0x1) != 0x0) {
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, appBarLayout, 0, 0.0f);
                    }
                    else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, 0);
                    }
                }
            }
            appBarLayout.resetPendingAction();
            this.mOffsetToChildIndexOnLayout = -1;
            this.setTopAndBottomOffset(MathUtils.clamp(this.getTopAndBottomOffset(), -appBarLayout.getTotalScrollRange(), 0));
            this.updateAppBarLayoutDrawableState(coordinatorLayout, appBarLayout, this.getTopAndBottomOffset(), 0, true);
            appBarLayout.dispatchOffsetUpdates(this.getTopAndBottomOffset());
            return onLayoutChild;
        }
        
        public boolean onMeasureChild(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final int n, final int n2, final int n3, final int n4) {
            if (((CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams()).height == -2) {
                coordinatorLayout.onMeasureChild((View)appBarLayout, n, n2, View$MeasureSpec.makeMeasureSpec(0, 0), n4);
                return true;
            }
            return super.onMeasureChild(coordinatorLayout, appBarLayout, n, n2, n3, n4);
        }
        
        public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final View view, int n, final int n2, final int[] array, int n3) {
            if (n2 != 0) {
                if (n2 < 0) {
                    n3 = -appBarLayout.getTotalScrollRange();
                    n = appBarLayout.getDownNestedPreScrollRange() + n3;
                }
                else {
                    n3 = -appBarLayout.getUpNestedPreScrollRange();
                    n = 0;
                }
                if (n3 != n) {
                    array[1] = this.scroll(coordinatorLayout, appBarLayout, n2, n3, n);
                }
            }
        }
        
        public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
            if (n4 < 0) {
                this.scroll(coordinatorLayout, appBarLayout, n4, -appBarLayout.getDownNestedScrollRange(), 0);
            }
        }
        
        public void onRestoreInstanceState(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final Parcelable parcelable) {
            if (parcelable instanceof SavedState) {
                final SavedState savedState = (SavedState)parcelable;
                super.onRestoreInstanceState(coordinatorLayout, appBarLayout, savedState.getSuperState());
                this.mOffsetToChildIndexOnLayout = savedState.firstVisibleChildIndex;
                this.mOffsetToChildIndexOnLayoutPerc = savedState.firstVisibleChildPercentageShown;
                this.mOffsetToChildIndexOnLayoutIsMinHeight = savedState.firstVisibleChildAtMinimumHeight;
            }
            else {
                super.onRestoreInstanceState(coordinatorLayout, appBarLayout, parcelable);
                this.mOffsetToChildIndexOnLayout = -1;
            }
        }
        
        public Parcelable onSaveInstanceState(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout) {
            final Parcelable onSaveInstanceState = super.onSaveInstanceState(coordinatorLayout, appBarLayout);
            final int topAndBottomOffset = this.getTopAndBottomOffset();
            final int childCount = appBarLayout.getChildCount();
            boolean firstVisibleChildAtMinimumHeight = false;
            for (int i = 0; i < childCount; ++i) {
                final View child = appBarLayout.getChildAt(i);
                final int n = child.getBottom() + topAndBottomOffset;
                if (child.getTop() + topAndBottomOffset <= 0 && n >= 0) {
                    final SavedState savedState = new SavedState(onSaveInstanceState);
                    savedState.firstVisibleChildIndex = i;
                    if (n == ViewCompat.getMinimumHeight(child) + appBarLayout.getTopInset()) {
                        firstVisibleChildAtMinimumHeight = true;
                    }
                    savedState.firstVisibleChildAtMinimumHeight = firstVisibleChildAtMinimumHeight;
                    savedState.firstVisibleChildPercentageShown = n / (float)child.getHeight();
                    return (Parcelable)savedState;
                }
            }
            return onSaveInstanceState;
        }
        
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final View view, final View view2, final int n, final int n2) {
            final boolean b = (n & 0x2) != 0x0 && appBarLayout.hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight();
            if (b && this.mOffsetAnimator != null) {
                this.mOffsetAnimator.cancel();
            }
            this.mLastNestedScrollingChildRef = null;
            return b;
        }
        
        public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final View referent, final int n) {
            if (n == 0) {
                this.snapToChildIfNeeded(coordinatorLayout, appBarLayout);
            }
            this.mLastNestedScrollingChildRef = new WeakReference<View>(referent);
        }
        
        public void setDragCallback(@Nullable final DragCallback mOnDragCallback) {
            this.mOnDragCallback = mOnDragCallback;
        }
        
        @Override
        int setHeaderTopBottomOffset(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, int interpolateOffset, int clamp, int n) {
            final int topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
            final int n2 = 0;
            if (clamp != 0 && topBottomOffsetForScrollingSibling >= clamp && topBottomOffsetForScrollingSibling <= n) {
                clamp = MathUtils.clamp(interpolateOffset, clamp, n);
                interpolateOffset = n2;
                if (topBottomOffsetForScrollingSibling != clamp) {
                    if (appBarLayout.hasChildWithInterpolator()) {
                        interpolateOffset = this.interpolateOffset(appBarLayout, clamp);
                    }
                    else {
                        interpolateOffset = clamp;
                    }
                    final boolean setTopAndBottomOffset = this.setTopAndBottomOffset(interpolateOffset);
                    n = topBottomOffsetForScrollingSibling - clamp;
                    this.mOffsetDelta = clamp - interpolateOffset;
                    if (!setTopAndBottomOffset && appBarLayout.hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged((View)appBarLayout);
                    }
                    appBarLayout.dispatchOffsetUpdates(this.getTopAndBottomOffset());
                    if (clamp < topBottomOffsetForScrollingSibling) {
                        interpolateOffset = -1;
                    }
                    else {
                        interpolateOffset = 1;
                    }
                    this.updateAppBarLayoutDrawableState(coordinatorLayout, appBarLayout, clamp, interpolateOffset, false);
                    interpolateOffset = n;
                }
            }
            else {
                this.mOffsetDelta = 0;
                interpolateOffset = n2;
            }
            return interpolateOffset;
        }
        
        public abstract static class DragCallback
        {
            public abstract boolean canDrag(@NonNull final AppBarLayout p0);
        }
        
        protected static class SavedState extends AbsSavedState
        {
            public static final Parcelable$Creator<SavedState> CREATOR;
            boolean firstVisibleChildAtMinimumHeight;
            int firstVisibleChildIndex;
            float firstVisibleChildPercentageShown;
            
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
                this.firstVisibleChildIndex = parcel.readInt();
                this.firstVisibleChildPercentageShown = parcel.readFloat();
                this.firstVisibleChildAtMinimumHeight = (parcel.readByte() != 0);
            }
            
            public SavedState(final Parcelable parcelable) {
                super(parcelable);
            }
            
            @Override
            public void writeToParcel(final Parcel parcel, final int n) {
                super.writeToParcel(parcel, n);
                parcel.writeInt(this.firstVisibleChildIndex);
                parcel.writeFloat(this.firstVisibleChildPercentageShown);
                parcel.writeByte((byte)(byte)(this.firstVisibleChildAtMinimumHeight ? 1 : 0));
            }
        }
    }
    
    public static class LayoutParams extends LinearLayout$LayoutParams
    {
        static final int COLLAPSIBLE_FLAGS = 10;
        static final int FLAG_QUICK_RETURN = 5;
        static final int FLAG_SNAP = 17;
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
        public static final int SCROLL_FLAG_SCROLL = 1;
        public static final int SCROLL_FLAG_SNAP = 16;
        int mScrollFlags;
        Interpolator mScrollInterpolator;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mScrollFlags = 1;
        }
        
        public LayoutParams(final int n, final int n2, final float n3) {
            super(n, n2, n3);
            this.mScrollFlags = 1;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mScrollFlags = 1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AppBarLayout_Layout);
            this.mScrollFlags = obtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            if (obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
                this.mScrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, obtainStyledAttributes.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
            }
            obtainStyledAttributes.recycle();
        }
        
        @RequiresApi(19)
        public LayoutParams(final LayoutParams layoutParams) {
            super((LinearLayout$LayoutParams)layoutParams);
            this.mScrollFlags = 1;
            this.mScrollFlags = layoutParams.mScrollFlags;
            this.mScrollInterpolator = layoutParams.mScrollInterpolator;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mScrollFlags = 1;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mScrollFlags = 1;
        }
        
        @RequiresApi(19)
        public LayoutParams(final LinearLayout$LayoutParams linearLayout$LayoutParams) {
            super(linearLayout$LayoutParams);
            this.mScrollFlags = 1;
        }
        
        public int getScrollFlags() {
            return this.mScrollFlags;
        }
        
        public Interpolator getScrollInterpolator() {
            return this.mScrollInterpolator;
        }
        
        boolean isCollapsible() {
            final int mScrollFlags = this.mScrollFlags;
            boolean b = true;
            if ((mScrollFlags & 0x1) != 0x1 || (this.mScrollFlags & 0xA) == 0x0) {
                b = false;
            }
            return b;
        }
        
        public void setScrollFlags(final int mScrollFlags) {
            this.mScrollFlags = mScrollFlags;
        }
        
        public void setScrollInterpolator(final Interpolator mScrollInterpolator) {
            this.mScrollInterpolator = mScrollInterpolator;
        }
        
        @Retention(RetentionPolicy.SOURCE)
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public @interface ScrollFlags {
        }
    }
    
    public interface OnOffsetChangedListener
    {
        void onOffsetChanged(final AppBarLayout p0, final int p1);
    }
    
    public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior
    {
        public ScrollingViewBehavior() {
        }
        
        public ScrollingViewBehavior(final Context context, final AttributeSet set) {
            super(context, set);
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ScrollingViewBehavior_Layout);
            this.setOverlayTop(obtainStyledAttributes.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            obtainStyledAttributes.recycle();
        }
        
        private static int getAppBarLayoutOffset(final AppBarLayout appBarLayout) {
            final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams()).getBehavior();
            if (behavior instanceof AppBarLayout.Behavior) {
                return ((AppBarLayout.Behavior)behavior).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }
        
        private void offsetChildAsNeeded(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
            final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)view2.getLayoutParams()).getBehavior();
            if (behavior instanceof AppBarLayout.Behavior) {
                ViewCompat.offsetTopAndBottom(view, view2.getBottom() - view.getTop() + ((AppBarLayout.Behavior)behavior).mOffsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(view2));
            }
        }
        
        AppBarLayout findFirstDependency(final List<View> list) {
            for (int size = list.size(), i = 0; i < size; ++i) {
                final View view = list.get(i);
                if (view instanceof AppBarLayout) {
                    return (AppBarLayout)view;
                }
            }
            return null;
        }
        
        @Override
        float getOverlapRatioForOffset(final View view) {
            if (view instanceof AppBarLayout) {
                final AppBarLayout appBarLayout = (AppBarLayout)view;
                final int totalScrollRange = appBarLayout.getTotalScrollRange();
                final int downNestedPreScrollRange = appBarLayout.getDownNestedPreScrollRange();
                final int appBarLayoutOffset = getAppBarLayoutOffset(appBarLayout);
                if (downNestedPreScrollRange != 0 && totalScrollRange + appBarLayoutOffset <= downNestedPreScrollRange) {
                    return 0.0f;
                }
                final int n = totalScrollRange - downNestedPreScrollRange;
                if (n != 0) {
                    return 1.0f + appBarLayoutOffset / (float)n;
                }
            }
            return 0.0f;
        }
        
        @Override
        int getScrollRange(final View view) {
            if (view instanceof AppBarLayout) {
                return ((AppBarLayout)view).getTotalScrollRange();
            }
            return super.getScrollRange(view);
        }
        
        @Override
        public boolean layoutDependsOn(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
            return view2 instanceof AppBarLayout;
        }
        
        @Override
        public boolean onDependentViewChanged(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
            this.offsetChildAsNeeded(coordinatorLayout, view, view2);
            return false;
        }
        
        @Override
        public boolean onRequestChildRectangleOnScreen(final CoordinatorLayout coordinatorLayout, final View view, final Rect rect, final boolean b) {
            final AppBarLayout firstDependency = this.findFirstDependency(coordinatorLayout.getDependencies(view));
            if (firstDependency != null) {
                rect.offset(view.getLeft(), view.getTop());
                final Rect mTempRect1 = this.mTempRect1;
                mTempRect1.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                if (!mTempRect1.contains(rect)) {
                    firstDependency.setExpanded(false, b ^ true);
                    return true;
                }
            }
            return false;
        }
    }
}
