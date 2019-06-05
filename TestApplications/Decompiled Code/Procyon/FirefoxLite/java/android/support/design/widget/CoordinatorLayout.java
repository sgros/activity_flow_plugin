// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.view.ViewParent;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.View$BaseSavedState;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.content.ContextCompat;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable$Callback;
import android.util.SparseArray;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.v4.widget.ViewGroupUtils;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.Region$Op;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewTreeObserver$OnPreDrawListener;
import java.util.Collection;
import android.os.SystemClock;
import android.view.MotionEvent;
import java.util.HashMap;
import android.text.TextUtils;
import java.util.Collections;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.content.res.Resources;
import android.content.res.TypedArray;
import java.util.ArrayList;
import android.support.coordinatorlayout.R;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.view.ViewGroup$OnHierarchyChangeListener;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.WindowInsetsCompat;
import java.util.List;
import android.support.v4.widget.DirectedAcyclicGraph;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.graphics.Rect;
import android.support.v4.util.Pools;
import java.lang.reflect.Constructor;
import java.util.Map;
import android.view.View;
import java.util.Comparator;
import android.support.v4.view.NestedScrollingParent2;
import android.view.ViewGroup;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2
{
    static final Class<?>[] CONSTRUCTOR_PARAMS;
    static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
    static final String WIDGET_PACKAGE_NAME;
    static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
    private static final Pools.Pool<Rect> sRectPool;
    private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
    private View mBehaviorTouchView;
    private final DirectedAcyclicGraph<View> mChildDag;
    private final List<View> mDependencySortedChildren;
    private boolean mDisallowInterceptReset;
    private boolean mDrawStatusBarBackground;
    private boolean mIsAttachedToWindow;
    private int[] mKeylines;
    private WindowInsetsCompat mLastInsets;
    private boolean mNeedsPreDrawListener;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private View mNestedScrollingTarget;
    ViewGroup$OnHierarchyChangeListener mOnHierarchyChangeListener;
    private OnPreDrawListener mOnPreDrawListener;
    private Paint mScrimPaint;
    private Drawable mStatusBarBackground;
    private final List<View> mTempDependenciesList;
    private final int[] mTempIntPair;
    private final List<View> mTempList1;
    
    static {
        final Package package1 = CoordinatorLayout.class.getPackage();
        String name;
        if (package1 != null) {
            name = package1.getName();
        }
        else {
            name = null;
        }
        WIDGET_PACKAGE_NAME = name;
        if (Build$VERSION.SDK_INT >= 21) {
            TOP_SORTED_CHILDREN_COMPARATOR = new ViewElevationComparator();
        }
        else {
            TOP_SORTED_CHILDREN_COMPARATOR = null;
        }
        CONSTRUCTOR_PARAMS = new Class[] { Context.class, AttributeSet.class };
        sConstructors = new ThreadLocal<Map<String, Constructor<Behavior>>>();
        sRectPool = new Pools.SynchronizedPool<Rect>(12);
    }
    
    public CoordinatorLayout(final Context context) {
        this(context, null);
    }
    
    public CoordinatorLayout(final Context context, final AttributeSet set) {
        this(context, set, R.attr.coordinatorLayoutStyle);
    }
    
    public CoordinatorLayout(final Context context, final AttributeSet set, int i) {
        super(context, set, i);
        this.mDependencySortedChildren = new ArrayList<View>();
        this.mChildDag = new DirectedAcyclicGraph<View>();
        this.mTempList1 = new ArrayList<View>();
        this.mTempDependenciesList = new ArrayList<View>();
        this.mTempIntPair = new int[2];
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        final int n = 0;
        TypedArray typedArray;
        if (i == 0) {
            typedArray = context.obtainStyledAttributes(set, R.styleable.CoordinatorLayout, 0, R.style.Widget_Support_CoordinatorLayout);
        }
        else {
            typedArray = context.obtainStyledAttributes(set, R.styleable.CoordinatorLayout, i, 0);
        }
        i = typedArray.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
        if (i != 0) {
            final Resources resources = context.getResources();
            this.mKeylines = resources.getIntArray(i);
            final float density = resources.getDisplayMetrics().density;
            int length;
            for (length = this.mKeylines.length, i = n; i < length; ++i) {
                this.mKeylines[i] *= (int)density;
            }
        }
        this.mStatusBarBackground = typedArray.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
        typedArray.recycle();
        this.setupForInsets();
        super.setOnHierarchyChangeListener((ViewGroup$OnHierarchyChangeListener)new HierarchyChangeListener());
    }
    
    private static Rect acquireTempRect() {
        Rect rect;
        if ((rect = CoordinatorLayout.sRectPool.acquire()) == null) {
            rect = new Rect();
        }
        return rect;
    }
    
    private static int clamp(final int n, final int n2, final int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    private void constrainChildRect(final LayoutParams layoutParams, final Rect rect, final int n, final int n2) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int max = Math.max(this.getPaddingLeft() + layoutParams.leftMargin, Math.min(rect.left, width - this.getPaddingRight() - n - layoutParams.rightMargin));
        final int max2 = Math.max(this.getPaddingTop() + layoutParams.topMargin, Math.min(rect.top, height - this.getPaddingBottom() - n2 - layoutParams.bottomMargin));
        rect.set(max, max2, n + max, n2 + max2);
    }
    
    private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat windowInsetsCompat) {
        if (windowInsetsCompat.isConsumed()) {
            return windowInsetsCompat;
        }
        int n = 0;
        final int childCount = this.getChildCount();
        WindowInsetsCompat windowInsetsCompat2;
        while (true) {
            windowInsetsCompat2 = windowInsetsCompat;
            if (n >= childCount) {
                break;
            }
            final View child = this.getChildAt(n);
            WindowInsetsCompat onApplyWindowInsets = windowInsetsCompat;
            if (ViewCompat.getFitsSystemWindows(child)) {
                final Behavior behavior = ((LayoutParams)child.getLayoutParams()).getBehavior();
                onApplyWindowInsets = windowInsetsCompat;
                if (behavior != null) {
                    windowInsetsCompat = (onApplyWindowInsets = behavior.onApplyWindowInsets(this, child, windowInsetsCompat));
                    if (windowInsetsCompat.isConsumed()) {
                        windowInsetsCompat2 = windowInsetsCompat;
                        break;
                    }
                }
            }
            ++n;
            windowInsetsCompat = onApplyWindowInsets;
        }
        return windowInsetsCompat2;
    }
    
    private void getDesiredAnchoredChildRectWithoutConstraints(final View view, int n, final Rect rect, final Rect rect2, final LayoutParams layoutParams, final int n2, final int n3) {
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(resolveAnchoredChildGravity(layoutParams.gravity), n);
        n = GravityCompat.getAbsoluteGravity(resolveGravity(layoutParams.anchorGravity), n);
        final int n4 = absoluteGravity & 0x7;
        final int n5 = absoluteGravity & 0x70;
        final int n6 = n & 0x7;
        final int n7 = n & 0x70;
        if (n6 != 1) {
            if (n6 != 5) {
                n = rect.left;
            }
            else {
                n = rect.right;
            }
        }
        else {
            n = rect.left + rect.width() / 2;
        }
        int n8;
        if (n7 != 16) {
            if (n7 != 80) {
                n8 = rect.top;
            }
            else {
                n8 = rect.bottom;
            }
        }
        else {
            n8 = rect.top + rect.height() / 2;
        }
        int n9;
        if (n4 != 1) {
            n9 = n;
            if (n4 != 5) {
                n9 = n - n2;
            }
        }
        else {
            n9 = n - n2 / 2;
        }
        if (n5 != 16) {
            n = n8;
            if (n5 != 80) {
                n = n8 - n3;
            }
        }
        else {
            n = n8 - n3 / 2;
        }
        rect2.set(n9, n, n2 + n9, n3 + n);
    }
    
    private int getKeyline(final int n) {
        if (this.mKeylines == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No keylines defined for ");
            sb.append(this);
            sb.append(" - attempted index lookup ");
            sb.append(n);
            Log.e("CoordinatorLayout", sb.toString());
            return 0;
        }
        if (n >= 0 && n < this.mKeylines.length) {
            return this.mKeylines[n];
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Keyline index ");
        sb2.append(n);
        sb2.append(" out of range for ");
        sb2.append(this);
        Log.e("CoordinatorLayout", sb2.toString());
        return 0;
    }
    
    private void getTopSortedChildren(final List<View> list) {
        list.clear();
        final boolean childrenDrawingOrderEnabled = this.isChildrenDrawingOrderEnabled();
        final int childCount = this.getChildCount();
        for (int i = childCount - 1; i >= 0; --i) {
            int childDrawingOrder;
            if (childrenDrawingOrderEnabled) {
                childDrawingOrder = this.getChildDrawingOrder(childCount, i);
            }
            else {
                childDrawingOrder = i;
            }
            list.add(this.getChildAt(childDrawingOrder));
        }
        if (CoordinatorLayout.TOP_SORTED_CHILDREN_COMPARATOR != null) {
            Collections.sort((List<Object>)list, (Comparator<? super Object>)CoordinatorLayout.TOP_SORTED_CHILDREN_COMPARATOR);
        }
    }
    
    private boolean hasDependencies(final View view) {
        return this.mChildDag.hasOutgoingEdges(view);
    }
    
    private void layoutChild(final View view, final int n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect acquireTempRect = acquireTempRect();
        acquireTempRect.set(this.getPaddingLeft() + layoutParams.leftMargin, this.getPaddingTop() + layoutParams.topMargin, this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin, this.getHeight() - this.getPaddingBottom() - layoutParams.bottomMargin);
        if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) && !ViewCompat.getFitsSystemWindows(view)) {
            acquireTempRect.left += this.mLastInsets.getSystemWindowInsetLeft();
            acquireTempRect.top += this.mLastInsets.getSystemWindowInsetTop();
            acquireTempRect.right -= this.mLastInsets.getSystemWindowInsetRight();
            acquireTempRect.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
        }
        final Rect acquireTempRect2 = acquireTempRect();
        GravityCompat.apply(resolveGravity(layoutParams.gravity), view.getMeasuredWidth(), view.getMeasuredHeight(), acquireTempRect, acquireTempRect2, n);
        view.layout(acquireTempRect2.left, acquireTempRect2.top, acquireTempRect2.right, acquireTempRect2.bottom);
        releaseTempRect(acquireTempRect);
        releaseTempRect(acquireTempRect2);
    }
    
    private void layoutChildWithAnchor(final View view, final View view2, final int n) {
        final Rect acquireTempRect = acquireTempRect();
        final Rect acquireTempRect2 = acquireTempRect();
        try {
            this.getDescendantRect(view2, acquireTempRect);
            this.getDesiredAnchoredChildRect(view, n, acquireTempRect, acquireTempRect2);
            view.layout(acquireTempRect2.left, acquireTempRect2.top, acquireTempRect2.right, acquireTempRect2.bottom);
        }
        finally {
            releaseTempRect(acquireTempRect);
            releaseTempRect(acquireTempRect2);
        }
    }
    
    private void layoutChildWithKeyline(final View view, int max, int max2) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), max2);
        final int n = absoluteGravity & 0x7;
        final int n2 = absoluteGravity & 0x70;
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();
        int n3 = max;
        if (max2 == 1) {
            n3 = width - max;
        }
        max = this.getKeyline(n3) - measuredWidth;
        max2 = 0;
        if (n != 1) {
            if (n == 5) {
                max += measuredWidth;
            }
        }
        else {
            max += measuredWidth / 2;
        }
        if (n2 != 16) {
            if (n2 == 80) {
                max2 = measuredHeight + 0;
            }
        }
        else {
            max2 = 0 + measuredHeight / 2;
        }
        max = Math.max(this.getPaddingLeft() + layoutParams.leftMargin, Math.min(max, width - this.getPaddingRight() - measuredWidth - layoutParams.rightMargin));
        max2 = Math.max(this.getPaddingTop() + layoutParams.topMargin, Math.min(max2, height - this.getPaddingBottom() - measuredHeight - layoutParams.bottomMargin));
        view.layout(max, max2, measuredWidth + max, measuredHeight + max2);
    }
    
    private void offsetChildByInset(final View view, final Rect rect, int n) {
        if (!ViewCompat.isLaidOut(view)) {
            return;
        }
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            return;
        }
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Behavior behavior = layoutParams.getBehavior();
        final Rect acquireTempRect = acquireTempRect();
        final Rect acquireTempRect2 = acquireTempRect();
        acquireTempRect2.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        if (behavior != null && behavior.getInsetDodgeRect(this, view, acquireTempRect)) {
            if (!acquireTempRect2.contains(acquireTempRect)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Rect should be within the child's bounds. Rect:");
                sb.append(acquireTempRect.toShortString());
                sb.append(" | Bounds:");
                sb.append(acquireTempRect2.toShortString());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        else {
            acquireTempRect.set(acquireTempRect2);
        }
        releaseTempRect(acquireTempRect2);
        if (acquireTempRect.isEmpty()) {
            releaseTempRect(acquireTempRect);
            return;
        }
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(layoutParams.dodgeInsetEdges, n);
        Label_0249: {
            if ((absoluteGravity & 0x30) == 0x30) {
                n = acquireTempRect.top - layoutParams.topMargin - layoutParams.mInsetOffsetY;
                if (n < rect.top) {
                    this.setInsetOffsetY(view, rect.top - n);
                    n = 1;
                    break Label_0249;
                }
            }
            n = 0;
        }
        int n2 = n;
        if ((absoluteGravity & 0x50) == 0x50) {
            final int n3 = this.getHeight() - acquireTempRect.bottom - layoutParams.bottomMargin + layoutParams.mInsetOffsetY;
            n2 = n;
            if (n3 < rect.bottom) {
                this.setInsetOffsetY(view, n3 - rect.bottom);
                n2 = 1;
            }
        }
        if (n2 == 0) {
            this.setInsetOffsetY(view, 0);
        }
        Label_0376: {
            if ((absoluteGravity & 0x3) == 0x3) {
                n = acquireTempRect.left - layoutParams.leftMargin - layoutParams.mInsetOffsetX;
                if (n < rect.left) {
                    this.setInsetOffsetX(view, rect.left - n);
                    n = 1;
                    break Label_0376;
                }
            }
            n = 0;
        }
        int n4 = n;
        if ((absoluteGravity & 0x5) == 0x5) {
            final int n5 = this.getWidth() - acquireTempRect.right - layoutParams.rightMargin + layoutParams.mInsetOffsetX;
            n4 = n;
            if (n5 < rect.right) {
                this.setInsetOffsetX(view, n5 - rect.right);
                n4 = 1;
            }
        }
        if (n4 == 0) {
            this.setInsetOffsetX(view, 0);
        }
        releaseTempRect(acquireTempRect);
    }
    
    static Behavior parseBehavior(final Context context, final AttributeSet set, final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return null;
        }
        String s2;
        if (s.startsWith(".")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(s);
            s2 = sb.toString();
        }
        else if (s.indexOf(46) >= 0) {
            s2 = s;
        }
        else {
            s2 = s;
            if (!TextUtils.isEmpty((CharSequence)CoordinatorLayout.WIDGET_PACKAGE_NAME)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(CoordinatorLayout.WIDGET_PACKAGE_NAME);
                sb2.append('.');
                sb2.append(s);
                s2 = sb2.toString();
            }
        }
        try {
            Map<String, Constructor<Behavior>> value;
            if ((value = CoordinatorLayout.sConstructors.get()) == null) {
                value = new HashMap<String, Constructor<Behavior>>();
                CoordinatorLayout.sConstructors.set(value);
            }
            Constructor<?> constructor;
            if ((constructor = value.get(s2)) == null) {
                constructor = context.getClassLoader().loadClass(s2).getConstructor(CoordinatorLayout.CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                value.put(s2, (Constructor<Behavior>)constructor);
            }
            return constructor.newInstance(context, set);
        }
        catch (Exception cause) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Could not inflate Behavior subclass ");
            sb3.append(s2);
            throw new RuntimeException(sb3.toString(), cause);
        }
    }
    
    private boolean performIntercept(final MotionEvent motionEvent, final int n) {
        final int actionMasked = motionEvent.getActionMasked();
        final List<View> mTempList1 = this.mTempList1;
        this.getTopSortedChildren(mTempList1);
        final int size = mTempList1.size();
        MotionEvent motionEvent2 = null;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5;
        while (true) {
            n5 = n3;
            if (n2 >= size) {
                break;
            }
            final View mBehaviorTouchView = mTempList1.get(n2);
            final LayoutParams layoutParams = (LayoutParams)mBehaviorTouchView.getLayoutParams();
            final Behavior behavior = layoutParams.getBehavior();
            int n6;
            int n7;
            MotionEvent obtain;
            if ((n3 || n4 != 0) && actionMasked != 0) {
                n6 = n3;
                n7 = n4;
                obtain = motionEvent2;
                if (behavior != null) {
                    if ((obtain = motionEvent2) == null) {
                        final long uptimeMillis = SystemClock.uptimeMillis();
                        obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                    }
                    switch (n) {
                        default: {
                            n6 = n3;
                            n7 = n4;
                            break;
                        }
                        case 1: {
                            behavior.onTouchEvent(this, mBehaviorTouchView, obtain);
                            n6 = n3;
                            n7 = n4;
                            break;
                        }
                        case 0: {
                            behavior.onInterceptTouchEvent(this, mBehaviorTouchView, obtain);
                            n6 = n3;
                            n7 = n4;
                            break;
                        }
                    }
                }
            }
            else {
                n5 = n3;
                if (n3 == 0) {
                    n5 = n3;
                    if (behavior != null) {
                        switch (n) {
                            case 1: {
                                n3 = (behavior.onTouchEvent(this, mBehaviorTouchView, motionEvent) ? 1 : 0);
                                break;
                            }
                            case 0: {
                                n3 = (behavior.onInterceptTouchEvent(this, mBehaviorTouchView, motionEvent) ? 1 : 0);
                                break;
                            }
                        }
                        n5 = n3;
                        if (n3 != 0) {
                            this.mBehaviorTouchView = mBehaviorTouchView;
                            n5 = n3;
                        }
                    }
                }
                final boolean didBlockInteraction = layoutParams.didBlockInteraction();
                final boolean blockingInteractionBelow = layoutParams.isBlockingInteractionBelow(this, mBehaviorTouchView);
                final boolean b = blockingInteractionBelow && !didBlockInteraction;
                n6 = n5;
                n7 = (b ? 1 : 0);
                obtain = motionEvent2;
                if (blockingInteractionBelow) {
                    n6 = n5;
                    n7 = (b ? 1 : 0);
                    obtain = motionEvent2;
                    if (!b) {
                        break;
                    }
                }
            }
            ++n2;
            n3 = n6;
            n4 = n7;
            motionEvent2 = obtain;
        }
        mTempList1.clear();
        return n5 != 0;
    }
    
    private void prepareChildren() {
        this.mDependencySortedChildren.clear();
        this.mChildDag.clear();
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final LayoutParams resolvedLayoutParams = this.getResolvedLayoutParams(child);
            resolvedLayoutParams.findAnchorView(this, child);
            this.mChildDag.addNode(child);
            for (int j = 0; j < childCount; ++j) {
                if (j != i) {
                    final View child2 = this.getChildAt(j);
                    if (resolvedLayoutParams.dependsOn(this, child, child2)) {
                        if (!this.mChildDag.contains(child2)) {
                            this.mChildDag.addNode(child2);
                        }
                        this.mChildDag.addEdge(child2, child);
                    }
                }
            }
        }
        this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
        Collections.reverse(this.mDependencySortedChildren);
    }
    
    private static void releaseTempRect(final Rect rect) {
        rect.setEmpty();
        CoordinatorLayout.sRectPool.release(rect);
    }
    
    private void resetTouchBehaviors(final boolean b) {
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final Behavior behavior = ((LayoutParams)child.getLayoutParams()).getBehavior();
            if (behavior != null) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                if (b) {
                    behavior.onInterceptTouchEvent(this, child, obtain);
                }
                else {
                    behavior.onTouchEvent(this, child, obtain);
                }
                obtain.recycle();
            }
        }
        for (int j = 0; j < childCount; ++j) {
            ((LayoutParams)this.getChildAt(j).getLayoutParams()).resetTouchBehaviorTracking();
        }
        this.mBehaviorTouchView = null;
        this.mDisallowInterceptReset = false;
    }
    
    private static int resolveAnchoredChildGravity(final int n) {
        int n2 = n;
        if (n == 0) {
            n2 = 17;
        }
        return n2;
    }
    
    private static int resolveGravity(int n) {
        int n2 = n;
        if ((n & 0x7) == 0x0) {
            n2 = (n | 0x800003);
        }
        n = n2;
        if ((n2 & 0x70) == 0x0) {
            n = (n2 | 0x30);
        }
        return n;
    }
    
    private static int resolveKeylineGravity(final int n) {
        int n2 = n;
        if (n == 0) {
            n2 = 8388661;
        }
        return n2;
    }
    
    private void setInsetOffsetX(final View view, final int mInsetOffsetX) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mInsetOffsetX != mInsetOffsetX) {
            ViewCompat.offsetLeftAndRight(view, mInsetOffsetX - layoutParams.mInsetOffsetX);
            layoutParams.mInsetOffsetX = mInsetOffsetX;
        }
    }
    
    private void setInsetOffsetY(final View view, final int mInsetOffsetY) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mInsetOffsetY != mInsetOffsetY) {
            ViewCompat.offsetTopAndBottom(view, mInsetOffsetY - layoutParams.mInsetOffsetY);
            layoutParams.mInsetOffsetY = mInsetOffsetY;
        }
    }
    
    private void setupForInsets() {
        if (Build$VERSION.SDK_INT < 21) {
            return;
        }
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            if (this.mApplyWindowInsetsListener == null) {
                this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsets) {
                        return CoordinatorLayout.this.setWindowInsets(windowInsets);
                    }
                };
            }
            ViewCompat.setOnApplyWindowInsetsListener((View)this, this.mApplyWindowInsetsListener);
            this.setSystemUiVisibility(1280);
        }
        else {
            ViewCompat.setOnApplyWindowInsetsListener((View)this, null);
        }
    }
    
    void addPreDrawListener() {
        if (this.mIsAttachedToWindow) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            this.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = true;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public void dispatchDependentViewsChanged(final View view) {
        final List incomingEdges = this.mChildDag.getIncomingEdges(view);
        if (incomingEdges != null && !incomingEdges.isEmpty()) {
            for (int i = 0; i < incomingEdges.size(); ++i) {
                final View view2 = incomingEdges.get(i);
                final Behavior behavior = ((LayoutParams)view2.getLayoutParams()).getBehavior();
                if (behavior != null) {
                    behavior.onDependentViewChanged(this, view2, view);
                }
            }
        }
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mBehavior != null) {
            final float scrimOpacity = layoutParams.mBehavior.getScrimOpacity(this, view);
            if (scrimOpacity > 0.0f) {
                if (this.mScrimPaint == null) {
                    this.mScrimPaint = new Paint();
                }
                this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, view));
                this.mScrimPaint.setAlpha(clamp(Math.round(scrimOpacity * 255.0f), 0, 255));
                final int save = canvas.save();
                if (view.isOpaque()) {
                    canvas.clipRect((float)view.getLeft(), (float)view.getTop(), (float)view.getRight(), (float)view.getBottom(), Region$Op.DIFFERENCE);
                }
                canvas.drawRect((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - this.getPaddingBottom()), this.mScrimPaint);
                canvas.restoreToCount(save);
            }
        }
        return super.drawChild(canvas, view, n);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] drawableState = this.getDrawableState();
        final Drawable mStatusBarBackground = this.mStatusBarBackground;
        int n = 0;
        if (mStatusBarBackground != null) {
            n = n;
            if (mStatusBarBackground.isStateful()) {
                n = ((false | mStatusBarBackground.setState(drawableState)) ? 1 : 0);
            }
        }
        if (n != 0) {
            this.invalidate();
        }
    }
    
    void ensurePreDrawListener() {
        final int childCount = this.getChildCount();
        final boolean b = false;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= childCount) {
                break;
            }
            if (this.hasDependencies(this.getChildAt(n))) {
                b2 = true;
                break;
            }
            ++n;
        }
        if (b2 != this.mNeedsPreDrawListener) {
            if (b2) {
                this.addPreDrawListener();
            }
            else {
                this.removePreDrawListener();
            }
        }
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)viewGroup$LayoutParams);
        }
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    void getChildRect(final View view, final boolean b, final Rect rect) {
        if (!view.isLayoutRequested() && view.getVisibility() != 8) {
            if (b) {
                this.getDescendantRect(view, rect);
            }
            else {
                rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            return;
        }
        rect.setEmpty();
    }
    
    public List<View> getDependencies(final View view) {
        final List<View> outgoingEdges = this.mChildDag.getOutgoingEdges(view);
        this.mTempDependenciesList.clear();
        if (outgoingEdges != null) {
            this.mTempDependenciesList.addAll(outgoingEdges);
        }
        return this.mTempDependenciesList;
    }
    
    final List<View> getDependencySortedChildren() {
        this.prepareChildren();
        return Collections.unmodifiableList((List<? extends View>)this.mDependencySortedChildren);
    }
    
    public List<View> getDependents(final View view) {
        final List incomingEdges = this.mChildDag.getIncomingEdges(view);
        this.mTempDependenciesList.clear();
        if (incomingEdges != null) {
            this.mTempDependenciesList.addAll(incomingEdges);
        }
        return this.mTempDependenciesList;
    }
    
    void getDescendantRect(final View view, final Rect rect) {
        ViewGroupUtils.getDescendantRect(this, view, rect);
    }
    
    void getDesiredAnchoredChildRect(final View view, final int n, final Rect rect, final Rect rect2) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();
        this.getDesiredAnchoredChildRectWithoutConstraints(view, n, rect, rect2, layoutParams, measuredWidth, measuredHeight);
        this.constrainChildRect(layoutParams, rect2, measuredWidth, measuredHeight);
    }
    
    void getLastChildRect(final View view, final Rect rect) {
        rect.set(((LayoutParams)view.getLayoutParams()).getLastChildRect());
    }
    
    public final WindowInsetsCompat getLastWindowInsets() {
        return this.mLastInsets;
    }
    
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }
    
    LayoutParams getResolvedLayoutParams(View o) {
        final LayoutParams layoutParams = (LayoutParams)((View)o).getLayoutParams();
        if (!layoutParams.mBehaviorResolved) {
            if (o instanceof AttachedBehavior) {
                final Behavior behavior = ((AttachedBehavior)o).getBehavior();
                if (behavior == null) {
                    Log.e("CoordinatorLayout", "Attached behavior class is null");
                }
                layoutParams.setBehavior(behavior);
                layoutParams.mBehaviorResolved = true;
            }
            else {
                Class<?> clazz = o.getClass();
                o = null;
                while (clazz != null) {
                    final DefaultBehavior defaultBehavior = clazz.getAnnotation(DefaultBehavior.class);
                    if ((o = defaultBehavior) != null) {
                        break;
                    }
                    clazz = clazz.getSuperclass();
                    o = defaultBehavior;
                }
                if (o != null) {
                    try {
                        layoutParams.setBehavior((Behavior)((DefaultBehavior)o).value().getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]));
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Default behavior class ");
                        sb.append(((DefaultBehavior)o).value().getName());
                        sb.append(" could not be instantiated. Did you forget");
                        sb.append(" a default constructor?");
                        Log.e("CoordinatorLayout", sb.toString(), (Throwable)ex);
                    }
                }
                layoutParams.mBehaviorResolved = true;
            }
        }
        return layoutParams;
    }
    
    public Drawable getStatusBarBackground() {
        return this.mStatusBarBackground;
    }
    
    protected int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), this.getPaddingTop() + this.getPaddingBottom());
    }
    
    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), this.getPaddingLeft() + this.getPaddingRight());
    }
    
    public boolean isPointInChildBounds(final View view, final int n, final int n2) {
        final Rect acquireTempRect = acquireTempRect();
        this.getDescendantRect(view, acquireTempRect);
        try {
            return acquireTempRect.contains(n, n2);
        }
        finally {
            releaseTempRect(acquireTempRect);
        }
    }
    
    void offsetChildToAnchor(final View view, int n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mAnchorView != null) {
            final Rect acquireTempRect = acquireTempRect();
            final Rect acquireTempRect2 = acquireTempRect();
            final Rect acquireTempRect3 = acquireTempRect();
            this.getDescendantRect(layoutParams.mAnchorView, acquireTempRect);
            final int n2 = 0;
            this.getChildRect(view, false, acquireTempRect2);
            final int measuredWidth = view.getMeasuredWidth();
            final int measuredHeight = view.getMeasuredHeight();
            this.getDesiredAnchoredChildRectWithoutConstraints(view, n, acquireTempRect, acquireTempRect3, layoutParams, measuredWidth, measuredHeight);
            Label_0109: {
                if (acquireTempRect3.left == acquireTempRect2.left) {
                    n = n2;
                    if (acquireTempRect3.top == acquireTempRect2.top) {
                        break Label_0109;
                    }
                }
                n = 1;
            }
            this.constrainChildRect(layoutParams, acquireTempRect3, measuredWidth, measuredHeight);
            final int n3 = acquireTempRect3.left - acquireTempRect2.left;
            final int n4 = acquireTempRect3.top - acquireTempRect2.top;
            if (n3 != 0) {
                ViewCompat.offsetLeftAndRight(view, n3);
            }
            if (n4 != 0) {
                ViewCompat.offsetTopAndBottom(view, n4);
            }
            if (n != 0) {
                final Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    behavior.onDependentViewChanged(this, view, layoutParams.mAnchorView);
                }
            }
            releaseTempRect(acquireTempRect);
            releaseTempRect(acquireTempRect2);
            releaseTempRect(acquireTempRect3);
        }
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.resetTouchBehaviors(false);
        if (this.mNeedsPreDrawListener) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            this.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this.mOnPreDrawListener);
        }
        if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows((View)this)) {
            ViewCompat.requestApplyInsets((View)this);
        }
        this.mIsAttachedToWindow = true;
    }
    
    final void onChildViewsChanged(final int n) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        final int size = this.mDependencySortedChildren.size();
        final Rect acquireTempRect = acquireTempRect();
        final Rect acquireTempRect2 = acquireTempRect();
        final Rect acquireTempRect3 = acquireTempRect();
        for (int i = 0; i < size; ++i) {
            final View view = this.mDependencySortedChildren.get(i);
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (n != 0 || view.getVisibility() != 8) {
                for (int j = 0; j < i; ++j) {
                    if (layoutParams.mAnchorDirectChild == this.mDependencySortedChildren.get(j)) {
                        this.offsetChildToAnchor(view, layoutDirection);
                    }
                }
                this.getChildRect(view, true, acquireTempRect2);
                if (layoutParams.insetEdge != 0 && !acquireTempRect2.isEmpty()) {
                    final int absoluteGravity = GravityCompat.getAbsoluteGravity(layoutParams.insetEdge, layoutDirection);
                    final int n2 = absoluteGravity & 0x70;
                    if (n2 != 48) {
                        if (n2 == 80) {
                            acquireTempRect.bottom = Math.max(acquireTempRect.bottom, this.getHeight() - acquireTempRect2.top);
                        }
                    }
                    else {
                        acquireTempRect.top = Math.max(acquireTempRect.top, acquireTempRect2.bottom);
                    }
                    final int n3 = absoluteGravity & 0x7;
                    if (n3 != 3) {
                        if (n3 == 5) {
                            acquireTempRect.right = Math.max(acquireTempRect.right, this.getWidth() - acquireTempRect2.left);
                        }
                    }
                    else {
                        acquireTempRect.left = Math.max(acquireTempRect.left, acquireTempRect2.right);
                    }
                }
                if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0) {
                    this.offsetChildByInset(view, acquireTempRect, layoutDirection);
                }
                if (n != 2) {
                    this.getLastChildRect(view, acquireTempRect3);
                    if (acquireTempRect3.equals((Object)acquireTempRect2)) {
                        continue;
                    }
                    this.recordLastChildRect(view, acquireTempRect2);
                }
                for (int k = i + 1; k < size; ++k) {
                    final View view2 = this.mDependencySortedChildren.get(k);
                    final LayoutParams layoutParams2 = (LayoutParams)view2.getLayoutParams();
                    final Behavior behavior = layoutParams2.getBehavior();
                    if (behavior != null && behavior.layoutDependsOn(this, view2, view)) {
                        if (n == 0 && layoutParams2.getChangedAfterNestedScroll()) {
                            layoutParams2.resetChangedAfterNestedScroll();
                        }
                        else {
                            boolean onDependentViewChanged;
                            if (n != 2) {
                                onDependentViewChanged = behavior.onDependentViewChanged(this, view2, view);
                            }
                            else {
                                behavior.onDependentViewRemoved(this, view2, view);
                                onDependentViewChanged = true;
                            }
                            if (n == 1) {
                                layoutParams2.setChangedAfterNestedScroll(onDependentViewChanged);
                            }
                        }
                    }
                }
            }
        }
        releaseTempRect(acquireTempRect);
        releaseTempRect(acquireTempRect2);
        releaseTempRect(acquireTempRect3);
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.resetTouchBehaviors(false);
        if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
            this.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this.mOnPreDrawListener);
        }
        if (this.mNestedScrollingTarget != null) {
            this.onStopNestedScroll(this.mNestedScrollingTarget);
        }
        this.mIsAttachedToWindow = false;
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            int systemWindowInsetTop;
            if (this.mLastInsets != null) {
                systemWindowInsetTop = this.mLastInsets.getSystemWindowInsetTop();
            }
            else {
                systemWindowInsetTop = 0;
            }
            if (systemWindowInsetTop > 0) {
                this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), systemWindowInsetTop);
                this.mStatusBarBackground.draw(canvas);
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.resetTouchBehaviors(true);
        }
        final boolean performIntercept = this.performIntercept(motionEvent, 0);
        if (actionMasked == 1 || actionMasked == 3) {
            this.resetTouchBehaviors(true);
        }
        return performIntercept;
    }
    
    protected void onLayout(final boolean b, int i, int size, int layoutDirection, final int n) {
        layoutDirection = ViewCompat.getLayoutDirection((View)this);
        View view;
        Behavior behavior;
        for (size = this.mDependencySortedChildren.size(), i = 0; i < size; ++i) {
            view = this.mDependencySortedChildren.get(i);
            if (view.getVisibility() != 8) {
                behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
                if (behavior == null || !behavior.onLayoutChild(this, view, layoutDirection)) {
                    this.onLayoutChild(view, layoutDirection);
                }
            }
        }
    }
    
    public void onLayoutChild(final View view, final int n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.checkAnchorChanged()) {
            if (layoutParams.mAnchorView != null) {
                this.layoutChildWithAnchor(view, layoutParams.mAnchorView, n);
            }
            else if (layoutParams.keyline >= 0) {
                this.layoutChildWithKeyline(view, layoutParams.keyline, n);
            }
            else {
                this.layoutChild(view, n);
            }
            return;
        }
        throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.prepareChildren();
        this.ensurePreDrawListener();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        final int paddingRight = this.getPaddingRight();
        final int paddingBottom = this.getPaddingBottom();
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        final boolean b = layoutDirection == 1;
        final int mode = View$MeasureSpec.getMode(n);
        final int size = View$MeasureSpec.getSize(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        final int size2 = View$MeasureSpec.getSize(n2);
        int a = this.getSuggestedMinimumWidth();
        int n3 = this.getSuggestedMinimumHeight();
        final boolean b2 = this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this);
        final int size3 = this.mDependencySortedChildren.size();
        int combineMeasuredStates = 0;
        for (int i = 0; i < size3; ++i) {
            final View view = this.mDependencySortedChildren.get(i);
            if (view.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                int n5 = 0;
                Label_0293: {
                    if (layoutParams.keyline >= 0 && mode != 0) {
                        final int keyline = this.getKeyline(layoutParams.keyline);
                        final int n4 = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), layoutDirection) & 0x7;
                        if ((n4 == 3 && !b) || (n4 == 5 && b)) {
                            n5 = Math.max(0, size - paddingRight - keyline);
                            break Label_0293;
                        }
                        if ((n4 == 5 && !b) || (n4 == 3 && b)) {
                            n5 = Math.max(0, keyline - paddingLeft);
                            break Label_0293;
                        }
                    }
                    n5 = 0;
                }
                final int a2 = n3;
                int n6;
                int n7;
                if (b2 && !ViewCompat.getFitsSystemWindows(view)) {
                    final int systemWindowInsetLeft = this.mLastInsets.getSystemWindowInsetLeft();
                    final int systemWindowInsetRight = this.mLastInsets.getSystemWindowInsetRight();
                    final int systemWindowInsetTop = this.mLastInsets.getSystemWindowInsetTop();
                    final int systemWindowInsetBottom = this.mLastInsets.getSystemWindowInsetBottom();
                    final int measureSpec = View$MeasureSpec.makeMeasureSpec(size - (systemWindowInsetLeft + systemWindowInsetRight), mode);
                    final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(size2 - (systemWindowInsetTop + systemWindowInsetBottom), mode2);
                    n6 = measureSpec;
                    n7 = measureSpec2;
                }
                else {
                    n6 = n;
                    n7 = n2;
                }
                final Behavior behavior = layoutParams.getBehavior();
                if (behavior == null || !behavior.onMeasureChild(this, view, n6, n5, n7, 0)) {
                    this.onMeasureChild(view, n6, n5, n7, 0);
                }
                a = Math.max(a, paddingLeft + paddingRight + view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                n3 = Math.max(a2, paddingTop + paddingBottom + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, view.getMeasuredState());
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState(a, n, 0xFF000000 & combineMeasuredStates), View.resolveSizeAndState(n3, n2, combineMeasuredStates << 16));
    }
    
    public void onMeasureChild(final View view, final int n, final int n2, final int n3, final int n4) {
        this.measureChildWithMargins(view, n, n2, n3, n4);
    }
    
    public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
        final int childCount = this.getChildCount();
        int i = 0;
        int n3 = 0;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            boolean b2;
            if (child.getVisibility() == 8) {
                b2 = (n3 != 0);
            }
            else {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (!layoutParams.isNestedScrollAccepted(0)) {
                    b2 = (n3 != 0);
                }
                else {
                    final Behavior behavior = layoutParams.getBehavior();
                    b2 = (n3 != 0);
                    if (behavior != null) {
                        b2 = ((n3 | (behavior.onNestedFling(this, child, view, n, n2, b) ? 1 : 0)) != 0x0);
                    }
                }
            }
            ++i;
            n3 = (b2 ? 1 : 0);
        }
        if (n3 != 0) {
            this.onChildViewsChanged(1);
        }
        return n3 != 0;
    }
    
    public boolean onNestedPreFling(final View view, final float n, final float n2) {
        final int childCount = this.getChildCount();
        int i = 0;
        int n3 = 0;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            boolean b;
            if (child.getVisibility() == 8) {
                b = (n3 != 0);
            }
            else {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (!layoutParams.isNestedScrollAccepted(0)) {
                    b = (n3 != 0);
                }
                else {
                    final Behavior behavior = layoutParams.getBehavior();
                    b = (n3 != 0);
                    if (behavior != null) {
                        b = ((n3 | (behavior.onNestedPreFling(this, child, view, n, n2) ? 1 : 0)) != 0x0);
                    }
                }
            }
            ++i;
            n3 = (b ? 1 : 0);
        }
        return n3 != 0;
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
        this.onNestedPreScroll(view, n, n2, array, 0);
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int n3) {
        final int childCount = this.getChildCount();
        boolean b = false;
        int i = 0;
        int n4 = 0;
        int n5 = 0;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            int n6;
            int n7;
            if (child.getVisibility() == 8) {
                n6 = n4;
                n7 = n5;
            }
            else {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (!layoutParams.isNestedScrollAccepted(n3)) {
                    n6 = n4;
                    n7 = n5;
                }
                else {
                    final Behavior behavior = layoutParams.getBehavior();
                    n6 = n4;
                    n7 = n5;
                    if (behavior != null) {
                        this.mTempIntPair[this.mTempIntPair[1] = 0] = 0;
                        behavior.onNestedPreScroll(this, child, view, n, n2, this.mTempIntPair, n3);
                        int n8;
                        if (n > 0) {
                            n8 = Math.max(n4, this.mTempIntPair[0]);
                        }
                        else {
                            n8 = Math.min(n4, this.mTempIntPair[0]);
                        }
                        int n9;
                        if (n2 > 0) {
                            n9 = Math.max(n5, this.mTempIntPair[1]);
                        }
                        else {
                            n9 = Math.min(n5, this.mTempIntPair[1]);
                        }
                        n6 = n8;
                        n7 = n9;
                        b = true;
                    }
                }
            }
            ++i;
            n4 = n6;
            n5 = n7;
        }
        array[0] = n4;
        array[1] = n5;
        if (b) {
            this.onChildViewsChanged(1);
        }
    }
    
    public void onNestedScroll(final View view, final int n, final int n2, final int n3, final int n4) {
        this.onNestedScroll(view, n, n2, n3, n4, 0);
    }
    
    public void onNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        final int childCount = this.getChildCount();
        boolean b = false;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(n5)) {
                    final Behavior behavior = layoutParams.getBehavior();
                    if (behavior != null) {
                        behavior.onNestedScroll(this, child, view, n, n2, n3, n4, n5);
                        b = true;
                    }
                }
            }
        }
        if (b) {
            this.onChildViewsChanged(1);
        }
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.onNestedScrollAccepted(view, view2, n, 0);
    }
    
    public void onNestedScrollAccepted(final View view, final View mNestedScrollingTarget, final int n, final int n2) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, mNestedScrollingTarget, n, n2);
        this.mNestedScrollingTarget = mNestedScrollingTarget;
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.isNestedScrollAccepted(n2)) {
                final Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    behavior.onNestedScrollAccepted(this, child, view, mNestedScrollingTarget, n, n2);
                }
            }
        }
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        final SparseArray<Parcelable> behaviorStates = savedState.behaviorStates;
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            final int id = child.getId();
            final Behavior behavior = this.getResolvedLayoutParams(child).getBehavior();
            if (id != -1 && behavior != null) {
                final Parcelable parcelable2 = (Parcelable)behaviorStates.get(id);
                if (parcelable2 != null) {
                    behavior.onRestoreInstanceState(this, child, parcelable2);
                }
            }
        }
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        final SparseArray behaviorStates = new SparseArray();
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final int id = child.getId();
            final Behavior behavior = ((LayoutParams)child.getLayoutParams()).getBehavior();
            if (id != -1 && behavior != null) {
                final Parcelable onSaveInstanceState = behavior.onSaveInstanceState(this, child);
                if (onSaveInstanceState != null) {
                    behaviorStates.append(id, (Object)onSaveInstanceState);
                }
            }
        }
        savedState.behaviorStates = (SparseArray<Parcelable>)behaviorStates;
        return (Parcelable)savedState;
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n) {
        return this.onStartNestedScroll(view, view2, n, 0);
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n, final int n2) {
        final int childCount = this.getChildCount();
        int i = 0;
        boolean b = false;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    final boolean onStartNestedScroll = behavior.onStartNestedScroll(this, child, view, view2, n, n2);
                    layoutParams.setNestedScrollAccepted(n2, onStartNestedScroll);
                    b |= onStartNestedScroll;
                }
                else {
                    layoutParams.setNestedScrollAccepted(n2, false);
                }
            }
            ++i;
        }
        return b;
    }
    
    public void onStopNestedScroll(final View view) {
        this.onStopNestedScroll(view, 0);
    }
    
    public void onStopNestedScroll(final View view, final int n) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view, n);
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.isNestedScrollAccepted(n)) {
                final Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    behavior.onStopNestedScroll(this, child, view, n);
                }
                layoutParams.resetNestedScroll(n);
                layoutParams.resetChangedAfterNestedScroll();
            }
        }
        this.mNestedScrollingTarget = null;
    }
    
    public boolean onTouchEvent(MotionEvent obtain) {
        final int actionMasked = obtain.getActionMasked();
        int performIntercept = 0;
        boolean onTouchEvent = false;
        Label_0080: {
            Behavior behavior;
            while (true) {
                Label_0044: {
                    if (this.mBehaviorTouchView != null) {
                        performIntercept = 0;
                        break Label_0044;
                    }
                    performIntercept = (this.performIntercept(obtain, 1) ? 1 : 0);
                    final int n;
                    if ((n = performIntercept) != 0) {
                        break Label_0044;
                    }
                    final int n2 = 0;
                    performIntercept = n;
                    onTouchEvent = (n2 != 0);
                    break Label_0080;
                }
                behavior = ((LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
                int n = performIntercept;
                if (behavior == null) {
                    continue;
                }
                break;
            }
            onTouchEvent = behavior.onTouchEvent(this, this.mBehaviorTouchView, obtain);
        }
        final View mBehaviorTouchView = this.mBehaviorTouchView;
        final MotionEvent motionEvent = null;
        boolean b;
        if (mBehaviorTouchView == null) {
            b = (onTouchEvent | super.onTouchEvent(obtain));
            obtain = motionEvent;
        }
        else {
            b = onTouchEvent;
            obtain = motionEvent;
            if (performIntercept != 0) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                super.onTouchEvent(obtain);
                b = onTouchEvent;
            }
        }
        if (obtain != null) {
            obtain.recycle();
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.resetTouchBehaviors(false);
        }
        return b;
    }
    
    void recordLastChildRect(final View view, final Rect lastChildRect) {
        ((LayoutParams)view.getLayoutParams()).setLastChildRect(lastChildRect);
    }
    
    void removePreDrawListener() {
        if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null) {
            this.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = false;
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        final Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        return (behavior != null && behavior.onRequestChildRectangleOnScreen(this, view, rect, b)) || super.requestChildRectangleOnScreen(view, rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        super.requestDisallowInterceptTouchEvent(b);
        if (b && !this.mDisallowInterceptReset) {
            this.resetTouchBehaviors(false);
            this.mDisallowInterceptReset = true;
        }
    }
    
    public void setFitsSystemWindows(final boolean fitsSystemWindows) {
        super.setFitsSystemWindows(fitsSystemWindows);
        this.setupForInsets();
    }
    
    public void setOnHierarchyChangeListener(final ViewGroup$OnHierarchyChangeListener mOnHierarchyChangeListener) {
        this.mOnHierarchyChangeListener = mOnHierarchyChangeListener;
    }
    
    public void setStatusBarBackground(Drawable mStatusBarBackground) {
        if (this.mStatusBarBackground != mStatusBarBackground) {
            final Drawable mStatusBarBackground2 = this.mStatusBarBackground;
            Drawable mutate = null;
            if (mStatusBarBackground2 != null) {
                this.mStatusBarBackground.setCallback((Drawable$Callback)null);
            }
            if (mStatusBarBackground != null) {
                mutate = mStatusBarBackground.mutate();
            }
            this.mStatusBarBackground = mutate;
            if (this.mStatusBarBackground != null) {
                if (this.mStatusBarBackground.isStateful()) {
                    this.mStatusBarBackground.setState(this.getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection((View)this));
                mStatusBarBackground = this.mStatusBarBackground;
                mStatusBarBackground.setVisible(this.getVisibility() == 0, false);
                this.mStatusBarBackground.setCallback((Drawable$Callback)this);
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public void setStatusBarBackgroundColor(final int n) {
        this.setStatusBarBackground((Drawable)new ColorDrawable(n));
    }
    
    public void setStatusBarBackgroundResource(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = ContextCompat.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setStatusBarBackground(drawable);
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        final boolean b = visibility == 0;
        if (this.mStatusBarBackground != null && this.mStatusBarBackground.isVisible() != b) {
            this.mStatusBarBackground.setVisible(b, false);
        }
    }
    
    final WindowInsetsCompat setWindowInsets(final WindowInsetsCompat mLastInsets) {
        WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors = mLastInsets;
        if (!ObjectsCompat.equals(this.mLastInsets, mLastInsets)) {
            this.mLastInsets = mLastInsets;
            final boolean b = true;
            this.mDrawStatusBarBackground = (mLastInsets != null && mLastInsets.getSystemWindowInsetTop() > 0);
            this.setWillNotDraw(!this.mDrawStatusBarBackground && this.getBackground() == null && b);
            dispatchApplyWindowInsetsToBehaviors = this.dispatchApplyWindowInsetsToBehaviors(mLastInsets);
            this.requestLayout();
        }
        return dispatchApplyWindowInsetsToBehaviors;
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mStatusBarBackground;
    }
    
    public interface AttachedBehavior
    {
        Behavior getBehavior();
    }
    
    public abstract static class Behavior<V extends View>
    {
        public Behavior() {
        }
        
        public Behavior(final Context context, final AttributeSet set) {
        }
        
        public boolean blocksInteractionBelow(final CoordinatorLayout coordinatorLayout, final V v) {
            return this.getScrimOpacity(coordinatorLayout, v) > 0.0f;
        }
        
        public boolean getInsetDodgeRect(final CoordinatorLayout coordinatorLayout, final V v, final Rect rect) {
            return false;
        }
        
        public int getScrimColor(final CoordinatorLayout coordinatorLayout, final V v) {
            return -16777216;
        }
        
        public float getScrimOpacity(final CoordinatorLayout coordinatorLayout, final V v) {
            return 0.0f;
        }
        
        public boolean layoutDependsOn(final CoordinatorLayout coordinatorLayout, final V v, final View view) {
            return false;
        }
        
        public WindowInsetsCompat onApplyWindowInsets(final CoordinatorLayout coordinatorLayout, final V v, final WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }
        
        public void onAttachedToLayoutParams(final LayoutParams layoutParams) {
        }
        
        public boolean onDependentViewChanged(final CoordinatorLayout coordinatorLayout, final V v, final View view) {
            return false;
        }
        
        public void onDependentViewRemoved(final CoordinatorLayout coordinatorLayout, final V v, final View view) {
        }
        
        public void onDetachedFromLayoutParams() {
        }
        
        public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
            return false;
        }
        
        public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
            return false;
        }
        
        public boolean onMeasureChild(final CoordinatorLayout coordinatorLayout, final V v, final int n, final int n2, final int n3, final int n4) {
            return false;
        }
        
        public boolean onNestedFling(final CoordinatorLayout coordinatorLayout, final V v, final View view, final float n, final float n2, final boolean b) {
            return false;
        }
        
        public boolean onNestedPreFling(final CoordinatorLayout coordinatorLayout, final V v, final View view, final float n, final float n2) {
            return false;
        }
        
        @Deprecated
        public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final int n, final int n2, final int[] array) {
        }
        
        public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final int n, final int n2, final int[] array, final int n3) {
            if (n3 == 0) {
                this.onNestedPreScroll(coordinatorLayout, v, view, n, n2, array);
            }
        }
        
        @Deprecated
        public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final int n, final int n2, final int n3, final int n4) {
        }
        
        public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
            if (n5 == 0) {
                this.onNestedScroll(coordinatorLayout, v, view, n, n2, n3, n4);
            }
        }
        
        @Deprecated
        public void onNestedScrollAccepted(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n) {
        }
        
        public void onNestedScrollAccepted(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n, final int n2) {
            if (n2 == 0) {
                this.onNestedScrollAccepted(coordinatorLayout, v, view, view2, n);
            }
        }
        
        public boolean onRequestChildRectangleOnScreen(final CoordinatorLayout coordinatorLayout, final V v, final Rect rect, final boolean b) {
            return false;
        }
        
        public void onRestoreInstanceState(final CoordinatorLayout coordinatorLayout, final V v, final Parcelable parcelable) {
        }
        
        public Parcelable onSaveInstanceState(final CoordinatorLayout coordinatorLayout, final V v) {
            return (Parcelable)View$BaseSavedState.EMPTY_STATE;
        }
        
        @Deprecated
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n) {
            return false;
        }
        
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n, final int n2) {
            return n2 == 0 && this.onStartNestedScroll(coordinatorLayout, v, view, view2, n);
        }
        
        @Deprecated
        public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view) {
        }
        
        public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final int n) {
            if (n == 0) {
                this.onStopNestedScroll(coordinatorLayout, v, view);
            }
        }
        
        public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
            return false;
        }
    }
    
    @Deprecated
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultBehavior {
        Class<? extends Behavior> value();
    }
    
    private class HierarchyChangeListener implements ViewGroup$OnHierarchyChangeListener
    {
        HierarchyChangeListener() {
        }
        
        public void onChildViewAdded(final View view, final View view2) {
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }
        
        public void onChildViewRemoved(final View view, final View view2) {
            CoordinatorLayout.this.onChildViewsChanged(2);
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int anchorGravity;
        public int dodgeInsetEdges;
        public int gravity;
        public int insetEdge;
        public int keyline;
        View mAnchorDirectChild;
        int mAnchorId;
        View mAnchorView;
        Behavior mBehavior;
        boolean mBehaviorResolved;
        Object mBehaviorTag;
        private boolean mDidAcceptNestedScrollNonTouch;
        private boolean mDidAcceptNestedScrollTouch;
        private boolean mDidBlockInteraction;
        private boolean mDidChangeAfterNestedScroll;
        int mInsetOffsetX;
        int mInsetOffsetY;
        final Rect mLastChildRect;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }
        
        LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.CoordinatorLayout_Layout);
            this.gravity = obtainStyledAttributes.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
            this.mAnchorId = obtainStyledAttributes.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
            this.anchorGravity = obtainStyledAttributes.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
            this.keyline = obtainStyledAttributes.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
            this.insetEdge = obtainStyledAttributes.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
            this.dodgeInsetEdges = obtainStyledAttributes.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
            this.mBehaviorResolved = obtainStyledAttributes.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
            if (this.mBehaviorResolved) {
                this.mBehavior = CoordinatorLayout.parseBehavior(context, set, obtainStyledAttributes.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior));
            }
            obtainStyledAttributes.recycle();
            if (this.mBehavior != null) {
                this.mBehavior.onAttachedToLayoutParams(this);
            }
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }
        
        private void resolveAnchorView(final View obj, final CoordinatorLayout coordinatorLayout) {
            this.mAnchorView = coordinatorLayout.findViewById(this.mAnchorId);
            if (this.mAnchorView != null) {
                if (this.mAnchorView != coordinatorLayout) {
                    View mAnchorView = this.mAnchorView;
                    ViewParent viewParent = this.mAnchorView.getParent();
                    while (viewParent != coordinatorLayout && viewParent != null) {
                        if (viewParent == obj) {
                            if (coordinatorLayout.isInEditMode()) {
                                this.mAnchorDirectChild = null;
                                this.mAnchorView = null;
                                return;
                            }
                            throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                        }
                        else {
                            if (viewParent instanceof View) {
                                mAnchorView = (View)viewParent;
                            }
                            viewParent = viewParent.getParent();
                        }
                    }
                    this.mAnchorDirectChild = mAnchorView;
                    return;
                }
                if (coordinatorLayout.isInEditMode()) {
                    this.mAnchorDirectChild = null;
                    this.mAnchorView = null;
                    return;
                }
                throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
            }
            else {
                if (coordinatorLayout.isInEditMode()) {
                    this.mAnchorDirectChild = null;
                    this.mAnchorView = null;
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Could not find CoordinatorLayout descendant view with id ");
                sb.append(coordinatorLayout.getResources().getResourceName(this.mAnchorId));
                sb.append(" to anchor view ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString());
            }
        }
        
        private boolean shouldDodge(final View view, final int n) {
            final int absoluteGravity = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).insetEdge, n);
            return absoluteGravity != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, n) & absoluteGravity) == absoluteGravity;
        }
        
        private boolean verifyAnchorView(final View view, final CoordinatorLayout coordinatorLayout) {
            if (this.mAnchorView.getId() != this.mAnchorId) {
                return false;
            }
            View mAnchorView = this.mAnchorView;
            for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != coordinatorLayout; viewParent = viewParent.getParent()) {
                if (viewParent == null || viewParent == view) {
                    this.mAnchorDirectChild = null;
                    this.mAnchorView = null;
                    return false;
                }
                if (viewParent instanceof View) {
                    mAnchorView = (View)viewParent;
                }
            }
            this.mAnchorDirectChild = mAnchorView;
            return true;
        }
        
        boolean checkAnchorChanged() {
            return this.mAnchorView == null && this.mAnchorId != -1;
        }
        
        boolean dependsOn(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
            return view2 == this.mAnchorDirectChild || this.shouldDodge(view2, ViewCompat.getLayoutDirection((View)coordinatorLayout)) || (this.mBehavior != null && this.mBehavior.layoutDependsOn(coordinatorLayout, view, view2));
        }
        
        boolean didBlockInteraction() {
            if (this.mBehavior == null) {
                this.mDidBlockInteraction = false;
            }
            return this.mDidBlockInteraction;
        }
        
        View findAnchorView(final CoordinatorLayout coordinatorLayout, final View view) {
            if (this.mAnchorId == -1) {
                this.mAnchorDirectChild = null;
                return this.mAnchorView = null;
            }
            if (this.mAnchorView == null || !this.verifyAnchorView(view, coordinatorLayout)) {
                this.resolveAnchorView(view, coordinatorLayout);
            }
            return this.mAnchorView;
        }
        
        public int getAnchorId() {
            return this.mAnchorId;
        }
        
        public Behavior getBehavior() {
            return this.mBehavior;
        }
        
        boolean getChangedAfterNestedScroll() {
            return this.mDidChangeAfterNestedScroll;
        }
        
        Rect getLastChildRect() {
            return this.mLastChildRect;
        }
        
        boolean isBlockingInteractionBelow(final CoordinatorLayout coordinatorLayout, final View view) {
            return this.mDidBlockInteraction || (this.mDidBlockInteraction |= (this.mBehavior != null && this.mBehavior.blocksInteractionBelow(coordinatorLayout, view)));
        }
        
        boolean isNestedScrollAccepted(final int n) {
            switch (n) {
                default: {
                    return false;
                }
                case 1: {
                    return this.mDidAcceptNestedScrollNonTouch;
                }
                case 0: {
                    return this.mDidAcceptNestedScrollTouch;
                }
            }
        }
        
        void resetChangedAfterNestedScroll() {
            this.mDidChangeAfterNestedScroll = false;
        }
        
        void resetNestedScroll(final int n) {
            this.setNestedScrollAccepted(n, false);
        }
        
        void resetTouchBehaviorTracking() {
            this.mDidBlockInteraction = false;
        }
        
        public void setBehavior(final Behavior mBehavior) {
            if (this.mBehavior != mBehavior) {
                if (this.mBehavior != null) {
                    this.mBehavior.onDetachedFromLayoutParams();
                }
                this.mBehavior = mBehavior;
                this.mBehaviorTag = null;
                this.mBehaviorResolved = true;
                if (mBehavior != null) {
                    mBehavior.onAttachedToLayoutParams(this);
                }
            }
        }
        
        void setChangedAfterNestedScroll(final boolean mDidChangeAfterNestedScroll) {
            this.mDidChangeAfterNestedScroll = mDidChangeAfterNestedScroll;
        }
        
        void setLastChildRect(final Rect rect) {
            this.mLastChildRect.set(rect);
        }
        
        void setNestedScrollAccepted(final int n, final boolean b) {
            switch (n) {
                case 1: {
                    this.mDidAcceptNestedScrollNonTouch = b;
                    break;
                }
                case 0: {
                    this.mDidAcceptNestedScrollTouch = b;
                    break;
                }
            }
        }
    }
    
    class OnPreDrawListener implements ViewTreeObserver$OnPreDrawListener
    {
        public boolean onPreDraw() {
            CoordinatorLayout.this.onChildViewsChanged(0);
            return true;
        }
    }
    
    protected static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        SparseArray<Parcelable> behaviorStates;
        
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
            final int int1 = parcel.readInt();
            final int[] array = new int[int1];
            parcel.readIntArray(array);
            final Parcelable[] parcelableArray = parcel.readParcelableArray(classLoader);
            this.behaviorStates = (SparseArray<Parcelable>)new SparseArray(int1);
            for (int i = 0; i < int1; ++i) {
                this.behaviorStates.append(array[i], (Object)parcelableArray[i]);
            }
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            final SparseArray<Parcelable> behaviorStates = this.behaviorStates;
            int i = 0;
            int size;
            if (behaviorStates != null) {
                size = this.behaviorStates.size();
            }
            else {
                size = 0;
            }
            parcel.writeInt(size);
            final int[] array = new int[size];
            final Parcelable[] array2 = new Parcelable[size];
            while (i < size) {
                array[i] = this.behaviorStates.keyAt(i);
                array2[i] = (Parcelable)this.behaviorStates.valueAt(i);
                ++i;
            }
            parcel.writeIntArray(array);
            parcel.writeParcelableArray(array2, n);
        }
    }
    
    static class ViewElevationComparator implements Comparator<View>
    {
        @Override
        public int compare(final View view, final View view2) {
            final float z = ViewCompat.getZ(view);
            final float z2 = ViewCompat.getZ(view2);
            if (z > z2) {
                return -1;
            }
            if (z < z2) {
                return 1;
            }
            return 0;
        }
    }
}
