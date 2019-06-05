// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.v4.math.MathUtils;
import android.view.animation.Interpolator;
import android.support.v4.view.NestedScrollingChild;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.support.design.animation.AnimationUtils;
import android.animation.ValueAnimator;
import java.lang.ref.WeakReference;
import android.support.v4.util.ObjectsCompat;
import android.view.ViewGroup$MarginLayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.content.res.TypedArray;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.design.internal.ThemeEnforcement;
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
    private int downPreScrollRange;
    private int downScrollRange;
    private boolean haveChildWithInterpolator;
    private WindowInsetsCompat lastInsets;
    private boolean liftOnScroll;
    private boolean liftable;
    private boolean liftableOverride;
    private boolean lifted;
    private List<BaseOnOffsetChangedListener> listeners;
    private int pendingAction;
    private int[] tmpStatesArray;
    private int totalScrollRange;
    
    public AppBarLayout(final Context context) {
        this(context, null);
    }
    
    public AppBarLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
        this.pendingAction = 0;
        this.setOrientation(1);
        if (Build$VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setBoundsViewOutlineProvider((View)this);
            ViewUtilsLollipop.setStateListAnimatorFromAttrs((View)this, set, 0, R.style.Widget_Design_AppBarLayout);
        }
        final TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, set, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout, new int[0]);
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
        this.liftOnScroll = obtainStyledAttributes.getBoolean(R.styleable.AppBarLayout_liftOnScroll, false);
        obtainStyledAttributes.recycle();
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                return AppBarLayout.this.onWindowInsetChanged(windowInsetsCompat);
            }
        });
    }
    
    private boolean hasCollapsibleChild() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            if (((LayoutParams)this.getChildAt(i).getLayoutParams()).isCollapsible()) {
                return true;
            }
        }
        return false;
    }
    
    private void invalidateScrollRanges() {
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
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
        this.pendingAction = (n | n3 | n2);
        this.requestLayout();
    }
    
    private boolean setLiftableState(final boolean liftable) {
        if (this.liftable != liftable) {
            this.liftable = liftable;
            this.refreshDrawableState();
            return true;
        }
        return false;
    }
    
    public void addOnOffsetChangedListener(final BaseOnOffsetChangedListener baseOnOffsetChangedListener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<BaseOnOffsetChangedListener>();
        }
        if (baseOnOffsetChangedListener != null && !this.listeners.contains(baseOnOffsetChangedListener)) {
            this.listeners.add(baseOnOffsetChangedListener);
        }
    }
    
    public void addOnOffsetChangedListener(final OnOffsetChangedListener onOffsetChangedListener) {
        this.addOnOffsetChangedListener((BaseOnOffsetChangedListener)onOffsetChangedListener);
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    void dispatchOffsetUpdates(final int n) {
        if (this.listeners != null) {
            for (int i = 0; i < this.listeners.size(); ++i) {
                final BaseOnOffsetChangedListener<AppBarLayout> baseOnOffsetChangedListener = this.listeners.get(i);
                if (baseOnOffsetChangedListener != null) {
                    baseOnOffsetChangedListener.onOffsetChanged(this, n);
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
        if (this.downPreScrollRange != -1) {
            return this.downPreScrollRange;
        }
        int i = this.getChildCount() - 1;
        int b = 0;
        while (i >= 0) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            final int measuredHeight = child.getMeasuredHeight();
            final int scrollFlags = layoutParams.scrollFlags;
            int n2;
            if ((scrollFlags & 0x5) == 0x5) {
                final int n = b + (layoutParams.topMargin + layoutParams.bottomMargin);
                if ((scrollFlags & 0x8) != 0x0) {
                    n2 = n + ViewCompat.getMinimumHeight(child);
                }
                else if ((scrollFlags & 0x2) != 0x0) {
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
        return this.downPreScrollRange = Math.max(0, b);
    }
    
    int getDownNestedScrollRange() {
        if (this.downScrollRange != -1) {
            return this.downScrollRange;
        }
        final int childCount = this.getChildCount();
        int n = 0;
        int n2 = 0;
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
            final int scrollFlags = layoutParams.scrollFlags;
            b = n2;
            if ((scrollFlags & 0x1) == 0x0) {
                break;
            }
            n2 += measuredHeight + (topMargin + bottomMargin);
            if ((scrollFlags & 0x2) != 0x0) {
                b = n2 - (ViewCompat.getMinimumHeight(child) + this.getTopInset());
                break;
            }
            ++n;
        }
        return this.downScrollRange = Math.max(0, b);
    }
    
    public final int getMinimumHeightForVisibleOverlappingContent() {
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
        return this.pendingAction;
    }
    
    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }
    
    final int getTopInset() {
        int systemWindowInsetTop;
        if (this.lastInsets != null) {
            systemWindowInsetTop = this.lastInsets.getSystemWindowInsetTop();
        }
        else {
            systemWindowInsetTop = 0;
        }
        return systemWindowInsetTop;
    }
    
    public final int getTotalScrollRange() {
        if (this.totalScrollRange != -1) {
            return this.totalScrollRange;
        }
        final int childCount = this.getChildCount();
        int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n2;
            if (n >= childCount) {
                break;
            }
            final View child = this.getChildAt(n);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            final int measuredHeight = child.getMeasuredHeight();
            final int scrollFlags = layoutParams.scrollFlags;
            n3 = n2;
            if ((scrollFlags & 0x1) == 0x0) {
                break;
            }
            n2 += measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
            if ((scrollFlags & 0x2) != 0x0) {
                n3 = n2 - ViewCompat.getMinimumHeight(child);
                break;
            }
            ++n;
        }
        return this.totalScrollRange = Math.max(0, n3 - this.getTopInset());
    }
    
    int getUpNestedPreScrollRange() {
        return this.getTotalScrollRange();
    }
    
    boolean hasChildWithInterpolator() {
        return this.haveChildWithInterpolator;
    }
    
    boolean hasScrollableChildren() {
        return this.getTotalScrollRange() != 0;
    }
    
    public boolean isLiftOnScroll() {
        return this.liftOnScroll;
    }
    
    protected int[] onCreateDrawableState(int n) {
        if (this.tmpStatesArray == null) {
            this.tmpStatesArray = new int[4];
        }
        final int[] tmpStatesArray = this.tmpStatesArray;
        final int[] onCreateDrawableState = super.onCreateDrawableState(n + tmpStatesArray.length);
        if (this.liftable) {
            n = R.attr.state_liftable;
        }
        else {
            n = -R.attr.state_liftable;
        }
        tmpStatesArray[0] = n;
        if (this.liftable && this.lifted) {
            n = R.attr.state_lifted;
        }
        else {
            n = -R.attr.state_lifted;
        }
        tmpStatesArray[1] = n;
        if (this.liftable) {
            n = R.attr.state_collapsible;
        }
        else {
            n = -R.attr.state_collapsible;
        }
        tmpStatesArray[2] = n;
        if (this.liftable && this.lifted) {
            n = R.attr.state_collapsed;
        }
        else {
            n = -R.attr.state_collapsed;
        }
        tmpStatesArray[3] = n;
        return mergeDrawableStates(onCreateDrawableState, tmpStatesArray);
    }
    
    protected void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
        super.onLayout(b, i, childCount, n, n2);
        this.invalidateScrollRanges();
        boolean liftableState = false;
        this.haveChildWithInterpolator = false;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            if (((LayoutParams)this.getChildAt(i).getLayoutParams()).getScrollInterpolator() != null) {
                this.haveChildWithInterpolator = true;
                break;
            }
        }
        if (!this.liftableOverride) {
            if (this.liftOnScroll || this.hasCollapsibleChild()) {
                liftableState = true;
            }
            this.setLiftableState(liftableState);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.invalidateScrollRanges();
    }
    
    WindowInsetsCompat onWindowInsetChanged(final WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat lastInsets;
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            lastInsets = windowInsetsCompat;
        }
        else {
            lastInsets = null;
        }
        if (!ObjectsCompat.equals(this.lastInsets, lastInsets)) {
            this.lastInsets = lastInsets;
            this.invalidateScrollRanges();
        }
        return windowInsetsCompat;
    }
    
    public void removeOnOffsetChangedListener(final BaseOnOffsetChangedListener baseOnOffsetChangedListener) {
        if (this.listeners != null && baseOnOffsetChangedListener != null) {
            this.listeners.remove(baseOnOffsetChangedListener);
        }
    }
    
    public void removeOnOffsetChangedListener(final OnOffsetChangedListener onOffsetChangedListener) {
        this.removeOnOffsetChangedListener((BaseOnOffsetChangedListener)onOffsetChangedListener);
    }
    
    void resetPendingAction() {
        this.pendingAction = 0;
    }
    
    public void setExpanded(final boolean b) {
        this.setExpanded(b, ViewCompat.isLaidOut((View)this));
    }
    
    public void setExpanded(final boolean b, final boolean b2) {
        this.setExpanded(b, b2, true);
    }
    
    public void setLiftOnScroll(final boolean liftOnScroll) {
        this.liftOnScroll = liftOnScroll;
    }
    
    boolean setLiftedState(final boolean lifted) {
        if (this.lifted != lifted) {
            this.lifted = lifted;
            this.refreshDrawableState();
            return true;
        }
        return false;
    }
    
    public void setOrientation(final int orientation) {
        if (orientation == 1) {
            super.setOrientation(orientation);
            return;
        }
        throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
    }
    
    @Deprecated
    public void setTargetElevation(final float n) {
        if (Build$VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, n);
        }
    }
    
    protected static class BaseBehavior<T extends AppBarLayout> extends HeaderBehavior<T>
    {
        private WeakReference<View> lastNestedScrollingChildRef;
        private int lastStartedType;
        private ValueAnimator offsetAnimator;
        private int offsetDelta;
        private int offsetToChildIndexOnLayout;
        private boolean offsetToChildIndexOnLayoutIsMinHeight;
        private float offsetToChildIndexOnLayoutPerc;
        private BaseDragCallback onDragCallback;
        
        public BaseBehavior() {
            this.offsetToChildIndexOnLayout = -1;
        }
        
        public BaseBehavior(final Context context, final AttributeSet set) {
            super(context, set);
            this.offsetToChildIndexOnLayout = -1;
        }
        
        private void animateOffsetTo(final CoordinatorLayout coordinatorLayout, final T t, final int n, float abs) {
            final int abs2 = Math.abs(this.getTopBottomOffsetForScrollingSibling() - n);
            abs = Math.abs(abs);
            int n2;
            if (abs > 0.0f) {
                n2 = Math.round(abs2 / abs * 1000.0f) * 3;
            }
            else {
                n2 = (int)((abs2 / (float)t.getHeight() + 1.0f) * 150.0f);
            }
            this.animateOffsetWithDuration(coordinatorLayout, t, n, n2);
        }
        
        private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout, final T t, final int n, final int a) {
            final int topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
            if (topBottomOffsetForScrollingSibling == n) {
                if (this.offsetAnimator != null && this.offsetAnimator.isRunning()) {
                    this.offsetAnimator.cancel();
                }
                return;
            }
            if (this.offsetAnimator == null) {
                (this.offsetAnimator = new ValueAnimator()).setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.offsetAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                        BaseBehavior.this.setHeaderTopBottomOffset(coordinatorLayout, t, (int)valueAnimator.getAnimatedValue());
                    }
                });
            }
            else {
                this.offsetAnimator.cancel();
            }
            this.offsetAnimator.setDuration((long)Math.min(a, 600));
            this.offsetAnimator.setIntValues(new int[] { topBottomOffsetForScrollingSibling, n });
            this.offsetAnimator.start();
        }
        
        private boolean canScrollChildren(final CoordinatorLayout coordinatorLayout, final T t, final View view) {
            return t.hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= t.getHeight();
        }
        
        private static boolean checkFlag(final int n, final int n2) {
            return (n & n2) == n2;
        }
        
        private View findFirstScrollingChild(final CoordinatorLayout coordinatorLayout) {
            for (int childCount = coordinatorLayout.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof NestedScrollingChild) {
                    return child;
                }
            }
            return null;
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
        
        private int getChildIndexOnOffset(final T t, final int n) {
            for (int childCount = t.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = t.getChildAt(i);
                final int top = child.getTop();
                final int bottom = child.getBottom();
                final AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)child.getLayoutParams();
                int n2 = top;
                int n3 = bottom;
                if (checkFlag(layoutParams.getScrollFlags(), 32)) {
                    n2 = top - layoutParams.topMargin;
                    n3 = bottom + layoutParams.bottomMargin;
                }
                final int n4 = -n;
                if (n2 <= n4 && n3 >= n4) {
                    return i;
                }
            }
            return -1;
        }
        
        private int interpolateOffset(final T t, final int n) {
            final int abs = Math.abs(n);
            final int childCount = t.getChildCount();
            final int n2 = 0;
            int i = 0;
            while (i < childCount) {
                final View child = t.getChildAt(i);
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
                        n5 = n3 - t.getTopInset();
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
        
        private boolean shouldJumpElevationState(final CoordinatorLayout coordinatorLayout, final T t) {
            final List<View> dependents = coordinatorLayout.getDependents((View)t);
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
        
        private void snapToChildIfNeeded(final CoordinatorLayout coordinatorLayout, final T t) {
            final int topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
            final int childIndexOnOffset = this.getChildIndexOnOffset(t, topBottomOffsetForScrollingSibling);
            if (childIndexOnOffset >= 0) {
                final View child = t.getChildAt(childIndexOnOffset);
                final AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)child.getLayoutParams();
                final int scrollFlags = layoutParams.getScrollFlags();
                if ((scrollFlags & 0x11) == 0x11) {
                    final int n = -child.getTop();
                    int n3;
                    final int n2 = n3 = -child.getBottom();
                    if (childIndexOnOffset == t.getChildCount() - 1) {
                        n3 = n2 + t.getTopInset();
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
                    int n7 = n4;
                    if (checkFlag(scrollFlags, 32)) {
                        n6 = n5 + layoutParams.topMargin;
                        n7 = n4 - layoutParams.bottomMargin;
                    }
                    int n8 = n6;
                    if (topBottomOffsetForScrollingSibling < (n7 + n6) / 2) {
                        n8 = n7;
                    }
                    this.animateOffsetTo(coordinatorLayout, t, MathUtils.clamp(n8, -t.getTotalScrollRange(), 0), 0.0f);
                }
            }
        }
        
        private void stopNestedScrollIfNeeded(final int n, final T t, final View view, int topBottomOffsetForScrollingSibling) {
            if (topBottomOffsetForScrollingSibling == 1) {
                topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
                if ((n < 0 && topBottomOffsetForScrollingSibling == 0) || (n > 0 && topBottomOffsetForScrollingSibling == -t.getDownNestedScrollRange())) {
                    ViewCompat.stopNestedScroll(view, 1);
                }
            }
        }
        
        private void updateAppBarLayoutDrawableState(final CoordinatorLayout coordinatorLayout, final T t, final int n, final int n2, final boolean b) {
            final View appBarChildOnOffset = getAppBarChildOnOffset(t, n);
            if (appBarChildOnOffset != null) {
                final int scrollFlags = ((AppBarLayout.LayoutParams)appBarChildOnOffset.getLayoutParams()).getScrollFlags();
                boolean b2 = false;
                Label_0107: {
                    if ((scrollFlags & 0x1) != 0x0) {
                        final int minimumHeight = ViewCompat.getMinimumHeight(appBarChildOnOffset);
                        if ((n2 <= 0 || (scrollFlags & 0xC) == 0x0) ? ((scrollFlags & 0x2) != 0x0 && -n >= appBarChildOnOffset.getBottom() - minimumHeight - t.getTopInset()) : (-n >= appBarChildOnOffset.getBottom() - minimumHeight - t.getTopInset())) {
                            b2 = true;
                            break Label_0107;
                        }
                    }
                    b2 = false;
                }
                boolean liftedState = b2;
                if (t.isLiftOnScroll()) {
                    final View firstScrollingChild = this.findFirstScrollingChild(coordinatorLayout);
                    liftedState = b2;
                    if (firstScrollingChild != null) {
                        liftedState = (firstScrollingChild.getScrollY() > 0);
                    }
                }
                final boolean setLiftedState = t.setLiftedState(liftedState);
                if (Build$VERSION.SDK_INT >= 11 && (b || (setLiftedState && this.shouldJumpElevationState(coordinatorLayout, t)))) {
                    t.jumpDrawablesToCurrentState();
                }
            }
        }
        
        @Override
        boolean canDragView(final T t) {
            if (this.onDragCallback != null) {
                return this.onDragCallback.canDrag(t);
            }
            final WeakReference<View> lastNestedScrollingChildRef = this.lastNestedScrollingChildRef;
            boolean b = true;
            if (lastNestedScrollingChildRef != null) {
                final View view = this.lastNestedScrollingChildRef.get();
                if (view == null || !view.isShown() || view.canScrollVertically(-1)) {
                    b = false;
                }
                return b;
            }
            return true;
        }
        
        @Override
        int getMaxDragOffset(final T t) {
            return -t.getDownNestedScrollRange();
        }
        
        @Override
        int getScrollRangeForDragFling(final T t) {
            return t.getTotalScrollRange();
        }
        
        @Override
        int getTopBottomOffsetForScrollingSibling() {
            return this.getTopAndBottomOffset() + this.offsetDelta;
        }
        
        @Override
        void onFlingFinished(final CoordinatorLayout coordinatorLayout, final T t) {
            this.snapToChildIfNeeded(coordinatorLayout, t);
        }
        
        @Override
        public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final T t, int n) {
            final boolean onLayoutChild = super.onLayoutChild(coordinatorLayout, t, n);
            final int pendingAction = t.getPendingAction();
            if (this.offsetToChildIndexOnLayout >= 0 && (pendingAction & 0x8) == 0x0) {
                final View child = t.getChildAt(this.offsetToChildIndexOnLayout);
                n = -child.getBottom();
                if (this.offsetToChildIndexOnLayoutIsMinHeight) {
                    n += ViewCompat.getMinimumHeight(child) + t.getTopInset();
                }
                else {
                    n += Math.round(child.getHeight() * this.offsetToChildIndexOnLayoutPerc);
                }
                this.setHeaderTopBottomOffset(coordinatorLayout, t, n);
            }
            else if (pendingAction != 0) {
                if ((pendingAction & 0x4) != 0x0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                if ((pendingAction & 0x2) != 0x0) {
                    final int n2 = -t.getUpNestedPreScrollRange();
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, t, n2, 0.0f);
                    }
                    else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, t, n2);
                    }
                }
                else if ((pendingAction & 0x1) != 0x0) {
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, t, 0, 0.0f);
                    }
                    else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, t, 0);
                    }
                }
            }
            t.resetPendingAction();
            this.offsetToChildIndexOnLayout = -1;
            this.setTopAndBottomOffset(MathUtils.clamp(this.getTopAndBottomOffset(), -t.getTotalScrollRange(), 0));
            this.updateAppBarLayoutDrawableState(coordinatorLayout, t, this.getTopAndBottomOffset(), 0, true);
            t.dispatchOffsetUpdates(this.getTopAndBottomOffset());
            return onLayoutChild;
        }
        
        public boolean onMeasureChild(final CoordinatorLayout coordinatorLayout, final T t, final int n, final int n2, final int n3, final int n4) {
            if (((CoordinatorLayout.LayoutParams)t.getLayoutParams()).height == -2) {
                coordinatorLayout.onMeasureChild((View)t, n, n2, View$MeasureSpec.makeMeasureSpec(0, 0), n4);
                return true;
            }
            return super.onMeasureChild(coordinatorLayout, t, n, n2, n3, n4);
        }
        
        public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final T t, final View view, int n, final int n2, final int[] array, final int n3) {
            if (n2 != 0) {
                int n6;
                if (n2 < 0) {
                    final int n4 = -t.getTotalScrollRange();
                    final int downNestedPreScrollRange = t.getDownNestedPreScrollRange();
                    n = n4;
                    final int n5 = downNestedPreScrollRange + n4;
                    n6 = n;
                    n = n5;
                }
                else {
                    n6 = -t.getUpNestedPreScrollRange();
                    n = 0;
                }
                if (n6 != n) {
                    array[1] = this.scroll(coordinatorLayout, t, n2, n6, n);
                    this.stopNestedScrollIfNeeded(n2, t, view, n3);
                }
            }
        }
        
        public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final T t, final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
            if (n4 < 0) {
                this.scroll(coordinatorLayout, t, n4, -t.getDownNestedScrollRange(), 0);
                this.stopNestedScrollIfNeeded(n4, t, view, n5);
            }
            if (t.isLiftOnScroll()) {
                t.setLiftedState(view.getScrollY() > 0);
            }
        }
        
        public void onRestoreInstanceState(final CoordinatorLayout coordinatorLayout, final T t, final Parcelable parcelable) {
            if (parcelable instanceof SavedState) {
                final SavedState savedState = (SavedState)parcelable;
                super.onRestoreInstanceState(coordinatorLayout, t, savedState.getSuperState());
                this.offsetToChildIndexOnLayout = savedState.firstVisibleChildIndex;
                this.offsetToChildIndexOnLayoutPerc = savedState.firstVisibleChildPercentageShown;
                this.offsetToChildIndexOnLayoutIsMinHeight = savedState.firstVisibleChildAtMinimumHeight;
            }
            else {
                super.onRestoreInstanceState(coordinatorLayout, t, parcelable);
                this.offsetToChildIndexOnLayout = -1;
            }
        }
        
        public Parcelable onSaveInstanceState(final CoordinatorLayout coordinatorLayout, final T t) {
            final Parcelable onSaveInstanceState = super.onSaveInstanceState(coordinatorLayout, t);
            final int topAndBottomOffset = this.getTopAndBottomOffset();
            final int childCount = t.getChildCount();
            boolean firstVisibleChildAtMinimumHeight = false;
            for (int i = 0; i < childCount; ++i) {
                final View child = t.getChildAt(i);
                final int n = child.getBottom() + topAndBottomOffset;
                if (child.getTop() + topAndBottomOffset <= 0 && n >= 0) {
                    final SavedState savedState = new SavedState(onSaveInstanceState);
                    savedState.firstVisibleChildIndex = i;
                    if (n == ViewCompat.getMinimumHeight(child) + t.getTopInset()) {
                        firstVisibleChildAtMinimumHeight = true;
                    }
                    savedState.firstVisibleChildAtMinimumHeight = firstVisibleChildAtMinimumHeight;
                    savedState.firstVisibleChildPercentageShown = n / (float)child.getHeight();
                    return (Parcelable)savedState;
                }
            }
            return onSaveInstanceState;
        }
        
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final T t, final View view, final View view2, final int n, final int lastStartedType) {
            final boolean b = (n & 0x2) != 0x0 && (t.isLiftOnScroll() || this.canScrollChildren(coordinatorLayout, t, view));
            if (b && this.offsetAnimator != null) {
                this.offsetAnimator.cancel();
            }
            this.lastNestedScrollingChildRef = null;
            this.lastStartedType = lastStartedType;
            return b;
        }
        
        public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final T t, final View referent, final int n) {
            if (this.lastStartedType == 0 || n == 1) {
                this.snapToChildIfNeeded(coordinatorLayout, t);
            }
            this.lastNestedScrollingChildRef = new WeakReference<View>(referent);
        }
        
        @Override
        int setHeaderTopBottomOffset(final CoordinatorLayout coordinatorLayout, final T t, int interpolateOffset, int clamp, int n) {
            final int topBottomOffsetForScrollingSibling = this.getTopBottomOffsetForScrollingSibling();
            final int n2 = 0;
            if (clamp != 0 && topBottomOffsetForScrollingSibling >= clamp && topBottomOffsetForScrollingSibling <= n) {
                clamp = MathUtils.clamp(interpolateOffset, clamp, n);
                interpolateOffset = n2;
                if (topBottomOffsetForScrollingSibling != clamp) {
                    if (t.hasChildWithInterpolator()) {
                        interpolateOffset = this.interpolateOffset(t, clamp);
                    }
                    else {
                        interpolateOffset = clamp;
                    }
                    final boolean setTopAndBottomOffset = this.setTopAndBottomOffset(interpolateOffset);
                    n = topBottomOffsetForScrollingSibling - clamp;
                    this.offsetDelta = clamp - interpolateOffset;
                    if (!setTopAndBottomOffset && t.hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged((View)t);
                    }
                    t.dispatchOffsetUpdates(this.getTopAndBottomOffset());
                    if (clamp < topBottomOffsetForScrollingSibling) {
                        interpolateOffset = -1;
                    }
                    else {
                        interpolateOffset = 1;
                    }
                    this.updateAppBarLayoutDrawableState(coordinatorLayout, t, clamp, interpolateOffset, false);
                    interpolateOffset = n;
                }
            }
            else {
                this.offsetDelta = 0;
                interpolateOffset = n2;
            }
            return interpolateOffset;
        }
        
        public abstract static class BaseDragCallback<T extends AppBarLayout>
        {
            public abstract boolean canDrag(final T p0);
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
    
    public interface BaseOnOffsetChangedListener<T extends AppBarLayout>
    {
        void onOffsetChanged(final T p0, final int p1);
    }
    
    public static class Behavior extends BaseBehavior<AppBarLayout>
    {
        public Behavior() {
        }
        
        public Behavior(final Context context, final AttributeSet set) {
            super(context, set);
        }
    }
    
    public static class LayoutParams extends LinearLayout$LayoutParams
    {
        int scrollFlags;
        Interpolator scrollInterpolator;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.scrollFlags = 1;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.scrollFlags = 1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AppBarLayout_Layout);
            this.scrollFlags = obtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            if (obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
                this.scrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, obtainStyledAttributes.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
            }
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.scrollFlags = 1;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.scrollFlags = 1;
        }
        
        public LayoutParams(final LinearLayout$LayoutParams linearLayout$LayoutParams) {
            super(linearLayout$LayoutParams);
            this.scrollFlags = 1;
        }
        
        public int getScrollFlags() {
            return this.scrollFlags;
        }
        
        public Interpolator getScrollInterpolator() {
            return this.scrollInterpolator;
        }
        
        boolean isCollapsible() {
            final int scrollFlags = this.scrollFlags;
            boolean b = true;
            if ((scrollFlags & 0x1) != 0x1 || (this.scrollFlags & 0xA) == 0x0) {
                b = false;
            }
            return b;
        }
    }
    
    public interface OnOffsetChangedListener extends BaseOnOffsetChangedListener<AppBarLayout>
    {
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
            if (behavior instanceof BaseBehavior) {
                return ((BaseBehavior)behavior).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }
        
        private void offsetChildAsNeeded(final View view, final View view2) {
            final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)view2.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                ViewCompat.offsetTopAndBottom(view, view2.getBottom() - view.getTop() + ((BaseBehavior<AppBarLayout>)behavior).offsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(view2));
            }
        }
        
        private void updateLiftedStateIfNeeded(final View view, final View view2) {
            if (view2 instanceof AppBarLayout) {
                final AppBarLayout appBarLayout = (AppBarLayout)view2;
                if (appBarLayout.isLiftOnScroll()) {
                    appBarLayout.setLiftedState(view.getScrollY() > 0);
                }
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
                    return appBarLayoutOffset / (float)n + 1.0f;
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
            this.offsetChildAsNeeded(view, view2);
            this.updateLiftedStateIfNeeded(view, view2);
            return false;
        }
        
        @Override
        public boolean onRequestChildRectangleOnScreen(final CoordinatorLayout coordinatorLayout, final View view, final Rect rect, final boolean b) {
            final AppBarLayout firstDependency = this.findFirstDependency(coordinatorLayout.getDependencies(view));
            if (firstDependency != null) {
                rect.offset(view.getLeft(), view.getTop());
                final Rect tempRect1 = this.tempRect1;
                tempRect1.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                if (!tempRect1.contains(rect)) {
                    firstDependency.setExpanded(false, b ^ true);
                    return true;
                }
            }
            return false;
        }
    }
}
