// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.HashMap;
import android.os.Build$VERSION;
import android.view.ViewGroup$LayoutParams;
import android.util.TypedValue;
import android.content.res.TypedArray;
import android.view.ViewConfiguration;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.math.MathUtils;
import android.view.VelocityTracker;
import java.lang.ref.WeakReference;
import java.util.Map;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

public class BottomSheetBehavior<V extends View> extends Behavior<V>
{
    int activePointerId;
    private BottomSheetCallback callback;
    int collapsedOffset;
    private final ViewDragHelper.Callback dragCallback;
    private boolean fitToContents;
    int fitToContentsOffset;
    int halfExpandedOffset;
    boolean hideable;
    private boolean ignoreEvents;
    private Map<View, Integer> importantForAccessibilityMap;
    private int initialY;
    private int lastNestedScrollDy;
    private int lastPeekHeight;
    private float maximumVelocity;
    private boolean nestedScrolled;
    WeakReference<View> nestedScrollingChildRef;
    int parentHeight;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private boolean skipCollapsed;
    int state;
    boolean touchingScrollingChild;
    private VelocityTracker velocityTracker;
    ViewDragHelper viewDragHelper;
    WeakReference<V> viewRef;
    
    public BottomSheetBehavior() {
        this.fitToContents = true;
        this.state = 4;
        this.dragCallback = new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
                return view.getLeft();
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, int n2) {
                final int access$100 = BottomSheetBehavior.this.getExpandedOffset();
                if (BottomSheetBehavior.this.hideable) {
                    n2 = BottomSheetBehavior.this.parentHeight;
                }
                else {
                    n2 = BottomSheetBehavior.this.collapsedOffset;
                }
                return MathUtils.clamp(n, access$100, n2);
            }
            
