package android.support.p000v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.p002os.ParcelableCompat;
import android.support.p000v4.p002os.ParcelableCompatCreatorCallbacks;
import android.support.p000v4.view.accessibility.AccessibilityEventCompat;
import android.support.p000v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p000v4.view.accessibility.AccessibilityRecordCompat;
import android.support.p000v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* renamed from: android.support.v4.view.ViewPager */
public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR = new C01271();
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new C01282();
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId = -1;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable = new C01293();
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    /* renamed from: android.support.v4.view.ViewPager$1 */
    static class C01271 implements Comparator<ItemInfo> {
        C01271() {
        }

        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$2 */
    static class C01282 implements Interpolator {
        C01282() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$3 */
    class C01293 implements Runnable {
        C01293() {
        }

        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    }

    @Inherited
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    /* renamed from: android.support.v4.view.ViewPager$DecorView */
    public @interface DecorView {
    }

    /* renamed from: android.support.v4.view.ViewPager$ItemInfo */
    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$LayoutParams */
    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$OnAdapterChangeListener */
    public interface OnAdapterChangeListener {
        void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter2);
    }

    /* renamed from: android.support.v4.view.ViewPager$OnPageChangeListener */
    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    /* renamed from: android.support.v4.view.ViewPager$PageTransformer */
    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    /* renamed from: android.support.v4.view.ViewPager$PagerObserver */
    private class PagerObserver extends DataSetObserver {
        PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$ViewPositionComparator */
    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            LayoutParams llp = (LayoutParams) lhs.getLayoutParams();
            LayoutParams rlp = (LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor != rlp.isDecor) {
                return llp.isDecor ? 1 : -1;
            } else {
                return llp.position - rlp.position;
            }
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$4 */
    class C01304 implements OnApplyWindowInsetsListener {
        private final Rect mTempRect = new Rect();

        C01304() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat originalInsets) {
            WindowInsetsCompat applied = ViewCompat.onApplyWindowInsets(v, originalInsets);
            if (applied.isConsumed()) {
                return applied;
            }
            Rect res = this.mTempRect;
            res.left = applied.getSystemWindowInsetLeft();
            res.top = applied.getSystemWindowInsetTop();
            res.right = applied.getSystemWindowInsetRight();
            res.bottom = applied.getSystemWindowInsetBottom();
            int count = ViewPager.this.getChildCount();
            for (int i = 0; i < count; i++) {
                WindowInsetsCompat childInsets = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), applied);
                res.left = Math.min(childInsets.getSystemWindowInsetLeft(), res.left);
                res.top = Math.min(childInsets.getSystemWindowInsetTop(), res.top);
                res.right = Math.min(childInsets.getSystemWindowInsetRight(), res.right);
                res.bottom = Math.min(childInsets.getSystemWindowInsetBottom(), res.bottom);
            }
            return applied.replaceSystemWindowInsets(res.left, res.top, res.right, res.bottom);
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$MyAccessibilityDelegate */
    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            AccessibilityRecordCompat recordCompat = AccessibilityEventCompat.asRecord(event);
            recordCompat.setScrollable(canScroll());
            if (event.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                recordCompat.setItemCount(ViewPager.this.mAdapter.getCount());
                recordCompat.setFromIndex(ViewPager.this.mCurItem);
                recordCompat.setToIndex(ViewPager.this.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                info.addAction(8192);
            }
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            switch (action) {
                case 4096:
                    if (!ViewPager.this.canScrollHorizontally(1)) {
                        return false;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                    return true;
                case 8192:
                    if (!ViewPager.this.canScrollHorizontally(-1)) {
                        return false;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                    return true;
                default:
                    return false;
            }
        }

        private boolean canScroll() {
            return ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$SavedState */
    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new C01311());
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        /* renamed from: android.support.v4.view.ViewPager$SavedState$1 */
        static class C01311 implements ParcelableCompatCreatorCallbacks<SavedState> {
            C01311() {
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$SimpleOnPageChangeListener */
    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public ViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    /* Access modifiers changed, original: 0000 */
    public void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = configuration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int) (400.0f * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        this.mFlingDistance = (int) (25.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mDefaultGutterSize = (int) (16.0f * density);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener(this, new C01304());
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        if (!(this.mScroller == null || this.mScroller.isFinished())) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    /* Access modifiers changed, original: 0000 */
    public void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                enableLayers(newState != 0);
            }
            dispatchOnScrollStateChanged(newState);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        int i;
        if (this.mAdapter != null) {
            this.mAdapter.setViewPagerObserver(null);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (wasFirstLayout) {
                requestLayout();
            } else {
                populate();
            }
        }
        if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
            int count = this.mAdapterChangeListeners.size();
            for (i = 0; i < count; i++) {
                ((OnAdapterChangeListener) this.mAdapterChangeListeners.get(i)).onAdapterChanged(this, oldAdapter, adapter);
            }
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList();
        }
        this.mAdapterChangeListeners.add(listener);
    }

    public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners != null) {
            this.mAdapterChangeListeners.remove(listener);
        }
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public void setCurrentItem(int item) {
        boolean z;
        this.mPopulatePending = false;
        if (this.mFirstLayout) {
            z = false;
        } else {
            z = true;
        }
        setCurrentItemInternal(item, z, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    /* Access modifiers changed, original: 0000 */
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        boolean dispatchSelected = true;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (always || this.mCurItem != item || this.mItems.size() == 0) {
            if (item < 0) {
                item = 0;
            } else if (item >= this.mAdapter.getCount()) {
                item = this.mAdapter.getCount() - 1;
            }
            int pageLimit = this.mOffscreenPageLimit;
            if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                for (int i = 0; i < this.mItems.size(); i++) {
                    ((ItemInfo) this.mItems.get(i)).scrolling = true;
                }
            }
            if (this.mCurItem == item) {
                dispatchSelected = false;
            }
            if (this.mFirstLayout) {
                this.mCurItem = item;
                if (dispatchSelected) {
                    dispatchOnPageSelected(item);
                }
                requestLayout();
                return;
            }
            populate(item);
            scrollToItem(item, smoothScroll, velocity, dispatchSelected);
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo curInfo = infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            destX = (int) (((float) getClientWidth()) * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
                return;
            }
            return;
        }
        if (dispatchSelected) {
            dispatchOnPageSelected(item);
        }
        completeScroll(false);
        scrollTo(destX, 0);
        pageScrolled(destX);
    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList();
        }
        this.mOnPageChangeListeners.add(listener);
    }

    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListeners() {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.clear();
        }
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        setPageTransformer(reverseDrawingOrder, transformer, 2);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer, int pageLayerType) {
        int i = 1;
        if (VERSION.SDK_INT >= 11) {
            boolean z;
            boolean hasTransformer = transformer != null;
            if (this.mPageTransformer != null) {
                z = true;
            } else {
                z = false;
            }
            boolean needsPopulate = hasTransformer != z;
            this.mPageTransformer = transformer;
            setChildrenDrawingOrderEnabledCompat(hasTransformer);
            if (hasTransformer) {
                if (reverseDrawingOrder) {
                    i = 2;
                }
                this.mDrawingOrder = i;
                this.mPageTransformerLayerType = pageLayerType;
            } else {
                this.mDrawingOrder = 0;
            }
            if (needsPopulate) {
                populate();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setChildrenDrawingOrderEnabledCompat(boolean enable) {
        if (VERSION.SDK_INT >= 7) {
            if (this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "Can't find setChildrenDrawingOrderEnabled", e);
                }
            }
            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[]{Boolean.valueOf(enable)});
            } catch (Exception e2) {
                Log.e(TAG, "Error changing children drawing order", e2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        int index;
        if (this.mDrawingOrder == 2) {
            index = (childCount - 1) - i;
        } else {
            index = i;
        }
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
    }

    /* Access modifiers changed, original: 0000 */
    public OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w(TAG, "Requested offscreen page limit " + limit + " too small; defaulting to " + 1);
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageMarginDrawable(@DrawableRes int resId) {
        setPageMarginDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    /* Access modifiers changed, original: 0000 */
    public void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx;
        boolean wasScrolling = (this.mScroller == null || this.mScroller.isFinished()) ? false : true;
        if (wasScrolling) {
            sx = this.mIsScrollStarted ? this.mScroller.getCurrX() : this.mScroller.getStartX();
            this.mScroller.abortAnimation();
            setScrollingCacheEnabled(false);
        } else {
            sx = getScrollX();
        }
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }
        int duration;
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getClientWidth();
        int halfWidth = width / 2;
        float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(1.0f, (1.0f * ((float) Math.abs(dx))) / ((float) width))));
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
        } else {
            duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) this.mPageMargin) + (((float) width) * this.mAdapter.getPageWidth(this.mCurItem))))) * 100.0f);
        }
        duration = Math.min(duration, MAX_SETTLE_DURATION);
        this.mIsScrollStarted = false;
        this.mScroller.startScroll(sx, sy, dx, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index < 0 || index >= this.mItems.size()) {
            this.mItems.add(ii);
        } else {
            this.mItems.add(index, ii);
        }
        return ii;
    }

    /* Access modifiers changed, original: 0000 */
    public void dataSetChanged() {
        boolean needPopulate;
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        if (this.mItems.size() >= (this.mOffscreenPageLimit * 2) + 1 || this.mItems.size() >= adapterCount) {
            needPopulate = false;
        } else {
            needPopulate = true;
        }
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i--;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate = true;
                }
            }
            i++;
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            int childCount = getChildCount();
            for (i = 0; i < childCount; i++) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(newCurrItem, false, true);
            requestLayout();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void populate() {
        populate(this.mCurItem);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x040f  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01dd  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0200  */
    /* JADX WARNING: Removed duplicated region for block: B:173:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x041c  */
    public void populate(int r30) {
        /*
        r29 = this;
        r20 = 0;
        r0 = r29;
        r0 = r0.mCurItem;
        r26 = r0;
        r0 = r26;
        r1 = r30;
        if (r0 == r1) goto L_0x0022;
    L_0x000e:
        r0 = r29;
        r0 = r0.mCurItem;
        r26 = r0;
        r0 = r29;
        r1 = r26;
        r20 = r0.infoForPosition(r1);
        r0 = r30;
        r1 = r29;
        r1.mCurItem = r0;
    L_0x0022:
        r0 = r29;
        r0 = r0.mAdapter;
        r26 = r0;
        if (r26 != 0) goto L_0x002e;
    L_0x002a:
        r29.sortChildDrawingOrder();
    L_0x002d:
        return;
    L_0x002e:
        r0 = r29;
        r0 = r0.mPopulatePending;
        r26 = r0;
        if (r26 == 0) goto L_0x003a;
    L_0x0036:
        r29.sortChildDrawingOrder();
        goto L_0x002d;
    L_0x003a:
        r26 = r29.getWindowToken();
        if (r26 == 0) goto L_0x002d;
    L_0x0040:
        r0 = r29;
        r0 = r0.mAdapter;
        r26 = r0;
        r0 = r26;
        r1 = r29;
        r0.startUpdate(r1);
        r0 = r29;
        r0 = r0.mOffscreenPageLimit;
        r21 = r0;
        r26 = 0;
        r0 = r29;
        r0 = r0.mCurItem;
        r27 = r0;
        r27 = r27 - r21;
        r25 = java.lang.Math.max(r26, r27);
        r0 = r29;
        r0 = r0.mAdapter;
        r26 = r0;
        r4 = r26.getCount();
        r26 = r4 + -1;
        r0 = r29;
        r0 = r0.mCurItem;
        r27 = r0;
        r27 = r27 + r21;
        r12 = java.lang.Math.min(r26, r27);
        r0 = r29;
        r0 = r0.mExpectedAdapterCount;
        r26 = r0;
        r0 = r26;
        if (r4 == r0) goto L_0x00f4;
    L_0x0083:
        r26 = r29.getResources();	 Catch:{ NotFoundException -> 0x00ea }
        r27 = r29.getId();	 Catch:{ NotFoundException -> 0x00ea }
        r23 = r26.getResourceName(r27);	 Catch:{ NotFoundException -> 0x00ea }
    L_0x008f:
        r26 = new java.lang.IllegalStateException;
        r27 = new java.lang.StringBuilder;
        r27.<init>();
        r28 = "The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ";
        r27 = r27.append(r28);
        r0 = r29;
        r0 = r0.mExpectedAdapterCount;
        r28 = r0;
        r27 = r27.append(r28);
        r28 = ", found: ";
        r27 = r27.append(r28);
        r0 = r27;
        r27 = r0.append(r4);
        r28 = " Pager id: ";
        r27 = r27.append(r28);
        r0 = r27;
        r1 = r23;
        r27 = r0.append(r1);
        r28 = " Pager class: ";
        r27 = r27.append(r28);
        r28 = r29.getClass();
        r27 = r27.append(r28);
        r28 = " Problematic adapter: ";
        r27 = r27.append(r28);
        r0 = r29;
        r0 = r0.mAdapter;
        r28 = r0;
        r28 = r28.getClass();
        r27 = r27.append(r28);
        r27 = r27.toString();
        r26.<init>(r27);
        throw r26;
    L_0x00ea:
        r11 = move-exception;
        r26 = r29.getId();
        r23 = java.lang.Integer.toHexString(r26);
        goto L_0x008f;
    L_0x00f4:
        r8 = -1;
        r9 = 0;
        r8 = 0;
    L_0x00f7:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r26 = r26.size();
        r0 = r26;
        if (r8 >= r0) goto L_0x0139;
    L_0x0105:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r16 = r0.get(r8);
        r16 = (android.support.p000v4.view.ViewPager.ItemInfo) r16;
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r29;
        r0 = r0.mCurItem;
        r27 = r0;
        r0 = r26;
        r1 = r27;
        if (r0 < r1) goto L_0x0247;
    L_0x0125:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r29;
        r0 = r0.mCurItem;
        r27 = r0;
        r0 = r26;
        r1 = r27;
        if (r0 != r1) goto L_0x0139;
    L_0x0137:
        r9 = r16;
    L_0x0139:
        if (r9 != 0) goto L_0x014b;
    L_0x013b:
        if (r4 <= 0) goto L_0x014b;
    L_0x013d:
        r0 = r29;
        r0 = r0.mCurItem;
        r26 = r0;
        r0 = r29;
        r1 = r26;
        r9 = r0.addNewItem(r1, r8);
    L_0x014b:
        if (r9 == 0) goto L_0x01cf;
    L_0x014d:
        r13 = 0;
        r17 = r8 + -1;
        if (r17 < 0) goto L_0x024b;
    L_0x0152:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x0164:
        r7 = r29.getClientWidth();
        if (r7 > 0) goto L_0x024f;
    L_0x016a:
        r18 = 0;
    L_0x016c:
        r0 = r29;
        r0 = r0.mCurItem;
        r26 = r0;
        r22 = r26 + -1;
    L_0x0174:
        if (r22 < 0) goto L_0x0182;
    L_0x0176:
        r26 = (r13 > r18 ? 1 : (r13 == r18 ? 0 : -1));
        if (r26 < 0) goto L_0x02c0;
    L_0x017a:
        r0 = r22;
        r1 = r25;
        if (r0 >= r1) goto L_0x02c0;
    L_0x0180:
        if (r16 != 0) goto L_0x0269;
    L_0x0182:
        r14 = r9.widthFactor;
        r17 = r8 + 1;
        r26 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r26 = (r14 > r26 ? 1 : (r14 == r26 ? 0 : -1));
        if (r26 >= 0) goto L_0x01c8;
    L_0x018c:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r26 = r26.size();
        r0 = r17;
        r1 = r26;
        if (r0 >= r1) goto L_0x031e;
    L_0x019c:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x01ae:
        if (r7 > 0) goto L_0x0322;
    L_0x01b0:
        r24 = 0;
    L_0x01b2:
        r0 = r29;
        r0 = r0.mCurItem;
        r26 = r0;
        r22 = r26 + 1;
    L_0x01ba:
        r0 = r22;
        if (r0 >= r4) goto L_0x01c8;
    L_0x01be:
        r26 = (r14 > r24 ? 1 : (r14 == r24 ? 0 : -1));
        if (r26 < 0) goto L_0x0397;
    L_0x01c2:
        r0 = r22;
        if (r0 <= r12) goto L_0x0397;
    L_0x01c6:
        if (r16 != 0) goto L_0x0336;
    L_0x01c8:
        r0 = r29;
        r1 = r20;
        r0.calculatePageOffsets(r9, r8, r1);
    L_0x01cf:
        r0 = r29;
        r0 = r0.mAdapter;
        r27 = r0;
        r0 = r29;
        r0 = r0.mCurItem;
        r28 = r0;
        if (r9 == 0) goto L_0x040f;
    L_0x01dd:
        r0 = r9.object;
        r26 = r0;
    L_0x01e1:
        r0 = r27;
        r1 = r29;
        r2 = r28;
        r3 = r26;
        r0.setPrimaryItem(r1, r2, r3);
        r0 = r29;
        r0 = r0.mAdapter;
        r26 = r0;
        r0 = r26;
        r1 = r29;
        r0.finishUpdate(r1);
        r6 = r29.getChildCount();
        r15 = 0;
    L_0x01fe:
        if (r15 >= r6) goto L_0x0413;
    L_0x0200:
        r0 = r29;
        r5 = r0.getChildAt(r15);
        r19 = r5.getLayoutParams();
        r19 = (android.support.p000v4.view.ViewPager.LayoutParams) r19;
        r0 = r19;
        r0.childIndex = r15;
        r0 = r19;
        r0 = r0.isDecor;
        r26 = r0;
        if (r26 != 0) goto L_0x0244;
    L_0x0218:
        r0 = r19;
        r0 = r0.widthFactor;
        r26 = r0;
        r27 = 0;
        r26 = (r26 > r27 ? 1 : (r26 == r27 ? 0 : -1));
        if (r26 != 0) goto L_0x0244;
    L_0x0224:
        r0 = r29;
        r16 = r0.infoForChild(r5);
        if (r16 == 0) goto L_0x0244;
    L_0x022c:
        r0 = r16;
        r0 = r0.widthFactor;
        r26 = r0;
        r0 = r26;
        r1 = r19;
        r1.widthFactor = r0;
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r26;
        r1 = r19;
        r1.position = r0;
    L_0x0244:
        r15 = r15 + 1;
        goto L_0x01fe;
    L_0x0247:
        r8 = r8 + 1;
        goto L_0x00f7;
    L_0x024b:
        r16 = 0;
        goto L_0x0164;
    L_0x024f:
        r26 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r9.widthFactor;
        r27 = r0;
        r26 = r26 - r27;
        r27 = r29.getPaddingLeft();
        r0 = r27;
        r0 = (float) r0;
        r27 = r0;
        r0 = (float) r7;
        r28 = r0;
        r27 = r27 / r28;
        r18 = r26 + r27;
        goto L_0x016c;
    L_0x0269:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r22;
        r1 = r26;
        if (r0 != r1) goto L_0x02b9;
    L_0x0275:
        r0 = r16;
        r0 = r0.scrolling;
        r26 = r0;
        if (r26 != 0) goto L_0x02b9;
    L_0x027d:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r0.remove(r1);
        r0 = r29;
        r0 = r0.mAdapter;
        r26 = r0;
        r0 = r16;
        r0 = r0.object;
        r27 = r0;
        r0 = r26;
        r1 = r29;
        r2 = r22;
        r3 = r27;
        r0.destroyItem(r1, r2, r3);
        r17 = r17 + -1;
        r8 = r8 + -1;
        if (r17 < 0) goto L_0x02bd;
    L_0x02a7:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x02b9:
        r22 = r22 + -1;
        goto L_0x0174;
    L_0x02bd:
        r16 = 0;
        goto L_0x02b9;
    L_0x02c0:
        if (r16 == 0) goto L_0x02f0;
    L_0x02c2:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r22;
        r1 = r26;
        if (r0 != r1) goto L_0x02f0;
    L_0x02ce:
        r0 = r16;
        r0 = r0.widthFactor;
        r26 = r0;
        r13 = r13 + r26;
        r17 = r17 + -1;
        if (r17 < 0) goto L_0x02ed;
    L_0x02da:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x02ec:
        goto L_0x02b9;
    L_0x02ed:
        r16 = 0;
        goto L_0x02ec;
    L_0x02f0:
        r26 = r17 + 1;
        r0 = r29;
        r1 = r22;
        r2 = r26;
        r16 = r0.addNewItem(r1, r2);
        r0 = r16;
        r0 = r0.widthFactor;
        r26 = r0;
        r13 = r13 + r26;
        r8 = r8 + 1;
        if (r17 < 0) goto L_0x031b;
    L_0x0308:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x031a:
        goto L_0x02b9;
    L_0x031b:
        r16 = 0;
        goto L_0x031a;
    L_0x031e:
        r16 = 0;
        goto L_0x01ae;
    L_0x0322:
        r26 = r29.getPaddingRight();
        r0 = r26;
        r0 = (float) r0;
        r26 = r0;
        r0 = (float) r7;
        r27 = r0;
        r26 = r26 / r27;
        r27 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r24 = r26 + r27;
        goto L_0x01b2;
    L_0x0336:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r22;
        r1 = r26;
        if (r0 != r1) goto L_0x0390;
    L_0x0342:
        r0 = r16;
        r0 = r0.scrolling;
        r26 = r0;
        if (r26 != 0) goto L_0x0390;
    L_0x034a:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r0.remove(r1);
        r0 = r29;
        r0 = r0.mAdapter;
        r26 = r0;
        r0 = r16;
        r0 = r0.object;
        r27 = r0;
        r0 = r26;
        r1 = r29;
        r2 = r22;
        r3 = r27;
        r0.destroyItem(r1, r2, r3);
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r26 = r26.size();
        r0 = r17;
        r1 = r26;
        if (r0 >= r1) goto L_0x0394;
    L_0x037e:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x0390:
        r22 = r22 + 1;
        goto L_0x01ba;
    L_0x0394:
        r16 = 0;
        goto L_0x0390;
    L_0x0397:
        if (r16 == 0) goto L_0x03d5;
    L_0x0399:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r22;
        r1 = r26;
        if (r0 != r1) goto L_0x03d5;
    L_0x03a5:
        r0 = r16;
        r0 = r0.widthFactor;
        r26 = r0;
        r14 = r14 + r26;
        r17 = r17 + 1;
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r26 = r26.size();
        r0 = r17;
        r1 = r26;
        if (r0 >= r1) goto L_0x03d2;
    L_0x03bf:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x03d1:
        goto L_0x0390;
    L_0x03d2:
        r16 = 0;
        goto L_0x03d1;
    L_0x03d5:
        r0 = r29;
        r1 = r22;
        r2 = r17;
        r16 = r0.addNewItem(r1, r2);
        r17 = r17 + 1;
        r0 = r16;
        r0 = r0.widthFactor;
        r26 = r0;
        r14 = r14 + r26;
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r26 = r26.size();
        r0 = r17;
        r1 = r26;
        if (r0 >= r1) goto L_0x040c;
    L_0x03f9:
        r0 = r29;
        r0 = r0.mItems;
        r26 = r0;
        r0 = r26;
        r1 = r17;
        r26 = r0.get(r1);
        r26 = (android.support.p000v4.view.ViewPager.ItemInfo) r26;
        r16 = r26;
    L_0x040b:
        goto L_0x0390;
    L_0x040c:
        r16 = 0;
        goto L_0x040b;
    L_0x040f:
        r26 = 0;
        goto L_0x01e1;
    L_0x0413:
        r29.sortChildDrawingOrder();
        r26 = r29.hasFocus();
        if (r26 == 0) goto L_0x002d;
    L_0x041c:
        r10 = r29.findFocus();
        if (r10 == 0) goto L_0x0472;
    L_0x0422:
        r0 = r29;
        r16 = r0.infoForAnyChild(r10);
    L_0x0428:
        if (r16 == 0) goto L_0x043c;
    L_0x042a:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r29;
        r0 = r0.mCurItem;
        r27 = r0;
        r0 = r26;
        r1 = r27;
        if (r0 == r1) goto L_0x002d;
    L_0x043c:
        r15 = 0;
    L_0x043d:
        r26 = r29.getChildCount();
        r0 = r26;
        if (r15 >= r0) goto L_0x002d;
    L_0x0445:
        r0 = r29;
        r5 = r0.getChildAt(r15);
        r0 = r29;
        r16 = r0.infoForChild(r5);
        if (r16 == 0) goto L_0x046f;
    L_0x0453:
        r0 = r16;
        r0 = r0.position;
        r26 = r0;
        r0 = r29;
        r0 = r0.mCurItem;
        r27 = r0;
        r0 = r26;
        r1 = r27;
        if (r0 != r1) goto L_0x046f;
    L_0x0465:
        r26 = 2;
        r0 = r26;
        r26 = r5.requestFocus(r0);
        if (r26 != 0) goto L_0x002d;
    L_0x046f:
        r15 = r15 + 1;
        goto L_0x043d;
    L_0x0472:
        r16 = 0;
        goto L_0x0428;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.view.ViewPager.populate(int):void");
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mDrawingOrderedChildren.add(getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x005e A:{LOOP_END, LOOP:2: B:18:0x005a->B:20:0x005e} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a8 A:{LOOP_END, LOOP:5: B:33:0x00a4->B:35:0x00a8} */
    private void calculatePageOffsets(android.support.p000v4.view.ViewPager.ItemInfo r15, int r16, android.support.p000v4.view.ViewPager.ItemInfo r17) {
        /*
        r14 = this;
        r12 = r14.mAdapter;
        r1 = r12.getCount();
        r11 = r14.getClientWidth();
        if (r11 <= 0) goto L_0x0058;
    L_0x000c:
        r12 = r14.mPageMargin;
        r12 = (float) r12;
        r13 = (float) r11;
        r6 = r12 / r13;
    L_0x0012:
        if (r17 == 0) goto L_0x00bc;
    L_0x0014:
        r0 = r17;
        r8 = r0.position;
        r12 = r15.position;
        if (r8 >= r12) goto L_0x0072;
    L_0x001c:
        r5 = 0;
        r3 = 0;
        r0 = r17;
        r12 = r0.offset;
        r0 = r17;
        r13 = r0.widthFactor;
        r12 = r12 + r13;
        r7 = r12 + r6;
        r9 = r8 + 1;
    L_0x002b:
        r12 = r15.position;
        if (r9 > r12) goto L_0x00bc;
    L_0x002f:
        r12 = r14.mItems;
        r12 = r12.size();
        if (r5 >= r12) goto L_0x00bc;
    L_0x0037:
        r12 = r14.mItems;
        r3 = r12.get(r5);
        r3 = (android.support.p000v4.view.ViewPager.ItemInfo) r3;
    L_0x003f:
        r12 = r3.position;
        if (r9 <= r12) goto L_0x005a;
    L_0x0043:
        r12 = r14.mItems;
        r12 = r12.size();
        r12 = r12 + -1;
        if (r5 >= r12) goto L_0x005a;
    L_0x004d:
        r5 = r5 + 1;
        r12 = r14.mItems;
        r3 = r12.get(r5);
        r3 = (android.support.p000v4.view.ViewPager.ItemInfo) r3;
        goto L_0x003f;
    L_0x0058:
        r6 = 0;
        goto L_0x0012;
    L_0x005a:
        r12 = r3.position;
        if (r9 >= r12) goto L_0x0069;
    L_0x005e:
        r12 = r14.mAdapter;
        r12 = r12.getPageWidth(r9);
        r12 = r12 + r6;
        r7 = r7 + r12;
        r9 = r9 + 1;
        goto L_0x005a;
    L_0x0069:
        r3.offset = r7;
        r12 = r3.widthFactor;
        r12 = r12 + r6;
        r7 = r7 + r12;
        r9 = r9 + 1;
        goto L_0x002b;
    L_0x0072:
        r12 = r15.position;
        if (r8 <= r12) goto L_0x00bc;
    L_0x0076:
        r12 = r14.mItems;
        r12 = r12.size();
        r5 = r12 + -1;
        r3 = 0;
        r0 = r17;
        r7 = r0.offset;
        r9 = r8 + -1;
    L_0x0085:
        r12 = r15.position;
        if (r9 < r12) goto L_0x00bc;
    L_0x0089:
        if (r5 < 0) goto L_0x00bc;
    L_0x008b:
        r12 = r14.mItems;
        r3 = r12.get(r5);
        r3 = (android.support.p000v4.view.ViewPager.ItemInfo) r3;
    L_0x0093:
        r12 = r3.position;
        if (r9 >= r12) goto L_0x00a4;
    L_0x0097:
        if (r5 <= 0) goto L_0x00a4;
    L_0x0099:
        r5 = r5 + -1;
        r12 = r14.mItems;
        r3 = r12.get(r5);
        r3 = (android.support.p000v4.view.ViewPager.ItemInfo) r3;
        goto L_0x0093;
    L_0x00a4:
        r12 = r3.position;
        if (r9 <= r12) goto L_0x00b3;
    L_0x00a8:
        r12 = r14.mAdapter;
        r12 = r12.getPageWidth(r9);
        r12 = r12 + r6;
        r7 = r7 - r12;
        r9 = r9 + -1;
        goto L_0x00a4;
    L_0x00b3:
        r12 = r3.widthFactor;
        r12 = r12 + r6;
        r7 = r7 - r12;
        r3.offset = r7;
        r9 = r9 + -1;
        goto L_0x0085;
    L_0x00bc:
        r12 = r14.mItems;
        r4 = r12.size();
        r7 = r15.offset;
        r12 = r15.position;
        r9 = r12 + -1;
        r12 = r15.position;
        if (r12 != 0) goto L_0x00fc;
    L_0x00cc:
        r12 = r15.offset;
    L_0x00ce:
        r14.mFirstOffset = r12;
        r12 = r15.position;
        r13 = r1 + -1;
        if (r12 != r13) goto L_0x0100;
    L_0x00d6:
        r12 = r15.offset;
        r13 = r15.widthFactor;
        r12 = r12 + r13;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r12 = r12 - r13;
    L_0x00de:
        r14.mLastOffset = r12;
        r2 = r16 + -1;
    L_0x00e2:
        if (r2 < 0) goto L_0x0115;
    L_0x00e4:
        r12 = r14.mItems;
        r3 = r12.get(r2);
        r3 = (android.support.p000v4.view.ViewPager.ItemInfo) r3;
    L_0x00ec:
        r12 = r3.position;
        if (r9 <= r12) goto L_0x0104;
    L_0x00f0:
        r12 = r14.mAdapter;
        r10 = r9 + -1;
        r12 = r12.getPageWidth(r9);
        r12 = r12 + r6;
        r7 = r7 - r12;
        r9 = r10;
        goto L_0x00ec;
    L_0x00fc:
        r12 = -8388609; // 0xffffffffff7fffff float:-3.4028235E38 double:NaN;
        goto L_0x00ce;
    L_0x0100:
        r12 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        goto L_0x00de;
    L_0x0104:
        r12 = r3.widthFactor;
        r12 = r12 + r6;
        r7 = r7 - r12;
        r3.offset = r7;
        r12 = r3.position;
        if (r12 != 0) goto L_0x0110;
    L_0x010e:
        r14.mFirstOffset = r7;
    L_0x0110:
        r2 = r2 + -1;
        r9 = r9 + -1;
        goto L_0x00e2;
    L_0x0115:
        r12 = r15.offset;
        r13 = r15.widthFactor;
        r12 = r12 + r13;
        r7 = r12 + r6;
        r12 = r15.position;
        r9 = r12 + 1;
        r2 = r16 + 1;
    L_0x0122:
        if (r2 >= r4) goto L_0x0155;
    L_0x0124:
        r12 = r14.mItems;
        r3 = r12.get(r2);
        r3 = (android.support.p000v4.view.ViewPager.ItemInfo) r3;
    L_0x012c:
        r12 = r3.position;
        if (r9 >= r12) goto L_0x013c;
    L_0x0130:
        r12 = r14.mAdapter;
        r10 = r9 + 1;
        r12 = r12.getPageWidth(r9);
        r12 = r12 + r6;
        r7 = r7 + r12;
        r9 = r10;
        goto L_0x012c;
    L_0x013c:
        r12 = r3.position;
        r13 = r1 + -1;
        if (r12 != r13) goto L_0x014a;
    L_0x0142:
        r12 = r3.widthFactor;
        r12 = r12 + r7;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r12 = r12 - r13;
        r14.mLastOffset = r12;
    L_0x014a:
        r3.offset = r7;
        r12 = r3.widthFactor;
        r12 = r12 + r6;
        r7 = r7 + r12;
        r2 = r2 + 1;
        r9 = r9 + 1;
        goto L_0x0122;
    L_0x0155:
        r12 = 0;
        r14.mNeedCalculatePageOffsets = r12;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.view.ViewPager.calculatePageOffsets(android.support.v4.view.ViewPager$ItemInfo, int, android.support.v4.view.ViewPager$ItemInfo):void");
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                setCurrentItemInternal(ss.position, false, true);
                return;
            }
            this.mRestoredCurItem = ss.position;
            this.mRestoredAdapterState = ss.adapterState;
            this.mRestoredClassLoader = ss.loader;
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= ViewPager.isDecorView(child);
        if (!this.mInLayout) {
            super.addView(child, index, params);
        } else if (lp == null || !lp.isDecor) {
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    private static boolean isDecorView(@NonNull View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo infoForAnyChild(View child) {
        while (true) {
            View parent = child.getParent();
            if (parent == this) {
                return infoForChild(child);
            }
            if (parent != null && (parent instanceof View)) {
                child = parent;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        View child;
        LayoutParams lp;
        setMeasuredDimension(ViewPager.getDefaultSize(0, widthMeasureSpec), ViewPager.getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int childWidthSize = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int childHeightSize = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int size = getChildCount();
        for (i = 0; i < size; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = Integer.MIN_VALUE;
                    int heightMode = Integer.MIN_VALUE;
                    boolean consumeVertical = vgrav == 48 || vgrav == 80;
                    boolean consumeHorizontal = hgrav == 3 || hgrav == 5;
                    if (consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        heightMode = 1073741824;
                    }
                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != -2) {
                        widthMode = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != -2) {
                        heightMode = 1073741824;
                        if (lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        size = getChildCount();
        for (i = 0; i < size; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidthSize) * lp.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ItemInfo ii = infoForPosition(this.mCurItem);
            int scrollPos = (int) (((float) ((width - getPaddingLeft()) - getPaddingRight())) * (ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0f));
            if (scrollPos != getScrollX()) {
                completeScroll(false);
                scrollTo(scrollPos, getScrollY());
            }
        } else if (this.mScroller.isFinished()) {
            scrollTo((int) (((float) (((width - getPaddingLeft()) - getPaddingRight()) + margin)) * (((float) getScrollX()) / ((float) (((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldMargin)))), getScrollY());
        } else {
            this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        View child;
        LayoutParams lp;
        int childLeft;
        int childTop;
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int decorCount = 0;
        for (i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int vgrav = lp.gravity & 112;
                    switch (lp.gravity & 7) {
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 3:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case 5:
                            childLeft = (width - paddingRight) - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        default:
                            childLeft = paddingLeft;
                            break;
                    }
                    switch (vgrav) {
                        case 16:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case 48:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case 80:
                            childTop = (height - paddingBottom) - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                        default:
                            childTop = paddingTop;
                            break;
                    }
                    childLeft += scrollX;
                    child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
                    decorCount++;
                }
            }
        }
        int childWidth = (width - paddingLeft) - paddingRight;
        for (i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null) {
                        childLeft = paddingLeft + ((int) (((float) childWidth) * ii.offset));
                        childTop = paddingTop;
                        if (lp.needsMeasure) {
                            lp.needsMeasure = false;
                            child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidth) * lp.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec((height - paddingTop) - paddingBottom, 1073741824));
                        }
                        child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if (this.mFirstLayout) {
            scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (!(oldX == x && oldY == y)) {
            scrollTo(x, y);
            if (!pageScrolled(x)) {
                this.mScroller.abortAnimation();
                scrollTo(0, y);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() != 0) {
            ItemInfo ii = infoForCurrentScrollPosition();
            int width = getClientWidth();
            int widthWithMargin = width + this.mPageMargin;
            float marginOffset = ((float) this.mPageMargin) / ((float) width);
            int currentPage = ii.position;
            float pageOffset = ((((float) xpos) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int) (((float) widthWithMargin) * pageOffset);
            this.mCalledSuper = false;
            onPageScrolled(currentPage, pageOffset, offsetPixels);
            if (this.mCalledSuper) {
                return true;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        } else if (this.mFirstLayout) {
            return false;
        } else {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
    }

    /* Access modifiers changed, original: protected */
    @CallSuper
    public void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int childCount;
        int i;
        View child;
        if (this.mDecorChildCount > 0) {
            scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            childCount = getChildCount();
            for (i = 0; i < childCount; i++) {
                child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int childLeft;
                    switch (lp.gravity & 7) {
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 3:
                            childLeft = paddingLeft;
                            paddingLeft += child.getWidth();
                            break;
                        case 5:
                            childLeft = (width - paddingRight) - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        default:
                            childLeft = paddingLeft;
                            break;
                    }
                    int childOffset = (childLeft + scrollX) - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }
        dispatchOnPageScrolled(position, offset, offsetPixels);
        if (this.mPageTransformer != null) {
            scrollX = getScrollX();
            childCount = getChildCount();
            for (i = 0; i < childCount; i++) {
                child = getChildAt(i);
                if (!((LayoutParams) child.getLayoutParams()).isDecor) {
                    this.mPageTransformer.transformPage(child, ((float) (child.getLeft() - scrollX)) / ((float) getClientWidth()));
                }
            }
        }
        this.mCalledSuper = true;
    }

    private void dispatchOnPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mOnPageChangeListeners != null) {
            int z = this.mOnPageChangeListeners.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrolled(position, offset, offsetPixels);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
    }

    private void dispatchOnPageSelected(int position) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(position);
        }
        if (this.mOnPageChangeListeners != null) {
            int z = this.mOnPageChangeListeners.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(position);
        }
    }

    private void dispatchOnScrollStateChanged(int state) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        if (this.mOnPageChangeListeners != null) {
            int z = this.mOnPageChangeListeners.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate;
        boolean wasScrolling = true;
        if (this.mScrollState == 2) {
            needPopulate = true;
        } else {
            needPopulate = false;
        }
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            if (this.mScroller.isFinished()) {
                wasScrolling = false;
            }
            if (wasScrolling) {
                this.mScroller.abortAnimation();
                int oldX = getScrollX();
                int oldY = getScrollY();
                int x = this.mScroller.getCurrX();
                int y = this.mScroller.getCurrY();
                if (!(oldX == x && oldY == y)) {
                    scrollTo(x, y);
                    if (x != oldX) {
                        pageScrolled(x);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }
        if (!needPopulate) {
            return;
        }
        if (postEvents) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return (x < ((float) this.mGutterSize) && dx > 0.0f) || (x > ((float) (getWidth() - this.mGutterSize)) && dx < 0.0f);
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewCompat.setLayerType(getChildAt(i), enable ? this.mPageTransformerLayerType : 0, null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if (action == 3 || action == 1) {
            resetTouch();
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }
        switch (action) {
            case 0:
                float x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                x = ev.getY();
                this.mInitialMotionY = x;
                this.mLastMotionY = x;
                this.mActivePointerId = ev.getPointerId(0);
                this.mIsUnableToDrag = false;
                this.mIsScrollStarted = true;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    populate();
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    break;
                }
                completeScroll(false);
                this.mIsBeingDragged = false;
                break;
            case 2:
                int activePointerId = this.mActivePointerId;
                if (activePointerId != -1) {
                    int pointerIndex = ev.findPointerIndex(activePointerId);
                    float x2 = ev.getX(pointerIndex);
                    float dx = x2 - this.mLastMotionX;
                    float xDiff = Math.abs(dx);
                    float y = ev.getY(pointerIndex);
                    float yDiff = Math.abs(y - this.mInitialMotionY);
                    if (!(dx == 0.0f || isGutterDrag(this.mLastMotionX, dx))) {
                        if (canScroll(this, false, (int) dx, (int) x2, (int) y)) {
                            this.mLastMotionX = x2;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                    }
                    if (xDiff > ((float) this.mTouchSlop) && 0.5f * xDiff > yDiff) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                        this.mLastMotionX = dx > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                        this.mLastMotionY = y;
                        setScrollingCacheEnabled(true);
                    } else if (yDiff > ((float) this.mTouchSlop)) {
                        this.mIsUnableToDrag = true;
                    }
                    if (this.mIsBeingDragged && performDrag(x2)) {
                        ViewCompat.postInvalidateOnAnimation(this);
                        break;
                    }
                }
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mFakeDragging) {
            return true;
        }
        if (ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return false;
        }
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        boolean needsInvalidate = false;
        float x;
        switch (ev.getAction() & 255) {
            case 0:
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                x = ev.getY();
                this.mInitialMotionY = x;
                this.mLastMotionY = x;
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                    this.mPopulatePending = true;
                    int width = getClientWidth();
                    int scrollX = getScrollX();
                    ItemInfo ii = infoForCurrentScrollPosition();
                    float marginOffset = ((float) this.mPageMargin) / ((float) width);
                    setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset), initialVelocity, (int) (ev.getX(ev.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, initialVelocity);
                    needsInvalidate = resetTouch();
                    break;
                }
                break;
            case 2:
                if (!this.mIsBeingDragged) {
                    int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex == -1) {
                        needsInvalidate = resetTouch();
                        break;
                    }
                    float x2 = ev.getX(pointerIndex);
                    float xDiff = Math.abs(x2 - this.mLastMotionX);
                    float y = ev.getY(pointerIndex);
                    float yDiff = Math.abs(y - this.mLastMotionY);
                    if (xDiff > ((float) this.mTouchSlop) && xDiff > yDiff) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        if (x2 - this.mInitialMotionX > 0.0f) {
                            x = this.mInitialMotionX + ((float) this.mTouchSlop);
                        } else {
                            x = this.mInitialMotionX - ((float) this.mTouchSlop);
                        }
                        this.mLastMotionX = x;
                        this.mLastMotionY = y;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    needsInvalidate = false | performDrag(ev.getX(ev.findPointerIndex(this.mActivePointerId)));
                    break;
                }
                break;
            case 3:
                if (this.mIsBeingDragged) {
                    scrollToItem(this.mCurItem, true, 0, false);
                    needsInvalidate = resetTouch();
                    break;
                }
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = ev.getX(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                onSecondaryPointerUp(ev);
                this.mLastMotionX = ev.getX(ev.findPointerIndex(this.mActivePointerId));
                break;
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        endDrag();
        return this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float scrollX = ((float) getScrollX()) + deltaX;
        int width = getClientWidth();
        float leftBound = ((float) width) * this.mFirstOffset;
        float rightBound = ((float) width) * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * ((float) width);
        }
        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * ((float) width);
        }
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                needsInvalidate = this.mLeftEdge.onPull(Math.abs(leftBound - scrollX) / ((float) width));
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                needsInvalidate = this.mRightEdge.onPull(Math.abs(scrollX - rightBound) / ((float) width));
            }
            scrollX = rightBound;
        }
        this.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        float scrollOffset;
        float marginOffset = 0.0f;
        int width = getClientWidth();
        if (width > 0) {
            scrollOffset = ((float) getScrollX()) / ((float) width);
        } else {
            scrollOffset = 0.0f;
        }
        if (width > 0) {
            marginOffset = ((float) this.mPageMargin) / ((float) width);
        }
        int lastPos = -1;
        float lastOffset = 0.0f;
        float lastWidth = 0.0f;
        boolean first = true;
        ItemInfo lastItem = null;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (!(first || ii.position == lastPos + 1)) {
                ii = this.mTempItem;
                ii.offset = (lastOffset + lastWidth) + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;
            float leftBound = offset;
            float rightBound = (ii.widthFactor + offset) + marginOffset;
            if (!first && scrollOffset < leftBound) {
                return lastItem;
            }
            if (scrollOffset < rightBound || i == this.mItems.size() - 1) {
                return ii;
            }
            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
            i++;
        }
        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) {
            targetPage = currentPage + ((int) (pageOffset + (currentPage >= this.mCurItem ? 0.4f : 0.6f)));
        } else {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        }
        if (this.mItems.size() <= 0) {
            return targetPage;
        }
        return Math.max(((ItemInfo) this.mItems.get(0)).position, Math.min(targetPage, ((ItemInfo) this.mItems.get(this.mItems.size() - 1)).position));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = getOverScrollMode();
        if (overScrollMode == 0 || (overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
            int restoreCount;
            int height;
            int width;
            if (!this.mLeftEdge.isFinished()) {
                restoreCount = canvas.save();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                width = getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
                this.mLeftEdge.setSize(height, width);
                needsInvalidate = false | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mRightEdge.isFinished()) {
                restoreCount = canvas.save();
                width = getWidth();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) width));
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float marginOffset = ((float) this.mPageMargin) / ((float) width);
            int itemIndex = 0;
            ItemInfo ii = (ItemInfo) this.mItems.get(0);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ItemInfo) this.mItems.get(itemCount - 1)).position;
            int pos = firstPos;
            while (pos < lastPos) {
                float drawAt;
                while (pos > ii.position && itemIndex < itemCount) {
                    itemIndex++;
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                }
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * ((float) width);
                    offset = (ii.offset + ii.widthFactor) + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * ((float) width);
                    offset += widthFactor + marginOffset;
                }
                if (((float) this.mPageMargin) + drawAt > ((float) scrollX)) {
                    this.mMarginDrawable.setBounds(Math.round(drawAt), this.mTopPageBounds, Math.round(((float) this.mPageMargin) + drawAt), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (drawAt <= ((float) (scrollX + width))) {
                    pos++;
                } else {
                    return;
                }
            }
        }
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
        this.mFakeDragBeginTime = time;
        return true;
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                this.mPopulatePending = true;
                int width = getClientWidth();
                int scrollX = getScrollX();
                ItemInfo ii = infoForCurrentScrollPosition();
                setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / ii.widthFactor, initialVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, initialVelocity);
            }
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else if (this.mAdapter != null) {
            this.mLastMotionX += xOffset;
            float scrollX = ((float) getScrollX()) - xOffset;
            int width = getClientWidth();
            float leftBound = ((float) width) * this.mFirstOffset;
            float rightBound = ((float) width) * this.mLastOffset;
            ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
            ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * ((float) width);
            }
            if (lastItem.position != this.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * ((float) width);
            }
            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }
            this.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            pageScrolled((int) scrollX);
            MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    public boolean canScrollHorizontally(int direction) {
        boolean z = true;
        if (this.mAdapter == null) {
            return false;
        }
        int width = getClientWidth();
        int scrollX = getScrollX();
        if (direction < 0) {
            if (scrollX <= ((int) (((float) width) * this.mFirstOffset))) {
                z = false;
            }
            return z;
        } else if (direction <= 0) {
            return false;
        } else {
            if (scrollX >= ((int) (((float) width) * this.mLastOffset))) {
                z = false;
            }
            return z;
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
                return arrowScroll(17);
            case 22:
                return arrowScroll(66);
            case 61:
                if (VERSION.SDK_INT < 11) {
                    return false;
                }
                if (KeyEventCompat.hasNoModifiers(event)) {
                    return arrowScroll(2);
                }
                if (KeyEventCompat.hasModifiers(event, 1)) {
                    return arrowScroll(1);
                }
                return false;
            default:
                return false;
        }
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            boolean isChild = false;
            for (ViewPager parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent2 = currentFocused.getParent(); parent2 instanceof ViewGroup; parent2 = parent2.getParent()) {
                    sb.append(" => ").append(parent2.getClass().getSimpleName());
                }
                Log.e(TAG, "arrowScroll tried to find focus based on non-child current focused view " + sb.toString());
                currentFocused = null;
            }
        }
        boolean handled = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction == 17 || direction == 1) {
                handled = pageLeft();
            } else if (direction == 66 || direction == 2) {
                handled = pageRight();
            }
        } else if (direction == 17) {
            handled = (currentFocused == null || getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left < getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left) ? nextFocused.requestFocus() : pageLeft();
        } else if (direction == 66) {
            handled = (currentFocused == null || getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left > getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left) ? nextFocused.requestFocus() : pageRight();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();
            ViewGroup parent = child.getParent();
            while ((parent instanceof ViewGroup) && parent != this) {
                ViewGroup group = parent;
                outRect.left += group.getLeft();
                outRect.right += group.getRight();
                outRect.top += group.getTop();
                outRect.bottom += group.getBottom();
                parent = group.getParent();
            }
        }
        return outRect;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && focusableCount != views.size()) || !isFocusable()) {
            return;
        }
        if (((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && views != null) {
            views.add(this);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}