            @Override
            public int getViewVerticalDragRange(final View view) {
                if (BottomSheetBehavior.this.hideable) {
                    return BottomSheetBehavior.this.parentHeight;
                }
                return BottomSheetBehavior.this.collapsedOffset;
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                int stateInternal = 4;
                int n3 = 0;
                Label_0333: {
                    while (true) {
                        Label_0064: {
                            Label_0057: {
                                if (n2 < 0.0f) {
                                    if (BottomSheetBehavior.this.fitToContents) {
                                        n3 = BottomSheetBehavior.this.fitToContentsOffset;
                                    }
                                    else {
                                        if (view.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
                                            n3 = BottomSheetBehavior.this.halfExpandedOffset;
                                            break Label_0057;
                                        }
                                        break Label_0064;
                                    }
                                }
                                else {
                                    if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(view, n2) && (view.getTop() > BottomSheetBehavior.this.collapsedOffset || Math.abs(n) < Math.abs(n2))) {
                                        n3 = BottomSheetBehavior.this.parentHeight;
                                        stateInternal = 5;
                                        break Label_0333;
                                    }
                                    if (n2 != 0.0f && Math.abs(n) <= Math.abs(n2)) {
                                        n3 = BottomSheetBehavior.this.collapsedOffset;
                                        break Label_0333;
                                    }
                                    final int top = view.getTop();
                                    if (BottomSheetBehavior.this.fitToContents) {
                                        if (Math.abs(top - BottomSheetBehavior.this.fitToContentsOffset) < Math.abs(top - BottomSheetBehavior.this.collapsedOffset)) {
                                            n3 = BottomSheetBehavior.this.fitToContentsOffset;
                                            break Label_0028;
                                        }
                                        n3 = BottomSheetBehavior.this.collapsedOffset;
                                    }
                                    else if (top < BottomSheetBehavior.this.halfExpandedOffset) {
                                        if (top < Math.abs(top - BottomSheetBehavior.this.collapsedOffset)) {
                                            break Label_0064;
                                        }
                                        n3 = BottomSheetBehavior.this.halfExpandedOffset;
                                        break Label_0057;
                                    }
                                    else {
                                        if (Math.abs(top - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(top - BottomSheetBehavior.this.collapsedOffset)) {
                                            n3 = BottomSheetBehavior.this.halfExpandedOffset;
                                            break Label_0057;
                                        }
                                        n3 = BottomSheetBehavior.this.collapsedOffset;
                                    }
                                    break Label_0333;
                                }
                                stateInternal = 3;
                                break Label_0333;
                            }
                            stateInternal = 6;
                            break Label_0333;
                        }
                        n3 = 0;
                        continue;
                    }
                }
                if (BottomSheetBehavior.this.viewDragHelper.settleCapturedViewAt(view.getLeft(), n3)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
                }
                else {
                    BottomSheetBehavior.this.setStateInternal(stateInternal);
                }
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                final int state = BottomSheetBehavior.this.state;
                boolean b = true;
                if (state == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.touchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == n) {
                    final View view2 = BottomSheetBehavior.this.nestedScrollingChildRef.get();
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                if (BottomSheetBehavior.this.viewRef == null || BottomSheetBehavior.this.viewRef.get() != view) {
                    b = false;
                }
                return b;
            }
        };
    }
    
    public BottomSheetBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.fitToContents = true;
        this.state = 4;
        this.dragCallback = new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
                return view.getLeft();
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, int n2) {
                final int access$100 = BottomSheetBehavior.this.getExpandedOffset();
                if (BottomSheetBehavior.this.hideable) {
                    n2 = BottomSheetBehavior.this.parentHeight;
                }
                else {
                    n2 = BottomSheetBehavior.this.collapsedOffset;
                }
                return MathUtils.clamp(n, access$100, n2);
            }
            
            @Override
            public int getViewVerticalDragRange(final View view) {
                if (BottomSheetBehavior.this.hideable) {
                    return BottomSheetBehavior.this.parentHeight;
                }
                return BottomSheetBehavior.this.collapsedOffset;
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                int stateInternal = 4;
                int n3 = 0;
                Label_0333: {
                    while (true) {
                        Label_0064: {
                            Label_0057: {
                                if (n2 < 0.0f) {
                                    if (BottomSheetBehavior.this.fitToContents) {
                                        n3 = BottomSheetBehavior.this.fitToContentsOffset;
                                    }
                                    else {
                                        if (view.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
                                            n3 = BottomSheetBehavior.this.halfExpandedOffset;
                                            break Label_0057;
                                        }
                                        break Label_0064;
                                    }
                                }
                                else {
                                    if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(view, n2) && (view.getTop() > BottomSheetBehavior.this.collapsedOffset || Math.abs(n) < Math.abs(n2))) {
                                        n3 = BottomSheetBehavior.this.parentHeight;
                                        stateInternal = 5;
                                        break Label_0333;
                                    }
                                    if (n2 != 0.0f && Math.abs(n) <= Math.abs(n2)) {
                                        n3 = BottomSheetBehavior.this.collapsedOffset;
                                        break Label_0333;
                                    }
                                    final int top = view.getTop();
                                    if (BottomSheetBehavior.this.fitToContents) {
                                        if (Math.abs(top - BottomSheetBehavior.this.fitToContentsOffset) < Math.abs(top - BottomSheetBehavior.this.collapsedOffset)) {
                                            n3 = BottomSheetBehavior.this.fitToContentsOffset;
                                            break Label_0028;
                                        }
                                        n3 = BottomSheetBehavior.this.collapsedOffset;
                                    }
                                    else if (top < BottomSheetBehavior.this.halfExpandedOffset) {
                                        if (top < Math.abs(top - BottomSheetBehavior.this.collapsedOffset)) {
                                            break Label_0064;
                                        }
                                        n3 = BottomSheetBehavior.this.halfExpandedOffset;
                                        break Label_0057;
                                    }
                                    else {
                                        if (Math.abs(top - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(top - BottomSheetBehavior.this.collapsedOffset)) {
                                            n3 = BottomSheetBehavior.this.halfExpandedOffset;
                                            break Label_0057;
                                        }
                                        n3 = BottomSheetBehavior.this.collapsedOffset;
                                    }
                                    break Label_0333;
                                }
                                stateInternal = 3;
                                break Label_0333;
                            }
                            stateInternal = 6;
                            break Label_0333;
                        }
                        n3 = 0;
                        continue;
                    }
                }
                if (BottomSheetBehavior.this.viewDragHelper.settleCapturedViewAt(view.getLeft(), n3)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
                }
                else {
                    BottomSheetBehavior.this.setStateInternal(stateInternal);
                }
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                final int state = BottomSheetBehavior.this.state;
                boolean b = true;
                if (state == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.touchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == n) {
                    final View view2 = BottomSheetBehavior.this.nestedScrollingChildRef.get();
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                if (BottomSheetBehavior.this.viewRef == null || BottomSheetBehavior.this.viewRef.get() != view) {
                    b = false;
                }
                return b;
            }
        };
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.BottomSheetBehavior_Layout);
        final TypedValue peekValue = obtainStyledAttributes.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (peekValue != null && peekValue.data == -1) {
            this.setPeekHeight(peekValue.data);
        }
        else {
            this.setPeekHeight(obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }
        this.setHideable(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setFitToContents(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        this.setSkipCollapsed(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        obtainStyledAttributes.recycle();
        this.maximumVelocity = (float)ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }
    
    private void calculateCollapsedOffset() {
        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
        }
        else {
            this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
        }
    }
    
    public static <V extends View> BottomSheetBehavior<V> from(final V v) {
        final ViewGroup$LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        final Behavior behavior = ((LayoutParams)layoutParams).getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            return (BottomSheetBehavior<V>)behavior;
        }
        throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    }
    
    private int getExpandedOffset() {
        int fitToContentsOffset;
        if (this.fitToContents) {
            fitToContentsOffset = this.fitToContentsOffset;
        }
        else {
            fitToContentsOffset = 0;
        }
        return fitToContentsOffset;
    }
    
    private float getYVelocity() {
        if (this.velocityTracker == null) {
            return 0.0f;
        }
        this.velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }
    
    private void reset() {
        this.activePointerId = -1;
        if (this.velocityTracker != null) {
            this.velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }
    
    private void updateImportantForAccessibility(final boolean b) {
        if (this.viewRef == null) {
            return;
        }
        final ViewParent parent = this.viewRef.get().getParent();
        if (!(parent instanceof CoordinatorLayout)) {
            return;
        }
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)parent;
        final int childCount = coordinatorLayout.getChildCount();
        if (Build$VERSION.SDK_INT >= 16 && b) {
            if (this.importantForAccessibilityMap != null) {
                return;
            }
            this.importantForAccessibilityMap = new HashMap<View, Integer>(childCount);
        }
        for (int i = 0; i < childCount; ++i) {
            final View child = coordinatorLayout.getChildAt(i);
            if (child != this.viewRef.get()) {
                if (!b) {
                    if (this.importantForAccessibilityMap != null && this.importantForAccessibilityMap.containsKey(child)) {
                        ViewCompat.setImportantForAccessibility(child, this.importantForAccessibilityMap.get(child));
                    }
                }
                else {
                    if (Build$VERSION.SDK_INT >= 16) {
                        this.importantForAccessibilityMap.put(child, child.getImportantForAccessibility());
                    }
                    ViewCompat.setImportantForAccessibility(child, 4);
                }
            }
        }
        if (!b) {
            this.importantForAccessibilityMap = null;
        }
    }
    
    void dispatchOnSlide(final int n) {
        final View view = this.viewRef.get();
        if (view != null && this.callback != null) {
            if (n > this.collapsedOffset) {
                this.callback.onSlide(view, (this.collapsedOffset - n) / (float)(this.parentHeight - this.collapsedOffset));
            }
            else {
                this.callback.onSlide(view, (this.collapsedOffset - n) / (float)(this.collapsedOffset - this.getExpandedOffset()));
            }
        }
    }
    
    View findScrollingChild(View scrollingChild) {
        if (ViewCompat.isNestedScrollingEnabled(scrollingChild)) {
            return scrollingChild;
        }
        if (scrollingChild instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)scrollingChild;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                scrollingChild = this.findScrollingChild(viewGroup.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }
    
    public final int getPeekHeight() {
        int peekHeight;
        if (this.peekHeightAuto) {
            peekHeight = -1;
        }
        else {
            peekHeight = this.peekHeight;
        }
        return peekHeight;
    }
    
    public final int getState() {
        return this.state;
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        final boolean shown = v.isShown();
        final boolean b = false;
        if (!shown) {
            this.ignoreEvents = true;
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        final View view = null;
        Label_0237: {
            if (actionMasked != 3) {
                switch (actionMasked) {
                    default: {
                        break Label_0237;
                    }
                    case 0: {
                        final int n = (int)motionEvent.getX();
                        this.initialY = (int)motionEvent.getY();
                        View view2;
                        if (this.nestedScrollingChildRef != null) {
                            view2 = this.nestedScrollingChildRef.get();
                        }
                        else {
                            view2 = null;
                        }
                        if (view2 != null && coordinatorLayout.isPointInChildBounds(view2, n, this.initialY)) {
                            this.activePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                            this.touchingScrollingChild = true;
                        }
                        this.ignoreEvents = (this.activePointerId == -1 && !coordinatorLayout.isPointInChildBounds(v, n, this.initialY));
                        break Label_0237;
                    }
                    case 1: {
                        break;
                    }
                }
            }
            this.touchingScrollingChild = false;
            this.activePointerId = -1;
            if (this.ignoreEvents) {
                return this.ignoreEvents = false;
            }
        }
        if (!this.ignoreEvents && this.viewDragHelper != null && this.viewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        View view3 = view;
        if (this.nestedScrollingChildRef != null) {
            view3 = this.nestedScrollingChildRef.get();
        }
        boolean b2 = b;
        if (actionMasked == 2) {
            b2 = b;
            if (view3 != null) {
                b2 = b;
                if (!this.ignoreEvents) {
                    b2 = b;
                    if (this.state != 1) {
                        b2 = b;
                        if (!coordinatorLayout.isPointInChildBounds(view3, (int)motionEvent.getX(), (int)motionEvent.getY())) {
                            b2 = b;
                            if (this.viewDragHelper != null) {
                                b2 = b;
                                if (Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
                                    b2 = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V referent, final int n) {
        if (ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(referent)) {
            referent.setFitsSystemWindows(true);
        }
        final int top = referent.getTop();
        coordinatorLayout.onLayoutChild(referent, n);
        this.parentHeight = coordinatorLayout.getHeight();
        if (this.peekHeightAuto) {
            if (this.peekHeightMin == 0) {
                this.peekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - coordinatorLayout.getWidth() * 9 / 16);
        }
        else {
            this.lastPeekHeight = this.peekHeight;
        }
        this.fitToContentsOffset = Math.max(0, this.parentHeight - referent.getHeight());
        this.halfExpandedOffset = this.parentHeight / 2;
        this.calculateCollapsedOffset();
        if (this.state == 3) {
            ViewCompat.offsetTopAndBottom(referent, this.getExpandedOffset());
        }
        else if (this.state == 6) {
            ViewCompat.offsetTopAndBottom(referent, this.halfExpandedOffset);
        }
        else if (this.hideable && this.state == 5) {
            ViewCompat.offsetTopAndBottom(referent, this.parentHeight);
        }
        else if (this.state == 4) {
            ViewCompat.offsetTopAndBottom(referent, this.collapsedOffset);
        }
        else if (this.state == 1 || this.state == 2) {
            ViewCompat.offsetTopAndBottom(referent, top - referent.getTop());
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create(coordinatorLayout, this.dragCallback);
        }
        this.viewRef = new WeakReference<V>(referent);
        this.nestedScrollingChildRef = new WeakReference<View>(this.findScrollingChild(referent));
        return true;
    }
    
    @Override
    public boolean onNestedPreFling(final CoordinatorLayout coordinatorLayout, final V v, final View view, final float n, final float n2) {
        return view == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, v, view, n, n2));
    }
    
    @Override
    public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, int n, final int lastNestedScrollDy, final int[] array, int top) {
        if (top == 1) {
            return;
        }
        if (view != this.nestedScrollingChildRef.get()) {
            return;
        }
        top = v.getTop();
        n = top - lastNestedScrollDy;
        if (lastNestedScrollDy > 0) {
            if (n < this.getExpandedOffset()) {
                array[1] = top - this.getExpandedOffset();
                ViewCompat.offsetTopAndBottom(v, -array[1]);
                this.setStateInternal(3);
            }
            else {
                ViewCompat.offsetTopAndBottom(v, -(array[1] = lastNestedScrollDy));
                this.setStateInternal(1);
            }
        }
        else if (lastNestedScrollDy < 0 && !view.canScrollVertically(-1)) {
            if (n > this.collapsedOffset && !this.hideable) {
                array[1] = top - this.collapsedOffset;
                ViewCompat.offsetTopAndBottom(v, -array[1]);
                this.setStateInternal(4);
            }
            else {
                ViewCompat.offsetTopAndBottom(v, -(array[1] = lastNestedScrollDy));
                this.setStateInternal(1);
            }
        }
        this.dispatchOnSlide(v.getTop());
        this.lastNestedScrollDy = lastNestedScrollDy;
        this.nestedScrolled = true;
    }
    
    @Override
    public void onRestoreInstanceState(final CoordinatorLayout coordinatorLayout, final V v, final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, savedState.getSuperState());
        if (savedState.state != 1 && savedState.state != 2) {
            this.state = savedState.state;
        }
        else {
            this.state = 4;
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState(final CoordinatorLayout coordinatorLayout, final V v) {
        return (Parcelable)new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.state);
    }
    
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n, final int n2) {
        boolean b = false;
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        if ((n & 0x2) != 0x0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, int n) {
        final int top = v.getTop();
        n = this.getExpandedOffset();
        int stateInternal = 3;
        if (top == n) {
            this.setStateInternal(3);
            return;
        }
        if (view == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
            Label_0250: {
                if (this.lastNestedScrollDy > 0) {
                    n = this.getExpandedOffset();
                }
                else if (this.hideable && this.shouldHide(v, this.getYVelocity())) {
                    n = this.parentHeight;
                    stateInternal = 5;
                }
                else {
                    Label_0247: {
                        if (this.lastNestedScrollDy == 0) {
                            n = v.getTop();
                            if (!this.fitToContents) {
                                if (n < this.halfExpandedOffset) {
                                    if (n < Math.abs(n - this.collapsedOffset)) {
                                        n = 0;
                                        break Label_0250;
                                    }
                                    n = this.halfExpandedOffset;
                                }
                                else {
                                    if (Math.abs(n - this.halfExpandedOffset) >= Math.abs(n - this.collapsedOffset)) {
                                        n = this.collapsedOffset;
                                        break Label_0247;
                                    }
                                    n = this.halfExpandedOffset;
                                }
                                stateInternal = 6;
                                break Label_0250;
                            }
                            if (Math.abs(n - this.fitToContentsOffset) < Math.abs(n - this.collapsedOffset)) {
                                n = this.fitToContentsOffset;
                                break Label_0250;
                            }
                            n = this.collapsedOffset;
                        }
                        else {
                            n = this.collapsedOffset;
                        }
                    }
                    stateInternal = 4;
                }
            }
            if (this.viewDragHelper.smoothSlideViewTo(v, v.getLeft(), n)) {
                this.setStateInternal(2);
                ViewCompat.postOnAnimation(v, new SettleRunnable(v, stateInternal));
            }
            else {
                this.setStateInternal(stateInternal);
            }
            this.nestedScrolled = false;
        }
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (this.state == 1 && actionMasked == 0) {
            return true;
        }
        if (this.viewDragHelper != null) {
            this.viewDragHelper.processTouchEvent(motionEvent);
        }
        if (actionMasked == 0) {
            this.reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 2 && !this.ignoreEvents && Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
            this.viewDragHelper.captureChildView(v, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return this.ignoreEvents ^ true;
    }
    
    public void setBottomSheetCallback(final BottomSheetCallback callback) {
        this.callback = callback;
    }
    
    public void setFitToContents(final boolean fitToContents) {
        if (this.fitToContents == fitToContents) {
            return;
        }
        this.fitToContents = fitToContents;
        if (this.viewRef != null) {
            this.calculateCollapsedOffset();
        }
        int state;
        if (this.fitToContents && this.state == 6) {
            state = 3;
        }
        else {
            state = this.state;
        }
        this.setStateInternal(state);
    }
    
    public void setHideable(final boolean hideable) {
        this.hideable = hideable;
    }
    
    public final void setPeekHeight(int b) {
        final int n = 1;
        Label_0073: {
            if (b == -1) {
                if (!this.peekHeightAuto) {
                    this.peekHeightAuto = true;
                    b = n;
                    break Label_0073;
                }
            }
            else if (this.peekHeightAuto || this.peekHeight != b) {
                this.peekHeightAuto = false;
                this.peekHeight = Math.max(0, b);
                this.collapsedOffset = this.parentHeight - b;
                b = n;
                break Label_0073;
            }
            b = 0;
        }
        if (b != 0 && this.state == 4 && this.viewRef != null) {
            final View view = this.viewRef.get();
            if (view != null) {
                view.requestLayout();
            }
        }
    }
    
    public void setSkipCollapsed(final boolean skipCollapsed) {
        this.skipCollapsed = skipCollapsed;
    }
    
    public final void setState(final int state) {
        if (state == this.state) {
            return;
        }
        if (this.viewRef == null) {
            if (state == 4 || state == 3 || state == 6 || (this.hideable && state == 5)) {
                this.state = state;
            }
            return;
        }
        final View view = this.viewRef.get();
        if (view == null) {
            return;
        }
        final ViewParent parent = view.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
            view.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    BottomSheetBehavior.this.startSettlingAnimation(view, state);
                }
            });
        }
        else {
            this.startSettlingAnimation(view, state);
        }
    }
    
    void setStateInternal(final int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (state != 6 && state != 3) {
            if (state == 5 || state == 4) {
                this.updateImportantForAccessibility(false);
            }
        }
        else {
            this.updateImportantForAccessibility(true);
        }
        final View view = this.viewRef.get();
        if (view != null && this.callback != null) {
            this.callback.onStateChanged(view, state);
        }
    }
    
    boolean shouldHide(final View view, final float n) {
        final boolean skipCollapsed = this.skipCollapsed;
        boolean b = true;
        if (skipCollapsed) {
            return true;
        }
        if (view.getTop() < this.collapsedOffset) {
            return false;
        }
        if (Math.abs(view.getTop() + n * 0.1f - this.collapsedOffset) / this.peekHeight <= 0.5f) {
            b = false;
        }
        return b;
    }
    
    void startSettlingAnimation(final View view, int n) {
        int n2;
        if (n == 4) {
            n2 = this.collapsedOffset;
        }
        else if (n == 6) {
            n2 = this.halfExpandedOffset;
            if (this.fitToContents && n2 <= this.fitToContentsOffset) {
                n2 = this.fitToContentsOffset;
                n = 3;
            }
        }
        else if (n == 3) {
            n2 = this.getExpandedOffset();
        }
        else {
            if (!this.hideable || n != 5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Illegal state argument: ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            n2 = this.parentHeight;
        }
        if (this.viewDragHelper.smoothSlideViewTo(view, view.getLeft(), n2)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(view, new SettleRunnable(view, n));
        }
        else {
            this.setStateInternal(n);
        }
    }
    
    public abstract static class BottomSheetCallback
    {
        public abstract void onSlide(final View p0, final float p1);
        
        public abstract void onStateChanged(final View p0, final int p1);
    }
    
    protected static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        final int state;
        
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
            this.state = parcel.readInt();
        }
        
        public SavedState(final Parcelable parcelable, final int state) {
            super(parcelable);
            this.state = state;
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.state);
        }
    }
    
    private class SettleRunnable implements Runnable
    {
        private final int targetState;
        private final View view;
        
        SettleRunnable(final View view, final int targetState) {
            this.view = view;
            this.targetState = targetState;
        }
        
        @Override
        public void run() {
            if (BottomSheetBehavior.this.viewDragHelper != null && BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            }
            else {
                BottomSheetBehavior.this.setStateInternal(this.targetState);
            }
        }
    }
}
